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

import nl.ordina.bag.etl.loader.ExtractLoader;
import nl.ordina.bag.etl.util.ServiceLocator;

public class LoadExtract
{
	public static Log logger = LogFactory.getLog(LoadExtract.class);

	public static void main(String[] args) throws Exception
	{
		if (args.length == 1)
		{
			logger.info("LoadExtract started");
			ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml","nl/ordina/bag/etl/extract.xml");
			ExtractLoader extractLoader = (ExtractLoader)serviceLocator.get("extractLoader");
			String filename = args[0].trim(); 
			logger.info("Processing file " + filename + " started");
			extractLoader.execute(new File(filename));
			logger.info("Processing file " + filename + " finished");
			logger.info("LoadExtract finished");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.LoadExtract <filename>");
		System.exit(0);
	}
}
