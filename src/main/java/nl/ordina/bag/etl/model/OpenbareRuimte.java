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

public interface OpenbareRuimte extends BAGObject
{
	String getOpenbareRuimteNaam();
	TypeOpenbareRuimte getOpenbareRuimteType();
	StatusNaamgeving getOpenbareruimteStatus();
	long getGerelateerdeWoonplaats();
}
