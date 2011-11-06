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

public class ServiceDetailsVO
  extends GenericVO
{
  private String _serviceName = "";
  private String _name = "";
  private String _description = "";
  private String _attribution = "";
  private String _serviceIconURI = "";
  private Object _serviceIconObject = null;

  public ServiceDetailsVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _serviceName = (String) jsonObject.get("service_name");
        _name = (String) jsonObject.get("name");
        _description = (String) jsonObject.get("description");
        _attribution = (String) jsonObject.get("attribution");
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
    _serviceName = serviceName;
    propertyChangeSupport.firePropertyChange("ServiceName", oldServiceName, serviceName);
  }

  public String getServiceName()
  {
    return (_serviceName);
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

  public void setDescription(String description)
  {
    String oldDescription = _description;
    _description = description;
    propertyChangeSupport.firePropertyChange("Description", oldDescription, description);
  }

  public String getDescription()
  {
    return (_description);
  }

  public void setAttribution(String _attribution)
  {
    String oldAttribution = this._attribution;
    this._attribution = _attribution;
    propertyChangeSupport.firePropertyChange("Attribution", oldAttribution, _attribution);
  }

  public String getAttribution()
  {
    return (_attribution);
  }

  public void setServiceIconURI(String _serviceIconURI)
  {
    String oldServiceIconURI = _serviceIconURI;
    this._serviceIconURI = _serviceIconURI;
    propertyChangeSupport.firePropertyChange("ServiceIconURI", oldServiceIconURI, _serviceIconURI);
  }

  public String getServiceIconURI()
  {
    return _serviceIconURI;
  }

  @Deprecated
  public void setVerbs(String[] verbs)
  {
  }

  public void setServiceIconObject(Object _serviceIconObject)
  {
    Object oldServiceIconObject = this._serviceIconObject;
    this._serviceIconObject = _serviceIconObject;
    propertyChangeSupport.firePropertyChange("ServiceIconObject", oldServiceIconObject, _serviceIconObject);
  }

  public Object getServiceIconObject()
  {
    return (_serviceIconObject);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("service_name", _serviceName);
    jsonObject.put("name", _name);
    jsonObject.put("description", _description);
    jsonObject.put("service_icon_uri", _serviceIconURI);
    return (jsonObject);
  }
}
