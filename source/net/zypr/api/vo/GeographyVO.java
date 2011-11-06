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

import org.json.simple.JSONObject;

public class GeographyVO
  extends GenericVO
{
  private String _name;
  private GeoBoundsVO _bounds;
  private GeoPositionVO _centroid;
  private GeographyInfoVO _info;

  public GeographyVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    try
      {
        _name = (String) jsonObject.get("name");
        _bounds = new GeoBoundsVO((JSONObject) jsonObject.get("bbox"));
        _centroid = new GeoPositionVO((JSONObject) jsonObject.get("centroid"));
        _info = new GeographyInfoVO((JSONObject) jsonObject.get("info"));
      }
    catch (Exception exception)
      {
        throw new APIProtocolException(exception);
      }
  }

  public void setName(String name)
  {
    String oldName = _name;
    _name = name;
    propertyChangeSupport.firePropertyChange("Name", oldName, name);
  }

  public String getName()
  {
    return (_name);
  }

  public void setBounds(GeoBoundsVO bounds)
  {
    GeoBoundsVO oldBounds = _bounds;
    _bounds = bounds;
    propertyChangeSupport.firePropertyChange("Bounds", oldBounds, bounds);
  }

  public GeoBoundsVO getBounds()
  {
    return (_bounds);
  }

  public void setCentroid(GeoPositionVO centroid)
  {
    GeoPositionVO oldCentroid = _centroid;
    _centroid = centroid;
    propertyChangeSupport.firePropertyChange("Centroid", oldCentroid, centroid);
  }

  public GeoPositionVO getCentroid()
  {
    return (_centroid);
  }

  public void setInfo(GeographyInfoVO info)
  {
    GeographyInfoVO oldInfo = _info;
    _info = info;
    propertyChangeSupport.firePropertyChange("Info", oldInfo, info);
  }

  public GeographyInfoVO getInfo()
  {
    return (_info);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", _name);
    jsonObject.put("bbox", _bounds.toJSON());
    jsonObject.put("centroid", _centroid.toJSON());
    if (_info != null)
      jsonObject.put("info", _info.toJSON());
    return (jsonObject);
  }
}
