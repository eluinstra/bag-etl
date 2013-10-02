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
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.sql.DataSource;
import javax.xml.bind.JAXBException;

import nl.kadaster.schemas.bag_verstrekkingen.extract_levering.v20090901.BAGExtractLevering;
import nl.ordina.bag.etl.xml.XMLMessageBuilder;

public class Utils
{
	public enum FileType
	{
		EXTRACT("Leveringsdocument-BAG-Extract.xml"), MUTATIES("Leveringsdocument-BAG-Mutaties.xml");
		
		public String filename;

		FileType(String filename)
		{
			this.filename = filename;
		}
	}

	public static FileType getFileType(File file) throws ZipException, IOException, JAXBException
	{
		ZipFile zipFile = new ZipFile(file);
		try
		{
			if (zipFile.getEntry(FileType.EXTRACT.filename) != null)
				return FileType.EXTRACT;
			if (zipFile.getEntry(FileType.MUTATIES.filename) != null)
				return FileType.MUTATIES;
			else
				return null;
		}
		finally
		{
			zipFile.close();
		}
	}
	
	public static BAGExtractLevering readBagExtractLevering(ZipFile zipFile, FileType fileType) throws IOException, JAXBException
	{
		return read(XMLMessageBuilder.getInstance(BAGExtractLevering.class),fileType.filename,zipFile);
	}
	
	public static <T> T read(XMLMessageBuilder<T> messageBuilder, String filename, ZipFile zipFile) throws IOException, JAXBException
	{
		ZipEntry entry = zipFile.getEntry(filename);
		return entry == null ? null : messageBuilder.handle(zipFile.getInputStream(entry));
	}
	
	public static <T> T read(XMLMessageBuilder<T> messageBuilder, String filename, InputStream content) throws IOException, JAXBException
	{
		ZipInputStream stream = new ZipInputStream(content);
		try
		{
			ZipEntry entry;
			while ((entry = stream.getNextEntry()) != null)
			{
				if (filename.equals(entry.getName()))
					return messageBuilder.handle(stream);
				stream.closeEntry();
			}
			return null;
		}
		finally
		{
			stream.close();
		}
	}

	public static void testDbConnection(DataSource dataSource) throws SQLException
	{
		dataSource.getConnection();
	}
}
