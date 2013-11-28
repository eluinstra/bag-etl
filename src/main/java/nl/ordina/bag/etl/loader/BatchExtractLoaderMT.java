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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
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
import nl.ordina.bag.etl.xml.BatchExtractParser;
import nl.ordina.bag.etl.xml.HandlerException;
import nl.ordina.bag.etl.xml.ParserException;

public class BatchExtractLoaderMT extends ExtractLoaderMT
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
		BatchExtractParser reader = new BatchExtractParser()
		{
			@Override
			public void handleWoonplaatsen(final List<Woonplaats> woonplaatsen) throws HandlerException
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
								if (logger.isDebugEnabled())
									for (Woonplaats woonplaats : woonplaatsen)
										logger.debug("Inserting woonplaats " + woonplaats.getIdentificatie());
								List<nl.ordina.bag.etl.model.Woonplaats> batch = new ArrayList<>();
								for (Woonplaats woonplaats : woonplaatsen)
									batch.add(bagObjectFactory.getWoonplaats(woonplaats));
								bagDAO.insertWoonplaatsen(batch);
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
			public void handleOpenbareRuimten(final List<OpenbareRuimte> openbareRuimten) throws HandlerException
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
								if (logger.isDebugEnabled())
									for (OpenbareRuimte openbareRuimte : openbareRuimten)
										logger.debug("Inserting openbare ruimte " + openbareRuimte.getIdentificatie());
								List<nl.ordina.bag.etl.model.OpenbareRuimte> batch = new ArrayList<>();
								for (OpenbareRuimte openbareRuimte : openbareRuimten)
									batch.add(bagObjectFactory.getOpenbareRuimte(openbareRuimte));
								bagDAO.insertOpenbareRuimten(batch);
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
			public void handleNummeraanduidingen(final List<Nummeraanduiding> nummeraanduidingen) throws HandlerException
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
								if (logger.isDebugEnabled())
									for (Nummeraanduiding nummeraanduiding : nummeraanduidingen)
										logger.debug("Inserting nummeraanduiding " + nummeraanduiding.getIdentificatie());
								List<nl.ordina.bag.etl.model.Nummeraanduiding> batch = new ArrayList<>();
								for (Nummeraanduiding nummeraanduiding : nummeraanduidingen)
									batch.add(bagObjectFactory.getNummeraanduiding(nummeraanduiding));
								bagDAO.insertNummeraanduidingen(batch);
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
			public void handlePanden(final List<Pand> panden) throws HandlerException
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
								if (logger.isDebugEnabled())
									for (Pand pand : panden)
										logger.debug("Inserting pand " + pand.getIdentificatie());
								List<nl.ordina.bag.etl.model.Pand> batch = new ArrayList<>();
								for (Pand pand : panden)
									batch.add(bagObjectFactory.getPand(pand));
								bagDAO.insertPanden(batch);
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
			public void handleVerblijfsobjecten(final List<Verblijfsobject> verblijfsobjecten) throws HandlerException
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
								if (logger.isDebugEnabled())
									for (Verblijfsobject verblijfsobject : verblijfsobjecten)
										logger.debug("Inserting verblijfsobject " + verblijfsobject.getIdentificatie());
								List<nl.ordina.bag.etl.model.Verblijfsobject> batch = new ArrayList<>();
								for (Verblijfsobject verblijfsobject : verblijfsobjecten)
									batch.add(bagObjectFactory.getVerblijfsobject(verblijfsobject));
								bagDAO.insertVerblijfsobjecten(batch);
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
			public void handleLigplaatsen(final List<Ligplaats> ligplaatsen) throws HandlerException
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
								if (logger.isDebugEnabled())
									for (Ligplaats ligplaats : ligplaatsen)
										logger.debug("Inserting ligplaats " + ligplaats.getIdentificatie());
								List<nl.ordina.bag.etl.model.Ligplaats> batch = new ArrayList<>();
								for (Ligplaats ligplaats : ligplaatsen)
									batch.add(bagObjectFactory.getLigplaats(ligplaats));
								bagDAO.insertLigplaatsen(batch);
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
			public void handleStandplaatsen(final List<Standplaats> standplaatsen) throws HandlerException
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
								if (logger.isDebugEnabled())
									for (Standplaats standplaats : standplaatsen)
										logger.debug("Inserting standplaats " + standplaats.getIdentificatie());
								List<nl.ordina.bag.etl.model.Standplaats> batch = new ArrayList<>();
								for (Standplaats standplaats : standplaatsen)
									batch.add(bagObjectFactory.getStandplaats(standplaats));
								bagDAO.insertStandplaatsen(batch);
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

	public static void main(String[] args) throws Exception
	{
		BeanLocator beanLocator = BeanLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml");
		BatchExtractLoaderMT loader = new BatchExtractLoaderMT();
		loader.setMaxThreads(8);
		loader.setBagDAO((BAGDAO)beanLocator.get("bagDAO"));
		loader.setBagObjectFactory(new BAGObjectFactory(new BAGGeometrieHandler()));
		loader.setBagExtractLeveringValidator(new BAGExtractLeveringValidator("9990000000","DNLDLXEE02","NEDERLAND","LEVENSCYCLUS","XML","EENMALIG_EXTRACT","02"));
		loader.execute(new File("i:/BAGExtract/DNLDLXEE02-9990000000-999000006-01042011.zip"));
		System.exit(0);
	}
}
