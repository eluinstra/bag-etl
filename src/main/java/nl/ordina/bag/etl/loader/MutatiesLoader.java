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

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

import nl.ordina.bag.etl.Constants;
import nl.ordina.bag.etl.Constants.ProcessingStatus;
import nl.ordina.bag.etl.dao.BAGDAO;
import nl.ordina.bag.etl.dao.BAGMutatiesDAO;
import nl.ordina.bag.etl.model.mutatie.MutatiesFile;
import nl.ordina.bag.etl.processor.DefaultMutationListener;
import nl.ordina.bag.etl.processor.MutatiesFileProcessor;
import nl.ordina.bag.etl.processor.MutatiesProcessor;
import nl.ordina.bag.etl.processor.SimpleMutatiesProcessor;
import nl.ordina.bag.etl.util.BeanLocator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class MutatiesLoader
{
	protected transient Log logger = LogFactory.getLog(this.getClass());
	protected BAGMutatiesDAO bagMutatiesDAO;
	protected BAGDAO bagDAO;
	protected MutatiesFileProcessor mutatiesFileProcessor;
	protected MutatiesProcessor mutatiesProcessor;

	public void execute()
	{
		while (true)
		{
			final MutatiesFile mutatiesFile = bagMutatiesDAO.getNexMutatiesFile();
			if (mutatiesFile != null)
			{
				bagMutatiesDAO.doInTransaction(
					new TransactionCallbackWithoutResult()
					{
						@Override
						protected void doInTransactionWithoutResult(TransactionStatus status)
						{
							try
							{
								logger.info("Processing Mutaties File " + new SimpleDateFormat(Constants.DATE_FORMAT).format(mutatiesFile.getDateFrom()) + " started.");
								mutatiesFileProcessor.execute(new ByteArrayInputStream(mutatiesFile.getContent()));
								bagMutatiesDAO.setMutatiesFileStatus(mutatiesFile.getId(),ProcessingStatus.PROCESSED);
								mutatiesProcessor.execute();
							}
							finally
							{
								logger.info("Processing Mutaties File " + new SimpleDateFormat(Constants.DATE_FORMAT).format(mutatiesFile.getDateFrom()) + " ended.");
							}
						}
					}
				);
			}
			else
				break;
		}
	}

	public void setBagMutatiesDAO(BAGMutatiesDAO bagMutatiesDAO)
	{
		this.bagMutatiesDAO = bagMutatiesDAO;
	}
	
	public void setBagDAO(BAGDAO bagDAO)
	{
		this.bagDAO = bagDAO;
	}
	
	public void setMutatiesFileProcessor(MutatiesFileProcessor mutatiesFileProcessor)
	{
		this.mutatiesFileProcessor = mutatiesFileProcessor;
	}
	
	public void setMutatiesProcessor(MutatiesProcessor mutatiesProcessor)
	{
		this.mutatiesProcessor = mutatiesProcessor;
	}

	public static void main(String[] args) throws Exception
	{
		BeanLocator beanLocator = BeanLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml");
		
		MutatiesLoader mutatiesLoader = new MutatiesLoader();
		mutatiesLoader.setBagMutatiesDAO((BAGMutatiesDAO)beanLocator.get("bagMutatiesDAO"));
		mutatiesLoader.setBagDAO((BAGDAO)beanLocator.get("bagDAO"));
		
		MutatiesFileProcessor mutatiesFileProcessor = new MutatiesFileProcessor();
		mutatiesFileProcessor.setBagMutatiesDAO((BAGMutatiesDAO)beanLocator.get("bagMutatiesDAO"));
		mutatiesLoader.setMutatiesFileProcessor(mutatiesFileProcessor);
		
		MutatiesProcessor mutatiesProcessor = new SimpleMutatiesProcessor();
		mutatiesProcessor.setBagMutatiesDAO((BAGMutatiesDAO)beanLocator.get("bagMutatiesDAO"));
		mutatiesProcessor.setBagDAO((BAGDAO)beanLocator.get("bagDAO"));
		mutatiesProcessor.setMutationListener(new DefaultMutationListener());
		mutatiesLoader.setMutatiesProcessor(mutatiesProcessor);

		mutatiesLoader.execute();
		System.exit(0);
	}
}
