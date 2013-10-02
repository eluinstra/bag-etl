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

public class Constants
{
	public static final String BAG_DATETIME_FORMAT = "yyyyMMddHHmmssSS";
	public static final String BAG_DATE_FORMAT = "yyyyMMdd";
	public static final String DATE_FORMAT = "dd-MM-yyyy";

	public enum ProcessingStatus
	{
		UNPROCESSED, PROCESSED;
	}

	public enum BAGObjectType
	{
		WOONPLAATS("WPL","Woonplaats"), OPENBARE_RUIMTE("OPR","Openbare ruimte"), NUMMERAANDUIDING("NUM","Nummeraanduiding"), PAND("PND","Pand"), VERBLIJFSOBJECT("VBO","Verblijfsobject"), LIGPLAATS("LIG","Ligplaats"), STANDPLAATS("STA","Standplaats");
		
		private String code;
		private String name;
		
		private BAGObjectType(String code, String name)
		{
			this.code = code;
			this.name = name;
		}
		
		public static BAGObjectType getBAGObjectType(String code)
		{
			for (BAGObjectType bAGObjectType : BAGObjectType.values())
				if (bAGObjectType.code.equals(code))
					return bAGObjectType;
			return null;
		}

		public static BAGObjectType getBAGObjectTypeByFilename(String filename)
		{
			for (BAGObjectType bAGObjectType : BAGObjectType.values())
				if (filename.contains(bAGObjectType.code))
					return bAGObjectType;
			return null;
		}
		
		public String getCode()
		{
			return code;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}

}
