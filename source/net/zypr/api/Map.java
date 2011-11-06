/**
 * 
 * Initial version of this code (c) 2009-2011 Media Tuners LLC with a full license to Pioneer Corporation.
 * 
 * Pioneer Corporation licenses this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 * 
 */


package net.zypr.api;

import java.util.Hashtable;

import net.zypr.api.enums.APIVerbs;
import net.zypr.api.enums.MapFormat;
import net.zypr.api.enums.MapType;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIInvalidServiceException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.vo.AddressVO;
import net.zypr.api.vo.GeoBoundsVO;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.api.vo.ServiceVO;

import org.json.simple.JSONObject;

public class Map
  extends Protocol
{
  public Map()
    throws APICommunicationException, APIProtocolException
  {
    super(true);
  }

  public Map(ServiceVO[] services)
  {
    super(services);
  }

  public String getDefaultServiceForGet()
  {
    return (getDefaultService(APIVerbs.MAP_GET));
  }

  public byte[] get(int x, int y, int zoom, MapType type, String service)
    throws APICommunicationException
  {
    return (get(x, y, zoom, type, service, MapFormat.DEFAULT));
  }

  public byte[] get(int x, int y, int zoom, String service)
    throws APICommunicationException
  {
    return (get(x, y, zoom, MapType.DEFAULT, service, MapFormat.DEFAULT));
  }

  public byte[] get(int x, int y, int zoom, MapType type)
    throws APICommunicationException
  {
    return (get(x, y, zoom, type, null, MapFormat.DEFAULT));
  }

  public byte[] get(int x, int y, int zoom)
    throws APICommunicationException
  {
    return (get(x, y, zoom, MapType.DEFAULT, null, MapFormat.DEFAULT));
  }

  public byte[] get(int x, int y, int zoom, MapType type, String service, MapFormat format)
    throws APICommunicationException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForGet();
    if (!doesVerbExist(service, APIVerbs.MAP_GET))
      throw new APIInvalidServiceException(APIVerbs.MAP_GET, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("x", "" + x);
    parameters.put("y", "" + y);
    parameters.put("z", "" + zoom);
    if (type != null)
      parameters.put("type", type.getName());
    else
      parameters.put("type", MapType.DEFAULT.getName());
    parameters.put("service", service);
    if (format != null)
      parameters.put("format", format.getName());
    else
      parameters.put("format", MapFormat.DEFAULT.getName());
    if (format != null)
      parameters.put("format", format.getName());
    else
      parameters.put("format", MapFormat.DEFAULT.getName());
    return (doGetBytes(APIVerbs.MAP_GET, parameters));
  }

  public AddressVO reverseGeocode(GeoPositionVO geoPosition)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("target_lat", "" + geoPosition.getLatitude());
    parameters.put("target_lng", "" + geoPosition.getLongitude());
    AddressVO addressVO;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.MAP_REVERSE_GEOCODE, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          addressVO = new AddressVO(getDataKeyedObjectJSON(jsonObject, "address"));
        else
          throw new APIServerErrorException(getStatusMessage(jsonObject));
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    catch (NullPointerException nullPointerException)
      {
        throw new APIProtocolException(nullPointerException);
      }
    return (addressVO);
  }

  public GeoPositionVO geocode(String city, String state)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (geocode(new AddressVO(city, state)));
  }

  public GeoPositionVO geocode(AddressVO address)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("address", address.toJSONString());
    GeoPositionVO geoPosition;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.MAP_GEOCODE, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          geoPosition = new GeoPositionVO(getDataKeyedObjectJSON(jsonObject, "point"));
        else
          throw new APIServerErrorException(getStatusMessage(jsonObject));
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    catch (NullPointerException nullPointerException)
      {
        throw new APIProtocolException(nullPointerException);
      }
    return (geoPosition);
  }

  public GeoBoundsVO placenameGeoCode(String search)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("search", search);
    GeoBoundsVO geoBounds;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.MAP_PLACENAME_GEOCODE, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONObject jsonGeographyObject = getDataKeyedObjectJSON(jsonObject, "geography");
            if (jsonGeographyObject == null)
              return (null);
            geoBounds = new GeoBoundsVO((JSONObject) jsonGeographyObject.get("bbox"));
          }
        else
          {
            throw new APIServerErrorException(getStatusMessage(jsonObject));
          }
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    catch (NullPointerException nullPointerException)
      {
        throw new APIProtocolException(nullPointerException);
      }
    return (geoBounds);
  }

  public byte[] getMarker(String url)
    throws APICommunicationException
  {
    return (doGetBytes(url));
  }
}
