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

import java.util.Date;

import nl.kadaster.schemas.imbag.imbag_types.v20090901.Indicatie;

public abstract class AbstractBAGObject implements BAGObject
{
	private long identificatie;
	private Indicatie aanduidingRecordInactief;
	private long aanduidingRecordCorrectie;
	private Indicatie officieel;
	private Date begindatumTijdvakGeldigheid;
	private Date einddatumTijdvakGeldigheid;
	private Indicatie inOnderzoek;
	private Date documentdatum;
	private String documentnummer;
	
	@Override
	public long getIdentificatie()
	{
		return identificatie;
	}
	
	public void setIdentificatie(long identificatie)
	{
		this.identificatie = identificatie;
	}
	
	@Override
	public Indicatie getAanduidingRecordInactief()
	{
		return aanduidingRecordInactief;
	}
	
	public void setAanduidingRecordInactief(Indicatie aanduidingRecordInactief)
	{
		this.aanduidingRecordInactief = aanduidingRecordInactief;
	}
	
	@Override
	public long getAanduidingRecordCorrectie()
	{
		return aanduidingRecordCorrectie;
	}
	
	public void setAanduidingRecordCorrectie(long aanduidingRecordCorrectie)
	{
		this.aanduidingRecordCorrectie = aanduidingRecordCorrectie;
	}
	
	@Override
	public Indicatie getOfficieel()
	{
		return officieel;
	}
	
	public void setOfficieel(Indicatie officieel)
	{
		this.officieel = officieel;
	}
	
	@Override
	public Date getBegindatumTijdvakGeldigheid()
	{
		return begindatumTijdvakGeldigheid;
	}
	
	public void setBegindatumTijdvakGeldigheid(Date begindatumTijdvakGeldigheid)
	{
		this.begindatumTijdvakGeldigheid = begindatumTijdvakGeldigheid;
	}
	
	@Override
	public Date getEinddatumTijdvakGeldigheid()
	{
		return einddatumTijdvakGeldigheid;
	}
	
	public void setEinddatumTijdvakGeldigheid(Date einddatumTijdvakGeldigheid)
	{
		this.einddatumTijdvakGeldigheid = einddatumTijdvakGeldigheid;
	}
	
	@Override
	public Indicatie getInOnderzoek()
	{
		return inOnderzoek;
	}
	
	public void setInOnderzoek(Indicatie inOnderzoek)
	{
		this.inOnderzoek = inOnderzoek;
	}
	
	@Override
	public Date getDocumentdatum()
	{
		return documentdatum;
	}
	
	public void setDocumentdatum(Date documentdatum)
	{
		this.documentdatum = documentdatum;
	}
	
	@Override
	public String getDocumentnummer()
	{
		return documentnummer;
	}
	
	public void setDocumentnummer(String documentnummer)
	{
		this.documentnummer = documentnummer;
	}
	
}
