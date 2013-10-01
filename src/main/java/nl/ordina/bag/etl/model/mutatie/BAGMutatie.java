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
package nl.ordina.bag.etl.model.mutatie;

import java.util.Date;

import nl.kadaster.schemas.bag_verstrekkingen.extract_producten_lvc.v20090901.MutatieProduct;
import nl.ordina.bag.etl.Constants.BAGObjectType;

public class BAGMutatie
{
	private long id;
	private Date tijdstipVerwerking;
	private int volgnrVerwerking;
	private BAGObjectType objectType;
	private MutatieProduct mutatieProduct;

	public BAGMutatie()
	{
	}

	public BAGMutatie(Date tijdstipVerwerking, int volgnrVerwerking, BAGObjectType objectType, MutatieProduct mutatieProduct)
	{
		this.tijdstipVerwerking = tijdstipVerwerking;
		this.volgnrVerwerking = volgnrVerwerking;
		this.objectType = objectType;
		this.mutatieProduct = mutatieProduct;
	}

	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}

	public Date getTijdstipVerwerking()
	{
		return tijdstipVerwerking;
	}
	
	public void setTijdstipVerwerking(Date tijdstipVerwerking)
	{
		this.tijdstipVerwerking = tijdstipVerwerking;
	}

	public int getVolgnrVerwerking()
	{
		return volgnrVerwerking;
	}
	
	public void setVolgnrVerwerking(int volgnrVerwerking)
	{
		this.volgnrVerwerking = volgnrVerwerking;
	}

	public BAGObjectType getObjectType()
	{
		return objectType;
	}
	
	public void setObjectType(BAGObjectType objectType)
	{
		this.objectType = objectType;
	}

	public MutatieProduct getMutatieProduct()
	{
		return mutatieProduct;
	}
	
	public void setMutatieProduct(MutatieProduct mutatieProduct)
	{
		this.mutatieProduct = mutatieProduct;
	}

}
