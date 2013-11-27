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
package nl.ordina.bag.etl.xml;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import nl.kadaster.schemas.bag_verstrekkingen.extract_deelbestand_mutaties_lvc.v20090901.BAGMutatiesDeelbestandLVC;
import nl.kadaster.schemas.bag_verstrekkingen.extract_deelbestand_mutaties_lvc.v20090901.BAGMutatiesDeelbestandLVC.Antwoord.Vraag;
import nl.kadaster.schemas.bag_verstrekkingen.extract_producten_lvc.v20090901.MutatieProduct;
import nl.ordina.bag.etl.util.XMLStreamReaderUtils;
import nl.ordina.bag.etl.util.XMLStreamReaderUtils.Tag;
import nl.ordina.bag.etl.validation.SchemaValidator;

public abstract class MutatiesParser implements MutatiesHandler
{
	private final String XSD_FILE = "/nl/ordina/bag/etl/xsd/bag-verstrekkingen/extract-deelbestand-mutaties-lvc/v20090901/BagvsExtractDeelbestandMutatieLvc-1.4.xsd";
	protected Schema schema;
	protected XMLMessageBuilder<BAGMutatiesDeelbestandLVC> objectBuilder;

	public MutatiesParser()
	{
		try
		{
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = schemaFactory.newSchema(new StreamSource(SchemaValidator.class.getResourceAsStream(XSD_FILE),SchemaValidator.class.getResource(XSD_FILE).toString()));
			objectBuilder = XMLMessageBuilder.getInstance(BAGMutatiesDeelbestandLVC.class);
		}
		catch (Exception e)
		{
			throw new ParserException(e);
		}
	}
	
	public void parse(InputStream is) throws ParseException, HandlerException
	{
		try
		{
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(is);
			parse(reader);
		}
		catch (XMLStreamException | FactoryConfigurationError | JAXBException e)
		{
			throw new ParseException(e);
		}
	}

	private void parse(XMLStreamReader reader) throws XMLStreamException, JAXBException
	{
		XMLStreamReaderUtils.readStartTag(reader,"BAG-Mutaties-Deelbestand-LVC");
		XMLStreamReaderUtils.readStartTag(reader,"antwoord");
		XMLStreamReaderUtils.readStartTag(reader,"vraag");
		// objectBuilder.handle(schema,reader,Vraag.class);
		objectBuilder.handle(reader,Vraag.class);
		Tag tag = XMLStreamReaderUtils.getTag(reader);
		if ("producten".equals(tag.getLocalName()))
		{
			while (true)
			{
				tag = XMLStreamReaderUtils.readTag(reader,new Tag(XMLStreamReader.START_ELEMENT,"Mutatie-product"),new Tag(XMLStreamReader.END_ELEMENT,"producten"));
				if ("Mutatie-product".equals(tag.getLocalName()))
					handle(objectBuilder.handle(schema,reader,MutatieProduct.class));
				else if ("producten".equals(tag.getLocalName()))
					break;
			}
			XMLStreamReaderUtils.readEndTag(reader,"antwoord");
		}
		XMLStreamReaderUtils.readEndTag(reader,"BAG-Mutaties-Deelbestand-LVC");
	}

}
