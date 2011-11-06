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

import java.util.Date;

import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.DateUtils;
import net.zypr.api.utils.Debug;

import org.json.simple.JSONObject;

public class ReviewVO
  extends GenericVO
{
  private String _service;
  private String _itemID;
  private String _reviewID;
  private double _rating;
  private double _maxRating;
  private double _minRating;
  private int _votes;
  private int _helpfulVotes;
  private String _ratingImageURI;
  private Date _date;
  private String _reviewerName;
  private String _description;
  private String _reviewerIconURI;
  private String _sourceURI;
  private String _imageURI;

  public ReviewVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        _service = (String) jsonObject.get("service");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _itemID = (String) jsonObject.get("service_item_id");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _reviewID = (String) jsonObject.get("service_review_id");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _rating = Double.parseDouble("" + jsonObject.get("rating"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _maxRating = Double.parseDouble("" + jsonObject.get("max_rating"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _minRating = Double.parseDouble("" + jsonObject.get("min_rating"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _votes = Integer.parseInt("" + jsonObject.get("votes"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _helpfulVotes = Integer.parseInt("" + jsonObject.get("helpful_votes"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _ratingImageURI = (String) jsonObject.get("rating_img_url");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _date = DateUtils.parseISO8601String((String) jsonObject.get("date"));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _reviewerName = (String) jsonObject.get("reviewer_name");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _description = (String) jsonObject.get("description");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _reviewerIconURI = (String) jsonObject.get("reviewer_icon_uri");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _sourceURI = (String) jsonObject.get("review_source_uri");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
    try
      {
        _imageURI = (String) jsonObject.get("review_image_uri");
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
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

  public void setItemID(String itemID)
  {
    String oldItemID = _itemID;
    this._itemID = itemID;
    propertyChangeSupport.firePropertyChange("ItemID", oldItemID, itemID);
  }

  public String getItemID()
  {
    return (_itemID);
  }

  public void setReviewID(String reviewID)
  {
    String oldReviewID = _reviewID;
    this._reviewID = reviewID;
    propertyChangeSupport.firePropertyChange("ReviewID", oldReviewID, reviewID);
  }

  public String getReviewID()
  {
    return (_reviewID);
  }

  public void setRating(double rating)
  {
    double oldRating = _rating;
    this._rating = rating;
    propertyChangeSupport.firePropertyChange("Rating", oldRating, rating);
  }

  public double getRating()
  {
    return (_rating);
  }

  public void setMaxRating(double maxRating)
  {
    double oldMaxRating = _maxRating;
    this._maxRating = maxRating;
    propertyChangeSupport.firePropertyChange("MaxRating", oldMaxRating, maxRating);
  }

  public double getMaxRating()
  {
    return (_maxRating);
  }

  public void setMinRating(double minRating)
  {
    double oldMinRating = _minRating;
    this._minRating = minRating;
    propertyChangeSupport.firePropertyChange("MinRating", oldMinRating, minRating);
  }

  public double getMinRating()
  {
    return (_minRating);
  }

  public void setVotes(int votes)
  {
    int oldVotes = _votes;
    this._votes = votes;
    propertyChangeSupport.firePropertyChange("Votes", oldVotes, votes);
  }

  public int getVotes()
  {
    return (_votes);
  }

  public void setHelpfulVotes(int helpfulVotes)
  {
    int oldHelpfulVotes = _helpfulVotes;
    this._helpfulVotes = helpfulVotes;
    propertyChangeSupport.firePropertyChange("HelpfulVotes", oldHelpfulVotes, helpfulVotes);
  }

  public int getHelpfulVotes()
  {
    return (_helpfulVotes);
  }

  public void setRatingImageURI(String ratingImageURI)
  {
    String oldRatingImageURI = _ratingImageURI;
    this._ratingImageURI = ratingImageURI;
    propertyChangeSupport.firePropertyChange("RatingImageURI", oldRatingImageURI, ratingImageURI);
  }

  public String getRatingImageURI()
  {
    return (_ratingImageURI);
  }

  public void setDate(Date date)
  {
    Date oldDate = _date;
    this._date = date;
    propertyChangeSupport.firePropertyChange("Date", oldDate, date);
  }

  public Date getDate()
  {
    return (_date);
  }

  public String getDateAsString()
  {
    return DateUtils.getISO8601String(_date);
  }

  public void setReviewerName(String reviewerName)
  {
    String oldReviewerName = _reviewerName;
    this._reviewerName = reviewerName;
    propertyChangeSupport.firePropertyChange("ReviewerName", oldReviewerName, reviewerName);
  }

  public String getReviewerName()
  {
    return (_reviewerName);
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

  public void setReviewerIconURI(String reviewerIconURI)
  {
    String oldReviewerIconURI = _reviewerIconURI;
    this._reviewerIconURI = reviewerIconURI;
    propertyChangeSupport.firePropertyChange("ReviewerIconURI", oldReviewerIconURI, reviewerIconURI);
  }

  public String getReviewerIconURI()
  {
    return (_reviewerIconURI);
  }

  public void setSourceURI(String sourceURI)
  {
    String oldSourceURI = _sourceURI;
    this._sourceURI = sourceURI;
    propertyChangeSupport.firePropertyChange("SourceURI", oldSourceURI, sourceURI);
  }

  public String getSourceURI()
  {
    return (_sourceURI);
  }

  public void setImageURI(String imageURI)
  {
    String oldImageURI = _imageURI;
    this._imageURI = imageURI;
    propertyChangeSupport.firePropertyChange("ImageURI", oldImageURI, imageURI);
  }

  public String getImageURI()
  {
    return (_imageURI);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    if (_service != null)
      jsonObject.put("service", _service);
    if (_itemID != null)
      jsonObject.put("service_item_id", _itemID);
    if (_reviewID != null)
      jsonObject.put("service_review_id", _reviewID);
    jsonObject.put("rating", _rating);
    jsonObject.put("max_rating", _maxRating);
    jsonObject.put("min_rating", _minRating);
    jsonObject.put("votes", _votes);
    jsonObject.put("helpful_votes", _helpfulVotes);
    jsonObject.put("rating_img_url", _ratingImageURI);
    if (_date != null)
      jsonObject.put("date", DateUtils.getISO8601String(_date));
    jsonObject.put("reviewer_name", _reviewerName);
    jsonObject.put("description", _description);
    jsonObject.put("reviewer_icon_uri", _reviewerIconURI);
    jsonObject.put("review_source_uri", _sourceURI);
    jsonObject.put("review_image_uri", _imageURI);
    return (jsonObject);
  }
}
