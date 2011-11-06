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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ServiceAuthStatusVO
  extends GenericVO
{
  private ServiceAuthVO[] _serviceAuths;
  private ServiceStatusVO[] _serviceStatuses;

  public ServiceAuthStatusVO()
  {
    super();
  }

  public ServiceAuthStatusVO(JSONArray jsonAuthArray, JSONArray jsonStatusArray)
    throws APIProtocolException
  {
    super();
    _serviceAuths = new ServiceAuthVO[jsonAuthArray.size()];
    for (int index = 0; index < _serviceAuths.length; index++)
      _serviceAuths[index] = new ServiceAuthVO((JSONObject) jsonAuthArray.get(index));
    _serviceStatuses = new ServiceStatusVO[jsonStatusArray.size()];
    for (int index = 0; index < _serviceStatuses.length; index++)
      _serviceStatuses[index] = new ServiceStatusVO((JSONObject) jsonStatusArray.get(index));
  }

  public void setServiceAuths(ServiceAuthVO[] serviceAuths)
  {
    ServiceAuthVO[] oldServiceAuths = _serviceAuths;
    this._serviceAuths = serviceAuths;
    propertyChangeSupport.firePropertyChange("ServiceAuths", oldServiceAuths, serviceAuths);
  }

  public ServiceAuthVO[] getServiceAuths()
  {
    return (_serviceAuths);
  }

  public void setServiceStatuses(ServiceStatusVO[] serviceStatuses)
  {
    ServiceStatusVO[] oldServiceStatuses = _serviceStatuses;
    this._serviceStatuses = serviceStatuses;
    propertyChangeSupport.firePropertyChange("ServiceStatuses", oldServiceStatuses, serviceStatuses);
  }

  public ServiceStatusVO[] getServiceStatuses()
  {
    return (_serviceStatuses);
  }

  public ServiceStatusVO getServiceStatuses(String serviceName)
  {
    for (int index = 0; index < _serviceStatuses.length; index++)
      if (_serviceStatuses[index].getServiceName().equals(serviceName))
        return (_serviceStatuses[index]);
    return (null);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArrayServiceAuths = new JSONArray();
    for (int index = 0; index < _serviceAuths.length; index++)
      jsonArrayServiceAuths.add(_serviceAuths[index].toJSON());
    jsonObject.put("service_auth", jsonArrayServiceAuths);
    JSONArray jsonArrayServiceStatuses = new JSONArray();
    for (int index = 0; index < _serviceStatuses.length; index++)
      jsonArrayServiceStatuses.add(_serviceStatuses[index].toJSON());
    jsonObject.put("service_status", jsonArrayServiceStatuses);
    return (jsonObject);
  }
}
