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
import nl.ordina.bag.etl.util.BeanLocator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class LoadMutatiesDirect
{
	public static Log logger = LogFactory.getLog(LoadMutatiesDirect.class);

	public static void main(String[] args) throws Exception
	{
		if (args.length > 0)
		{
			logger.info("LoadMutatiesDirect started");
			BeanLocator beanLocator = BeanLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml","nl/ordina/bag/etl/mutaties.xml");
			BAGMutatiesDAO bagMutatiesDAO = (BAGMutatiesDAO)beanLocator.get("bagMutatiesDAO");
			final MutatiesFileProcessor mutatiesFileProcessor = (MutatiesFileProcessor)beanLocator.get("mutatiesFileProcessor");
			final MutatiesProcessor mutatiesProcessor = (MutatiesProcessor)beanLocator.get("mutatiesProcessor");
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
			logger.info("LoadMutatiesDirect finished");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.LoadMutatiesDirect <filename> [<filename>]");
		System.exit(0);
	}
}
