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

import java.util.Date;

import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.DateUtils;
import net.zypr.api.utils.Debug;

import org.json.simple.JSONObject;

public class InfoCalendarItemTypeVO
  extends InfoVO
{
  private Date _startTime;
  private Date _stopTime;
  private int _duration;
  private boolean _allDay;
  private AddressVO _address;
  private String _calendarID;
  private String _note;

  public InfoCalendarItemTypeVO()
  {
    super();
  }

  public InfoCalendarItemTypeVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    String type = (String) jsonObject.get("type");
    if (type == null || !type.equalsIgnoreCase("calendar_item"))
      throw new APIProtocolException("Unknown enumerated value : " + type);
    try
      {
        _startTime = DateUtils.parseISO8601String((String) jsonObject.get("start_time"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _stopTime = DateUtils.parseISO8601String((String) jsonObject.get("stop_time"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _duration = Integer.parseInt((String) jsonObject.get("duration"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _allDay = (Boolean) jsonObject.get("all_day");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _address = new AddressVO((JSONObject) jsonObject.get("address"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _calendarID = (String) jsonObject.get("calendar_id");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _note = (String) jsonObject.get("note");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  public void setStartTime(Date startTime)
  {
    Date oldStartTime = _startTime;
    this._startTime = startTime;
    propertyChangeSupport.firePropertyChange("StartTime", oldStartTime, startTime);
  }

  public Date getStartTime()
  {
    return (_startTime);
  }

  public void setStopTime(Date stopTime)
  {
    Date oldStopTime = _stopTime;
    this._stopTime = stopTime;
    propertyChangeSupport.firePropertyChange("StopTime", oldStopTime, stopTime);
  }

  public Date getStopTime()
  {
    return (_stopTime);
  }

  public void setDuration(int duration)
  {
    int oldDuration = _duration;
    this._duration = duration;
    propertyChangeSupport.firePropertyChange("Duration", oldDuration, duration);
  }

  public int getDuration()
  {
    return (_duration);
  }

  public void setAllDay(boolean allDay)
  {
    boolean oldAllDay = _allDay;
    this._allDay = allDay;
    propertyChangeSupport.firePropertyChange("AllDay", oldAllDay, allDay);
  }

  public boolean isAllDay()
  {
    return (_allDay);
  }

  public void setAddress(AddressVO address)
  {
    AddressVO oldAddress = _address;
    this._address = address;
    propertyChangeSupport.firePropertyChange("Address", oldAddress, address);
  }

  public AddressVO getAddress()
  {
    return (_address);
  }

  public void setCalendarID(String calendarID)
  {
    String oldCalendarID = _calendarID;
    this._calendarID = calendarID;
    propertyChangeSupport.firePropertyChange("CalendarID", oldCalendarID, calendarID);
  }

  public String getCalendarID()
  {
    return (_calendarID);
  }

  public void setNote(String note)
  {
    String oldNote = _note;
    this._note = note;
    propertyChangeSupport.firePropertyChange("Note", oldNote, note);
  }

  public String getNote()
  {
    return (_note);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", "calendar_item");
    if (_startTime != null)
      jsonObject.put("start_time", DateUtils.getISO8601String(_startTime));
    if (_stopTime != null)
      jsonObject.put("stop_time", DateUtils.getISO8601String(_stopTime));
    jsonObject.put("duration", _duration);
    jsonObject.put("all_day", _allDay);
    if (_address != null)
      jsonObject.put("address", _address.toJSON());
    if (_calendarID != null)
      jsonObject.put("calendar_id", _calendarID);
    if (_note != null)
      jsonObject.put("note", _note);
    return (jsonObject);
  }
}
