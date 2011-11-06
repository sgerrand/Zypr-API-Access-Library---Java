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


package net.zypr.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class DateUtils
{
  private static SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
  private static SimpleDateFormat RFC822 = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z");

  public static Date parseRFC822String(String dateString)
    throws ParseException
  {
    return (RFC822.parse(dateString));
  }

  public static String getRFC822String(Date date)
  {
    return (RFC822.format(date));
  }

  public static Date parseISO8601String(String dateString)
    throws ParseException
  {
    if (dateString == null)
      return (null);
    if (!dateString.endsWith("Z"))
      {
        int lastColon = dateString.lastIndexOf(":");
        dateString = dateString.substring(0, lastColon) + dateString.substring(lastColon + 1);
      }
    return (ISO8601.parse(dateString));
  }

  public static String getISO8601String(Date date)
  {
    String result = ISO8601.format(date);
    result = result.substring(0, result.length() - 2) + ":" + result.substring(result.length() - 2);
    return (result);
  }
}
