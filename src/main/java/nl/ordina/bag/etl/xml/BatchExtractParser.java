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
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import nl.kadaster.schemas.bag_verstrekkingen.extract_deelbestand_lvc.v20090901.BAGExtractDeelbestandLVC;
import nl.ordina.bag.etl.validation.SchemaValidator;

import org.apache.commons.io.input.CloseShieldInputStream;

public abstract class BatchExtractParser implements BatchExtractHandler
{
	private final String XSD_FILE = "/nl/ordina/bag/etl/xsd/bag-verstrekkingen/extract-deelbestand-lvc/v20090901/BagvsExtractDeelbestandExtractLvc-1.4.xsd";
	protected Schema schema;
	protected XMLMessageBuilder<BAGExtractDeelbestandLVC> objectBuilder;

	public BatchExtractParser()
	{
		try
		{
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = schemaFactory.newSchema(new StreamSource(SchemaValidator.class.getResourceAsStream(XSD_FILE),SchemaValidator.class.getResource(XSD_FILE).toString()));
			objectBuilder = XMLMessageBuilder.getInstance(BAGExtractDeelbestandLVC.class);
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
			BAGExtractDeelbestandLVC extract = objectBuilder.handle(new CloseShieldInputStream(is));
			parse(extract);
		}
		catch (XMLStreamException | FactoryConfigurationError | JAXBException e)
		{
			throw new ParseException(e);
		}
	}

	private void parse(BAGExtractDeelbestandLVC extract) throws XMLStreamException, JAXBException
	{
		handleWoonplaatsen(extract.getAntwoord().getProducten().getLVCProduct().getWoonplaats());
		handleOpenbareRuimten(extract.getAntwoord().getProducten().getLVCProduct().getOpenbareRuimte());
		handleNummeraanduidingen(extract.getAntwoord().getProducten().getLVCProduct().getNummeraanduiding());
		handlePanden(extract.getAntwoord().getProducten().getLVCProduct().getPand());
		handleVerblijfsobjecten(extract.getAntwoord().getProducten().getLVCProduct().getVerblijfsobject());
		handleLigplaatsen(extract.getAntwoord().getProducten().getLVCProduct().getLigplaats());
		handleStandplaatsen(extract.getAntwoord().getProducten().getLVCProduct().getStandplaats());
	}

}
