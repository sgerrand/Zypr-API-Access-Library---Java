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
import net.zypr.api.utils.Debug;
import net.zypr.api.utils.NumberUtils;

import org.json.simple.JSONObject;

public class GeoBoundsVO
  extends GenericVO
{
  private GeoPositionVO _neGeoPosition;
  private GeoPositionVO _nwGeoPosition;
  private GeoPositionVO _seGeoPosition;
  private GeoPositionVO _swGeoPosition;

  public GeoBoundsVO(GeoPositionVO geoPosition1, CardinalDirection cardinalDirection1, GeoPositionVO geoPosition2, CardinalDirection cardinalDirection2)
  {
    if (cardinalDirection1 == CardinalDirection.NE || cardinalDirection1 == CardinalDirection.NORTHEAST)
      _neGeoPosition = geoPosition1;
    else if (cardinalDirection1 == CardinalDirection.NW || cardinalDirection1 == CardinalDirection.NORTHWEST)
      _nwGeoPosition = geoPosition1;
    else if (cardinalDirection1 == CardinalDirection.SE || cardinalDirection1 == CardinalDirection.SOUTHEAST)
      _seGeoPosition = geoPosition1;
    else if (cardinalDirection1 == CardinalDirection.SW || cardinalDirection1 == CardinalDirection.SOUTHWEST)
      _swGeoPosition = geoPosition1;
    if (cardinalDirection2 == CardinalDirection.NE || cardinalDirection2 == CardinalDirection.NORTHEAST)
      _neGeoPosition = geoPosition2;
    else if (cardinalDirection2 == CardinalDirection.NW || cardinalDirection2 == CardinalDirection.NORTHWEST)
      _nwGeoPosition = geoPosition2;
    else if (cardinalDirection2 == CardinalDirection.SE || cardinalDirection2 == CardinalDirection.SOUTHEAST)
      _seGeoPosition = geoPosition2;
    else if (cardinalDirection2 == CardinalDirection.SW || cardinalDirection2 == CardinalDirection.SOUTHWEST)
      _swGeoPosition = geoPosition2;
    buildPoints();
  }

  public GeoBoundsVO(JSONObject jsonObject)
  {
    if (jsonObject == null)
      return;
    try
      {
        JSONObject jsonPosition = (JSONObject) jsonObject.get("ne_point");
        if (jsonPosition != null)
          _neGeoPosition = new GeoPositionVO(jsonPosition);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        JSONObject jsonPosition = (JSONObject) jsonObject.get("nw_point");
        if (jsonPosition != null)
          _nwGeoPosition = new GeoPositionVO(jsonPosition);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        JSONObject jsonPosition = (JSONObject) jsonObject.get("se_point");
        if (jsonPosition != null)
          _seGeoPosition = new GeoPositionVO(jsonPosition);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        JSONObject jsonPosition = (JSONObject) jsonObject.get("sw_point");
        if (jsonPosition != null)
          _swGeoPosition = new GeoPositionVO(jsonPosition);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    buildPoints();
  }

  private void buildPoints()
  {
    if (_neGeoPosition == null && _swGeoPosition == null && _seGeoPosition != null && _nwGeoPosition != null)
      {
        _neGeoPosition = new GeoPositionVO(_nwGeoPosition.getLatitude(), _seGeoPosition.getLongitude());
        _swGeoPosition = new GeoPositionVO(_seGeoPosition.getLatitude(), _nwGeoPosition.getLongitude());
      }
    else if (_neGeoPosition != null && _swGeoPosition != null && _seGeoPosition == null && _nwGeoPosition == null)
      {
        _nwGeoPosition = new GeoPositionVO(_neGeoPosition.getLatitude(), _swGeoPosition.getLongitude());
        _seGeoPosition = new GeoPositionVO(_swGeoPosition.getLatitude(), _neGeoPosition.getLongitude());
      }
  }

  public GeoPositionVO getNorthEast()
  {
    return _neGeoPosition;
  }

  public GeoPositionVO getNorthWest()
  {
    return _nwGeoPosition;
  }

  public GeoPositionVO getSouthEast()
  {
    return _seGeoPosition;
  }

  public GeoPositionVO getSouthWest()
  {
    return _swGeoPosition;
  }

  public GeoPositionVO getCenter()
  {
    return (new GeoPositionVO((_neGeoPosition.getLatitude() + _swGeoPosition.getLatitude()) / 2, (_neGeoPosition.getLongitude() + _swGeoPosition.getLongitude()) / 2));
  }

  public JSONObject toJSON()
  {
    JSONObject jsonBBoxObject = new JSONObject();
    JSONObject swPointJSONObject = new JSONObject();
    swPointJSONObject.put("lat", NumberUtils.roundToDecimals(getSouthWest().getLatitude()));
    swPointJSONObject.put("lng", NumberUtils.roundToDecimals(getSouthWest().getLongitude()));
    jsonBBoxObject.put("sw_point", swPointJSONObject);
    JSONObject nePointJSONObject = new JSONObject();
    nePointJSONObject.put("lat", NumberUtils.roundToDecimals(getNorthEast().getLatitude()));
    nePointJSONObject.put("lng", NumberUtils.roundToDecimals(getNorthEast().getLongitude()));
    jsonBBoxObject.put("ne_point", nePointJSONObject);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("bbox", jsonBBoxObject);
    return (jsonObject);
  }
}
