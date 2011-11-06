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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import net.zypr.api.bgprocesses.BackgroundProcess;
import net.zypr.api.bgprocesses.FriendListUpdater;
import net.zypr.api.bgprocesses.MessageRetriever;
import net.zypr.api.enums.APIVerbs;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.utils.Debug;
import net.zypr.api.vo.GeoBoundsVO;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.api.vo.InfoMessageGetTypeVO;
import net.zypr.api.vo.InfoUserTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.ServiceAuthStatusVO;

public class Session
{
  private static final Session INSTANCE = new Session();
  private String _username;
  private String _password;
  private String _token;
  private String _deviceID;
  private GeoPositionVO _position;
  private GeoBoundsVO _geoBounds;
  private double _positionAccuracy;
  private InfoUserTypeVO _userInfo = new InfoUserTypeVO();
  private ItemVO[] _friendList = new ItemVO[0];
  private Hashtable<String, BackgroundProcess> _backgroundProcesses = new Hashtable<String, BackgroundProcess>();
  private ItemVO[] _messages = new ItemVO[0];
  private Date _lastReceivedMessageDate = new Date(0);
  private int _activeRequestCount = 0;
  private int _messageRetrivalInterval = 50;
  private int _friendListUpdateInterval = 600;
  private String _mediaSource = Protocol.SERVICE_NAME_ALL;
  private Vector<ItemVO> _favorites = new Vector<ItemVO>();
  private ServiceAuthStatusVO _serviceAuthStatus;
  private Set<APIVerbs> _partialAPICalls = Collections.synchronizedSet(new HashSet<APIVerbs>());
  private transient PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

