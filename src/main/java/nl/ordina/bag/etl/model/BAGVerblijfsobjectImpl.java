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

import nl.kadaster.schemas.imbag.imbag_types.v20090901.Gebruiksdoel;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.Indicatie;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.StatusVerblijfsobject;
import nl.kadaster.schemas.imbag.lvc.v20090901.GerelateerdeAdressen.Nevenadres;
import nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject.GerelateerdPand;
import nl.ordina.bag.etl.Constants;
import nl.ordina.bag.etl.xml.BAGGeometrieHandler;

public class BAGVerblijfsobjectImpl implements Verblijfsobject
{
	private BAGGeometrieHandler geometrieHandler;
	private nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject verblijfsobject;

	protected BAGVerblijfsobjectImpl(BAGGeometrieHandler geometrieHandler, nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject verblijfsobject)
	{
		this.geometrieHandler = geometrieHandler;
		this.verblijfsobject = verblijfsobject;
	}

	@Override
	public long getIdentificatie()
	{
		return Long.parseLong(verblijfsobject.getIdentificatie());
	}

	@Override
	public Indicatie getAanduidingRecordInactief()
	{
		return verblijfsobject.getAanduidingRecordInactief();
	}

	@Override
	public long getAanduidingRecordCorrectie()
	{
		return verblijfsobject.getAanduidingRecordCorrectie().longValue();
	}

	@Override
	public Indicatie getOfficieel()
	{
		return verblijfsobject.getOfficieel();
	}

	@Override
	public String getVerblijfsobjectGeometrie()
	{
		return geometrieHandler.handle(verblijfsobject.getVerblijfsobjectGeometrie());
	}

	@Override
	public List<Gebruiksdoel> getGebruiksdoelenVerblijfsobject()
	{
		return verblijfsobject.getGebruiksdoelVerblijfsobject();
	}

	@Override
	public int getOppervlakteVerblijfsobject()
	{
		return verblijfsobject.getOppervlakteVerblijfsobject();
	}

	@Override
	public StatusVerblijfsobject getVerblijfsobjectStatus()
	{
		return verblijfsobject.getVerblijfsobjectStatus();
	}

	@Override
	public Date getBegindatumTijdvakGeldigheid()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(verblijfsobject.getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid());
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
			return verblijfsobject.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid() == null ? null : new SimpleDateFormat(Constants.BAG_DATETIME_FORMAT).parse(verblijfsobject.getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Indicatie getInOnderzoek()
	{
		return verblijfsobject.getInOnderzoek();
	}

	@Override
	public Date getDocumentdatum()
	{
		try
		{
			return new SimpleDateFormat(Constants.BAG_DATE_FORMAT).parse(verblijfsobject.getBron().getDocumentdatum());
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDocumentnummer()
	{
		return verblijfsobject.getBron().getDocumentnummer();
	}

	@Override
	public long getHoofdAdres()
	{
		return Long.parseLong(verblijfsobject.getGerelateerdeAdressen().getHoofdadres().getIdentificatie());
	}
	
	@Override
	public List<Long> getNevenAdressen()
	{
		List<Long> result = new ArrayList<Long>();
		for (Nevenadres nevenadres : verblijfsobject.getGerelateerdeAdressen().getNevenadres())
			result.add(Long.parseLong(nevenadres.getIdentificatie()));
		return result;
	}
	
	@Override
	public List<Long> getGerelateerdPanden()
	{
		List<Long> result = new ArrayList<Long>();
		for (GerelateerdPand pand : verblijfsobject.getGerelateerdPand())
			result.add(Long.parseLong(pand.getIdentificatie()));
		return result;
	}
	
	public nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject getVerblijfsobject()
	{
		return verblijfsobject;
	}

}
