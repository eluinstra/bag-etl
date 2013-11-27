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

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import nl.kadaster.schemas.bag_verstrekkingen.extract_deelbestand_lvc.v20090901.BAGExtractDeelbestandLVC;
import nl.kadaster.schemas.imbag.lvc.v20090901.Ligplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Nummeraanduiding;
import nl.kadaster.schemas.imbag.lvc.v20090901.OpenbareRuimte;
import nl.kadaster.schemas.imbag.lvc.v20090901.Pand;
import nl.kadaster.schemas.imbag.lvc.v20090901.Standplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject;
import nl.kadaster.schemas.imbag.lvc.v20090901.Woonplaats;

import org.apache.commons.io.input.CloseShieldInputStream;

public abstract class SimpleExtractParser extends ExtractParser
{
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
		for (Woonplaats woonplaats : extract.getAntwoord().getProducten().getLVCProduct().getWoonplaats())
			handle(woonplaats);
		for (OpenbareRuimte openbareRuimte : extract.getAntwoord().getProducten().getLVCProduct().getOpenbareRuimte())
			handle(openbareRuimte);
		for (Nummeraanduiding nummeraanduiding : extract.getAntwoord().getProducten().getLVCProduct().getNummeraanduiding())
			handle(nummeraanduiding);
		for (Pand pand : extract.getAntwoord().getProducten().getLVCProduct().getPand())
			handle(pand);
		for (Verblijfsobject verblijfsobject : extract.getAntwoord().getProducten().getLVCProduct().getVerblijfsobject())
			handle(verblijfsobject);
		for (Ligplaats ligplaats : extract.getAntwoord().getProducten().getLVCProduct().getLigplaats())
			handle(ligplaats);
		for (Standplaats standplaats : extract.getAntwoord().getProducten().getLVCProduct().getStandplaats())
			handle(standplaats);
	}

}
