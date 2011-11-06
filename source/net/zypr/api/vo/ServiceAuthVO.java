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

public class ServiceAuthVO
  extends GenericVO
{
  private String _serviceName;
  private String _authLinkGeneric;
  private String _serviceIconURI;

  public ServiceAuthVO()
  {
    super();
  }

  public ServiceAuthVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _serviceName = (String) jsonObject.get("servicename");
        _authLinkGeneric = (String) jsonObject.get("auth_link_generic");
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

  public void setAuthLinkGeneric(String authLinkGeneric)
  {
    String oldAuthLinkGeneric = _authLinkGeneric;
    this._authLinkGeneric = authLinkGeneric;
    propertyChangeSupport.firePropertyChange("AuthLinkGeneric", oldAuthLinkGeneric, authLinkGeneric);
  }

  public String getAuthLinkGeneric()
  {
    return (_authLinkGeneric);
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
    jsonObject.put("auth_link_generic", _authLinkGeneric);
    jsonObject.put("service_icon_uri", _serviceIconURI);
    return (jsonObject);
  }
}
