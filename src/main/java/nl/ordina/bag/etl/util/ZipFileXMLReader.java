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
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import nl.ordina.bag.etl.xml.XMLMessageBuilder;

public abstract class ZipFileXMLReader<T>
{
	public T read(XMLMessageBuilder<T> messageBuilder, String filename, ZipFile zipFile) throws IOException, JAXBException
	{
		ZipEntry entry = zipFile.getEntry(filename);
		if (entry != null)
			return messageBuilder.handle(zipFile.getInputStream(entry));

		Enumeration<? extends ZipEntry> files = zipFile.entries();
		while (files.hasMoreElements())
		{
			entry = files.nextElement();
			if (filename.matches(".*\\.zip"))
				return read(messageBuilder,entry.getName(),zipFile.getInputStream(entry));
		}
		return null;
	}
	
	public T read(XMLMessageBuilder<T> messageBuilder, String filename, InputStream inputStream) throws IOException, JAXBException
	{
		ZipInputStream stream = new ZipInputStream(inputStream);
		try
		{
			ZipEntry entry;
			while ((entry = stream.getNextEntry()) != null)
			{
				if (filename.equals(entry.getName()))
					return messageBuilder.handle(stream);
				else if (filename.matches(".*\\.zip"))
					return read(messageBuilder,entry.getName(),stream);
				stream.closeEntry();
			}
			return null;
		}
		finally
		{
			stream.close();
		}
	}

}
