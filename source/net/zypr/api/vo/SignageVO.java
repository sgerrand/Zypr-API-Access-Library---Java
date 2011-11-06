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

public class SignageVO
  extends GenericVO
{
  private String _imageURI;
  private String _destination;

  public SignageVO()
  {
    super();
  }

  public SignageVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _imageURI = (String) jsonObject.get("image_uri");
        _destination = (String) jsonObject.get("destination");
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

  public void setImageURI(String imageURI)
  {
    String oldImageURI = _imageURI;
    this._imageURI = imageURI;
    propertyChangeSupport.firePropertyChange("ImageURI", oldImageURI, imageURI);
  }

  public String getImageURI()
  {
    return (_imageURI);
  }

  public void setDestination(String destination)
  {
    String oldDestination = _destination;
    this._destination = destination;
    propertyChangeSupport.firePropertyChange("Destination", oldDestination, destination);
  }

  public String getDestination()
  {
    return (_destination);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("image_uri", _imageURI);
    jsonObject.put("destination", _destination);
    return (jsonObject);
  }
}
