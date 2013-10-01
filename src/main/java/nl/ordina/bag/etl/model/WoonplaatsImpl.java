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

import nl.kadaster.schemas.imbag.imbag_types.v20090901.StatusWoonplaats;

public class WoonplaatsImpl extends AbstractBAGObject implements Woonplaats
{
	private String woonplaatsNaam;
	private String woonplaatsGeometrie;
	private StatusWoonplaats woonplaatsStatus;
	
	@Override
	public String getWoonplaatsNaam()
	{
		return woonplaatsNaam;
	}
	
	public void setWoonplaatsNaam(String woonplaatsNaam)
	{
		this.woonplaatsNaam = woonplaatsNaam;
	}
	
	@Override
	public String getWoonplaatsGeometrie()
	{
		return woonplaatsGeometrie;
	}
	
	public void setWoonplaatsGeometrie(String woonplaatsGeometrie)
	{
		this.woonplaatsGeometrie = woonplaatsGeometrie;
	}
	
	@Override
	public StatusWoonplaats getWoonplaatsStatus()
	{
		return woonplaatsStatus;
	}
	
	public void setWoonplaatsStatus(StatusWoonplaats woonplaatsStatus)
	{
		this.woonplaatsStatus = woonplaatsStatus;
	}
}
