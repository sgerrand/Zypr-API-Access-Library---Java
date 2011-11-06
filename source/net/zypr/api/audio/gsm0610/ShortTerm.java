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

public class ShortTerm
{

  static private short STEP(short B, short MIC, short INVA, short LARc)
  {
    short temp;
    temp = (short) (Util.GSM_ADD(LARc, MIC) << 10);
    temp = (short) Util.GSM_SUB(temp, (short) (B << 1));
    temp = (short) Util.GSM_MULT_R(INVA, temp);
    return (short) Util.GSM_ADD(temp, temp);
  }
  /*
 * SHORT TERM ANALYSIS FILTERING SECTION
 */
  /* 4.2.8 */

  static public void Decoding_of_the_coded_Log_Area_Ratios(short[] LARc, /* coded log area ratio	[0..7] 	IN	*/
    short[][] LARpp, /* out: decoded ..			*/
    int x)
  {
    /* This procedure requires for efficient implementation
 * two tables.
 *
 * INVA[1..8] = integer( (32768 * 8) / real_A[1..8])
 * MIC[1..8] = minimum value of the LARc[1..8]
 */
    /* Compute the LARpp[1..8]
 */
    /* 	for (i = 1; i <= 8; i++, B++, MIC++, INVA++, LARc++, LARpp++) {
 *
 *		temp1 = GSM_ADD( *LARc, *MIC ) << 10;
 *		temp2 = *B << 1;
 *		temp1 = GSM_SUB( temp1, temp2 );
 *
 *		assert(*INVA != MIN_WORD);
 *
 *		temp1 = GSM_MULT_R( *INVA, temp1 );
 *		*LARpp = GSM_ADD( temp1, temp1 );
 *	}
 */
    int LARcIdx = 0;
    int LARppIdx = 0;
    LARpp[x][LARppIdx++] = STEP((short) 0, (short) -32, (short) 13107, LARc[LARcIdx++]);
    LARpp[x][LARppIdx++] = STEP((short) 0, (short) -32, (short) 13107, LARc[LARcIdx++]);
    LARpp[x][LARppIdx++] = STEP((short) 2048, (short) -16, (short) 13107, LARc[LARcIdx++]);
    LARpp[x][LARppIdx++] = STEP((short) -2560, (short) -16, (short) 13107, LARc[LARcIdx++]);
    LARpp[x][LARppIdx++] = STEP((short) 94, (short) -8, (short) 19223, LARc[LARcIdx++]);
    LARpp[x][LARppIdx++] = STEP((short) -1792, (short) -8, (short) 17476, LARc[LARcIdx++]);
    LARpp[x][LARppIdx++] = STEP((short) -341, (short) -4, (short) 31454, LARc[LARcIdx++]);
    LARpp[x][LARppIdx++] = STEP((short) -1144, (short) -4, (short) 29708, LARc[LARcIdx++]);
    /* NOTE: the addition of *MIC is used to restore
 * the sign of *LARc.
 */
  }
  /* 4.2.9 */
  /* Computation of the quantized reflection coefficients
 */
  /* 4.2.9.1 Interpolation of the LARpp[1..8] to get the LARp[1..8]
 */
  /*
 * Within each frame of 160 analyzed speech samples the short term
 * analysis and synthesis filters operate with four different sets of
 * coefficients, derived from the previous set of decoded LARs(LARpp(j-1))
 * and the actual set of decoded LARs (LARpp(j))
 *
 * (Initial value: LARpp(j-1)[1..8] = 0.)
 */

  static private void Coefficients_0_12(short LARpp_j_1, short LARpp_j, short[][] LARpp, short[] LARp)
  {
    int i;
    for (i = 0; i < 8; i++)
      {
        LARp[i] = (short) Util.GSM_ADD(Util.SASR_W(LARpp[LARpp_j_1][i], (short) 2), Util.SASR_W(LARpp[LARpp_j][i], (short) 2));
        LARp[i] = (short) Util.GSM_ADD(LARp[i], Util.SASR_W(LARpp[LARpp_j_1][i], (short) 1));
      }
  }

