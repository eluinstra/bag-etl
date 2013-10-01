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
package nl.ordina.bag.etl.model.mutatie;

import java.util.Date;

import nl.ordina.bag.etl.Constants.ProcessingStatus;

public class MutatiesFile
{
	private long id;
	private Date dateFrom;
	private Date dateTo;
	private byte[] content;
	private ProcessingStatus status;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public Date getDateFrom()
	{
		return dateFrom;
	}

	public void setDateFrom(Date datestamp)
	{
		this.dateFrom = datestamp;
	}
	
	public Date getDateTo()
	{
		return dateTo;
	}
	
	public void setDateTo(Date dateTo)
	{
		this.dateTo = dateTo;
	}

	public byte[] getContent()
	{
		return content;
	}

	public void setContent(byte[] content)
	{
		this.content = content;
	}

	public ProcessingStatus getStatus()
	{
		return status;
	}

	public void setStatus(ProcessingStatus status)
	{
		this.status = status;
	}

}
