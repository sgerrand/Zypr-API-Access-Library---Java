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
  * This program is free software; you can redistribute it and/or modify it as long as this notice is kept with the code.
  *
  * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
  * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  *
  * This JAVA code is a close port of the code that is Copyright 1992 by Jutta Degener and Carsten Bormann, Technische
  * Universitaet Berlin.
  *
  */

package net.zypr.api.audio.gsm0610;

public class Lpc
{
  //******************************************
  // Local Utilities
  //******************************************

  private void Scale(Main GSMobj, short[] s, short n)
  {
    int k;
    for (k = 0; k <= 159; k++)
      s[k] = (short) Util.GSM_MULT_R(s[k], (short) (16384 >> (n - 1)));
  }
  //******************************************	
  /* 4.2.4 .. 4.2.7 LPC ANALYSIS SECTION */
  //******************************************
  /* 4.2.4 */

  public void Autocorrelation(Main GSMobj, short[] s, /* [0..159]	IN/OUT */
    long[] L_ACF) /* [0..8]	OUT */
    /*
 * The goal is to compute the array L_ACF[k]. The signal s[i] must
 * be scaled in order to avoid an overflow situation.
 */
  {
    int k;
    int i;
    short temp;
    short smax;
    short scalauto;
    /* Dynamic scaling of the array s[0..159] */
    /* Search for the maximum. */
    smax = 0;
    for (k = 0; k <= 159; k++)
      {
        temp = Util.GSM_ABS(s[k]);
        if (temp > smax)
          smax = temp;
      }
    /* Computation of the scaling factor. */
    if (smax == 0)
      scalauto = 0;
    else
      {
        assert (smax > 0);
        scalauto = (short) (4 - Util.gsm_norm((long) smax << 16)); /* sub(4,..) */
      }
    /* Scaling of the array s[0...159] */
    if (scalauto > 0)
      {
        switch (scalauto)
          {
            case 1:
              Scale(GSMobj, s, (short) 1);
              break;
            case 2:
              Scale(GSMobj, s, (short) 2);
              break;
            case 3:
              Scale(GSMobj, s, (short) 3);
              break;
            case 4:
              Scale(GSMobj, s, (short) 4);
              break;
          }
      }
    /* Compute the L_ACF[..]. */
    short[] sp = s;
    int slIdx = 0;
    short sl = sp[slIdx];
    //#	define STEP(k) L_ACF[k] += ((longword)sl * sp[ -(k) ]);
    //#	define NEXTI sl = *++sp
    // Clear array
    java.util.Arrays.fill(L_ACF, (short) 0);
    //STEP (0); NEXTI;
    L_ACF[0] += ((long) sl * sp[slIdx - (0)]);
    sl = sp[++slIdx];
    //STEP(0); STEP(1); NEXTI;
    L_ACF[0] += ((long) sl * sp[slIdx - (0)]);
    L_ACF[1] += ((long) sl * sp[slIdx - (1)]);
    sl = sp[++slIdx];
    // STEP(0); STEP(1); STEP(2); NEXTI;
    L_ACF[0] += ((long) sl * sp[slIdx - (0)]);
    L_ACF[1] += ((long) sl * sp[slIdx - (1)]);
    L_ACF[2] += ((long) sl * sp[slIdx - (2)]);
    sl = sp[++slIdx];
    // STEP(0); STEP(1); STEP(2); STEP(3);	NEXTI;
    L_ACF[0] += ((long) sl * sp[slIdx - (0)]);
    L_ACF[1] += ((long) sl * sp[slIdx - (1)]);
    L_ACF[2] += ((long) sl * sp[slIdx - (2)]);
    L_ACF[3] += ((long) sl * sp[slIdx - (3)]);
    sl = sp[++slIdx];
    // STEP(0); STEP(1); STEP(2); STEP(3); STEP(4); NEXTI;
    L_ACF[0] += ((long) sl * sp[slIdx - (0)]);
    L_ACF[1] += ((long) sl * sp[slIdx - (1)]);
    L_ACF[2] += ((long) sl * sp[slIdx - (2)]);
    L_ACF[3] += ((long) sl * sp[slIdx - (3)]);
    L_ACF[4] += ((long) sl * sp[slIdx - (4)]);
    sl = sp[++slIdx];
    // STEP(0); STEP(1); STEP(2); STEP(3); STEP(4); STEP(5); NEXTI;
    L_ACF[0] += ((long) sl * sp[slIdx - (0)]);
    L_ACF[1] += ((long) sl * sp[slIdx - (1)]);
    L_ACF[2] += ((long) sl * sp[slIdx - (2)]);
    L_ACF[3] += ((long) sl * sp[slIdx - (3)]);
    L_ACF[4] += ((long) sl * sp[slIdx - (4)]);
    L_ACF[5] += ((long) sl * sp[slIdx - (5)]);
    sl = sp[++slIdx];
    // STEP(0); STEP(1); STEP(2); STEP(3); STEP(4); STEP(5); STEP(6); NEXTI;
    L_ACF[0] += ((long) sl * sp[slIdx - (0)]);
    L_ACF[1] += ((long) sl * sp[slIdx - (1)]);
    L_ACF[2] += ((long) sl * sp[slIdx - (2)]);
    L_ACF[3] += ((long) sl * sp[slIdx - (3)]);
    L_ACF[4] += ((long) sl * sp[slIdx - (4)]);
    L_ACF[5] += ((long) sl * sp[slIdx - (5)]);
    L_ACF[6] += ((long) sl * sp[slIdx - (6)]);
    sl = sp[++slIdx];
    // STEP(0); STEP(1); STEP(2); STEP(3); STEP(4); STEP(5); STEP(6); STEP(7);
    L_ACF[0] += ((long) sl * sp[slIdx - (0)]);
    L_ACF[1] += ((long) sl * sp[slIdx - (1)]);
    L_ACF[2] += ((long) sl * sp[slIdx - (2)]);
    L_ACF[3] += ((long) sl * sp[slIdx - (3)]);
    L_ACF[4] += ((long) sl * sp[slIdx - (4)]);
    L_ACF[5] += ((long) sl * sp[slIdx - (5)]);
    L_ACF[6] += ((long) sl * sp[slIdx - (6)]);
    L_ACF[7] += ((long) sl * sp[slIdx - (7)]);
    for (i = 8; i <= 159; i++)
      {
        // NEXTI; STEP(0); STEP(1); STEP(2); STEP(3); STEP(4); STEP(5); STEP(6); STEP(7); STEP(8);
        sl = sp[++slIdx];
        L_ACF[0] += ((long) sl * sp[slIdx - (0)]);
        L_ACF[1] += ((long) sl * sp[slIdx - (1)]);
        L_ACF[2] += ((long) sl * sp[slIdx - (2)]);
        L_ACF[3] += ((long) sl * sp[slIdx - (3)]);
        L_ACF[4] += ((long) sl * sp[slIdx - (4)]);
        L_ACF[5] += ((long) sl * sp[slIdx - (5)]);
        L_ACF[6] += ((long) sl * sp[slIdx - (6)]);
        L_ACF[7] += ((long) sl * sp[slIdx - (7)]);
        L_ACF[8] += ((long) sl * sp[slIdx - (8)]);
      }
    for (k = 0; k < 9; k++)
      L_ACF[k] <<= 1;
    /* Rescaling of the array s[0..159] */
    if (scalauto > 0)
      {
        assert (scalauto <= 4);
        for (k = 0; k < 160; k++)
          s[k] <<= scalauto;
      }
  }
  /* 4.2.5 */

