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
package nl.ordina.bag.etl.xml;

import javax.xml.bind.JAXBException;

import net.opengis.gml.MultiSurfaceType;
import net.opengis.gml.ObjectFactory;
import net.opengis.gml.PointType;
import net.opengis.gml.PolygonType;
import net.opengis.gml.SurfacePropertyType;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.PuntOfVlak;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.VlakOfMultiVlak;

public class BAGGeometrieHandler
{
	public String handle(VlakOfMultiVlak vlakOfMultiVlak) throws HandlerException
	{
		try
		{
			if (vlakOfMultiVlak.getSurface() != null)
				return XMLMessageBuilder.getInstance(PolygonType.class).handle(new ObjectFactory().createPolygon((PolygonType)vlakOfMultiVlak.getSurface().getValue()));
			else if (vlakOfMultiVlak.getMultiSurface() != null)
				return XMLMessageBuilder.getInstance(MultiSurfaceType.class).handle(new ObjectFactory().createMultiSurface(vlakOfMultiVlak.getMultiSurface()));
			return null;
		}
		catch (JAXBException e)
		{
			throw new HandlerException(e);
		}
	}

	public String handle(SurfacePropertyType surfaceProperty)
	{
		try
		{
			if (surfaceProperty.getSurface() != null)
				return XMLMessageBuilder.getInstance(PolygonType.class).handle(new ObjectFactory().createPolygon((PolygonType)surfaceProperty.getSurface().getValue()));
			return null;
		}
		catch (JAXBException e)
		{
			throw new HandlerException(e);
		}
	}

	public String handle(PuntOfVlak puntOfVlak)
	{
		try
		{
			if (puntOfVlak.getPoint() != null)
				return XMLMessageBuilder.getInstance(PointType.class).handle(new ObjectFactory().createPoint(puntOfVlak.getPoint()));
			else if (puntOfVlak.getSurface() != null)
				return XMLMessageBuilder.getInstance(PolygonType.class).handle(new ObjectFactory().createPolygon((PolygonType)puntOfVlak.getSurface().getValue()));
			return null;
		}
		catch (JAXBException e)
		{
			throw new HandlerException(e);
		}
	}
}
