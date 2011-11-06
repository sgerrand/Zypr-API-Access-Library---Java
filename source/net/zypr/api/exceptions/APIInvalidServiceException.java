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


package net.zypr.api.exceptions;

import net.zypr.api.enums.APIVerbs;

public class APIInvalidServiceException
  extends APIRuntimeException
{
  public APIInvalidServiceException(Throwable throwable)
  {
    super(throwable);
  }

  public APIInvalidServiceException(String string, Throwable throwable)
  {
    super(string, throwable);
  }

  public APIInvalidServiceException(APIVerbs apiVerb, String service)
  {
    super(service != null ? "Access to " + apiVerb.toPath() + " is not allowed for the service \"" + service + "\"." : "Service is null therefore access to " + apiVerb.toPath() + " is not allowed.");
  }

  public APIInvalidServiceException(String string)
  {
    super(string);
  }

  public APIInvalidServiceException()
  {
    super();
  }
}
