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

import java.util.Date;
import java.util.List;

import nl.ordina.bag.etl.Constants.ProcessingStatus;
import nl.ordina.bag.etl.model.mutatie.BAGMutatie;
import nl.ordina.bag.etl.model.mutatie.MutatiesFile;

import org.springframework.transaction.support.TransactionCallback;

public interface BAGMutatiesDAO
{
	boolean existsMutatiesFile(Date dateFrom) throws DAOException;
	long insertMutatiesFile(Date dateFrom, Date dateTo, byte[] content) throws DAOException;

	MutatiesFile getNexMutatiesFile() throws DAOException;
	void setMutatiesFileStatus(long id, ProcessingStatus status) throws DAOException;
	
	void doInTransaction(TransactionCallback<?> callback) throws DAOException;

	List<BAGMutatie> getNextBAGMutaties() throws DAOException;

	void insert(BAGMutatie mutatie) throws DAOException;

	void delete(BAGMutatie mutatie) throws DAOException;
}
