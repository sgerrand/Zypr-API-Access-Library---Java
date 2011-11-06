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

public class Debug
{
  private static final boolean ENABLED = true;

  public static void log(String name, String message)
  {
    print(name + " : " + message);
  }

  public static void displayStack(Object object, Throwable throwable)
  {
    if (!ENABLED)
      return;
    displayStack(object, throwable, 9999);
  }

  public static void displayStack(Object object, Throwable throwable, int depth)
  {
    if (!ENABLED)
      return;
    StringBuffer stringBuffer = new StringBuffer("*** ERROR STACK TRACE INFORMATION FOR DEBUGGING AT ");
    stringBuffer.append(System.currentTimeMillis());
    stringBuffer.append(" ");
    if (object != null)
      {
        stringBuffer.append("IN ");
        stringBuffer.append(object.getClass().getCanonicalName());
      }
    stringBuffer.append(" ***\n");
    stringBuffer.append(throwable.getClass().getCanonicalName());
    stringBuffer.append(": ");
    stringBuffer.append(throwable.getMessage());
    stringBuffer.append("\n");
    StackTraceElement[] stackElements = throwable.getStackTrace();
    for (int index = 0; index < stackElements.length && index < depth; index++)
      {
        stringBuffer.append("at ");
        stringBuffer.append(stackElements[index].toString());
        stringBuffer.append("\n");
      }
    System.err.println(stringBuffer.toString());
    if (throwable.getCause() != null)
      displayStack(object, throwable.getCause(), depth);
  }

  public static void displayWarning(Object object, String message)
  {
    if (!ENABLED)
      return;
    StringBuffer stringBuffer = new StringBuffer("*** WARNING FOR DEBUGGING AT ");
    stringBuffer.append(System.currentTimeMillis());
    stringBuffer.append(" ");
    if (object != null)
      {
        stringBuffer.append("IN ");
        stringBuffer.append(object.getClass().getCanonicalName());
      }
    stringBuffer.append(" ***\n");
    stringBuffer.append(message);
    stringBuffer.append("\n");
    System.err.println(stringBuffer.toString());
  }

  public static void displayInfo(Object object, String message)
  {
    if (!ENABLED)
      return;
    StringBuffer stringBuffer = new StringBuffer("*** INFO FOR DEBUGGING AT ");
    stringBuffer.append(System.currentTimeMillis());
    stringBuffer.append(" ");
    if (object != null)
      {
        stringBuffer.append("IN ");
        stringBuffer.append(object.getClass().getCanonicalName());
      }
    stringBuffer.append(" ***\n");
    stringBuffer.append(message);
    stringBuffer.append("\n");
    System.err.println(stringBuffer.toString());
  }

  public static void print(String message)
  {
    if (!ENABLED)
      return;
    System.out.println(message);
  }

  public static void print(Object object)
  {
    if (!ENABLED)
      return;
    System.out.println("" + object);
  }
}
