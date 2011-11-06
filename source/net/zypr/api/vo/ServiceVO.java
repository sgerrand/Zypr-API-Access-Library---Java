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

import java.util.Enumeration;
import java.util.Hashtable;

import net.zypr.api.exceptions.APIProtocolException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ServiceVO
  extends GenericVO
{
  private ServiceDetailsVO _serviceDetails = null;
  private Hashtable<String, VerbParameterVO[]> _verbParameters = new Hashtable<String, VerbParameterVO[]>();

  public ServiceVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    _serviceDetails = new ServiceDetailsVO(jsonObject);
    try
      {
        JSONObject verbsJSONObject = (JSONObject) jsonObject.get("verbs");
        String[] verbs = (String[]) verbsJSONObject.keySet().toArray(new String[0]);
        for (int index = 0; index < verbs.length; index++)
          {
            JSONArray jsonArrayVerb = (JSONArray) verbsJSONObject.get(verbs[index]);
            VerbParameterVO[] verbParameters = new VerbParameterVO[jsonArrayVerb.size()];
            for (int paramIndex = 0; paramIndex < verbParameters.length; paramIndex++)
              verbParameters[paramIndex] = new VerbParameterVO((JSONObject) jsonArrayVerb.get(paramIndex));
            _verbParameters.put(verbs[index], verbParameters);
          }
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

  public void setServiceDetails(ServiceDetailsVO serviceDetails)
  {
    ServiceDetailsVO oldServiceDetails = _serviceDetails;
    _serviceDetails = serviceDetails;
    propertyChangeSupport.firePropertyChange("ServiceName", oldServiceDetails, serviceDetails);
  }

  public ServiceDetailsVO getServiceDetails()
  {
    return (_serviceDetails);
  }

  public void setServiceName(String serviceName)
  {
    String oldServiceName = _serviceDetails.getServiceName();
    _serviceDetails.setServiceName(serviceName);
    propertyChangeSupport.firePropertyChange("ServiceName", oldServiceName, serviceName);
  }

  public String getServiceName()
  {
    return (_serviceDetails.getServiceName());
  }

  public void setName(String name)
  {
    String oldName = _serviceDetails.getName();
    _serviceDetails.setName(name);
    propertyChangeSupport.firePropertyChange("Name", oldName, name);
  }

  public String getName()
  {
    return (_serviceDetails.getName());
  }

  public void setDescription(String description)
  {
    String oldDescription = _serviceDetails.getDescription();
    _serviceDetails.setDescription(description);
    propertyChangeSupport.firePropertyChange("Description", oldDescription, description);
  }

  public String getDescription()
  {
    return (_serviceDetails.getDescription());
  }

  public void setAttribution(String _attribution)
  {
    String oldAttribution = _serviceDetails.getAttribution();
    _serviceDetails.setAttribution(_attribution);
    propertyChangeSupport.firePropertyChange("Attribution", oldAttribution, _attribution);
  }

  public String getAttribution()
  {
    return (_serviceDetails.getAttribution());
  }

  public void setServiceIconURI(String _serviceIconURI)
  {
    String oldServiceIconURI = _serviceDetails.getServiceIconURI();
    _serviceDetails.setServiceIconURI(_serviceIconURI);
    propertyChangeSupport.firePropertyChange("ServiceIconURI", oldServiceIconURI, _serviceIconURI);
  }

  public String getServiceIconURI()
  {
    return (_serviceDetails.getServiceIconURI());
  }

  @Deprecated
  public void setVerbs(String[] verbs)
  {
  }

  public String[] getVerbs()
  {
    return (_verbParameters.keySet().toArray(new String[]
        { }));
  }

  public VerbParameterVO[] getVerbParameters(String verb)
  {
    return (_verbParameters.get(verb));
  }

  public VerbParameterVO getVerbParameter(String verb, String parameter)
  {
    VerbParameterVO[] verbParameters = _verbParameters.get(verb);
    if (verbParameters == null)
      return (null);
    for (int index = 0; index < verbParameters.length; index++)
      if (verbParameters[index].getName().equals(parameter))
        return (verbParameters[index]);
    return (null);
  }

  public boolean doesVerbExist(String verb)
  {
    return (_verbParameters.get(verb) != null);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = _serviceDetails.toJSON();
    JSONObject jsonObjectVerbs = new JSONObject();
    for (Enumeration verbKeys = _verbParameters.keys(); verbKeys.hasMoreElements(); )
      {
        String verb = verbKeys.nextElement().toString();
        VerbParameterVO[] verbParams = _verbParameters.get(verb);
        JSONArray jsonArray = new JSONArray();
        for (int index = 0; index < verbParams.length; index++)
          jsonArray.add(verbParams[index].toJSON());
        jsonObjectVerbs.put(verb, jsonArray);
      }
    jsonObject.put("verbs", jsonObjectVerbs);
    return (jsonObject);
  }
}
