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
package nl.ordina.bag.etl;

import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.Writer;

//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

//import org.apache.xml.serialize.OutputFormat;
//import org.apache.xml.serialize.XMLSerializer;
//import org.w3c.dom.Document;
//import org.xml.sax.SAXException;

public class XMLFormatter
{

	private static void format(Source source, Result result) throws TransformerException
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number",2);
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.STANDALONE,"yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no");
		transformer.setOutputProperty(OutputKeys.METHOD,"xml");
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		transformer.transform(source,result);
	}

//	public static void format(InputStream source, OutputStream result) throws ParserConfigurationException, SAXException, IOException
//	{
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		Document document = db.parse(source);
//		OutputFormat format = new OutputFormat();
//		format.setLineWidth(65);
//		format.setIndenting(true);
//		format.setIndent(2);
//		Writer w = new OutputStreamWriter(result);
//		XMLSerializer serializer = new XMLSerializer(w,format);
//		serializer.serialize(document);
//	}

	public static void main(String[] args) throws Exception
	{
		if (args.length == 1)
		{
			String filename = args[0];
			format(new StreamSource(new File(filename)),new StreamResult(new File(filename + ".xml")));
//			format(new FileInputStream(new File(filename)),new FileOutputStream(new File(filename + ".xml")));
			System.out.println("Done.");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.XMLFormatter <filename>");
	}

}
