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

import java.util.ArrayList;
import java.util.List;

import nl.kadaster.schemas.imbag.imbag_types.v20090901.StatusPlaats;

public class StandplaatsImpl extends AbstractBAGObject implements Standplaats
{
  StatusPlaats standplaatsStatus;
  String standplaatsGeometrie;
	long hoofdAdres;
	List<Long> nevenAdressen = new ArrayList<Long>();

	@Override
	public StatusPlaats getStandplaatsStatus()
	{
		return standplaatsStatus;
	}
	
	public void setStandplaatsStatus(StatusPlaats standplaatsStatus)
	{
		this.standplaatsStatus = standplaatsStatus;
	}

	@Override
	public String getStandplaatsGeometrie()
	{
		return standplaatsGeometrie;
	}
	
	public void setStandplaatsGeometrie(String standplaatsGeometrie)
	{
		this.standplaatsGeometrie = standplaatsGeometrie;
	}

	@Override
	public long getHoofdAdres()
	{
		return hoofdAdres;
	}
	
	public void setHoofdAdres(long hoofdAdres)
	{
		this.hoofdAdres = hoofdAdres;
	}

	@Override
	public List<Long> getNevenAdressen()
	{
		return nevenAdressen;
	}
	
	public void setNevenAdressen(List<Long> nevenAdressen)
	{
		this.nevenAdressen = nevenAdressen;
	}

}
