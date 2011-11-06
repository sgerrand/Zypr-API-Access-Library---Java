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
import net.zypr.api.utils.Debug;
import net.zypr.api.vo.GeoBoundsItemsVO;
import net.zypr.api.vo.GeoBoundsVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.ServiceVO;
import net.zypr.api.vo.ShopItemVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Media
  extends Protocol
{
  public Media()
    throws APICommunicationException, APIProtocolException
  {
    super(true);
  }

  public Media(ServiceVO[] services)
  {
    super(services);
  }

  public String getDefaultServiceForList()
  {
    return (getDefaultService(APIVerbs.MEDIA_LIST));
  }

  public ItemVO[] list()
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (list(Session.getInstance().getMediaSource(), null));
  }

  public ItemVO[] list(String category)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (list(Session.getInstance().getMediaSource(), category));
  }

  public ItemVO[] list(String service, String category)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null)
      service = SERVICE_NAME_ALL;
    else if (service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForSearch();
    if (!doesVerbExist(service, APIVerbs.MEDIA_LIST) && !service.equalsIgnoreCase(SERVICE_NAME_ALL))
      throw new APIInvalidServiceException(APIVerbs.MEDIA_LIST, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    if (category != null)
      parameters.put("category", category);
    ItemVO[] itemVOs = new ItemVO[0];
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.MEDIA_LIST, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonData = getActionArrayJSON(jsonObject);
            itemVOs = new ItemVO[jsonData.size()];
            for (int index = 0; index < itemVOs.length; index++)
              itemVOs[index] = new ItemVO((JSONObject) jsonData.get(index));
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
    return (itemVOs);
  }

  public String getDefaultServiceForSearch()
  {
    return (getDefaultService(APIVerbs.MEDIA_SEARCH));
  }

  public GeoBoundsItemsVO search(String term, String service, GeoBoundsVO geoBounds, int limit, int offset)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (search(term, service, geoBounds, null, limit, offset));
  }

  public GeoBoundsItemsVO search(String term, String service, String placename, int limit, int offset)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (search(term, service, null, placename, limit, offset));
  }

  public GeoBoundsItemsVO search(String term, String service, int limit, int offset)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (search(term, service, null, null, limit, offset));
  }

  public GeoBoundsItemsVO search(String term, String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (search(term, service, null, null, 0, 0));
  }

  public GeoBoundsItemsVO search(String term)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (search(term, Session.getInstance().getMediaSource(), null, null, 0, 0));
  }

  private GeoBoundsItemsVO search(String term, String service, GeoBoundsVO geoBounds, String placename, int limit, int offset)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null)
      service = SERVICE_NAME_ALL;
    else if (service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForSearch();
    if (!doesVerbExist(service, APIVerbs.MEDIA_SEARCH) && !service.equalsIgnoreCase(SERVICE_NAME_ALL))
      throw new APIInvalidServiceException(APIVerbs.MEDIA_SEARCH, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("search", term);
    parameters.put("service", service);
    if (geoBounds != null)
      parameters.put("bounds", geoBounds.toJSONString());
    if (placename != null)
      parameters.put("placename", placename);
    if (limit > 0)
      parameters.put("limit", "" + limit);
    else
      parameters.put("limit", "10");
    if (limit > 0)
      parameters.put("offset", "" + offset);
    GeoBoundsItemsVO geoBoundsItemsVO;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.MEDIA_SEARCH, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonAction = getActionArrayJSON(jsonObject);
            ItemVO[] itemVOs = new ItemVO[jsonAction.size()];
            for (int index = 0; index < itemVOs.length; index++)
              itemVOs[index] = new ItemVO((JSONObject) jsonAction.get(index));
            JSONObject jsonGeographyObject = getDataKeyedObjectJSON(jsonObject, "geography");
            GeoBoundsVO itemsGeoBounds = null;
            if (jsonGeographyObject != null)
              itemsGeoBounds = new GeoBoundsVO((JSONObject) jsonGeographyObject.get("bbox"));
            geoBoundsItemsVO = new GeoBoundsItemsVO(itemsGeoBounds, itemVOs);
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
    return (geoBoundsItemsVO);
  }

  public byte[] getAlbumArt(String artist, String title)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    byte[] imageBytes = new byte[0];
    ShopItemVO[] _shopItemVOs = API.getInstance().getShop().search(artist, title);
    String imageURI = null;
    for (int index = 0; index < _shopItemVOs.length && imageURI == null; index++)
      imageURI = _shopItemVOs[index].getAImageURI();
    try
      {
        imageBytes = doGetBytes(imageURI);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    return (imageBytes);
  }
}
