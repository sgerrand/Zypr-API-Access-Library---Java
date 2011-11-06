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

public class ShopItemInCartVO
  extends GenericVO
{
  private String _service;
  private String _serviceItemID;
  private ShopItemVO _shopItem;
  private int _quantity;
  private ShopPriceVO _subtotal;

  public ShopItemInCartVO()
  {
    super();
  }

  public ShopItemInCartVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _service = (String) jsonObject.get("service");
        _serviceItemID = (String) jsonObject.get("service_item_id");
        _quantity = ((Number) jsonObject.get("quantity")).intValue();
        _shopItem = new ShopItemVO((JSONObject) jsonObject.get("item"));
        _subtotal = new ShopPriceVO((JSONObject) jsonObject.get("subtotal"));
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

  public void setService(String service)
  {
    String oldService = _service;
    this._service = service;
    propertyChangeSupport.firePropertyChange("Service", oldService, service);
  }

  public String getService()
  {
    return (_service);
  }

  public void setServiceItemID(String serviceItemID)
  {
    String oldServiceItemID = _serviceItemID;
    this._serviceItemID = serviceItemID;
    propertyChangeSupport.firePropertyChange("ServiceItemID", oldServiceItemID, serviceItemID);
  }

  public String getServiceItemID()
  {
    return (_serviceItemID);
  }

  public void setShopItem(ShopItemVO shopItem)
  {
    ShopItemVO oldShopItem = _shopItem;
    this._shopItem = shopItem;
    propertyChangeSupport.firePropertyChange("ShopItem", oldShopItem, shopItem);
  }

  public ShopItemVO getShopItem()
  {
    return (_shopItem);
  }

  public void setQuantity(int quantity)
  {
    int oldQuantity = _quantity;
    this._quantity = quantity;
    propertyChangeSupport.firePropertyChange("Quantity", oldQuantity, quantity);
  }

  public int getQuantity()
  {
    return (_quantity);
  }

  public void setSubtotal(ShopPriceVO subtotal)
  {
    ShopPriceVO oldSubtotal = _subtotal;
    this._subtotal = subtotal;
    propertyChangeSupport.firePropertyChange("Subtotal", oldSubtotal, subtotal);
  }

  public ShopPriceVO getSubtotal()
  {
    return (_subtotal);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("_service", _service);
    jsonObject.put("_serviceItemID", _serviceItemID);
    jsonObject.put("item", _shopItem.toJSON());
    jsonObject.put("quantity", _quantity);
    jsonObject.put("subtotal", _subtotal.toJSON());
    return (jsonObject);
  }
}
