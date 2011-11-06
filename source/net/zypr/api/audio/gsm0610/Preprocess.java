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

public class Preprocess
{
  //**********************************************************
  // process
  //**********************************************************

  public void process(Main GSMobj, short[] Source, // Audio sample
    short[] Sout) // [0..159] IM / OUT
  {
    /*	4.2.0 .. 4.2.3	PREPROCESSING SECTION
 *
 * 	After A-law to linear conversion (or directly from the
 * 	A to D converter) the following scaling is assumed for
 * input to the RPE-LTP algorithm:
 *
 * in: 0.1.....................12
 * S.v.v.v.v.v.v.v.v.v.v.v.v.*.*.*
 *
 *	Where S is the sign bit, v a valid bit, and * a "don't care" bit.
 * 	The original signal is called sop[..]
 *
 * out: 0.1................... 12
 * S.S.v.v.v.v.v.v.v.v.v.v.v.v.0.0
 */
    short z1 = GSMobj.z1;
    ;
    long L_z2 = GSMobj.L_z2;
    int mp = GSMobj.mp;
    short s1;
    long L_s2;
    long L_temp;
    short msp;
    short lsp;
    short SO;
    int k = 160;
    int sourceIdx = 0;
    int soutIdx = 0;
    while (k-- > 0)
      {
        /* 4.2.1 Downscaling of the input signal */
        SO = (short) (Util.SASR_W(Source[sourceIdx], (short) 3) << 2);
        sourceIdx++;
        // DEBUG	assert (SO >= -0x4000);	/* downscaled by */
        // DEBUG	assert (SO <= 0x3FFC);	/* previous routine. */
        /* 4.2.2 Offset compensation
 *
 * This part implements a high-pass filter and requires extended
 * arithmetic precision for the recursive part of this filter.
 * The input of this procedure is the array so[0...159] and the
 * output the array sof[ 0...159 ].
 */
        /* Compute the non-recursive part */
        s1 = (short) (SO - z1); /* s1 = gsm_sub( *so, z1 ); */
        z1 = SO;
        // DEBUG assert(s1 != MIN_WORD);
        /* Compute the recursive part
 */
        L_s2 = s1;
        L_s2 <<= 15;
        /* Execution of a 31 bv 16 bits multiplication
 */
        msp = (short) Util.SASR_L(L_z2, (short) 15);
        lsp = (short) (L_z2 - ((long) msp << 15)); /* gsm_L_sub(L_z2,(msp<<15)); */
        L_s2 += Util.GSM_MULT_R(lsp, (short) 32735);
        L_temp = (long) ((long) msp * (long) 32735); /* GSM_L_MULT(msp,32735) >> 1;*/
        L_z2 = Util.GSM_L_ADD(L_temp, L_s2);
        /* Compute sof[k] with rounding */
        L_temp = Util.GSM_L_ADD(L_z2, 16384);
        /* 4.2.3 Preemphasis */
        msp = (short) Util.GSM_MULT_R((short) mp, (short) -28180);
        mp = (int) Util.SASR_L(L_temp, (short) 15);
        Sout[soutIdx++] = (short) Util.GSM_ADD((short) mp, msp);
      }
    GSMobj.z1 = z1;
    GSMobj.L_z2 = L_z2;
    GSMobj.mp = mp;
  }
}
