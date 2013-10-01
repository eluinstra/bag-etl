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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.ordina.bag.etl.Constants;
import nl.ordina.bag.etl.dao.BAGMutatiesDAO;
import nl.ordina.bag.etl.util.ServiceLocator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleImportMutatiesFileJob
{
	protected transient Log logger = LogFactory.getLog(this.getClass());
	private BAGMutatiesDAO bagMutatiesDAO;

	public void execute(Date dateFrom, Date dateTo, InputStream mutatiesFile)
	{
		try
		{
			logger.info("Import Mutaties File Job started.");
			if (bagMutatiesDAO.existsMutatiesFile(dateFrom))
			{
				logger.warn("Mutaties File " + new SimpleDateFormat(Constants.DATE_FORMAT).format(dateFrom) + " already exists!");
				return;
			}
			logger.info("Importing Mutaties File " + new SimpleDateFormat(Constants.DATE_FORMAT).format(dateFrom));
			bagMutatiesDAO.insertMutatiesFile(dateFrom,dateTo,IOUtils.toByteArray(mutatiesFile));
		}
		catch (IOException e)
		{
			logger.error("",e);
		}
		finally
		{
			logger.info("Import Mutaties File Job ended.");
		}
	}

	public void setBagMutatiesDAO(BAGMutatiesDAO bagMutatiesDAO)
	{
		this.bagMutatiesDAO = bagMutatiesDAO;
	}
	
	public static void main(String[] args) throws Exception
	{
		ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/dao/datasource.xml","nl/ordina/bag/etl/dao/oracle.xml");
		SimpleImportMutatiesFileJob importMutatiesFile = new SimpleImportMutatiesFileJob();
		importMutatiesFile.setBagMutatiesDAO((BAGMutatiesDAO)serviceLocator.get("bagMutatiesDAO"));
		importMutatiesFile.execute(new SimpleDateFormat("ddMMyyyy").parse("01042011"),new SimpleDateFormat("ddMMyyyy").parse("02042011"),new FileInputStream("I:/BAGMutaties/DNLDLXAM02-9990000000-999000000-01042011-02042011.zip"));
		importMutatiesFile.execute(new SimpleDateFormat("ddMMyyyy").parse("02042011"),new SimpleDateFormat("ddMMyyyy").parse("03042011"),new FileInputStream("I:/BAGMutaties/DNLDLXAM02-9990000000-999000001-02042011-03042011.zip"));
		importMutatiesFile.execute(new SimpleDateFormat("ddMMyyyy").parse("03042011"),new SimpleDateFormat("ddMMyyyy").parse("04042011"),new FileInputStream("I:/BAGMutaties/DNLDLXAM02-9990000000-999000002-03042011-04042011.zip"));
		importMutatiesFile.execute(new SimpleDateFormat("ddMMyyyy").parse("04042011"),new SimpleDateFormat("ddMMyyyy").parse("05042011"),new FileInputStream("I:/BAGMutaties/DNLDLXAM02-9990000000-999000003-04042011-05042011.zip"));
		System.exit(0);
	}
}
