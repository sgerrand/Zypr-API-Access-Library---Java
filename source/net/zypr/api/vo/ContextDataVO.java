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

import org.json.simple.JSONObject;

public class ContextDataVO
  extends GenericVO
{
  private String _name;
  private String _id;

  public ContextDataVO()
  {
    super();
  }

  public ContextDataVO(String name, String id)
  {
    super();
    _name = name;
    _id = id;
  }

  public void setName(String name)
  {
    String oldName = _name;
    this._name = name;
    propertyChangeSupport.firePropertyChange("Name", oldName, name);
  }

  public String getName()
  {
    return (_name);
  }

  public void setId(String id)
  {
    String oldId = _id;
    this._id = id;
    propertyChangeSupport.firePropertyChange("Id", oldId, id);
  }

  public String getId()
  {
    return (_id);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", _name);
    jsonObject.put("id", _id);
    return (jsonObject);
  }
}
