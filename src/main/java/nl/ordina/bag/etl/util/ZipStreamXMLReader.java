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

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import nl.ordina.bag.etl.xml.XMLMessageBuilder;

public class ZipStreamXMLReader<T>
{
	public T read(XMLMessageBuilder<T> messageBuilder, String filename, InputStream content) throws IOException
	{
		ZipInputStream stream = new ZipInputStream(content);
		try
		{
			ZipEntry entry;
			while ((entry = stream.getNextEntry()) != null)
			{
				try
				{
					if (filename.equals(entry.getName()))
						return messageBuilder.handle(stream);
					stream.closeEntry();
				}
				catch (JAXBException e)
				{
					throw new IOException(e);
				}
			}
			return null;
		}
		finally
		{
			stream.close();
		}
	}
	
}