  public void Reflection_coefficients(Main GSMobj, long[] L_ACF, /* 0...8	IN	*/
    short[] r) /* 0...7	OUT 	*/
  {
    int i, m, n;
    short temp;
    short[] ACF = new short[9]; /* 0..8 */
    short[] P = new short[9]; /* 0..8 */
    short[] K = new short[9]; /* 2..8 */
    /* Schur recursion with 16 bits arithmetic.
 */
    if (L_ACF[0] == 0)
      {
        for (i = 0; i < 8; i++)
          r[i] = 0;
        return;
      }
    temp = Util.gsm_norm(L_ACF[0]);
    assert (temp >= 0 && temp < 32);
    /* ? overflow ? */
    for (i = 0; i <= 8; i++)
      ACF[i] = (short) Util.SASR_L(L_ACF[i] << temp, (short) 16);
    /* Initialize array P[..] and K[..] for the recursion. */
    for (i = 1; i <= 7; i++)
      K[i] = ACF[i];
    for (i = 0; i <= 8; i++)
      P[i] = ACF[i];
    /* Compute reflection coefficients */
    int rIdx = 0;
    for (n = 1; n <= 8; n++, rIdx++)
      {
        temp = P[1];
        temp = Util.GSM_ABS(temp);
        if (P[0] < temp)
          {
            for (i = n; i <= 8; i++)
              r[rIdx++] = 0;
            return;
          }
        r[rIdx] = Util.gsm_div(temp, P[0]);
        // DEBUG assert(r[rIdx] >= 0);
        if (P[1] > 0)
          r[rIdx] = (short) -r[rIdx]; /* r[n] = sub(0, r[n]) */
        // DEBUG assert (*r != MIN_WORD);
        if (n == 8)
          return;
        /* Schur recursion
 */
        temp = (short) Util.GSM_MULT_R(P[1], r[rIdx]);
        P[0] = (short) Util.GSM_ADD(P[0], temp);
        for (m = 1; m <= 8 - n; m++)
          {
            temp = (short) Util.GSM_MULT_R(K[m], r[rIdx]);
            P[m] = (short) Util.GSM_ADD(P[m + 1], temp);
            temp = (short) Util.GSM_MULT_R(P[m + 1], r[rIdx]);
            K[m] = (short) Util.GSM_ADD(K[m], temp);
          }
      }
  }
  /* 4.2.6 */

