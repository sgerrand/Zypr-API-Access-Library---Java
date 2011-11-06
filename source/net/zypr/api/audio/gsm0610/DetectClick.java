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


package net.zypr.api.audio.gsm0610;

import net.zypr.api.utils.Debug;
//***************************************
//  Use this class to detect a "click"
//  sound that is the mouse or button noise
//  when starting audio recording.
//
//  A "click" is defined as any noise that is
//  "x" times greater in magnitude then the ambient
//  noise that occures before speech begins and
//  is of short duration.
//
//  Methodology: Keep a running average of the
//  sample data and look for a region that is
//  "x" times larger then the average and not
//  any longer then the maximum duration. Then
//  wait for "n" samples of back to average data
//  before declairing it a "click".
//
//                          *** ***
//         **             ***********   <--------- "x" times ambient (clickFact)
//  *****************************************  <-- ambient level
//         **             ***********
//                          *** ***
//
//  |...|                               <--- initial window to establish ambient nosie value (preClick)
//        |..|                          <--- maximum click length (clickWindow)
//           |.........|                <--- minimum wait time to asume it is a click (clickWait)
//                        |..........|  <--- actual good audio data
//
//  It is assumed that the first few packets will not contain a click (else that is codec noise)
//
//  Parameters:
//    preClick      - Time at start of audio to establish a noise floor.
//    clickWindow   - Maximum Size in packets for a click
//    clickFact     - factor over average that detects the presence of a click
//    clickWait     - minimum packets to wait after detection of click to determin if
//                      was click of just start of valid audio.
//    clickMinValue - this is the minimum value used to detect a click. This is
//                      for the case of "too quite" and ambient is near zero.
//****************************************
public class DetectClick
{
  //********************************************
  //  controls  (set for 8000Hz sampling
  //********************************************
  private static final int preClick = 160; // set to 0.02 seconds
  private static final int clickWindow = 800; // set to 0.1 seconds
  private static final int clickFact = 2; // set to 2x ambient
  private static final int clickWait = 1600; // set to 0.2 seconds
  private static final int clickMinValue = 1000;
  private static final boolean DEBUG_PRINT = false;
  //********************************************
  //  Session parameters
  //********************************************

  public enum Status
  {
    NO_CLICK, // Initial state
    POSSIBLE_CLICK, // Found noise and not determined if click or valid data yet.
    CLICK_DETECTED, // A click has been detected
    VALID_AUDIO_DETECTED; // No click detected
  }
  private Status clickStatus;
  private int clickIdx;
  private int clickSize;
  private int sampleIdx;
  private int ambientValue;
  private int triggerValue;
  private int maxTotalIdx;

  public DetectClick()
  {
    Init(0);
  }

  public DetectClick(int maxIdx)
  {
    Init(maxIdx);
  }
  //********************************************
  //  Initilize for new click detection session.
  //
  //  Set maxIdx to the maximum overall sample index
  //  to scan and then if no click found, always
  //  return status of VALID_AUDIO_DATA from FindCLick.
  //  use value of 0 for no limit.
  //********************************************

  public void Init(int maxIdx)
  {
    clickStatus = Status.NO_CLICK;
    clickIdx = 0;
    clickSize = 0;
    ambientValue = 0;
    sampleIdx = 0;
    triggerValue = 0;
    maxTotalIdx = maxIdx;
  }

  public void Init()
  {
    Init(0);
  }
  //********************************************
  //  Get click info
  //********************************************

  public Status GetStatus()
  {
    return clickStatus;
  }
  // Return the index in the audio stream where a
  // click starts. This index is absolute from the
  // beginning of the inital audio sample and not
  // relative to the current sample.
  // Since the first part of audio is used to establish
  // a noise floor, this value can not be zero if a click
  // has been detected. If zero then no click has been found.

  public int GetClickIdx()
  {
    return clickIdx;
  }
  // Return the size of the click is packets.
  // If no click has been found this value will be zero.

  public int GetCLickSize()
  {
    return clickSize;
  }
  //*******************************************
  // Find a "click" in the given sample.
  //
  // The sample will be scanned from beginning
  // until "endIdx" or end of sample.
  //
  //*******************************************

  public Status FindClick(short[] sample, int endIdx)
  {
    // Check to see if click has been detected previously. If so then
    // assume all after is valid audio
    if (clickStatus == Status.CLICK_DETECTED)
      {
        clickStatus = Status.VALID_AUDIO_DETECTED;
        if (DEBUG_PRINT)
          Debug.print("Audio found after click in audio.\n");
      }
    // Only look for click once. So if found or valid audio found then just return
    if (clickStatus != Status.VALID_AUDIO_DETECTED)
      {
        // Find maximum length bounded by length of sample array and
        // endIdx;
        int maxIdx = sample.length - 1;
        if (endIdx < maxIdx)
          maxIdx = endIdx;
        // Scan the sample
        int idx;
        for (idx = 0; idx <= maxIdx; idx++)
          {
            // Check for exceeded max total samples
            if ((maxTotalIdx > 0) && (sampleIdx >= maxTotalIdx))
              {
                clickStatus = Status.VALID_AUDIO_DETECTED;
                if (DEBUG_PRINT)
                  Debug.print("No click found at beginning of audio.\n");
                break;
              }
            // Get absolute value of current sample
            int currentSample = (sample[idx] >= 0) ? sample[idx] : -sample[idx];
            // Test against ambient value if past inital packets
            if (sampleIdx > preClick)
              {
                if (currentSample > triggerValue)
                  {
                    switch (clickStatus)
                      {
                          // See if start of click
                        case NO_CLICK:
                          clickStatus = Status.POSSIBLE_CLICK;
                          clickIdx = sampleIdx;
                          if (DEBUG_PRINT)
                            Debug.print("POSSIBLE_CLICK found in audio at index=" + sampleIdx + "\n");
                          break;
                          // If already detect click - check that not to long
                        case POSSIBLE_CLICK:
                          clickSize = sampleIdx - clickIdx;
                          if (clickSize > clickWindow)
                            {
                              clickStatus = Status.VALID_AUDIO_DETECTED;
                              if (DEBUG_PRINT)
                                Debug.print("VALID_AUDIO_DETECTED found in audio at index=" + sampleIdx + "\n");
                            }
                          break;
                      }
                  }
                else
                  {
                    // Test to see if looking for minimum time after a click
                    if (clickStatus == Status.POSSIBLE_CLICK)
                      {
                        int afterClick = sampleIdx - (clickIdx + clickSize);
                        if (afterClick > clickWait)
                          {
                            clickStatus = Status.CLICK_DETECTED;
                            if (DEBUG_PRINT)
                              Debug.print("CLICK_DETECTED found in audio at index=" + sampleIdx + "\n");
                          }
                      }
                  }
              }
            else
              {
                // Compute the running average
                ambientValue = (currentSample + ((sampleIdx) * ambientValue)) / (sampleIdx + 1);
                // Keep updating trigger value so ready when finished ambient calculation
                triggerValue = ambientValue * clickFact;
                if (triggerValue < clickMinValue)
                  triggerValue = clickMinValue;
              }
            // Advance the overall sample index
            sampleIdx++;
          }
      }
    return clickStatus;
  }
}
