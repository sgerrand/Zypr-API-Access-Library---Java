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

import java.util.Iterator;
import java.util.Map;

import net.zypr.api.exceptions.APIProtocolException;

import org.json.simple.JSONObject;

public class VoiceCommandVariableVO
  extends GenericVO
{
  private String _name;
  private Object _value;

  public VoiceCommandVariableVO()
  {
    super();
  }

  public VoiceCommandVariableVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _name = (String) jsonObject.get("name");
        _value = jsonObject.get("value");
        String type = (String) jsonObject.get("type");
        if (_value instanceof String)
          {
            if (!type.equalsIgnoreCase("string"))
              throw new APIProtocolException("Variable type \"string\" and value type \"" + type + "\" mismatch!");
          }
        else if (_value instanceof Number)
          {
            if (!type.equalsIgnoreCase("number"))
              throw new APIProtocolException("Variable type \"number\" and value type \"" + type + "\" mismatch!");
          }
        else if (_value instanceof Map)
          {
            if (!type.equalsIgnoreCase("object"))
              throw new APIProtocolException("Variable type \"object\" and value type \"" + type + "\" mismatch!");
            Iterator keySetIterator = ((JSONObject) _value).keySet().iterator();
            if (keySetIterator.hasNext())
              {
                type = (String) keySetIterator.next();
                JSONObject value = (JSONObject) ((JSONObject) _value).get(type);
                if (type.equalsIgnoreCase("address"))
                  _value = new AddressVO(value);
                else if (type.equalsIgnoreCase("category"))
                  _value = new CategoryVO(value);
                else if (type.equalsIgnoreCase("bbox"))
                  _value = new GeoBoundsVO(value);
                else if (type.equalsIgnoreCase("point"))
                  _value = new GeoPositionVO(value);
                else if (type.equalsIgnoreCase("phone_number"))
                  _value = new PhoneNumberVO(value);
                else if (type.equalsIgnoreCase("email"))
                  _value = new EMailAddressVO(value);
                else if (type.equalsIgnoreCase("uri"))
                  _value = new UniformResourceIdentifierVO(value);
                else
                  throw new APIProtocolException("Unknown object type \"" + type + "\"");
              }
            else
              {
                throw new APIProtocolException("Empty value object.");
              }
          }
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

  public void setValue(Object value)
  {
    Object oldValue = _value;
    this._value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public Object getValue()
  {
    return (_value);
  }

  public String getValueAsString()
  {
    return (_value == null ? null : "" + _value);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", _name);
    jsonObject.put("value", _value);
    return (jsonObject);
  }
}
