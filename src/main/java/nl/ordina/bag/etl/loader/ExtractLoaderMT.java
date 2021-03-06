/**
 * Copyright 2013 Ordina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.ordina.bag.etl.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import nl.kadaster.schemas.bag_verstrekkingen.extract_levering.v20090901.BAGExtractLevering;
import nl.kadaster.schemas.imbag.lvc.v20090901.Ligplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Nummeraanduiding;
import nl.kadaster.schemas.imbag.lvc.v20090901.OpenbareRuimte;
import nl.kadaster.schemas.imbag.lvc.v20090901.Pand;
import nl.kadaster.schemas.imbag.lvc.v20090901.Standplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject;
import nl.kadaster.schemas.imbag.lvc.v20090901.Woonplaats;
import nl.ordina.bag.etl.Constants.BAGObjectType;
import nl.ordina.bag.etl.dao.BAGDAO;
import nl.ordina.bag.etl.dao.DAOException;
import nl.ordina.bag.etl.model.BAGObjectFactory;
import nl.ordina.bag.etl.processor.ProcessingException;
import nl.ordina.bag.etl.processor.ProcessorException;
import nl.ordina.bag.etl.util.BeanLocator;
import nl.ordina.bag.etl.util.ZipStreamReader;
import nl.ordina.bag.etl.validation.BAGExtractLeveringValidator;
import nl.ordina.bag.etl.xml.BAGGeometrieHandler;
import nl.ordina.bag.etl.xml.ExtractParser;
import nl.ordina.bag.etl.xml.SimpleExtractParser;
import nl.ordina.bag.etl.xml.HandlerException;
import nl.ordina.bag.etl.xml.ParserException;

public class ExtractLoaderMT extends ExtractLoader
{
	public class ExceptionListener
	{
		private Exception exception;

		public boolean exception()
		{
			return exception != null;
		}
		public void onException(Exception exception)
		{
			synchronized (this)
			{
				if (this.exception == null)
					this.exception = exception;
			}
		}
		public Exception getException()
		{
			return exception;
		}
	}

	protected ExceptionListener exceptionListener = new ExceptionListener();
	protected ExecutorService executorService;
	protected Integer maxThreads;
	protected Integer processorsScaleFactor;
	protected Integer queueScaleFactor;

	public void init()
	{
		if (maxThreads == null || maxThreads <= 0)
		{
			maxThreads = Runtime.getRuntime().availableProcessors() * processorsScaleFactor;
			logger.info("Using " + maxThreads + " threads");
		}
		if (processorsScaleFactor == null || processorsScaleFactor <= 0)
		{
			processorsScaleFactor = 1;
			logger.info("Using processors scale factor " + processorsScaleFactor);
		}
		if (queueScaleFactor == null || queueScaleFactor <= 0)
		{
			queueScaleFactor = 1;
			logger.info("Using queue scale factor " + queueScaleFactor);
		}
	}

	protected void processBAGExtractFile(BAGExtractLevering levering, ZipFile zipFile) throws ProcessorException
	{
		//final Map<String,String> files = getFiles(levering);
		final Map<String,String> files = getFiles(zipFile);
		BAGObjectType[] objectTypes = new BAGObjectType[]{BAGObjectType.WOONPLAATS,BAGObjectType.OPENBARE_RUIMTE,BAGObjectType.NUMMERAANDUIDING,BAGObjectType.PAND,BAGObjectType.VERBLIJFSOBJECT,BAGObjectType.LIGPLAATS,BAGObjectType.STANDPLAATS};
		for (final BAGObjectType objectType : objectTypes)
		{
			//executorService = Executors.newFixedThreadPool(maxThreads);
			executorService = new ThreadPoolExecutor(maxThreads - 1,maxThreads - 1,1,TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(maxThreads * queueScaleFactor,true),new ThreadPoolExecutor.CallerRunsPolicy());
			try
			{
				String filename = files.get(objectType.getCode());
				ZipEntry entry = zipFile.getEntry(filename);
				ZipStreamReader zipStreamReader = new ZipStreamReader()
				{
					@Override
					public void handle(String filename, InputStream stream) throws IOException
					{
						if (filename.matches("\\d{4}(WPL|OPR|NUM|PND|VBO|LIG|STA)\\d{8}-\\d{6}\\.xml"))
						{
							logger.info("Processing file " + filename + " started");
							processXML(stream);
							logger.info("Processing file " + filename + " finished");
						}
						else
							logger.info("Skipping file " + filename);
					}
				};
				logger.info("Processing file " + filename + " started");
				zipStreamReader.read(zipFile.getInputStream(entry));
				logger.info("Processing file " + filename + " finished");

				try
				{
					executorService.shutdown();
					executorService.awaitTermination(Long.MAX_VALUE,TimeUnit.DAYS);
				}
				catch (InterruptedException e)
				{
					logger.trace("",e);
				}
			}
			catch (ParserException | DAOException | IOException e)
			{
				try
				{
					executorService.shutdownNow();
					executorService.awaitTermination(Long.MAX_VALUE,TimeUnit.DAYS);
				}
				catch (InterruptedException ignore)
				{
					logger.trace("",ignore);
				}
				throw new ProcessingException(e);
			}
		}
	}
	
	protected void processXML(InputStream stream)
	{
		ExtractParser reader = new SimpleExtractParser()
		{
			@Override
			public void handle(final Woonplaats woonplaats) throws HandlerException
			{
				if (exceptionListener.exception())
					throw new HandlerException(exceptionListener.getException());
  			executorService.execute(
	  			new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								logger.debug("Inserting woonplaats " + woonplaats.getIdentificatie());
								bagDAO.insert(bagObjectFactory.getWoonplaats(woonplaats));
							}
							catch (Exception e)
							{
								exceptionListener.onException(e);
							}
						}
					}
	  		);
			}
			
			@Override
			public void handle(final OpenbareRuimte openbareRuimte) throws HandlerException
			{
				if (exceptionListener.exception())
					throw new HandlerException(exceptionListener.getException());
  			executorService.execute(
	  			new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								logger.debug("Inserting openbare ruimte " + openbareRuimte.getIdentificatie());
								bagDAO.insert(bagObjectFactory.getOpenbareRuimte(openbareRuimte));
							}
							catch (Exception e)
							{
								exceptionListener.onException(e);
							}
						}
					}
	  		);
			}
			
			@Override
			public void handle(final Nummeraanduiding nummeraanduiding) throws HandlerException
			{
				if (exceptionListener.exception())
					throw new HandlerException(exceptionListener.getException());
  			executorService.execute(
	  			new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								logger.debug("Inserting nummeraanduiding " + nummeraanduiding.getIdentificatie());
								bagDAO.insert(bagObjectFactory.getNummeraanduiding(nummeraanduiding));
							}
							catch (Exception e)
							{
								exceptionListener.onException(e);
							}
						}
					}
	  		);
			}

			@Override
			public void handle(final Pand pand) throws HandlerException
			{
				if (exceptionListener.exception())
					throw new HandlerException(exceptionListener.getException());
  			executorService.execute(
	  			new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								logger.debug("Inserting pand " + pand.getIdentificatie());
								bagDAO.insert(bagObjectFactory.getPand(pand));
							}
							catch (Exception e)
							{
								exceptionListener.onException(e);
							}
						}
					}
	  		);
			}

			@Override
			public void handle(final Verblijfsobject verblijfsobject) throws HandlerException
			{
				if (exceptionListener.exception())
					throw new HandlerException(exceptionListener.getException());
  			executorService.execute(
	  			new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								logger.debug("Inserting verblijfsobject " + verblijfsobject.getIdentificatie());
								bagDAO.insert(bagObjectFactory.getVerblijfsobject(verblijfsobject));
							}
							catch (Exception e)
							{
								exceptionListener.onException(e);
							}
						}
					}
	  		);
			}
			
			@Override
			public void handle(final Ligplaats ligplaats) throws HandlerException
			{
				if (exceptionListener.exception())
					throw new HandlerException(exceptionListener.getException());
  			executorService.execute(
	  			new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								logger.debug("Inserting ligplaats " + ligplaats.getIdentificatie());
								bagDAO.insert(bagObjectFactory.getLigplaats(ligplaats));
							}
							catch (Exception e)
							{
								exceptionListener.onException(e);
							}
						}
					}
	  		);
			}
			
			@Override
			public void handle(final Standplaats standplaats) throws HandlerException
			{
				if (exceptionListener.exception())
					throw new HandlerException(exceptionListener.getException());
  			executorService.execute(
	  			new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								logger.debug("Inserting standplaats " + standplaats.getIdentificatie());
								bagDAO.insert(bagObjectFactory.getStandplaats(standplaats));
							}
							catch (Exception e)
							{
								exceptionListener.onException(e);
							}
						}
					}
	  		);
			}
			
		};
		reader.parse(stream);
	}

	public void setMaxThreads(int maxThreads)
	{
		this.maxThreads = maxThreads;
	}
	
	public void setProcessorsScaleFactor(Integer processorsScaleFactor)
	{
		this.processorsScaleFactor = processorsScaleFactor;
	}
	
	public void setQueueScaleFactor(Integer queueScaleFactor)
	{
		this.queueScaleFactor = queueScaleFactor;
	}
	
	public static void main(String[] args) throws Exception
	{
		BeanLocator beanLocator = BeanLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml");
		ExtractLoaderMT loader = new ExtractLoaderMT();
		loader.setMaxThreads(8);
		loader.setBagDAO((BAGDAO)beanLocator.get("bagDAO"));
		loader.setBagObjectFactory(new BAGObjectFactory(new BAGGeometrieHandler()));
		loader.setBagExtractLeveringValidator(new BAGExtractLeveringValidator("9990000000","DNLDLXEE02","NEDERLAND","LEVENSCYCLUS","XML","EENMALIG_EXTRACT","02"));
		loader.execute(new File("i:/BAGExtract/DNLDLXEE02-9990000000-999000006-01042011.zip"));
		System.exit(0);
	}
}
