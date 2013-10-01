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
package nl.ordina.bag.etl.service;

import nl.kadaster.schemas.bag_verstrekkingen.extract_levering.v20090901.BAGExtractLevering;
import nl.ordina.bag.etl.Utils.FileType;
import nl.ordina.bag.etl.ValidationException;

import org.apache.commons.lang.StringUtils;

public class BAGExtractLeveringValidator
{
	private String klantnummer;
	private String productcode;
	private String gebiedType;
	private String gegevensvariant;
	private String formaat;
	private String producttype;
	private String productversie;
	
	public BAGExtractLeveringValidator()
	{
	}

	public BAGExtractLeveringValidator(String klantnummer, String productcode, String gebiedType, String gegevensvariant, String formaat, String producttype, String productversie)
	{
		this.klantnummer = klantnummer;
		this.productcode = productcode;
		this.gebiedType = gebiedType;
		this.gegevensvariant = gegevensvariant;
		this.formaat = formaat;
		this.producttype = producttype;
		this.productversie = productversie;
	}

	void validate(FileType fileType, BAGExtractLevering bagExtractLevering)
	{
		if (bagExtractLevering == null)
			throw new ValidationException(fileType.filename.concat(" not found!"));
		validate("Klantnummer",klantnummer,bagExtractLevering.getMetadata().getKlantgegevens().getKlantnummer());
		validate("Productcode",productcode,bagExtractLevering.getMetadata().getProductgegevens().getProductcode());
		validate("GebiedType",gebiedType,bagExtractLevering.getMetadata().getProductgegevens().getGebiedType());
		validate("Gegevensvariant",gegevensvariant,bagExtractLevering.getMetadata().getProductgegevens().getGegevensvariant());
		validate("Formaat",formaat,bagExtractLevering.getMetadata().getProductgegevens().getFormaat());
		validate("Producttype",producttype,bagExtractLevering.getMetadata().getProductgegevens().getProducttype());
		validate("Productversie",productversie,bagExtractLevering.getMetadata().getProductgegevens().getProductversie());
	}

	private void validate(String name, String expectedValue, String actualValue)
	{
		if (!StringUtils.isEmpty(expectedValue) && !expectedValue.equals(actualValue))
		{
			throw new ValidationException(name + " " + actualValue + " found. " + name + " " + expectedValue + " expected.");
		}
	}

	public String getKlantnummer()
	{
		return klantnummer;
	}

	public void setKlantnummer(String klantnummer)
	{
		this.klantnummer = klantnummer;
	}

	public String getProductcode()
	{
		return productcode;
	}

	public void setProductcode(String productcode)
	{
		this.productcode = productcode;
	}

	public String getGebiedType()
	{
		return gebiedType;
	}

	public void setGebiedType(String gebiedType)
	{
		this.gebiedType = gebiedType;
	}

	public String getGegevensvariant()
	{
		return gegevensvariant;
	}

	public void setGegevensvariant(String gegevensvariant)
	{
		this.gegevensvariant = gegevensvariant;
	}

	public String getFormaat()
	{
		return formaat;
	}

	public void setFormaat(String formaat)
	{
		this.formaat = formaat;
	}

	public String getProducttype()
	{
		return producttype;
	}

	public void setProducttype(String producttype)
	{
		this.producttype = producttype;
	}

	public String getProductversie()
	{
		return productversie;
	}

	public void setProductversie(String productversie)
	{
		this.productversie = productversie;
	}
	
	
}
