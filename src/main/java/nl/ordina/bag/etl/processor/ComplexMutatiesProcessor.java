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
package nl.ordina.bag.etl.processor;

import nl.ordina.bag.etl.dao.BAGDAO;
import nl.ordina.bag.etl.dao.BAGMutatiesDAO;
import nl.ordina.bag.etl.model.Ligplaats;
import nl.ordina.bag.etl.model.Nummeraanduiding;
import nl.ordina.bag.etl.model.OpenbareRuimte;
import nl.ordina.bag.etl.model.Pand;
import nl.ordina.bag.etl.model.Standplaats;
import nl.ordina.bag.etl.model.Verblijfsobject;
import nl.ordina.bag.etl.model.Woonplaats;
import nl.ordina.bag.etl.util.ServiceLocator;

public class ComplexMutatiesProcessor extends MutatiesProcessor
{
	protected void insert(Woonplaats woonplaats)
	{
		if (!bagDAO.exists(woonplaats))
			bagDAO.insert(woonplaats);
		else
			throw new ProcessingException("Woonplaats [" + woonplaats.getIdentificatie() + "] already exists!");
	}

	protected void update(Woonplaats origineel, Woonplaats wijziging)
	{
		bagDAO.delete(origineel);
		if (!bagDAO.exists(wijziging))
			bagDAO.insert(wijziging);
		else
			throw new ProcessingException("Woonplaats [" + wijziging.getIdentificatie() + "] already exists!");
	}

	protected void insert(OpenbareRuimte openbareRuimte)
	{
		if (!bagDAO.exists(openbareRuimte))
			bagDAO.insert(openbareRuimte);
		else
			throw new ProcessingException("OpenbareRuimte [" + openbareRuimte.getIdentificatie() + "] already exists!");
	}

	protected void update(OpenbareRuimte origineel, OpenbareRuimte wijziging)
	{
		bagDAO.delete(origineel);
		if (!bagDAO.exists(wijziging))
			bagDAO.insert(wijziging);
		else
			throw new ProcessingException("OpenbareRuimte [" + wijziging.getIdentificatie() + "] already exists!");
	}

	protected void insert(Nummeraanduiding nummeraanduiding)
	{
		if (!bagDAO.exists(nummeraanduiding))
			bagDAO.insert(nummeraanduiding);
		else
			throw new ProcessingException("Nummeraanduiding [" + nummeraanduiding.getIdentificatie() + "] already exists!");
	}

	protected void update(Nummeraanduiding origineel, Nummeraanduiding wijziging)
	{
		bagDAO.delete(origineel);
		if (!bagDAO.exists(wijziging))
			bagDAO.insert(wijziging);
		else
			throw new ProcessingException("Nummeraanduiding [" + wijziging.getIdentificatie() + "] already exists!");
	}

	protected void insert(Pand pand)
	{
		if (!bagDAO.exists(pand))
			bagDAO.insert(pand);
		else
			throw new ProcessingException("Pand [" + pand.getIdentificatie() + "] already exists!");
	}

	protected void update(Pand origineel, Pand wijziging)
	{
		bagDAO.delete(origineel);
		if (!bagDAO.exists(wijziging))
			bagDAO.insert(wijziging);
		else
			throw new ProcessingException("Pand [" + wijziging.getIdentificatie() + "] already exists!");
	}

	protected void insert(Verblijfsobject verblijfsobject)
	{
		if (!bagDAO.exists(verblijfsobject))
			bagDAO.insert(verblijfsobject);
		else
			throw new ProcessingException("Verblijfsobject [" + verblijfsobject.getIdentificatie() + "] already exists!");
	}

	protected void update(Verblijfsobject origineel, Verblijfsobject wijziging)
	{
		bagDAO.delete(origineel);
		if (!bagDAO.exists(wijziging))
			bagDAO.insert(wijziging);
		else
			throw new ProcessingException("Verblijfsobject [" + wijziging.getIdentificatie() + "] already exists!");
	}

	protected void insert(Ligplaats ligplaats)
	{
		if (!bagDAO.exists(ligplaats))
			bagDAO.insert(ligplaats);
		else
			throw new ProcessingException("Ligplaats [" + ligplaats.getIdentificatie() + "] already exists!");
	}

	protected void update(Ligplaats origineel, Ligplaats wijziging)
	{
		bagDAO.delete(origineel);
		if (!bagDAO.exists(wijziging))
			bagDAO.insert(wijziging);
		else
			throw new ProcessingException("Ligplaats [" + wijziging.getIdentificatie() + "] already exists!");
	}

	protected void insert(Standplaats standplaats)
	{
		if (!bagDAO.exists(standplaats))
			bagDAO.insert(standplaats);
		else
			throw new ProcessingException("Standplaats [" + standplaats.getIdentificatie() + "] already exists!");
	}

	protected void update(Standplaats origineel, Standplaats wijziging)
	{
		bagDAO.delete(origineel);
		if (!bagDAO.exists(wijziging))
			bagDAO.insert(wijziging);
		else
			throw new ProcessingException("Standplaats [" + wijziging.getIdentificatie() + "] already exists!");
	}

	public static void main(String[] args) throws Exception
	{
		ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml");
		ComplexMutatiesProcessor processor = new ComplexMutatiesProcessor();
		processor.logger.info("Processor started");
		processor.setBagMutatiesDAO((BAGMutatiesDAO)serviceLocator.get("bagMutatiesDAO"));
		processor.setBagDAO((BAGDAO)serviceLocator.get("bagDAO"));
		processor.execute();
		processor.logger.info("Processor finished");
		System.exit(0);
	}
}
