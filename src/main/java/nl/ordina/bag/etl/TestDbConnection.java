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
package nl.ordina.bag.etl;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import nl.ordina.bag.etl.util.ServiceLocator;
import nl.ordina.bag.etl.util.Utils;

public class TestDbConnection
{
	public static Log logger = LogFactory.getLog(TestDbConnection.class);

	public static void main(String[] args) throws SQLException
	{
		if (args.length == 0)
		{
			logger.info("TestDbConnection started");
			ServiceLocator serviceLocator = ServiceLocator.getInstance("nl/ordina/bag/etl/applicationConfig.xml","nl/ordina/bag/etl/datasource.xml","nl/ordina/bag/etl/dao.xml");
			DataSource dataSource = (DataSource)serviceLocator.get("bagDataSource");
			Utils.testDbConnection(dataSource);
			logger.info("TestDbConnection finised");
		}
		else
			System.out.println("Usage: nl.ordina.bag.etl.TestDbConnection");
		System.exit(0);
	}
}
