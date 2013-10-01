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
package nl.ordina.bag.etl.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.kadaster.schemas.imbag.imbag_types.v20090901.Indicatie;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.StatusPlaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.GerelateerdeAdressen.Nevenadres;
import nl.ordina.bag.etl.Constants;
import nl.ordina.bag.etl.xml.BAGGeometrieHandler;

public class BAGLigplaatsImpl implements Ligplaats
{
	private BAGGeometrieHandler geometrieHandler;
	private nl.kadaster.schemas.imbag.lvc.v20090901.Ligplaats ligplaats;

	protected BAGLigplaatsImpl(BAGGeometrieHandler geometrieHandler, nl.kadaster.schemas.imbag.lvc.v20090901.Ligplaats ligplaats)
	{
		this.geometrieHandler = geometrieHandler;
		this.ligplaats = ligplaats;
	}

	@Override
	public long getIdentificatie()
	{
		return Long.parseLong(ligplaats.getIdentificatie());
	}

	@Override
	public Indicatie getAanduidingRecordInactief()
	{
		return ligplaats.getAanduidingRecordInactief();
	}

	@Override
	public long getAanduidingRecordCorrectie()
	{
		return ligplaats.getAanduidingRecordCorrectie().longValue();
	}

	@Override
	public Indicatie getOfficieel()
	{
		return ligplaats.getOfficieel();
	}

	@Override
	public StatusPlaats getLigplaatsStatus()
	{
		return ligplaats.getLigplaatsStatus();
	}

	@Override
	public String getLigplaatsGeometrie()
	{
		return geometrieHandler.handle(ligplaats.getLigplaatsGeometrie());
	}

	@Override
	public Date getBegindatumTijdvakGeldigheid()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(ligplaats.getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date getEinddatumTijdvakGeldigheid()
	{
		try
		{
			return ligplaats.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid() == null ? null : new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(ligplaats.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Indicatie getInOnderzoek()
	{
		return ligplaats.getInOnderzoek();
	}

	@Override
	public Date getDocumentdatum()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATE_FORMAT).parse(ligplaats.getBron().getDocumentdatum());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDocumentnummer()
	{
		return ligplaats.getBron().getDocumentnummer();
	}

	@Override
	public long getHoofdAdres()
	{
		return Long.parseLong(ligplaats.getGerelateerdeAdressen().getHoofdadres().getIdentificatie());
	}
	
	@Override
	public List<Long> getNevenAdressen()
	{
		List<Long> result = new ArrayList<Long>();
		for (Nevenadres nevenadres : ligplaats.getGerelateerdeAdressen().getNevenadres())
			result.add(Long.parseLong(nevenadres.getIdentificatie()));
		return result;
	}
	
}
