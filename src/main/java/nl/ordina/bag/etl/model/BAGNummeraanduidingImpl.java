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
import nl.kadaster.schemas.imbag.imbag_types.v20090901.StatusNaamgeving;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.TypeAdresseerbaarObject;
import nl.ordina.bag.etl.Constants;

public class BAGNummeraanduidingImpl implements Nummeraanduiding
{
	private nl.kadaster.schemas.imbag.lvc.v20090901.Nummeraanduiding nummeraanduiding;

	protected BAGNummeraanduidingImpl(nl.kadaster.schemas.imbag.lvc.v20090901.Nummeraanduiding nummeraanduiding)
	{
		this.nummeraanduiding = nummeraanduiding;
	}

	@Override
	public long getIdentificatie()
	{
		return Long.parseLong(nummeraanduiding.getIdentificatie());
	}

	@Override
	public Indicatie getAanduidingRecordInactief()
	{
		return nummeraanduiding.getAanduidingRecordInactief();
	}

	@Override
	public long getAanduidingRecordCorrectie()
	{
		return nummeraanduiding.getAanduidingRecordCorrectie().longValue();
	}

	@Override
	public int getHuisnummer()
	{
		return nummeraanduiding.getHuisnummer();
  }

	@Override
	public Indicatie getOfficieel()
	{
		return nummeraanduiding.getOfficieel();
	}
	
	@Override
	public String getHuisletter()
	{
		return nummeraanduiding.getHuisletter();
	}

	@Override
	public String getHuisnummertoevoeging()
	{
		return nummeraanduiding.getHuisnummertoevoeging();
	}

	@Override
	public String getPostcode()
	{
		return nummeraanduiding.getPostcode();
	}

	@Override
	public Date getBegindatumTijdvakGeldigheid()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(nummeraanduiding.getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid());
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
			return nummeraanduiding.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid() == null ? null : new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(nummeraanduiding.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Indicatie getInOnderzoek()
	{
		return nummeraanduiding.getInOnderzoek();
	}

	@Override
	public TypeAdresseerbaarObject getTypeAdresseerbaarObject()
	{
		return nummeraanduiding.getTypeAdresseerbaarObject();
	}

	@Override
	public Date getDocumentdatum()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATE_FORMAT).parse(nummeraanduiding.getBron().getDocumentdatum());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDocumentnummer()
	{
		return nummeraanduiding.getBron().getDocumentnummer();
	}

	@Override
	public StatusNaamgeving getNummeraanduidingStatus()
	{
		return nummeraanduiding.getNummeraanduidingStatus();
	}

	@Override
	public long getGerelateerdeOpenbareRuimte()
	{
		return Long.parseLong(nummeraanduiding.getGerelateerdeOpenbareRuimte().getIdentificatie());
	}

	@Override
	public Long getGerelateerdeWoonplaats()
	{
		return nummeraanduiding.getGerelateerdeWoonplaats() == null ? null : Long.parseLong(nummeraanduiding.getGerelateerdeWoonplaats().getIdentificatie());
	}

}
