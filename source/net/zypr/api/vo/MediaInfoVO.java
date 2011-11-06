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

public class MediaInfoVO
  extends InfoVO
{
  private String _transport;
  private String _mimeType;
  private String _bitrate;

  public MediaInfoVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    if (jsonObject != null)
      try
        {
          _transport = (String) jsonObject.get("transport");
          _mimeType = (String) jsonObject.get("mime_type");
          _bitrate = (String) jsonObject.get("bitrate");
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

  public void setTransport(String transport)
  {
    String oldTransport = _transport;
    this._transport = transport;
    propertyChangeSupport.firePropertyChange("Transport", oldTransport, transport);
  }

  public String getTransport()
  {
    return (_transport);
  }

  public void setMimeType(String mimeType)
  {
    String oldMimeType = _mimeType;
    this._mimeType = mimeType;
    propertyChangeSupport.firePropertyChange("MimeType", oldMimeType, mimeType);
  }

  public String getMimeType()
  {
    return (_mimeType);
  }

  public void setBitrate(String bitrate)
  {
    String oldBitrate = _bitrate;
    this._bitrate = bitrate;
    propertyChangeSupport.firePropertyChange("Bitrate", oldBitrate, bitrate);
  }

  public String getBitrate()
  {
    return (_bitrate);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", "media");
    if (_transport != null)
      jsonObject.put("transport", _transport);
    if (_transport != null)
      jsonObject.put("mime_type", _mimeType);
    if (_transport != null)
      jsonObject.put("bitrate", _bitrate);
    return (jsonObject);
  }
}
