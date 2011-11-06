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
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIInvalidServiceException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.vo.ServiceVO;
import net.zypr.api.vo.ShopCartVO;
import net.zypr.api.vo.ShopItemVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Shop
  extends Protocol
{
  public Shop()
    throws APICommunicationException, APIProtocolException
  {
    super(true);
  }

  public Shop(ServiceVO[] services)
  {
    super(services);
  }

  public String getDefaultServiceForSearch()
  {
    return (getDefaultService(APIVerbs.SHOP_SEARCH));
  }

  public ShopItemVO[] search(String artist, String title)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (search(null, artist, title, null));
  }

  public ShopItemVO[] search(String service, String artist, String title, String category)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null)
      service = SERVICE_NAME_ALL;
    else if (service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForSearch();
    if (!doesVerbExist(service, APIVerbs.SHOP_SEARCH) && !service.equalsIgnoreCase(SERVICE_NAME_ALL))
      throw new APIInvalidServiceException(APIVerbs.SHOP_SEARCH, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    if (category != null)
      parameters.put("category", category);
    JSONObject jsonObjectSearchData = new JSONObject();
    jsonObjectSearchData.put("artist", artist);
    jsonObjectSearchData.put("title", title);
    JSONObject jsonObjectSearch = new JSONObject();
    jsonObjectSearch.put("search", jsonObjectSearchData);
    parameters.put("search", jsonObjectSearch.toJSONString());
    ShopItemVO[] shopItemVOs = new ShopItemVO[0];
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.SHOP_SEARCH, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonArray = getDataArrayJSON(jsonObject);
            shopItemVOs = new ShopItemVO[jsonArray.size()];
            for (int index = 0; index < shopItemVOs.length; index++)
              shopItemVOs[index] = new ShopItemVO((JSONObject) ((JSONObject) jsonArray.get(index)).get("shop_item"));
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
    return (shopItemVOs);
  }

  public String getDefaultServiceForCartCreate()
  {
    return (getDefaultService(APIVerbs.SHOP_CART_CREATE));
  }

  public ShopCartVO cartCreate()
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (cartCreate(null));
  }

  public ShopCartVO cartCreate(String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForCartCreate();
    if (!doesVerbExist(service, APIVerbs.SHOP_CART_CREATE))
      throw new APIInvalidServiceException(APIVerbs.SHOP_CART_CREATE, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    ShopCartVO shopCartVO = new ShopCartVO();
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.SHOP_CART_CREATE, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          shopCartVO = new ShopCartVO(getDataKeyedObjectJSON(jsonObject, "shop_cart"));
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
    return (shopCartVO);
  }

  public String getDefaultServiceForCartAdd()
  {
    return (getDefaultService(APIVerbs.SHOP_CART_ADD));
  }

  public ShopCartVO cartAdd(String cartID, String itemID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (cartAdd(null, cartID, itemID));
  }

  public ShopCartVO cartAdd(String service, String cartID, String itemID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForCartAdd();
    if (!doesVerbExist(service, APIVerbs.SHOP_CART_ADD))
      throw new APIInvalidServiceException(APIVerbs.SHOP_CART_ADD, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    parameters.put("cart_id", cartID);
    parameters.put("item_id", itemID);
    ShopCartVO shopCartVO = new ShopCartVO();
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.SHOP_CART_ADD, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          shopCartVO = new ShopCartVO(getDataKeyedObjectJSON(jsonObject, "shop_cart"));
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
    return (shopCartVO);
  }

  public String getDefaultServiceForCartRemove()
  {
    return (getDefaultService(APIVerbs.SHOP_CART_ADD));
  }

  public ShopCartVO cartRemove(String cartID, String itemID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (cartRemove(null, cartID, itemID));
  }

  public ShopCartVO cartRemove(String service, String cartID, String itemID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForCartRemove();
    if (!doesVerbExist(service, APIVerbs.SHOP_CART_REMOVE))
      throw new APIInvalidServiceException(APIVerbs.SHOP_CART_REMOVE, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    parameters.put("cart_id", cartID);
    parameters.put("item_id", itemID);
    ShopCartVO shopCartVO = new ShopCartVO();
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.SHOP_CART_REMOVE, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          shopCartVO = new ShopCartVO(getDataKeyedObjectJSON(jsonObject, "shop_cart"));
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
    return (shopCartVO);
  }

  public String getDefaultServiceForCartDetails()
  {
    return (getDefaultService(APIVerbs.SHOP_CART_DETAILS));
  }

  public ShopCartVO cartDetails(String cartID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (cartDetails(null, cartID));
  }

  public ShopCartVO cartDetails(String service, String cartID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForCartDetails();
    if (!doesVerbExist(service, APIVerbs.SHOP_CART_DETAILS))
      throw new APIInvalidServiceException(APIVerbs.SHOP_CART_DETAILS, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    parameters.put("cart_id", cartID);
    ShopCartVO shopCartVO = new ShopCartVO();
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.SHOP_CART_DETAILS, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          shopCartVO = new ShopCartVO(getDataKeyedObjectJSON(jsonObject, "shop_cart"));
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
    return (shopCartVO);
  }

  public String getDefaultServiceForItemDetails()
  {
    return (getDefaultService(APIVerbs.SHOP_ITEM_DETAILS));
  }

  public ShopItemVO itemDetails(String itemID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (itemDetails(null, itemID));
  }

  public ShopItemVO itemDetails(String service, String itemID)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForItemDetails();
    if (!doesVerbExist(service, APIVerbs.SHOP_ITEM_DETAILS))
      throw new APIInvalidServiceException(APIVerbs.SHOP_ITEM_DETAILS, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    parameters.put("item_id", itemID);
    ShopItemVO shopItemVO = new ShopItemVO();
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.SHOP_ITEM_DETAILS, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonAction = getDataArrayJSON(jsonObject);
            shopItemVO = new ShopItemVO((JSONObject) jsonAction.get(0));
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
    return (shopItemVO);
  }
}
