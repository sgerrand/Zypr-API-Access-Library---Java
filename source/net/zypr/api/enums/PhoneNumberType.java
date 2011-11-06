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

public enum PhoneNumberType
{
  WORK("work"),
  HOME("home"),
  MAIN("main"),
  CELL("cell"),
  OTHER("other"),
  MOBILE("mobile"),
  MOBILE_MAIN("mobile main"),
  MOBILE_WORK("mobile work"),
  MOBILE_HOME("mobile home"),
  MOBILE_OTHER("mobile other"),
  LAND_LINE("land line"),
  LAND_LINE_MAIN("land line main"),
  LAND_LINE_WORK("land line work"),
  LAND_LINE_HOME("land line home"),
  LAND_LINE_OTHER("land line other"),
  VOIP("voip"),
  VOIP_MAIN("voip main"),
  VOIP_WORK("voip work"),
  VOIP_HOME("voip home"),
  VOIP_OTHER("voip other"),
  SIP("sip"),
  SIP_MAIN("sip main"),
  SIP_WORK("sip work"),
  SIP_HOME("sip home"),
  SIP_OTHER("sip other"),
  FAX("fax"),
  FAX_MAIN("fax main"),
  FAX_WORK("fax work"),
  FAX_HOME("fax home"),
  FAX_OTHER("fax other"),
  VIDEO_CALL("video call"),
  VIDEO_CALL_MAIN("video call main"),
  VIDEO_CALL_WORK("video call work"),
  VIDEO_CALL_HOME("video call home"),
  VIDEO_CALL_OTHER("video call other");
  private String _value;

  PhoneNumberType(String value)
  {
    _value = value;
  }

  public String getValue()
  {
    return (_value);
  }
}
