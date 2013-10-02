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
package nl.ordina.bag.etl.validation;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import nl.ordina.bag.etl.ValidationException;
import nl.ordina.bag.etl.ValidatorException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SchemaValidator
{
	private Schema schema;

	public SchemaValidator(String xsdFile)
	{
		try
		{
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = schemaFactory.newSchema(new StreamSource(SchemaValidator.class.getResourceAsStream(xsdFile),SchemaValidator.class.getResource(xsdFile).toString()));
		}
		catch (SAXException e)
		{
			throw new ValidatorException("Error creating XSD validator using file: " + xsdFile,e);
		}
	}
	
	public void validate(InputStream stream) throws ValidatorException
	{
		try
		{
			Validator validator = schema.newValidator();
			validator.validate(new SAXSource(new InputSource(stream)));
		}
		catch (IOException e)
		{
			throw new ValidatorException(e);
		}
		catch (SAXException e)
		{
			throw new ValidationException(e);
		}
	}
	
}