  public void Transformation_to_Log_Area_Ratios(Main GSMobj, short[] r) /* 0..7 IN/OUT */
    /*
 * The following scaling for r[..] and LAR[..] has been used:
 *
 * r[..] = integer( real_r[..]*32768. ); -1 <= real_r < 1.
 * LAR[..] = integer( real_LAR[..] * 16384 );
 * with -1.625 <= real_LAR <= 1.625
 */
  {
    short temp;
    int i;
    int rIdx = 0;
    /* Computation of the LAR[0..7] from the r[0..7]
 */
    for (i = 1; i <= 8; i++, rIdx++)
      {
        temp = r[rIdx];
        temp = (short) Util.GSM_ABS(temp);
        assert (temp >= 0);
        if (temp < 22118)
          {
            temp >>= 1;
          }
        else if (temp < 31130)
          {
            assert (temp >= 11059);
            temp -= 11059;
          }
        else
          {
            assert (temp >= 26112);
            temp -= 26112;
            temp <<= 2;
          }
        r[rIdx] = (short) ((r[rIdx] < 0) ? -temp : temp);
        // DEBUG assert( r[rIdx] != MIN_WORD );
      }
  }
  /* 4.2.7 */

  public void Quantization_and_coding(Main GSMobj, short[] LAR) /* [0..7]	IN/OUT	*/
  {
    short LARIdx = 0;
    /* This procedure needs four tables; the following equations
 * give the optimum scaling for the constants:
 *
 * A[0..7] = integer( real_A[0..7] * 1024 )
 * B[0..7] = integer( real_B[0..7] * 512 )
 * MAC[0..7] = maximum of the LARc[0..7]
 * MIC[0..7] = minimum of the LARc[0..7]
 */
    QCStep(GSMobj, LAR, LARIdx++, (short) 20480, (short) 0, (short) 31, (short) -32);
    QCStep(GSMobj, LAR, LARIdx++, (short) 20480, (short) 0, (short) 31, (short) -32);
    QCStep(GSMobj, LAR, LARIdx++, (short) 20480, (short) 2048, (short) 15, (short) -16);
    QCStep(GSMobj, LAR, LARIdx++, (short) 20480, (short) -2560, (short) 15, (short) -16);
    QCStep(GSMobj, LAR, LARIdx++, (short) 13964, (short) 94, (short) 7, (short) -8);
    QCStep(GSMobj, LAR, LARIdx++, (short) 15360, (short) -1792, (short) 7, (short) -8);
    QCStep(GSMobj, LAR, LARIdx++, (short) 8534, (short) -341, (short) 3, (short) -4);
    QCStep(GSMobj, LAR, LARIdx++, (short) 9036, (short) -1144, (short) 3, (short) -4);
  }

  private void QCStep(Main GSMobj, short[] LAR, /* [0..7]	IN/OUT	*/
    short LARIdx, short A, short B, short MAC, short MIC)
  {
    short temp;
    temp = (short) Util.GSM_MULT(A, LAR[LARIdx]);
    temp = (short) Util.GSM_ADD(temp, B);
    temp = (short) Util.GSM_ADD(temp, (short) 256);
    temp = (short) Util.SASR_W(temp, (short) 9);
    LAR[LARIdx] = (short) ((temp > MAC) ? (MAC - MIC) : ((temp < MIC) ? 0 : (temp - MIC)));
  }

  public void Gsm_LPC_Analysis(Main GSMobj, short[] s, /* 0..159 signals	IN/OUT	*/
    short[] LARc) /* 0..7 LARc's	OUT	*/
  {
    long[] L_ACF = new long[9];
    Autocorrelation(GSMobj, s, L_ACF);
    Reflection_coefficients(GSMobj, L_ACF, LARc);
    Transformation_to_Log_Area_Ratios(GSMobj, LARc);
    Quantization_and_coding(GSMobj, LARc);
  }
}
