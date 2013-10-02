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
package nl.ordina.bag.etl.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import nl.kadaster.schemas.bag_verstrekkingen.extract_producten_lvc.v20090901.MutatieProduct;
import nl.ordina.bag.etl.Constants.BAGObjectType;
import nl.ordina.bag.etl.ProcessingException;
import nl.ordina.bag.etl.dao.BAGMutatiesDAO;
import nl.ordina.bag.etl.model.mutatie.BAGMutatie;
import nl.ordina.bag.etl.util.ServiceLocator;
import nl.ordina.bag.etl.util.ZipStreamReader;
import nl.ordina.bag.etl.xml.HandlerException;
import nl.ordina.bag.etl.xml.MutatiesParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class MutatiesFileProcessor
{
	protected transient Log logger = LogFactory.getLog(this.getClass());
	protected BAGMutatiesDAO bagMutatiesDAO;

	public void execute(final InputStream mutatiesFile)
	{
		bagMutatiesDAO.doInTransaction(
			new TransactionCallbackWithoutResult()
			{
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status)
				{
					try
					{
						processMutatiesFile(mutatiesFile);
					}
					catch (IOException e)
					{
						throw new ProcessingException(e);
					}
				}
			}
		);
	}

	protected void processMutatiesFile(InputStream mutatiesFile) throws IOException
	{
		ZipStreamReader zipStreamReader = new ZipStreamReader()
		{
			@Override
			public void handle(String filename, InputStream stream) throws IOException
			{
				if (filename.matches("9999MUT\\d{8}-\\d{8}\\.zip"))
				{
					logger.info("Processing file " + filename + " started");
					read(stream);
					logger.info("Processing file " + filename + " finished");
				}
				else if (filename.matches("9999MUT\\d{8}-\\d{8}-\\d{6}\\.xml"))
				{
					logger.info("Processing file " + filename + " started");
					processMutatiesXML(stream);
					logger.info("Processing file " + filename + " finished");
				}
				else
					logger.info("Skipping file " + filename);
			}
		};
		zipStreamReader.read(mutatiesFile);
	}

	protected void processMutatiesXML(InputStream mutatiesXML)
	{
		MutatiesParser reader = new MutatiesParser()
		{
			@Override
			public void handle(MutatieProduct mutatieProduct) throws HandlerException
			{
				logger.debug("Importing mutatie-product " + mutatieProduct.getVerwerking().getTijdstipVerwerking() + " - " + mutatieProduct.getVerwerking().getVolgnrVerwerking() + " [" + mutatieProduct.getVerwerking().getObjectType() + "]");
				BAGMutatie mutatie = new BAGMutatie();
				mutatie.setTijdstipVerwerking(mutatieProduct.getVerwerking().getTijdstipVerwerking().toGregorianCalendar().getTime());
				mutatie.setVolgnrVerwerking(mutatieProduct.getVerwerking().getVolgnrVerwerking().intValue());
				mutatie.setObjectType(BAGObjectType.getBAGObjectType(mutatieProduct.getVerwerking().getObjectType()));
				mutatie.setMutatieProduct(mutatieProduct);
				bagMutatiesDAO.insert(mutatie);
			}
		};
		reader.parse(mutatiesXML);
	}

	protected void processMutatiesFile(String filename) throws FileNotFoundException, IOException
	{
		File file = new File(filename);
		logger.info("Processing file " + file.getAbsoluteFile() + " started");
		processMutatiesFile(new FileInputStream(file));
		logger.info("Processing file " + file.getAbsoluteFile() + " finished");
	}
	
	public void setBagMutatiesDAO(BAGMutatiesDAO bagMutatiesDAO)
	{
		this.bagMutatiesDAO = bagMutatiesDAO;
	}

	public static void main(String[] args) throws Exception
	{
		ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml");
		MutatiesFileProcessor processor = new MutatiesFileProcessor();
		processor.logger.info("Processor started");
		processor.setBagMutatiesDAO((BAGMutatiesDAO)serviceLocator.get("bagMutatiesDAO"));
		processor.execute(new FileInputStream("i:/BAGMutaties/DNLDLXAM02-9990000000-999000000-01042011-02042011.zip"));
		processor.execute(new FileInputStream("i:/BAGMutaties/DNLDLXAM02-9990000000-999000001-02042011-03042011.zip"));
		processor.execute(new FileInputStream("i:/BAGMutaties/DNLDLXAM02-9990000000-999000002-03042011-04042011.zip"));
		processor.execute(new FileInputStream("i:/BAGMutaties/DNLDLXAM02-9990000000-999000003-04042011-05042011.zip"));
		processor.logger.info("Processor finished");
		System.exit(0);
	}
}
