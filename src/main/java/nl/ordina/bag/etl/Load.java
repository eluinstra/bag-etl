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

import nl.ordina.bag.etl.loader.ExtractLoader;
import nl.ordina.bag.etl.loader.MutatiesFileLoader;
import nl.ordina.bag.etl.loader.MutatiesLoader;
import nl.ordina.bag.etl.util.Log4jUtils;
import nl.ordina.bag.etl.util.ServiceLocator;
import nl.ordina.bag.etl.util.Utils;
import nl.ordina.bag.etl.util.Utils.FileType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;

public class Load
{
	public static Log logger = LogFactory.getLog(Load.class);

	public static void main(String[] args) throws Exception
	{
		if (args.length > 0 && args.length < 3)
		{
			logger.info("Load started");
			ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationContext.xml");
			File file = new File(args[0].trim());
			if (args.length == 2)
				Log4jUtils.setLogLevel("nl.ordina.bag.etl",Level.toLevel(args[1].trim()));
			FileType fileType = Utils.getFileType(file);
			if (FileType.EXTRACT.equals(fileType))
			{
				logger.info("LoadExtract started");
				ExtractLoader extractLoader = (ExtractLoader)serviceLocator.get("extractService");
				logger.info("Processing file " + file.getName() + " started");
				extractLoader.execute(file);
				logger.info("Processing file " + file.getName() + " finished");
				logger.info("LoadExtract finished");
			}
			else if (FileType.MUTATIES.equals(fileType))
			{
				logger.info("LoadMutaties started");
				MutatiesFileLoader mutatiesFileLoader = (MutatiesFileLoader)serviceLocator.get("mutatiesFileService");
				MutatiesLoader mutatiesLoader = (MutatiesLoader)serviceLocator.get("mutatiesService");
				logger.info("Load Mutaties File started.");
				logger.info("Processing file " + file.getName() + " started");
				mutatiesFileLoader.execute(file);
				logger.info("Processing file " + file.getName() + " finished");
				logger.info("Load Mutaties File ended.");

				logger.info("Load Mutaties started.");
				mutatiesLoader.execute();
				logger.info("Load Mutaties ended.");
				logger.info("LoadMutaties finished");
			}
			else
				logger.warn(file.getName() + " is not a valid BAG file!");
			logger.info("Load finished");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.Load <filename> [<loglevel>]");
		System.exit(0);
	}
}