  static private void Coefficients_13_26(short LARpp_j_1, short LARpp_j, short[][] LARpp, short[] LARp)
  {
    int i;
    for (i = 0; i < 8; i++)
      {
        LARp[i] = (short) Util.GSM_ADD(Util.SASR_W(LARpp[LARpp_j_1][i], (short) 1), Util.SASR_W(LARpp[LARpp_j][i], (short) 1));
      }
  }

  static private void Coefficients_27_39(short LARpp_j_1, short LARpp_j, short[][] LARpp, short[] LARp)
  {
    int i;
    for (i = 0; i < 8; i++)
      {
        LARp[i] = (short) Util.GSM_ADD(Util.SASR_W(LARpp[LARpp_j_1][i], (short) 2), Util.SASR_W(LARpp[LARpp_j][i], (short) 2));
        LARp[i] = (short) Util.GSM_ADD(LARp[i], Util.SASR_W(LARpp[LARpp_j][i], (short) 1));
      }
  }

  static private void Coefficients_40_159(short LARpp_j, short[][] LARpp, short[] LARp)
  {
    int i;
    for (i = 0; i < 8; i++)
      LARp[i] = LARpp[LARpp_j][i];
  }
  /* 4.2.9.2 */

  static private void LARp_to_rp(short[] LARp) /* [0..7] IN/OUT */
    /*
 * The input of this procedure is the interpolated LARp[0..7] array.
 * The reflection coefficients, rp[i], are used in the analysis
 * filter and in the synthesis filter.
 */
  {
    int i;
    short temp;
    for (i = 0; i < 8; i++)
      {
        /* temp = GSM_ABS( *LARp );
 *
 * if (temp < 11059) temp <<= 1;
 * else if (temp < 20070) temp += 11059;
 * else temp = GSM_ADD( temp >> 2, 26112 );
 *
 * *LARp = *LARp < 0 ? -temp : temp;
 */
        if (LARp[i] < 0)
          {
            temp = (short) (LARp[i] == Util.MIN_WORD ? Util.MAX_WORD : -(LARp[i]));
            if (temp < 11059)
              {
                LARp[i] = (short) -(temp << 1);
              }
            else if (temp < 20070)
              {
                LARp[i] = (short) -(temp + 11059);
              }
            else
              {
                LARp[i] = (short) -Util.GSM_ADD((short) (temp >> 2), (short) 26112);
              }
          }
        else
          {
            temp = LARp[i];
            if (temp < 11059)
              {
                LARp[i] = (short) (temp << 1);
              }
            else if (temp < 20070)
              {
                LARp[i] = (short) (temp + 11059);
              }
            else
              {
                LARp[i] = (short) Util.GSM_ADD((short) (temp >> 2), (short) 26112);
              }
          }
      }
  }
  /* 4.2.10 */

  static private void Short_term_analysis_filtering(Main S, short[] rp, /* [0..7]	IN	*/
    int k_n, /* k_end - k_start	*/
    short[] s, /* [0..n-1]	IN/OUT	*/
    int sIdx)
    /*
 * This procedure computes the short term residual signal d[..] to be fed
 * to the RPE-LTP loop from the s[..] signal and from the local rp[..]
 * array (quantized reflection coefficients). As the call of this
 * procedure can be done in many ways (see the interpolation of the LAR
 * coefficient), it is assumed that the computation begins with index
 * k_start (for arrays d[..] and s[..]) and stops with index k_end
 * (k_start and k_end are defined in 4.2.9.1). This procedure also
 * needs to keep the array u[0..7] in memory for each call.
 */
  {
    int i;
    short di, zzz, ui, sav, rpi;
    //int		sIdx = 0;
    for (; (k_n--) > 0; sIdx++)
      {
        di = sav = s[sIdx];
        for (i = 0; i < 8; i++)
          { /* YYY */
            ui = S.u[i];
            rpi = rp[i];
            S.u[i] = sav;
            zzz = (short) Util.GSM_MULT_R(rpi, di);
            sav = (short) Util.GSM_ADD(ui, zzz);
            zzz = (short) Util.GSM_MULT_R(rpi, ui);
            di = (short) Util.GSM_ADD(di, zzz);
          }
        s[sIdx] = di;
      }
  }

