package nl.ordina.bag.etl.model;

import java.util.Date;

public class GerelateerdPand
{
	private Verblijfsobject verblijfsobject;
	private Long gerelateerdPand;

	public GerelateerdPand(Verblijfsobject verblijfsobject, Long gerelateerdPand)
	{
		this.verblijfsobject = verblijfsobject;
		this.gerelateerdPand = gerelateerdPand;
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
	
	public Long getGerelateerdPand()
	{
		return gerelateerdPand;
	}
}
