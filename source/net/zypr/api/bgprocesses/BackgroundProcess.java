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


package net.zypr.api.bgprocesses;

public class BackgroundProcess
  extends Thread
{
  protected boolean _running = true;
  protected int _interval = 30;
  protected boolean _forceUpdate = false;

  public BackgroundProcess()
  {
    super();
    setName(this.getClass().getCanonicalName());
  }

  public BackgroundProcess(int interval)
  {
    super();
    setName(this.getClass().getCanonicalName());
    setInterval(interval);
  }

  public void setRunning(boolean running)
  {
    _running = running;
  }

  public boolean isRunning()
  {
    return (_running);
  }

  public void setInterval(int interval)
  {
    _interval = interval;
  }

  public int getInterval()
  {
    return (_interval);
  }

  public void forceUpdate()
  {
    _forceUpdate = true;
  }

  protected void waitForInterval()
    throws InterruptedException
  {
    for (int repeat = 0; repeat < _interval & !_forceUpdate; repeat++)
      sleep(1000);
    _forceUpdate = false;
  }
}
