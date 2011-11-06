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

import net.zypr.api.enums.ImageType;
import net.zypr.api.utils.Debug;

import org.json.simple.JSONObject;

public class ImageVO
  extends GenericVO
{
  private ImageType _type = ImageType.UNKNOWN;
  private String _title;
  private String _uri;
  private int _height;
  private int _width;

  public ImageVO()
  {
    super();
  }

  public ImageVO(JSONObject jsonObject)
  {
    try
      {
        _type = ImageType.valueOf(((String) jsonObject.get("type")).toUpperCase());
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _title = (String) jsonObject.get("title");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _uri = (String) jsonObject.get("uri");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception, 1);
      }
    try
      {
        _height = Integer.parseInt("" + jsonObject.get("height"));
      }
    catch (Exception exception)
      {
        // Ignore
      }
    try
      {
        _width = Integer.parseInt("" + jsonObject.get("width"));
      }
    catch (Exception exception)
      {
        // Ignore
      }
  }

  public void setType(ImageType type)
  {
    ImageType oldType = _type;
    this._type = type;
    propertyChangeSupport.firePropertyChange("Type", oldType, type);
  }

  public ImageType getType()
  {
    return (_type);
  }

  public void setTitle(String title)
  {
    String oldTitle = _title;
    this._title = title;
    propertyChangeSupport.firePropertyChange("Title", oldTitle, title);
  }

  public String getTitle()
  {
    return (_title);
  }

  public void setUri(String uri)
  {
    String oldUri = _uri;
    this._uri = uri;
    propertyChangeSupport.firePropertyChange("Uri", oldUri, uri);
  }

  public String getUri()
  {
    return (_uri);
  }

  public void setHeight(int height)
  {
    int oldHeight = _height;
    this._height = height;
    propertyChangeSupport.firePropertyChange("Height", oldHeight, height);
  }

  public int getHeight()
  {
    return (_height);
  }

  public void setWidth(int width)
  {
    int oldWidth = _width;
    this._width = width;
    propertyChangeSupport.firePropertyChange("Width", oldWidth, width);
  }

  public int getWidth()
  {
    return (_width);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", _type.name());
    jsonObject.put("title", _title);
    jsonObject.put("uri", _uri);
    jsonObject.put("height", _height);
    jsonObject.put("width", _width);
    return (jsonObject);
  }

  @Override
  public ImageVO clone()
    throws CloneNotSupportedException
  {
    return ((ImageVO) super.clone());
  }
}
