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
package nl.ordina.bag.etl.dao;

import nl.ordina.bag.etl.Constants.BAGObjectType;
import nl.ordina.bag.etl.model.Ligplaats;
import nl.ordina.bag.etl.model.Nummeraanduiding;
import nl.ordina.bag.etl.model.OpenbareRuimte;
import nl.ordina.bag.etl.model.Pand;
import nl.ordina.bag.etl.model.Standplaats;
import nl.ordina.bag.etl.model.Verblijfsobject;
import nl.ordina.bag.etl.model.Woonplaats;

public interface BAGDAO
{
	long getCount(BAGObjectType bagObjectType) throws DAOException;

	boolean exists(Woonplaats woonplaats) throws DAOException;
	boolean exists(OpenbareRuimte openbareRuimte) throws DAOException;
	boolean exists(Nummeraanduiding nummeraanduiding) throws DAOException;
	boolean exists(Pand pand) throws DAOException;
	boolean exists(Verblijfsobject verblijfsobject) throws DAOException;
	boolean exists(Ligplaats ligplaats) throws DAOException;
	boolean exists(Standplaats standplaats) throws DAOException;
	
	void insert(Woonplaats woonplaats) throws DAOException;
	void insert(OpenbareRuimte openbareRuimte) throws DAOException;
	void insert(Nummeraanduiding nummeraanduiding) throws DAOException;
	void insert(Pand pand) throws DAOException;
	void insert(Verblijfsobject verblijfsobject) throws DAOException;
	void insert(Ligplaats ligplaats) throws DAOException;
	void insert(Standplaats standplaats) throws DAOException;

	void update(Woonplaats woonplaats) throws DAOException;
	void update(OpenbareRuimte openbareRuimte) throws DAOException;
	void update(Nummeraanduiding nummeraanduiding) throws DAOException;
	void update(Pand pand) throws DAOException;
	void update(Verblijfsobject verblijfsobject) throws DAOException;
	void update(Ligplaats ligplaats) throws DAOException;
	void update(Standplaats standplaats) throws DAOException;

	void update(Woonplaats origineel, Woonplaats wijziging) throws DAOException;
	void update(OpenbareRuimte origineel, OpenbareRuimte wijziging) throws DAOException;
	void update(Nummeraanduiding origineel, Nummeraanduiding wijziging) throws DAOException;
	void update(Pand origineel, Pand wijziging) throws DAOException;
	void update(Verblijfsobject origineel, Verblijfsobject wijziging) throws DAOException;
	void update(Ligplaats origineel, Ligplaats wijziging) throws DAOException;
	void update(Standplaats origineel, Standplaats wijziging) throws DAOException;

	void delete(Woonplaats woonplaats) throws DAOException;
	void delete(OpenbareRuimte openbareRuimte) throws DAOException;
	void delete(Nummeraanduiding nummeraanduiding) throws DAOException;
	void delete(Pand pand) throws DAOException;
	void delete(Verblijfsobject verblijfsobject) throws DAOException;
	void delete(Ligplaats ligplaats) throws DAOException;
	void delete(Standplaats standplaats) throws DAOException;
}
