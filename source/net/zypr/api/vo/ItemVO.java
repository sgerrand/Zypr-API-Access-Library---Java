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

import java.util.Enumeration;
import java.util.Hashtable;

import net.zypr.api.enums.ItemDisplay;
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.MessageType;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.Debug;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ItemVO
  extends GenericVO
  implements Comparable<ItemVO>
{
  private String _rawJSONString = null;
  private ItemType _type = ItemType.POI;
  private ItemDisplay _display = ItemDisplay.LIST;
  private String _service = "";
  private String _globalItemID = "";
  private String _serviceItemID = "";
  private int _listPriority = 0;
  private String _name = "";
  private String _description = "";
  private double _rating = 0;
  private GeoPositionVO _position = new GeoPositionVO();
  private String _iconURL = "";
  private String _serviceIconURL = "";
  private Hashtable<String, ActionHandlerVO> _actions = new Hashtable<String, ActionHandlerVO>();
  private ItemVO[] _attachments;
  private InfoVO _info;

  public ItemVO(ItemType itemType)
  {
    _type = itemType;
  }

  public ItemVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    _rawJSONString = jsonObject.toJSONString();
    String stringTemp = (String) jsonObject.get("type");
    try
      {
        _type = ItemType.valueOf(stringTemp.toUpperCase());
      }
    catch (IllegalArgumentException illegalArgumentException)
      {
        throw new APIProtocolException("Unknown Item Type : " + stringTemp);
      }
    stringTemp = (String) jsonObject.get("display");
    try
      {
        _display = ItemDisplay.valueOf(stringTemp.toUpperCase());
      }
    catch (IllegalArgumentException illegalArgumentException)
      {
        throw new APIProtocolException("Unknown Item display : " + stringTemp);
      }
    _service = (String) jsonObject.get("service").toString();
    _globalItemID = (String) jsonObject.get("global_item_id");
    _serviceItemID = (String) jsonObject.get("service_item_id");
    _listPriority = Integer.parseInt(jsonObject.get("list_priority").toString());
    _name = (String) jsonObject.get("name");
    _description = (String) jsonObject.get("description");
    try
      {
        _rating = Double.parseDouble(jsonObject.get("rating").toString());
      }
    catch (Exception exception)
      {
        _rating = 0;
      }
    try
      {
        _iconURL = (String) jsonObject.get("icon_uri");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
        Debug.print(_rawJSONString);
      }
    try
      {
        _serviceIconURL = (String) jsonObject.get("service_icon_uri");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
        Debug.print(_rawJSONString);
      }
    try
      {
        _position = new GeoPositionVO((JSONObject) jsonObject.get("point"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    JSONArray jsonArray = (JSONArray) jsonObject.get("actions");
    try
      {
        for (int index = 0; index < jsonArray.size(); index++)
          {
            ActionHandlerVO actionHandlerVO = new ActionHandlerVO((JSONObject) jsonArray.get(index));
            _actions.put(actionHandlerVO.getHandlerName(), actionHandlerVO);
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _info = InfoVO.getObject((JSONObject) jsonObject.get("info"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
        Debug.print(jsonObject.toJSONString());
      }
    try
      {
        jsonArray = (JSONArray) jsonObject.get("attachments");
        if (jsonArray != null)
          {
            if (jsonArray.size() > 0)
              {
                _attachments = new ItemVO[jsonArray.size()];
                for (int index = 0; index < _attachments.length; index++)
                  {
                    _attachments[index] = new ItemVO((JSONObject) jsonArray.get(index));
                    if (_attachments[index].getType() == ItemType.AUDIO && _info instanceof InfoMessageGetTypeVO)
                      ((InfoMessageGetTypeVO) _info).setMessageType(MessageType.VOICE);
                  }
              }
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
  }

  public String getRawJSONString()
  {
    return (_rawJSONString);
  }

  public void setType(ItemType type)
  {
    ItemType oldType = _type;
    this._type = type;
    propertyChangeSupport.firePropertyChange("Type", oldType, type);
  }

  public ItemType getType()
  {
    return (_type);
  }

  public void setDisplay(ItemDisplay display)
  {
    ItemDisplay oldDisplay = _display;
    this._display = display;
    propertyChangeSupport.firePropertyChange("Display", oldDisplay, display);
  }

  public ItemDisplay getDisplay()
  {
    return (_display);
  }

  public void setService(String service)
  {
    String oldService = _service;
    this._service = service;
    propertyChangeSupport.firePropertyChange("Service", oldService, service);
  }

  public String getService()
  {
    return (_service);
  }

  public void setGlobalItemID(String globalItemID)
  {
    String oldGlobalItemID = _globalItemID;
    this._globalItemID = globalItemID;
    propertyChangeSupport.firePropertyChange("GlobalItemID", oldGlobalItemID, globalItemID);
  }

  public String getGlobalItemID()
  {
    return (_globalItemID);
  }

  public void setServiceItemID(String serviceItemID)
  {
    String oldServiceItemID = _serviceItemID;
    this._serviceItemID = serviceItemID;
    propertyChangeSupport.firePropertyChange("ServiceItemID", oldServiceItemID, serviceItemID);
  }

  public String getServiceItemID()
  {
    return (_serviceItemID);
  }

  public void setListPriority(int listPriority)
  {
    int oldListPriority = _listPriority;
    this._listPriority = listPriority;
    propertyChangeSupport.firePropertyChange("ListPriority", oldListPriority, listPriority);
  }

  public int getListPriority()
  {
    return (_listPriority);
  }

  public void setName(String name)
  {
    String oldName = _name;
    this._name = name;
    propertyChangeSupport.firePropertyChange("Name", oldName, name);
  }

  public String getName()
  {
    return (_name);
  }

  public void setDescription(String description)
  {
    String oldDescription = _description;
    this._description = description;
    propertyChangeSupport.firePropertyChange("Description", oldDescription, description);
  }

  public String getDescription()
  {
    return (_description);
  }

  public void setRating(double rating)
  {
    double oldRating = _rating;
    this._rating = rating;
    propertyChangeSupport.firePropertyChange("Rating", oldRating, rating);
  }

  public double getRating()
  {
    return (_rating);
  }

  public void setPosition(GeoPositionVO position)
  {
    GeoPositionVO oldPosition = _position;
    this._position = position;
    propertyChangeSupport.firePropertyChange("Position", oldPosition, position);
  }

  public GeoPositionVO getPosition()
  {
    return (_position);
  }

  public void setIconURL(String iconURL)
  {
    String oldIconURL = _iconURL;
    this._iconURL = iconURL;
    propertyChangeSupport.firePropertyChange("IconURL", oldIconURL, iconURL);
  }

  public String getIconURL()
  {
    return (_iconURL);
  }

  public void setActions(Hashtable<String, ActionHandlerVO> actions)
  {
    Hashtable<String, ActionHandlerVO> oldActions = _actions;
    this._actions = actions;
    propertyChangeSupport.firePropertyChange("Actions", oldActions, actions);
  }

  public Hashtable<String, ActionHandlerVO> getActions()
  {
    return (_actions);
  }

  public ActionHandlerVO getAction(String handler)
  {
    return (_actions.get(handler));
  }

  public void setInfo(InfoVO info)
  {
    InfoVO oldInfo = _info;
    this._info = info;
    propertyChangeSupport.firePropertyChange("Info", oldInfo, info);
  }

  public InfoVO getInfo()
  {
    return (_info);
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

  public ItemVO getAttachmentByType(ItemType itemType)
  {
    ItemVO itemVO = null;
    if (_attachments != null)
      for (int index = 0; index < _attachments.length && itemVO == null; index++)
        if (_attachments[index].getType() == itemType)
          itemVO = _attachments[index];
    return (itemVO);
  }

  public void setServiceIconURL(String serviceIconURL)
  {
    String oldServiceIconURL = _serviceIconURL;
    this._serviceIconURL = serviceIconURL;
    propertyChangeSupport.firePropertyChange("ServiceIconURL", oldServiceIconURL, serviceIconURL);
  }

  public String getServiceIconURL()
  {
    return (_serviceIconURL);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    if (_type != null)
      jsonObject.put("type", _type.toString().toLowerCase());
    jsonObject.put("display", _display.name().toLowerCase());
    if (_service != null)
      jsonObject.put("service", _service);
    if (_globalItemID != null)
      jsonObject.put("global_item_id", _globalItemID);
    if (_serviceItemID != null)
      jsonObject.put("service_item_id", _serviceItemID);
    jsonObject.put("list_priority", _listPriority);
    if (_name != null)
      jsonObject.put("name", _name);
    if (_description != null)
      jsonObject.put("description", _description);
    jsonObject.put("rating", _rating);
    if (_iconURL != null)
      jsonObject.put("icon_uri", _iconURL);
    if (_position != null)
      jsonObject.put("point", _position.toJSON());
    if (_serviceIconURL != null)
      jsonObject.put("service_icon_uri", _serviceIconURL);
    JSONArray jsonArray = new JSONArray();
    if (_actions != null)
      for (Enumeration enumeration = _actions.keys(); enumeration.hasMoreElements(); )
        jsonArray.add(((ActionHandlerVO) _actions.get(enumeration.nextElement().toString())).toJSON());
    jsonObject.put("actions", jsonArray);
    jsonArray = new JSONArray();
    if (_info != null)
      jsonObject.put("info", _info.toJSON());
    jsonArray = new JSONArray();
    if (_attachments != null)
      for (int index = 0; index < _attachments.length; index++)
        jsonArray.add(_attachments[index].toJSON());
    jsonObject.put("attachments", jsonArray);
    return (jsonObject);
  }

  @Override
  public boolean equals(Object object)
  {
    if (this == object)
      return (true);
    if (!(object instanceof ItemVO))
      return (false);
    return (_globalItemID.equals(((ItemVO) object).getService()) && _globalItemID.equals(((ItemVO) object).getServiceItemID()));
  }

  public int compareTo(ItemVO itemVO)
  {
    if (itemVO.getType() == ItemType.MESSAGE)
      return (((InfoMessageGetTypeVO) itemVO.getInfo()).getDateSent().compareTo(((InfoMessageGetTypeVO) getInfo()).getDateSent()));
    else if (itemVO.getType() == ItemType.CONTACT)
      return (getName().compareTo(itemVO.getName()));
    return (0);
  }
}
