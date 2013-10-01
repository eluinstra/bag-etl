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

import nl.ordina.bag.etl.xml.BAGGeometrieHandler;

public class BAGObjectFactory
{
	private BAGGeometrieHandler bagGeometrieHandler;

	public BAGObjectFactory()
	{
	}

	public BAGObjectFactory(BAGGeometrieHandler bagGeometrieHandler)
	{
		this.bagGeometrieHandler = bagGeometrieHandler;
	}

	public BAGWoonplaatsImpl getWoonplaats(nl.kadaster.schemas.imbag.lvc.v20090901.Woonplaats woonplaats)
	{
		return new BAGWoonplaatsImpl(bagGeometrieHandler,woonplaats);
	}

	public BAGOpenbareRuimteImpl getOpenbareRuimte(nl.kadaster.schemas.imbag.lvc.v20090901.OpenbareRuimte openbareRuimte)
	{
		return new BAGOpenbareRuimteImpl(openbareRuimte);
	}

	public BAGNummeraanduidingImpl getNummeraanduiding(nl.kadaster.schemas.imbag.lvc.v20090901.Nummeraanduiding nummeraanduiding)
	{
		return new BAGNummeraanduidingImpl(nummeraanduiding);
	}

	public BAGPandImpl getPand(nl.kadaster.schemas.imbag.lvc.v20090901.Pand pand)
	{
		return new BAGPandImpl(bagGeometrieHandler,pand);
	}

	public BAGVerblijfsobjectImpl getVerblijfsobject(nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject verblijfsobject)
	{
		return new BAGVerblijfsobjectImpl(bagGeometrieHandler,verblijfsobject);
	}

	public BAGLigplaatsImpl getLigplaats(nl.kadaster.schemas.imbag.lvc.v20090901.Ligplaats ligplaats)
	{
		return new BAGLigplaatsImpl(bagGeometrieHandler,ligplaats);
	}

	public BAGStandplaatsImpl getStandplaats(nl.kadaster.schemas.imbag.lvc.v20090901.Standplaats standplaats)
	{
		return new BAGStandplaatsImpl(bagGeometrieHandler,standplaats);
	}

	public void setBagGeometrieHandler(BAGGeometrieHandler bagGeometrieHandler)
	{
		this.bagGeometrieHandler = bagGeometrieHandler;
	}
}
