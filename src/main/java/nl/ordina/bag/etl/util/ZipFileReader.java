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
package nl.ordina.bag.etl.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public abstract class ZipFileReader
{
	public void read(File file, String[] files) throws IOException
	{
		ZipFile zipFile = new ZipFile(file);
		try
		{
			for (String f : files)
			{
				ZipEntry entry = zipFile.getEntry(f);
				if (entry != null)
					handle(entry.getName(),zipFile.getInputStream(entry));
			}
		}
		finally
		{
			zipFile.close();
		}
	}
	
	public abstract void handle(String filename, InputStream stream) throws IOException;
}
