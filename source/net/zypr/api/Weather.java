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
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIInvalidServiceException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.api.vo.ServiceVO;
import net.zypr.api.vo.WeatherCurrentVO;
import net.zypr.api.vo.WeatherForecastVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Weather
  extends Protocol
{

  public Weather()
    throws APICommunicationException, APIProtocolException
  {
    super(true);
  }

  public Weather(ServiceVO[] services)
  {
    super(services);
  }

  public String getDefaultServiceForCurrent()
  {
    return (getDefaultService(APIVerbs.WEATHER_CURRENT));
  }

  public WeatherCurrentVO current(String placename)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (current(getDefaultServiceForCurrent(), placename, null));
  }

  public WeatherCurrentVO current(String service, String placename)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (current(service, placename, null));
  }

  public WeatherCurrentVO current(GeoPositionVO geoPosition)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (current(getDefaultServiceForCurrent(), null, geoPosition));
  }

  public WeatherCurrentVO current(String service, GeoPositionVO geoPosition)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (current(service, null, geoPosition));
  }

  private WeatherCurrentVO current(String service, String placename, GeoPositionVO geoPosition)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForCurrent();
    if (!doesVerbExist(service, APIVerbs.WEATHER_CURRENT))
      throw new APIInvalidServiceException(APIVerbs.WEATHER_CURRENT, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    if (placename != null)
      parameters.put("placename", placename);
    if (geoPosition != null)
      {
        JSONObject pointJSONObject = new JSONObject();
        pointJSONObject.put("point", geoPosition);
        parameters.put("point", pointJSONObject.toJSONString());
      }
    WeatherCurrentVO weatherCurrentVO;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.WEATHER_CURRENT, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          weatherCurrentVO = new WeatherCurrentVO(getDataKeyedObjectJSON(jsonObject, "weather_current"));
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
    return (weatherCurrentVO);
  }

  public String getDefaultServiceForForecast()
  {
    return (getDefaultService(APIVerbs.WEATHER_FORECAST));
  }

  public WeatherForecastVO[] forecast(String placename)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (forecast(getDefaultServiceForForecast(), placename, null));
  }

  public WeatherForecastVO[] forecast(String service, String placename)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (forecast(service, placename, null));
  }

  public WeatherForecastVO[] forecast(GeoPositionVO geoPosition)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (forecast(getDefaultServiceForForecast(), null, geoPosition));
  }

  public WeatherForecastVO[] forecast(String service, GeoPositionVO geoPosition)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (forecast(service, null, geoPosition));
  }

  private WeatherForecastVO[] forecast(String service, String placename, GeoPositionVO geoPosition)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForForecast();
    if (!doesVerbExist(service, APIVerbs.WEATHER_FORECAST))
      throw new APIInvalidServiceException(APIVerbs.WEATHER_FORECAST, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    if (placename != null)
      parameters.put("placename", placename);
    if (geoPosition != null)
      {
        JSONObject pointJSONObject = new JSONObject();
        pointJSONObject.put("point", geoPosition);
        parameters.put("point", pointJSONObject.toJSONString());
      }
    WeatherForecastVO[] weatherForecastVOs;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.WEATHER_FORECAST, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonArray = getDataKeyedArrayJSON(jsonObject, "weather_forecast");
            weatherForecastVOs = new WeatherForecastVO[jsonArray.size()];
            for (int index = 0; index < weatherForecastVOs.length; index++)
              weatherForecastVOs[index] = new WeatherForecastVO((JSONObject) jsonArray.get(index));
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
    return (weatherForecastVOs);
  }
}
