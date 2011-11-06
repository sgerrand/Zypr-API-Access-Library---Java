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

import net.zypr.api.enums.APIVerbs;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.utils.Debug;
import net.zypr.api.vo.ActionHandlerVO;
import net.zypr.api.vo.InfoVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.ServiceVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class API
{
  public static final String LICENCE_KEY = System.getProperty("net.zypr.api.key");
  public static String SERVER_URL = System.getProperty("net.zypr.api.url");
  private static API _api;
  private static Auth _auth;
  private static Calendar _calendar;
  private static Contact _contact;
  private static Map _map;
  private static Media _media;
  private static Nav _nav;
  private static POI _poi;
  private static Shop _shop;
  private static Social _social;
  private static User _user;
  private static Voice _voice;
  private static Weather _weather;

  private API()
  {
    super();
    Protocol protocol = new Protocol();
    try
      {
        JSONObject jsonObject = protocol.doGetJSON(protocol.buildURL(APIVerbs.DESCRIBE, protocol.buildParameters()));
        if (protocol.getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonArray = protocol.getDataArrayJSON(jsonObject);
            for (int index = 0; index < jsonArray.size(); index++)
              {
                jsonObject = (JSONObject) jsonArray.get(index);
                JSONArray serviceVerbsJSONArray = (JSONArray) jsonObject.get("service_verbs");
                ServiceVO[] services = new ServiceVO[serviceVerbsJSONArray.size()];
                for (int indexServiceVerbs = 0; indexServiceVerbs < services.length; indexServiceVerbs++)
                  services[indexServiceVerbs] = new ServiceVO((JSONObject) serviceVerbsJSONArray.get(indexServiceVerbs));
                String entity = ((String) jsonObject.get("entity")).toLowerCase();
                if (entity.equals("auth"))
                  _auth = new Auth(services);
                else if (entity.equals("calendar"))
                  _calendar = new Calendar(services);
                else if (entity.equals("contact"))
                  _contact = new Contact(services);
                else if (entity.equals("map"))
                  _map = new Map(services);
                else if (entity.equals("media"))
                  _media = new Media(services);
                else if (entity.equals("nav"))
                  _nav = new Nav(services);
                else if (entity.equals("poi"))
                  _poi = new POI(services);
                else if (entity.equals("shop"))
                  _shop = new Shop(services);
                else if (entity.equals("social"))
                  _social = new Social(services);
                else if (entity.equals("user"))
                  _user = new User(services);
                else if (entity.equals("voice"))
                  _voice = new Voice(services);
                else if (entity.equals("weather"))
                  _weather = new Weather(services);
                else
                  Debug.displayWarning(this, "Unknown API entity : \"" + entity + "\"");
              }
          }
        else
          throw new APIProtocolException(protocol.getStatusMessage(jsonObject));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  public static API getInstance()
  {
    if (_api == null)
      _api = new API();
    return (_api);
  }

  public synchronized Auth getAuth()
    throws APICommunicationException, APIProtocolException
  {
    if (_auth == null)
      _auth = new Auth();
    return (_auth);
  }

  public synchronized Calendar getCalendar()
    throws APICommunicationException, APIProtocolException
  {
    if (_calendar == null)
      _calendar = new Calendar();
    return (_calendar);
  }

  public synchronized Contact getContact()
    throws APICommunicationException, APIProtocolException
  {
    if (_contact == null)
      _contact = new Contact();
    return (_contact);
  }

  public synchronized Map getMap()
    throws APICommunicationException, APIProtocolException
  {
    if (_map == null)
      _map = new Map();
    return (_map);
  }

  public synchronized Media getMedia()
    throws APICommunicationException, APIProtocolException
  {
    if (_media == null)
      _media = new Media();
    return (_media);
  }

  public synchronized Nav getNav()
    throws APICommunicationException, APIProtocolException
  {
    if (_nav == null)
      _nav = new Nav();
    return (_nav);
  }

  public synchronized POI getPOI()
    throws APICommunicationException, APIProtocolException
  {
    if (_poi == null)
      _poi = new POI();
    return (_poi);
  }

  public synchronized Shop getShop()
    throws APICommunicationException, APIProtocolException
  {
    if (_shop == null)
      _shop = new Shop();
    return (_shop);
  }

  public synchronized Social getSocial()
    throws APICommunicationException, APIProtocolException
  {
    if (_social == null)
      _social = new Social();
    return (_social);
  }

  public synchronized User getUser()
    throws APICommunicationException, APIProtocolException
  {
    if (_user == null)
      _user = new User();
    return (_user);
  }

  public synchronized Voice getVoice()
    throws APICommunicationException, APIProtocolException
  {
    if (_voice == null)
      _voice = new Voice();
    return (_voice);
  }

  public synchronized Weather getWeather()
    throws APICommunicationException, APIProtocolException
  {
    if (_weather == null)
      _weather = new Weather();
    return (_weather);
  }

  public byte[] getBytes(String url)
    throws APICommunicationException, APIProtocolException
  {
    return (getAuth().doGetBytes(url));
  }

  public JSONObject getJSON(String url)
    throws APICommunicationException, APIProtocolException
  {
    return (getAuth().doGetJSON(url));
  }

  public InfoVO getInfoVO(String url)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    InfoVO infoVO = null;
    try
      {
        JSONObject jsonObject = getAuth().doGetJSON(url);
        if (getAuth().getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          infoVO = InfoVO.getObject(getAuth().getDataKeyedObjectJSON(jsonObject, "info"));
        else
          throw new APIServerErrorException(getAuth().getStatusMessage(jsonObject));
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    catch (NullPointerException nullPointerException)
      {
        throw new APIProtocolException(nullPointerException);
      }
    return (infoVO);
  }

  public ItemVO getItemVO(String url)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    ItemVO[] itemVOs = getItemVOs(url);
    if (itemVOs != null && itemVOs.length > 0)
      return (itemVOs[0]);
    return (null);
  }

  public ItemVO[] getItemVOs(String url)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    ItemVO[] itemVOs = null;
    try
      {
        JSONObject jsonObject = getAuth().doGetJSON(url);
        if (getAuth().getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonAction = getAuth().getActionArrayJSON(jsonObject);
            itemVOs = new ItemVO[jsonAction.size()];
            for (int index = 0; index < itemVOs.length; index++)
              itemVOs[index] = new ItemVO((JSONObject) jsonAction.get(index));
          }
        else
          {
            throw new APIServerErrorException(getAuth().getStatusMessage(jsonObject));
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

  public void doCallback(ItemVO itemVO, String actionHandlerName)
  {
    try
      {
        doCallback(itemVO.getAction(actionHandlerName));
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  public void doCallback(final ActionHandlerVO actionHandler)
  {
    if (actionHandler == null)
      return;
    new Thread(new Runnable()
    {
      public void run()
      {
        try
          {
            JSONObject jsonObject = getAuth().doGetJSON(actionHandler.getCallbackURI());
            if (getAuth().getStatusCode(jsonObject) != StatusCode.SUCCESSFUL)
              Debug.displayWarning(this, getAuth().getStatusMessage(jsonObject));
          }
        catch (Exception exception)
          {
            Debug.displayStack(this, exception);
          }
      }
    }).start();
  }
}
