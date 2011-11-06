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

import java.util.Date;
import java.util.Hashtable;

import net.zypr.api.enums.APIVerbs;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIInvalidServiceException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.utils.DateUtils;
import net.zypr.api.vo.GeoPointVO;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.api.vo.RouteVO;
import net.zypr.api.vo.ServiceVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Nav
  extends Protocol
{
  public Nav()
    throws APICommunicationException, APIProtocolException
  {
    super(true);
  }

  public Nav(ServiceVO[] services)
  {
    super(services);
  }

  public String getDefaultServiceForRouteGet()
  {
    return (getDefaultService(APIVerbs.NAV_ROUTE_GET));
  }

  public RouteVO routeGet(GeoPositionVO geoPositionStart, GeoPositionVO geoPositionEnd)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    GeoPointVO[] geoPoints = new GeoPointVO[2];
    geoPoints[0] = new GeoPointVO(geoPositionStart);
    geoPoints[1] = new GeoPointVO(geoPositionEnd);
    return (routeGet(geoPoints));
  }

  public RouteVO routeGet(GeoPointVO[] geoPoints)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (routeGet(null, geoPoints, null, null));
  }

  public RouteVO routeGet(GeoPointVO[] geoPoints, Date startTime, Date arrivalTime)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (routeGet(null, geoPoints, startTime, arrivalTime));
  }

  public RouteVO routeGet(String service, GeoPointVO[] geoPoints, Date startTime, Date arrivalTime)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForRouteGet();
    if (!doesVerbExist(service, APIVerbs.NAV_ROUTE_GET))
      throw new APIInvalidServiceException(APIVerbs.NAV_ROUTE_GET, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    JSONArray jsonArray = new JSONArray();
    for (int index = 0; index < geoPoints.length; index++)
      jsonArray.add(geoPoints[index].toJSON());
    parameters.put("points", jsonArray.toJSONString());
    if (startTime != null)
      parameters.put("start_time", DateUtils.getISO8601String(startTime));
    if (arrivalTime != null)
      parameters.put("arrival_time", DateUtils.getISO8601String(arrivalTime));
    RouteVO routeVO = null;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.NAV_ROUTE_GET, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          routeVO = new RouteVO(getDataKeyedObjectJSON(jsonObject, "routedetails"));
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
    return (routeVO);
  }
}
