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

public class SocialServiceAccountVO
  extends GenericVO
{
  private String _servicename = "";
  private String _username = "";
  private String _password = "";

  public SocialServiceAccountVO()
  {
    super();
  }

  public SocialServiceAccountVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    try
      {
        _servicename = jsonObject.get("servicename").toString();
        _username = jsonObject.get("username").toString();
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

  public SocialServiceAccountVO(String servicename, String username, String password)
  {
    super();
    _servicename = servicename;
    _username = username;
    _password = password;
  }

  public void setServiceName(String servicename)
  {
    String oldServiceName = _servicename;
    this._servicename = servicename;
    propertyChangeSupport.firePropertyChange("ServiceName", oldServiceName, servicename);
  }

  public String getServiceName()
  {
    return (_servicename);
  }

  public void setUsername(String username)
  {
    String oldUsername = _username;
    this._username = username;
    propertyChangeSupport.firePropertyChange("Username", oldUsername, username);
  }

  public String getUsername()
  {
    return (_username);
  }

  public void setPassword(String password)
  {
    String oldPassword = _password;
    this._password = password;
    propertyChangeSupport.firePropertyChange("Password", oldPassword, password);
  }

  public String getPassword()
  {
    return (_password);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("servicename", _servicename);
    jsonObject.put("username", _username);
    if (_password != null)
      jsonObject.put("password", _password);
    return (jsonObject);
  }

  public void delete()
  {
    setPassword(null);
  }

  @Override
  public SocialServiceAccountVO clone()
    throws CloneNotSupportedException
  {
    return ((SocialServiceAccountVO) super.clone());
  }
}
