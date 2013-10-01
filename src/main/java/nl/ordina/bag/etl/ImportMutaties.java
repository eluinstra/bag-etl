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

import nl.ordina.bag.etl.service.ImportMutatiesFileJob;
import nl.ordina.bag.etl.service.ImportMutatiesJob;
import nl.ordina.bag.etl.util.ServiceLocator;

public class ImportMutaties
{
	public static void main(String[] args) throws Exception
	{
		if (args.length == 1)
		{
			System.out.println("ImportMutaties started");
			ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/mutatie.xml");
			ImportMutatiesFileJob importMutatiesFileJob = (ImportMutatiesFileJob)serviceLocator.get("importMutatiesFileJob");
			ImportMutatiesJob importMutatiesJob = (ImportMutatiesJob)serviceLocator.get("importMutatiesJob");
			for (String filename : args[0].split(","))
				importMutatiesFileJob.execute(new File(filename.trim()));
			importMutatiesJob.execute();
			System.out.println("ImportMutaties finished");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.ImportMutaties <filename>[,<filename>]");
		System.exit(0);
	}
}
