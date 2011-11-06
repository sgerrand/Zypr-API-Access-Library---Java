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
/*
 * 4.2 FIXED POINT IMPLEMENTATION OF THE RPE-LTP CODER
 */
public class LongTerm
{
  /*
 * 4.2.11 .. 4.2.12 LONG TERM PREDICTOR (LTP) SECTION
 */
  /*
 * This module computes the LTP gain (bc) and the LTP lag (Nc)
 * for the long term analysis filter. This is done by calculating a
 * maximum of the cross-correlation function between the current
 * sub-segment short term residual signal d[0..39] (output of
 * the short term analysis filter; for simplification the index
 * of this array begins at 0 and ends at 39 for each sub-segment of the
 * RPE-LTP analysis) and the previous reconstructed short term
 * residual signal dp[ -120 .. -1 ]. A dynamic scaling must be
 * performed to avoid overflow.
 */
  /* The next procedure exists in six versions. First two integer
 * version (if USE_FLOAT_MUL is not defined); then four floating
 * point versions, twice with proper scaling (USE_FLOAT_MUL defined),
 * once without (USE_FLOAT_MUL and FAST defined, and fast run-time
 * option used). Every pair has first a Cut version (see the -C
 * option to toast or the LTP_CUT option to gsm_option()), then the
 * uncut one. (For a detailed explanation of why this is altogether
 * a bad idea, see Henry Spencer and Geoff Collyer, ``#ifdef Considered
 * Harmful''.)
 */

  static private void Calculation_of_the_LTP_parameters(Main GSMobj, short[] d, /* [0..39]	IN	*/
    int dIdx, int dpIdx, /* [-120..-1]	IN	*/
    int bcIdx, /* 		OUT	*/
    int NcIdx) /* 		OUT	*/
  {
    int k, lambda;
    short Nc, bc;
    short[] wt = new short[40];
    long L_max, L_power;
    short R, S, dmax, scal;
    short temp;
    /* Search of the optimum scaling of d[0..39].
 */
    dmax = 0;
    for (k = 0; k <= 39; k++)
      {
        temp = d[dIdx + k];
        temp = Util.GSM_ABS(temp);
        if (temp > dmax)
          dmax = temp;
      }
    temp = 0;
    if (dmax == 0)
      scal = 0;
    else
      {
        // DEBUG assert(dmax > 0);
        temp = Util.gsm_norm((long) dmax << 16);
      }
    if (temp > 6)
      scal = 0;
    else
      scal = (short) (6 - temp);
    // DEBUG assert(scal >= 0);
    /* Initialization of a working array wt
 */
    for (k = 0; k <= 39; k++)
      wt[k] = Util.SASR_W(d[dIdx + k], scal);
    /* Search for the maximum cross-correlation and coding of the LTP lag
 */
    L_max = 0;
    Nc = 40; /* index for the maximum cross-correlation */
    for (lambda = 40; lambda <= 120; lambda++)
      {
        long L_result = (long) wt[0] * GSMobj.dp0[dpIdx + 0 - lambda];
        for (k = 1; k < 40; k++)
          L_result += (long) wt[k] * GSMobj.dp0[dpIdx + k - lambda];
        if (L_result > L_max)
          {
            Nc = (short) lambda;
            L_max = L_result;
          }
      }
    GSMobj.Nc[NcIdx] = Nc;
    L_max <<= 1;
    /* Rescaling of L_max
 */
    // DEBUG assert(scal <= 100 && scal >= -100);
    L_max = L_max >> (6 - scal); /* sub(6, scal) */
    // DEBUG assert( Nc <= 120 && Nc >= 40);
    /* Compute the power of the reconstructed short term residual
 * signal dp[..]
 */
    L_power = 0;
    for (k = 0; k <= 39; k++)
      {
        long L_temp;
        L_temp = Util.SASR_W(GSMobj.dp0[dpIdx + k - Nc], (short) 3);
        L_power += L_temp * L_temp;
      }
    L_power <<= 1; /* from L_MULT */
    /* Normalization of L_max and L_power
 */
    if (L_max <= 0)
      {
        GSMobj.bc[bcIdx] = 0;
        return;
      }
    if (L_max >= L_power)
      {
        GSMobj.bc[bcIdx] = 3;
        return;
      }
    temp = Util.gsm_norm(L_power);
    R = (short) Util.SASR_L(L_max << temp, (short) 16);
    S = (short) Util.SASR_L(L_power << temp, (short) 16);
    /* Coding of the LTP gain
 */
    /* Table 4.3a must be used to obtain the level DLB[i] for the
 * quantization of the LTP gain b to get the coded version bc.
 */
    for (bc = 0; bc <= 2; bc++)
      {
        if (R <= Util.gsm_mult(S, Table.gsm_DLB[bc]))
          break;
      }
    GSMobj.bc[bcIdx] = bc;
  }
  /* 4.2.12 */
  private static short[] BP = new short[]
    { 3277, 11469, 21299, 32767 };

