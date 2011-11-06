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
import net.zypr.api.utils.Debug;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class InfoContactTypeVO
  extends InfoVO
{
  private SocialServiceAccountVO[] _socialServiceAccounts = new SocialServiceAccountVO[0];
  private String _email;
  private String _firstName;
  private String _lastName;
  private String _statusMessage;
  private GeoPositionVO _currentPosition;
  private AddressVO[] _addresses;
  private PhoneNumberVO[] _phoneNumbers;
  private String _service;
  private String _relation;
  private String _title;
  private String _businessName;
  private UniformResourceIdentifierVO[] _uris;

  public InfoContactTypeVO()
  {
    super();
  }

  public InfoContactTypeVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    String type = (String) jsonObject.get("type");
    if (type == null || !type.equalsIgnoreCase("contact"))
      throw new APIProtocolException("Unknown enumerated value : " + type);
    JSONArray jsonArray;
    try
      {
        jsonArray = (JSONArray) jsonObject.get("service_ids");
        if (jsonArray != null)
          {
            _socialServiceAccounts = new SocialServiceAccountVO[jsonArray.size()];
            for (int index = 0; index < _socialServiceAccounts.length; index++)
              _socialServiceAccounts[index] = new SocialServiceAccountVO((JSONObject) jsonArray.get(index));
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _email = (String) jsonObject.get("email");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _firstName = (String) jsonObject.get("first_name");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _lastName = (String) jsonObject.get("last_name");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _statusMessage = (String) jsonObject.get("status");
        if (_statusMessage == null)
          _statusMessage = "";
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _currentPosition = new GeoPositionVO((JSONObject) jsonObject.get("point"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        jsonArray = (JSONArray) jsonObject.get("addresses");
        if (jsonArray != null)
          {
            _addresses = new AddressVO[jsonArray.size()];
            for (int index = 0; index < _addresses.length; index++)
              _addresses[index] = new AddressVO((JSONObject) jsonArray.get(index));
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        jsonArray = (JSONArray) jsonObject.get("phones");
        if (jsonArray != null)
          {
            _phoneNumbers = new PhoneNumberVO[jsonArray.size()];
            for (int index = 0; index < _phoneNumbers.length; index++)
              _phoneNumbers[index] = new PhoneNumberVO((JSONObject) jsonArray.get(index));
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _service = (String) jsonObject.get("service");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _relation = (String) jsonObject.get("relation");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _title = (String) jsonObject.get("title");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _businessName = (String) jsonObject.get("business_name");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        jsonArray = (JSONArray) jsonObject.get("uris");
        if (jsonArray != null)
          {
            _uris = new UniformResourceIdentifierVO[jsonArray.size()];
            for (int index = 0; index < _uris.length; index++)
              _uris[index] = new UniformResourceIdentifierVO((JSONObject) jsonArray.get(index));
          }
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  public void setFirstName(String firstName)
  {
    String oldFirstName = _firstName;
    this._firstName = firstName;
    propertyChangeSupport.firePropertyChange("FirstName", oldFirstName, firstName);
  }

  public String getFirstName()
  {
    return (_firstName);
  }

  public void setLastName(String lastName)
  {
    String oldLastName = _lastName;
    this._lastName = lastName;
    propertyChangeSupport.firePropertyChange("LastName", oldLastName, lastName);
  }

  public String getLastName()
  {
    return (_lastName);
  }

  public void setStatusMessage(String statusMessage)
  {
    String oldStatusMessage = _statusMessage;
    this._statusMessage = statusMessage;
    propertyChangeSupport.firePropertyChange("StatusMessage", oldStatusMessage, statusMessage);
  }

  public String getStatusMessage()
  {
    return (_statusMessage);
  }

  public void setAddresses(AddressVO[] addresses)
  {
    AddressVO[] oldAddresses = _addresses;
    this._addresses = addresses;
    propertyChangeSupport.firePropertyChange("Addresses", oldAddresses, addresses);
  }

  public AddressVO[] getAddresses()
  {
    return (_addresses);
  }

  public void setPhoneNumbers(PhoneNumberVO[] phoneNumbers)
  {
    PhoneNumberVO[] oldPhoneNumbers = _phoneNumbers;
    this._phoneNumbers = phoneNumbers;
    propertyChangeSupport.firePropertyChange("PhoneNumbers", oldPhoneNumbers, phoneNumbers);
  }

  public PhoneNumberVO[] getPhoneNumbers()
  {
    return (_phoneNumbers);
  }

  public void setSocialServiceAccounts(SocialServiceAccountVO[] socialServiceAccounts)
  {
    SocialServiceAccountVO[] oldSocialServiceAccounts = _socialServiceAccounts;
    this._socialServiceAccounts = socialServiceAccounts;
    propertyChangeSupport.firePropertyChange("SocialServiceAccounts", oldSocialServiceAccounts, socialServiceAccounts);
  }

  public SocialServiceAccountVO[] getSocialServiceAccounts()
  {
    return (_socialServiceAccounts);
  }

  public SocialServiceAccountVO getSocialServiceAccount(SocialServiceAccountVO socialServiceAccount)
  {
    for (int index = 0; index < _socialServiceAccounts.length; index++)
      if (_socialServiceAccounts[index].equals(socialServiceAccount))
        return (_socialServiceAccounts[index]);
    return (null);
  }

  public SocialServiceAccountVO getSocialServiceAccount(String servicename, String username)
  {
    return (getSocialServiceAccount(new SocialServiceAccountVO(servicename, username, null)));
  }

  public void setEmail(String email)
  {
    String oldEmail = _email;
    this._email = email;
    propertyChangeSupport.firePropertyChange("Email", oldEmail, email);
  }

  public String getEmail()
  {
    return (_email);
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

  public void addSocialServiceAccount(SocialServiceAccountVO socialServiceAccount)
  {
    SocialServiceAccountVO[] newSocialServiceAccounts = new SocialServiceAccountVO[_socialServiceAccounts.length + 1];
    System.arraycopy(_socialServiceAccounts, 0, newSocialServiceAccounts, 0, _socialServiceAccounts.length);
    newSocialServiceAccounts[_socialServiceAccounts.length] = socialServiceAccount;
    _socialServiceAccounts = newSocialServiceAccounts;
  }

  @Override
  public InfoContactTypeVO clone()
    throws CloneNotSupportedException
  {
    InfoContactTypeVO infoContactTypeVO = (InfoContactTypeVO) super.clone();
    if (_socialServiceAccounts != null)
      {
        SocialServiceAccountVO[] socialServiceAccounts = new SocialServiceAccountVO[_socialServiceAccounts.length];
        for (int index = 0; index < socialServiceAccounts.length; index++)
          socialServiceAccounts[index] = _socialServiceAccounts[index].clone();
        infoContactTypeVO.setSocialServiceAccounts(socialServiceAccounts);
      }
    if (_addresses != null)
      {
        AddressVO[] addresses = new AddressVO[_addresses.length];
        for (int index = 0; index < addresses.length; index++)
          addresses[index] = _addresses[index].clone();
        infoContactTypeVO.setAddresses(addresses);
      }
    if (_phoneNumbers != null)
      {
        PhoneNumberVO[] phoneNumbers = new PhoneNumberVO[_phoneNumbers.length];
        for (int index = 0; index < phoneNumbers.length; index++)
          phoneNumbers[index] = _phoneNumbers[index].clone();
        infoContactTypeVO.setPhoneNumbers(phoneNumbers);
      }
    return (infoContactTypeVO);
  }

  public void setCurrentPosition(GeoPositionVO currentPosition)
  {
    GeoPositionVO oldCurrentPosition = _currentPosition;
    this._currentPosition = currentPosition;
    propertyChangeSupport.firePropertyChange("CurrentPosition", oldCurrentPosition, currentPosition);
  }

  public GeoPositionVO getCurrentPosition()
  {
    return (_currentPosition);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", "contact");
    if (_service != null)
      jsonObject.put("service", _service);
    JSONArray jsonArray = new JSONArray();
    if (_socialServiceAccounts != null)
      for (int index = 0; index < _socialServiceAccounts.length; index++)
        jsonArray.add(_socialServiceAccounts[index].toJSON());
    jsonObject.put("service_ids", jsonArray);
    if (_email != null)
      jsonObject.put("email", _email);
    if (_firstName != null)
      jsonObject.put("first_name", _firstName);
    if (_lastName != null)
      jsonObject.put("last_name", _lastName);
    if (_statusMessage != null)
      jsonObject.put("status", _statusMessage);
    if (_currentPosition != null)
      jsonObject.put("point", _currentPosition);
    jsonArray = new JSONArray();
    if (_addresses != null)
      for (int index = 0; index < _addresses.length; index++)
        jsonArray.add(_addresses[index].toJSON());
    jsonObject.put("addresses", jsonArray);
    jsonArray = new JSONArray();
    if (_phoneNumbers != null)
      for (int index = 0; index < _phoneNumbers.length; index++)
        jsonArray.add(_phoneNumbers[index].toJSON());
    jsonObject.put("phones", jsonArray);
    if (_relation != null)
      jsonObject.put("relation", _relation);
    if (_title != null)
      jsonObject.put("title", _title);
    if (_businessName != null)
      jsonObject.put("business_name", _businessName);
    if (_uris != null)
      for (int index = 0; index < _uris.length; index++)
        jsonArray.add(_uris[index].toJSON());
    jsonObject.put("uris", jsonArray);
    return (jsonObject);
  }

  public void setRelation(String relation)
  {
    String oldRelation = _relation;
    this._relation = relation;
    propertyChangeSupport.firePropertyChange("Relation", oldRelation, relation);
  }

  public String getRelation()
  {
    return (_relation);
  }

  public void setTitle(String _title)
  {
    String oldTitle = _title;
    this._title = _title;
    propertyChangeSupport.firePropertyChange("Title", oldTitle, _title);
  }

  public String getTitle()
  {
    return (_title);
  }

  public void setBusinessName(String businessName)
  {
    String oldBusinessName = _businessName;
    this._businessName = businessName;
    propertyChangeSupport.firePropertyChange("BusinessName", oldBusinessName, businessName);
  }

  public String getBusinessName()
  {
    return (_businessName);
  }

  public void setUris(UniformResourceIdentifierVO[] uris)
  {
    UniformResourceIdentifierVO[] oldUris = _uris;
    this._uris = uris;
    propertyChangeSupport.firePropertyChange("Uris", oldUris, uris);
  }

  public UniformResourceIdentifierVO[] getUris()
  {
    return (_uris);
  }
}
