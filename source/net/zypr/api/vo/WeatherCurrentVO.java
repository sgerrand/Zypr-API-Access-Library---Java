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

import java.beans.PropertyChangeListener;

import java.util.Date;

import net.zypr.api.enums.CardinalDirection;
import net.zypr.api.enums.MeasurementSystem;
import net.zypr.api.enums.TemperatureScale;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.DateUtils;
import net.zypr.api.utils.Debug;

import org.json.simple.JSONObject;

public class WeatherCurrentVO
  extends GenericVO
{
  private Date _date;
  private String _iconCode;
  private String _iconURI;
  private String _description;
  private String _descriptionLong;
  private GeographyVO _geography;
  private MeasurementSystem _units;
  private TemperatureScale _scale;
  private long _currentTemperature;
  private String _currentHumidity;
  private long _currentEffectiveTemperature;
  private long _currentWindChill;
  private String _cloudCover;
  private String _pressure;
  private long _uvIndex;
  private long _windSpeed;
  private long _windGust;
  private CardinalDirection _windDirection;
  private String _maxUV;
  private double _totalPrecipitation;
  private String _detailsURI;

  public WeatherCurrentVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    if (jsonObject == null)
      throw new APIProtocolException();
    try
      {
        _date = DateUtils.parseISO8601String((String) jsonObject.get("date"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _iconCode = (String) jsonObject.get("icon_code");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _iconURI = (String) jsonObject.get("icon_uri");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _description = (String) jsonObject.get("description");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _descriptionLong = (String) jsonObject.get("description_long");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _geography = new GeographyVO((JSONObject) jsonObject.get("geography"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _units = MeasurementSystem.valueOf(((String) jsonObject.get("units")).toUpperCase());
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _scale = _units == MeasurementSystem.ENGLISH ? TemperatureScale.FAHRENHEIT : TemperatureScale.CELSIUS;
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _currentTemperature = (Long) jsonObject.get("current_temp");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _currentHumidity = (String) jsonObject.get("current_humidity");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _currentEffectiveTemperature = (Long) jsonObject.get("current_effective_temp");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _currentWindChill = (Long) jsonObject.get("current_windchill");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _cloudCover = (String) jsonObject.get("cloud_cover");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _pressure = (String) jsonObject.get("pressure");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _uvIndex = (Long) jsonObject.get("uv_index");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _windSpeed = (Long) jsonObject.get("wind_speed");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _windGust = (Long) jsonObject.get("wind_gust");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _windDirection = CardinalDirection.valueOf((String) jsonObject.get("wind_direction"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _maxUV = (String) jsonObject.get("max_uv");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _totalPrecipitation = (Double) jsonObject.get("total_precipitation");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _detailsURI = (String) jsonObject.get("details_uri");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
  }

  public void setDate(Date date)
  {
    Date oldDate = _date;
    _date = date;
    propertyChangeSupport.firePropertyChange("Date", oldDate, _date);
  }

  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    propertyChangeSupport.addPropertyChangeListener(l);
  }

  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    propertyChangeSupport.removePropertyChangeListener(l);
  }

  public Date getDate()
  {
    return (_date);
  }

  public void setIconCode(String iconCode)
  {
    String oldIconCode = _iconCode;
    _iconCode = iconCode;
    propertyChangeSupport.firePropertyChange("IconCode", oldIconCode, _iconCode);
  }

  public String getIconCode()
  {
    return (_iconCode);
  }

  public void setIconURI(String iconURI)
  {
    String oldIconURI = _iconURI;
    _iconURI = iconURI;
    propertyChangeSupport.firePropertyChange("IconURI", oldIconURI, _iconURI);
  }

  public String getIconURI()
  {
    return (_iconURI);
  }

  public void setDescription(String description)
  {
    String oldDescription = _description;
    _description = description;
    propertyChangeSupport.firePropertyChange("Description", oldDescription, _description);
  }

  public String getDescription()
  {
    return (_description);
  }

  public void setDescriptionLong(String descriptionLong)
  {
    String oldDescriptionLong = _descriptionLong;
    _descriptionLong = descriptionLong;
    propertyChangeSupport.firePropertyChange("DescriptionLong", oldDescriptionLong, _descriptionLong);
  }

  public String getDescriptionLong()
  {
    return (_descriptionLong);
  }

  public void setGeography(GeographyVO geography)
  {
    GeographyVO oldGeography = _geography;
    _geography = geography;
    propertyChangeSupport.firePropertyChange("Geography", oldGeography, _geography);
  }

  public GeographyVO getGeography()
  {
    return (_geography);
  }

  public void setUnits(MeasurementSystem units)
  {
    MeasurementSystem oldUnits = _units;
    _units = units;
    propertyChangeSupport.firePropertyChange("Units", oldUnits, _units);
  }

  public MeasurementSystem getUnits()
  {
    return (_units);
  }

  public void setScale(TemperatureScale scale)
  {
    TemperatureScale oldScale = _scale;
    _scale = scale;
    propertyChangeSupport.firePropertyChange("Scale", oldScale, _scale);
  }

  public TemperatureScale getScale()
  {
    return (_scale);
  }

  public void setCurrentTemperature(long currentTemperature)
  {
    long oldCurrentTemperature = _currentTemperature;
    _currentTemperature = currentTemperature;
    propertyChangeSupport.firePropertyChange("CurrentTemperature", oldCurrentTemperature, _currentTemperature);
  }

  public long getCurrentTemperature()
  {
    return (_currentTemperature);
  }

  public void setCurrentHumidity(String currentHumidity)
  {
    String oldCurrentHumidity = _currentHumidity;
    _currentHumidity = currentHumidity;
    propertyChangeSupport.firePropertyChange("CurrentHumidity", oldCurrentHumidity, _currentHumidity);
  }

  public String getCurrentHumidity()
  {
    return (_currentHumidity);
  }

  public void setCurrentEffectiveTemperature(long currentEffectiveTemperature)
  {
    long oldCurrentEffectiveTemperature = _currentEffectiveTemperature;
    _currentEffectiveTemperature = currentEffectiveTemperature;
    propertyChangeSupport.firePropertyChange("CurrentEffectiveTemperature", oldCurrentEffectiveTemperature, _currentEffectiveTemperature);
  }

  public long getCurrentEffectiveTemperature()
  {
    return (_currentEffectiveTemperature);
  }

  public void setCurrentWindChill(long currentWindChill)
  {
    long oldCurrentWindChill = _currentWindChill;
    _currentWindChill = currentWindChill;
    propertyChangeSupport.firePropertyChange("CurrentWindChill", oldCurrentWindChill, _currentWindChill);
  }

  public long getCurrentWindChill()
  {
    return (_currentWindChill);
  }

  public void setCloudCover(String cloudCover)
  {
    String oldCloudCover = _cloudCover;
    _cloudCover = cloudCover;
    propertyChangeSupport.firePropertyChange("CloudCover", oldCloudCover, _cloudCover);
  }

  public String getCloudCover()
  {
    return (_cloudCover);
  }

  public void setPressure(String pressure)
  {
    String oldPressure = _pressure;
    _pressure = pressure;
    propertyChangeSupport.firePropertyChange("Pressure", oldPressure, _pressure);
  }

  public String getPressure()
  {
    return (_pressure);
  }

  public void setUvIndex(long uvIndex)
  {
    long oldUvIndex = _uvIndex;
    _uvIndex = uvIndex;
    propertyChangeSupport.firePropertyChange("UvIndex", oldUvIndex, _uvIndex);
  }

  public long getUvIndex()
  {
    return (_uvIndex);
  }

  public void setWindSpeed(long windSpeed)
  {
    long oldWindSpeed = _windSpeed;
    _windSpeed = windSpeed;
    propertyChangeSupport.firePropertyChange("WindSpeed", oldWindSpeed, _windSpeed);
  }

  public long getWindSpeed()
  {
    return (_windSpeed);
  }

  public void setWindGust(long windGust)
  {
    long oldWindGust = _windGust;
    _windGust = windGust;
    propertyChangeSupport.firePropertyChange("WindGust", oldWindGust, _windGust);
  }

  public long getWindGust()
  {
    return (_windGust);
  }

  public void setWindDirection(CardinalDirection windDirection)
  {
    CardinalDirection oldWindDirection = _windDirection;
    _windDirection = windDirection;
    propertyChangeSupport.firePropertyChange("WindDirection", oldWindDirection, _windDirection);
  }

  public CardinalDirection getWindDirection()
  {
    return (_windDirection);
  }

  public void setMaxUV(String maxUV)
  {
    String oldMaxUV = _maxUV;
    _maxUV = maxUV;
    propertyChangeSupport.firePropertyChange("MaxUV", oldMaxUV, _maxUV);
  }

  public String getMaxUV()
  {
    return (_maxUV);
  }

  public void setTotalPrecipitation(double totalPrecipitation)
  {
    double oldTotalPrecipitation = _totalPrecipitation;
    _totalPrecipitation = totalPrecipitation;
    propertyChangeSupport.firePropertyChange("TotalPrecipitation", oldTotalPrecipitation, _totalPrecipitation);
  }

  public double getTotalPrecipitation()
  {
    return (_totalPrecipitation);
  }

  public void setDetailsURI(String detailsURI)
  {
    String oldDetailsURI = _detailsURI;
    _detailsURI = detailsURI;
    propertyChangeSupport.firePropertyChange("DetailsURI", oldDetailsURI, _detailsURI);
  }

  public String getDetailsURI()
  {
    return (_detailsURI);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("date", DateUtils.getISO8601String(_date));
    jsonObject.put("icon_code", _iconCode);
    jsonObject.put("icon_uri", _iconURI);
    jsonObject.put("description", _description);
    jsonObject.put("description_long", _descriptionLong);
    jsonObject.put("geography", _geography.toJSON());
    jsonObject.put("units", _units.toString().toLowerCase());
    jsonObject.put("current_temp", _currentTemperature);
    jsonObject.put("current_humidity", _currentHumidity);
    jsonObject.put("current_effective_temp", _currentEffectiveTemperature);
    jsonObject.put("current_windchill", _currentWindChill);
    jsonObject.put("cloud_cover", _cloudCover);
    jsonObject.put("pressure", _pressure);
    jsonObject.put("uv_index", _uvIndex);
    jsonObject.put("wind_speed", _windSpeed);
    jsonObject.put("wind_gust", _windGust);
    jsonObject.put("wind_direction", _windDirection.toString().toLowerCase());
    jsonObject.put("max_uv", _maxUV);
    jsonObject.put("total_precipitation", _totalPrecipitation);
    jsonObject.put("details_uri", _detailsURI);
    return (jsonObject);
  }
}
