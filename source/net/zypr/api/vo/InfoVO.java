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

public abstract class InfoVO
  extends GenericVO
{

  public static InfoVO getObject(JSONObject jsonObject)
    throws APIProtocolException
  {
    InfoVO infoVO = null;
    if (jsonObject != null && jsonObject.get("type") != null)
      try
        {
          String type = (String) jsonObject.get("type");
          if (type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("video") || type.equalsIgnoreCase("photo") || type.equalsIgnoreCase("media"))
            infoVO = new MediaInfoVO(jsonObject);
          else if (type.equalsIgnoreCase("user"))
            infoVO = new InfoUserTypeVO(jsonObject);
          else if (type.equalsIgnoreCase("contact"))
            infoVO = new InfoContactTypeVO(jsonObject);
          else if (type.equalsIgnoreCase("message"))
            infoVO = new InfoMessageGetTypeVO(jsonObject);
          else if (type.equalsIgnoreCase("poi"))
            infoVO = new InfoPOITypeVO(jsonObject);
          else if (type.equalsIgnoreCase("calendar"))
            infoVO = new InfoCalendarTypeVO(jsonObject);
          else if (type.equalsIgnoreCase("calendar_item"))
            infoVO = new InfoCalendarItemTypeVO(jsonObject);
          else
            throw new APIProtocolException("Unsuppored Info Type : " + type);
        }
      catch (ClassCastException classCastException)
        {
          throw new APIProtocolException(classCastException);
        }
      catch (NullPointerException nullPointerException)
        {
          throw new APIProtocolException(nullPointerException);
        }
    return (infoVO);
  }
}
