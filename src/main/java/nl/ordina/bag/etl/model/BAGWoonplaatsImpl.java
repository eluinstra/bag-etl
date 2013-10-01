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
import java.util.Date;

import nl.kadaster.schemas.imbag.imbag_types.v20090901.Indicatie;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.StatusWoonplaats;
import nl.ordina.bag.etl.Constants;
import nl.ordina.bag.etl.xml.BAGGeometrieHandler;

public class BAGWoonplaatsImpl implements Woonplaats
{
	private BAGGeometrieHandler geometrieHandler;
	private nl.kadaster.schemas.imbag.lvc.v20090901.Woonplaats woonplaats;

	protected BAGWoonplaatsImpl(BAGGeometrieHandler geometrieHandler, nl.kadaster.schemas.imbag.lvc.v20090901.Woonplaats woonplaats)
	{
		this.geometrieHandler = geometrieHandler;
		this.woonplaats = woonplaats;
	}

	@Override
	public long getIdentificatie()
	{
		return Long.parseLong(woonplaats.getIdentificatie());
	}

	@Override
	public Indicatie getAanduidingRecordInactief()
	{
		return woonplaats.getAanduidingRecordInactief();
	}

	@Override
	public long getAanduidingRecordCorrectie()
	{
		return woonplaats.getAanduidingRecordCorrectie().longValue();
	}

	@Override
	public String getWoonplaatsNaam()
	{
		return woonplaats.getWoonplaatsNaam();
	}

	@Override
	public String getWoonplaatsGeometrie()
	{
		return geometrieHandler.handle(woonplaats.getWoonplaatsGeometrie());
	}

	@Override
	public Indicatie getOfficieel()
	{
		return woonplaats.getOfficieel();
	}

	@Override
	public Date getBegindatumTijdvakGeldigheid()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(woonplaats.getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid());
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
			return woonplaats.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid() == null ? null : new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(woonplaats.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Indicatie getInOnderzoek()
	{
		return woonplaats.getInOnderzoek();
	}

	@Override
	public Date getDocumentdatum()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATE_FORMAT).parse(woonplaats.getBron().getDocumentdatum());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDocumentnummer()
	{
		return woonplaats.getBron().getDocumentnummer();
	}

	@Override
	public StatusWoonplaats getWoonplaatsStatus()
	{
		return woonplaats.getWoonplaatsStatus();
	}

}
