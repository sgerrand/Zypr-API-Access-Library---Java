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

import net.zypr.api.Session;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.NumberUtils;

import org.json.simple.JSONObject;

public class ActionHandlerVO
  extends GenericVO
{
  private String _handlerName;
  private String _handlerURI;
  private String _callbackURI;

  public ActionHandlerVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _handlerName = (String) jsonObject.get("handler");
        _handlerURI = (String) jsonObject.get("uri");
        _callbackURI = (String) jsonObject.get("callback_uri");
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

  public void setHandlerName(String handlerName)
  {
    String oldHandlerName = _handlerName;
    this._handlerName = handlerName;
    propertyChangeSupport.firePropertyChange("HandlerName", oldHandlerName, handlerName);
  }

  public String getHandlerName()
  {
    return (_handlerName);
  }

  public void setHandlerURI(String handlerURI)
  {
    String oldHandlerURI = _handlerURI;
    this._handlerURI = handlerURI;
    propertyChangeSupport.firePropertyChange("HandlerURI", oldHandlerURI, handlerURI);
  }

  public String getHandlerURI()
  {
    return (_handlerURI + (_handlerURI.indexOf("?") == -1 ? "?" : "&") + "token=" + Session.getInstance().getToken() + (Session.getInstance().getPosition() == null ? "" : "&lat=" + NumberUtils.roundToDecimals(Session.getInstance().getPosition().getLatitude()) + "&lng=" + NumberUtils.roundToDecimals(Session.getInstance().getPosition().getLongitude())));
  }

  public String getHandlerURIWithoutToken()
  {
    return (_handlerURI);
  }

  public void setCallbackURI(String callbackURI)
  {
    String oldCallbackURI = _callbackURI;
    this._callbackURI = callbackURI;
    propertyChangeSupport.firePropertyChange("CallbackURI", oldCallbackURI, callbackURI);
  }

  public String getCallbackURI()
  {
    return (_callbackURI);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    if (_handlerName != null)
      jsonObject.put("handler", _handlerName);
    if (_handlerURI != null)
      jsonObject.put("uri", _handlerURI);
    if (_callbackURI != null)
      jsonObject.put("callback_uri", _callbackURI);
    return (jsonObject);
  }
}
