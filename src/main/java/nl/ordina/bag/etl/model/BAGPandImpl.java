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
import nl.ordina.bag.etl.Constants;
import nl.ordina.bag.etl.xml.BAGGeometrieHandler;

public class BAGPandImpl implements Pand
{
	private BAGGeometrieHandler geometrieHandler;
	private nl.kadaster.schemas.imbag.lvc.v20090901.Pand pand;

	protected BAGPandImpl(BAGGeometrieHandler geometrieHandler, nl.kadaster.schemas.imbag.lvc.v20090901.Pand pand)
	{
		this.geometrieHandler = geometrieHandler;
		this.pand = pand;
	}

	@Override
	public long getIdentificatie()
	{
		return Long.parseLong(pand.getIdentificatie());
	}

	@Override
	public Indicatie getAanduidingRecordInactief()
	{
		return pand.getAanduidingRecordInactief();
	}

	@Override
	public long getAanduidingRecordCorrectie()
	{
		return pand.getAanduidingRecordCorrectie().longValue();
	}

	@Override
	public Indicatie getOfficieel()
	{
		return pand.getOfficieel();
	}

	@Override
	public String getPandGeometrie()
	{
		return geometrieHandler.handle(pand.getPandGeometrie());
	}

	@Override
	public int getBouwjaar()
	{
		return pand.getBouwjaar();
	}

	@Override
	public String getPandStatus()
	{
		return pand.getPandstatus();
	}

	@Override
	public Date getBegindatumTijdvakGeldigheid()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(pand.getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid());
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
			return pand.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid() == null ? null : new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(pand.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Indicatie getInOnderzoek()
	{
		return pand.getInOnderzoek();
	}

	@Override
	public Date getDocumentdatum()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATE_FORMAT).parse(pand.getBron().getDocumentdatum());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDocumentnummer()
	{
		return pand.getBron().getDocumentnummer();
	}

}
