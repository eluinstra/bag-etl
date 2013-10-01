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

import nl.kadaster.schemas.imbag.imbag_types.v20090901.Gebruiksdoel;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.StatusVerblijfsobject;

public class VerblijfsobjectImpl extends AbstractBAGObject implements Verblijfsobject
{
	private String verblijfsobjectGeometrie;
	private List<Gebruiksdoel> gebruiksdoelVerblijfsobject = new ArrayList<Gebruiksdoel>();
	private int oppervlakteVerblijfsobject;
	private StatusVerblijfsobject verblijfsobjectStatus;
	private long hoofdAdres;
	private List<Long> nevenAdressen = new ArrayList<Long>();
	private List<Long> gerelateerdPand = new ArrayList<Long>();

	public String getVerblijfsobjectGeometrie()
  {
  	return verblijfsobjectGeometrie;
  }
	
	public void setVerblijfsobjectGeometrie(String verblijfsobjectGeometrie)
	{
		this.verblijfsobjectGeometrie = verblijfsobjectGeometrie;
	}

	public List<Gebruiksdoel> getGebruiksdoelenVerblijfsobject()
  {
  	return gebruiksdoelVerblijfsobject;
  }
	
	public void setGebruiksdoelenVerblijfsobject(List<Gebruiksdoel> gebruiksdoelVerblijfsobject)
	{
		this.gebruiksdoelVerblijfsobject = gebruiksdoelVerblijfsobject;
	}
  
  public int getOppervlakteVerblijfsobject()
  {
  	return oppervlakteVerblijfsobject;
  }
  
  public void setOppervlakteVerblijfsobject(int oppervlakteVerblijfsobject)
	{
		this.oppervlakteVerblijfsobject = oppervlakteVerblijfsobject;
	}
  
  public StatusVerblijfsobject getVerblijfsobjectStatus()
  {
  	return verblijfsobjectStatus;
  }
  
  public void setVerblijfsobjectStatus(StatusVerblijfsobject verblijfsobjectStatus)
	{
		this.verblijfsobjectStatus = verblijfsobjectStatus;
	}
 
  public long getHoofdAdres()
  {
  	return hoofdAdres;
  }
  
  public void setHoofdAdres(long hoofdAdres)
	{
		this.hoofdAdres = hoofdAdres;
	}
  
  public List<Long> getNevenAdressen()
  {
  	return nevenAdressen;
  }
  
  public void setNevenAdressen(List<Long> nevenAdressen)
	{
		this.nevenAdressen = nevenAdressen;
	}
  
  public List<Long> getGerelateerdPanden()
  {
  	return gerelateerdPand;
  }
  
  public void setGerelateerdPanden(List<Long> gerelateerdPand)
	{
		this.gerelateerdPand = gerelateerdPand;
	}
  
}
