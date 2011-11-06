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
import net.zypr.api.enums.ItemType;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIInvalidServiceException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.vo.InfoContactTypeVO;
import net.zypr.api.vo.InfoUserTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.ServiceVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class User
  extends Protocol
{
  public User()
    throws APICommunicationException, APIProtocolException
  {
    super(true);
  }

  public User(ServiceVO[] services)
  {
    super(services);
  }

  public String getDefaultServiceForInfoGet()
  {
    return (getDefaultService(APIVerbs.USER_INFO_GET));
  }
  // This is a work-around function.

  public InfoContactTypeVO infoGetForCurrentUser(String userID, String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForInfoGet();
    if (!doesVerbExist(service, APIVerbs.USER_INFO_GET))
      throw new APIInvalidServiceException(APIVerbs.USER_INFO_GET, service);
    Hashtable<String, String> parameters = buildParameters();
    if (userID != null && !userID.trim().equals(""))
      parameters.put("userid", userID);
    if (service != null && !service.trim().equals(""))
      parameters.put("service", service);
    InfoContactTypeVO contactInfoVO = new InfoContactTypeVO();
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.USER_INFO_GET, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          contactInfoVO = new InfoContactTypeVO(getDataKeyedObjectJSON(jsonObject, "info"));
        else
          throw new APIServerErrorException(getStatusMessage(jsonObject));
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    catch (NullPointerException nullPointerException)
      {
        throw new APIProtocolException(nullPointerException);
      }
    return (contactInfoVO);
  }

  public InfoUserTypeVO infoGetForCurrentUser(String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (infoGet(null, service));
  }

  public InfoUserTypeVO infoGet()
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (infoGet(null, null));
  }

  public InfoUserTypeVO infoGet(String userID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (infoGet(userID, null));
  }

  public InfoUserTypeVO infoGet(String userID, String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForInfoGet();
    if (!doesVerbExist(service, APIVerbs.USER_INFO_GET))
      throw new APIInvalidServiceException(APIVerbs.USER_INFO_GET, service);
    Hashtable<String, String> parameters = buildParameters();
    if (userID != null && !userID.trim().equals(""))
      parameters.put("userid", userID);
    if (service != null && !service.trim().equals(""))
      parameters.put("service", service);
    InfoUserTypeVO userInfoVO = new InfoUserTypeVO();
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.USER_INFO_GET, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          userInfoVO = new InfoUserTypeVO(getDataKeyedObjectJSON(jsonObject, "info"));
        else
          throw new APIServerErrorException(getStatusMessage(jsonObject));
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    catch (NullPointerException nullPointerException)
      {
        throw new APIProtocolException(nullPointerException);
      }
    return (userInfoVO);
  }

  public void infoSet(InfoUserTypeVO currentInfoUserTypeVO, InfoUserTypeVO newInfoUserTypeVO)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    JSONObject jsonDifference = currentInfoUserTypeVO.diffAsJSON(newInfoUserTypeVO);
    if (!((JSONObject) jsonDifference.get("info")).isEmpty())
      {
        parameters.put("parameters", jsonDifference.toJSONString());
        try
          {
            JSONObject jsonObject = getJSON(APIVerbs.USER_INFO_SET, parameters);
            if (getStatusCode(jsonObject) != StatusCode.SUCCESSFUL)
              throw new APIServerErrorException(getStatusMessage(jsonObject));
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
  }

  public void favoriteSet(ItemVO favorite)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("favorite", favorite.getRawJSONString() == null ? favorite.toJSONString() : favorite.getRawJSONString());
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.USER_FAVORITE_SET, parameters);
        if (getStatusCode(jsonObject) != StatusCode.SUCCESSFUL)
          throw new APIServerErrorException(getStatusMessage(jsonObject));
        else
          Session.getInstance().addToFavorites(favorite);
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

  public void favoriteDelete(String globalItemID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("global_item_id", globalItemID);
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.USER_FAVORITE_DELETE, parameters);
        if (getStatusCode(jsonObject) != StatusCode.SUCCESSFUL)
          throw new APIServerErrorException(getStatusMessage(jsonObject));
        else
          Session.getInstance().removeFromFavorites(globalItemID);
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

  public void favoriteList(ItemType itemType)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    if (itemType != null)
      parameters.put("type", itemType.name());
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.USER_FAVORITE_LIST, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonAction = getActionArrayJSON(jsonObject);
            for (int index = 0; index < jsonAction.size(); index++)
              Session.getInstance().appendToFavorites(new ItemVO((JSONObject) jsonAction.get(index)));
          }
        else
          {
            throw new APIServerErrorException(getStatusMessage(jsonObject));
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

  public ItemVO favoriteGet(String globalItemID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("global_item_id", globalItemID);
    ItemVO itemVO = null;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.USER_FAVORITE_LIST, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonAction = getActionArrayJSON(jsonObject);
            itemVO = new ItemVO((JSONObject) jsonAction.get(0));
          }
        else
          {
            throw new APIServerErrorException(getStatusMessage(jsonObject));
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
    return (itemVO);
  }
}
