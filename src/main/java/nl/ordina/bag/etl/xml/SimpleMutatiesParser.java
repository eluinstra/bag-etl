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

import nl.kadaster.schemas.bag_verstrekkingen.extract_deelbestand_mutaties_lvc.v20090901.BAGMutatiesDeelbestandLVC;
import nl.kadaster.schemas.bag_verstrekkingen.extract_producten_lvc.v20090901.MutatieProduct;

import org.apache.commons.io.input.CloseShieldInputStream;

public abstract class SimpleMutatiesParser extends MutatiesParser
{
	public void parse(InputStream is) throws ParseException, HandlerException
	{
		try
		{
			BAGMutatiesDeelbestandLVC mutaties = objectBuilder.handle(new CloseShieldInputStream(is));
			parse(mutaties);
		}
		catch (XMLStreamException | FactoryConfigurationError | JAXBException e)
		{
			throw new ParseException(e);
		}
	}

	private void parse(BAGMutatiesDeelbestandLVC mutaties) throws XMLStreamException, JAXBException
	{
		for (MutatieProduct mutatie : mutaties.getAntwoord().getProducten().getMutatieProduct())
			handle(mutatie);
	}

}
