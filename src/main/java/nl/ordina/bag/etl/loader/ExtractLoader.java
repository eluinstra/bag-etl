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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXBException;

import nl.kadaster.schemas.bag_verstrekkingen.extract_levering.v20090901.BAGExtractLevering;
import nl.kadaster.schemas.bag_verstrekkingen.extract_meta.v20090901.DeelbestandInfo;
import nl.kadaster.schemas.imbag.lvc.v20090901.Ligplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Nummeraanduiding;
import nl.kadaster.schemas.imbag.lvc.v20090901.OpenbareRuimte;
import nl.kadaster.schemas.imbag.lvc.v20090901.Pand;
import nl.kadaster.schemas.imbag.lvc.v20090901.Standplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject;
import nl.kadaster.schemas.imbag.lvc.v20090901.Woonplaats;
import nl.ordina.bag.etl.Constants;
import nl.ordina.bag.etl.Constants.BAGObjectType;
import nl.ordina.bag.etl.Constants.ProcessingStatus;
import nl.ordina.bag.etl.dao.BAGDAO;
import nl.ordina.bag.etl.dao.BAGMutatiesDAO;
import nl.ordina.bag.etl.dao.DAOException;
import nl.ordina.bag.etl.model.BAGObjectFactory;
import nl.ordina.bag.etl.processor.ProcessingException;
import nl.ordina.bag.etl.processor.ProcessorException;
import nl.ordina.bag.etl.util.BeanLocator;
import nl.ordina.bag.etl.util.Utils;
import nl.ordina.bag.etl.util.ZipStreamReader;
import nl.ordina.bag.etl.util.Utils.FileType;
import nl.ordina.bag.etl.validation.BAGExtractLeveringValidator;
import nl.ordina.bag.etl.xml.BAGGeometrieHandler;
import nl.ordina.bag.etl.xml.ExtractParser;
import nl.ordina.bag.etl.xml.HandlerException;
import nl.ordina.bag.etl.xml.SimpleExtractParser;
import nl.ordina.bag.etl.xml.XMLMessageBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExtractLoader
{
	protected transient Log logger = LogFactory.getLog(this.getClass());
	protected BAGMutatiesDAO bagMutatiesDAO;
	protected BAGDAO bagDAO;
	protected BAGObjectFactory bagObjectFactory;
	protected BAGExtractLeveringValidator bagExtractLeveringValidator;
	protected long skipObjects;

	public void execute(File extractFile) throws ZipException, IOException, ParseException, JAXBException
	{
		ZipFile zipFile = new ZipFile(extractFile);
		BAGExtractLevering levering = Utils.readBagExtractLevering(zipFile,FileType.EXTRACT);
		bagExtractLeveringValidator.validate(FileType.EXTRACT,levering);
		processBAGExtractFile(levering,zipFile);

		Date date = new SimpleDateFormat(Constants.BAG_DATE_FORMAT).parse(levering.getAntwoord().getVraag().getLVCExtract().getStandTechnischeDatum());
		long id = bagMutatiesDAO.insertMutatiesFile(new Date(0),date,new byte[]{});
		bagMutatiesDAO.setMutatiesFileStatus(id,ProcessingStatus.PROCESSED);

		zipFile.close();
	}

	protected BAGExtractLevering processFile(ZipFile zipFile, final String filename) throws JAXBException, IOException
	{
		ZipEntry entry = zipFile.getEntry(filename);
		return entry == null ? null : XMLMessageBuilder.getInstance(BAGExtractLevering.class).handle(zipFile.getInputStream(entry));
	}

	protected Map<String,String> getFiles(BAGExtractLevering levering)
	{
		Map<String,String> result = new HashMap<String,String>();
		List<DeelbestandInfo> deelbestandInfo = levering.getMetadata().getBestandInfo().getDeelbestandInfo();
		for (DeelbestandInfo deelbestand : deelbestandInfo)
			result.put(deelbestand.getObjecttype().get(0),deelbestand.getBestandsnaam());
		return result;
	}

	protected Map<String,String> getFiles(ZipFile zipFile)
	{
		Map<String,String> result = new HashMap<String,String>();
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements())
		{
			ZipEntry entry = entries.nextElement();
			if (isMutatieFile(entry.getName()))
				result.put(getObjectType(entry.getName()),entry.getName());
		}
		return result;
	}

	private boolean isMutatieFile(String name)
	{
		return name.matches("^\\d{4}\\w{3}\\d{8}\\.zip$");
	}

	private String getObjectType(String name)
	{
		return name.replaceFirst("^\\d{4}(\\w{3})\\d{8}\\.zip$","$1");
	}

	protected void processBAGExtractFile(BAGExtractLevering levering, ZipFile zipFile) throws ProcessorException
	{
		//final Map<String,String> files = getFiles(levering);
		final Map<String,String> files = getFiles(zipFile);
		BAGObjectType[] objectTypes = new BAGObjectType[]{BAGObjectType.WOONPLAATS,BAGObjectType.OPENBARE_RUIMTE,BAGObjectType.NUMMERAANDUIDING,BAGObjectType.PAND,BAGObjectType.VERBLIJFSOBJECT,BAGObjectType.LIGPLAATS,BAGObjectType.STANDPLAATS};
		for (final BAGObjectType objectType : objectTypes)
		{
			try
			{
				String filename = files.get(objectType.getCode());
				ZipEntry entry = zipFile.getEntry(filename);
				skipObjects = bagDAO.getCount(objectType);
				if (skipObjects > 0)
					logger.info("Skipping " + skipObjects + " object" + (skipObjects > 1 ? "en" : "") + " (" + objectType.toString() + ")");
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
			}
			catch (DAOException e)
			{
				throw new ProcessingException(e);
			}
			catch (IOException e)
			{
				throw new ProcessingException(e);
			}
		}
	}
	
	protected void processXML(InputStream stream)
	{
		ExtractParser reader = new SimpleExtractParser()
		{
			@Override
			public void handle(Woonplaats woonplaats) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Inserting woonplaats " + woonplaats.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getWoonplaats(woonplaats));
				}
				else
				{
					logger.trace("Skipping woonplaats " + woonplaats.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getWoonplaats(woonplaats)))
							throw new HandlerException("Could not find woonplaats " + woonplaats.getIdentificatie());
				}
			}
			
			@Override
			public void handle(OpenbareRuimte openbareRuimte) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Inserting openbare ruimte " + openbareRuimte.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getOpenbareRuimte(openbareRuimte));
				}
				else
				{
					logger.trace("Skipping openbare ruimte " + openbareRuimte.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getOpenbareRuimte(openbareRuimte)))
							throw new HandlerException("Could not find openbare ruimte " + openbareRuimte.getIdentificatie());
				}
			}
			
			@Override
			public void handle(Nummeraanduiding nummeraanduiding) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Inserting nummeraanduiding " + nummeraanduiding.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getNummeraanduiding(nummeraanduiding));
				}
				else
				{
					logger.trace("Skipping nummeraanduiding " + nummeraanduiding.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getNummeraanduiding(nummeraanduiding)))
							throw new HandlerException("Could not find nummeraanduiding " + nummeraanduiding.getIdentificatie());
				}
			}

			@Override
			public void handle(Pand pand) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Inserting pand " + pand.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getPand(pand));
				}
				else
				{
					logger.trace("Skipping pand " + pand.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getPand(pand)))
							throw new HandlerException("Could not find pand " + pand.getIdentificatie());
				}
			}

			@Override
			public void handle(Verblijfsobject verblijfsobject) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Inserting verblijfsobject " + verblijfsobject.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getVerblijfsobject(verblijfsobject));
				}
				else
				{
					logger.trace("Skipping verblijfsobject " + verblijfsobject.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getVerblijfsobject(verblijfsobject)))
							throw new HandlerException("Could not find verblijfsobject " + verblijfsobject.getIdentificatie());
				}
			}
			
			@Override
			public void handle(Ligplaats ligplaats) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Inserting ligplaats " + ligplaats.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getLigplaats(ligplaats));
				}
				else
				{
					logger.trace("Skipping ligplaats " + ligplaats.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getLigplaats(ligplaats)))
							throw new HandlerException("Could not find ligplaats " + ligplaats.getIdentificatie());
				}
			}
			
			@Override
			public void handle(Standplaats standplaats) throws HandlerException
			{
				if (skipObjects == 0)
				{
					logger.debug("Inserting standplaats " + standplaats.getIdentificatie());
					bagDAO.insert(bagObjectFactory.getStandplaats(standplaats));
				}
				else
				{
					logger.trace("Skipping standplaats " + standplaats.getIdentificatie());
					skipObjects--;
					if (skipObjects == 0)
						if (!bagDAO.exists(bagObjectFactory.getStandplaats(standplaats)))
							throw new HandlerException("Could not find standplaats " + standplaats.getIdentificatie());
				}
			}
			
		};
		reader.parse(stream);
	}

	public void setBagMutatiesDAO(BAGMutatiesDAO bagMutatiesDAO)
	{
		this.bagMutatiesDAO = bagMutatiesDAO;
	}
	
	public void setBagDAO(BAGDAO bagDAO)
	{
		this.bagDAO = bagDAO;
	}
	
	public void setBagObjectFactory(BAGObjectFactory bagObjectFactory)
	{
		this.bagObjectFactory = bagObjectFactory;
	}
	
	public void setBagExtractLeveringValidator(BAGExtractLeveringValidator bagExtractLeveringValidator)
	{
		this.bagExtractLeveringValidator = bagExtractLeveringValidator;
	}
	
	public static void main(String[] args) throws Exception
	{
		BeanLocator beanLocator = BeanLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml");
		ExtractLoader loader = new ExtractLoader();
		loader.setBagDAO((BAGDAO)beanLocator.get("bagDAO"));
		loader.setBagObjectFactory(new BAGObjectFactory(new BAGGeometrieHandler()));
		loader.setBagExtractLeveringValidator(new BAGExtractLeveringValidator("9990000000","DNLDLXEE02","NEDERLAND","LEVENSCYCLUS","XML","EENMALIG_EXTRACT","02"));
		loader.execute(new File("i:/BAGExtract/DNLDLXEE02-9990000000-999000006-01042011.zip"));
		System.exit(0);
	}
}
