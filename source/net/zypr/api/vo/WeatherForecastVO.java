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

import java.util.Date;

import net.zypr.api.enums.CardinalDirection;
import net.zypr.api.enums.MeasurementSystem;
import net.zypr.api.enums.PeriodOfTime;
import net.zypr.api.enums.TemperatureScale;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.DateUtils;
import net.zypr.api.utils.Debug;

import org.json.simple.JSONObject;

public class WeatherForecastVO
  extends GenericVO
{
  private PeriodOfTime _periodOfTime;
  private Date _date;
  private String _iconCode;
  private String _iconURI;
  private String _description;
  private String _descriptionLong;
  private GeographyVO _geography;
  private MeasurementSystem _units;
  private TemperatureScale _scale;
  private long _highTemperature;
  private long _lowTemperature;
  private long _effectiveHighTemperature;
  private long _effectiveLowTemperature;
  private long _windSpeed;
  private long _windGust;
  private CardinalDirection _windDirection;
  private String _maxUV;
  private double _rainAmount;
  private double _snowAmount;
  private double _iceAmount;
  private double _totalPrecipitation;
  private long _thunderstormProbability;
  private String _detailsURI;

  public WeatherForecastVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    if (jsonObject == null)
      throw new APIProtocolException();
    try
      {
        _periodOfTime = PeriodOfTime.valueOf(((String) jsonObject.get("night_or_day")).toUpperCase());
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
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
        _highTemperature = (Long) jsonObject.get("high_temp");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _lowTemperature = (Long) jsonObject.get("low_temp");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _effectiveHighTemperature = (Long) jsonObject.get("effective_high_temp");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _effectiveLowTemperature = (Long) jsonObject.get("effective_low_temp");
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
        _rainAmount = (Double) jsonObject.get("rain_amount");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _snowAmount = (Double) jsonObject.get("snow_amount");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _iceAmount = (Double) jsonObject.get("ice_amount");
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
        _thunderstormProbability = (Long) jsonObject.get("thunderstorm_probability");
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

  public void setPeriodOfTime(PeriodOfTime periodOfTime)
  {
    PeriodOfTime oldPeriodOfTime = this._periodOfTime;
    this._periodOfTime = periodOfTime;
    propertyChangeSupport.firePropertyChange("PeriodOfTime", oldPeriodOfTime, periodOfTime);
  }

  public PeriodOfTime getPeriodOfTime()
  {
    return (_periodOfTime);
  }

  public void setDate(Date date)
  {
    Date oldDate = this._date;
    this._date = date;
    propertyChangeSupport.firePropertyChange("Date", oldDate, date);
  }

  public Date getDate()
  {
    return (_date);
  }

  public void setIconCode(String iconCode)
  {
    String oldIconCode = this._iconCode;
    this._iconCode = iconCode;
    propertyChangeSupport.firePropertyChange("IconCode", oldIconCode, iconCode);
  }

  public String getIconCode()
  {
    return (_iconCode);
  }

  public void setIconURI(String iconURI)
  {
    String oldIconURI = this._iconURI;
    this._iconURI = iconURI;
    propertyChangeSupport.firePropertyChange("IconURI", oldIconURI, iconURI);
  }

  public String getIconURI()
  {
    return (_iconURI);
  }

  public void setDescription(String description)
  {
    String oldDescription = this._description;
    this._description = description;
    propertyChangeSupport.firePropertyChange("Description", oldDescription, description);
  }

  public String getDescription()
  {
    return (_description);
  }

  public void setDescriptionLong(String descriptionLong)
  {
    String oldDescriptionLong = this._descriptionLong;
    this._descriptionLong = descriptionLong;
    propertyChangeSupport.firePropertyChange("DescriptionLong", oldDescriptionLong, descriptionLong);
  }

  public String getDescriptionLong()
  {
    return (_descriptionLong);
  }

  public void setGeography(GeographyVO geography)
  {
    GeographyVO oldGeography = this._geography;
    this._geography = geography;
    propertyChangeSupport.firePropertyChange("Geography", oldGeography, geography);
  }

  public GeographyVO getGeography()
  {
    return (_geography);
  }

  public void setUnits(MeasurementSystem units)
  {
    MeasurementSystem oldUnits = this._units;
    this._units = units;
    propertyChangeSupport.firePropertyChange("Units", oldUnits, units);
  }

  public MeasurementSystem getUnits()
  {
    return (_units);
  }

  public void setScale(TemperatureScale scale)
  {
    TemperatureScale oldScale = this._scale;
    this._scale = scale;
    propertyChangeSupport.firePropertyChange("Scale", oldScale, scale);
  }

  public TemperatureScale getScale()
  {
    return (_scale);
  }

  public void setHighTemperature(long highTemperature)
  {
    long oldHighTemperature = this._highTemperature;
    this._highTemperature = highTemperature;
    propertyChangeSupport.firePropertyChange("HighTemperature", oldHighTemperature, highTemperature);
  }

  public long getHighTemperature()
  {
    return (_highTemperature);
  }

  public void setLowTemperature(long lowTemperature)
  {
    long oldLowTemperature = this._lowTemperature;
    this._lowTemperature = lowTemperature;
    propertyChangeSupport.firePropertyChange("LowTemperature", oldLowTemperature, lowTemperature);
  }

  public long getLowTemperature()
  {
    return (_lowTemperature);
  }

  public void setEffectiveHighTemperature(long effectiveHighTemperature)
  {
    long oldEffectiveHighTemperature = this._effectiveHighTemperature;
    this._effectiveHighTemperature = effectiveHighTemperature;
    propertyChangeSupport.firePropertyChange("EffectiveHighTemperature", oldEffectiveHighTemperature, effectiveHighTemperature);
  }

  public long getEffectiveHighTemperature()
  {
    return (_effectiveHighTemperature);
  }

  public void setEffectiveLowTemperature(long effectiveLowTemperature)
  {
    long oldEffectiveLowTemperature = this._effectiveLowTemperature;
    this._effectiveLowTemperature = effectiveLowTemperature;
    propertyChangeSupport.firePropertyChange("EffectiveLowTemperature", oldEffectiveLowTemperature, effectiveLowTemperature);
  }

  public long getEffectiveLowTemperature()
  {
    return (_effectiveLowTemperature);
  }

  public void setWindSpeed(long windSpeed)
  {
    long oldWindSpeed = this._windSpeed;
    this._windSpeed = windSpeed;
    propertyChangeSupport.firePropertyChange("WindSpeed", oldWindSpeed, windSpeed);
  }

  public long getWindSpeed()
  {
    return (_windSpeed);
  }

  public void setWindGust(long windGust)
  {
    long oldWindGust = this._windGust;
    this._windGust = windGust;
    propertyChangeSupport.firePropertyChange("WindGust", oldWindGust, windGust);
  }

  public long getWindGust()
  {
    return (_windGust);
  }

  public void setWindDirection(CardinalDirection windDirection)
  {
    CardinalDirection oldWindDirection = this._windDirection;
    this._windDirection = windDirection;
    propertyChangeSupport.firePropertyChange("WindDirection", oldWindDirection, windDirection);
  }

  public CardinalDirection getWindDirection()
  {
    return (_windDirection);
  }

  public void setMaxUV(String maxUV)
  {
    String oldMaxUV = this._maxUV;
    this._maxUV = maxUV;
    propertyChangeSupport.firePropertyChange("MaxUV", oldMaxUV, maxUV);
  }

  public String getMaxUV()
  {
    return (_maxUV);
  }

  public void setRainAmount(double rainAmount)
  {
    double oldRainAmount = this._rainAmount;
    this._rainAmount = rainAmount;
    propertyChangeSupport.firePropertyChange("RainAmount", oldRainAmount, rainAmount);
  }

  public double getRainAmount()
  {
    return (_rainAmount);
  }

  public void setSnowAmount(double snowAmount)
  {
    double oldSnowAmount = this._snowAmount;
    this._snowAmount = snowAmount;
    propertyChangeSupport.firePropertyChange("SnowAmount", oldSnowAmount, snowAmount);
  }

  public double getSnowAmount()
  {
    return (_snowAmount);
  }

  public void setIceAmount(double iceAmount)
  {
    double oldIceAmount = this._iceAmount;
    this._iceAmount = iceAmount;
    propertyChangeSupport.firePropertyChange("IceAmount", oldIceAmount, iceAmount);
  }

  public double getIceAmount()
  {
    return (_iceAmount);
  }

  public void setTotalPrecipitation(double totalPrecipitation)
  {
    double oldTotalPrecipitation = this._totalPrecipitation;
    this._totalPrecipitation = totalPrecipitation;
    propertyChangeSupport.firePropertyChange("TotalPrecipitation", oldTotalPrecipitation, totalPrecipitation);
  }

  public double getTotalPrecipitation()
  {
    return (_totalPrecipitation);
  }

  public void setThunderstormProbability(long thunderstormProbability)
  {
    long oldThunderstormProbability = this._thunderstormProbability;
    this._thunderstormProbability = thunderstormProbability;
    propertyChangeSupport.firePropertyChange("ThunderstormProbability", oldThunderstormProbability, thunderstormProbability);
  }

  public long getThunderstormProbability()
  {
    return (_thunderstormProbability);
  }

  public void setDetailsURI(String detailsURI)
  {
    String oldDetailsURI = this._detailsURI;
    this._detailsURI = detailsURI;
    propertyChangeSupport.firePropertyChange("DetailsURI", oldDetailsURI, detailsURI);
  }

  public String getDetailsURI()
  {
    return (_detailsURI);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("night_or_day", _periodOfTime.toString().toLowerCase());
    jsonObject.put("date", DateUtils.getISO8601String(_date));
    jsonObject.put("icon_code", _iconCode);
    jsonObject.put("icon_uri", _iconURI);
    jsonObject.put("description", _description);
    jsonObject.put("description_long", _descriptionLong);
    jsonObject.put("geography", _geography.toJSON());
    jsonObject.put("units", _units.toString().toLowerCase());
    jsonObject.put("high_temp", _highTemperature);
    jsonObject.put("low_temp", _lowTemperature);
    jsonObject.put("effective_high_temp", _effectiveHighTemperature);
    jsonObject.put("effective_low_temp", _effectiveLowTemperature);
    jsonObject.put("wind_speed", _windSpeed);
    jsonObject.put("wind_gust", _windGust);
    jsonObject.put("wind_direction", _windDirection.toString().toLowerCase());
    jsonObject.put("max_uv", _maxUV);
    jsonObject.put("rain_amount", _rainAmount);
    jsonObject.put("snow_amount", _snowAmount);
    jsonObject.put("ice_amount", _iceAmount);
    jsonObject.put("total_precipitation", _totalPrecipitation);
    jsonObject.put("details_uri", _detailsURI);
    return (jsonObject);
  }
}
