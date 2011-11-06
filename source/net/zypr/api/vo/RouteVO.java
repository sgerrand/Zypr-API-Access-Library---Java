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


package net.zypr.api.vo;

import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.Debug;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class RouteVO
  extends GenericVO
{
  private long _travelTimeInMinutes;
  private long _averageSpeed;
  private boolean _trafficaware;
  private double _distance;
  private ManuverVO[] _manuvers;
  private GeoBoundsVO _geoBounds;
  private GeoPositionVO[] _geoPositions;

  public RouteVO()
  {
    super();
  }

  public RouteVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _travelTimeInMinutes = (Long) jsonObject.get("traveltimeminutes");
        _averageSpeed = (Long) jsonObject.get("averagespeed");
        _trafficaware = (Boolean) jsonObject.get("trafficaware");
        _distance = (Double) jsonObject.get("distance");
        JSONArray jsonArray = (JSONArray) jsonObject.get("maneuvers");
        _manuvers = new ManuverVO[jsonArray.size()];
        for (int index = 0; index < _manuvers.length; index++)
          _manuvers[index] = new ManuverVO((JSONObject) jsonArray.get(index));
        jsonArray = (JSONArray) jsonObject.get("line");
        _geoPositions = new GeoPositionVO[jsonArray.size() / 2];
        for (int index = 0; index < _geoPositions.length; index++)
          _geoPositions[index] = new GeoPositionVO(Double.parseDouble("" + jsonArray.get(index * 2)), Double.parseDouble("" + jsonArray.get(index * 2 + 1)));
        _geoBounds = new GeoBoundsVO((JSONObject) jsonObject.get("bbox"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
        throw new APIProtocolException(exception);
      }
  }

  public void setTravelTimeInMinutes(long _travelTimeInMinutes)
  {
    long oldTravelTimeInMinutes = _travelTimeInMinutes;
    this._travelTimeInMinutes = _travelTimeInMinutes;
    propertyChangeSupport.firePropertyChange("TravelTimeInMinutes", oldTravelTimeInMinutes, _travelTimeInMinutes);
  }

  public long getTravelTimeInMinutes()
  {
    return (_travelTimeInMinutes);
  }

  public void setAverageSpeed(long _averageSpeed)
  {
    long oldAverageSpeed = _averageSpeed;
    this._averageSpeed = _averageSpeed;
    propertyChangeSupport.firePropertyChange("AverageSpeed", oldAverageSpeed, _averageSpeed);
  }

  public long getAverageSpeed()
  {
    return (_averageSpeed);
  }

  public void setTrafficaware(boolean _trafficaware)
  {
    boolean oldTrafficaware = _trafficaware;
    this._trafficaware = _trafficaware;
    propertyChangeSupport.firePropertyChange("Trafficaware", oldTrafficaware, _trafficaware);
  }

  public boolean isTrafficaware()
  {
    return (_trafficaware);
  }

  public void setDistance(double distance)
  {
    double oldDistance = distance;
    this._distance = distance;
    propertyChangeSupport.firePropertyChange("Distance", oldDistance, distance);
  }

  public double getDistance()
  {
    return (_distance);
  }

  public void setManuvers(ManuverVO[] _manuvers)
  {
    ManuverVO[] oldManuvers = _manuvers;
    this._manuvers = _manuvers;
    propertyChangeSupport.firePropertyChange("Manuvers", oldManuvers, _manuvers);
  }

  public ManuverVO[] getManuvers()
  {
    return (_manuvers);
  }

  public void setGeoBounds(GeoBoundsVO _geoBounds)
  {
    GeoBoundsVO oldGeoBounds = _geoBounds;
    this._geoBounds = _geoBounds;
    propertyChangeSupport.firePropertyChange("GeoBounds", oldGeoBounds, _geoBounds);
  }

  public GeoBoundsVO getGeoBounds()
  {
    return (_geoBounds);
  }

  public void setGeoPositions(GeoPositionVO[] _geoPositions)
  {
    GeoPositionVO[] oldGeoPositions = _geoPositions;
    this._geoPositions = _geoPositions;
    propertyChangeSupport.firePropertyChange("GeoPositions", oldGeoPositions, _geoPositions);
  }

  public GeoPositionVO[] getGeoPositions()
  {
    return (_geoPositions);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("traveltimeminutes", _travelTimeInMinutes);
    jsonObject.put("averagespeed", _averageSpeed);
    jsonObject.put("trafficaware", _trafficaware);
    jsonObject.put("distance", _distance);
    JSONArray jsonArray = new JSONArray();
    for (int index = 0; index < _manuvers.length; index++)
      jsonArray.add(_manuvers[index].toJSON());
    jsonObject.put("maneuvers", jsonArray);
    jsonArray = null;
    jsonArray = new JSONArray();
    for (int index = 0; index < _geoPositions.length; index++)
      {
        jsonArray.add(_geoPositions[index].getLatitude());
        jsonArray.add(_geoPositions[index].getLongitude());
      }
    jsonObject.put("line", jsonArray);
    jsonObject.put("bbox", _geoBounds.toJSON());
    return (jsonObject);
  }
}
