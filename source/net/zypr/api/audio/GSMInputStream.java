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

public class GSMInputStream
  extends InputStream
{
  public static final int DEFAULT_PIPE_SIZE = 4096;
  protected boolean _closedByWriter = false;
  protected volatile boolean _closedByReader = false;
  protected boolean _connected = false;
  protected Thread _readSideThread;
  protected Thread _writeSideThread;
  protected static final int PIPE_SIZE = DEFAULT_PIPE_SIZE;
  protected byte _buffer[];
  protected int _inBufferLenght = -1;
  protected int _outBufferLenght = 0;

  public GSMInputStream(GSMOutputStream gsmOutputStream)
    throws IOException
  {
    this(gsmOutputStream, DEFAULT_PIPE_SIZE);
  }

  public GSMInputStream(GSMOutputStream gsmOutputStream, int pipeSize)
    throws IOException
  {
    initPipe(pipeSize);
    connect(gsmOutputStream);
  }

  public GSMInputStream()
  {
    initPipe(DEFAULT_PIPE_SIZE);
  }

  public GSMInputStream(int pipeSize)
  {
    initPipe(pipeSize);
  }

  private void initPipe(int pipeSize)
  {
    if (pipeSize <= 0)
      throw new IllegalArgumentException("Pipe Size <= 0");
    _buffer = new byte[pipeSize];
  }

  public void connect(GSMOutputStream gsmOutputStream)
    throws IOException
  {
    gsmOutputStream.connect(this);
  }

  protected synchronized void receive(int value)
    throws IOException
  {
    checkStateForReceive();
    _writeSideThread = Thread.currentThread();
    if (_inBufferLenght == _outBufferLenght)
      awaitSpace();
    if (_inBufferLenght < 0)
      {
        _inBufferLenght = 0;
        _outBufferLenght = 0;
      }
    _buffer[_inBufferLenght++] = (byte) (value & 0xFF);
    if (_inBufferLenght >= _buffer.length)
      _inBufferLenght = 0;
  }

  synchronized void receive(byte[] bytes, int offset, int lenght)
    throws IOException
  {
    checkStateForReceive();
    _writeSideThread = Thread.currentThread();
    int bytesToTransfer = lenght;
    while (bytesToTransfer > 0)
      {
        if (_inBufferLenght == _outBufferLenght)
          awaitSpace();
        int nextTransferAmount = 0;
        if (_outBufferLenght < _inBufferLenght)
          {
            nextTransferAmount = _buffer.length - _inBufferLenght;
          }
        else if (_inBufferLenght < _outBufferLenght)
          {
            if (_inBufferLenght == -1)
              {
                _inBufferLenght = 0;
                _outBufferLenght = 0;
                nextTransferAmount = _buffer.length - _inBufferLenght;
              }
            else
              {
                nextTransferAmount = _outBufferLenght - _inBufferLenght;
              }
          }
        if (nextTransferAmount > bytesToTransfer)
          nextTransferAmount = bytesToTransfer;
        assert (nextTransferAmount > 0);
        System.arraycopy(bytes, offset, _buffer, _inBufferLenght, nextTransferAmount);
        bytesToTransfer -= nextTransferAmount;
        offset += nextTransferAmount;
        _inBufferLenght += nextTransferAmount;
        if (_inBufferLenght >= _buffer.length)
          _inBufferLenght = 0;
      }
  }

  private void checkStateForReceive()
    throws IOException
  {
    if (!_connected)
      throw new IOException("Pipe not _connected");
    else if (_closedByWriter || _closedByReader)
      throw new IOException("Pipe closed");
    else if (_readSideThread != null && !_readSideThread.isAlive())
      throw new IOException("Read end dead");
  }

  private void awaitSpace()
    throws IOException
  {
    while (_inBufferLenght == _outBufferLenght)
      {
        checkStateForReceive();
        notifyAll();
        try
          {
            wait(1000);
          }
        catch (InterruptedException ex)
          {
            throw new java.io.InterruptedIOException();
          }
      }
  }

  synchronized void receivedLast()
  {
    _closedByWriter = true;
    notifyAll();
  }

  public synchronized int read()
    throws IOException
  {
    if (!_connected)
      throw new IOException("Pipe not _connected");
    else if (_closedByReader)
      throw new IOException("Pipe closed");
    else if (_writeSideThread != null && !_writeSideThread.isAlive() && !_closedByWriter && (_inBufferLenght < 0))
      throw new IOException("Write end dead");
    _readSideThread = Thread.currentThread();
    int trials = 2;
    while (_inBufferLenght < 0)
      {
        if (_closedByWriter)
          return (-1);
        if ((_writeSideThread != null) && (!_writeSideThread.isAlive()) && (--trials < 0))
          throw new IOException("Pipe broken");
        notifyAll();
        try
          {
            wait(1000);
          }
        catch (InterruptedException ex)
          {
            throw new java.io.InterruptedIOException();
          }
      }
    int valueRead = _buffer[_outBufferLenght++] & 0xFF;
    if (_outBufferLenght >= _buffer.length)
      _outBufferLenght = 0;
    if (_inBufferLenght == _outBufferLenght)
      _inBufferLenght = -1;
    return (valueRead);
  }

  public synchronized int read(byte[] bytes, int offset, int lenght)
    throws IOException
  {
    if (bytes == null)
      throw new NullPointerException();
    else if (offset < 0 || lenght < 0 || lenght > bytes.length - offset)
      throw new IndexOutOfBoundsException();
    else if (lenght == 0)
      return (0);
    int valueRead = read();
    if (valueRead < 0)
      return (-1);
    bytes[offset] = (byte) valueRead;
    int readLenght = 1;
    while ((_inBufferLenght >= 0) && (lenght > 1))
      {
        int available;
        if (_inBufferLenght > _outBufferLenght)
          available = Math.min((_buffer.length - _outBufferLenght), (_inBufferLenght - _outBufferLenght));
        else
          available = _buffer.length - _outBufferLenght;
        if (available > (lenght - 1))
          available = lenght - 1;
        System.arraycopy(_buffer, _outBufferLenght, bytes, offset + readLenght, available);
        _outBufferLenght += available;
        readLenght += available;
        lenght -= available;
        if (_outBufferLenght >= _buffer.length)
          _outBufferLenght = 0;
        if (_inBufferLenght == _outBufferLenght)
          _inBufferLenght = -1;
      }
    return (readLenght);
  }

  public synchronized int available()
    throws IOException
  {
    if (_inBufferLenght < 0)
      return (0);
    else if (_inBufferLenght == _outBufferLenght)
      return (_buffer.length);
    else if (_inBufferLenght > _outBufferLenght)
      return (_inBufferLenght - _outBufferLenght);
    else
      return (_inBufferLenght + _buffer.length - _outBufferLenght);
  }

  public void close()
    throws IOException
  {
    _closedByReader = true;
    synchronized (this)
      {
        _inBufferLenght = -1;
      }
  }
}