  public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    _propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener)
  {
    _propertyChangeSupport.addPropertyChangeListener(propertyName, propertyChangeListener);
  }

  public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    _propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener)
  {
    _propertyChangeSupport.removePropertyChangeListener(propertyName, propertyChangeListener);
  }

  private Session()
  {
    super();
  }

  public static Session getInstance()
  {
    return (INSTANCE);
  }

  public void setUsername(String username)
  {
    String oldUsername = username;
    _username = username;
    _propertyChangeSupport.firePropertyChange("Username", oldUsername, username);
  }

  public String getUsername()
  {
    return (_username);
  }

  public void setToken(String token)
  {
    String oldToken = _token;
    _token = token;
    _propertyChangeSupport.firePropertyChange("Token", oldToken, token);
  }

  public String getToken()
  {
    return (_token);
  }

  public void setPosition(double latitude, double longitude)
  {
    setPosition(new GeoPositionVO(latitude, longitude));
  }

  public void setPosition(double latitude, double longitude, double elevation)
  {
    setPosition(new GeoPositionVO(latitude, longitude, elevation));
  }

  public void setPosition(GeoPositionVO position)
  {
    GeoPositionVO oldPosition = _position;
    _position = position;
    _propertyChangeSupport.firePropertyChange("Position", oldPosition, position);
  }

  public GeoPositionVO getPosition()
  {
    return (_position);
  }

  public void clear()
  {
    _username = null;
    _token = null;
    _deviceID = null;
    _userInfo = new InfoUserTypeVO();
  }

  @Override
  public String toString()
  {
    return ("[" + getClass().getCanonicalName() + " = {\"username\":\"" + _username + "\", \"token\":\"" + _token + "\", \"userInfo\":" + _userInfo.toString() + ", \"position\":" + (_position == null ? "null" : _position.toString()) + ", \"_activeRequestCount\":" + _activeRequestCount + "}]");
  }

  public void setUserInfo(InfoUserTypeVO userInfo)
  {
    InfoUserTypeVO oldUserInfo = _userInfo;
    _userInfo = userInfo;
    _propertyChangeSupport.firePropertyChange("UserInfo", oldUserInfo, userInfo);
    if (_userInfo != null)
      setPosition(_userInfo.getCurrentPosition());
  }

  public InfoUserTypeVO getUserInfo()
  {
    return (_userInfo);
  }

  public void setPositionAccuracy(double positionAccuracy)
  {
    double oldPositionAccuracy = _positionAccuracy;
    _positionAccuracy = positionAccuracy;
    _propertyChangeSupport.firePropertyChange("PositionAccuracy", oldPositionAccuracy, positionAccuracy);
  }

  public double getPositionAccuracy()
  {
    return (_positionAccuracy);
  }

  public void setFriendList(ItemVO[] friendList)
  {
    ItemVO[] oldFriendList = _friendList;
    _friendList = friendList;
    _propertyChangeSupport.firePropertyChange("FriendList", oldFriendList, friendList);
  }

  public void setFriendList(ItemVO[] friendList, boolean append)
  {
    ItemVO[] oldFriendList = _friendList;
    if (append)
      {
        ItemVO[] newFriendList = new ItemVO[_friendList.length + friendList.length];
        System.arraycopy(_friendList, 0, newFriendList, 0, _friendList.length);
        System.arraycopy(friendList, 0, newFriendList, _friendList.length, friendList.length);
        Arrays.sort(newFriendList);
        _friendList = newFriendList;
      }
    else
      {
        _friendList = friendList;
      }
    _propertyChangeSupport.firePropertyChange("FriendList", oldFriendList, friendList);
  }

  public ItemVO[] getFriendList()
  {
    return (_friendList);
  }

  public ItemVO getFriend(String name)
  {
    for (int index = 0; index < _friendList.length; index++)
      if (_friendList[index].getName().equalsIgnoreCase(name))
        return (_friendList[index]);
    return (null);
  }

  public ItemVO getFriend(String id, String service)
  {
    for (int index = 0; index < _friendList.length; index++)
      if (_friendList[index].getServiceItemID().equalsIgnoreCase(id) && _friendList[index].getService().equalsIgnoreCase(service))
        return (_friendList[index]);
    return (null);
  }

  public boolean isLoggedIn()
  {
    return (_username != null && _token != null && _deviceID != null);
  }

  public void loggedIn()
  {
    FriendListUpdater friendListUpdater = new FriendListUpdater(_friendListUpdateInterval);
    friendListUpdater.start();
    _backgroundProcesses.put(friendListUpdater.getName(), friendListUpdater);
    MessageRetriever messageRetriever = new MessageRetriever(_messageRetrivalInterval);
    messageRetriever.start();
    _backgroundProcesses.put(messageRetriever.getName(), messageRetriever);
    try
      {
        API.getInstance().getUser().favoriteList(null);
      }
    catch (Exception exception)
      {
        Debug.displayStack(this, exception);
      }
  }

  public void forceUpdateBGProcesses()
  {
    _friendList = new ItemVO[0];
    _messages = new ItemVO[0];
    _lastReceivedMessageDate = new Date(0);
    for (Enumeration enumeration = _backgroundProcesses.keys(); enumeration.hasMoreElements(); )
      {
        String key = enumeration.nextElement().toString();
        BackgroundProcess backgroundProcess = _backgroundProcesses.get(key);
        backgroundProcess.forceUpdate();
      }
  }

  public void loggedOut()
  {
    _token = null;
    _username = null;
    _deviceID = null;
    _userInfo = new InfoUserTypeVO();
    _position = null;
    _geoBounds = null;
    _positionAccuracy = 0;
    _friendList = new ItemVO[0];
    if (_favorites != null)
      _favorites.clear();
    _messages = new ItemVO[0];
    _lastReceivedMessageDate = new Date(0);
    for (Enumeration enumeration = _backgroundProcesses.keys(); enumeration.hasMoreElements(); )
      {
        String key = enumeration.nextElement().toString();
        BackgroundProcess backgroundProcess = _backgroundProcesses.get(key);
        backgroundProcess.setRunning(false);
        backgroundProcess.forceUpdate();
        _backgroundProcesses.remove(key);
      }
  }

  public void addMessages(ItemVO[] messages)
  {
    if (messages != null && messages.length > 0)
      {
        ItemVO[] newMessages = new ItemVO[_messages.length + messages.length];
        System.arraycopy(_messages, 0, newMessages, 0, _messages.length);
        System.arraycopy(messages, 0, newMessages, _messages.length, messages.length);
        Arrays.sort(newMessages);
        _messages = newMessages;
        int newCount = 0;
        boolean found = false;
        for (int index = 0; index < _messages.length && !found; index++)
          if (((InfoMessageGetTypeVO) _messages[index].getInfo()).getDateSent().after(_lastReceivedMessageDate))
            newCount++;
          else
            found = true;
        if (newCount > 0)
          {
            _lastReceivedMessageDate = ((InfoMessageGetTypeVO) _messages[0].getInfo()).getDateSent();
            newMessages = new ItemVO[newCount];
            System.arraycopy(_messages, 0, newMessages, 0, newMessages.length);
            _propertyChangeSupport.firePropertyChange("Messages", null, newMessages);
          }
      }
  }

  public ItemVO[] getMessages()
  {
    return (_messages);
  }

  public void setMessageRead(int index)
  {
    ((InfoMessageGetTypeVO) _messages[index].getInfo()).setRead(true);
  }

  public ItemVO getMessage(int index)
  {
    return (_messages[index]);
  }

  public int getMessageIndex(String globalItemID)
  {
    if (_messages == null)
      return (-1);
    for (int index = 0; index < _messages.length; index++)
      if (((ItemVO) _messages[index]).getGlobalItemID().equals(globalItemID))
        return (index);
    return (-1);
  }

  public ItemVO getNextMessage(String globalItemID)
  {
    if (_messages == null)
      return (null);
    int index = getMessageIndex(globalItemID);
    if (index != -1 && index + 1 < _messages.length)
      return (((ItemVO) _messages[index + 1]));
    return (null);
  }

  public ItemVO getPreviousMessage(String globalItemID)
  {
    if (_messages == null)
      return (null);
    int index = getMessageIndex(globalItemID);
    if (index != -1 && index - 1 >= 0)
      return (((ItemVO) _messages[index - 1]));
    return (null);
  }

  public void deleteMessage(int index)
  {
    //    if (_messages != null)
    //      _messages.remove(index);
  }

  public int messageCount()
  {
    if (_messages == null)
      return (-1);
    return (_messages.length);
  }

  public void setGeoBounds(GeoBoundsVO geoBounds)
  {
    _geoBounds = geoBounds;
  }

  public GeoBoundsVO getGeoBounds()
  {
    return (_geoBounds);
  }

  public synchronized void addActiveRequestCount()
  {
    _activeRequestCount++;
    _propertyChangeSupport.firePropertyChange("ActiveRequestCount", _activeRequestCount - 1, _activeRequestCount);
  }

  public synchronized void removeActiveRequestCount()
  {
    _activeRequestCount--;
    _propertyChangeSupport.firePropertyChange("ActiveRequestCount", _activeRequestCount + 1, _activeRequestCount);
  }

  public int getActiveRequestCount()
  {
    return (_activeRequestCount);
  }

  public void setMessageRetrivalInterval(int messageRetrivalInterval)
  {
    int oldMessageRetrivalInterval = _messageRetrivalInterval;
    _messageRetrivalInterval = messageRetrivalInterval;
    _propertyChangeSupport.firePropertyChange("MessageRetrivalInterval", oldMessageRetrivalInterval, messageRetrivalInterval);
  }

  public int getMessageRetrivalInterval()
  {
    return (_messageRetrivalInterval);
  }

  public void setFriendListUpdateInterval(int friendListUpdateInterval)
  {
    int oldFriendListUpdateInterval = _friendListUpdateInterval;
    _friendListUpdateInterval = friendListUpdateInterval;
    _propertyChangeSupport.firePropertyChange("FriendListUpdateInterval", oldFriendListUpdateInterval, friendListUpdateInterval);
  }

  public int getFriendListUpdateInterval()
  {
    return (_friendListUpdateInterval);
  }

  public void setDeviceID(String deviceID)
  {
    String oldDeviceID = _deviceID;
    _deviceID = deviceID;
    _propertyChangeSupport.firePropertyChange("DeviceID", oldDeviceID, deviceID);
  }

  public String getDeviceID()
  {
    return (_deviceID);
  }

  public int getFavoriteIndex(ItemVO itemVO)
  {
    return (getFavoriteIndex(itemVO.getGlobalItemID()));
  }

  public int getFavoriteIndex(String globalItemID)
  {
    for (int index = 0; index < _favorites.size(); index++)
      if (_favorites.get(index).getGlobalItemID().equals(globalItemID))
        return (index);
    return (-1);
  }

  public void addToFavorites(ItemVO itemVO)
  {
    _favorites.add(0, itemVO);
  }

  public void appendToFavorites(ItemVO itemVO)
  {
    _favorites.add(itemVO);
  }

  public ItemVO removeFromFavorites(ItemVO itemVO)
  {
    return (removeFromFavorites(itemVO.getGlobalItemID()));
  }

  public ItemVO removeFromFavorites(String globalItemID)
  {
    int index = getFavoriteIndex(globalItemID);
    if (index != -1)
      return (_favorites.remove(index));
    return (null);
  }

  public boolean isInFavorites(ItemVO itemVO)
  {
    return (isInFavorites(itemVO.getGlobalItemID()));
  }

  public boolean isInFavorites(String globalItemID)
  {
    return (getFavoriteIndex(globalItemID) != -1);
  }

  public ItemVO[] getFavorites()
  {
    ItemVO[] itemVOs = new ItemVO[_favorites.size()];
    int index = 0;
    for (Enumeration<ItemVO> enumeration = _favorites.elements(); enumeration.hasMoreElements(); )
      itemVOs[index++] = enumeration.nextElement();
    return (itemVOs);
  }

  public boolean hasFavorites()
  {
    return (!_favorites.isEmpty());
  }

  public void setPassword(String password)
  {
    String oldPassword = _password;
    _password = password;
    _propertyChangeSupport.firePropertyChange("Password", oldPassword, password);
  }

  public String getPassword()
  {
    return (_password);
  }

  public void setMediaSource(String mediaSource)
  {
    String oldMediaSource = _mediaSource;
    _mediaSource = mediaSource;
    _propertyChangeSupport.firePropertyChange("MediaSource", oldMediaSource, mediaSource);
  }

  public String getMediaSource()
  {
    return (_mediaSource);
  }

  public void setServiceAuthStatus(ServiceAuthStatusVO _serviceAuthStatus)
  {
    ServiceAuthStatusVO oldServiceAuthStatus = _serviceAuthStatus;
    _serviceAuthStatus = _serviceAuthStatus;
    _propertyChangeSupport.firePropertyChange("ServiceAuthStatus", oldServiceAuthStatus, _serviceAuthStatus);
  }

  public ServiceAuthStatusVO getServiceAuthStatus()
  {
    return (_serviceAuthStatus);
  }

  public void setPartialAPICall(StatusCode statusCode, APIVerbs apiVerb)
  {
    if (statusCode == StatusCode.SUCCESSFUL)
      removePartialAPICall(apiVerb);
    else if (statusCode == StatusCode.PARTIAL_CONTENT)
      addPartialAPICall(apiVerb);
  }

  public void addPartialAPICall(APIVerbs apiVerb)
  {
    _partialAPICalls.add(apiVerb);
  }

  public void removePartialAPICall(APIVerbs apiVerb)
  {
    _partialAPICalls.remove(apiVerb);
  }

  public boolean isPartialAPICall(APIVerbs apiVerb)
  {
    return (_partialAPICalls.contains(apiVerb));
  }
}
