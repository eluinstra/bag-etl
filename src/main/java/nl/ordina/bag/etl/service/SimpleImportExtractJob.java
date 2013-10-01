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
package nl.ordina.bag.etl.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import nl.kadaster.schemas.imbag.lvc.v20090901.Ligplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Nummeraanduiding;
import nl.kadaster.schemas.imbag.lvc.v20090901.OpenbareRuimte;
import nl.kadaster.schemas.imbag.lvc.v20090901.Pand;
import nl.kadaster.schemas.imbag.lvc.v20090901.Standplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject;
import nl.kadaster.schemas.imbag.lvc.v20090901.Woonplaats;
import nl.ordina.bag.etl.Constants.BAGObjectType;
import nl.ordina.bag.etl.dao.BAGDAO;
import nl.ordina.bag.etl.model.BAGObjectFactory;
import nl.ordina.bag.etl.util.ServiceLocator;
import nl.ordina.bag.etl.util.ZipStreamReader;
import nl.ordina.bag.etl.xml.BAGGeometrieHandler;
import nl.ordina.bag.etl.xml.ExtractParser;
import nl.ordina.bag.etl.xml.HandlerException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleImportExtractJob
{
	protected transient Log logger = LogFactory.getLog(this.getClass());
	protected BAGDAO bagDAO;
	private BAGObjectFactory bagObjectFactory;
	protected long skipObjects;

	public void execute(InputStream extractFile)
	{
		try
		{
			logger.info("Import Extract Job started");
			processBAGExtractFile(extractFile);
		}
		catch (Exception e)
		{
			logger.error("",e);
		}
		finally
		{
			logger.info("Import Extract Job ended");
		}
	}

	protected void processBAGExtractFile(InputStream stream) throws FileNotFoundException, IOException
	{
		ZipStreamReader zipStreamReader = new ZipStreamReader()
		{
			@Override
			public void handle(String filename, InputStream stream) throws IOException
			{
				if (filename.matches("9999(WPL|OPR|NUM|PND|VBO|LIG|STA)\\d{8}\\.zip"))
				{
					BAGObjectType objectType = BAGObjectType.getBAGObjectTypeByFilename(filename);
					skipObjects = bagDAO.getCount(objectType);
					if (skipObjects > 0)
						logger.info("Skipping " + skipObjects + " object" + (skipObjects > 1 ? "en" : "") + " (" + objectType.toString() + ")");
					logger.info("Processing file: " + filename + " started");
					read(stream);
					logger.info("Processing file: " + filename + " finished");
				}
				else if (filename.matches("9999(WPL|OPR|NUM|PND|VBO|LIG|STA)\\d{8}-\\d{6}\\.xml"))
				{
					logger.info("Processing file: " + filename + " started");
					//parseFile(stream);
					byte[] b = IOUtils.toByteArray(stream);
					ProcessXML(new ByteArrayInputStream(b));
					logger.info("Processing file: " + filename + " finished");
				}
				else
					logger.info("Skipping file " + filename);
			}
		};
		zipStreamReader.read(stream);
	}
	
	protected void ProcessXML(InputStream stream)
	{
		ExtractParser reader = new ExtractParser()
		{
			@Override
			public void handle(Woonplaats woonplaats) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Processing woonplaats: " + woonplaats.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getWoonplaats(woonplaats));
				}
				else
				{
					logger.trace("Skipping woonplaats: " + woonplaats.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getWoonplaats(woonplaats)))
							throw new HandlerException("Could not find woonplaats: " + woonplaats.getIdentificatie());
				}
			}
			
			@Override
			public void handle(OpenbareRuimte openbareRuimte) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Processing openbare ruimte: " + openbareRuimte.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getOpenbareRuimte(openbareRuimte));
				}
				else
				{
					logger.trace("Skipping openbare ruimte: " + openbareRuimte.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getOpenbareRuimte(openbareRuimte)))
							throw new HandlerException("Could not find openbare ruimte: " + openbareRuimte.getIdentificatie());
				}
			}
			
			@Override
			public void handle(Nummeraanduiding nummeraanduiding) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Processing nummeraanduiding: " + nummeraanduiding.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getNummeraanduiding(nummeraanduiding));
				}
				else
				{
					logger.trace("Skipping nummeraanduiding: " + nummeraanduiding.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getNummeraanduiding(nummeraanduiding)))
							throw new HandlerException("Could not find nummeraanduiding: " + nummeraanduiding.getIdentificatie());
				}
			}

			@Override
			public void handle(Pand pand) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Processing pand: " + pand.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getPand(pand));
				}
				else
				{
					logger.trace("Skipping pand: " + pand.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getPand(pand)))
							throw new HandlerException("Could not find pand: " + pand.getIdentificatie());
				}
			}

			@Override
			public void handle(Verblijfsobject verblijfsobject) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Processing verblijfsobject: " + verblijfsobject.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getVerblijfsobject(verblijfsobject));
				}
				else
				{
					logger.trace("Skipping verblijfsobject: " + verblijfsobject.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getVerblijfsobject(verblijfsobject)))
							throw new HandlerException("Could not find verblijfsobject: " + verblijfsobject.getIdentificatie());
				}
			}
			
			@Override
			public void handle(Ligplaats ligplaats) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Processing ligplaats: " + ligplaats.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getLigplaats(ligplaats));
				}
				else
				{
					logger.trace("Skipping ligplaats: " + ligplaats.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getLigplaats(ligplaats)))
							throw new HandlerException("Could not find ligplaats: " + ligplaats.getIdentificatie());
				}
			}
			
			@Override
			public void handle(Standplaats standplaats) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Processing standplaats: " + standplaats.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getStandplaats(standplaats));
				}
				else
				{
					logger.trace("Skipping standplaats: " + standplaats.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getStandplaats(standplaats)))
							throw new HandlerException("Could not find standplaats: " + standplaats.getIdentificatie());
				}
			}
			
		};
		reader.parse(stream);
	}

	public void setBagDAO(BAGDAO bagDAO)
	{
		this.bagDAO = bagDAO;
	}
	
	public void setBagObjectFactory(BAGObjectFactory bagObjectFactory)
	{
		this.bagObjectFactory = bagObjectFactory;
	}

	public static void main(String[] args) throws Exception
	{
		ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/dao/datasource.xml","nl/ordina/bag/etl/dao/oracle.xml");
		SimpleImportExtractJob job = new SimpleImportExtractJob();
		job.setBagDAO((BAGDAO)serviceLocator.get("bagDAO"));
		job.setBagObjectFactory(new BAGObjectFactory(new BAGGeometrieHandler()));

		//String[] filenames = new String[]{"9999WPL01042011.zip","9999OPR01042011.zip","9999NUM01042011.zip","9999PND01042011.zip","9999VBO01042011.zip","9999LIG01042011.zip","9999STA01042011.zip"};
		//for (String filename : filenames)
			//job.execute(new FileInputStream("i:/BAGExtract/" + filename));

		job.execute(new FileInputStream("i:/BAGExtract/DNLDLXEE02-9990000000-999000006-01042011.zip"));

		System.exit(0);
	}

}
