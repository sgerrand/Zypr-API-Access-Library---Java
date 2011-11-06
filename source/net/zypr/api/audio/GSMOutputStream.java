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


package net.zypr.api.audio;

import java.io.IOException;
import java.io.OutputStream;

import net.zypr.api.audio.gsm0610.DetectClick;
import net.zypr.api.audio.gsm0610.Main;

public class GSMOutputStream
  extends OutputStream
{
  private static final int CLICK_DETECTOR_INDEX = 1600;
  private static final int SOURCE_BUFFER_SIZE = 160;
  private static final boolean USE_CLICK_DETECT = true;
  private static final int GSM_FRAME_SIZE = 33;
  private Main _gsmEncoder = new Main();
  private DetectClick _clickDetector = new DetectClick(CLICK_DETECTOR_INDEX);
  private short[] _sourceBuffer = new short[SOURCE_BUFFER_SIZE];
  private int _sourceBufferIndex = 0;
  private boolean _isUnderFlow = false;
  private byte _underFlowByte;
  private boolean _isFirstFrame = true;
  private byte[] _gsmFrame = new byte[GSM_FRAME_SIZE];
  private GSMInputStream _gsmInputStream;

  public GSMOutputStream(GSMInputStream gsmInputStream)
    throws IOException
  {
    connect(gsmInputStream);
  }

  public GSMOutputStream()
  {
  }

  public synchronized void connect(GSMInputStream gsmInputStream)
    throws IOException
  {
    if (gsmInputStream == null)
      throw new NullPointerException();
    else if (_gsmInputStream != null || gsmInputStream._connected)
      throw new IOException("Already connected");
    _gsmInputStream = gsmInputStream;
    gsmInputStream._inBufferLenght = -1;
    gsmInputStream._outBufferLenght = 0;
    gsmInputStream._connected = true;
  }

  public void write(int value)
    throws IOException
  {
    if (_gsmInputStream == null)
      throw new IOException("Pipe not connected");
    _gsmInputStream.receive(value);
  }

  public void write(byte[] bytes, int offset, int lenght)
    throws IOException
  {
    if (_gsmInputStream == null)
      throw new IOException("Pipe not connected");
    else if (bytes == null)
      throw new NullPointerException();
    else if ((offset < 0) || (offset > bytes.length) || (lenght < 0) || ((offset + lenght) > bytes.length) || ((offset + lenght) < 0))
      throw new IndexOutOfBoundsException();
    else if (lenght == 0)
      return;
    if (_isFirstFrame && (offset == 0) && (lenght > 20))
      {
        _isFirstFrame = false;
        offset = 20;
      }
    while (offset < lenght)
      {
        if (_sourceBufferIndex == SOURCE_BUFFER_SIZE)
          {
            DetectClick.Status audioClickStatus = DetectClick.Status.VALID_AUDIO_DETECTED;
            if (USE_CLICK_DETECT)
              audioClickStatus = _clickDetector.FindClick(_sourceBuffer, SOURCE_BUFFER_SIZE);
            if (audioClickStatus == DetectClick.Status.VALID_AUDIO_DETECTED)
              {
                _gsmEncoder.Encode(_sourceBuffer, _gsmFrame);
                _gsmInputStream.receive(_gsmFrame, 0, _gsmFrame.length);
              }
            _sourceBufferIndex = 0;
          }
        if (_isUnderFlow)
          {
            _sourceBuffer[_sourceBufferIndex++] = (short) (((short) (_underFlowByte) << 8) + (short) (bytes[offset++] & 0xff));
            _isUnderFlow = false;
          }
        else if ((offset + 1) < lenght)
          {
            _sourceBuffer[_sourceBufferIndex++] = (short) (((short) (bytes[offset]) << 8) + (short) (bytes[offset + 1] & 0xff));
            offset += 2;
          }
        else
          {
            _underFlowByte = bytes[offset++];
            _isUnderFlow = true;
          }
      }
  }

  public synchronized void flush()
    throws IOException
  {
    if (_gsmInputStream != null)
      synchronized (_gsmInputStream)
        {
          _gsmInputStream.notifyAll();
        }
  }

  public void close()
    throws IOException
  {
    if (_gsmInputStream != null)
      _gsmInputStream.receivedLast();
  }
}
