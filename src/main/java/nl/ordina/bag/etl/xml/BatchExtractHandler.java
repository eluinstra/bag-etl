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
package nl.ordina.bag.etl.xml;

import java.util.List;

import nl.kadaster.schemas.imbag.lvc.v20090901.Ligplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Nummeraanduiding;
import nl.kadaster.schemas.imbag.lvc.v20090901.OpenbareRuimte;
import nl.kadaster.schemas.imbag.lvc.v20090901.Pand;
import nl.kadaster.schemas.imbag.lvc.v20090901.Standplaats;
import nl.kadaster.schemas.imbag.lvc.v20090901.Verblijfsobject;
import nl.kadaster.schemas.imbag.lvc.v20090901.Woonplaats;

public interface BatchExtractHandler
{
	void handleWoonplaatsen(List<Woonplaats> woonplaatsen) throws HandlerException;
	void handleOpenbareRuimten(List<OpenbareRuimte> openbareRuimten) throws HandlerException;
	void handleNummeraanduidingen(List<Nummeraanduiding> nummeraanduidingen) throws HandlerException;
	void handlePanden(List<Pand> panden) throws HandlerException;
	void handleVerblijfsobjecten(List<Verblijfsobject> verblijfsobjecten) throws HandlerException;
	void handleLigplaatsen(List<Ligplaats> ligplaatsen) throws HandlerException;
	void handleStandplaatsen(List<Standplaats> standplaatsen) throws HandlerException;
}
