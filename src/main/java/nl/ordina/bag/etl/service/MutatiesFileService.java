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
package nl.ordina.bag.etl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXBException;

import nl.kadaster.schemas.bag_verstrekkingen.extract_levering.v20090901.BAGExtractLevering;
import nl.ordina.bag.etl.Constants;
import nl.ordina.bag.etl.ProcessingException;
import nl.ordina.bag.etl.ValidationException;
import nl.ordina.bag.etl.dao.BAGMutatiesDAO;
import nl.ordina.bag.etl.util.ServiceLocator;
import nl.ordina.bag.etl.xml.XMLMessageBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MutatiesFileService
{
	protected transient Log logger = LogFactory.getLog(this.getClass());
	private BAGMutatiesDAO bagMutatiesDAO;
	private BAGExtractLeveringValidator bagExtractLeveringValidator;

	public void importMutatiesFile(File mutatiesFile) throws ZipException, IOException, JAXBException
	{
		ZipFile zipFile = new ZipFile(mutatiesFile);
		BAGExtractLevering levering = processFile(zipFile,"Leveringsdocument-BAG-Mutaties.xml");
		if (levering == null)
			throw new ValidationException("Leveringsdocument-BAG-Mutaties.xml not found!");
		if (bagExtractLeveringValidator != null)
			bagExtractLeveringValidator.validate(levering);
		Date dateFrom = levering.getAntwoord().getVraag().getMUTExtract().getMutatieperiode().getMutatiedatumVanaf().toGregorianCalendar().getTime();
		Date dateTo = levering.getAntwoord().getVraag().getMUTExtract().getMutatieperiode().getMutatiedatumTot().toGregorianCalendar().getTime();
		if (bagMutatiesDAO.existsMutatiesFile(dateFrom))
			throw new ProcessingException("Mutaties File " + new SimpleDateFormat(Constants.DATE_FORMAT).format(dateFrom) + " already exists!");
		logger.info("Mutaties File " + new SimpleDateFormat(Constants.DATE_FORMAT).format(dateFrom) + " found.");
		zipFile.close();

		FileInputStream stream = new FileInputStream(mutatiesFile);
		bagMutatiesDAO.insertMutatiesFile(dateFrom,dateTo,IOUtils.toByteArray(stream));
		stream.close();
	}

	private BAGExtractLevering processFile(ZipFile zipFile, String filename) throws IOException, JAXBException
	{
		ZipEntry entry = zipFile.getEntry(filename);
		return entry == null ? null : XMLMessageBuilder.getInstance(BAGExtractLevering.class).handle(zipFile.getInputStream(entry));
	}

	public void setBagMutatiesDAO(BAGMutatiesDAO bagMutatiesDAO)
	{
		this.bagMutatiesDAO = bagMutatiesDAO;
	}
	
	public void setBagExtractLeveringValidator(BAGExtractLeveringValidator bagExtractLeveringValidator)
	{
		this.bagExtractLeveringValidator = bagExtractLeveringValidator;
	}

	public static void main(String[] args) throws Exception
	{
		ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/dao/datasource.xml","nl/ordina/bag/etl/dao/oracle.xml");
		MutatiesFileService importMutatiesFile = new MutatiesFileService();
		importMutatiesFile.setBagMutatiesDAO((BAGMutatiesDAO)serviceLocator.get("bagMutatiesDAO"));
		importMutatiesFile.setBagExtractLeveringValidator(new BAGExtractLeveringValidator("9990000000","DNLDLXAM02","NEDERLAND","LEVENSCYCLUS","XML","ABONNEMENT_MUTATIE","02"));
		importMutatiesFile.importMutatiesFile(new File("i:/BAGMutaties/DNLDLXAM02-9990000000-999000000-01042011-02042011.zip"));
		importMutatiesFile.importMutatiesFile(new File("i:/BAGMutaties/DNLDLXAM02-9990000000-999000001-02042011-03042011.zip"));
		importMutatiesFile.importMutatiesFile(new File("i:/BAGMutaties/DNLDLXAM02-9990000000-999000002-03042011-04042011.zip"));
		importMutatiesFile.importMutatiesFile(new File("i:/BAGMutaties/DNLDLXAM02-9990000000-999000003-04042011-05042011.zip"));
		System.exit(0);
	}
}
