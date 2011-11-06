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

public class ShopPriceVO
  extends GenericVO
{
  private double _amount;
  private String _currency;
  private String _formattedAmount;

  public ShopPriceVO()
  {
    super();
  }

  public ShopPriceVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _amount = Double.parseDouble("" + jsonObject.get("amount"));
        _currency = (String) jsonObject.get("currency");
        _formattedAmount = (String) jsonObject.get("formatted_amount");
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

  public void setAmount(double _amount)
  {
    double oldAmount = _amount;
    this._amount = _amount;
    propertyChangeSupport.firePropertyChange("Amount", oldAmount, _amount);
  }

  public double getAmount()
  {
    return (_amount);
  }

  public void setCurrency(String currency)
  {
    String oldCurrency = _currency;
    this._currency = currency;
    propertyChangeSupport.firePropertyChange("Currency", oldCurrency, currency);
  }

  public String getCurrency()
  {
    return (_currency);
  }

  public void setFormattedAmount(String formattedAmount)
  {
    String oldFormattedAmount = _formattedAmount;
    this._formattedAmount = formattedAmount;
    propertyChangeSupport.firePropertyChange("FormattedAmount", oldFormattedAmount, formattedAmount);
  }

  public String getFormattedAmount()
  {
    return (_formattedAmount);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("amount", _amount);
    jsonObject.put("currency", _currency);
    jsonObject.put("formatted_amount", _formattedAmount);
    return (jsonObject);
  }
}
