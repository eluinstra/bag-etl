package nl.ordina.bag.etl;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import nl.kadaster.schemas.bag_verstrekkingen.extract_levering.v20090901.BAGExtractLevering;
import nl.ordina.bag.etl.xml.XMLMessageBuilder;

public class Utils
{
	public static final String BAG_EXTRACT_LEVERING_FILENAME = "Leveringsdocument-BAG-Mutaties.xml";

	public static BAGExtractLevering readBagExtractLevering(ZipFile zipFile) throws IOException, JAXBException
	{
		return read(XMLMessageBuilder.getInstance(BAGExtractLevering.class),BAG_EXTRACT_LEVERING_FILENAME,zipFile);
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

}
