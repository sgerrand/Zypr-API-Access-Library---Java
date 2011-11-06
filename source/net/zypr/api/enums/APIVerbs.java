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


package net.zypr.api.enums;

public enum APIVerbs
{
  DESCRIBE(null, "describe"),
  AUTH_LOGIN("auth", "login"),
  AUTH_SERVICE_AUTH_STATUS("auth", "service_auth_status"),
  AUTH_CREATE_USER("auth", "create_user"),
  AUTH_RESET_PASSWORD("auth", "reset_password"),
  AUTH_LOGOUT("auth", "logout"),
  MEDIA_LIST("media", "list"),
  MEDIA_SEARCH("media", "search"),
  USER_INFO_GET("user", "info_get"),
  USER_INFO_SET("user", "info_set"),
  USER_FAVORITE_SET("user", "favorite_set"),
  USER_FAVORITE_LIST("user", "favorite_list"),
  USER_FAVORITE_DELETE("user", "favorite_delete"),
  SOCIAL_FRIEND_LIST("social", "friend_list"),
  SOCIAL_FRIEND_ADD("social", "friend_add"),
  SOCIAL_FRIEND_DELETE("social", "friend_delete"),
  SOCIAL_FRIEND_BLOCK("social", "friend_block"),
  SOCIAL_FRIEND_UNBLOCK("social", "friend_unblock"),
  SOCIAL_FEED_GET("social", "feed_get"),
  SOCIAL_MESSAGE_GET("social", "message_get"),
  SOCIAL_MESSAGE_SEND("social", "message_send"),
  SOCIAL_MESSAGE_SEND_PUBLIC("social", "message_send_public"),
  SOCIAL_POST_STATUS("social", "post_status"),
  VOICE_PARSE("voice", "parse"),
  VOICE_TTS("voice", "tts"),
  VOICE_LIST("voice", "list"),
  MAP_GET("map", "get"),
  MAP_GEOCODE("map", "geocode"),
  MAP_PLACENAME_GEOCODE("map", "placename_geocode"),
  MAP_REVERSE_GEOCODE("map", "reverse_geocode"),
  SHOP_SEARCH("shop", "search"),
  SHOP_CART_CREATE("shop", "cart_create"),
  SHOP_CART_ADD("shop", "cart_add"),
  SHOP_CART_REMOVE("shop", "cart_remove"),
  SHOP_CART_DETAILS("shop", "cart_details"),
  SHOP_ITEM_DETAILS("shop", "item_details"),
  POI_SEARCH("poi", "search"),
  POI_DETAILS("poi", "details"),
  POI_LIST("poi", "list"),
  NAV_ROUTE_GET("nav", "route_get"),
  WEATHER_CURRENT("weather", "current"),
  WEATHER_FORECAST("weather", "forecast");
  private String _entity;
  private String _verb;

  APIVerbs(String entity, String verb)
  {
    _entity = entity;
    _verb = verb;
  }

  public String getVerb()
  {
    return (_verb);
  }

  public String getEntity()
  {
    return (_entity);
  }

  public String toPath()
  {
    return ((_entity != null ? _entity + "/" : "") + _verb);
  }

  public static APIVerbs valueOf(String entity, String verb)
  {
    APIVerbs[] apiVerbs = APIVerbs.values();
    for (int index = 0; index < apiVerbs.length; index++)
      if (apiVerbs[index].getEntity() != null && apiVerbs[index].getEntity().equalsIgnoreCase(entity) && apiVerbs[index].getVerb().equalsIgnoreCase(verb))
        return (apiVerbs[index]);
    return (null);
  }
}
