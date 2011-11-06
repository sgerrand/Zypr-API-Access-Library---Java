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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ShopItemVO
  extends GenericVO
{
  private String _service;
  private String _serviceItemID;
  private String _sellerName;
  private String _name;
  private String _description;
  private String _category;
  private String _manufacturer;
  private ShopPriceVO _shopPrice;
  private String _uri;
  private ImageVO[] _images;
  private ReviewVO[] _reviews;

  public ShopItemVO()
  {
    super();
  }

  public ShopItemVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _service = (String) jsonObject.get("service");
        _serviceItemID = (String) jsonObject.get("service_item_id");
        _sellerName = (String) jsonObject.get("seller_name");
        _name = (String) jsonObject.get("name");
        _description = (String) jsonObject.get("description");
        _category = (String) jsonObject.get("category");
        _manufacturer = (String) jsonObject.get("manufacturer");
        _shopPrice = new ShopPriceVO((JSONObject) jsonObject.get("shop_price"));
        _uri = (String) jsonObject.get("uri");
        JSONArray jsonArray = (JSONArray) jsonObject.get("images");
        _images = new ImageVO[jsonArray.size()];
        for (int index = 0; index < _images.length; index++)
          _images[index] = new ImageVO((JSONObject) (((JSONObject) jsonArray.get(index)).get("image")));
        jsonArray = (JSONArray) jsonObject.get("reviews");
        _reviews = new ReviewVO[jsonArray.size()];
        for (int index = 0; index < _reviews.length; index++)
          _reviews[index] = new ReviewVO((JSONObject) jsonArray.get(index));
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

  public void setSellerName(String sellerName)
  {
    String oldSellerName = _sellerName;
    this._sellerName = sellerName;
    propertyChangeSupport.firePropertyChange("SellerName", oldSellerName, sellerName);
  }

  public String getSellerName()
  {
    return (_sellerName);
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

  public void setDescription(String description)
  {
    String oldDescription = _description;
    this._description = description;
    propertyChangeSupport.firePropertyChange("Description", oldDescription, description);
  }

  public String getDescription()
  {
    return (_description);
  }

  public void setCategory(String category)
  {
    String oldCategory = _category;
    this._category = category;
    propertyChangeSupport.firePropertyChange("Category", oldCategory, category);
  }

  public String getCategory()
  {
    return (_category);
  }

  public void setManufacturer(String manufacturer)
  {
    String oldManufacturer = _manufacturer;
    this._manufacturer = manufacturer;
    propertyChangeSupport.firePropertyChange("Manufacturer", oldManufacturer, manufacturer);
  }

  public String getManufacturer()
  {
    return (_manufacturer);
  }

  public void setShopPrice(ShopPriceVO shopPrice)
  {
    ShopPriceVO oldShopPrice = _shopPrice;
    this._shopPrice = shopPrice;
    propertyChangeSupport.firePropertyChange("ShopPrice", oldShopPrice, shopPrice);
  }

  public ShopPriceVO getShopPrice()
  {
    return (_shopPrice);
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

  public void setImages(ImageVO[] images)
  {
    ImageVO[] oldImages = _images;
    this._images = images;
    propertyChangeSupport.firePropertyChange("Images", oldImages, images);
  }

  public ImageVO[] getImages()
  {
    return (_images);
  }

  public String getAImageURI()
  {
    String imageURI = null;
    if (_images != null)
      for (int index = 0; index < _images.length && imageURI == null; index++)
        if (_images[index].getUri() != null && !_images[index].getUri().trim().equals(""))
          imageURI = _images[index].getUri();
    return (imageURI);
  }

  public void setReviews(ReviewVO[] reviews)
  {
    ReviewVO[] oldReviews = _reviews;
    this._reviews = reviews;
    propertyChangeSupport.firePropertyChange("Reviews", oldReviews, reviews);
  }

  public ReviewVO[] getReviews()
  {
    return (_reviews);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("service", _service);
    jsonObject.put("service_item_id", _serviceItemID);
    jsonObject.put("seller_name", _sellerName);
    jsonObject.put("name", _name);
    jsonObject.put("description", _description);
    jsonObject.put("category", _category);
    jsonObject.put("manufacturer", _manufacturer);
    jsonObject.put("uri", _uri);
    jsonObject.put("price", _shopPrice.toJSON());
    JSONArray jsonArray = new JSONArray();
    if (_images != null)
      for (int index = 0; index < _images.length; index++)
        {
          JSONObject jsonImage = new JSONObject();
          jsonImage.put("image", _images[index]);
          jsonArray.add(jsonImage);
        }
    jsonObject.put("images", jsonArray);
    jsonArray = new JSONArray();
    if (_reviews != null)
      for (int index = 0; index < _reviews.length; index++)
        jsonArray.add(_reviews[index].toJSON());
    jsonObject.put("review", jsonArray);
    return (jsonObject);
  }
}
