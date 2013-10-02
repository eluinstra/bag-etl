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

import java.util.List;

import nl.kadaster.schemas.bag_verstrekkingen.extract_producten_lvc.v20090901.MutatieProduct;
import nl.ordina.bag.etl.Constants.BAGObjectType;
import nl.ordina.bag.etl.dao.BAGDAO;
import nl.ordina.bag.etl.dao.BAGMutatiesDAO;
import nl.ordina.bag.etl.model.BAGObjectFactory;
import nl.ordina.bag.etl.model.Ligplaats;
import nl.ordina.bag.etl.model.Nummeraanduiding;
import nl.ordina.bag.etl.model.OpenbareRuimte;
import nl.ordina.bag.etl.model.Pand;
import nl.ordina.bag.etl.model.Standplaats;
import nl.ordina.bag.etl.model.Verblijfsobject;
import nl.ordina.bag.etl.model.Woonplaats;
import nl.ordina.bag.etl.model.mutatie.BAGMutatie;
import nl.ordina.bag.etl.util.ServiceLocator;
import nl.ordina.bag.etl.xml.BAGGeometrieHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class MutatiesProcessor
{
	protected transient Log logger = LogFactory.getLog(this.getClass());
	protected BAGMutatiesDAO bagMutatiesDAO;
	protected BAGDAO bagDAO;
	protected BAGObjectFactory bagObjectFactory;
	protected MutationListener mutationListener = new DefaultMutationListener();

	public void execute()
	{
		TransactionCallback<?> processMutaties = new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status)
			{
				while (true)
				{
					List<BAGMutatie> mutaties = bagMutatiesDAO.getNextBAGMutaties();
					if (mutaties.size() > 0)
						for (BAGMutatie mutatie : mutaties)
						{
							if (BAGObjectType.WOONPLAATS.equals(mutatie.getObjectType()))
							{
								MutatieProduct mutatieProduct = mutatie.getMutatieProduct();
								if (mutatieProduct.getNieuw() != null)
								{
									Woonplaats woonplaats = bagObjectFactory.getWoonplaats(mutatieProduct.getNieuw().getWoonplaats());
									logger.debug("Woonplaats " + woonplaats.getIdentificatie() + " inserting");
									insert(woonplaats);
									mutationListener.onInsert(mutatie.getObjectType(),woonplaats);
								}
								else if (mutatieProduct.getOrigineel() != null && mutatieProduct.getWijziging() != null)
								{
									Woonplaats origineel = bagObjectFactory.getWoonplaats(mutatieProduct.getOrigineel().getWoonplaats());
									Woonplaats wijziging = bagObjectFactory.getWoonplaats(mutatieProduct.getWijziging().getWoonplaats());
									logger.debug("Woonplaats " + origineel.getIdentificatie() + " updating");
									update(origineel,wijziging);
									mutationListener.onUpdate(mutatie.getObjectType(),origineel,wijziging);
								}
								else
									handleException(mutatieProduct);
							}
							else if (BAGObjectType.OPENBARE_RUIMTE.equals(mutatie.getObjectType()))
							{
								MutatieProduct mutatieProduct = mutatie.getMutatieProduct();
								if (mutatieProduct.getNieuw() != null)
								{
									OpenbareRuimte openbareRuimte = bagObjectFactory.getOpenbareRuimte(mutatieProduct.getNieuw().getOpenbareRuimte());
									logger.debug("Openbare ruimte " + openbareRuimte.getIdentificatie() + " inserting");
									insert(openbareRuimte);
									mutationListener.onInsert(mutatie.getObjectType(),openbareRuimte);
								}
								else if (mutatieProduct.getOrigineel() != null && mutatieProduct.getWijziging() != null)
								{
									OpenbareRuimte origineel = bagObjectFactory.getOpenbareRuimte(mutatieProduct.getOrigineel().getOpenbareRuimte());
									OpenbareRuimte wijziging = bagObjectFactory.getOpenbareRuimte(mutatieProduct.getWijziging().getOpenbareRuimte());
									logger.debug("Openbare ruimte " + origineel.getIdentificatie() + " updating");
									update(origineel,wijziging);
									mutationListener.onUpdate(mutatie.getObjectType(),origineel,wijziging);
								}
								else
									handleException(mutatieProduct);
							}
							else if (BAGObjectType.NUMMERAANDUIDING.equals(mutatie.getObjectType()))
							{
								MutatieProduct mutatieProduct = mutatie.getMutatieProduct();
								if (mutatieProduct.getNieuw() != null)
								{
									Nummeraanduiding nummeraanduiding = bagObjectFactory.getNummeraanduiding(mutatieProduct.getNieuw().getNummeraanduiding());
									logger.debug("Nummeraanduiding " + nummeraanduiding.getIdentificatie() + " inserting");
									insert(nummeraanduiding);
									mutationListener.onInsert(mutatie.getObjectType(),nummeraanduiding);
								}
								else if (mutatieProduct.getOrigineel() != null && mutatieProduct.getWijziging() != null)
								{
									Nummeraanduiding origineel = bagObjectFactory.getNummeraanduiding(mutatieProduct.getOrigineel().getNummeraanduiding());
									Nummeraanduiding wijziging = bagObjectFactory.getNummeraanduiding(mutatieProduct.getWijziging().getNummeraanduiding());
									logger.debug("Nummeraanduiding " + origineel.getIdentificatie() + " updating");
									update(origineel,wijziging);
									mutationListener.onUpdate(mutatie.getObjectType(),origineel,wijziging);
								}
								else
									handleException(mutatieProduct);
							}
							else if (BAGObjectType.PAND.equals(mutatie.getObjectType()))
							{
								MutatieProduct mutatieProduct = mutatie.getMutatieProduct();
								if (mutatieProduct.getNieuw() != null)
								{
									Pand pand = bagObjectFactory.getPand(mutatieProduct.getNieuw().getPand());
									logger.debug("Pand " + pand.getIdentificatie() + " inserting");
									insert(pand);
									mutationListener.onInsert(mutatie.getObjectType(),pand);
								}
								else if (mutatieProduct.getOrigineel() != null && mutatieProduct.getWijziging() != null)
								{
									Pand origineel = bagObjectFactory.getPand(mutatieProduct.getOrigineel().getPand());
									Pand wijziging = bagObjectFactory.getPand(mutatieProduct.getWijziging().getPand());
									logger.debug("Pand " + origineel.getIdentificatie() + " updating");
									update(origineel,wijziging);
									mutationListener.onUpdate(mutatie.getObjectType(),origineel,wijziging);
								}
								else
									handleException(mutatieProduct);
							}
							else if (BAGObjectType.VERBLIJFSOBJECT.equals(mutatie.getObjectType()))
							{
								MutatieProduct mutatieProduct = mutatie.getMutatieProduct();
								if (mutatieProduct.getNieuw() != null)
								{
									Verblijfsobject verblijfsobject = bagObjectFactory.getVerblijfsobject(mutatieProduct.getNieuw().getVerblijfsobject());
									logger.debug("Verblijfsobject " + verblijfsobject.getIdentificatie() + " inserting");
									insert(verblijfsobject);
									mutationListener.onInsert(mutatie.getObjectType(),verblijfsobject);
								}
								else if (mutatieProduct.getOrigineel() != null && mutatieProduct.getWijziging() != null)
								{
									Verblijfsobject origineel = bagObjectFactory.getVerblijfsobject(mutatieProduct.getOrigineel().getVerblijfsobject());
									Verblijfsobject wijziging = bagObjectFactory.getVerblijfsobject(mutatieProduct.getWijziging().getVerblijfsobject());
									logger.debug("Verblijfsobject " + origineel.getIdentificatie() + " updating");
									update(origineel,wijziging);
									mutationListener.onUpdate(mutatie.getObjectType(),origineel,wijziging);
								}
								else
									handleException(mutatieProduct);
							}
							else if (BAGObjectType.LIGPLAATS.equals(mutatie.getObjectType()))
							{
								MutatieProduct mutatieProduct = mutatie.getMutatieProduct();
								if (mutatieProduct.getNieuw() != null)
								{
									Ligplaats ligplaats = bagObjectFactory.getLigplaats(mutatieProduct.getNieuw().getLigplaats());
									logger.debug("Ligplaats " + ligplaats.getIdentificatie() + " inserting");
									insert(ligplaats);
									mutationListener.onInsert(mutatie.getObjectType(),ligplaats);
								}
								else if (mutatieProduct.getOrigineel() != null && mutatieProduct.getWijziging() != null)
								{
									Ligplaats origineel = bagObjectFactory.getLigplaats(mutatieProduct.getOrigineel().getLigplaats());
									Ligplaats wijziging = bagObjectFactory.getLigplaats(mutatieProduct.getWijziging().getLigplaats());
									logger.debug("Ligplaats " + origineel.getIdentificatie() + " updating");
									update(origineel,wijziging);
									mutationListener.onUpdate(mutatie.getObjectType(),origineel,wijziging);
								}
								else
									handleException(mutatieProduct);
							}
							else if (BAGObjectType.STANDPLAATS.equals(mutatie.getObjectType()))
							{
								MutatieProduct mutatieProduct = mutatie.getMutatieProduct();
								if (mutatieProduct.getNieuw() != null)
								{
									Standplaats standplaats = bagObjectFactory.getStandplaats(mutatieProduct.getNieuw().getStandplaats());
									logger.debug("Standplaats " + standplaats.getIdentificatie() + " inserting");
									insert(standplaats);
									mutationListener.onInsert(mutatie.getObjectType(),standplaats);
								}
								else if (mutatieProduct.getOrigineel() != null && mutatieProduct.getWijziging() != null)
								{
									Standplaats origineel = bagObjectFactory.getStandplaats(mutatieProduct.getOrigineel().getStandplaats());
									Standplaats wijziging = bagObjectFactory.getStandplaats(mutatieProduct.getWijziging().getStandplaats());
									logger.debug("Standplaats " + origineel.getIdentificatie() + " updating");
									update(origineel,wijziging);
									mutationListener.onUpdate(mutatie.getObjectType(),origineel,wijziging);
								}
								else
									handleException(mutatieProduct);
							}
							bagMutatiesDAO.delete(mutatie);
						}
					else
						break;
				}
			}

		};
		bagMutatiesDAO.doInTransaction(processMutaties);
	}

	protected void handleException(MutatieProduct mutatieProduct)
	{
		throw new ProcessingException("Unable to process mutatie-product " + mutatieProduct.getVerwerking().getTijdstipVerwerking() + " - " + mutatieProduct.getVerwerking().getVolgnrVerwerking() + " [" + mutatieProduct.getVerwerking().getObjectType() + "]");
	}

	protected void insert(Woonplaats woonplaats)
	{
		bagDAO.insert(woonplaats);
	}

	protected void update(Woonplaats origineel, Woonplaats wijziging)
	{
		bagDAO.update(origineel,wijziging);
	}

	protected void insert(OpenbareRuimte openbareRuimte)
	{
		bagDAO.insert(openbareRuimte);
	}

	protected void update(OpenbareRuimte origineel, OpenbareRuimte wijziging)
	{
		bagDAO.update(origineel,wijziging);
	}

	protected void insert(Nummeraanduiding nummeraanduiding)
	{
		bagDAO.insert(nummeraanduiding);
	}

	protected void update(Nummeraanduiding origineel, Nummeraanduiding wijziging)
	{
		bagDAO.update(origineel,wijziging);
	}

	protected void insert(Pand pand)
	{
		bagDAO.insert(pand);
	}

	protected void update(Pand origineel, Pand wijziging)
	{
		bagDAO.update(origineel,wijziging);
	}

	protected void insert(Verblijfsobject verblijfsobject)
	{
		bagDAO.insert(verblijfsobject);
	}

	protected void update(Verblijfsobject origineel, Verblijfsobject wijziging)
	{
		bagDAO.update(origineel,wijziging);
	}

	protected void insert(Ligplaats ligplaats)
	{
		bagDAO.insert(ligplaats);
	}

	protected void update(Ligplaats origineel, Ligplaats wijziging)
	{
		bagDAO.update(origineel,wijziging);
	}

	protected void insert(Standplaats standplaats)
	{
		bagDAO.insert(standplaats);
	}

	protected void update(Standplaats origineel, Standplaats wijziging)
	{
		bagDAO.update(origineel,wijziging);
	}

	public void setBagMutatiesDAO(BAGMutatiesDAO bagMutatiesDAO)
	{
		this.bagMutatiesDAO = bagMutatiesDAO;
	}
	
	public void setBagDAO(BAGDAO bagDAO)
	{
		this.bagDAO = bagDAO;
	}
	
	public void setBagObjectFactory(BAGObjectFactory bagObjectFactory)
	{
		this.bagObjectFactory = bagObjectFactory;
	}
	
	public void setMutationListener(MutationListener mutationListener)
	{
		this.mutationListener = mutationListener;
	}

	public static void main(String[] args) throws Exception
	{
		ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml");
		MutatiesProcessor processor = new MutatiesProcessor();
		processor.logger.info("Processor started");
		processor.setBagMutatiesDAO((BAGMutatiesDAO)serviceLocator.get("bagMutatiesDAO"));
		processor.setBagDAO((BAGDAO)serviceLocator.get("bagDAO"));
		processor.setBagObjectFactory(new BAGObjectFactory(new BAGGeometrieHandler()));
		processor.execute();
		processor.logger.info("Processor finished");
		System.exit(0);
	}
}
