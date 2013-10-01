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

import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.apache.commons.lang.StringUtils;

public class XMLStreamReaderUtils
{
	public static class Tag
	{
		private int elementType; 
		private String localName;

		public Tag(int elementType, String localName)
		{
			this.elementType = elementType;
			this.localName = localName;
		}

		public int getElementType()
		{
			return elementType;
		}
		
		public String getLocalName()
		{
			return localName;
		}
		
		@Override
		public String toString()
		{
			return localName;
		}
	}
	
	public static XMLStreamReader getXMLStreamReader(InputStream is) throws XMLStreamException, FactoryConfigurationError
	{
		//return XMLInputFactory.newInstance().createXMLStreamReader(is);
		return new StreamReaderDelegate(XMLInputFactory.newInstance().createXMLStreamReader(is))
		{
			public int next() throws XMLStreamException
			{
				while (true)
				{
					int event = super.next();
					switch (event)
					{
						case XMLStreamConstants.COMMENT:
						case XMLStreamConstants.PROCESSING_INSTRUCTION:
							continue;
						default:
							return event;
					}
				}
			}
		};
	}

	public static String readText(XMLStreamReader reader) throws XMLStreamException
	{
		return reader.getElementText();
//		reader.nextTag();
//		reader.require(XMLStreamConstants.CHARACTERS,null,null);
//		return reader.getText();
	}
	
	public static Tag getTag(XMLStreamReader reader) throws XMLStreamException
	{
		if (!reader.isStartElement() && !reader.isEndElement())
			reader.nextTag();
		return new Tag(reader.getEventType(),reader.getLocalName());
	}

	public static void getTag(XMLStreamReader reader, Tag tag) throws XMLStreamException
	{
		if (!reader.isStartElement() && !reader.isEndElement())
			reader.nextTag();
		reader.require(tag.getElementType(),null,tag.getLocalName());
	}

	public static void readStartTag(XMLStreamReader reader, String localName) throws XMLStreamException
	{
		reader.nextTag();
		reader.require(XMLStreamConstants.START_ELEMENT,null,localName);
	}

	public static Tag readTag(XMLStreamReader reader, Tag...tags) throws XMLStreamException
	{
		reader.nextTag();
		for (Tag tag : tags)
			if (reader.getEventType() == tag.getElementType())
				if (reader.getLocalName().equals(tag.getLocalName()))
					return tag;
		throw new XMLStreamException("Encountered element [" + reader.getLocalName() + "] while one of the following elements [" + StringUtils.join(tags,",") + "] was expected",reader.getLocation());
	}
	
	public static void readEndTag(XMLStreamReader reader, String localName) throws XMLStreamException
	{
		reader.nextTag();
		reader.require(XMLStreamConstants.END_ELEMENT,null,localName);
	}

}