  static private void Short_term_synthesis_filtering(Main S, short[] rrp, /* [0..7]	IN	*/
    int k, /* k_end - k_start	*/
    short[] wt, /* [0..k-1]	IN	*/
    int wtIdx, short[] sr, /* [0..k-1]	OUT	*/
    int srIdx)
  {
    //short		* v = S->v;
    int i;
    short sri, tmp1, tmp2;
    //int srIdx = 0;
    //int		wtIdx = 0;
    while ((k--) > 0)
      {
        sri = wt[wtIdx++];
        for (i = 8; (i--) > 0; )
          {
            /* sri = GSM_SUB( sri, gsm_mult_r( rrp[i], v[i] ) );
	 */
            tmp1 = rrp[i];
            tmp2 = S.v[i];
            if ((tmp1 == Util.MIN_WORD) && (tmp2 == Util.MIN_WORD))
              tmp2 = Util.MAX_WORD;
            else
              tmp2 = (short) (0x0FFFF & (((long) tmp1 * (long) tmp2 + 16384) >> 15));
            sri = (short) Util.GSM_SUB(sri, tmp2);
            /* v[i+1] = GSM_ADD( v[i], gsm_mult_r( rrp[i], sri ) );
	 */
            if ((tmp1 == Util.MIN_WORD) && (sri == Util.MIN_WORD))
              tmp1 = Util.MAX_WORD;
            else
              tmp1 = (short) (0x0FFFF & (((long) tmp1 * (long) sri + 16384) >> 15));
            S.v[i + 1] = (short) Util.GSM_ADD(S.v[i], tmp1);
          }
        sr[srIdx++] = S.v[0] = sri;
      }
  }

  void Gsm_Short_Term_Analysis_Filter(Main S, short[] LARc, /* coded log area ratio [0..7] IN	*/
    short[] s) /* signal [0..159]		IN/OUT	*/
  {
    short LARpp_j = S.j;
    short LARpp_j_1 = S.j ^= 1;
    short[] LARp = new short[8];
    Decoding_of_the_coded_Log_Area_Ratios(LARc, S.LARpp, LARpp_j);
    Coefficients_0_12(LARpp_j_1, LARpp_j, S.LARpp, LARp);
    LARp_to_rp(LARp);
    Short_term_analysis_filtering(S, LARp, 13, s, 0);
    Coefficients_13_26(LARpp_j_1, LARpp_j, S.LARpp, LARp);
    LARp_to_rp(LARp);
    Short_term_analysis_filtering(S, LARp, 14, s, 13);
    Coefficients_27_39(LARpp_j_1, LARpp_j, S.LARpp, LARp);
    LARp_to_rp(LARp);
    Short_term_analysis_filtering(S, LARp, 13, s, 27);
    Coefficients_40_159(LARpp_j, S.LARpp, LARp);
    LARp_to_rp(LARp);
    Short_term_analysis_filtering(S, LARp, 120, s, 40);
  }

  void Gsm_Short_Term_Synthesis_Filter(Main S, short[] LARcr, /* received log area ratios [0..7] IN */
    short[] wt, /* received d [0..159] IN */
    short[] s) /* signal s [0..159] OUT */
  {
    short LARpp_j = S.j;
    short LARpp_j_1 = S.j ^= 1;
    short[] LARp = new short[8];
    Decoding_of_the_coded_Log_Area_Ratios(LARcr, S.LARpp, LARpp_j);
    Coefficients_0_12(LARpp_j_1, LARpp_j, S.LARpp, LARp);
    LARp_to_rp(LARp);
    Short_term_synthesis_filtering(S, LARp, 13, wt, 0, s, 0);
    Coefficients_13_26(LARpp_j_1, LARpp_j, S.LARpp, LARp);
    LARp_to_rp(LARp);
    Short_term_synthesis_filtering(S, LARp, 14, wt, 13, s, 13);
    Coefficients_27_39(LARpp_j_1, LARpp_j, S.LARpp, LARp);
    LARp_to_rp(LARp);
    Short_term_synthesis_filtering(S, LARp, 13, wt, 27, s, 27);
    Coefficients_40_159(LARpp_j, S.LARpp, LARp);
    LARp_to_rp(LARp);
    Short_term_synthesis_filtering(S, LARp, 120, wt, 40, s, 40);
  }
}
