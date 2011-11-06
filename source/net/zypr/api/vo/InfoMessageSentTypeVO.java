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

import net.zypr.api.utils.Debug;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class InfoMessageSentTypeVO
  extends GenericVO
{
  private String _to;
  private String _toServiceUserID;
  private String _toService;
  private String _scope;
  private String _text;
  private GeoPositionVO _point;
  private String _mimeType;
  private String[] _attachments;

  public InfoMessageSentTypeVO()
  {
    super();
  }

  public InfoMessageSentTypeVO(String to, String toService, String toServiceUserID, String scope, String text, String[] attachments)
  {
    super();
    _to = to;
    _toService = toService;
    _toServiceUserID = toServiceUserID;
    _scope = scope;
    _text = text;
    _attachments = attachments;
  }

  public InfoMessageSentTypeVO(String to, String toService, String toServiceUserID, String scope, String text)
  {
    super();
    _to = to;
    _toService = toService;
    _toServiceUserID = toServiceUserID;
    _scope = scope;
    _text = text;
  }

  public InfoMessageSentTypeVO(String to, String toService, String toServiceUserID, String scope, String text, String mimeType, String[] attachments)
  {
    super();
    _to = to;
    _toService = toService;
    _toServiceUserID = toServiceUserID;
    _scope = scope;
    _text = text;
    _mimeType = mimeType;
    _attachments = attachments;
  }

  public InfoMessageSentTypeVO(String to, String toService, String toServiceUserID, String scope, String text, String mimeType)
  {
    super();
    _to = to;
    _toService = toService;
    _toServiceUserID = toServiceUserID;
    _scope = scope;
    _text = text;
    _mimeType = mimeType;
  }

  public InfoMessageSentTypeVO(String to, String toService, String toServiceUserID, String scope, String text, String mimeType, GeoPositionVO point, String[] attachments)
  {
    super();
    _to = to;
    _toService = toService;
    _toServiceUserID = toServiceUserID;
    _scope = scope;
    _text = text;
    _mimeType = mimeType;
    _attachments = attachments;
    _point = point;
  }

  public InfoMessageSentTypeVO(String to, String toService, String toServiceUserID, String scope, String text, String mimeType, GeoPositionVO point)
  {
    super();
    _to = to;
    _toService = toService;
    _toServiceUserID = toServiceUserID;
    _scope = scope;
    _text = text;
    _mimeType = mimeType;
    _point = point;
  }

  public void setTo(String to)
  {
    String oldTo = _to;
    this._to = to;
    propertyChangeSupport.firePropertyChange("To", oldTo, to);
  }

  public String getTo()
  {
    return (_to);
  }

  public void setToServiceUserID(String toServiceUserID)
  {
    String oldToServiceUserID = _toServiceUserID;
    this._toServiceUserID = toServiceUserID;
    propertyChangeSupport.firePropertyChange("ToServiceUserID", oldToServiceUserID, toServiceUserID);
  }

  public String getToServiceUserID()
  {
    return (_toServiceUserID);
  }

  public void setScope(String scope)
  {
    String oldScope = _scope;
    this._scope = scope;
    propertyChangeSupport.firePropertyChange("Scope", oldScope, scope);
  }

  public String getScope()
  {
    return (_scope);
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

  public void setAttachments(String[] attachments)
  {
    String[] oldAttachments = _attachments;
    this._attachments = attachments;
    propertyChangeSupport.firePropertyChange("Attachments", oldAttachments, attachments);
  }

  public String[] getAttachments()
  {
    return (_attachments);
  }

  public void setToService(String toService)
  {
    String oldToService = _toService;
    this._toService = toService;
    propertyChangeSupport.firePropertyChange("ToService", oldToService, toService);
  }

  public String getToService()
  {
    return (_toService);
  }

  public void setPoint(GeoPositionVO point)
  {
    GeoPositionVO oldPoint = _point;
    this._point = point;
    propertyChangeSupport.firePropertyChange("Point", oldPoint, point);
  }

  public GeoPositionVO getPoint()
  {
    return (_point);
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

  public JSONObject toJSON()
  {
    JSONObject jsonInfoObject = new JSONObject();
    jsonInfoObject.put("type", "message");
    jsonInfoObject.put("to", _to);
    jsonInfoObject.put("to_service", _toService);
    jsonInfoObject.put("to_service_user_id", _toServiceUserID);
    jsonInfoObject.put("scope", _scope);
    jsonInfoObject.put("text", _text);
    if (_point != null)
      jsonInfoObject.put("point", _point.toJSON());
    if (_mimeType != null)
      jsonInfoObject.put("mime_type", _mimeType);
    if (_attachments != null)
      {
        JSONArray jsonAttachmentsArray = new JSONArray();
        for (int index = 0; index < _attachments.length; index++)
          {
            try
              {
                if (_attachments[index] != null && !_attachments[index].trim().equals(""))
                  jsonAttachmentsArray.add((new JSONParser()).parse(_attachments[index]));
              }
            catch (Exception exception)
              {
                Debug.displayStack(this, exception);
              }
          }
        jsonInfoObject.put("attachments", jsonAttachmentsArray);
      }
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("info", jsonInfoObject);
    return (jsonObject);
  }
}
