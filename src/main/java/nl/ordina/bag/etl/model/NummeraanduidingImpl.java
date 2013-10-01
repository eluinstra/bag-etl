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

import nl.kadaster.schemas.imbag.imbag_types.v20090901.StatusNaamgeving;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.TypeAdresseerbaarObject;

public class NummeraanduidingImpl extends AbstractBAGObject implements Nummeraanduiding
{
	private int huisnummer;
	private String huisletter;
	private String huisnummertoevoeging;
	private String postcode;
	private TypeAdresseerbaarObject typeAdresseerbaarObject;
	private StatusNaamgeving nummeraanduidingStatus;
	private long gerelateerdeOpenbareRuimte;
	private Long gerelateerdeWoonplaats;

	@Override
	public int getHuisnummer()
	{
		return huisnummer;
	}
	
	public void setHuisnummer(int huisnummer)
	{
		this.huisnummer = huisnummer;
	}

	@Override
	public String getHuisletter()
	{
		return huisletter;
	}
	
	public void setHuisletter(String huisletter)
	{
		this.huisletter = huisletter;
	}

	@Override
	public String getHuisnummertoevoeging()
	{
		return huisnummertoevoeging;
	}
	
	public void setHuisnummertoevoeging(String huisnummertoevoeging)
	{
		this.huisnummertoevoeging = huisnummertoevoeging;
	}

	@Override
	public String getPostcode()
	{
		return postcode;
	}
	
	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	@Override
	public TypeAdresseerbaarObject getTypeAdresseerbaarObject()
	{
		return typeAdresseerbaarObject;
	}
	
	public void setTypeAdresseerbaarObject(TypeAdresseerbaarObject typeAdresseerbaarObject)
	{
		this.typeAdresseerbaarObject = typeAdresseerbaarObject;
	}

	@Override
	public StatusNaamgeving getNummeraanduidingStatus()
	{
		return nummeraanduidingStatus;
	}
	
	public void setNummeraanduidingStatus(StatusNaamgeving nummeraanduidingStatus)
	{
		this.nummeraanduidingStatus = nummeraanduidingStatus;
	}

	@Override
	public long getGerelateerdeOpenbareRuimte()
	{
		return gerelateerdeOpenbareRuimte;
	}
	
	public void setGerelateerdeOpenbareRuimte(long gerelateerdeOpenbareRuimte)
	{
		this.gerelateerdeOpenbareRuimte = gerelateerdeOpenbareRuimte;
	}

	@Override
	public Long getGerelateerdeWoonplaats()
	{
		return gerelateerdeWoonplaats;
	}
	
	public void setGerelateerdeWoonplaats(Long gerelateerdeWoonplaats)
	{
		this.gerelateerdeWoonplaats = gerelateerdeWoonplaats;
	}

}
