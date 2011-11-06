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

public class VoiceCommandListVO
  extends GenericVO
{
  private VoiceCommandVO[] _commands;
  private ItemVO[] _categories;

  public VoiceCommandListVO(ItemVO[] categories, VoiceCommandVO[] commands)
  {
    super();
    _categories = categories;
    _commands = commands;
  }

  public void setCommands(VoiceCommandVO[] commands)
  {
    VoiceCommandVO[] oldCommands = this._commands;
    this._commands = commands;
    propertyChangeSupport.firePropertyChange("Commands", oldCommands, commands);
  }

  public VoiceCommandVO[] getCommands()
  {
    return _commands;
  }

  public void setCategories(ItemVO[] categories)
  {
    ItemVO[] oldCategories = this._categories;
    this._categories = categories;
    propertyChangeSupport.firePropertyChange("Categories", oldCategories, categories);
  }

  public ItemVO[] getCategories()
  {
    return _categories;
  }

  public JSONObject toJSON()
  {
    return (new JSONObject());
  }
}
