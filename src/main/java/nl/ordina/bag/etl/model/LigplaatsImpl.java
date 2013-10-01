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

public class LigplaatsImpl extends AbstractBAGObject implements Ligplaats
{
  private StatusPlaats ligplaatsStatus;
  private String ligplaatsGeometrie;
  private long hoofdAdres;
  private List<Long> nevenAdressen = new ArrayList<Long>();

	@Override
	public StatusPlaats getLigplaatsStatus()
	{
		return ligplaatsStatus;
	}
	
	public void setLigplaatsStatus(StatusPlaats ligplaatsStatus)
	{
		this.ligplaatsStatus = ligplaatsStatus;
	}

	@Override
	public String getLigplaatsGeometrie()
	{
		return ligplaatsGeometrie;
	}
	
	public void setLigplaatsGeometrie(String ligplaatsGeometrie)
	{
		this.ligplaatsGeometrie = ligplaatsGeometrie;
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
