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

public class ServiceStatusVO
  extends GenericVO
{
  private String _serviceName;
  private String _userName;
  private boolean _currentlyAuthenticated;
  private String _authLinkForExistingConnection;
  private String _logoutLinkForExistingConnection;
  private String _serviceIconURI;

  public ServiceStatusVO()
  {
    super();
  }

  public ServiceStatusVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _serviceName = (String) jsonObject.get("servicename");
        _userName = (String) jsonObject.get("username");
        _currentlyAuthenticated = (Boolean) jsonObject.get("currently_authenticated");
        _authLinkForExistingConnection = (String) jsonObject.get("auth_link_for_existing_connection");
        _logoutLinkForExistingConnection = (String) jsonObject.get("logout_link_for_existing_connection");
        _serviceIconURI = (String) jsonObject.get("service_icon_uri");
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

  public void setServiceName(String serviceName)
  {
    String oldServiceName = _serviceName;
    this._serviceName = serviceName;
    propertyChangeSupport.firePropertyChange("ServiceName", oldServiceName, serviceName);
  }

  public String getServiceName()
  {
    return (_serviceName);
  }

  public void setUserName(String userName)
  {
    String oldUserName = _userName;
    this._userName = userName;
    propertyChangeSupport.firePropertyChange("UserName", oldUserName, userName);
  }

  public String getUserName()
  {
    return (_userName);
  }

  public void setCurrentlyAuthenticated(boolean currentlyAuthenticated)
  {
    boolean oldCurrentlyAuthenticated = _currentlyAuthenticated;
    this._currentlyAuthenticated = currentlyAuthenticated;
    propertyChangeSupport.firePropertyChange("CurrentlyAuthenticated", oldCurrentlyAuthenticated, currentlyAuthenticated);
  }

  public boolean isCurrentlyAuthenticated()
  {
    return (_currentlyAuthenticated);
  }

  public void setAuthLinkForExistingConnection(String authLinkForExistingConnection)
  {
    String oldAuthLinkForExistingConnection = _authLinkForExistingConnection;
    this._authLinkForExistingConnection = authLinkForExistingConnection;
    propertyChangeSupport.firePropertyChange("AuthLinkForExistingConnection", oldAuthLinkForExistingConnection, authLinkForExistingConnection);
  }

  public String getAuthLinkForExistingConnection()
  {
    return (_authLinkForExistingConnection);
  }

  public void setLogoutLinkForExistingConnection(String logoutLinkForExistingConnection)
  {
    String oldLogoutLinkForExistingConnection = _logoutLinkForExistingConnection;
    this._logoutLinkForExistingConnection = logoutLinkForExistingConnection;
    propertyChangeSupport.firePropertyChange("LogoutLinkForExistingConnection", oldLogoutLinkForExistingConnection, logoutLinkForExistingConnection);
  }

  public String getLogoutLinkForExistingConnection()
  {
    return (_logoutLinkForExistingConnection);
  }

  public void setServiceIconURI(String serviceIconURI)
  {
    String oldServiceIconURI = _serviceIconURI;
    this._serviceIconURI = serviceIconURI;
    propertyChangeSupport.firePropertyChange("ServiceIconURI", oldServiceIconURI, _serviceIconURI);
  }

  public String getServiceIconURI()
  {
    return (_serviceIconURI);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("servicename", _serviceName);
    jsonObject.put("username", _userName);
    jsonObject.put("currently_authenticated", _currentlyAuthenticated);
    jsonObject.put("auth_link_for_existing_connection", _authLinkForExistingConnection);
    jsonObject.put("logout_link_for_existing_connection", _logoutLinkForExistingConnection);
    jsonObject.put("service_icon_uri", _serviceIconURI);
    return (jsonObject);
  }
}
