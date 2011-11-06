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

import net.zypr.api.enums.EMailAddressType;
import net.zypr.api.exceptions.APIProtocolException;

import org.json.simple.JSONObject;

public class EMailAddressVO
  extends GenericVO
{
  private EMailAddressType _type;
  private String _title;
  private String _value;

  public EMailAddressVO()
  {
    super();
  }

  public EMailAddressVO(EMailAddressType type, String title, String value)
  {
    super();
    _type = type;
    _title = title;
    _value = value;
  }

  public EMailAddressVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    try
      {
        String type = (String) jsonObject.get("type");
        EMailAddressType[] availableType = EMailAddressType.values();
        for (int index = 0; index < availableType.length && _type == null; index++)
          if (availableType[index].getValue().equalsIgnoreCase(type))
            _type = availableType[index];
        if (_type == null)
          throw new APIProtocolException("Unknown enumerated value : " + type);
        _title = (String) jsonObject.get("title");
        _value = (String) jsonObject.get("value");
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

  public void setType(EMailAddressType type)
  {
    EMailAddressType oldType = _type;
    this._type = type;
    propertyChangeSupport.firePropertyChange("Type", oldType, type);
  }

  public EMailAddressType getType()
  {
    return (_type);
  }

  public void setTitle(String title)
  {
    String oldTitle = _title;
    this._title = title;
    propertyChangeSupport.firePropertyChange("Title", oldTitle, title);
  }

  public String getTitle()
  {
    return (_title);
  }

  public void setValue(String value)
  {
    String oldValue = _value;
    this._value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public String getValue()
  {
    return (_value);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", _type.getValue());
    jsonObject.put("title", _title);
    jsonObject.put("value", _value);
    return (jsonObject);
  }

  @Override
  public EMailAddressVO clone()
    throws CloneNotSupportedException
  {
    return ((EMailAddressVO) super.clone());
  }
}
