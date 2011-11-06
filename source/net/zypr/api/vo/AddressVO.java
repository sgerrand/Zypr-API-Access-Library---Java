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

import java.io.Serializable;

import net.zypr.api.exceptions.APIProtocolException;

import org.json.simple.JSONObject;

public class AddressVO
  extends GenericVO
  implements Serializable
{
  private String _type;
  private String _number;
  private String _street;
  private String _city;
  private String _state;
  private String _province;
  private String _postal;
  private String _country;

  public AddressVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _type = (String) jsonObject.get("type");
        if (_type != null)
          _type = _type.trim();
        _number = "" + jsonObject.get("number");
        if (_number != null)
          _number = _number.trim();
        _street = (String) jsonObject.get("street");
        if (_street != null)
          _street = _street.trim();
        _city = (String) jsonObject.get("city");
        if (_city != null)
          _city = _city.trim();
        _state = (String) jsonObject.get("state");
        if (_state != null)
          _state = _state.trim();
        _province = (String) jsonObject.get("province");
        if (_province != null)
          _province = _province.trim();
        _postal = (String) jsonObject.get("postal");
        if (_postal != null)
          _postal = _postal.trim();
        _country = (String) jsonObject.get("country");
        if (_country != null)
          _country = _country.trim();
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

  public AddressVO(String type, String number, String street, String city, String state, String province, String postal, String country)
  {
    super();
    this._type = type;
    this._number = number;
    this._street = street;
    this._city = city;
    this._state = state;
    this._province = province;
    this._postal = postal;
    this._country = country;
  }

  public AddressVO(String city, String state)
  {
    super();
    this._type = "";
    this._number = "";
    this._street = "";
    this._city = city;
    this._state = state;
    this._province = "";
    this._postal = "";
    this._country = "";
  }

  public AddressVO()
  {
    super();
  }

  public void setType(String type)
  {
    String oldType = _type;
    this._type = type;
    propertyChangeSupport.firePropertyChange("Type", oldType, type);
  }

  public String getType()
  {
    return (_type);
  }

  public void setNumber(String number)
  {
    String oldNumber = _number;
    this._number = number;
    propertyChangeSupport.firePropertyChange("Number", oldNumber, number);
  }

  public String getNumber()
  {
    return (_number);
  }

  public void setStreet(String street)
  {
    String oldStreet = _street;
    this._street = street;
    propertyChangeSupport.firePropertyChange("Street", oldStreet, street);
  }

  public String getStreet()
  {
    return (_street);
  }

  public void setCity(String city)
  {
    String oldCity = _city;
    this._city = city;
    propertyChangeSupport.firePropertyChange("City", oldCity, city);
  }

  public String getCity()
  {
    return (_city);
  }

  public void setState(String state)
  {
    String oldState = _state;
    this._state = state;
    propertyChangeSupport.firePropertyChange("State", oldState, state);
  }

  public String getState()
  {
    return (_state);
  }

  public void setProvince(String province)
  {
    String oldProvince = _province;
    this._province = province;
    propertyChangeSupport.firePropertyChange("Province", oldProvince, province);
  }

  public String getProvince()
  {
    return (_province);
  }

  public void setPostal(String postal)
  {
    String oldPostal = _postal;
    this._postal = postal;
    propertyChangeSupport.firePropertyChange("Postal", oldPostal, postal);
  }

  public String getPostal()
  {
    return (_postal);
  }

  public void setCountry(String country)
  {
    String oldCountry = _country;
    this._country = country;
    propertyChangeSupport.firePropertyChange("Country", oldCountry, country);
  }

  public String getCountry()
  {
    return (_country);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    if (_type != null)
      jsonObject.put("type", _type);
    if (_number != null)
      jsonObject.put("number", _number);
    if (_street != null)
      jsonObject.put("street", _street);
    if (_city != null)
      jsonObject.put("city", _city);
    if (_state != null)
      jsonObject.put("state", _state);
    if (_province != null)
      jsonObject.put("province", _province);
    if (_postal != null)
      jsonObject.put("postal", _postal);
    if (_country != null)
      jsonObject.put("country", _country);
    return (jsonObject);
  }

  @Override
  public AddressVO clone()
    throws CloneNotSupportedException
  {
    return ((AddressVO) super.clone());
  }

  public String toAddressString()
  {
    String output = "";
    if (_number != null && !_number.equals("") && !_number.equals("0"))
      output += _number + " ";
    if (_street != null && !_street.equals(""))
      output += _street;
    if (_city != null && !_city.equals(""))
      output += (!output.equals("") ? ", " : "") + _city;
    if (_state != null && !_state.equals(""))
      output += (!output.equals("") ? ", " : "") + _state;
    if (_province != null && !_province.equals(""))
      output += (!output.equals("") ? ", " : "") + _province;
    if (_postal != null && !_postal.equals(""))
      output += (!output.equals("") ? ", " : "") + _postal;
    if (_country != null && !_country.equals(""))
      output += (!output.equals("") ? " " : "") + _country;
    return (output);
  }

  public String toCityStateCountry()
  {
    String output = "";
    if (_city != null && !_city.equals(""))
      output += _city;
    if (_state != null && !_state.equals(""))
      output += (!output.equals("") ? ", " : "") + _state;
    if (_province != null && !_province.equals(""))
      output += (!output.equals("") ? ", " : "") + _province;
    if (_country != null && !_country.equals(""))
      output += (!output.equals("") ? ", " : "") + _country;
    return (output);
  }

  public String toString()
  {
    return (toAddressString());
  }
}
