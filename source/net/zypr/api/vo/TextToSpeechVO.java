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

public class TextToSpeechVO
  extends GenericVO
{
  private String _text;
  private String _audioURI;
  private String _responseCode;

  public TextToSpeechVO()
  {
    super();
  }

  public TextToSpeechVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _text = (String) jsonObject.get("text");
        _audioURI = (String) jsonObject.get("audio_uri");
        _responseCode = (String) jsonObject.get("response_code");
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

  public void setText(String text)
  {
    String oldText = _text;
    this._text = text;
    propertyChangeSupport.firePropertyChange("Text", oldText, text);
  }

  public String getText()
  {
    return (_text);
  }

  public void setAudioURI(String audioURI)
  {
    String oldAudioURI = _audioURI;
    this._audioURI = audioURI;
    propertyChangeSupport.firePropertyChange("AudioURI", oldAudioURI, audioURI);
  }

  public String getAudioURI()
  {
    return (_audioURI);
  }

  public void setResponseCode(String responseCode)
  {
    String oldResponseCode = _responseCode;
    this._responseCode = responseCode;
    propertyChangeSupport.firePropertyChange("ResponseCode", oldResponseCode, responseCode);
  }

  public String getResponseCode()
  {
    return (_responseCode);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("text", _text);
    jsonObject.put("audio_uri", _audioURI);
    jsonObject.put("response_code", _responseCode);
    return (jsonObject);
  }
}
