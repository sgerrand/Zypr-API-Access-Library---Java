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


package net.zypr.api;

import java.util.Hashtable;

import net.zypr.api.enums.APIVerbs;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIInvalidServiceException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.utils.Debug;
import net.zypr.api.vo.ServiceAuthStatusVO;
import net.zypr.api.vo.ServiceVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Auth
  extends Protocol
{
  public Auth()
    throws APICommunicationException, APIProtocolException
  {
    super(true);
  }

  public Auth(ServiceVO[] services)
  {
    super(services);
  }

  public String getDefaultServiceForLogin()
  {
    return (getDefaultService(APIVerbs.AUTH_LOGIN));
  }

  public StatusCode login(String username, String password, String deviceID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (login(username, password, getDefaultServiceForLogin(), deviceID));
  }

  public StatusCode login(String username, String password, String service, String deviceID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (!doesVerbExist(service, APIVerbs.AUTH_LOGIN))
      throw new APIInvalidServiceException(APIVerbs.AUTH_LOGIN, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("username", username);
    parameters.put("password", password);
    parameters.put("service", service);
    if (deviceID != null)
      parameters.put("deviceid", deviceID);
    JSONObject jsonObject = getJSON(APIVerbs.AUTH_LOGIN, parameters);
    StatusCode statusCode = getStatusCode(jsonObject);
    JSONArray jsonData = getDataArrayJSON(jsonObject);
    if (statusCode == StatusCode.SUCCESSFUL)
      {
        for (int index = 0; index < jsonData.size() && Session.getInstance().getToken() == null; index++)
          {
            try
              {
                Session.getInstance().setToken((String) ((JSONObject) jsonData.get(index)).get("token"));
                Session.getInstance().setDeviceID((String) ((JSONObject) jsonData.get(index)).get("deviceid"));
              }
            catch (Exception exception)
              {
                Debug.displayStack(this, exception);
              }
          }
        if (Session.getInstance().getToken() == null)
          throw new APIProtocolException("Unable to get token");
        Session.getInstance().setUsername(username);
        Session.getInstance().setPassword(password);
        Session.getInstance().loggedIn();
      }
    else
      {
        throw new APIServerErrorException(getStatusMessage(jsonObject));
      }
    return (statusCode);
  }

  public ServiceAuthStatusVO serviceAuthStatus()
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    JSONObject jsonObject = getJSON(APIVerbs.AUTH_SERVICE_AUTH_STATUS, parameters);
    ServiceAuthStatusVO serviceAuthStatusVO;
    if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
      serviceAuthStatusVO = new ServiceAuthStatusVO(getDataKeyedArrayJSON(jsonObject, "service_auth"), getDataKeyedArrayJSON(jsonObject, "service_status"));
    else
      throw new APIServerErrorException(getStatusMessage(jsonObject));
    Session.getInstance().setServiceAuthStatus(serviceAuthStatusVO);
    return (serviceAuthStatusVO);
  }

  public StatusCode createUser(String username, String password, String passwordConfirm)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("username", username);
    parameters.put("password", password);
    parameters.put("password_confirm", passwordConfirm);
    JSONObject jsonObject = getJSON(APIVerbs.AUTH_CREATE_USER, parameters);
    if (getStatusCode(jsonObject) != StatusCode.SUCCESSFUL)
      throw new APIServerErrorException(getStatusMessage(jsonObject));
    return (getStatusCode(jsonObject));
  }

  public StatusCode resetPassword(String username)
    throws APIProtocolException, APICommunicationException
  {
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("username", username);
    return (getStatusCode(getJSON(APIVerbs.AUTH_RESET_PASSWORD, parameters)));
  }

  public StatusCode logout()
    throws APIProtocolException, APICommunicationException
  {
    Hashtable<String, String> parameters = buildParameters();
    StatusCode statusCode = getStatusCode(getJSON(APIVerbs.AUTH_LOGOUT, parameters));
    if (statusCode == StatusCode.SUCCESSFUL)
      Session.getInstance().loggedOut();
    return (statusCode);
  }
}
