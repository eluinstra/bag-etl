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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import nl.kadaster.schemas.imbag.imbag_types.v20090901.TypeAdresseerbaarObject;
import nl.ordina.bag.etl.Constants.BAGObjectType;
import nl.ordina.bag.etl.model.BAGAdresseerbaarObject;
import nl.ordina.bag.etl.model.Gebruiksdoel;
import nl.ordina.bag.etl.model.GerelateerdPand;
import nl.ordina.bag.etl.model.Ligplaats;
import nl.ordina.bag.etl.model.NevenAdres;
import nl.ordina.bag.etl.model.Nummeraanduiding;
import nl.ordina.bag.etl.model.OpenbareRuimte;
import nl.ordina.bag.etl.model.Pand;
import nl.ordina.bag.etl.model.Standplaats;
import nl.ordina.bag.etl.model.Verblijfsobject;
import nl.ordina.bag.etl.model.Woonplaats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class AbstractBAGDAO implements BAGDAO
{
	protected transient Log logger = LogFactory.getLog(this.getClass());
	protected TransactionTemplate transactionTemplate;
	protected JdbcTemplate jdbcTemplate;

	public AbstractBAGDAO(TransactionTemplate transactionTemplate, JdbcTemplate jdbcTemplate)
	{
		this.transactionTemplate = transactionTemplate;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public long getCount(BAGObjectType bagObjectType)
	{
		try
		{
			if (BAGObjectType.WOONPLAATS.equals(bagObjectType))
				return jdbcTemplate.queryForLong("select count(*) from bag_woonplaats");
			if (BAGObjectType.OPENBARE_RUIMTE.equals(bagObjectType))
				return jdbcTemplate.queryForLong("select count(*) from bag_openbare_ruimte");
			if (BAGObjectType.NUMMERAANDUIDING.equals(bagObjectType))
				return jdbcTemplate.queryForLong("select count(*) from bag_nummeraanduiding");
			if (BAGObjectType.PAND.equals(bagObjectType))
				return jdbcTemplate.queryForLong("select count(*) from bag_pand");
			if (BAGObjectType.VERBLIJFSOBJECT.equals(bagObjectType))
				return jdbcTemplate.queryForLong("select count(*) from bag_verblijfsobject");
			if (BAGObjectType.LIGPLAATS.equals(bagObjectType))
				return jdbcTemplate.queryForLong("select count(*) from bag_ligplaats");
			if (BAGObjectType.STANDPLAATS.equals(bagObjectType))
				return jdbcTemplate.queryForLong("select count(*) from bag_standplaats");
			return 0;
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public boolean exists(Woonplaats woonplaats)
	{
		try
		{
			return jdbcTemplate.queryForInt(
				"select count(bag_woonplaats_id)" +
				" from bag_woonplaats" +
				" where bag_woonplaats_id = ?" +
				" and aanduiding_record_correctie = ?" +
				" and begindatum_tijdvak_geldigheid = ?",
				woonplaats.getIdentificatie(),
				woonplaats.getAanduidingRecordCorrectie(),
				woonplaats.getBegindatumTijdvakGeldigheid()
			) > 0;
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public boolean exists(OpenbareRuimte openbareRuimte)
	{
		try
		{
			return jdbcTemplate.queryForInt(
				"select count(bag_openbare_ruimte_id)" +
				" from bag_openbare_ruimte" +
				" where bag_openbare_ruimte_id = ?" +
				" and aanduiding_record_correctie = ?" +
				" and begindatum_tijdvak_geldigheid = ?",
				openbareRuimte.getIdentificatie(),
				openbareRuimte.getAanduidingRecordCorrectie(),
				openbareRuimte.getBegindatumTijdvakGeldigheid()
			) > 0;
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public boolean exists(Nummeraanduiding nummeraanduiding)
	{
		try
		{
			return jdbcTemplate.queryForInt(
				"select count(bag_nummeraanduiding_id)" +
				" from bag_nummeraanduiding" +
				" where bag_nummeraanduiding_id = ?" +
				" and aanduiding_record_correctie = ?" +
				" and begindatum_tijdvak_geldigheid = ?",
				nummeraanduiding.getIdentificatie(),
				nummeraanduiding.getAanduidingRecordCorrectie(),
				nummeraanduiding.getBegindatumTijdvakGeldigheid()
			) > 0;
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public boolean exists(Pand pand)
	{
		try
		{
			return jdbcTemplate.queryForInt(
				"select count(bag_pand_id)" +
				" from bag_pand" +
				" where bag_pand_id = ?" +
				" and aanduiding_record_correctie = ?" +
				" and begindatum_tijdvak_geldigheid = ?",
				pand.getIdentificatie(),
				pand.getAanduidingRecordCorrectie(),
				pand.getBegindatumTijdvakGeldigheid()
			) > 0;
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public boolean exists(Verblijfsobject verblijfsobject)
	{
		try
		{
			return jdbcTemplate.queryForInt(
				"select count(bag_verblijfsobject_id)" +
				" from bag_verblijfsobject" +
				" where bag_verblijfsobject_id = ?" +
				" and aanduiding_record_correctie = ?" +
				" and begindatum_tijdvak_geldigheid = ?",
				verblijfsobject.getIdentificatie(),
				verblijfsobject.getAanduidingRecordCorrectie(),
				verblijfsobject.getBegindatumTijdvakGeldigheid()
			) > 0;
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public boolean exists(Ligplaats ligplaats)
	{
		try
		{
			return jdbcTemplate.queryForInt(
				"select count(bag_ligplaats_id)" +
				" from bag_ligplaats" +
				" where bag_ligplaats_id = ?" +
				" and aanduiding_record_correctie = ?" +
				" and begindatum_tijdvak_geldigheid = ?",
				ligplaats.getIdentificatie(),
				ligplaats.getAanduidingRecordCorrectie(),
				ligplaats.getBegindatumTijdvakGeldigheid()
			) > 0;
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public boolean exists(Standplaats standplaats)
	{
		try
		{
			return jdbcTemplate.queryForInt(
				"select count(bag_standplaats_id)" +
				" from bag_standplaats" +
				" where bag_standplaats_id = ?" +
				" and aanduiding_record_correctie = ?" +
				" and begindatum_tijdvak_geldigheid = ?",
				standplaats.getIdentificatie(),
				standplaats.getAanduidingRecordCorrectie(),
				standplaats.getBegindatumTijdvakGeldigheid()
			) > 0;
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public void insert(final Woonplaats woonplaats) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"insert into bag_woonplaats (" +
								"bag_woonplaats_id," +
								"aanduiding_record_inactief," +
								"aanduiding_record_correctie," +
								"woonplaats_naam," +
								"woonplaats_geometrie," +
								"officieel," +
								"begindatum_tijdvak_geldigheid," +
								"einddatum_tijdvak_geldigheid," +
								"in_onderzoek," +
								"bron_documentdatum," +
								"bron_documentnummer," +
								"woonplaats_status" +
							") values (?,?,?,?,?,?,?,?,?,?,?,?)"
						);
						ps.setLong(1,woonplaats.getIdentificatie());
						ps.setInt(2,woonplaats.getAanduidingRecordInactief().ordinal());
						ps.setLong(3,woonplaats.getAanduidingRecordCorrectie());
						ps.setString(4,woonplaats.getWoonplaatsNaam());
						ps.setString(5,woonplaats.getWoonplaatsGeometrie());
						ps.setInt(6,woonplaats.getOfficieel().ordinal());
						ps.setTimestamp(7,new Timestamp(woonplaats.getBegindatumTijdvakGeldigheid().getTime()));
						if (woonplaats.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(8,Types.TIMESTAMP);
						else
							ps.setTimestamp(8,new Timestamp(woonplaats.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(9,woonplaats.getInOnderzoek().ordinal());
						ps.setDate(10,new Date(woonplaats.getDocumentdatum().getTime()));
						ps.setString(11,woonplaats.getDocumentnummer());
						ps.setInt(12,woonplaats.getWoonplaatsStatus().ordinal());
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting woonplaats: " + woonplaats.getIdentificatie(),e);
		}
	}

	@Override
	public void insert(final OpenbareRuimte openbareRuimte) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"insert into bag_openbare_ruimte (" +
								"bag_openbare_ruimte_id," +
								"aanduiding_record_inactief," +
								"aanduiding_record_correctie," +
								"openbare_ruimte_naam," +
								"officieel," +
								"begindatum_tijdvak_geldigheid," +
								"einddatum_tijdvak_geldigheid," +
								"in_onderzoek," +
								"openbare_ruimte_type," +
								"bron_documentdatum," +
								"bron_documentnummer," +
								"openbareruimte_status," +
								"bag_woonplaats_id," +
								"verkorte_openbare_ruimte_naam" +
							") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
						);
						ps.setLong(1,openbareRuimte.getIdentificatie());
						ps.setInt(2,openbareRuimte.getAanduidingRecordInactief().ordinal());
						ps.setLong(3,openbareRuimte.getAanduidingRecordCorrectie());
						ps.setString(4,openbareRuimte.getOpenbareRuimteNaam());
						ps.setInt(5,openbareRuimte.getOfficieel().ordinal());
						ps.setTimestamp(6,new Timestamp(openbareRuimte.getBegindatumTijdvakGeldigheid().getTime()));
						if (openbareRuimte.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(7,Types.TIMESTAMP);
						else
							ps.setTimestamp(7,new Timestamp(openbareRuimte.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(8,openbareRuimte.getInOnderzoek().ordinal());
						ps.setInt(9,openbareRuimte.getOpenbareRuimteType().ordinal());
						ps.setDate(10,new Date(openbareRuimte.getDocumentdatum().getTime()));
						ps.setString(11,openbareRuimte.getDocumentnummer());
						ps.setInt(12,openbareRuimte.getOpenbareruimteStatus().ordinal());
						ps.setLong(13,openbareRuimte.getGerelateerdeWoonplaats());
						ps.setString(14,openbareRuimte.getVerkorteOpenbareRuimteNaam());
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting openbare ruimte: " + openbareRuimte.getIdentificatie(),e);
		}
	}

	@Override
	public void insert(final Nummeraanduiding nummeraanduiding) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"insert into bag_nummeraanduiding (" +
								"bag_nummeraanduiding_id," +
								"aanduiding_record_inactief," +
								"aanduiding_record_correctie," +
								"huisnummer," +
								"officieel," +
								"huisletter," +
								"huisnummertoevoeging," +
								"postcode," +
								"begindatum_tijdvak_geldigheid," +
								"einddatum_tijdvak_geldigheid," +
								"in_onderzoek," +
								"type_adresseerbaar_object," +
								"bron_documentdatum," +
								"bron_documentnummer," +
								"nummeraanduiding_status," +
								"bag_woonplaats_id," +
								"bag_openbare_ruimte_id" +
							") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
						);
						ps.setLong(1,nummeraanduiding.getIdentificatie());
						ps.setInt(2,nummeraanduiding.getAanduidingRecordInactief().ordinal());
						ps.setLong(3,nummeraanduiding.getAanduidingRecordCorrectie());
						ps.setInt(4,nummeraanduiding.getHuisnummer());
						ps.setInt(5,nummeraanduiding.getOfficieel().ordinal());
						if (nummeraanduiding.getHuisletter() == null)
							ps.setNull(6,Types.VARCHAR);
						else
							ps.setString(6,nummeraanduiding.getHuisletter());
						if (nummeraanduiding.getHuisnummertoevoeging() == null)
							ps.setNull(7,Types.VARCHAR);
						else
							ps.setString(7,nummeraanduiding.getHuisnummertoevoeging());
						if (nummeraanduiding.getPostcode() == null)
							ps.setNull(8,Types.VARCHAR);
						else
							ps.setString(8,nummeraanduiding.getPostcode());
						ps.setTimestamp(9,new Timestamp(nummeraanduiding.getBegindatumTijdvakGeldigheid().getTime()));
						if (nummeraanduiding.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(10,Types.TIMESTAMP);
						else
							ps.setTimestamp(10,new Timestamp(nummeraanduiding.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(11,nummeraanduiding.getInOnderzoek().ordinal());
						ps.setInt(12,nummeraanduiding.getTypeAdresseerbaarObject().ordinal());
						ps.setDate(13,new Date(nummeraanduiding.getDocumentdatum().getTime()));
						ps.setString(14,nummeraanduiding.getDocumentnummer());
						ps.setInt(15,nummeraanduiding.getNummeraanduidingStatus().ordinal());
						if (nummeraanduiding.getGerelateerdeWoonplaats() == null)
							ps.setNull(16,Types.INTEGER);
						else
							ps.setLong(16,nummeraanduiding.getGerelateerdeWoonplaats());
						ps.setLong(17,nummeraanduiding.getGerelateerdeOpenbareRuimte());
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting nummeraanduiding: " + nummeraanduiding.getIdentificatie(),e);
		}
	}

	@Override
	public void insert(final Pand pand) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"insert into bag_pand (" +
								"bag_pand_id," +
								"aanduiding_record_inactief," +
								"aanduiding_record_correctie," +
								"officieel," +
								"pand_geometrie," +
								"bouwjaar," +
								"pand_status," +
								"begindatum_tijdvak_geldigheid," +
								"einddatum_tijdvak_geldigheid," +
								"in_onderzoek," +
								"bron_documentdatum," +
								"bron_documentnummer" +
							") values (?,?,?,?,?,?,?,?,?,?,?,?)"
						);
						ps.setLong(1,pand.getIdentificatie());
						ps.setInt(2,pand.getAanduidingRecordInactief().ordinal());
						ps.setLong(3,pand.getAanduidingRecordCorrectie());
						ps.setInt(4,pand.getOfficieel().ordinal());
						ps.setString(5,pand.getPandGeometrie());
						ps.setInt(6,pand.getBouwjaar());
						ps.setString(7,pand.getPandStatus());
						ps.setTimestamp(8,new Timestamp(pand.getBegindatumTijdvakGeldigheid().getTime()));
						if (pand.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(9,Types.TIMESTAMP);
						else
							ps.setTimestamp(9,new Timestamp(pand.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(10,pand.getInOnderzoek().ordinal());
						ps.setDate(11,new Date(pand.getDocumentdatum().getTime()));
						ps.setString(12,pand.getDocumentnummer());
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting pand: " + pand.getIdentificatie(),e);
		}
	}

	@Override
	public void insert(final Verblijfsobject verblijfsobject) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.update(
							new PreparedStatementCreator()
							{
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
								{
									PreparedStatement ps = connection.prepareStatement(
										"insert into bag_verblijfsobject (" +
											"bag_verblijfsobject_id," +
											"aanduiding_record_inactief," +
											"aanduiding_record_correctie," +
											"officieel," +
											"verblijfsobject_geometrie," +
											"oppervlakte_verblijfsobject," +
											"verblijfsobject_status," +
											"begindatum_tijdvak_geldigheid," +
											"einddatum_tijdvak_geldigheid," +
											"in_onderzoek," +
											"bron_documentdatum," +
											"bron_documentnummer," +
											"bag_nummeraanduiding_id" +
										") values (?,?,?,?,?,?,?,?,?,?,?,?,?)"
									);
									ps.setLong(1,verblijfsobject.getIdentificatie());
									ps.setInt(2,verblijfsobject.getAanduidingRecordInactief().ordinal());
									ps.setLong(3,verblijfsobject.getAanduidingRecordCorrectie());
									ps.setInt(4,verblijfsobject.getOfficieel().ordinal());
									ps.setString(5,verblijfsobject.getVerblijfsobjectGeometrie());
									ps.setInt(6,verblijfsobject.getOppervlakteVerblijfsobject());
									ps.setInt(7,verblijfsobject.getVerblijfsobjectStatus().ordinal());
									ps.setTimestamp(8,new Timestamp(verblijfsobject.getBegindatumTijdvakGeldigheid().getTime()));
									if (verblijfsobject.getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(9,Types.TIMESTAMP);
									else
										ps.setTimestamp(9,new Timestamp(verblijfsobject.getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(10,verblijfsobject.getInOnderzoek().ordinal());
									ps.setDate(11,new Date(verblijfsobject.getDocumentdatum().getTime()));
									ps.setString(12,verblijfsobject.getDocumentnummer());
									ps.setLong(13,verblijfsobject.getHoofdAdres());
									return ps;
								}
							}
						);
						insertGebruikersdoelen(verblijfsobject);
						insertNevenadressen(TypeAdresseerbaarObject.VERBLIJFSOBJECT,verblijfsobject);
						insertGerelateerdePanden(verblijfsobject);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting verblijfsobject: " + verblijfsobject.getIdentificatie(),e);
		}
	}

	@Override
	public void insert(final Ligplaats ligplaats) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.update(
							new PreparedStatementCreator()
							{
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
								{
									PreparedStatement ps = connection.prepareStatement(
										"insert into bag_ligplaats (" +
											"bag_ligplaats_id," +
											"aanduiding_record_inactief," +
											"aanduiding_record_correctie," +
											"officieel," +
											"ligplaats_status," +
											"ligplaats_geometrie," +
											"begindatum_tijdvak_geldigheid," +
											"einddatum_tijdvak_geldigheid," +
											"in_onderzoek," +
											"bron_documentdatum," +
											"bron_documentnummer," +
											"bag_nummeraanduiding_id" +
										") values (?,?,?,?,?,?,?,?,?,?,?,?)"
									);
									ps.setLong(1,ligplaats.getIdentificatie());
									ps.setInt(2,ligplaats.getAanduidingRecordInactief().ordinal());
									ps.setLong(3,ligplaats.getAanduidingRecordCorrectie());
									ps.setInt(4,ligplaats.getOfficieel().ordinal());
									ps.setInt(5,ligplaats.getLigplaatsStatus().ordinal());
									ps.setString(6,ligplaats.getLigplaatsGeometrie());
									ps.setTimestamp(7,new Timestamp(ligplaats.getBegindatumTijdvakGeldigheid().getTime()));
									if (ligplaats.getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(8,Types.TIMESTAMP);
									else
										ps.setTimestamp(8,new Timestamp(ligplaats.getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(9,ligplaats.getInOnderzoek().ordinal());
									ps.setDate(10,new Date(ligplaats.getDocumentdatum().getTime()));
									ps.setString(11,ligplaats.getDocumentnummer());
									ps.setLong(12,ligplaats.getHoofdAdres());
									return ps;
								}
							}
						);
						insertNevenadressen(TypeAdresseerbaarObject.LIGPLAATS,ligplaats);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting ligplaats: " + ligplaats.getIdentificatie(),e);
		}
	}

	@Override
	public void insert(final Standplaats standplaats) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.update(
							new PreparedStatementCreator()
							{
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
								{
									PreparedStatement ps = connection.prepareStatement(
										"insert into bag_standplaats (" +
											"bag_standplaats_id," +
											"aanduiding_record_inactief," +
											"aanduiding_record_correctie," +
											"officieel," +
											"standplaats_status," +
											"standplaats_geometrie," +
											"begindatum_tijdvak_geldigheid," +
											"einddatum_tijdvak_geldigheid," +
											"in_onderzoek," +
											"bron_documentdatum," +
											"bron_documentnummer," +
											"bag_nummeraanduiding_id" +
										") values (?,?,?,?,?,?,?,?,?,?,?,?)"
									);
									ps.setLong(1,standplaats.getIdentificatie());
									ps.setInt(2,standplaats.getAanduidingRecordInactief().ordinal());
									ps.setLong(3,standplaats.getAanduidingRecordCorrectie());
									ps.setInt(4,standplaats.getOfficieel().ordinal());
									ps.setInt(5,standplaats.getStandplaatsStatus().ordinal());
									ps.setString(6,standplaats.getStandplaatsGeometrie());
									ps.setTimestamp(7,new Timestamp(standplaats.getBegindatumTijdvakGeldigheid().getTime()));
									if (standplaats.getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(8,Types.TIMESTAMP);
									else
										ps.setTimestamp(8,new Timestamp(standplaats.getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(9,standplaats.getInOnderzoek().ordinal());
									ps.setDate(10,new Date(standplaats.getDocumentdatum().getTime()));
									ps.setString(11,standplaats.getDocumentnummer());
									ps.setLong(12,standplaats.getHoofdAdres());
									return ps;
								}
							}
						);
						insertNevenadressen(TypeAdresseerbaarObject.STANDPLAATS,standplaats);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting standplaats: " + standplaats.getIdentificatie(),e);
		}
	}

	@Override
	public void insertWoonplaatsen(final List<Woonplaats> woonplaatsen) throws DAOException
	{
		try
		{
			jdbcTemplate.batchUpdate(
				"insert into bag_woonplaats (" +
					"bag_woonplaats_id," +
					"aanduiding_record_inactief," +
					"aanduiding_record_correctie," +
					"woonplaats_naam," +
					"woonplaats_geometrie," +
					"officieel," +
					"begindatum_tijdvak_geldigheid," +
					"einddatum_tijdvak_geldigheid," +
					"in_onderzoek," +
					"bron_documentdatum," +
					"bron_documentnummer," +
					"woonplaats_status" +
				") values (?,?,?,?,?,?,?,?,?,?,?,?)",
				new BatchPreparedStatementSetter()
				{
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException
					{
						ps.setLong(1,woonplaatsen.get(i).getIdentificatie());
						ps.setInt(2,woonplaatsen.get(i).getAanduidingRecordInactief().ordinal());
						ps.setLong(3,woonplaatsen.get(i).getAanduidingRecordCorrectie());
						ps.setString(4,woonplaatsen.get(i).getWoonplaatsNaam());
						ps.setString(5,woonplaatsen.get(i).getWoonplaatsGeometrie());
						ps.setInt(6,woonplaatsen.get(i).getOfficieel().ordinal());
						ps.setTimestamp(7,new Timestamp(woonplaatsen.get(i).getBegindatumTijdvakGeldigheid().getTime()));
						if (woonplaatsen.get(i).getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(8,Types.TIMESTAMP);
						else
							ps.setTimestamp(8,new Timestamp(woonplaatsen.get(i).getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(9,woonplaatsen.get(i).getInOnderzoek().ordinal());
						ps.setDate(10,new Date(woonplaatsen.get(i).getDocumentdatum().getTime()));
						ps.setString(11,woonplaatsen.get(i).getDocumentnummer());
						ps.setInt(12,woonplaatsen.get(i).getWoonplaatsStatus().ordinal());
					}
					
					@Override
					public int getBatchSize()
					{
						return woonplaatsen.size();
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting woonplaatsen",e);
		}
	}

	@Override
	public void insertOpenbareRuimten(final List<OpenbareRuimte> openbareRuimten) throws DAOException
	{
		try
		{
			jdbcTemplate.batchUpdate(
				"insert into bag_openbare_ruimte (" +
					"bag_openbare_ruimte_id," +
					"aanduiding_record_inactief," +
					"aanduiding_record_correctie," +
					"openbare_ruimte_naam," +
					"officieel," +
					"begindatum_tijdvak_geldigheid," +
					"einddatum_tijdvak_geldigheid," +
					"in_onderzoek," +
					"openbare_ruimte_type," +
					"bron_documentdatum," +
					"bron_documentnummer," +
					"openbareruimte_status," +
					"bag_woonplaats_id," +
					"verkorte_openbare_ruimte_naam" +
				") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new BatchPreparedStatementSetter()
				{
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException
					{
						ps.setLong(1,openbareRuimten.get(i).getIdentificatie());
						ps.setInt(2,openbareRuimten.get(i).getAanduidingRecordInactief().ordinal());
						ps.setLong(3,openbareRuimten.get(i).getAanduidingRecordCorrectie());
						ps.setString(4,openbareRuimten.get(i).getOpenbareRuimteNaam());
						ps.setInt(5,openbareRuimten.get(i).getOfficieel().ordinal());
						ps.setTimestamp(6,new Timestamp(openbareRuimten.get(i).getBegindatumTijdvakGeldigheid().getTime()));
						if (openbareRuimten.get(i).getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(7,Types.TIMESTAMP);
						else
							ps.setTimestamp(7,new Timestamp(openbareRuimten.get(i).getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(8,openbareRuimten.get(i).getInOnderzoek().ordinal());
						ps.setInt(9,openbareRuimten.get(i).getOpenbareRuimteType().ordinal());
						ps.setDate(10,new Date(openbareRuimten.get(i).getDocumentdatum().getTime()));
						ps.setString(11,openbareRuimten.get(i).getDocumentnummer());
						ps.setInt(12,openbareRuimten.get(i).getOpenbareruimteStatus().ordinal());
						ps.setLong(13,openbareRuimten.get(i).getGerelateerdeWoonplaats());
						ps.setString(14,openbareRuimten.get(i).getVerkorteOpenbareRuimteNaam());
					}
					
					@Override
					public int getBatchSize()
					{
						return openbareRuimten.size();
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting openbare ruimten",e);
		}
	}

	@Override
	public void insertNummeraanduidingen(final List<Nummeraanduiding> nummeraanduidingen) throws DAOException
	{
		try
		{
			jdbcTemplate.batchUpdate(
				"insert into bag_nummeraanduiding (" +
					"bag_nummeraanduiding_id," +
					"aanduiding_record_inactief," +
					"aanduiding_record_correctie," +
					"huisnummer," +
					"officieel," +
					"huisletter," +
					"huisnummertoevoeging," +
					"postcode," +
					"begindatum_tijdvak_geldigheid," +
					"einddatum_tijdvak_geldigheid," +
					"in_onderzoek," +
					"type_adresseerbaar_object," +
					"bron_documentdatum," +
					"bron_documentnummer," +
					"nummeraanduiding_status," +
					"bag_woonplaats_id," +
					"bag_openbare_ruimte_id" +
				") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new BatchPreparedStatementSetter()
				{
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException
					{
						ps.setLong(1,nummeraanduidingen.get(i).getIdentificatie());
						ps.setInt(2,nummeraanduidingen.get(i).getAanduidingRecordInactief().ordinal());
						ps.setLong(3,nummeraanduidingen.get(i).getAanduidingRecordCorrectie());
						ps.setInt(4,nummeraanduidingen.get(i).getHuisnummer());
						ps.setInt(5,nummeraanduidingen.get(i).getOfficieel().ordinal());
						if (nummeraanduidingen.get(i).getHuisletter() == null)
							ps.setNull(6,Types.VARCHAR);
						else
							ps.setString(6,nummeraanduidingen.get(i).getHuisletter());
						if (nummeraanduidingen.get(i).getHuisnummertoevoeging() == null)
							ps.setNull(7,Types.VARCHAR);
						else
							ps.setString(7,nummeraanduidingen.get(i).getHuisnummertoevoeging());
						if (nummeraanduidingen.get(i).getPostcode() == null)
							ps.setNull(8,Types.VARCHAR);
						else
							ps.setString(8,nummeraanduidingen.get(i).getPostcode());
						ps.setTimestamp(9,new Timestamp(nummeraanduidingen.get(i).getBegindatumTijdvakGeldigheid().getTime()));
						if (nummeraanduidingen.get(i).getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(10,Types.TIMESTAMP);
						else
							ps.setTimestamp(10,new Timestamp(nummeraanduidingen.get(i).getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(11,nummeraanduidingen.get(i).getInOnderzoek().ordinal());
						ps.setInt(12,nummeraanduidingen.get(i).getTypeAdresseerbaarObject().ordinal());
						ps.setDate(13,new Date(nummeraanduidingen.get(i).getDocumentdatum().getTime()));
						ps.setString(14,nummeraanduidingen.get(i).getDocumentnummer());
						ps.setInt(15,nummeraanduidingen.get(i).getNummeraanduidingStatus().ordinal());
						if (nummeraanduidingen.get(i).getGerelateerdeWoonplaats() == null)
							ps.setNull(16,Types.INTEGER);
						else
							ps.setLong(16,nummeraanduidingen.get(i).getGerelateerdeWoonplaats());
						ps.setLong(17,nummeraanduidingen.get(i).getGerelateerdeOpenbareRuimte());
					}
					
					@Override
					public int getBatchSize()
					{
						return nummeraanduidingen.size();
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting nummeraanduidingen",e);
		}
	}

	@Override
	public void insertPanden(final List<Pand> panden) throws DAOException
	{
		try
		{
			jdbcTemplate.batchUpdate(
				"insert into bag_pand (" +
					"bag_pand_id," +
					"aanduiding_record_inactief," +
					"aanduiding_record_correctie," +
					"officieel," +
					"pand_geometrie," +
					"bouwjaar," +
					"pand_status," +
					"begindatum_tijdvak_geldigheid," +
					"einddatum_tijdvak_geldigheid," +
					"in_onderzoek," +
					"bron_documentdatum," +
					"bron_documentnummer" +
				") values (?,?,?,?,?,?,?,?,?,?,?,?)",
				new BatchPreparedStatementSetter()
				{
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException
					{
						ps.setLong(1,panden.get(i).getIdentificatie());
						ps.setInt(2,panden.get(i).getAanduidingRecordInactief().ordinal());
						ps.setLong(3,panden.get(i).getAanduidingRecordCorrectie());
						ps.setInt(4,panden.get(i).getOfficieel().ordinal());
						ps.setString(5,panden.get(i).getPandGeometrie());
						ps.setInt(6,panden.get(i).getBouwjaar());
						ps.setString(7,panden.get(i).getPandStatus());
						ps.setTimestamp(8,new Timestamp(panden.get(i).getBegindatumTijdvakGeldigheid().getTime()));
						if (panden.get(i).getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(9,Types.TIMESTAMP);
						else
							ps.setTimestamp(9,new Timestamp(panden.get(i).getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(10,panden.get(i).getInOnderzoek().ordinal());
						ps.setDate(11,new Date(panden.get(i).getDocumentdatum().getTime()));
						ps.setString(12,panden.get(i).getDocumentnummer());
					}
					
					@Override
					public int getBatchSize()
					{
						return panden.size();
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting panden",e);
		}
	}

	@Override
	public void insertVerblijfsobjecten(final List<Verblijfsobject> verblijfsobjecten) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.batchUpdate(
							"insert into bag_verblijfsobject (" +
								"bag_verblijfsobject_id," +
								"aanduiding_record_inactief," +
								"aanduiding_record_correctie," +
								"officieel," +
								"verblijfsobject_geometrie," +
								"oppervlakte_verblijfsobject," +
								"verblijfsobject_status," +
								"begindatum_tijdvak_geldigheid," +
								"einddatum_tijdvak_geldigheid," +
								"in_onderzoek," +
								"bron_documentdatum," +
								"bron_documentnummer," +
								"bag_nummeraanduiding_id" +
							") values (?,?,?,?,?,?,?,?,?,?,?,?,?)",
							new BatchPreparedStatementSetter()
							{
								@Override
								public void setValues(PreparedStatement ps, int i) throws SQLException
								{
									ps.setLong(1,verblijfsobjecten.get(i).getIdentificatie());
									ps.setInt(2,verblijfsobjecten.get(i).getAanduidingRecordInactief().ordinal());
									ps.setLong(3,verblijfsobjecten.get(i).getAanduidingRecordCorrectie());
									ps.setInt(4,verblijfsobjecten.get(i).getOfficieel().ordinal());
									ps.setString(5,verblijfsobjecten.get(i).getVerblijfsobjectGeometrie());
									ps.setInt(6,verblijfsobjecten.get(i).getOppervlakteVerblijfsobject());
									ps.setInt(7,verblijfsobjecten.get(i).getVerblijfsobjectStatus().ordinal());
									ps.setTimestamp(8,new Timestamp(verblijfsobjecten.get(i).getBegindatumTijdvakGeldigheid().getTime()));
									if (verblijfsobjecten.get(i).getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(9,Types.TIMESTAMP);
									else
										ps.setTimestamp(9,new Timestamp(verblijfsobjecten.get(i).getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(10,verblijfsobjecten.get(i).getInOnderzoek().ordinal());
									ps.setDate(11,new Date(verblijfsobjecten.get(i).getDocumentdatum().getTime()));
									ps.setString(12,verblijfsobjecten.get(i).getDocumentnummer());
									ps.setLong(13,verblijfsobjecten.get(i).getHoofdAdres());
								}
								
								@Override
								public int getBatchSize()
								{
									return verblijfsobjecten.size();
								}
							}
						);
						insertGebruikersdoelen(verblijfsobjecten);
						insertNevenadressen(TypeAdresseerbaarObject.VERBLIJFSOBJECT,verblijfsobjecten);
						insertGerelateerdePanden(verblijfsobjecten);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting verblijfsobjecten",e);
		}
	}

	@Override
	public void insertLigplaatsen(final List<Ligplaats> ligplaatsen) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.batchUpdate(
							"insert into bag_ligplaats (" +
								"bag_ligplaats_id," +
								"aanduiding_record_inactief," +
								"aanduiding_record_correctie," +
								"officieel," +
								"ligplaats_status," +
								"ligplaats_geometrie," +
								"begindatum_tijdvak_geldigheid," +
								"einddatum_tijdvak_geldigheid," +
								"in_onderzoek," +
								"bron_documentdatum," +
								"bron_documentnummer," +
								"bag_nummeraanduiding_id" +
							") values (?,?,?,?,?,?,?,?,?,?,?,?)",
							new BatchPreparedStatementSetter()
							{
								@Override
								public void setValues(PreparedStatement ps, int i) throws SQLException
								{
									ps.setLong(1,ligplaatsen.get(i).getIdentificatie());
									ps.setInt(2,ligplaatsen.get(i).getAanduidingRecordInactief().ordinal());
									ps.setLong(3,ligplaatsen.get(i).getAanduidingRecordCorrectie());
									ps.setInt(4,ligplaatsen.get(i).getOfficieel().ordinal());
									ps.setInt(5,ligplaatsen.get(i).getLigplaatsStatus().ordinal());
									ps.setString(6,ligplaatsen.get(i).getLigplaatsGeometrie());
									ps.setTimestamp(7,new Timestamp(ligplaatsen.get(i).getBegindatumTijdvakGeldigheid().getTime()));
									if (ligplaatsen.get(i).getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(8,Types.TIMESTAMP);
									else
										ps.setTimestamp(8,new Timestamp(ligplaatsen.get(i).getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(9,ligplaatsen.get(i).getInOnderzoek().ordinal());
									ps.setDate(10,new Date(ligplaatsen.get(i).getDocumentdatum().getTime()));
									ps.setString(11,ligplaatsen.get(i).getDocumentnummer());
									ps.setLong(12,ligplaatsen.get(i).getHoofdAdres());
								}
								
								@Override
								public int getBatchSize()
								{
									return ligplaatsen.size();
								}
							}
						);
						insertNevenadressen(TypeAdresseerbaarObject.LIGPLAATS,ligplaatsen);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting ligplaatsen",e);
		}
	}

	@Override
	public void insertStandplaatsen(final List<Standplaats> standplaatsen) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.batchUpdate(
							"insert into bag_standplaats (" +
								"bag_standplaats_id," +
								"aanduiding_record_inactief," +
								"aanduiding_record_correctie," +
								"officieel," +
								"standplaats_status," +
								"standplaats_geometrie," +
								"begindatum_tijdvak_geldigheid," +
								"einddatum_tijdvak_geldigheid," +
								"in_onderzoek," +
								"bron_documentdatum," +
								"bron_documentnummer," +
								"bag_nummeraanduiding_id" +
							") values (?,?,?,?,?,?,?,?,?,?,?,?)",
							new BatchPreparedStatementSetter()
							{
								@Override
								public void setValues(PreparedStatement ps, int i) throws SQLException
								{
									ps.setLong(1,standplaatsen.get(i).getIdentificatie());
									ps.setInt(2,standplaatsen.get(i).getAanduidingRecordInactief().ordinal());
									ps.setLong(3,standplaatsen.get(i).getAanduidingRecordCorrectie());
									ps.setInt(4,standplaatsen.get(i).getOfficieel().ordinal());
									ps.setInt(5,standplaatsen.get(i).getStandplaatsStatus().ordinal());
									ps.setString(6,standplaatsen.get(i).getStandplaatsGeometrie());
									ps.setTimestamp(7,new Timestamp(standplaatsen.get(i).getBegindatumTijdvakGeldigheid().getTime()));
									if (standplaatsen.get(i).getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(8,Types.TIMESTAMP);
									else
										ps.setTimestamp(8,new Timestamp(standplaatsen.get(i).getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(9,standplaatsen.get(i).getInOnderzoek().ordinal());
									ps.setDate(10,new Date(standplaatsen.get(i).getDocumentdatum().getTime()));
									ps.setString(11,standplaatsen.get(i).getDocumentnummer());
									ps.setLong(12,standplaatsen.get(i).getHoofdAdres());
								}
								
								@Override
								public int getBatchSize()
								{
									return standplaatsen.size();
								}
							}
						);
						insertNevenadressen(TypeAdresseerbaarObject.STANDPLAATS,standplaatsen);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error inserting standplaatsen",e);
		}
	}

	@Override
	public void update(final Woonplaats woonplaats) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"update bag_woonplaats set" +
							" aanduiding_record_inactief = ?," +
							" woonplaats_naam = ?," +
							" woonplaats_geometrie = ?," +
							" officieel = ?," +
							" einddatum_tijdvak_geldigheid = ?," +
							" in_onderzoek = ?," +
							" bron_documentdatum = ?," +
							" bron_documentnummer = ?," +
							" woonplaats_status = ?" +
							" where bag_woonplaats_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?"
						);
						ps.setInt(1,woonplaats.getAanduidingRecordInactief().ordinal());
						ps.setString(2,woonplaats.getWoonplaatsNaam());
						ps.setString(3,woonplaats.getWoonplaatsGeometrie());
						ps.setInt(4,woonplaats.getOfficieel().ordinal());
						if (woonplaats.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(5,Types.TIMESTAMP);
						else
							ps.setTimestamp(5,new Timestamp(woonplaats.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(6,woonplaats.getInOnderzoek().ordinal());
						ps.setDate(7,new Date(woonplaats.getDocumentdatum().getTime()));
						ps.setString(8,woonplaats.getDocumentnummer());
						ps.setInt(9,woonplaats.getWoonplaatsStatus().ordinal());
						ps.setLong(10,woonplaats.getIdentificatie());
						ps.setLong(11,woonplaats.getAanduidingRecordCorrectie());
						ps.setTimestamp(12,new Timestamp(woonplaats.getBegindatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating woonplaats: " + woonplaats.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final OpenbareRuimte openbareRuimte) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"update bag_openbare_ruimte set" +
							" aanduiding_record_inactief = ?," +
							" openbare_ruimte_naam = ?," +
							" officieel = ?," +
							" einddatum_tijdvak_geldigheid = ?," +
							" in_onderzoek = ?," +
							" openbare_ruimte_type = ?," +
							" bron_documentdatum = ?," +
							" bron_documentnummer = ?," +
							" openbareruimte_status = ?," +
							" bag_woonplaats_id = ?," +
							" verkorte_openbare_ruimte_naam = ?" +
							" where bag_openbare_ruimte_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?"
						);
						ps.setInt(1,openbareRuimte.getAanduidingRecordInactief().ordinal());
						ps.setString(2,openbareRuimte.getOpenbareRuimteNaam());
						ps.setInt(3,openbareRuimte.getOfficieel().ordinal());
						if (openbareRuimte.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(4,Types.TIMESTAMP);
						else
							ps.setTimestamp(4,new Timestamp(openbareRuimte.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(5,openbareRuimte.getInOnderzoek().ordinal());
						ps.setInt(6,openbareRuimte.getOpenbareRuimteType().ordinal());
						ps.setDate(7,new Date(openbareRuimte.getDocumentdatum().getTime()));
						ps.setString(8,openbareRuimte.getDocumentnummer());
						ps.setInt(9,openbareRuimte.getOpenbareruimteStatus().ordinal());
						ps.setLong(10,openbareRuimte.getGerelateerdeWoonplaats());
						ps.setString(11,openbareRuimte.getVerkorteOpenbareRuimteNaam());
						ps.setLong(12,openbareRuimte.getIdentificatie());
						ps.setLong(13,openbareRuimte.getAanduidingRecordCorrectie());
						ps.setTimestamp(14,new Timestamp(openbareRuimte.getBegindatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating openbare ruimte: " + openbareRuimte.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Nummeraanduiding nummeraanduiding) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"update bag_nummeraanduiding set" +
							" aanduiding_record_inactief = ?," +
							" huisnummer = ?," +
							" officieel = ?," +
							" huisletter = ?," +
							" huisnummertoevoeging = ?," +
							" postcode = ?," +
							" einddatum_tijdvak_geldigheid = ?," +
							" in_onderzoek = ?," +
							" type_adresseerbaar_object = ?," +
							" bron_documentdatum = ?," +
							" bron_documentnummer = ?," +
							" nummeraanduiding_status = ?," +
							" bag_woonplaats_id = ?," +
							" bag_openbare_ruimte_id = ?" +
							" where bag_nummeraanduiding_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?"
						);
						ps.setInt(1,nummeraanduiding.getAanduidingRecordInactief().ordinal());
						ps.setInt(2,nummeraanduiding.getHuisnummer());
						ps.setInt(3,nummeraanduiding.getOfficieel().ordinal());
						if (nummeraanduiding.getHuisletter() == null)
							ps.setNull(4,Types.INTEGER);
						else
							ps.setString(4,nummeraanduiding.getHuisletter());
						if (nummeraanduiding.getHuisnummertoevoeging() == null)
							ps.setNull(5,Types.VARCHAR);
						else
							ps.setString(5,nummeraanduiding.getHuisnummertoevoeging());
						if (nummeraanduiding.getPostcode() == null)
							ps.setNull(6,Types.VARCHAR);
						else
							ps.setString(6,nummeraanduiding.getPostcode());
						if (nummeraanduiding.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(7,Types.TIMESTAMP);
						else
							ps.setTimestamp(7,new Timestamp(nummeraanduiding.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(8,nummeraanduiding.getInOnderzoek().ordinal());
						ps.setInt(9,nummeraanduiding.getTypeAdresseerbaarObject().ordinal());
						ps.setDate(10,new Date(nummeraanduiding.getDocumentdatum().getTime()));
						ps.setString(11,nummeraanduiding.getDocumentnummer());
						ps.setInt(12,nummeraanduiding.getNummeraanduidingStatus().ordinal());
						if (nummeraanduiding.getGerelateerdeWoonplaats() == null)
							ps.setNull(13,Types.INTEGER);
						else
							ps.setLong(13,nummeraanduiding.getGerelateerdeWoonplaats());
						ps.setLong(14,nummeraanduiding.getGerelateerdeOpenbareRuimte());
						ps.setLong(15,nummeraanduiding.getIdentificatie());
						ps.setLong(16,nummeraanduiding.getAanduidingRecordCorrectie());
						ps.setTimestamp(17,new Timestamp(nummeraanduiding.getBegindatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating nummeraanduiding: " + nummeraanduiding.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Pand pand) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"update bag_pand set" +
							" aanduiding_record_inactief = ?," +
							" officieel = ?," +
							" pand_geometrie = ?," +
							" bouwjaar = ?," +
							" pand_status = ?," +
							" einddatum_tijdvak_geldigheid = ?," +
							" in_onderzoek = ?," +
							" bron_documentdatum = ?," +
							" bron_documentnummer = ?" +
							" where bag_pand_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?"
						);
						ps.setInt(1,pand.getAanduidingRecordInactief().ordinal());
						ps.setInt(2,pand.getOfficieel().ordinal());
						ps.setString(3,pand.getPandGeometrie());
						ps.setInt(4,pand.getBouwjaar());
						ps.setString(5,pand.getPandStatus());
						if (pand.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(6,Types.TIMESTAMP);
						else
							ps.setTimestamp(6,new Timestamp(pand.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(7,pand.getInOnderzoek().ordinal());
						ps.setDate(8,new Date(pand.getDocumentdatum().getTime()));
						ps.setString(9,pand.getDocumentnummer());
						ps.setLong(10,pand.getIdentificatie());
						ps.setLong(11,pand.getAanduidingRecordCorrectie());
						ps.setTimestamp(12,new Timestamp(pand.getBegindatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating pand: " + pand.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Verblijfsobject verblijfsobject) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.update(
							new PreparedStatementCreator()
							{
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
								{
									PreparedStatement ps = connection.prepareStatement(
										"update bag_verblijfsobject set" +
										" aanduiding_record_inactief = ?," +
										" officieel = ?," +
										" verblijfsobject_geometrie = ?," +
										" oppervlakte_verblijfsobject = ?," +
										" verblijfsobject_status = ?," +
										" einddatum_tijdvak_geldigheid = ?," +
										" in_onderzoek = ?," +
										" bron_documentdatum = ?," +
										" bron_documentnummer = ?," +
										" bag_nummeraanduiding_id = ?" +
										" where bag_verblijfsobject_id = ?" +
										" and aanduiding_record_correctie = ?" +
										" and begindatum_tijdvak_geldigheid = ?"
									);
									ps.setInt(1,verblijfsobject.getAanduidingRecordInactief().ordinal());
									ps.setInt(2,verblijfsobject.getOfficieel().ordinal());
									ps.setString(3,verblijfsobject.getVerblijfsobjectGeometrie());
									ps.setInt(4,verblijfsobject.getOppervlakteVerblijfsobject());
									ps.setInt(5,verblijfsobject.getVerblijfsobjectStatus().ordinal());
									if (verblijfsobject.getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(6,Types.TIMESTAMP);
									else
										ps.setTimestamp(6,new Timestamp(verblijfsobject.getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(7,verblijfsobject.getInOnderzoek().ordinal());
									ps.setDate(8,new Date(verblijfsobject.getDocumentdatum().getTime()));
									ps.setString(9,verblijfsobject.getDocumentnummer());
									ps.setLong(10,verblijfsobject.getHoofdAdres());
									ps.setLong(11,verblijfsobject.getIdentificatie());
									ps.setLong(12,verblijfsobject.getAanduidingRecordCorrectie());
									ps.setTimestamp(13,new Timestamp(verblijfsobject.getBegindatumTijdvakGeldigheid().getTime()));
									return ps;
								}
							}
						);
						deleteGebruikersdoelen(verblijfsobject);
						insertGebruikersdoelen(verblijfsobject);
						deleteNevenadressen(TypeAdresseerbaarObject.VERBLIJFSOBJECT,verblijfsobject);
						insertNevenadressen(TypeAdresseerbaarObject.VERBLIJFSOBJECT,verblijfsobject);
						deleteGerelateerdePanden(verblijfsobject);
						insertGerelateerdePanden(verblijfsobject);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating verblijfsobject: " + verblijfsobject.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Ligplaats ligplaats) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.update(
							new PreparedStatementCreator()
							{
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
								{
									PreparedStatement ps = connection.prepareStatement(
										"update bag_ligplaats set" +
										" aanduiding_record_inactief = ?," +
										" officieel = ?," +
										" ligplaats_status = ?," +
										" ligplaats_geometrie = ?," +
										" einddatum_tijdvak_geldigheid = ?," +
										" in_onderzoek = ?," +
										" bron_documentdatum = ?," +
										" bron_documentnummer = ?," +
										" bag_nummeraanduiding_id = ?" +
										" where bag_ligplaats_id = ?" +
										" and aanduiding_record_correctie = ?" +
										" and begindatum_tijdvak_geldigheid = ?"
									);
									ps.setInt(1,ligplaats.getAanduidingRecordInactief().ordinal());
									ps.setInt(2,ligplaats.getOfficieel().ordinal());
									ps.setInt(3,ligplaats.getLigplaatsStatus().ordinal());
									ps.setString(4,ligplaats.getLigplaatsGeometrie());
									if (ligplaats.getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(5,Types.TIMESTAMP);
									else
										ps.setTimestamp(5,new Timestamp(ligplaats.getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(6,ligplaats.getInOnderzoek().ordinal());
									ps.setDate(7,new Date(ligplaats.getDocumentdatum().getTime()));
									ps.setString(8,ligplaats.getDocumentnummer());
									ps.setLong(9,ligplaats.getHoofdAdres());
									ps.setLong(10,ligplaats.getIdentificatie());
									ps.setLong(11,ligplaats.getAanduidingRecordCorrectie());
									ps.setTimestamp(12,new Timestamp(ligplaats.getBegindatumTijdvakGeldigheid().getTime()));
									return ps;
								}
							}
						);
						deleteNevenadressen(TypeAdresseerbaarObject.LIGPLAATS,ligplaats);
						insertNevenadressen(TypeAdresseerbaarObject.LIGPLAATS,ligplaats);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating ligplaats: " + ligplaats.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Standplaats standplaats) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.update(
							new PreparedStatementCreator()
							{
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
								{
									PreparedStatement ps = connection.prepareStatement(
										"update bag_standplaats set" +
										" aanduiding_record_inactief = ?," +
										" officieel = ?," +
										" standplaats_status = ?," +
										" standplaats_geometrie = ?," +
										" einddatum_tijdvak_geldigheid = ?," +
										" in_onderzoek = ?," +
										" bron_documentdatum = ?," +
										" bron_documentnummer = ?," +
										" bag_nummeraanduiding_id = ?" +
										" where bag_standplaats_id = ?" +
										" and aanduiding_record_correctie = ?" +
										" and begindatum_tijdvak_geldigheid = ?"
									);
									ps.setInt(1,standplaats.getAanduidingRecordInactief().ordinal());
									ps.setInt(2,standplaats.getOfficieel().ordinal());
									ps.setInt(3,standplaats.getStandplaatsStatus().ordinal());
									ps.setString(4,standplaats.getStandplaatsGeometrie());
									if (standplaats.getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(5,Types.TIMESTAMP);
									else
										ps.setTimestamp(5,new Timestamp(standplaats.getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(6,standplaats.getInOnderzoek().ordinal());
									ps.setDate(7,new Date(standplaats.getDocumentdatum().getTime()));
									ps.setString(8,standplaats.getDocumentnummer());
									ps.setLong(9,standplaats.getHoofdAdres());
									ps.setLong(10,standplaats.getIdentificatie());
									ps.setLong(11,standplaats.getAanduidingRecordCorrectie());
									ps.setTimestamp(12,new Timestamp(standplaats.getBegindatumTijdvakGeldigheid().getTime()));
									return ps;
								}
							}
						);
						deleteNevenadressen(TypeAdresseerbaarObject.STANDPLAATS,standplaats);
						insertNevenadressen(TypeAdresseerbaarObject.STANDPLAATS,standplaats);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating standplaats: " + standplaats.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Woonplaats origineel, final Woonplaats wijziging) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"update bag_woonplaats set" +
							" aanduiding_record_inactief = ?," +
							" aanduiding_record_correctie = ?," +
							" woonplaats_naam = ?," +
							" woonplaats_geometrie = ?," +
							" officieel = ?," +
							" einddatum_tijdvak_geldigheid = ?," +
							" in_onderzoek = ?," +
							" bron_documentdatum = ?," +
							" bron_documentnummer = ?," +
							" woonplaats_status = ?" +
							" where bag_woonplaats_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?"
						);
						ps.setInt(1,wijziging.getAanduidingRecordInactief().ordinal());
						ps.setLong(2,wijziging.getAanduidingRecordCorrectie());
						ps.setString(3,wijziging.getWoonplaatsNaam());
						ps.setString(4,wijziging.getWoonplaatsGeometrie());
						ps.setInt(5,wijziging.getOfficieel().ordinal());
						if (wijziging.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(6,Types.TIMESTAMP);
						else
							ps.setTimestamp(6,new Timestamp(wijziging.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(7,wijziging.getInOnderzoek().ordinal());
						ps.setDate(8,new Date(wijziging.getDocumentdatum().getTime()));
						ps.setString(9,wijziging.getDocumentnummer());
						ps.setInt(10,wijziging.getWoonplaatsStatus().ordinal());
						ps.setLong(11,origineel.getIdentificatie());
						ps.setLong(12,origineel.getAanduidingRecordCorrectie());
						ps.setTimestamp(13,new Timestamp(origineel.getBegindatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating woonplaats: " + origineel.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final OpenbareRuimte origineel, final OpenbareRuimte mutation) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"update bag_openbare_ruimte set" +
							" aanduiding_record_inactief = ?," +
							" aanduiding_record_correctie = ?," +
							" openbare_ruimte_naam = ?," +
							" officieel = ?," +
							" einddatum_tijdvak_geldigheid = ?," +
							" in_onderzoek = ?," +
							" openbare_ruimte_type = ?," +
							" bron_documentdatum = ?," +
							" bron_documentnummer = ?," +
							" openbareruimte_status = ?," +
							" bag_woonplaats_id = ?," +
							" verkorte_openbare_ruimte_naam = ?" +
							" where bag_openbare_ruimte_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?"
						);
						ps.setInt(1,mutation.getAanduidingRecordInactief().ordinal());
						ps.setLong(2,mutation.getAanduidingRecordCorrectie());
						ps.setString(3,mutation.getOpenbareRuimteNaam());
						ps.setInt(4,mutation.getOfficieel().ordinal());
						if (mutation.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(5,Types.TIMESTAMP);
						else
							ps.setTimestamp(5,new Timestamp(mutation.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(6,mutation.getInOnderzoek().ordinal());
						ps.setInt(7,mutation.getOpenbareRuimteType().ordinal());
						ps.setDate(8,new Date(mutation.getDocumentdatum().getTime()));
						ps.setString(9,mutation.getDocumentnummer());
						ps.setInt(10,mutation.getOpenbareruimteStatus().ordinal());
						ps.setLong(11,mutation.getGerelateerdeWoonplaats());
						ps.setString(12,mutation.getVerkorteOpenbareRuimteNaam());
						ps.setLong(13,origineel.getIdentificatie());
						ps.setLong(14,origineel.getAanduidingRecordCorrectie());
						ps.setTimestamp(15,new Timestamp(origineel.getBegindatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating openbare ruimte: " + origineel.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Nummeraanduiding origineel, final Nummeraanduiding mutation) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"update bag_nummeraanduiding set" +
							" aanduiding_record_inactief = ?," +
							" aanduiding_record_correctie = ?," +
							" huisnummer = ?," +
							" officieel = ?," +
							" huisletter = ?," +
							" huisnummertoevoeging = ?," +
							" postcode = ?," +
							" einddatum_tijdvak_geldigheid = ?," +
							" in_onderzoek = ?," +
							" type_adresseerbaar_object = ?," +
							" bron_documentdatum = ?," +
							" bron_documentnummer = ?," +
							" nummeraanduiding_status = ?," +
							" bag_woonplaats_id = ?," +
							" bag_openbare_ruimte_id = ?" +
							" where bag_nummeraanduiding_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?"
						);
						ps.setInt(1,mutation.getAanduidingRecordInactief().ordinal());
						ps.setLong(2,mutation.getAanduidingRecordCorrectie());
						ps.setInt(3,mutation.getHuisnummer());
						ps.setInt(4,mutation.getOfficieel().ordinal());
						if (mutation.getHuisletter() == null)
							ps.setNull(5,Types.INTEGER);
						else
							ps.setString(5,mutation.getHuisletter());
						if (mutation.getHuisnummertoevoeging() == null)
							ps.setNull(6,Types.VARCHAR);
						else
							ps.setString(6,mutation.getHuisnummertoevoeging());
						if (mutation.getPostcode() == null)
							ps.setNull(7,Types.VARCHAR);
						else
							ps.setString(7,mutation.getPostcode());
						if (mutation.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(8,Types.TIMESTAMP);
						else
							ps.setTimestamp(8,new Timestamp(mutation.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(9,mutation.getInOnderzoek().ordinal());
						ps.setInt(10,mutation.getTypeAdresseerbaarObject().ordinal());
						ps.setDate(11,new Date(mutation.getDocumentdatum().getTime()));
						ps.setString(12,mutation.getDocumentnummer());
						ps.setInt(13,mutation.getNummeraanduidingStatus().ordinal());
						if (mutation.getGerelateerdeWoonplaats() == null)
							ps.setNull(14,Types.INTEGER);
						else
							ps.setLong(14,mutation.getGerelateerdeWoonplaats());
						ps.setLong(15,mutation.getGerelateerdeOpenbareRuimte());
						ps.setLong(16,origineel.getIdentificatie());
						ps.setLong(17,origineel.getAanduidingRecordCorrectie());
						ps.setTimestamp(18,new Timestamp(origineel.getBegindatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating nummeraanduiding: " + origineel.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Pand origineel, final Pand mutation) throws DAOException
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"update bag_pand set" +
							" aanduiding_record_inactief = ?," +
							" aanduiding_record_correctie = ?," +
							" officieel = ?," +
							" pand_geometrie = ?," +
							" bouwjaar = ?," +
							" pand_status = ?," +
							" einddatum_tijdvak_geldigheid = ?," +
							" in_onderzoek = ?," +
							" bron_documentdatum = ?," +
							" bron_documentnummer = ?" +
							" where bag_pand_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?"
						);
						ps.setInt(1,mutation.getAanduidingRecordInactief().ordinal());
						ps.setLong(2,mutation.getAanduidingRecordCorrectie());
						ps.setInt(3,mutation.getOfficieel().ordinal());
						ps.setString(4,mutation.getPandGeometrie());
						ps.setInt(5,mutation.getBouwjaar());
						ps.setString(6,mutation.getPandStatus());
						if (mutation.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(7,Types.TIMESTAMP);
						else
							ps.setTimestamp(7,new Timestamp(mutation.getEinddatumTijdvakGeldigheid().getTime()));
						ps.setInt(8,mutation.getInOnderzoek().ordinal());
						ps.setDate(9,new Date(mutation.getDocumentdatum().getTime()));
						ps.setString(10,mutation.getDocumentnummer());
						ps.setLong(11,origineel.getIdentificatie());
						ps.setLong(12,origineel.getAanduidingRecordCorrectie());
						ps.setTimestamp(13,new Timestamp(origineel.getBegindatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating pand: " + origineel.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Verblijfsobject origineel, final Verblijfsobject mutation) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.update(
							new PreparedStatementCreator()
							{
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
								{
									PreparedStatement ps = connection.prepareStatement(
										"update bag_verblijfsobject set" +
										" aanduiding_record_inactief = ?," +
										" aanduiding_record_correctie = ?," +
										" officieel = ?," +
										" verblijfsobject_geometrie = ?," +
										" oppervlakte_verblijfsobject = ?," +
										" verblijfsobject_status = ?," +
										" einddatum_tijdvak_geldigheid = ?," +
										" in_onderzoek = ?," +
										" bron_documentdatum = ?," +
										" bron_documentnummer = ?," +
										" bag_nummeraanduiding_id = ?" +
										" where bag_verblijfsobject_id = ?" +
										" and aanduiding_record_correctie = ?" +
										" and begindatum_tijdvak_geldigheid = ?"
									);
									ps.setInt(1,mutation.getAanduidingRecordInactief().ordinal());
									ps.setLong(2,mutation.getAanduidingRecordCorrectie());
									ps.setInt(3,mutation.getOfficieel().ordinal());
									ps.setString(4,mutation.getVerblijfsobjectGeometrie());
									ps.setInt(5,mutation.getOppervlakteVerblijfsobject());
									ps.setInt(6,mutation.getVerblijfsobjectStatus().ordinal());
									if (mutation.getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(7,Types.TIMESTAMP);
									else
										ps.setTimestamp(7,new Timestamp(mutation.getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(8,mutation.getInOnderzoek().ordinal());
									ps.setDate(9,new Date(mutation.getDocumentdatum().getTime()));
									ps.setString(10,mutation.getDocumentnummer());
									ps.setLong(11,mutation.getHoofdAdres());
									ps.setLong(12,origineel.getIdentificatie());
									ps.setLong(13,origineel.getAanduidingRecordCorrectie());
									ps.setTimestamp(14,new Timestamp(origineel.getBegindatumTijdvakGeldigheid().getTime()));
									return ps;
								}
							}
						);
						deleteGebruikersdoelen(origineel);
						insertGebruikersdoelen(mutation);
						deleteNevenadressen(TypeAdresseerbaarObject.VERBLIJFSOBJECT,origineel);
						insertNevenadressen(TypeAdresseerbaarObject.VERBLIJFSOBJECT,mutation);
						deleteGerelateerdePanden(origineel);
						insertGerelateerdePanden(mutation);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating verblijfsobject: " + origineel.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Ligplaats origineel, final Ligplaats mutation) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.update(
							new PreparedStatementCreator()
							{
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
								{
									PreparedStatement ps = connection.prepareStatement(
										"update bag_ligplaats set" +
										" aanduiding_record_inactief = ?," +
										" aanduiding_record_correctie = ?," +
										" officieel = ?," +
										" ligplaats_status = ?," +
										" ligplaats_geometrie = ?," +
										" einddatum_tijdvak_geldigheid = ?," +
										" in_onderzoek = ?," +
										" bron_documentdatum = ?," +
										" bron_documentnummer = ?," +
										" bag_nummeraanduiding_id = ?" +
										" where bag_ligplaats_id = ?" +
										" and aanduiding_record_correctie = ?" +
										" and begindatum_tijdvak_geldigheid = ?"
									);
									ps.setInt(1,mutation.getAanduidingRecordInactief().ordinal());
									ps.setLong(2,mutation.getAanduidingRecordCorrectie());
									ps.setInt(3,mutation.getOfficieel().ordinal());
									ps.setInt(4,mutation.getLigplaatsStatus().ordinal());
									ps.setString(5,mutation.getLigplaatsGeometrie());
									if (mutation.getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(6,Types.TIMESTAMP);
									else
										ps.setTimestamp(6,new Timestamp(mutation.getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(7,mutation.getInOnderzoek().ordinal());
									ps.setDate(8,new Date(mutation.getDocumentdatum().getTime()));
									ps.setString(9,mutation.getDocumentnummer());
									ps.setLong(10,mutation.getHoofdAdres());
									ps.setLong(11,origineel.getIdentificatie());
									ps.setLong(12,origineel.getAanduidingRecordCorrectie());
									ps.setTimestamp(13,new Timestamp(origineel.getBegindatumTijdvakGeldigheid().getTime()));
									return ps;
								}
							}
						);
						deleteNevenadressen(TypeAdresseerbaarObject.LIGPLAATS,origineel);
						insertNevenadressen(TypeAdresseerbaarObject.LIGPLAATS,mutation);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating ligplaats: " + origineel.getIdentificatie(),e);
		}
	}

	@Override
	public void update(final Standplaats origineel, final Standplaats mutation) throws DAOException
	{
		try
		{
			transactionTemplate.execute(
				new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status)
					{
						jdbcTemplate.update(
							new PreparedStatementCreator()
							{
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
								{
									PreparedStatement ps = connection.prepareStatement(
										"update bag_standplaats set" +
										" aanduiding_record_inactief = ?," +
										" aanduiding_record_correctie = ?," +
										" officieel = ?," +
										" standplaats_status = ?," +
										" standplaats_geometrie = ?," +
										" einddatum_tijdvak_geldigheid = ?," +
										" in_onderzoek = ?," +
										" bron_documentdatum = ?," +
										" bron_documentnummer = ?," +
										" bag_nummeraanduiding_id = ?" +
										" where bag_standplaats_id = ?" +
										" and aanduiding_record_correctie = ?" +
										" and begindatum_tijdvak_geldigheid = ?"
									);
									ps.setInt(1,mutation.getAanduidingRecordInactief().ordinal());
									ps.setLong(2,mutation.getAanduidingRecordCorrectie());
									ps.setInt(3,mutation.getOfficieel().ordinal());
									ps.setInt(4,mutation.getStandplaatsStatus().ordinal());
									ps.setString(5,mutation.getStandplaatsGeometrie());
									if (mutation.getEinddatumTijdvakGeldigheid() == null)
										ps.setNull(6,Types.TIMESTAMP);
									else
										ps.setTimestamp(6,new Timestamp(mutation.getEinddatumTijdvakGeldigheid().getTime()));
									ps.setInt(7,mutation.getInOnderzoek().ordinal());
									ps.setDate(8,new Date(mutation.getDocumentdatum().getTime()));
									ps.setString(9,mutation.getDocumentnummer());
									ps.setLong(10,mutation.getHoofdAdres());
									ps.setLong(11,origineel.getIdentificatie());
									ps.setLong(12,origineel.getAanduidingRecordCorrectie());
									ps.setTimestamp(13,new Timestamp(origineel.getBegindatumTijdvakGeldigheid().getTime()));
									return ps;
								}
							}
						);
						deleteNevenadressen(TypeAdresseerbaarObject.STANDPLAATS,origineel);
						insertNevenadressen(TypeAdresseerbaarObject.STANDPLAATS,mutation);
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException("Error updating standplaats: " + origineel.getIdentificatie(),e);
		}
	}

	protected void insertGebruikersdoelen(final Verblijfsobject verblijfsobject)
	{
		jdbcTemplate.batchUpdate(
			"insert into bag_gebruiksdoel (" +
				"bag_verblijfsobject_id," +
				"aanduiding_record_correctie," +
				"begindatum_tijdvak_geldigheid," +
				"gebruiksdoel" +
			") values (?,?,?,?)",
			new BatchPreparedStatementSetter()
			{
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException
				{
					ps.setLong(1,verblijfsobject.getIdentificatie());
					ps.setLong(2,verblijfsobject.getAanduidingRecordCorrectie());
					ps.setTimestamp(3,new Timestamp(verblijfsobject.getBegindatumTijdvakGeldigheid().getTime()));
					ps.setInt(4,verblijfsobject.getGebruiksdoelenVerblijfsobject().get(i).ordinal());
				}
				
				@Override
				public int getBatchSize()
				{
					return verblijfsobject.getGebruiksdoelenVerblijfsobject().size();
				}
			}
		);
	}

	protected void insertGebruikersdoelen(final List<Verblijfsobject> verblijfsobjecten)
	{
		final List<Gebruiksdoel> batch = new ArrayList<Gebruiksdoel>();
		for (Verblijfsobject verblijfsobject : verblijfsobjecten)
			for (nl.kadaster.schemas.imbag.imbag_types.v20090901.Gebruiksdoel gebruiksdoel : verblijfsobject.getGebruiksdoelenVerblijfsobject())
				batch.add(new Gebruiksdoel(verblijfsobject,gebruiksdoel));
		jdbcTemplate.batchUpdate(
			"insert into bag_gebruiksdoel (" +
				"bag_verblijfsobject_id," +
				"aanduiding_record_correctie," +
				"begindatum_tijdvak_geldigheid," +
				"gebruiksdoel" +
			") values (?,?,?,?)",
			new BatchPreparedStatementSetter()
			{
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException
				{
					ps.setLong(1,batch.get(i).getIdentificatie());
					ps.setLong(2,batch.get(i).getAanduidingRecordCorrectie());
					ps.setTimestamp(3,new Timestamp(batch.get(i).getBegindatumTijdvakGeldigheid().getTime()));
					ps.setInt(4,batch.get(i).getGebruiksdoel().ordinal());
				}
				
				@Override
				public int getBatchSize()
				{
					return batch.size();
				}
			}
		);
	}

	protected void deleteGebruikersdoelen(final Verblijfsobject verblijfsobject)
	{
		jdbcTemplate.batchUpdate(
			"delete from bag_gebruiksdoel" +
			" where bag_verblijfsobject_id = ?" +
			" and aanduiding_record_correctie = ?" +
			" and begindatum_tijdvak_geldigheid = ?",
			new BatchPreparedStatementSetter()
			{
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException
				{
					ps.setLong(1,verblijfsobject.getIdentificatie());
					ps.setLong(2,verblijfsobject.getAanduidingRecordCorrectie());
					ps.setTimestamp(3,new Timestamp(verblijfsobject.getBegindatumTijdvakGeldigheid().getTime()));
				}
				
				@Override
				public int getBatchSize()
				{
					return verblijfsobject.getGebruiksdoelenVerblijfsobject().size();
				}
			}
		);
	}

	protected void insertNevenadressen(final TypeAdresseerbaarObject objectType, final BAGAdresseerbaarObject object)
	{
		jdbcTemplate.batchUpdate(
			"insert into bag_neven_adres (" +
				"bag_object_id," +
				"aanduiding_record_correctie," +
				"begindatum_tijdvak_geldigheid," +
				"bag_object_type," +
				"bag_nummeraanduiding_id" +
			") values (?,?,?,?,?)",
			new BatchPreparedStatementSetter()
			{
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException
				{
					ps.setLong(1,object.getIdentificatie());
					ps.setLong(2,object.getAanduidingRecordCorrectie());
					ps.setTimestamp(3,new Timestamp(object.getBegindatumTijdvakGeldigheid().getTime()));
					ps.setInt(4,objectType.ordinal());
					ps.setLong(5,object.getNevenAdressen().get(i));
				}
				
				@Override
				public int getBatchSize()
				{
					return object.getNevenAdressen().size();
				}
			}
		);
	}

	protected void insertNevenadressen(final TypeAdresseerbaarObject objectType, final List<? extends BAGAdresseerbaarObject> objects)
	{
		final List<NevenAdres> batch = new ArrayList<NevenAdres>();
		for (BAGAdresseerbaarObject object : objects)
			for (Long nevenAdres : object.getNevenAdressen())
				batch.add(new NevenAdres(object,nevenAdres));
		jdbcTemplate.batchUpdate(
			"insert into bag_neven_adres (" +
				"bag_object_id," +
				"aanduiding_record_correctie," +
				"begindatum_tijdvak_geldigheid," +
				"bag_object_type," +
				"bag_nummeraanduiding_id" +
			") values (?,?,?,?,?)",
			new BatchPreparedStatementSetter()
			{
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException
				{
					ps.setLong(1,batch.get(i).getIdentificatie());
					ps.setLong(2,batch.get(i).getAanduidingRecordCorrectie());
					ps.setTimestamp(3,new Timestamp(batch.get(i).getBegindatumTijdvakGeldigheid().getTime()));
					ps.setInt(4,objectType.ordinal());
					ps.setLong(5,batch.get(i).getNevenAdres());
				}
				
				@Override
				public int getBatchSize()
				{
					return batch.size();
				}
			}
		);
	}

	protected void deleteNevenadressen(final TypeAdresseerbaarObject objectType, final BAGAdresseerbaarObject object)
	{
		jdbcTemplate.batchUpdate(
			"delete from bag_neven_adres" +
			" where bag_object_id = ?" +
			" and bag_object_type = ?" +
			" and aanduiding_record_correctie = ?" +
			" and begindatum_tijdvak_geldigheid = ?",
			new BatchPreparedStatementSetter()
			{
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException
				{
					ps.setLong(1,object.getIdentificatie());
					ps.setInt(2,objectType.ordinal());
					ps.setLong(3,object.getAanduidingRecordCorrectie());
					ps.setTimestamp(4,new Timestamp(object.getBegindatumTijdvakGeldigheid().getTime()));
				}
				
				@Override
				public int getBatchSize()
				{
					return object.getNevenAdressen().size();
				}
			}
		);
	}

	protected void insertGerelateerdePanden(final Verblijfsobject verblijfsobject)
	{
		jdbcTemplate.batchUpdate(
			"insert into bag_gerelateerd_pand (" +
				"bag_verblijfsobject_id," +
				"aanduiding_record_correctie," +
				"begindatum_tijdvak_geldigheid," +
				"bag_pand_id" +
			") values (?,?,?,?)",
			new BatchPreparedStatementSetter()
			{
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException
				{
					ps.setLong(1,verblijfsobject.getIdentificatie());
					ps.setLong(2,verblijfsobject.getAanduidingRecordCorrectie());
					ps.setTimestamp(3,new Timestamp(verblijfsobject.getBegindatumTijdvakGeldigheid().getTime()));
					ps.setLong(4,verblijfsobject.getGerelateerdPanden().get(i));
				}
				
				@Override
				public int getBatchSize()
				{
					return verblijfsobject.getGerelateerdPanden().size();
				}
			}
		);
	}

	protected void insertGerelateerdePanden(final List<Verblijfsobject> verblijfsobjecten)
	{
		final List<GerelateerdPand> batch = new ArrayList<GerelateerdPand>();
		for (Verblijfsobject verblijfsobject : verblijfsobjecten)
			for (Long gerelateerdPand : verblijfsobject.getGerelateerdPanden())
				batch.add(new GerelateerdPand(verblijfsobject,gerelateerdPand));
		jdbcTemplate.batchUpdate(
			"insert into bag_gerelateerd_pand (" +
				"bag_verblijfsobject_id," +
				"aanduiding_record_correctie," +
				"begindatum_tijdvak_geldigheid," +
				"bag_pand_id" +
			") values (?,?,?,?)",
			new BatchPreparedStatementSetter()
			{
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException
				{
					ps.setLong(1,batch.get(i).getIdentificatie());
					ps.setLong(2,batch.get(i).getAanduidingRecordCorrectie());
					ps.setTimestamp(3,new Timestamp(batch.get(i).getBegindatumTijdvakGeldigheid().getTime()));
					ps.setLong(4,batch.get(i).getGerelateerdPand());
				}
				
				@Override
				public int getBatchSize()
				{
					return batch.size();
				}
			}
		);
	}

	protected void deleteGerelateerdePanden(final Verblijfsobject verblijfsobject)
	{
		jdbcTemplate.update(
			new PreparedStatementCreator()
			{
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
				{
					PreparedStatement ps = connection.prepareStatement(
						"delete from bag_gerelateerd_pand" +
						" where bag_verblijfsobject_id = ?" +
						" and aanduiding_record_correctie = ?" +
						" and begindatum_tijdvak_geldigheid = ?"
					);
					ps.setLong(1,verblijfsobject.getIdentificatie());
					ps.setLong(2,verblijfsobject.getAanduidingRecordCorrectie());
					ps.setTimestamp(3,new Timestamp(verblijfsobject.getBegindatumTijdvakGeldigheid().getTime()));
					return ps;
				}
			}
		);
	}

	@Override
	public void delete(final Woonplaats woonplaats)
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"delete from bag_woonplaats" +
							" where bag_woonplaats_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?" +
							" and einddatum_tijdvak_geldigheid = ?"
						);
						ps.setLong(1,woonplaats.getIdentificatie());
						ps.setLong(2,woonplaats.getAanduidingRecordCorrectie());
						ps.setTimestamp(3,new Timestamp(woonplaats.getBegindatumTijdvakGeldigheid().getTime()));
						if (woonplaats.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(4,Types.TIMESTAMP);
						else
							ps.setTimestamp(4,new Timestamp(woonplaats.getEinddatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(final OpenbareRuimte openbareRuimte)
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"delete from bag_openbare_ruimte" +
							" where bag_openbare_ruimte_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?" +
							" and einddatum_tijdvak_geldigheid = ?"
						);
						ps.setLong(1,openbareRuimte.getIdentificatie());
						ps.setLong(2,openbareRuimte.getAanduidingRecordCorrectie());
						ps.setTimestamp(3,new Timestamp(openbareRuimte.getBegindatumTijdvakGeldigheid().getTime()));
						if (openbareRuimte.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(4,Types.TIMESTAMP);
						else
							ps.setTimestamp(4,new Timestamp(openbareRuimte.getEinddatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(final Nummeraanduiding nummeraanduiding)
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"delete from bag_nummeraanduiding" +
							" where bag_nummeraanduiding_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?" +
							" and einddatum_tijdvak_geldigheid = ?"
						);
						ps.setLong(1,nummeraanduiding.getIdentificatie());
						ps.setLong(2,nummeraanduiding.getAanduidingRecordCorrectie());
						ps.setTimestamp(3,new Timestamp(nummeraanduiding.getBegindatumTijdvakGeldigheid().getTime()));
						if (nummeraanduiding.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(4,Types.TIMESTAMP);
						else
							ps.setTimestamp(4,new Timestamp(nummeraanduiding.getEinddatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(final Verblijfsobject verblijfsobject)
	{
		try
		{
			deleteGebruikersdoelen(verblijfsobject);
			deleteNevenadressen(TypeAdresseerbaarObject.VERBLIJFSOBJECT,verblijfsobject);
			deleteGerelateerdePanden(verblijfsobject);
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"delete from bag_verblijfsobject" +
							" where bag_verblijfsobject_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?" +
							" and einddatum_tijdvak_geldigheid = ?"
						);
						ps.setLong(1,verblijfsobject.getIdentificatie());
						ps.setLong(2,verblijfsobject.getAanduidingRecordCorrectie());
						ps.setTimestamp(3,new Timestamp(verblijfsobject.getBegindatumTijdvakGeldigheid().getTime()));
						if (verblijfsobject.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(4,Types.TIMESTAMP);
						else
							ps.setTimestamp(4,new Timestamp(verblijfsobject.getEinddatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(final Pand pand)
	{
		try
		{
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"delete from bag_pand" +
							" where bag_pand_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?" +
							" and einddatum_tijdvak_geldigheid = ?"
						);
						ps.setLong(1,pand.getIdentificatie());
						ps.setLong(2,pand.getAanduidingRecordCorrectie());
						ps.setTimestamp(3,new Timestamp(pand.getBegindatumTijdvakGeldigheid().getTime()));
						if (pand.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(4,Types.TIMESTAMP);
						else
							ps.setTimestamp(4,new Timestamp(pand.getEinddatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(final Ligplaats ligplaats)
	{
		try
		{
			deleteNevenadressen(TypeAdresseerbaarObject.LIGPLAATS,ligplaats);
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"delete from bag_ligplaats" +
							" where bag_ligplaats_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?" +
							" and einddatum_tijdvak_geldigheid = ?"
						);
						ps.setLong(1,ligplaats.getIdentificatie());
						ps.setLong(2,ligplaats.getAanduidingRecordCorrectie());
						ps.setTimestamp(3,new Timestamp(ligplaats.getBegindatumTijdvakGeldigheid().getTime()));
						if (ligplaats.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(4,Types.TIMESTAMP);
						else
							ps.setTimestamp(4,new Timestamp(ligplaats.getEinddatumTijdvakGeldigheid().getTime()));
						return ps;
					}
				}
			);
		}
		catch (DataAccessException e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(final Standplaats standplaats)
	{
		try
		{
			deleteNevenadressen(TypeAdresseerbaarObject.STANDPLAATS,standplaats);
			jdbcTemplate.update(
				new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement ps = connection.prepareStatement(
							"delete from bag_standplaats" +
							" where bag_standplaats_id = ?" +
							" and aanduiding_record_correctie = ?" +
							" and begindatum_tijdvak_geldigheid = ?" +
							" and einddatum_tijdvak_geldigheid = ?"
						);
						ps.setLong(1,standplaats.getIdentificatie());
						ps.setLong(2,standplaats.getAanduidingRecordCorrectie());
						ps.setTimestamp(3,new Timestamp(standplaats.getBegindatumTijdvakGeldigheid().getTime()));
						if (standplaats.getEinddatumTijdvakGeldigheid() == null)
							ps.setNull(4,Types.TIMESTAMP);
						else
							ps.setTimestamp(4,new Timestamp(standplaats.getEinddatumTijdvakGeldigheid().getTime()));
						return ps;
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
