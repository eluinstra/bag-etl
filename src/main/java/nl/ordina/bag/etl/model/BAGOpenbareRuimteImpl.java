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
import nl.kadaster.schemas.imbag.imbag_types.v20090901.TypeOpenbareRuimte;
import nl.ordina.bag.etl.Constants;

public class BAGOpenbareRuimteImpl implements OpenbareRuimte
{
	private nl.kadaster.schemas.imbag.lvc.v20090901.OpenbareRuimte openbareRuimte;

	protected BAGOpenbareRuimteImpl(nl.kadaster.schemas.imbag.lvc.v20090901.OpenbareRuimte openbareRuimte)
	{
		this.openbareRuimte = openbareRuimte;
	}

	@Override
	public long getIdentificatie()
	{
		return Long.parseLong(openbareRuimte.getIdentificatie());
	}

	@Override
	public Indicatie getAanduidingRecordInactief()
	{
		return openbareRuimte.getAanduidingRecordInactief();
	}

	@Override
	public long getAanduidingRecordCorrectie()
	{
		return openbareRuimte.getAanduidingRecordCorrectie().longValue();
	}

	@Override
	public String getOpenbareRuimteNaam()
	{
		return openbareRuimte.getOpenbareRuimteNaam();
	}

	@Override
	public Indicatie getOfficieel()
	{
		return openbareRuimte.getOfficieel();
	}

	@Override
	public Date getBegindatumTijdvakGeldigheid()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(openbareRuimte.getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid());
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
			return openbareRuimte.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid() == null ? null : new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(openbareRuimte.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Indicatie getInOnderzoek()
	{
		return openbareRuimte.getInOnderzoek();
	}

	@Override
	public TypeOpenbareRuimte getOpenbareRuimteType()
	{
		return openbareRuimte.getOpenbareRuimteType();
	}

	@Override
	public Date getDocumentdatum()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATE_FORMAT).parse(openbareRuimte.getBron().getDocumentdatum());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDocumentnummer()
	{
		return openbareRuimte.getBron().getDocumentnummer();
	}

	@Override
	public StatusNaamgeving getOpenbareruimteStatus()
	{
		return openbareRuimte.getOpenbareruimteStatus();
	}

	@Override
	public long getGerelateerdeWoonplaats()
	{
		return Long.parseLong(openbareRuimte.getGerelateerdeWoonplaats().getIdentificatie());
	}

	@Override
	public String getVerkorteOpenbareRuimteNaam()
	{
		return openbareRuimte.getVerkorteOpenbareruimteNaam();
	}

}
