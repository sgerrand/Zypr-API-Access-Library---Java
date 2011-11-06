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

public class NumberUtils
{
  public static double roundToDecimals(double doubleValue)
  {
    return (roundToDecimals(doubleValue, 6));
  }

  public static double roundToDecimals(double doubleValue, int decimalPlaces)
  {
    int intValue = (int) ((doubleValue * Math.pow(10, decimalPlaces)));
    return (((double) intValue) / Math.pow(10, decimalPlaces));
  }
}
