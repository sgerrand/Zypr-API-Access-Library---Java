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

import java.beans.PropertyChangeSupport;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;

import net.zypr.api.audio.GSMInputStream;
import net.zypr.api.enums.VoiceCommandContextState;
import net.zypr.api.enums.VoiceCommandState;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class VoiceCommandDataVO
  extends GenericVO
{
  private File _audioFile = new File(System.getProperty("user.home") + File.separator + ".zypr" + File.separator + "zypr.audio");
  private GSMInputStream _inputStream = null;
  private String _audioFormat = "wav";
  private VoiceCommandState _mainState = VoiceCommandState.MAIN;
  private ArrayList<VoiceCommandContextState> _contextStates = new ArrayList<VoiceCommandContextState>();
  private ArrayList<ContextDataVO> _contextData = new ArrayList<ContextDataVO>();
  private GeoPointVO _location;
  private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public VoiceCommandDataVO(GeoPointVO location)
  {
    super();
    setLocation(location);
  }

  public VoiceCommandDataVO(File audioFile, GeoPointVO location, VoiceCommandState mainState)
  {
    super();
    setAudioFile(audioFile);
    setLocation(location);
    setMainState(mainState);
  }

  public VoiceCommandDataVO(GeoPointVO location, VoiceCommandState mainState)
  {
    super();
    setLocation(location);
    setMainState(mainState);
  }

  public VoiceCommandDataVO(GeoPointVO location, String audioFormat, VoiceCommandState mainState)
  {
    super();
    setLocation(location);
    setAudioFormat(audioFormat);
    setMainState(mainState);
  }

  public VoiceCommandDataVO(GeoPointVO location, String audioFormat, VoiceCommandState mainState, ArrayList<VoiceCommandContextState> optionalStates, ArrayList<ContextDataVO> contextData)
  {
    super();
    setLocation(location);
    setAudioFormat(audioFormat);
    setMainState(mainState);
    setContextStates(optionalStates);
    setContextData(contextData);
  }

  public VoiceCommandDataVO(GeoPointVO location, String audioFormat, VoiceCommandState mainState, VoiceCommandContextState[] optionalStates, ContextDataVO[] contextData)
  {
    super();
    setLocation(location);
    setAudioFormat(audioFormat);
    setMainState(mainState);
    setContextStates(optionalStates);
    setContextData(contextData);
  }

  public VoiceCommandDataVO(GSMInputStream outputStream, GeoPointVO location)
  {
    super();
    setInputStream(outputStream);
    setLocation(location);
  }

  public VoiceCommandDataVO(GSMInputStream outputStream, GeoPointVO location, VoiceCommandState mainState)
  {
    super();
    setInputStream(outputStream);
    setLocation(location);
    setMainState(mainState);
  }

  public VoiceCommandDataVO(GSMInputStream outputStream, GeoPointVO location, String audioFormat, VoiceCommandState mainState)
  {
    super();
    setInputStream(outputStream);
    setLocation(location);
    setAudioFormat(audioFormat);
    setMainState(mainState);
  }

  public VoiceCommandDataVO(GSMInputStream outputStream, GeoPointVO location, String audioFormat, VoiceCommandState mainState, ArrayList<VoiceCommandContextState> optionalStates, ArrayList<ContextDataVO> contextData)
  {
    super();
    setInputStream(outputStream);
    setLocation(location);
    setAudioFormat(audioFormat);
    setMainState(mainState);
    setContextStates(optionalStates);
    setContextData(contextData);
  }

  public VoiceCommandDataVO(GSMInputStream outputStream, GeoPointVO location, String audioFormat, VoiceCommandState mainState, VoiceCommandContextState[] optionalStates, ContextDataVO[] contextData)
  {
    super();
    setInputStream(outputStream);
    setLocation(location);
    setAudioFormat(audioFormat);
    setMainState(mainState);
    setContextStates(optionalStates);
    setContextData(contextData);
  }

  public void setAudioFile(File audioFile)
  {
    _audioFile = audioFile;
    setAudioFormat(audioFile.getName().substring(audioFile.getName().lastIndexOf(".") + 1).toLowerCase());
  }

  public File getAudioFile()
  {
    return (_audioFile);
  }

  public void setAudioFormat(String audioFormat)
  {
    _audioFormat = audioFormat;
  }

  public String getAudioFormat()
  {
    return (_audioFormat);
  }

  public void setMainState(VoiceCommandState mainState)
  {
    _mainState = mainState;
  }

  public VoiceCommandState getMainState()
  {
    return (_mainState);
  }

  public void setContextStates(ArrayList<VoiceCommandContextState> optionalStates)
  {
    _contextStates = optionalStates;
  }

  public void setContextStates(VoiceCommandContextState[] optionalStates)
  {
    _contextStates.addAll(Arrays.asList(optionalStates));
  }

  public boolean addContextState(VoiceCommandContextState optionalState)
  {
    return (_contextStates.add(optionalState));
  }

  public boolean removeContextState(VoiceCommandState optionalState)
  {
    return (_contextStates.remove(optionalState));
  }

  public VoiceCommandContextState removeContextState(int index)
  {
    return (_contextStates.remove(index));
  }

  public ArrayList<VoiceCommandContextState> getContextStates()
  {
    return (_contextStates);
  }

  public VoiceCommandContextState[] getContextStatesAsArray()
  {
    return (_contextStates.toArray(new VoiceCommandContextState[0]));
  }

  public void setContextData(ArrayList<ContextDataVO> contextData)
  {
    _contextData = contextData;
  }

  public void setContextData(ContextDataVO[] contextData)
  {
    _contextData.addAll(Arrays.asList(contextData));
  }

  public boolean addContextData(String name, String id)
  {
    return (_contextData.add(new ContextDataVO(name, id)));
  }

  public boolean addContextData(ContextDataVO contextData)
  {
    return (_contextData.add(contextData));
  }

  public boolean removeContextData(String name, String id)
  {
    return (_contextData.add(new ContextDataVO(name, id)));
  }

  public boolean removeContextData(ContextDataVO contextData)
  {
    return (_contextData.remove(contextData));
  }

  public ContextDataVO removeContextData(int index)
  {
    return (_contextData.remove(index));
  }

  public ArrayList<ContextDataVO> getContextData()
  {
    return (_contextData);
  }

  public void setLocation(GeoPointVO location)
  {
    _location = location;
  }

  public GeoPointVO getLocation()
  {
    return (_location);
  }

  public void setInputStream(GSMInputStream outputStream)
  {
    _inputStream = outputStream;
  }

  public GSMInputStream getInputStream()
  {
    return _inputStream;
  }

  public JSONObject toJSON()
  {
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "audio_format");
    jsonObject.put("value", _audioFormat);
    jsonArray.add(jsonObject);
    jsonObject = null;
    jsonObject = new JSONObject();
    jsonObject.put("name", "main_state");
    jsonObject.put("value", _mainState.toString());
    jsonArray.add(jsonObject);
    jsonObject = null;
    String optionalStates = "";
    for (int index = 0; index < _contextStates.size(); index++)
      optionalStates += (index != 0 ? "," : "") + _contextStates.get(index);
    jsonObject = new JSONObject();
    jsonObject.put("name", "optional_state");
    jsonObject.put("value", optionalStates);
    jsonArray.add(jsonObject);
    jsonObject = null;
    jsonObject = new JSONObject();
    jsonObject.put("name", "location");
    jsonObject.put("value", _location.toJSON());
    jsonArray.add(jsonObject);
    jsonObject = null;
    jsonObject = new JSONObject();
    jsonObject.put("name", "generic_context_data");
    JSONArray jsonArrayContextData = new JSONArray();
    for (int index = 0; index < _contextData.size(); index++)
      jsonArrayContextData.add(_contextData.get(index).toJSON());
    jsonObject.put("value", jsonArrayContextData);
    jsonArray.add(jsonObject);
    jsonObject = null;
    jsonObject = new JSONObject();
    jsonObject.put("data", jsonArray);
    return (jsonObject);
  }
}
