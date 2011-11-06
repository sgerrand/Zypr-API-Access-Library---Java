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

import net.zypr.api.enums.SilenceDetectStatus;

public class SilenceDetect
{
  // RUNTIME TUNING
  private final int RUNNING_AVG_FRAMES;
  private final int SILENT_DETECT_FRAMES;
  private final int SPEAKING_SNRx10;
  private final int MINIMUM_SPEECH_VALUE;
  private final int FREQUENCY;

  public SilenceDetect()
  {
    super();
    RUNNING_AVG_FRAMES = 5;
    SILENT_DETECT_FRAMES = 5;
    SPEAKING_SNRx10 = 17;
    MINIMUM_SPEECH_VALUE = 2500;
    FREQUENCY = 16;
    RunningAvgData = new short[RUNNING_AVG_FRAMES];
    Init(3000);
  }

  public SilenceDetect(int timeout, int RUNNING_AVG_FRAMES, int SILENT_DETECT_FRAMES, int SPEAKING_SNRx10, int MINIMUM_SPEECH_VALUE, int FREQUENCY)
  {
    super();
    this.RUNNING_AVG_FRAMES = RUNNING_AVG_FRAMES;
    this.SILENT_DETECT_FRAMES = SILENT_DETECT_FRAMES;
    this.SPEAKING_SNRx10 = SPEAKING_SNRx10;
    this.MINIMUM_SPEECH_VALUE = MINIMUM_SPEECH_VALUE;
    this.FREQUENCY = FREQUENCY;
    RunningAvgData = new short[RUNNING_AVG_FRAMES];
    Init(timeout);
  }
  // Current Data
  private short FrameHighVal;
  // Running Average calculator
  private short RunningAvg;
  private short RunningAvgIdx;
  private short RunningAvgInitFlag; // Set to 1 after init
  private short[] RunningAvgData;
  // Silence detect
  private short SilentFrameCount;
  private short Low;
  private short HighThreshold;
  private short High;
  private SilenceDetectStatus CurrentStatus;
  private int NumPkts;
  private int TimeoutPkts;
  // *************************************************************
  // Find High Value
  // *************************************************************

  private int FindHighValue(short[] PCMBuffer, int numPkts)
  {
    int HighValue = 0;
    int i;
    // First OR all data together
    for (i = 0; i < numPkts; i++)
      {
        // Only care about the positive side-band
        if ((PCMBuffer[i] > 0) && (PCMBuffer[i] > HighValue))
          HighValue = PCMBuffer[i];
      }
    return HighValue;
  }
  // *************************************************************
  // Update Running Average
  // *************************************************************

  private short UpdateRunningAverage(int newValue)
  {
    int i;
    // See if first time
    if (RunningAvgInitFlag == 0)
      {
        for (i = 0; i < RUNNING_AVG_FRAMES; i++)
          RunningAvgData[i] = (short) newValue;
        RunningAvg = (short) newValue;
        RunningAvgInitFlag = 1;
        RunningAvgIdx = 1;
      }
    else
      {
        // Add new value to running average
        RunningAvgData[RunningAvgIdx] = (short) newValue;
        if (++RunningAvgIdx >= RUNNING_AVG_FRAMES)
          RunningAvgIdx = 0;
        // Compute new running average
        RunningAvg = RunningAvgData[0];
        for (i = 1; i < RUNNING_AVG_FRAMES; i++)
          RunningAvg = (short) ((RunningAvg + RunningAvgData[i]) >> 1);
      }
    return RunningAvg;
  }
  // *************************************************************
  // Init
  //
  // You must call this method before each silence detect session.
  //
  // param: TimeOut Set to number or milliseconds. If no speech
  // or silence is detected in this time then a
  // "Timeout" status will be returened by "Process".
  // Set this parameter to 0 to not check for Timeout.
  // *************************************************************

  public void Init(int TimeOut)
  {
    FrameHighVal = 0;
    RunningAvg = 0;
    RunningAvgIdx = 0;
    RunningAvgInitFlag = 0;
    SilentFrameCount = 0;
    Low = 0;
    HighThreshold = 0;
    High = 0;
    NumPkts = 0;
    CurrentStatus = SilenceDetectStatus.NoSpeech;
    for (int i = 0; i < RUNNING_AVG_FRAMES; i++)
      RunningAvgData[i] = 0;
    // Compute max frames before timeout
    TimeoutPkts = (FREQUENCY * TimeOut);
  }
  // *************************************************************
  // Silence Detect
  // *************************************************************

  public SilenceDetectStatus Process(short[] PCMBuffer, int numPkts)
  {
    // Find High data
    FrameHighVal = (short) FindHighValue(PCMBuffer, numPkts);
    // See if need to ingore first frame Click
    if ((Low == 0) && (FrameHighVal > 8000))
      return SilenceDetectStatus.NoSpeech;
    // Update the running Average
    UpdateRunningAverage(FrameHighVal);
    do
      {
        // See if set Low value
        if (Low == 0)
          {
            Low = RunningAvg;
            HighThreshold = (short) ((RunningAvg * SPEAKING_SNRx10) / 10);
            if (HighThreshold < MINIMUM_SPEECH_VALUE)
              HighThreshold = (short) MINIMUM_SPEECH_VALUE;
            CurrentStatus = SilenceDetectStatus.NoSpeech;
            break;
          }
        // See if set high value
        if (High == 0)
          {
            if (RunningAvg > HighThreshold)
              {
                High = RunningAvg;
                CurrentStatus = SilenceDetectStatus.Speech;
              }
            break;
          }
        // Check for silences
        if (RunningAvg < HighThreshold)
          {
            if (++SilentFrameCount > SILENT_DETECT_FRAMES)
              CurrentStatus = SilenceDetectStatus.SilenceDetected;
          }
        else
          {
            SilentFrameCount = 0;
          }
      }
    while (false);
    // Check for timeout
    NumPkts += numPkts;
    if ((TimeoutPkts > 0) && (CurrentStatus != SilenceDetectStatus.SilenceDetected) && (NumPkts > TimeoutPkts))
      {
        CurrentStatus = SilenceDetectStatus.Timeout;
      }
    return CurrentStatus;
  }
}
