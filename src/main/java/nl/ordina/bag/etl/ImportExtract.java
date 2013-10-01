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

import nl.ordina.bag.etl.service.ImportExtractJob;
import nl.ordina.bag.etl.util.ServiceLocator;

public class ImportExtract
{
	public static void main(String[] args) throws Exception
	{
		if (args.length == 1)
		{
			System.out.println("ImportExtract started");
			ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/extract.xml");
			ImportExtractJob importExtractJob = (ImportExtractJob)serviceLocator.get("importBAGExtractJob");
			importExtractJob.execute(new File(args[0]));
			System.out.println("ImportExtract finished");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.ImportExtract <filename>");
		System.exit(0);
	}
}
