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

import java.text.ParseException;

import java.util.Date;

import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.DateUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ShopCartVO
  extends GenericVO
{
  private String _service;
  private String _serviceItemID;
  private Date _expiresAt;
  private String _purchaseURL;
  private ShopPriceVO _subtotal;
  private ShopItemInCartVO[] _itemsInCart;

  public ShopCartVO()
  {
    super();
  }

  public ShopCartVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _service = (String) jsonObject.get("service");
        _serviceItemID = (String) jsonObject.get("service_item_id");
        _expiresAt = DateUtils.parseISO8601String((String) jsonObject.get("expires"));
        _purchaseURL = (String) jsonObject.get("purchase_url");
        _subtotal = new ShopPriceVO((JSONObject) jsonObject.get("subtotal"));
        JSONArray jsonArray = (JSONArray) jsonObject.get("items");
        _itemsInCart = new ShopItemInCartVO[jsonArray.size()];
        for (int index = 0; index < _itemsInCart.length; index++)
          _itemsInCart[index] = new ShopItemInCartVO((JSONObject) jsonArray.get(index));
      }
    catch (ParseException parseException)
      {
        throw new APIProtocolException(parseException);
      }
    catch (IllegalArgumentException illegalArgumentException)
      {
        throw new APIProtocolException(illegalArgumentException);
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

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("service", _service);
    jsonObject.put("service_item_id", _serviceItemID);
    jsonObject.put("expires", DateUtils.getISO8601String(_expiresAt));
    jsonObject.put("purchase_url", _purchaseURL);
    jsonObject.put("subtotal", _subtotal.toJSON());
    JSONArray jsonArray = new JSONArray();
    for (int index = 0; index < _itemsInCart.length; index++)
      jsonArray.add(_itemsInCart[index].toJSON());
    jsonObject.put("items", jsonArray);
    return (jsonObject);
  }
}
