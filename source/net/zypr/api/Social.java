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

import java.io.File;

import java.util.Hashtable;

import net.zypr.api.enums.APIVerbs;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIInvalidServiceException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.vo.GeoBoundsVO;
import net.zypr.api.vo.GeoPositionVO;
import net.zypr.api.vo.InfoMessageSentTypeVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.ServiceStatusVO;
import net.zypr.api.vo.ServiceVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Social
  extends Protocol
{

  public Social()
    throws APICommunicationException, APIProtocolException
  {
    super(true);
  }

  public Social(ServiceVO[] services)
  {
    super(services);
  }

  public String getDefaultServiceForFriendList()
  {
    return (getDefaultService(APIVerbs.SOCIAL_FRIEND_LIST));
  }

  public ItemVO[] friendList()
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (friendList(null, null, 0, 0));
  }

  public ItemVO[] friendList(String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (friendList(service, null, 0, 0));
  }

  public ItemVO[] friendList(String service, int limit)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (friendList(service, null, limit, 0));
  }

  public ItemVO[] friendList(String service, GeoBoundsVO geoBounds, int limit)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (friendList(service, geoBounds, limit, 0));
  }

  public ItemVO[] friendList(GeoBoundsVO geoBounds, int limit)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (friendList(null, geoBounds, limit, 0));
  }

  public ItemVO[] friendList(String service, GeoBoundsVO geoBounds, int limit, int offset)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null)
      service = SERVICE_NAME_ALL;
    else if (service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForFriendList();
    if (!doesVerbExist(service, APIVerbs.SOCIAL_FRIEND_LIST) && !service.equalsIgnoreCase(SERVICE_NAME_ALL))
      throw new APIInvalidServiceException(APIVerbs.SOCIAL_FRIEND_LIST, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    if (geoBounds != null)
      parameters.put("bounds", geoBounds.toJSONString());
    if (limit > 0)
      parameters.put("limit", "" + limit);
    if (limit > 0)
      parameters.put("offset", "" + offset);
    ItemVO[] itemVOs = new ItemVO[0];
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.SOCIAL_FRIEND_LIST, parameters);
        StatusCode statusCode = getStatusCode(jsonObject, APIVerbs.SOCIAL_MESSAGE_GET);
        if (statusCode == StatusCode.SUCCESSFUL || statusCode == StatusCode.PARTIAL_CONTENT)
          {
            Session.getInstance().setPartialAPICall(statusCode, APIVerbs.SOCIAL_MESSAGE_GET);
            JSONArray jsonAction = getActionArrayJSON(jsonObject);
            itemVOs = new ItemVO[jsonAction.size()];
            for (int index = 0; index < itemVOs.length; index++)
              itemVOs[index] = new ItemVO((JSONObject) jsonAction.get(index));
          }
        else
          {
            throw new APIServerErrorException(getStatusMessage(jsonObject));
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

  private StatusCode friendFunctions(APIVerbs apiVerb, String friendID, String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      getDefaultService(apiVerb);
    if (!doesVerbExist(service, apiVerb))
      throw new APIInvalidServiceException(apiVerb, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("friendid", friendID);
    parameters.put("service", service);
    JSONObject jsonObject = getJSON(apiVerb, parameters);
    return (getStatusCode(jsonObject));
  }

  public String getDefaultServiceForFriendAdd()
  {
    return (getDefaultService(APIVerbs.SOCIAL_FRIEND_ADD));
  }

  public StatusCode friendAdd(String friendID, String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (friendFunctions(APIVerbs.SOCIAL_FRIEND_ADD, friendID, service));
  }

  public String getDefaultServiceForFriendDelete()
  {
    return (getDefaultService(APIVerbs.SOCIAL_FRIEND_DELETE));
  }

  public StatusCode friendDelete(String friendID, String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (friendFunctions(APIVerbs.SOCIAL_FRIEND_DELETE, friendID, service));
  }

  public String getDefaultServiceForFriendBlock()
  {
    return (getDefaultService(APIVerbs.SOCIAL_FRIEND_BLOCK));
  }

  public StatusCode friendBlock(String friendID, String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (friendFunctions(APIVerbs.SOCIAL_FRIEND_BLOCK, friendID, service));
  }

  public String getDefaultServiceForFriendUnblock()
  {
    return (getDefaultService(APIVerbs.SOCIAL_FRIEND_UNBLOCK));
  }

  public StatusCode friendUnblock(String friendID, String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (friendFunctions(APIVerbs.SOCIAL_FRIEND_UNBLOCK, friendID, service));
  }

  public StatusCode messageSend(String service, String to, String toService, String toServiceUserID, String scope, String text, String attachment)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    String[] attachments = null;
    if (attachment != null)
      {
        attachments = new String[1];
        attachments[0] = attachment;
      }
    return (messageSend(service, new InfoMessageSentTypeVO(to, toService, toServiceUserID, scope, text, attachments)));
  }

  public StatusCode messageSend(String service, String to, String toService, String toServiceUserID, String scope, String text, String[] attachments)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (messageSend(service, new InfoMessageSentTypeVO(to, toService, toServiceUserID, scope, text, attachments)));
  }

  public String getDefaultServiceForMessageSend()
  {
    return (getDefaultService(APIVerbs.SOCIAL_MESSAGE_SEND));
  }

  public boolean canMessageSend(String service)
  {
    return (doesVerbExist(service, APIVerbs.SOCIAL_MESSAGE_SEND) || service.equals(SERVICE_NAME_ALL));
  }

  public StatusCode messageSend(String service, InfoMessageSentTypeVO infoMessageSentTypeVO)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (!canMessageSend(service))
      throw new APIInvalidServiceException(APIVerbs.SOCIAL_MESSAGE_SEND, service);
    Hashtable<String, String> parameters = buildParameters();
    if (service != null)
      parameters.put("service", service);
    parameters.put("message", infoMessageSentTypeVO.toJSONString());
    return (getStatusCode(getJSON(APIVerbs.SOCIAL_MESSAGE_SEND, parameters)));
  }

  public String getDefaultServiceForMessageGet()
  {
    return (getDefaultService(APIVerbs.SOCIAL_MESSAGE_GET));
  }

  public ItemVO[] messageGet(String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (!canMessageSend(service))
      throw new APIInvalidServiceException(APIVerbs.SOCIAL_MESSAGE_GET, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    if (Session.getInstance().getGeoBounds() != null)
      parameters.put("bounds", Session.getInstance().getGeoBounds().toJSONString());
    ItemVO[] itemVOs = null;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.SOCIAL_MESSAGE_GET, parameters);
        StatusCode statusCode = getStatusCode(jsonObject, APIVerbs.SOCIAL_MESSAGE_GET);
        if (statusCode == StatusCode.SUCCESSFUL || statusCode == StatusCode.PARTIAL_CONTENT)
          {
            Session.getInstance().setPartialAPICall(statusCode, APIVerbs.SOCIAL_MESSAGE_GET);
            JSONArray jsonAction = getActionArrayJSON(jsonObject);
            itemVOs = new ItemVO[jsonAction.size()];
            for (int index = 0; index < itemVOs.length; index++)
              itemVOs[index] = new ItemVO((JSONObject) jsonAction.get(index));
          }
        else
          {
            throw new APIServerErrorException(getStatusMessage(jsonObject));
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

  public ItemVO[] feedGet(String service, String username)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (!doesVerbExist(service, APIVerbs.SOCIAL_FEED_GET))
      throw new APIInvalidServiceException(APIVerbs.SOCIAL_FEED_GET, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    parameters.put("username", username);
    ItemVO[] itemVOs = null;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.SOCIAL_FEED_GET, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonAction = getActionArrayJSON(jsonObject);
            itemVOs = new ItemVO[jsonAction.size()];
            for (int index = 0; index < itemVOs.length; index++)
              itemVOs[index] = new ItemVO((JSONObject) jsonAction.get(index));
          }
        else
          {
            throw new APIServerErrorException(getStatusMessage(jsonObject));
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

  public StatusCode voiceNoteSend(String service, File file, String mimeType, String to, String toService, String toServiceUserID, String scope, GeoPositionVO point, String text, String attachment)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    String[] attachments = null;
    if (attachment != null)
      {
        attachments = new String[1];
        attachments[0] = attachment;
      }
    return (postStatus(service, file, new InfoMessageSentTypeVO(to, toService, toServiceUserID, scope, text, mimeType, point, attachments)));
  }

  public String getDefaultServiceForPostStatus()
  {
    return (getDefaultService(APIVerbs.SOCIAL_POST_STATUS));
  }

  public boolean canPostStatus(String service)
  {
    boolean canPost = false;
    try
      {
        if (Session.getInstance().getServiceAuthStatus() == null)
          API.getInstance().getAuth().serviceAuthStatus();
        canPost = doesVerbExist(service, APIVerbs.SOCIAL_POST_STATUS) || service.equals(SERVICE_NAME_ALL);
        if (canPost)
          {
            ServiceStatusVO serviceStatusVO = Session.getInstance().getServiceAuthStatus().getServiceStatuses(service);
            canPost = (serviceStatusVO != null && serviceStatusVO.isCurrentlyAuthenticated());
          }
      }
    catch (Exception exception)
      {
        // Ignore
      }
    return (canPost);
  }

  public StatusCode postStatus(String service, File file, String mimeType, String to, String toService, String toServiceUserID, String scope, GeoPositionVO point, String text, String[] attachments)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (postStatus(service, file, new InfoMessageSentTypeVO(to, toService, toServiceUserID, scope, text, mimeType, point, attachments)));
  }

  public StatusCode postStatus(String service, String to, String toService, String toServiceUserID, String scope, String text, String[] attachments)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (postStatus(service, null, new InfoMessageSentTypeVO(to, toService, toServiceUserID, scope, text, attachments)));
  }

  public StatusCode postStatus(String service, String to, String toService, String toServiceUserID, String scope, String text, String attachment)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (postStatus(service, null, new InfoMessageSentTypeVO(to, toService, toServiceUserID, scope, text, new String[]
          { attachment })));
  }

  public StatusCode postStatus(String service, File file, InfoMessageSentTypeVO infoMessageSentTypeVO)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (!canPostStatus(service))
      throw new APIInvalidServiceException(APIVerbs.SOCIAL_POST_STATUS, service);
    Hashtable<String, String> parameters = buildParameters();
    if (service != null)
      parameters.put("service", service);
    parameters.put("message", infoMessageSentTypeVO.toJSONString());
    if (file == null)
      return (getStatusCode(getJSON(APIVerbs.SOCIAL_POST_STATUS, parameters)));
    return (getStatusCode(doFilePost(APIVerbs.SOCIAL_POST_STATUS, parameters, null, file)));
  }

  public boolean canMessageSendPublic(String service)
  {
    boolean canPost = false;
    try
      {
        if (Session.getInstance().getServiceAuthStatus() == null)
          API.getInstance().getAuth().serviceAuthStatus();
        canPost = doesVerbExist(service, APIVerbs.SOCIAL_MESSAGE_SEND_PUBLIC) || service.equals(SERVICE_NAME_ALL);
        if (canPost)
          {
            ServiceStatusVO serviceStatusVO = Session.getInstance().getServiceAuthStatus().getServiceStatuses(service);
            canPost = (serviceStatusVO != null && serviceStatusVO.isCurrentlyAuthenticated());
          }
      }
    catch (Exception exception)
      {
        // Ignore
      }
    return (canPost);
  }

  public StatusCode messageSendPublic(String service, File file, String mimeType, String to, String toService, String toServiceUserID, String scope, GeoPositionVO point, String text, String[] attachments)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (messageSendPublic(service, file, new InfoMessageSentTypeVO(to, toService, toServiceUserID, scope, text, mimeType, point, attachments)));
  }

  public StatusCode messageSendPublic(String service, String to, String toService, String toServiceUserID, String scope, String text, String[] attachments)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (messageSendPublic(service, null, new InfoMessageSentTypeVO(to, toService, toServiceUserID, scope, text, attachments)));
  }

  public StatusCode messageSendPublic(String service, String to, String toService, String toServiceUserID, String scope, String text, String attachment)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (messageSendPublic(service, null, new InfoMessageSentTypeVO(to, toService, toServiceUserID, scope, text, new String[]
          { attachment })));
  }

  public StatusCode messageSendPublic(String service, File file, InfoMessageSentTypeVO infoMessageSentTypeVO)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (!canMessageSendPublic(service))
      throw new APIInvalidServiceException(APIVerbs.SOCIAL_MESSAGE_SEND_PUBLIC, service);
    Hashtable<String, String> parameters = buildParameters();
    if (service != null)
      parameters.put("service", service);
    parameters.put("message", infoMessageSentTypeVO.toJSONString());
    if (file == null)
      return (getStatusCode(getJSON(APIVerbs.SOCIAL_MESSAGE_SEND_PUBLIC, parameters)));
    return (getStatusCode(doFilePost(APIVerbs.SOCIAL_MESSAGE_SEND_PUBLIC, parameters, null, file)));
  }

  public String getMessageScope(String service, String scope)
  {
    if (scope.equalsIgnoreCase("public"))
      {
        if (canMessageSendPublic(service))
          return ("public");
        else if (canMessageSend(service))
          return ("private");
      }
    else if (scope.equalsIgnoreCase("private"))
      {
        if (canMessageSend(service))
          return ("private");
        else if (canMessageSendPublic(service))
          return ("public");
      }
    return (null);
  }
}
