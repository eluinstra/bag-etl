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

public class BAGStandplaatsImpl implements Standplaats
{
	private BAGGeometrieHandler geometrieHandler;
	private nl.kadaster.schemas.imbag.lvc.v20090901.Standplaats standplaats;

	public BAGStandplaatsImpl(BAGGeometrieHandler geometrieHandler, nl.kadaster.schemas.imbag.lvc.v20090901.Standplaats standplaats)
	{
		this.geometrieHandler = geometrieHandler;
		this.standplaats = standplaats;
	}

	@Override
	public long getIdentificatie()
	{
		return Long.parseLong(standplaats.getIdentificatie());
	}

	@Override
	public Indicatie getAanduidingRecordInactief()
	{
		return standplaats.getAanduidingRecordInactief();
	}

	@Override
	public long getAanduidingRecordCorrectie()
	{
		return standplaats.getAanduidingRecordCorrectie().longValue();
	}

	@Override
	public Indicatie getOfficieel()
	{
		return standplaats.getOfficieel();
	}

	@Override
	public StatusPlaats getStandplaatsStatus()
	{
		return standplaats.getStandplaatsStatus();
	}

	@Override
	public String getStandplaatsGeometrie()
	{
		return geometrieHandler.handle(standplaats.getStandplaatsGeometrie());
	}

	@Override
	public Date getBegindatumTijdvakGeldigheid()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(standplaats.getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid());
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
			return standplaats.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid() == null ? null : new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(standplaats.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Indicatie getInOnderzoek()
	{
		return standplaats.getInOnderzoek();
	}

	@Override
	public Date getDocumentdatum()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATE_FORMAT).parse(standplaats.getBron().getDocumentdatum());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDocumentnummer()
	{
		return standplaats.getBron().getDocumentnummer();
	}

	@Override
	public long getHoofdAdres()
	{
		return Long.parseLong(standplaats.getGerelateerdeAdressen().getHoofdadres().getIdentificatie());
	}
	
	@Override
	public List<Long> getNevenAdressen()
	{
		List<Long> result = new ArrayList<Long>();
		for (Nevenadres nevenadres : standplaats.getGerelateerdeAdressen().getNevenadres())
			result.add(Long.parseLong(nevenadres.getIdentificatie()));
		return result;
	}
	
}