  private static void Long_term_analysis_filtering(Main GSMobj, int bcIdx, /* 					IN */
    int NcIdx, /* 					IN */
    int dpIdx, /* previous d	[-120..-1]		IN */
    short[] d, /* d		[0..39]			IN */
    int dIdx, int dppIdx, /* estimate	[0..39]			OUT */
    int eIdx) /* long term res. signal [0..39]	OUT */
    /*
 * In this part, we have to decode the bc parameter to compute
 * the samples of the estimate dpp[0..39]. The decoding of bc needs the
 * use of table 4.3b. The long term residual signal e[0..39]
 * is then calculated to be fed to the RPE encoding section.
 */
  {
    short BPVal = BP[GSMobj.bc[bcIdx]];
    short NcVal = GSMobj.Nc[NcIdx];
    for (int k = 0; k <= 39; k++)
      {
        GSMobj.dp0[dpIdx + k] = (short) Util.GSM_MULT_R(BPVal, GSMobj.dp0[dpIdx + k - NcVal]);
        GSMobj.e[eIdx + k] = (short) Util.GSM_SUB(d[dIdx + k], GSMobj.dp0[dppIdx + k]);
      }
  }

  public void Gsm_Long_Term_Predictor( /* 4x for 160 samples */
    Main GSMobj, short[] d, /* d [0..39] residual signal IN	*/
    int dIdx, /* d Index */
    int dpIdx, /* dp Index [-120..-1] IN	*/
    int eIdx, /* e Index [0..39] OUT	*/
    int dppIdx, /* dp0 Index [0..39] OUT */
    int NcIdx, /* Nc Index OUT */
    int bcIdx) /* bc Index OUT */
  {
    Calculation_of_the_LTP_parameters(GSMobj, d, dIdx, dpIdx, bcIdx, NcIdx);
    Long_term_analysis_filtering(GSMobj, bcIdx, NcIdx, dpIdx, d, dIdx, dppIdx, eIdx);
  }
  /* 4.3.2 */

  void Gsm_Long_Term_Synthesis_Filtering(Main GSMobj, short Ncr, short bcr, short[] erp, /* [0..39] IN */
    short[] drp) /* [-120..-1] IN, [-120..40] OUT */
    /*
 * This procedure uses the bcr and Ncr parameter to realize the
 * long term synthesis filtering. The decoding of bcr needs
 * table 4.3b.
 */
  {
    int k;
    short brp, drpp, Nr;
    /* Check the limits of Nr.
 */
    Nr = Ncr < 40 || Ncr > 120 ? GSMobj.nrp : Ncr;
    GSMobj.nrp = Nr;
    // DEBUG assert(Nr >= 40 && Nr <= 120);
    /* Decoding of the LTP gain bcr
 */
    brp = Table.gsm_QLB[bcr];
    /* Computation of the reconstructed short term residual
 * signal drp[0..39]
 */
    // DEBUG assert(brp != MIN_WORD);
    for (k = 0; k <= 39; k++)
      {
        drpp = (short) Util.GSM_MULT_R(brp, drp[k - Nr]);
        drp[k] = (short) Util.GSM_ADD(erp[k], drpp);
      }
    /*
 * Update of the reconstructed short term residual signal
 * drp[ -1..-120 ]
 */
    for (k = 0; k <= 119; k++)
      drp[-120 + k] = drp[-80 + k];
  }
}
