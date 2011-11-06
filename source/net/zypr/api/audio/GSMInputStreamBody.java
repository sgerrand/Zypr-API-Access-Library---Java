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

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;

public class GSMInputStreamBody
  extends AbstractContentBody
{
  private final static int BUFFER_SIZE = 4096;
  private final InputStream _gsmInputStream;
  private final String _filename;

  public GSMInputStreamBody(final InputStream gsmInputStream, final String mimeType, final String filename)
  {
    super(mimeType);
    if (gsmInputStream == null)
      throw new IllegalArgumentException("Input stream may not be null");
    _gsmInputStream = gsmInputStream;
    _filename = filename;
  }

  public GSMInputStreamBody(final InputStream gsmInputStream, final String filename)
  {
    this(gsmInputStream, "application/octet-stream", filename);
  }

  public InputStream getInputStream()
  {
    return (_gsmInputStream);
  }

  /**
   * @deprecated use {@link #writeTo(OutputStream)}
   */
  @Deprecated
  public void writeTo(final OutputStream outputStream, int mode)
    throws IOException
  {
    writeTo(outputStream);
  }

  public void writeTo(final OutputStream outputStream)
    throws IOException
  {
    if (outputStream == null)
      throw new IllegalArgumentException("Output stream may not be null");
    try
      {
        byte[] buffer = new byte[BUFFER_SIZE];
        int readLenght;
        while ((readLenght = _gsmInputStream.read(buffer)) != -1)
          outputStream.write(buffer, 0, readLenght);
        outputStream.flush();
      }
    finally
      {
        _gsmInputStream.close();
      }
  }

  public String getTransferEncoding()
  {
    return (MIME.ENC_BINARY);
  }

  public String getCharset()
  {
    return (null);
  }

  public long getContentLength()
  {
    return (-1);
  }

  public String getFilename()
  {
    return (_filename);
  }
}
