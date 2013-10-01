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

import nl.ordina.bag.etl.service.MutatiesFileService;
import nl.ordina.bag.etl.service.MutatiesService;
import nl.ordina.bag.etl.util.ServiceLocator;

public class ImportMutaties
{
	public static Log logger = LogFactory.getLog(ImportMutaties.class);

	public static void main(String[] args) throws Exception
	{
		if (args.length == 1)
		{
			logger.info("ImportMutaties started");
			ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/mutatie.xml");
			MutatiesFileService mutatiesFileService = (MutatiesFileService)serviceLocator.get("mutatiesFileService");
			MutatiesService mutatiesService = (MutatiesService)serviceLocator.get("mutatiesService");
			logger.info("Import Mutaties File started.");
			for (String filename : args[0].split(","))
				try
				{
					filename = filename.trim();
					logger.info("Processing file " + filename + " started");
					mutatiesFileService.importMutatiesFile(new File(filename));
					logger.info("Processing file " + filename + " finished");
				}
				catch (ProcessingException | ValidationException e)
				{
					logger.error(e);
				}
			logger.info("Import Mutaties File ended.");
			logger.info("Import Mutaties started.");
			mutatiesService.importMutaties();
			logger.info("Import Mutaties ended.");
			System.out.println("ImportMutaties finished");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.ImportMutaties <filename>[,<filename>]");
		System.exit(0);
	}
}
