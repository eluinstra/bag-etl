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
import nl.kadaster.schemas.imbag.imbag_types.v20090901.TypeOpenbareRuimte;

public class OpenbareRuimteImpl extends AbstractBAGObject implements OpenbareRuimte
{
	private String openbareRuimteNaam;
	private TypeOpenbareRuimte openbareRuimteType;
	private StatusNaamgeving openbareruimteStatus;
	private long gerelateerdeWoonplaats;
	private String verkorteOpenbareRuimteNaam;

	public String getOpenbareRuimteNaam()
	{
		return openbareRuimteNaam;
	}
	
	public void setOpenbareRuimteNaam(String openbareRuimteNaam)
	{
		this.openbareRuimteNaam = openbareRuimteNaam;
	}

	public TypeOpenbareRuimte getOpenbareRuimteType()
	{
		return openbareRuimteType;
	}
	
	public void setOpenbareRuimteType(TypeOpenbareRuimte openbareRuimteType)
	{
		this.openbareRuimteType = openbareRuimteType;
	}

	public StatusNaamgeving getOpenbareruimteStatus()
	{
		return openbareruimteStatus;
	}
	
	public void setOpenbareruimteStatus(StatusNaamgeving openbareruimteStatus)
	{
		this.openbareruimteStatus = openbareruimteStatus;
	}

	public long getGerelateerdeWoonplaats()
	{
		return gerelateerdeWoonplaats;
	}
	
	public void setGerelateerdeWoonplaats(long gerelateerdeWoonplaats)
	{
		this.gerelateerdeWoonplaats = gerelateerdeWoonplaats;
	}

	public String getVerkorteOpenbareRuimteNaam()
	{
		return verkorteOpenbareRuimteNaam;
	}

	public void setVerkorteOpenbareRuimteNaam(String verkorteOpenbareRuimteNaam)
	{
		this.verkorteOpenbareRuimteNaam = verkorteOpenbareRuimteNaam;
	}
}
