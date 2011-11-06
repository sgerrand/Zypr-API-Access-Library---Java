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

import java.beans.PropertyChangeSupport;

import net.zypr.api.enums.CardinalDirection;
import net.zypr.api.enums.DistanceUnit;
import net.zypr.api.enums.ManuverAction;
import net.zypr.api.exceptions.APIProtocolException;

import org.json.simple.JSONObject;

public class ManuverVO
  extends GenericVO
{
  private double _distance;
  private DistanceUnit _unit;
  private GeoPositionVO _geoPosition;
  private CardinalDirection _heading;
  private ManuverAction _action;
  private SignageVO _signage;
  private String _description;
  private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public ManuverVO()
  {
    super();
  }

  public ManuverVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _distance = ((Number) jsonObject.get("distance")).doubleValue();
        _unit = DistanceUnit.MILE.getEnum(((String) jsonObject.get("units")));
        _geoPosition = new GeoPositionVO((JSONObject) jsonObject.get("point"));
        String heading = (String) jsonObject.get("heading");
        if (heading != null && !heading.equals(""))
          _heading = CardinalDirection.valueOf(heading.toUpperCase());
        _action = ManuverAction.valueOf(((String) jsonObject.get("action")).toUpperCase());
        _signage = new SignageVO((JSONObject) jsonObject.get("signage"));
        _description = (String) jsonObject.get("description");
      }
    catch (IllegalArgumentException illegalArgumentException)
      {
        throw new APIProtocolException(illegalArgumentException);
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    catch (NullPointerException nullPointerException)
      {
        throw new APIProtocolException(nullPointerException);
      }
  }

  public void setDistance(double distance)
  {
    double oldDistance = _distance;
    this._distance = distance;
    propertyChangeSupport.firePropertyChange("Distance", oldDistance, distance);
  }

  public double getDistance()
  {
    return (_distance);
  }

  public void setUnit(DistanceUnit unit)
  {
    DistanceUnit oldUnit = _unit;
    this._unit = unit;
    propertyChangeSupport.firePropertyChange("Unit", oldUnit, unit);
  }

  public DistanceUnit getUnit()
  {
    return (_unit);
  }

  public void setGeoPosition(GeoPositionVO geoPosition)
  {
    GeoPositionVO oldGeoPosition = _geoPosition;
    this._geoPosition = geoPosition;
    propertyChangeSupport.firePropertyChange("GeoPosition", oldGeoPosition, geoPosition);
  }

  public GeoPositionVO getGeoPosition()
  {
    return (_geoPosition);
  }

  public void setHeading(CardinalDirection heading)
  {
    CardinalDirection oldHeading = _heading;
    this._heading = heading;
    propertyChangeSupport.firePropertyChange("Heading", oldHeading, heading);
  }

  public CardinalDirection getHeading()
  {
    return (_heading);
  }

  public void setAction(ManuverAction action)
  {
    ManuverAction oldAction = _action;
    this._action = action;
    propertyChangeSupport.firePropertyChange("Action", oldAction, action);
  }

  public ManuverAction getAction()
  {
    return (_action);
  }

  public void setSignage(SignageVO signage)
  {
    SignageVO oldSignage = _signage;
    this._signage = signage;
    propertyChangeSupport.firePropertyChange("Signage", oldSignage, signage);
  }

  public SignageVO getSignage()
  {
    return (_signage);
  }

  public void setDescription(String description)
  {
    String oldDescription = _description;
    this._description = description;
    propertyChangeSupport.firePropertyChange("Description", oldDescription, description);
  }

  public String getDescription()
  {
    return (_description);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("distance", _distance);
    jsonObject.put("units", _unit);
    jsonObject.put("units", _geoPosition.toJSON());
    jsonObject.put("heading", _heading);
    jsonObject.put("action", _action);
    jsonObject.put("signage", _signage.toJSON());
    jsonObject.put("description", _description);
    return (jsonObject);
  }
}
