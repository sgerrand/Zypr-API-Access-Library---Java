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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import net.zypr.api.enums.APIVerbs;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIInvalidServiceException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIServerErrorException;
import net.zypr.api.vo.ActionHandlerVO;
import net.zypr.api.vo.ContextDataVO;
import net.zypr.api.vo.ItemVO;
import net.zypr.api.vo.ServiceVO;
import net.zypr.api.vo.TextToSpeechVO;
import net.zypr.api.vo.VoiceCommandDataVO;
import net.zypr.api.vo.VoiceCommandListVO;
import net.zypr.api.vo.VoiceCommandResponseVO;
import net.zypr.api.vo.VoiceCommandVO;
import net.zypr.api.vo.VoiceCommandVariableVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Voice
  extends Protocol
{
  public Voice()
    throws APICommunicationException, APIProtocolException
  {
    super(true);
  }

  public Voice(ServiceVO[] services)
  {
    super(services);
  }

  public String getDefaultServiceForParse()
  {
    return (getDefaultService(APIVerbs.VOICE_PARSE));
  }

  public VoiceCommandResponseVO parse(VoiceCommandDataVO voiceCommandDataVO)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (parse(null, voiceCommandDataVO));
  }

  public VoiceCommandResponseVO parse(String service, VoiceCommandDataVO voiceCommandDataVO)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null)
      service = SERVICE_NAME_ALL;
    else if (service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForParse();
    if (!doesVerbExist(service, APIVerbs.VOICE_PARSE) && !service.equalsIgnoreCase(SERVICE_NAME_ALL))
      throw new APIInvalidServiceException(APIVerbs.VOICE_PARSE, service);
    Hashtable<String, String> urlParameters = buildParameters();
    urlParameters.put("service", service);
    Hashtable<String, String> postParameters = new Hashtable<String, String>();
    postParameters.put("options", voiceCommandDataVO.toJSONString());
    VoiceCommandVO[] voiceCommandVOs = new VoiceCommandVO[0];
    VoiceCommandVariableVO[] voiceCommandVariableVOs = new VoiceCommandVariableVO[0];
    try
      {
        JSONObject jsonObject = null;
        if (voiceCommandDataVO.getInputStream() != null)
          jsonObject = doStreamPost(APIVerbs.VOICE_PARSE, urlParameters, postParameters, voiceCommandDataVO.getInputStream());
        else
          jsonObject = doFilePost(APIVerbs.VOICE_PARSE, urlParameters, postParameters, voiceCommandDataVO.getAudioFile());
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonAction = getDataArrayJSON(jsonObject);
            for (int index = 0; index < jsonAction.size(); index++)
              {
                if (((JSONObject) jsonAction.get(index)).get("command") != null)
                  {
                    VoiceCommandVO voiceCommandVO = new VoiceCommandVO((JSONObject) ((JSONObject) jsonAction.get(index)).get("command"));
                    VoiceCommandVO[] newVoiceCommandVOs = new VoiceCommandVO[voiceCommandVOs.length + 1];
                    System.arraycopy(voiceCommandVOs, 0, newVoiceCommandVOs, 0, voiceCommandVOs.length);
                    newVoiceCommandVOs[voiceCommandVOs.length] = voiceCommandVO;
                    voiceCommandVOs = newVoiceCommandVOs;
                  }
                else if (((JSONObject) jsonAction.get(index)).get("variables") != null)
                  {
                    JSONArray variables = (JSONArray) ((JSONObject) jsonAction.get(index)).get("variables");
                    voiceCommandVariableVOs = new VoiceCommandVariableVO[variables.size()];
                    for (int offset = 0; offset < voiceCommandVariableVOs.length; offset++)
                      voiceCommandVariableVOs[offset] = new VoiceCommandVariableVO((JSONObject) variables.get(offset));
                  }
              }
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
    return (new VoiceCommandResponseVO(voiceCommandVariableVOs, voiceCommandVOs));
  }

  public String getDefaultServiceForTTS()
  {
    return (getDefaultService(APIVerbs.VOICE_TTS));
  }

  public byte[] getTTSFile(String url)
    throws APICommunicationException
  {
    return (doGetBytes(url));
  }

  public String ttsPLS(String title, ArrayList<ContextDataVO> contextData)
  {
    return (ttsPLS(null, title, contextData));
  }

  public String ttsPLS(String service, String title, ArrayList<ContextDataVO> contextData)
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForTTS();
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "pls");
    jsonObject.put("value", "true");
    jsonArray.add(jsonObject);
    if (title != null)
      {
        jsonObject = new JSONObject();
        jsonObject.put("name", "text");
        jsonObject.put("value", title);
      }
    jsonArray.add(jsonObject);
    for (int index = 0; index < contextData.size() && index < 3; index++)
      {
        jsonObject = new JSONObject();
        jsonObject.put("name", "text");
        jsonObject.put("value", contextData.get(index).getName());
        jsonArray.add(jsonObject);
      }
    JSONObject jsonObjectData = new JSONObject();
    jsonObjectData.put("data", jsonArray);
    parameters.put("options", jsonObjectData.toJSONString());
    return (buildURL(APIVerbs.VOICE_TTS, parameters).replaceFirst("https", "http"));
  }

  public TextToSpeechVO ttsWave(String text)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (ttsWave(null, text));
  }

  public TextToSpeechVO ttsWave(String service, String text)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Properties options = new Properties();
    options.setProperty("text", text);
    options.setProperty("audio_format", "wav");
    return (tts(service, options));
  }

  public TextToSpeechVO tts(String text)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (tts(null, text));
  }

  public TextToSpeechVO tts(String service, String text)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    Properties options = new Properties();
    options.setProperty("text", text);
    return (tts(service, options));
  }

  public TextToSpeechVO tts(String service, Properties options)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForTTS();
    if (!doesVerbExist(service, APIVerbs.VOICE_TTS))
      throw new APIInvalidServiceException(APIVerbs.VOICE_TTS, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    JSONArray jsonArray = new JSONArray();
    for (Enumeration enumeration = options.keys(); enumeration.hasMoreElements(); )
      {
        String key = (String) enumeration.nextElement();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", key);
        jsonObject.put("value", options.getProperty(key));
        jsonArray.add(jsonObject);
      }
    JSONObject jsonObjectData = new JSONObject();
    jsonObjectData.put("data", jsonArray);
    parameters.put("options", jsonObjectData.toJSONString());
    TextToSpeechVO textToSpeechVO = new TextToSpeechVO();
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.VOICE_TTS, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray jsonData = getDataArrayJSON(jsonObject);
            textToSpeechVO = new TextToSpeechVO((JSONObject) jsonData.get(0));
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
    return (textToSpeechVO);
  }

  public String getDefaultServiceForList()
  {
    return (getDefaultService(APIVerbs.VOICE_LIST));
  }

  public VoiceCommandListVO list(String service, String category)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    if (service == null || service.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
      service = getDefaultServiceForList();
    if (!doesVerbExist(service, APIVerbs.VOICE_LIST))
      throw new APIInvalidServiceException(APIVerbs.VOICE_LIST, service);
    Hashtable<String, String> parameters = buildParameters();
    parameters.put("service", service);
    if (category != null)
      parameters.put("category", category);
    return (list(getJSON(APIVerbs.VOICE_LIST, parameters)));
  }

  public VoiceCommandListVO list(String service)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (list(service, null));
  }

  public VoiceCommandListVO list()
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (list(null, null));
  }

  public VoiceCommandListVO list(ActionHandlerVO actionHandler)
    throws APIProtocolException, APICommunicationException, APIServerErrorException
  {
    return (list(doGetJSON(actionHandler.getHandlerURI())));
  }

  private VoiceCommandListVO list(JSONObject jsonObject)
    throws APIProtocolException, APIServerErrorException
  {
    VoiceCommandVO[] voiceCommandVOs;
    ItemVO[] itemVOs;
    if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
      {
        JSONArray jsonArray = getActionArrayJSON(jsonObject);
        itemVOs = new ItemVO[jsonArray.size()];
        for (int index = 0; index < itemVOs.length; index++)
          itemVOs[index] = new ItemVO((JSONObject) jsonArray.get(index));
        jsonArray = getDataArrayJSON(jsonObject);
        voiceCommandVOs = new VoiceCommandVO[jsonArray.size()];
        for (int index = 0; index < voiceCommandVOs.length; index++)
          voiceCommandVOs[index] = new VoiceCommandVO((JSONObject) ((JSONObject) jsonArray.get(index)).get("command"));
      }
    else
      {
        throw new APIServerErrorException(getStatusMessage(jsonObject));
      }
    return (new VoiceCommandListVO(itemVOs, voiceCommandVOs));
  }
}
