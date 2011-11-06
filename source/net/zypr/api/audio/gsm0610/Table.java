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


/*
 * GSM 06.10 JAVA
 * Copyright (C) 2010 Alan K. Gordon
 *
 * This program is free software; you can redistribute it and/or
 * modify it as long as this notice is kept with the code.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 *
 * This JAVA code is a close port of the code that is
 * Copyright 1992 by Jutta Degener and Carsten Bormann, Technische
 * Universitaet Berlin. See the accompanying file "Copyright1992.txt"
 * for the original Copyright file for the original C code.
 */
package net.zypr.api.audio.gsm0610;

public class Table
{
  /* Table 4.1 Quantization of the Log.-Area Ratios */
  public static short[] gsm_A = new short[]
    { 20480, 20480, 20480, 20480, 13964, 15360, 8534, 9036 };
  public static short[] gsm_B = new short[]
    { 0, 0, 2048, -2560, 94, -1792, -341, -1144 };
  public static short[] gsm_MIC = new short[]
    { -32, -32, -16, -16, -8, -8, -4, -4 };
  public static short[] gsm_MAC = new short[]
    { 31, 31, 15, 15, 7, 7, 3, 3 };
  /* Table 4.2 Tabulation of 1/A[1..8] */
  public static short[] gsm_INVA = new short[]
    { 13107, 13107, 13107, 13107, 19223, 17476, 31454, 29708 };
  /* Table 4.3a Decision level of the LTP gain quantizer */
  /* bc 0 1 2 3			*/
  public static short[] gsm_DLB = new short[]
    { 6554, 16384, 26214, 32767 };
  /* Table 4.3b Quantization levels of the LTP gain quantizer */
  /* bc 0 1 2 3			*/
  public static short[] gsm_QLB = new short[]
    { 3277, 11469, 21299, 32767 };
  /* Table 4.4 Coefficients of the weighting filter */
  /* i 0 1 2 3 4 5 6 7 8 9 10 */
  public static short[] gsm_H = new short[]
    { -134, -374, 0, 2054, 5741, 8192, 5741, 2054, 0, -374, -134 };
  /* Table 4.5 Normalized inverse mantissa used to compute xM/xmax */
  /* i 	0 1 2 3 4 5 6 7 */
  public static short[] gsm_NRFAC = new short[]
    { 29128, 26215, 23832, 21846, 20165, 18725, 17476, 16384 };
  /* Table 4.6 Normalized direct mantissa used to compute xM/xmax */
  /* i 0 1 2 3 4 5 6 7 */
  public static short[] gsm_FAC = new short[]
    { 18431, 20479, 22527, 24575, 26623, 28671, 30719, 32767 };
}
