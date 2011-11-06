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

public class VerbParameterVO
  extends GenericVO
{
  private String _name;
  private Object _value;

  public VerbParameterVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    if (jsonObject != null)
      try
        {
          _name = (String) jsonObject.get("name");
          _value = jsonObject.get("value");
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
    _name = name;
    propertyChangeSupport.firePropertyChange("Name", oldName, name);
  }

  public String getName()
  {
    return (_name);
  }

  public void setValue(Object value)
  {
    Object oldValue = _value;
    _value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public void setValue(int value)
  {
    Object oldValue = _value;
    _value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public void setValue(String value)
  {
    Object oldValue = _value;
    _value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public void setValue(double value)
  {
    Object oldValue = _value;
    _value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public void setValue(short value)
  {
    Object oldValue = _value;
    _value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public void setValue(float value)
  {
    Object oldValue = _value;
    _value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public void setValue(boolean value)
  {
    Object oldValue = _value;
    _value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public void setValue(long value)
  {
    Object oldValue = _value;
    _value = value;
    propertyChangeSupport.firePropertyChange("Value", oldValue, value);
  }

  public String getValueAsString()
  {
    return (_value.toString());
  }

  public Object getValueAsObject()
  {
    return (_value);
  }

  public short getValueAsShort()
    throws NumberFormatException
  {
    if (_value instanceof Short)
      return ((Short) _value);
    return (Short.parseShort(_value.toString()));
  }

  public int getValueAsInt()
    throws NumberFormatException
  {
    if (_value instanceof Integer)
      return ((Integer) _value);
    return (Integer.parseInt(_value.toString()));
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    if (_name != null)
      jsonObject.put("name", _name);
    if (_value != null)
      jsonObject.put("value", _value);
    return (jsonObject);
  }
}
