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

import org.json.simple.JSONObject;

public class GeoPointVO
  extends GenericVO
{
  private AddressVO _address;
  private GeoPositionVO _geoPosition;

  public GeoPointVO(AddressVO address)
  {
    super();
    _address = address;
  }

  public GeoPointVO(GeoPositionVO geoPosition)
  {
    super();
    _geoPosition = geoPosition;
  }

  public AddressVO getAddress()
  {
    return (_address);
  }

  public GeoPositionVO getGeoPosition()
  {
    return (_geoPosition);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    if (_address != null)
      {
        jsonObject.put("address", _address.toJSON());
      }
    else if (_geoPosition != null)
      {
        JSONObject jsonGeoPosition = new JSONObject();
        jsonGeoPosition.put("lat", _geoPosition.getLatitude());
        jsonGeoPosition.put("lng", _geoPosition.getLongitude());
        jsonObject.put("point", jsonGeoPosition);
      }
    return (jsonObject);
  }
}
