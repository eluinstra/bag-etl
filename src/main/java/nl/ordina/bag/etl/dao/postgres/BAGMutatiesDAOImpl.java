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
package nl.ordina.bag.etl.dao.postgres;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import nl.kadaster.schemas.bag_verstrekkingen.extract_producten_lvc.v20090901.MutatieProduct;
import nl.ordina.bag.etl.dao.AbstractBAGMutatiesDAO;
import nl.ordina.bag.etl.dao.DAOException;
import nl.ordina.bag.etl.model.mutatie.BAGMutatie;
import nl.ordina.bag.etl.util.Utils;
import nl.ordina.bag.etl.xml.XMLMessageBuilder;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.support.TransactionTemplate;

public class BAGMutatiesDAOImpl extends AbstractBAGMutatiesDAO
{
	public class IdExtractor implements ResultSetExtractor<Long>
	{
		@Override
		public Long extractData(ResultSet rs) throws SQLException, DataAccessException
		{
			if (rs.next())
				return rs.getLong("id");
			else
				return null;
		}
	}

	public BAGMutatiesDAOImpl(TransactionTemplate transactionTemplate, JdbcTemplate jdbcTemplate)
	{
		super(transactionTemplate,jdbcTemplate);
	}

	@Override
	public boolean existsMutatiesFile(java.util.Date dateFrom) throws DAOException
	{
		try
		{
			return jdbcTemplate.queryForInt(
				"select count(id)" +
				" from bag_mutaties_file" +
				" where date_from = date_trunc('day', ?::timestamp)",
			dateFrom) > 0;
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public long insertMutatiesFile(final java.util.Date dateFrom, final java.util.Date dateTo, final byte[] content) throws DAOException
	{
		try
		{
			return jdbcTemplate.query(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"insert into bag_mutaties_file (" +
							" id," +
							" date_from," +
							" date_to," +
							" content" +
							") values ((select coalesce(max(id),0) + 1 from bag_mutaties_file),date_trunc('day', ?::timestamp),date_trunc('day', ?::timestamp),?)" +
							" returning id"
						);
						ps.setDate(1,new Date(dateFrom.getTime()));
						ps.setDate(2,new Date(dateTo.getTime()));
						ps.setBytes(3,content);
						return ps;
					}
				},
				new IdExtractor()
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public void insert(final BAGMutatie mutatie) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						try
						{
							PreparedStatement ps = connection.prepareStatement(
								"insert into bag_mutatie (" +
									"id," +
									"tijdstip_verwerking," +
									"volgnr_verwerking," +
									"object_type," +
									"mutatie_product" +
								") values ((select coalesce(max(id),0) + 1 from bag_mutatie),?,?,?,?)"
							);
							ps.setTimestamp(1,Utils.toTimestamp(mutatie.getTijdstipVerwerking()));
							ps.setLong(2,mutatie.getVolgnrVerwerking());
							ps.setInt(3,mutatie.getObjectType().ordinal());
							ps.setString(4,XMLMessageBuilder.getInstance(MutatieProduct.class).handle(new JAXBElement<MutatieProduct>(new QName("http://www.kadaster.nl/schemas/bag-verstrekkingen/extract-producten-lvc/v20090901","Mutatie-product"),MutatieProduct.class,mutatie.getMutatieProduct())));
							return ps;
						}
						catch (JAXBException e)
						{
							throw new DAOException(e);
						}
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

}
