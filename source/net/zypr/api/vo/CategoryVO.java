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

public class CategoryVO
  extends GenericVO
{
  private String _name;
  private String _type;
  private String _search;

  public CategoryVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _name = (String) jsonObject.get("name");
        _type = (String) jsonObject.get("type");
        _search = (String) jsonObject.get("search");
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

  public void setType(String type)
  {
    String oldType = _type;
    this._type = type;
    propertyChangeSupport.firePropertyChange("Type", oldType, type);
  }

  public String getType()
  {
    return (_type);
  }

  public void setSearch(String search)
  {
    String oldSearch = _search;
    this._search = search;
    propertyChangeSupport.firePropertyChange("Search", oldSearch, search);
  }

  public String getSearch()
  {
    return (_search);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    if (_name != null)
      jsonObject.put("name", _name);
    if (_type != null)
      jsonObject.put("type", _type);
    if (_search != null)
      jsonObject.put("search", _search);
    return (jsonObject);
  }
}
