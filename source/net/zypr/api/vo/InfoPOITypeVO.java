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
import net.zypr.api.utils.Debug;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class InfoPOITypeVO
  extends InfoVO
{
  private AddressVO _address;
  private CategoryVO[] _categories;
  private ReviewVO[] _reviews;
  private PhoneNumberVO[] _phoneNumbers;

  public InfoPOITypeVO()
  {
    super();
  }

  public InfoPOITypeVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        if (jsonObject.get("address") != null)
          _address = new AddressVO((JSONObject) jsonObject.get("address"));
      }
    catch (APIProtocolException protocolException)
      {
        Debug.displayStack(this, protocolException, 1);
      }
    JSONArray jsonArray = (JSONArray) jsonObject.get("phones");
    if (jsonArray != null)
      {
        _phoneNumbers = new PhoneNumberVO[jsonArray.size()];
        for (int index = 0; index < _phoneNumbers.length; index++)
          _phoneNumbers[index] = new PhoneNumberVO((JSONObject) jsonArray.get(index));
      }
    jsonArray = (JSONArray) jsonObject.get("categories");
    if (jsonArray != null)
      {
        _categories = new CategoryVO[jsonArray.size()];
        for (int index = 0; index < _categories.length; index++)
          try
            {
              _categories[index] = new CategoryVO((JSONObject) jsonArray.get(index));
            }
          catch (APIProtocolException protocolException)
            {
              Debug.displayStack(this, protocolException);
            }
      }
    jsonArray = (JSONArray) jsonObject.get("reviews");
    if (jsonArray != null)
      {
        _reviews = new ReviewVO[jsonArray.size()];
        for (int index = 0; index < _reviews.length; index++)
          try
            {
              _reviews[index] = new ReviewVO((JSONObject) jsonArray.get(index));
            }
          catch (APIProtocolException protocolException)
            {
              Debug.displayStack(this, protocolException, 1);
            }
      }
  }

  public InfoPOITypeVO(AddressVO address, PhoneNumberVO[] phoneNumbers, CategoryVO[] categories, ReviewVO[] reviews)
  {
    super();
    _address = address;
    _phoneNumbers = phoneNumbers;
    _categories = categories;
    _reviews = reviews;
  }

  public void setAddress(AddressVO address)
  {
    AddressVO oldAddress = _address;
    this._address = address;
    propertyChangeSupport.firePropertyChange("Address", oldAddress, address);
  }

  public AddressVO getAddress()
  {
    return (_address);
  }

  public void setCategories(CategoryVO[] categories)
  {
    CategoryVO[] oldCategories = _categories;
    this._categories = categories;
    propertyChangeSupport.firePropertyChange("Categories", oldCategories, categories);
  }

  public CategoryVO[] getCategories()
  {
    return (_categories);
  }

  public void setReviews(ReviewVO[] reviews)
  {
    ReviewVO[] oldReviews = _reviews;
    this._reviews = reviews;
    propertyChangeSupport.firePropertyChange("Reviews", oldReviews, _reviews);
  }

  public ReviewVO[] getReviews()
  {
    return (_reviews);
  }

  public void setPhoneNumbers(PhoneNumberVO[] phoneNumbers)
  {
    PhoneNumberVO[] oldPhoneNumber = _phoneNumbers;
    this._phoneNumbers = phoneNumbers;
    propertyChangeSupport.firePropertyChange("PhoneNumber", oldPhoneNumber, phoneNumbers);
  }

  public PhoneNumberVO[] getPhoneNumbers()
  {
    return (_phoneNumbers);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", "poi");
    if (_address != null)
      jsonObject.put("address", _address.toJSON());
    JSONArray jsonArray = new JSONArray();
    if (_phoneNumbers != null)
      for (int index = 0; index < _phoneNumbers.length; index++)
        jsonArray.add(_phoneNumbers[index].toJSON());
    jsonObject.put("phones", jsonArray);
    jsonArray = new JSONArray();
    if (_categories != null)
      for (int index = 0; index < _categories.length; index++)
        jsonArray.add(_categories[index].toJSON());
    jsonObject.put("categories", jsonArray);
    jsonArray = new JSONArray();
    if (_reviews != null)
      for (int index = 0; index < _reviews.length; index++)
        jsonArray.add(_reviews[index].toJSON());
    jsonObject.put("review", jsonArray);
    return (jsonObject);
  }
}
