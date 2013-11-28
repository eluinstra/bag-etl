package nl.ordina.bag.etl.model;

import java.util.Date;

public class Gebruiksdoel
{
	private Verblijfsobject verblijfsobject;
	private nl.kadaster.schemas.imbag.imbag_types.v20090901.Gebruiksdoel gebruiksdoel;

	public Gebruiksdoel(Verblijfsobject verblijfsobject, nl.kadaster.schemas.imbag.imbag_types.v20090901.Gebruiksdoel gebruiksdoel)
	{
		this.verblijfsobject = verblijfsobject;
		this.gebruiksdoel = gebruiksdoel;
	}

	public long getIdentificatie()
	{
		return verblijfsobject.getIdentificatie();
	}

	public long getAanduidingRecordCorrectie()
	{
		return verblijfsobject.getAanduidingRecordCorrectie();
	}

	public Date getBegindatumTijdvakGeldigheid()
	{
		return verblijfsobject.getBegindatumTijdvakGeldigheid();
	}

	public nl.kadaster.schemas.imbag.imbag_types.v20090901.Gebruiksdoel getGebruiksdoel()
	{
		return gebruiksdoel;
	}

	public void setGebruiksdoel(nl.kadaster.schemas.imbag.imbag_types.v20090901.Gebruiksdoel gebruiksdoel)
	{
		this.gebruiksdoel = gebruiksdoel;
	}
}
