package nl.ordina.bag.etl.model;

public class NevenAdres
{
	private BAGAdresseerbaarObject object;
	private Long nevenAdres;

	public NevenAdres(BAGAdresseerbaarObject object, Long nevenAdres)
	{
		this.object = object;
		this.nevenAdres = nevenAdres;
	}

	public long getIdentificatie()
	{
		return object.getIdentificatie();
	}

	public long getAanduidingRecordCorrectie()
	{
		return object.getAanduidingRecordCorrectie();
	}

	public java.util.Date getBegindatumTijdvakGeldigheid()
	{
		return object.getBegindatumTijdvakGeldigheid();
	}

	public long getNevenAdres()
	{
		return nevenAdres;
	}

}
