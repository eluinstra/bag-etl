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

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class AbstractDAOFactory<T> implements FactoryBean<T>
{
	protected DataSource dataSource;
	protected TransactionTemplate transactionTemplate;
	protected JdbcTemplate jdbcTemplate;

	@Override
	public T getObject() throws Exception
	{
		if ("org.hsqldb.jdbcDriver".equals(((ComboPooledDataSource)dataSource).getDriverClass()))
			return createHsqldbDAO();
		else if ("com.mysql.jdbc.Driver".equals(((ComboPooledDataSource)dataSource).getDriverClass()))
			return createMysqlDAO();
		else if ("org.postgresql.Driver".equals(((ComboPooledDataSource)dataSource).getDriverClass()))
			return createPostgresDAO();
		else if ("oracle.jdbc.OracleDriver".equals(((ComboPooledDataSource)dataSource).getDriverClass()))
			return createOracleDAO();
		else if ("net.sourceforge.jtds.jdbc.Driver".equals(((ComboPooledDataSource)dataSource).getDriverClass()))
			return createMssqlDAO();
		else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(((ComboPooledDataSource)dataSource).getDriverClass()))
			return createMssqlDAO();
		throw new RuntimeException("SQL Driver " + ((ComboPooledDataSource)dataSource).getDriverClass() + " not recognized!");
	}

	@Override
	public abstract Class<T> getObjectType();

	@Override
	public boolean isSingleton()
	{
		return true;
	}

	public abstract T createHsqldbDAO();

	public abstract T createMysqlDAO();

	public abstract T createOracleDAO();

	public abstract T createPostgresDAO();

	public abstract T createMssqlDAO();

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate)
	{
		this.transactionTemplate = transactionTemplate;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	public static abstract class DefaultDAOFactory<U> extends AbstractDAOFactory<U>
	{
		@Override
		public U createHsqldbDAO()
		{
			throw new RuntimeException("HSQLDB not supported!");
		}

		@Override
		public U createMysqlDAO()
		{
			throw new RuntimeException("MySQL not supported!");
		}

		@Override
		public U createOracleDAO()
		{
			throw new RuntimeException("Oracle not supported!");
		}

		@Override
		public U createPostgresDAO()
		{
			throw new RuntimeException("Postgres not supported!");
		}

		@Override
		public U createMssqlDAO()
		{
			throw new RuntimeException("MSSQL not supported!");
		}
	}

}
