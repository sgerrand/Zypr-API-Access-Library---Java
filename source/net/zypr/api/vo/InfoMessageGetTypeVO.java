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

import java.text.ParseException;

import java.util.Date;

import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.MessageType;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.DateUtils;
import net.zypr.api.utils.Debug;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class InfoMessageGetTypeVO
  extends InfoVO
{
  private String _to;
  private String _toServiceUserID;
  private String _from;
  private String _fromServiceUserID;
  private String _text;
  private String _scope;
  private Date _dateSent;
  private Date _dateToDeliver;
  private GeoPositionVO _point;
  private String _mimeType;
  private ItemVO[] _attachments;
  private boolean _read;
  private MessageType _messageType = MessageType.TEXT;

  public InfoMessageGetTypeVO()
  {
    super();
  }

  public InfoMessageGetTypeVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    if (jsonObject != null)
      {
        String type = (String) jsonObject.get("type");
        if (type == null || !type.equalsIgnoreCase("message"))
          throw new APIProtocolException("Unknown enumerated value : " + type);
        try
          {
            _to = (String) jsonObject.get("to");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        try
          {
            _toServiceUserID = (String) jsonObject.get("to_service_user_id");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        try
          {
            _from = (String) jsonObject.get("from");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        try
          {
            _fromServiceUserID = (String) jsonObject.get("from_service_user_id");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        try
          {
            _text = (String) jsonObject.get("text");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        try
          {
            _scope = (String) jsonObject.get("scope");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        try
          {
            _dateSent = DateUtils.parseISO8601String((String) jsonObject.get("date_sent"));
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        try
          {
            _dateToDeliver = DateUtils.parseISO8601String((String) jsonObject.get("date_to_deliver"));
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        try
          {
            _point = new GeoPositionVO((JSONObject) jsonObject.get("point"));
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        try
          {
            _mimeType = (String) jsonObject.get("mime_type");
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
        JSONArray jsonArray = (JSONArray) jsonObject.get("attachments");
        if (jsonArray != null)
          {
            if (jsonArray.size() > 0)
              {
                _attachments = new ItemVO[jsonArray.size()];
                for (int index = 0; index < _attachments.length; index++)
                  try
                    {
                      _attachments[index] = new ItemVO((JSONObject) jsonArray.get(index));
                      if (_attachments[index].getType() == ItemType.AUDIO)
                        _messageType = MessageType.VOICE;
                    }
                  catch (APIProtocolException protocolException)
                    {
                      Debug.displayStack(this, protocolException);
                    }
              }
          }
      }
  }

  public void setFrom(String from)
  {
    String oldFrom = _from;
    this._from = from;
    propertyChangeSupport.firePropertyChange("From", oldFrom, from);
  }

  public String getFrom()
  {
    return (_from);
  }

  public void setFromServiceUserID(String fromServiceUserID)
  {
    String oldFromServiceUserID = _fromServiceUserID;
    this._fromServiceUserID = fromServiceUserID;
    propertyChangeSupport.firePropertyChange("FromServiceUserID", oldFromServiceUserID, fromServiceUserID);
  }

  public String getFromServiceUserID()
  {
    return (_fromServiceUserID);
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

  public void setDateSent(String dateSent)
  {
    try
      {
        setDateSent(DateUtils.parseISO8601String(dateSent));
      }
    catch (ParseException parseException)
      {
        Debug.displayStack(this, parseException);
      }
  }

  public void setDateSent(Date dateSent)
  {
    Date oldDateSent = _dateSent;
    this._dateSent = dateSent;
    propertyChangeSupport.firePropertyChange("DateSent", oldDateSent, dateSent);
  }

  public String getDateSentAsString()
  {
    return DateUtils.getISO8601String(_dateSent);
  }

  public void setDateToDeliver(Date dateToDeliver)
  {
    Date oldDateToDeliver = _dateToDeliver;
    this._dateToDeliver = dateToDeliver;
    propertyChangeSupport.firePropertyChange("DateToDeliver", oldDateToDeliver, dateToDeliver);
  }

  public String getDateToDeliver()
  {
    return DateUtils.getISO8601String(_dateToDeliver);
  }

  public Date getDateSent()
  {
    return (_dateSent);
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

  public void setAttachments(ItemVO[] attachments)
  {
    ItemVO[] oldAttachments = _attachments;
    this._attachments = attachments;
    propertyChangeSupport.firePropertyChange("Attachments", oldAttachments, attachments);
  }

  public ItemVO[] getAttachments()
  {
    return (_attachments);
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
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", "message");
    if (_to != null)
      jsonObject.put("to", _to);
    if (_toServiceUserID != null)
      jsonObject.put("to_service_user_id", _toServiceUserID);
    if (_from != null)
      jsonObject.put("to", _from);
    if (_fromServiceUserID != null)
      jsonObject.put("from_service_user_id", _fromServiceUserID);
    if (_text != null)
      jsonObject.put("text", _text);
    if (_point != null)
      jsonObject.put("point", _point.toJSON());
    if (_mimeType != null)
      jsonObject.put("mime_type", _mimeType);
    if (_scope != null)
      jsonObject.put("scope", _scope);
    if (_dateSent != null)
      jsonObject.put("date_sent", getDateSentAsString());
    if (_dateToDeliver != null)
      jsonObject.put("date_to_deliver", getDateSentAsString());
    // jsonObject.put("read", _read);
    JSONArray jsonArray = new JSONArray();
    if (_attachments != null)
      for (int index = 0; index < _attachments.length; index++)
        jsonArray.add(_attachments[index].toJSON());
    jsonObject.put("attachments", jsonArray);
    return (jsonObject);
  }

  public void setRead(boolean read)
  {
    boolean oldRead = _read;
    this._read = read;
    propertyChangeSupport.firePropertyChange("Read", oldRead, read);
  }

  public boolean isRead()
  {
    return (_read);
  }

  public void setMessageType(MessageType messageType)
  {
    MessageType oldMessageType = _messageType;
    this._messageType = messageType;
    propertyChangeSupport.firePropertyChange("MessageType", oldMessageType, messageType);
  }

  public MessageType getMessageType()
  {
    return (_messageType);
  }
}
