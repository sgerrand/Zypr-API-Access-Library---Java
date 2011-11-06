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
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.AbstractHttpEntity;

public class GSMInputStreamEntity
  extends AbstractHttpEntity
{
  private final static int BUFFER_SIZE = 2048;
  private final GSMInputStream _gsmInputStream;
  private final long _length;

  public GSMInputStreamEntity(final GSMInputStream gsmInputStream, long length)
  {
    super();
    if (gsmInputStream == null)
      throw new IllegalArgumentException("Source input stream may not be null");
    _gsmInputStream = gsmInputStream;
    _length = length;
  }

  public boolean isRepeatable()
  {
    return (false);
  }

  public long getContentLength()
  {
    return (_length);
  }

  public InputStream getContent()
    throws IOException
  {
    return (_gsmInputStream);
  }

  public void writeTo(final OutputStream outputStream)
    throws IOException
  {
    if (outputStream == null)
      throw new IllegalArgumentException("Output stream may not be null");
    byte[] buffer = new byte[BUFFER_SIZE];
    int readLenght;
    if (_length < 0)
      {
        while ((readLenght = _gsmInputStream.read(buffer)) != -1)
          outputStream.write(buffer, 0, readLenght);
      }
    else
      {
        long remainingLenght = _length;
        while (remainingLenght > 0)
          {
            readLenght = _gsmInputStream.read(buffer, 0, (int) Math.min(BUFFER_SIZE, remainingLenght));
            if (readLenght == -1)
              break;
            outputStream.write(buffer, 0, readLenght);
            remainingLenght -= readLenght;
          }
      }
  }

  public boolean isStreaming()
  {
    return (true);
  }
}
