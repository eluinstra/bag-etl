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

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import nl.ordina.bag.etl.loader.MutatiesFileLoader;
import nl.ordina.bag.etl.loader.MutatiesLoader;
import nl.ordina.bag.etl.processor.ProcessingException;
import nl.ordina.bag.etl.util.ServiceLocator;
import nl.ordina.bag.etl.validation.ValidationException;

public class LoadMutaties
{
	public static Log logger = LogFactory.getLog(LoadMutaties.class);

	public static void main(String[] args) throws Exception
	{
		if (args.length > 0)
		{
			logger.info("LoadMutaties started");
			ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml","nl/ordina/bag/etl/mutaties.xml");
			MutatiesFileLoader mutatiesFileLoader = (MutatiesFileLoader)serviceLocator.get("mutatiesFileService");
			MutatiesLoader mutatiesLoader = (MutatiesLoader)serviceLocator.get("mutatiesService");
			logger.info("Load Mutaties File started.");
			for (String filename : args)
				try
				{
					filename = filename.trim();
					logger.info("Processing file " + filename + " started");
					mutatiesFileLoader.execute(new File(filename));
					logger.info("Processing file " + filename + " finished");
				}
				catch (ProcessingException | ValidationException e)
				{
					logger.error(e);
				}
			logger.info("Load Mutaties File ended.");
			logger.info("Load Mutaties started.");
			mutatiesLoader.execute();
			logger.info("Load Mutaties ended.");
			logger.info("LoadMutaties finished");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.LoadMutaties <filename> [<filename>]");
		System.exit(0);
	}
}
