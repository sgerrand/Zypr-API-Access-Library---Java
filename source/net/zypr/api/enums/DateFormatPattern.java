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

public enum DateFormatPattern
{
  YYYYMMDDDOT("yyyy.MM.dd"),
  YYYYMMDDSLASH("yyyy/MM/dd"),
  YYYYMMDDDASH("yyyy-MM-dd"),
  YYYYMMDDCOLON("yyyy:MM:dd"),
  MMDDYYYYDOT("MM.dd.yyyy"),
  MMDDYYYYSLASH("MM/dd/yyyy"),
  MMDDYYYYDASH("MM-dd-yyyy"),
  MMDDYYYYCOLON("MM:dd:yyyy"),
  DDMMYYYYDOT("dd.MM.yyyy"),
  DDMMYYYYSLASH("dd/MM/yyyy"),
  DDMMYYYYDASH("dd-MM-yyyy"),
  DDMMYYYYCOLON("dd:MM:yyyy");
  private String _pattern;

  DateFormatPattern(String pattern)
  {
    _pattern = pattern;
  }

  public String getPattern()
  {
    return (_pattern);
  }
}
