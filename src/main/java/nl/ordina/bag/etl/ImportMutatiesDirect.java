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
package nl.ordina.bag.etl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import nl.ordina.bag.etl.dao.BAGMutatiesDAO;
import nl.ordina.bag.etl.processor.MutatiesFileProcessor;
import nl.ordina.bag.etl.processor.MutatiesProcessor;
import nl.ordina.bag.etl.processor.ProcessingException;
import nl.ordina.bag.etl.util.ServiceLocator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class ImportMutatiesDirect
{
	public static Log logger = LogFactory.getLog(ImportMutatiesDirect.class);

	public static void main(String[] args) throws Exception
	{
		if (args.length > 0)
		{
			logger.info("ImportMutatiesDirect started");
			ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/mutaties.xml");
			BAGMutatiesDAO bagMutatiesDAO = (BAGMutatiesDAO)serviceLocator.get("bagMutatiesDAO");
			final MutatiesFileProcessor mutatiesFileProcessor = (MutatiesFileProcessor)serviceLocator.get("mutatiesFileProcessor");
			final MutatiesProcessor mutatiesProcessor = (MutatiesProcessor)serviceLocator.get("mutatiesProcessor");
			for (final String filename : args)
			{
				bagMutatiesDAO.doInTransaction(
					new TransactionCallbackWithoutResult()
					{
						@Override
						protected void doInTransactionWithoutResult(TransactionStatus status)
						{
							try
							{
								logger.info("Processing Mutaties File " + filename + " started.");
								mutatiesFileProcessor.execute(new FileInputStream(filename.trim()));
								mutatiesProcessor.execute();
							}
							catch (FileNotFoundException e)
							{
								throw new ProcessingException(e);
							}
							finally
							{
								logger.info("Processing Mutaties File " + filename + " ended.");
							}
						}
					}
				);
			}
			logger.info("ImportMutatiesDirect finished");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.ImportMutatiesDirect <filename> [<filename>]");
		System.exit(0);
	}
}
