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

import net.zypr.api.enums.CardinalDirection;

import org.json.simple.JSONObject;

public class GeoPositionVO
  extends GenericVO
{
  private double _latitude;
  private double _longitude;
  private double _altitude;

  public GeoPositionVO()
  {
  }

  public GeoPositionVO(JSONObject jsonObject)
  {
    try
      {
        setLatitude(Double.parseDouble(jsonObject.get("lat").toString()));
      }
    catch (Exception exception)
      {
        _latitude = Double.MIN_VALUE;
      }
    try
      {
        setLongitude(Double.parseDouble(jsonObject.get("lng").toString()));
      }
    catch (Exception exception)
      {
        _longitude = Double.MIN_VALUE;
      }
    try
      {
        setAltitude(Double.parseDouble(jsonObject.get("alt").toString()));
      }
    catch (Exception exception)
      {
        this._altitude = Double.MIN_VALUE;
      }
  }

  public GeoPositionVO(double latitude, double longitude)
  {
    setLatitude(latitude);
    setLongitude(longitude);
  }

  public GeoPositionVO(double latitude, double longitude, double altitude)
  {
    setLatitude(latitude);
    setLongitude(longitude);
    setAltitude(altitude);
  }

  public GeoPositionVO(double[] coordinates)
  {
    this(coordinates[0], coordinates[1], (coordinates.length == 3 ? coordinates[2] : Double.MIN_VALUE));
  }

  public GeoPositionVO(double latDegreesMinutes, CardinalDirection latDirection, double lonDegreesMinutes, CardinalDirection lonDirection)
  {
    this(latDegreesMinutes, latDirection, lonDegreesMinutes, lonDirection, Double.MIN_VALUE);
  }

  public GeoPositionVO(double latDegreesMinutes, CardinalDirection latDirection, double lonDegreesMinutes, CardinalDirection lonDirection, double altitude)
  {
    _latitude = latDegreesMinutes;
    int iLatitude = (int) (_latitude / 100);
    _latitude = (((_latitude / 100) - iLatitude) / 60) * 100;
    _latitude += iLatitude;
    _latitude *= (latDirection == CardinalDirection.N || latDirection == CardinalDirection.NORTH ? 1 : -1);
    _longitude = lonDegreesMinutes;
    int ilongitude = (int) (_longitude / 100);
    _longitude = (((_longitude / 100) - ilongitude) / 60) * 100;
    _longitude += ilongitude;
    _longitude *= (lonDirection == CardinalDirection.E || lonDirection == CardinalDirection.EAST ? 1 : -1);
    _altitude = altitude;
  }

  public GeoPositionVO(double latDegrees, double latMinutes, double latSeconds, double lonDegrees, double lonMinutes, double lonSeconds)
  {
    this(latDegrees + (latMinutes + latSeconds / 60.0) / 60.0, lonDegrees + (lonMinutes + lonSeconds / 60.0) / 60.0);
  }

  public double getLatitude()
  {
    return (_latitude);
  }

  public double getLongitude()
  {
    return (_longitude);
  }

  public double getAltitude()
  {
    return (_altitude);
  }

  @Override
  public boolean equals(Object object)
  {
    if (object instanceof GeoPositionVO)
      {
        GeoPositionVO coordinate = (GeoPositionVO) object;
        return coordinate.getLatitude() == _latitude && coordinate.getLongitude() == _longitude && coordinate.getAltitude() == _altitude;
      }
    return (false);
  }

  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 1;
    long temp = Double.doubleToLongBits(_latitude);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_longitude);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_altitude);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("lat", _latitude);
    jsonObject.put("lng", _longitude);
    return (jsonObject);
  }

  public void setLatitude(double latitude)
  {
    double oldLatitude = _latitude;
    this._latitude = latitude;
    propertyChangeSupport.firePropertyChange("Latitude", oldLatitude, latitude);
  }

  public void setLongitude(double longitude)
  {
    double oldLongitude = _longitude;
    if (longitude > 180)
      longitude = ((longitude - 180) % 360) - 180;
    else if (longitude < -180)
      longitude = 180 - Math.abs((longitude - 180) % 360);
    this._longitude = longitude;
    propertyChangeSupport.firePropertyChange("Longitude", oldLongitude, longitude);
  }

  public void setAltitude(double altitude)
  {
    double oldAltitude = _altitude;
    this._altitude = altitude;
    propertyChangeSupport.firePropertyChange("Altitude", oldAltitude, altitude);
  }
}
