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

public class InfoUserTypeVO
  extends InfoVO
{
  private SocialServiceAccountVO[] _socialServiceAccounts = new SocialServiceAccountVO[0];
  private String _email;
  private String _firstName;
  private String _lastName;
  private String _statusMessage;
  private GeoPositionVO _currentPosition;
  private boolean _sendResetPasswordEMail;
  private String _newPassword;
  private String _confirmNewPassword;
  private String _currentPassword;
  private boolean _locationIsVisible = true;
  private AddressVO[] _addresses;
  private PhoneNumberVO[] _phoneNumbers;
  private String _service;
  private ImageVO[] _images;

  public InfoUserTypeVO()
  {
    super();
  }

  public InfoUserTypeVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    String type = (String) jsonObject.get("type");
    if (type == null || !type.equalsIgnoreCase("user"))
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
        _locationIsVisible = ((Boolean) jsonObject.get("location_is_visible")).booleanValue();
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    //    try
    //    {
    //      _sendResetPasswordEMail = ((Boolean) jsonObject.get("send_reset_password_email")).booleanValue();
    //    }
    //    catch (Exception exception)
    //    {
    //      Debug.displayStack(this, exception, 1);
    //    }
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
        jsonArray = (JSONArray) jsonObject.get("images");
        if (jsonArray != null)
          {
            _images = new ImageVO[jsonArray.size()];
            for (int index = 0; index < _images.length; index++)
              _images[index] = new ImageVO((JSONObject) jsonArray.get(index));
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

  public SocialServiceAccountVO getSocialServiceAccount(String servicename, String username, String password)
  {
    return (getSocialServiceAccount(new SocialServiceAccountVO(servicename, username, password)));
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

  public void setSendResetPasswordEMail(boolean sendResetPasswordEMail)
  {
    boolean oldSendResetPasswordEMail = _sendResetPasswordEMail;
    this._sendResetPasswordEMail = sendResetPasswordEMail;
    propertyChangeSupport.firePropertyChange("SendResetPasswordEMail", oldSendResetPasswordEMail, sendResetPasswordEMail);
  }

  public boolean isSendResetPasswordEMail()
  {
    return (_sendResetPasswordEMail);
  }

  public void setNewPassword(String newPassword)
  {
    String oldNewPassword = _newPassword;
    this._newPassword = newPassword;
    propertyChangeSupport.firePropertyChange("NewPassword", oldNewPassword, newPassword);
  }

  public String getNewPassword()
  {
    return (_newPassword);
  }

  public void setConfirmNewPassword(String confirmNewPassword)
  {
    String oldConfirmNewPassword = _confirmNewPassword;
    this._confirmNewPassword = confirmNewPassword;
    propertyChangeSupport.firePropertyChange("ConfirmNewPassword", oldConfirmNewPassword, confirmNewPassword);
  }

  public String getConfirmNewPassword()
  {
    return (_confirmNewPassword);
  }

  public void setCurrentPassword(String currentPassword)
  {
    String oldCurrentPassword = _currentPassword;
    this._currentPassword = currentPassword;
    propertyChangeSupport.firePropertyChange("OldPassword", oldCurrentPassword, currentPassword);
  }

  public String getCurrentPassword()
  {
    return (_currentPassword);
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

  public void setImages(ImageVO[] images)
  {
    ImageVO[] oldImages = _images;
    this._images = images;
    propertyChangeSupport.firePropertyChange("Images", oldImages, images);
  }

  public ImageVO[] getImages()
  {
    return (_images);
  }

  @Override
  public InfoUserTypeVO clone()
    throws CloneNotSupportedException
  {
    InfoUserTypeVO infoUserTypeVO = (InfoUserTypeVO) super.clone();
    if (_socialServiceAccounts != null)
      {
        SocialServiceAccountVO[] socialServiceAccounts = new SocialServiceAccountVO[_socialServiceAccounts.length];
        for (int index = 0; index < socialServiceAccounts.length; index++)
          socialServiceAccounts[index] = _socialServiceAccounts[index].clone();
        infoUserTypeVO.setSocialServiceAccounts(socialServiceAccounts);
      }
    if (_addresses != null)
      {
        AddressVO[] addresses = new AddressVO[_addresses.length];
        for (int index = 0; index < addresses.length; index++)
          addresses[index] = _addresses[index].clone();
        infoUserTypeVO.setAddresses(addresses);
      }
    if (_phoneNumbers != null)
      {
        PhoneNumberVO[] phoneNumbers = new PhoneNumberVO[_phoneNumbers.length];
        for (int index = 0; index < phoneNumbers.length; index++)
          phoneNumbers[index] = _phoneNumbers[index].clone();
        infoUserTypeVO.setPhoneNumbers(phoneNumbers);
      }
    if (_images != null)
      {
        ImageVO[] images = new ImageVO[_images.length];
        for (int index = 0; index < images.length; index++)
          images[index] = _images[index].clone();
        infoUserTypeVO.setImages(images);
      }
    return (infoUserTypeVO);
  }

  public JSONObject diffAsJSON(InfoUserTypeVO infoUserTypeVO)
  {
    JSONObject jsonObject = new JSONObject();
    JSONObject jsonInfo = new JSONObject();
    if ((_email == null && infoUserTypeVO.getEmail() != null) || (_email != null && infoUserTypeVO.getEmail() != null && !_email.equals(infoUserTypeVO.getEmail())))
      jsonInfo.put("email", infoUserTypeVO.getEmail());
    if ((_firstName == null && infoUserTypeVO.getFirstName() != null) || (_firstName != null && infoUserTypeVO.getFirstName() != null && !_firstName.equals(infoUserTypeVO.getFirstName())))
      jsonInfo.put("first_name", infoUserTypeVO.getFirstName());
    if ((_lastName == null && infoUserTypeVO.getLastName() != null) || (_lastName != null && infoUserTypeVO.getLastName() != null && !_lastName.equals(infoUserTypeVO.getLastName())))
      jsonInfo.put("last_name", infoUserTypeVO.getLastName());
    if ((_statusMessage == null && infoUserTypeVO.getStatusMessage() != null) || (_statusMessage != null && infoUserTypeVO.getStatusMessage() != null && !_statusMessage.equals(infoUserTypeVO.getStatusMessage())))
      jsonInfo.put("status", infoUserTypeVO.getStatusMessage());
    if (_locationIsVisible != infoUserTypeVO.isLocationIsVisible())
      jsonInfo.put("location_is_visible", infoUserTypeVO.isLocationIsVisible());
    if (_sendResetPasswordEMail != infoUserTypeVO.isSendResetPasswordEMail())
      jsonInfo.put("send_reset_password_email", infoUserTypeVO.isSendResetPasswordEMail());
    if ((_newPassword == null && infoUserTypeVO.getNewPassword() != null) || (_newPassword != null && infoUserTypeVO.getNewPassword() != null && !_newPassword.equals(infoUserTypeVO.getNewPassword())) && (_confirmNewPassword == null && infoUserTypeVO.getConfirmNewPassword() != null) || (_confirmNewPassword != null && infoUserTypeVO.getConfirmNewPassword() != null && !_confirmNewPassword.equals(infoUserTypeVO.getConfirmNewPassword())) && (_currentPassword == null && infoUserTypeVO.getCurrentPassword() != null) || (_currentPassword != null && infoUserTypeVO.getCurrentPassword() != null && !_currentPassword.equals(infoUserTypeVO.getCurrentPassword())))
      {
        JSONArray jsonPasswordArray = new JSONArray();
        jsonPasswordArray.add(infoUserTypeVO.getNewPassword());
        jsonPasswordArray.add(infoUserTypeVO.getConfirmNewPassword());
        jsonPasswordArray.add(infoUserTypeVO.getCurrentPassword());
        jsonInfo.put("password", jsonPasswordArray);
      }
    if (infoUserTypeVO.getSocialServiceAccounts() != null)
      {
        JSONArray jsonSSAArray = new JSONArray();
        for (int index = 0; index < infoUserTypeVO.getSocialServiceAccounts().length; index++)
          {
            SocialServiceAccountVO currentSocialServiceAccountVO = getSocialServiceAccount(infoUserTypeVO.getSocialServiceAccounts()[index]);
            if (currentSocialServiceAccountVO == null || infoUserTypeVO.getSocialServiceAccounts()[index].getPassword() == null || !infoUserTypeVO.getSocialServiceAccounts()[index].getPassword().equals("\0\0\0\0\0\0"))
              jsonSSAArray.add(infoUserTypeVO.getSocialServiceAccounts()[index].toJSON());
          }
        if (jsonSSAArray.size() > 0)
          jsonInfo.put("service_ids", jsonSSAArray);
      }
    jsonObject.put("info", jsonInfo);
    return (jsonObject);
  }

  public String diffAsJSONString(InfoUserTypeVO infoUserTypeVO)
  {
    return (diffAsJSON(infoUserTypeVO).toJSONString());
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

  public void setLocationIsVisible(boolean locationIsVisible)
  {
    boolean oldLocationIsVisible = _locationIsVisible;
    this._locationIsVisible = locationIsVisible;
    propertyChangeSupport.firePropertyChange("LocationIsVisible", oldLocationIsVisible, _locationIsVisible);
  }

  public boolean isLocationIsVisible()
  {
    return (_locationIsVisible);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", "user");
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
    jsonObject.put("location_is_visible", _locationIsVisible);
    jsonObject.put("send_reset_password_email", _sendResetPasswordEMail);
    jsonArray = new JSONArray();
    if (_newPassword != null && _confirmNewPassword != null && _currentPassword != null)
      {
        jsonArray.add(_newPassword);
        jsonArray.add(_confirmNewPassword);
        jsonArray.add(_currentPassword);
        jsonObject.put("password", jsonArray);
      }
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
    if (_images != null)
      for (int index = 0; index < _images.length; index++)
        jsonArray.add(_images[index].toJSON());
    jsonObject.put("images", jsonArray);
    return (jsonObject);
  }
}
