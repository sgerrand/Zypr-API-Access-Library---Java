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

import net.zypr.api.enums.VoiceCommandAction;
import net.zypr.api.enums.VoiceCommandResponse;
import net.zypr.api.enums.VoiceCommandState;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.utils.Debug;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class VoiceCommandVO
  extends GenericVO
{
  private VoiceCommandAction _name;
  private VoiceCommandState _state;
  private String _serverHandler;
  private int _confidence;
  private VoiceCommandResponse _responseCode;
  private VoiceCommandState _followupState;
  private String _followupTTSURI;
  private String _followupText;
  private VoiceCommandVariableVO[] _variables;

  public VoiceCommandVO()
  {
    super();
  }

  public VoiceCommandVO(JSONObject jsonObject)
    throws APIProtocolException
  {
    super();
    try
      {
        try
          {
            _name = VoiceCommandAction.valueOf(((String) jsonObject.get("name")).replaceAll("-", "").replaceAll("__", "_").toUpperCase());
          }
        catch (Exception exception)
          {
            Debug.displayWarning(this, "Unknown command action = " + ((String) jsonObject.get("name")).replaceAll("-", "").replaceAll("__", "_").toUpperCase());
            _name = VoiceCommandAction.UNKNOWN;
            Debug.displayWarning(this, "Command action is set to VoiceCommandAction.UNKNOWN");
          }
        try
          {
            _state = VoiceCommandState.valueOf(((String) jsonObject.get("state")).toUpperCase());
          }
        catch (Exception exception)
          {
            Debug.displayWarning(this, "Unknown command state = " + ((String) jsonObject.get("state")).toUpperCase());
            _state = VoiceCommandState.MAIN;
            Debug.displayWarning(this, "Command state is set to VoiceCommandState.MAIN");
          }
        _serverHandler = (String) jsonObject.get("server_handler");
        _confidence = ((Long) jsonObject.get("confidence")).intValue();
        _responseCode = VoiceCommandResponse.valueOf((String) jsonObject.get("response_code"));
        try
          {
            _followupState = VoiceCommandState.valueOf(((String) jsonObject.get("followup_state")).toUpperCase());
          }
        catch (Exception exception)
          {
            _followupState = VoiceCommandState.MAIN;
          }
        _followupTTSURI = (String) jsonObject.get("followup_TTS_uri");
        _followupText = (String) jsonObject.get("followup_text");
        JSONArray variables = (JSONArray) jsonObject.get("variables");
        _variables = new VoiceCommandVariableVO[variables.size()];
        for (int index = 0; index < _variables.length; index++)
          _variables[index] = new VoiceCommandVariableVO((JSONObject) variables.get(index));
        if (_name == VoiceCommandAction.ITEM_SELECTED && getVariable("list_item_name") != null)
          _name = VoiceCommandAction.ITEM_SELECTED_4_ITEM_NAME;
        else if (_name == VoiceCommandAction.ITEM_SELECTED && getVariable("list_item_number") != null)
          _name = VoiceCommandAction.ITEM_SELECTED_4_ITEM_NUMBER;
        else if (_name == VoiceCommandAction.SHOW_STATIONS_IN_THE_PLACE && getVariable("location") != null && getVariable("location").getValue().toString().equalsIgnoreCase("nearby"))
          _name = VoiceCommandAction.SHOW_STATIONS_IN_THE_PLACE_4_NEARBY;
        else if (_name == VoiceCommandAction.SHOW_STATIONS_IN_THE_PLACE && getVariable("city") != null)
          _name = VoiceCommandAction.SHOW_STATIONS_IN_THE_PLACE_4_IN_A_LOCATION;
        else if (_name == VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION && getVariable("location") != null && (getVariable("location").getValue().toString().equalsIgnoreCase("nearby") || getVariable("location").getValue().toString().equalsIgnoreCase("here")))
          _name = VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION_4_NEARBY;
        else if (_name == VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION && getVariable("location") == null && getVariable("city") == null && getVariable("state") == null)
          _name = VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION_4_NEARBY;
        else if (_name == VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION && getVariable("city") != null && getVariable("state") != null)
          _name = VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION_4_IN_A_LOCATION;
        else if (_name == VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION && getVariable("location") != null && (getVariable("location").getValue().toString().equalsIgnoreCase("near destination") || getVariable("location").getValue().toString().equalsIgnoreCase("destination")))
          _name = VoiceCommandAction.CATEGORY_OR_NATIONAL_BRAND_WITH_ACTION_IN_OPTIONAL_LOCATION_4_NEAR_DESTINATION;
        else if (_name == VoiceCommandAction.PLAY_STATION && getVariable("station_name") == null)
          _name = VoiceCommandAction.PLAY_STATION_4_GLOBAL_STATION_NAME;
        else if (_name == VoiceCommandAction.SAVE_FAVORITE && getVariable("list_item_name") != null)
          _name = VoiceCommandAction.SAVE_FAVORITE_4_ITEM_NAME;
        else if (_name == VoiceCommandAction.SAVE_FAVORITE && getVariable("list_item_number") != null)
          _name = VoiceCommandAction.SAVE_FAVORITE_4_ITEM_NUMBER;
        else if (_name == VoiceCommandAction.DELETE_FAVORITE && getVariable("list_item_name") != null)
          _name = VoiceCommandAction.DELETE_FAVORITE_4_ITEM_NAME;
        else if (_name == VoiceCommandAction.DELETE_FAVORITE && getVariable("list_item_number") != null)
          _name = VoiceCommandAction.DELETE_FAVORITE_4_ITEM_NUMBER;
        else if (_name == VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT && getVariable("list_item_number") == null && getVariable("contact") != null)
          _name = VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT_NAME;
        else if (_name == VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT && getVariable("list_item_number") != null && getVariable("contact") != null)
          _name = VoiceCommandAction.OPEN_MESSAGE_FROM_CONTACT_NUMBER;
        else if (_name == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION && getVariable("location") != null && (getVariable("location").getValue().toString().equalsIgnoreCase("nearby") || getVariable("location").getValue().toString().equalsIgnoreCase("here")))
          _name = VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION_4_NEARBY;
        else if (_name == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION && getVariable("location") == null && getVariable("city") == null && getVariable("state") == null)
          _name = VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION_4_NEARBY;
        else if (_name == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION && getVariable("city") != null && getVariable("state") != null)
          _name = VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION_4_IN_A_LOCATION;
        else if (_name == VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION && getVariable("location") != null && (getVariable("location").getValue().toString().equalsIgnoreCase("near destination") || getVariable("location").getValue().toString().equalsIgnoreCase("destination")))
          _name = VoiceCommandAction.VOICE_MEMO_FOR_CONTACT_AT_LOCATION_4_NEAR_DESTINATION;
        else if (_name == VoiceCommandAction.BUSINESS_SEARCH_INTENT && getVariable("city") != null && getVariable("state") != null && getVariable("location") == null)
          _name = VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_IN_A_LOCATION;
        else if (_name == VoiceCommandAction.BUSINESS_SEARCH_INTENT && getVariable("city") == null && getVariable("state") == null && getVariable("location") != null && getVariable("location").getValue().toString().equalsIgnoreCase("nearby"))
          _name = VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_NEARBY_LOCATION;
        else if (_name == VoiceCommandAction.BUSINESS_SEARCH_INTENT && getVariable("location") != null && (getVariable("location").getValue().toString().equalsIgnoreCase("near destination") || getVariable("location").getValue().toString().equalsIgnoreCase("destination")))
          _name = VoiceCommandAction.BUSINESS_SEARCH_INTENT_4_NEAR_DESTINATION;
        else if (_name == VoiceCommandAction.GET_WEATHER && getVariable("location") != null && (getVariable("location").getValue().toString().equalsIgnoreCase("nearby") || getVariable("location").getValue().toString().equalsIgnoreCase("here")))
          _name = VoiceCommandAction.GET_WEATHER_4_NEARBY;
        else if (_name == VoiceCommandAction.GET_WEATHER && getVariable("location") == null && getVariable("city") == null && getVariable("state") == null)
          _name = VoiceCommandAction.GET_WEATHER_4_NEARBY;
        else if (_name == VoiceCommandAction.GET_WEATHER && getVariable("city") != null && getVariable("state") != null)
          _name = VoiceCommandAction.GET_WEATHER_4_IN_A_LOCATION;
        else if (_name == VoiceCommandAction.GET_WEATHER && getVariable("location") != null && (getVariable("location").getValue().toString().equalsIgnoreCase("near destination") || getVariable("location").getValue().toString().equalsIgnoreCase("destination")))
          _name = VoiceCommandAction.GET_WEATHER_4_NEAR_DESTINATION;
        else if (_name == VoiceCommandAction.GET_FORECAST && getVariable("location") != null && (getVariable("location").getValue().toString().equalsIgnoreCase("nearby") || getVariable("location").getValue().toString().equalsIgnoreCase("here")))
          _name = VoiceCommandAction.GET_FORECAST_4_NEARBY;
        else if (_name == VoiceCommandAction.GET_FORECAST && getVariable("location") == null && getVariable("city") == null && getVariable("state") == null)
          _name = VoiceCommandAction.GET_FORECAST_4_NEARBY;
        else if (_name == VoiceCommandAction.GET_FORECAST && getVariable("city") != null && getVariable("state") != null)
          _name = VoiceCommandAction.GET_FORECAST_4_IN_A_LOCATION;
        else if (_name == VoiceCommandAction.GET_FORECAST && getVariable("location") != null && (getVariable("location").getValue().toString().equalsIgnoreCase("near destination") || getVariable("location").getValue().toString().equalsIgnoreCase("destination")))
          _name = VoiceCommandAction.GET_FORECAST_4_NEAR_DESTINATION;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("in"))
          _name = VoiceCommandAction.MAP_ZOOM_4_IN;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("max"))
          _name = VoiceCommandAction.MAP_ZOOM_4_MAX;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("min"))
          _name = VoiceCommandAction.MAP_ZOOM_4_MIN;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("street"))
          _name = VoiceCommandAction.MAP_ZOOM_4_STREET;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("neighborhood"))
          _name = VoiceCommandAction.MAP_ZOOM_4_NEIGHBORHOOD;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("metro"))
          _name = VoiceCommandAction.MAP_ZOOM_4_METRO;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("county"))
          _name = VoiceCommandAction.MAP_ZOOM_4_COUNTY;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("region"))
          _name = VoiceCommandAction.MAP_ZOOM_4_REGION;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("country"))
          _name = VoiceCommandAction.MAP_ZOOM_4_COUNTRY;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("1"))
          _name = VoiceCommandAction.MAP_ZOOM_4_1;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("2"))
          _name = VoiceCommandAction.MAP_ZOOM_4_2;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("3"))
          _name = VoiceCommandAction.MAP_ZOOM_4_3;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("4"))
          _name = VoiceCommandAction.MAP_ZOOM_4_4;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("5"))
          _name = VoiceCommandAction.MAP_ZOOM_4_5;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("out"))
          _name = VoiceCommandAction.MAP_ZOOM_4_OUT;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("city"))
          _name = VoiceCommandAction.MAP_ZOOM_4_CITY;
        else if (_name == VoiceCommandAction.MAP_ZOOM && getVariable("zoom") != null && getVariable("zoom").getValue().toString().equalsIgnoreCase("state"))
          _name = VoiceCommandAction.MAP_ZOOM_4_STATE;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") == null && getVariable("direction").getValue().toString().equalsIgnoreCase("up"))
          _name = VoiceCommandAction.MAP_PAN_4_UP;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") == null && getVariable("direction").getValue().toString().equalsIgnoreCase("down"))
          _name = VoiceCommandAction.MAP_PAN_4_DOWN;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") == null && getVariable("direction").getValue().toString().equalsIgnoreCase("left"))
          _name = VoiceCommandAction.MAP_PAN_4_LEFT;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") == null && getVariable("direction").getValue().toString().equalsIgnoreCase("right"))
          _name = VoiceCommandAction.MAP_PAN_4_RIGHT;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") == null && getVariable("direction").getValue().toString().equalsIgnoreCase("north"))
          _name = VoiceCommandAction.MAP_PAN_4_NORTH;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") == null && getVariable("direction").getValue().toString().equalsIgnoreCase("south"))
          _name = VoiceCommandAction.MAP_PAN_4_SOUTH;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") == null && getVariable("direction").getValue().toString().equalsIgnoreCase("west"))
          _name = VoiceCommandAction.MAP_PAN_4_WEST;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") == null && getVariable("direction").getValue().toString().equalsIgnoreCase("east"))
          _name = VoiceCommandAction.MAP_PAN_4_EAST;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("up") && getVariable("speed").getValue().toString().equalsIgnoreCase("slow"))
          _name = VoiceCommandAction.MAP_PAN_4_UP_SLOW;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("down") && getVariable("speed").getValue().toString().equalsIgnoreCase("slow"))
          _name = VoiceCommandAction.MAP_PAN_4_DOWN_SLOW;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("left") && getVariable("speed").getValue().toString().equalsIgnoreCase("slow"))
          _name = VoiceCommandAction.MAP_PAN_4_LEFT_SLOW;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("right") && getVariable("speed").getValue().toString().equalsIgnoreCase("slow"))
          _name = VoiceCommandAction.MAP_PAN_4_RIGHT_SLOW;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("north") && getVariable("speed").getValue().toString().equalsIgnoreCase("slow"))
          _name = VoiceCommandAction.MAP_PAN_4_NORTH_SLOW;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("south") && getVariable("speed").getValue().toString().equalsIgnoreCase("slow"))
          _name = VoiceCommandAction.MAP_PAN_4_SOUTH_SLOW;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("west") && getVariable("speed").getValue().toString().equalsIgnoreCase("slow"))
          _name = VoiceCommandAction.MAP_PAN_4_WEST_SLOW;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("east") && getVariable("speed").getValue().toString().equalsIgnoreCase("slow"))
          _name = VoiceCommandAction.MAP_PAN_4_EAST_SLOW;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("up") && getVariable("speed").getValue().toString().equalsIgnoreCase("fast"))
          _name = VoiceCommandAction.MAP_PAN_4_UP_FAST;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("down") && getVariable("speed").getValue().toString().equalsIgnoreCase("fast"))
          _name = VoiceCommandAction.MAP_PAN_4_DOWN_FAST;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("left") && getVariable("speed").getValue().toString().equalsIgnoreCase("fast"))
          _name = VoiceCommandAction.MAP_PAN_4_LEFT_FAST;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("right") && getVariable("speed").getValue().toString().equalsIgnoreCase("fast"))
          _name = VoiceCommandAction.MAP_PAN_4_RIGHT_FAST;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("north") && getVariable("speed").getValue().toString().equalsIgnoreCase("fast"))
          _name = VoiceCommandAction.MAP_PAN_4_NORTH_FAST;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("south") && getVariable("speed").getValue().toString().equalsIgnoreCase("fast"))
          _name = VoiceCommandAction.MAP_PAN_4_SOUTH_FAST;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("west") && getVariable("speed").getValue().toString().equalsIgnoreCase("fast"))
          _name = VoiceCommandAction.MAP_PAN_4_WEST_FAST;
        else if (_name == VoiceCommandAction.MAP_PAN && getVariable("direction") != null && getVariable("speed") != null && getVariable("direction").getValue().toString().equalsIgnoreCase("east") && getVariable("speed").getValue().toString().equalsIgnoreCase("fast"))
          _name = VoiceCommandAction.MAP_PAN_4_EAST_FAST;
      }
    catch (NumberFormatException numberFormatException)
      {
        throw new APIProtocolException(numberFormatException);
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

  public void setName(VoiceCommandAction name)
  {
    VoiceCommandAction oldName = _name;
    this._name = name;
    propertyChangeSupport.firePropertyChange("Name", oldName, name);
  }

  public VoiceCommandAction getName()
  {
    return (_name);
  }

  public void setState(VoiceCommandState state)
  {
    VoiceCommandState oldState = _state;
    this._state = state;
    propertyChangeSupport.firePropertyChange("State", oldState, state);
  }

  public VoiceCommandState getState()
  {
    return (_state);
  }

  public void setServerHandler(String serverHandler)
  {
    String oldServerHandler = _serverHandler;
    this._serverHandler = serverHandler;
    propertyChangeSupport.firePropertyChange("ServerHandler", oldServerHandler, serverHandler);
  }

  public String getServerHandler()
  {
    return (_serverHandler);
  }

  public void setConfidence(int confidence)
  {
    int oldConfidence = _confidence;
    this._confidence = confidence;
    propertyChangeSupport.firePropertyChange("Confidence", oldConfidence, confidence);
  }

  public int getConfidence()
  {
    return (_confidence);
  }

  public void setResponseCode(VoiceCommandResponse responseCode)
  {
    VoiceCommandResponse oldResponseCode = _responseCode;
    this._responseCode = responseCode;
    propertyChangeSupport.firePropertyChange("ResponseCode", oldResponseCode, responseCode);
  }

  public VoiceCommandResponse getResponseCode()
  {
    return (_responseCode);
  }

  public void setFollowupState(VoiceCommandState followupState)
  {
    VoiceCommandState oldFollowupState = _followupState;
    this._followupState = followupState;
    propertyChangeSupport.firePropertyChange("FollowupStates", oldFollowupState, followupState);
  }

  public VoiceCommandState getFollowupState()
  {
    return (_followupState);
  }

  public void setFollowupTTSURI(String followupTTSURI)
  {
    String oldFollowupTTSURI = _followupTTSURI;
    this._followupTTSURI = followupTTSURI;
    propertyChangeSupport.firePropertyChange("FollowupTTSURI", oldFollowupTTSURI, followupTTSURI);
  }

  public String getFollowupTTSURI()
  {
    return (_followupTTSURI);
  }

  public void setFollowupText(String followupText)
  {
    String oldFollowupText = _followupText;
    this._followupText = followupText;
    propertyChangeSupport.firePropertyChange("FollowupText", oldFollowupText, followupText);
  }

  public String getFollowupText()
  {
    return (_followupText);
  }

  public void setVariables(VoiceCommandVariableVO[] variables)
  {
    VoiceCommandVariableVO[] oldVariables = _variables;
    this._variables = variables;
    propertyChangeSupport.firePropertyChange("Variables", oldVariables, variables);
  }

  public VoiceCommandVariableVO[] getVariables()
  {
    return (_variables);
  }

  public VoiceCommandVariableVO getVariable(String name)
  {
    String nameNoUnderscore = name.replaceAll("_", "");
    for (int index = 0; index < _variables.length; index++)
      if (_variables[index].getName().equalsIgnoreCase(name) || _variables[index].getName().equalsIgnoreCase(nameNoUnderscore))
        return (_variables[index]);
    return (null);
  }

  public JSONObject toJSON()
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", _name.toString().toLowerCase());
    jsonObject.put("state", _state.toString().toLowerCase());
    jsonObject.put("server_handler", _serverHandler);
    jsonObject.put("confidence", _confidence);
    jsonObject.put("response_code", _responseCode.toString());
    jsonObject.put("followup_state", _followupState.toString());
    jsonObject.put("followup_TTS_uri", _followupTTSURI);
    jsonObject.put("followup_text", _followupText);
    JSONArray jsonArray = new JSONArray();
    for (int index = 0; index < _variables.length; index++)
      jsonArray.add(_variables[index]);
    jsonObject.put("variables", jsonArray);
    return (jsonObject);
  }
}
