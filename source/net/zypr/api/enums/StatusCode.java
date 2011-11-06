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

public enum StatusCode
{
  UNKNOWN(0),
  SUCCESSFUL(200),
  PARTIAL_CONTENT(202),
  FAILED(401),
  NOT_FOUND(404);
  private long _value;

  StatusCode(long value)
  {
    _value = value;
  }

  public long getValue()
  {
    return (_value);
  }

  public static StatusCode valueOf(long value)
  {
    StatusCode[] statusCodes = StatusCode.values();
    for (int index = 0; index < statusCodes.length; index++)
      if (statusCodes[index].getValue() == value)
        return (statusCodes[index]);
    return (StatusCode.UNKNOWN);
  }
}
