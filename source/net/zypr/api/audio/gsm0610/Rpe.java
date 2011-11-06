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

public class Rpe
{
  /* 4.2.13 .. 4.2.17 RPE ENCODING SECTION
 */
  /* 4.2.13 */

  static private void Weighting_filter(Main GSMobj, int eIdx, /* signal [-5..0.39.44]	IN */
    short[] x) /* signal [0..39]	OUT */
    /*
 * The coefficients of the weighting filter are stored in a table
 * (see table 4.4). The following scaling is used:
 *
 *	H[0..10] = integer( real_H[ 0..10] * 8192 );
 */
  {
    /* short			wt[ 50 ]; */
    long L_result;
    int k; /* , i */
    /* Initialization of a temporary working array wt[0...49]
 */
    /* for (k = 0; k <= 4; k++) wt[k] = 0;
 * for (k = 5; k <= 44; k++) wt[k] = *e++;
 * for (k = 45; k <= 49; k++) wt[k] = 0;
 *
 * (e[-5..-1] and e[40..44] are allocated by the caller,
 * are initially zero and are not written anywhere.)
 */
    eIdx -= 5;
    /* Compute the signal x[0..39]
 */
    for (k = 0; k <= 39; k++)
      {
        L_result = 8192 >> 1;
        /* for (i = 0; i <= 10; i++) {
 *	L_temp = GSM_L_MULT( wt[k+i], gsm_H[i] );
 *	L_result = GSM_L_ADD( L_result, L_temp );
 * }
 */
        /* Every one of these multiplications is done twice --
 * but I don't see an elegant way to optimize this.
 * Do you?
 */
        L_result += (GSMobj.e[eIdx + k + 0] * (long) -134);
        L_result += (GSMobj.e[eIdx + k + 1] * (long) -374);
        L_result += (GSMobj.e[eIdx + k + 3] * (long) 2054);
        L_result += (GSMobj.e[eIdx + k + 4] * (long) 5741);
        L_result += (GSMobj.e[eIdx + k + 5] * (long) 8192);
        L_result += (GSMobj.e[eIdx + k + 6] * (long) 5741);
        L_result += (GSMobj.e[eIdx + k + 7] * (long) 2054);
        L_result += (GSMobj.e[eIdx + k + 9] * (long) -374);
        L_result += (GSMobj.e[eIdx + k + 10] * (long) -134);
        /* L_result = GSM_L_ADD( L_result, L_result ); (* scaling(x2) *)
 * L_result = GSM_L_ADD( L_result, L_result ); (* scaling(x4) *)
 *
 * x[k] = SASR( L_result, 16 );
 */
        /* 2 adds vs. >>16 => 14, minus one shift to compensate for
 * those we lost when replacing L_MULT by '*'.
 */
        L_result = Util.SASR_L(L_result, (short) 13);
        x[k] = (short) (L_result < Util.MIN_WORD ? Util.MIN_WORD : (L_result > Util.MAX_WORD ? Util.MAX_WORD : L_result));
      }
  }
  /* 4.2.14 */

  private static long STEP(short[] x, int m, int i)
  {
    long L_temp = Util.SASR_W(x[m + 3 * i], (short) 2);
    return (L_temp * L_temp);
  }

  static void RPE_grid_selection(Main GSMobj, short[] x, /* [0..39]		IN */
    short[] xM, /* [0..12]		OUT */
    int McIdx) /*				OUT */
    /*
 * The signal x[0..39] is used to select the RPE grid which is
 * represented by Mc.
 */
  {
    /* short	temp1;	*/
    int /* m, */i;
    long L_result;
    long EM; /* xxx should be L_EM? */
    short Mc;
    long L_common_0_3;
    EM = 0;
    Mc = 0;
    /* for (m = 0; m <= 3; m++) {
 *	L_result = 0;
 *
 *
 *	for (i = 0; i <= 12; i++) {
 *
 *		temp1 = SASR_W( x[m + 3*i], 2 );
 *
 *		assert(temp1 != MIN_WORD);
 *
 *		L_temp = GSM_L_MULT( temp1, temp1 );
 *		L_result = GSM_L_ADD( L_temp, L_result );
 *	}
 *
 *	if (L_result > EM) {
 *		Mc = m;
 *		EM = L_result;
 *	}
 * }
 */
    /* common part of 0 and 3 */
    L_result = STEP(x, 0, 1) + STEP(x, 0, 2) + STEP(x, 0, 3) + STEP(x, 0, 4) + STEP(x, 0, 5) + STEP(x, 0, 6) + STEP(x, 0, 7) + STEP(x, 0, 8) + STEP(x, 0, 9) + STEP(x, 0, 10) + STEP(x, 0, 11) + STEP(x, 0, 12);
    L_common_0_3 = L_result;
    /* i = 0 */
    L_result += STEP(x, 0, 0);
    L_result <<= 1; /* implicit in L_MULT */
    EM = L_result;
    /* i = 1 */
    L_result = STEP(x, 1, 0) + STEP(x, 1, 1) + STEP(x, 1, 2) + STEP(x, 1, 3) + STEP(x, 1, 4) + STEP(x, 1, 5) + STEP(x, 1, 6) + STEP(x, 1, 7) + STEP(x, 1, 8) + STEP(x, 1, 9) + STEP(x, 1, 10) + STEP(x, 1, 11) + STEP(x, 1, 12);
    L_result <<= 1;
    if (L_result > EM)
      {
        Mc = 1;
        EM = L_result;
      }
    /* i = 2 */
    L_result = STEP(x, 2, 0) + STEP(x, 2, 1) + STEP(x, 2, 2) + STEP(x, 2, 3) + STEP(x, 2, 4) + STEP(x, 2, 5) + STEP(x, 2, 6) + STEP(x, 2, 7) + STEP(x, 2, 8) + STEP(x, 2, 9) + STEP(x, 2, 10) + STEP(x, 2, 11) + STEP(x, 2, 12);
    L_result <<= 1;
    if (L_result > EM)
      {
        Mc = 2;
        EM = L_result;
      }
    /* i = 3 */
    L_result = L_common_0_3;
    L_result += STEP(x, 3, 12);
    L_result <<= 1;
    if (L_result > EM)
      {
        Mc = 3;
        EM = L_result;
      }
    /**/
    /* Down-sampling by a factor 3 to get the selected xM[0..12]
 * RPE sequence.
 */
    for (i = 0; i <= 12; i++)
      xM[i] = x[Mc + 3 * i];
    GSMobj.Mc[McIdx] = Mc;
  }
  /* 4.12.15 */

  static void APCM_quantization_xmaxc_to_exp_mant(short xmaxc, /* IN 	*/
    short[] mantExpon) /* 0=mant 1=expon OUT */
  {
    short expon, mant;
    /* Compute exponent and mantissa of the decoded version of xmaxc
 */
    expon = 0;
    if (xmaxc > 15)
      expon = (short) (Util.SASR_W(xmaxc, (short) 3) - 1);
    mant = (short) (xmaxc - (expon << 3));
    if (mant == 0)
      {
        expon = -4;
        mant = 7;
      }
    else
      {
        while (mant <= 7)
          {
            mant = (short) (mant << 1 | 1);
            expon--;
          }
        mant -= 8;
      }
    assert (expon >= -4 && expon <= 6);
    assert (mant >= 0 && mant <= 7);
    mantExpon[1] = expon;
    mantExpon[0] = mant;
  }

  static void APCM_quantization(Main GSMobj, short[] xM, /* [0..12]		IN	*/
    int xMcIdx, /* [0..12]		OUT	*/
    short[] mantExpon, /* 0=mant 1=expon OUT */
    int xmaxcIdx) /*			OUT	*/
  {
    int i, itest;
    int xmax, xmaxc, temp, temp1, temp2;
    short expon, mant;
    /* Find the maximum absolute value xmax of xM[0..12].
 */
    xmax = 0;
    for (i = 0; i <= 12; i++)
      {
        temp = xM[i];
        temp = Util.GSM_ABS((short) temp);
        if (temp > xmax)
          xmax = temp;
      }
    /* Qantizing and coding of xmax to get xmaxc.
 */
    expon = 0;
    temp = Util.SASR_W((short) xmax, (short) 9);
    itest = 0;
    for (i = 0; i <= 5; i++)
      {
        itest = (short) itest | (short) ((temp <= 0) ? 1 : 0);
        temp = Util.SASR_W((short) temp, (short) 1);
        assert (expon <= 5);
        if (itest == 0)
          expon++; /* expon = add (expon, 1) */
      }
    assert (expon <= 6 && expon >= 0);
    temp = expon + 5;
    assert (temp <= 11 && temp >= 0);
    xmaxc = Util.gsm_add(Util.SASR_W((short) xmax, (short) temp), (short) (expon << 3));
    /* Quantizing and coding of the xM[0..12] RPE sequence
 * to get the xMc[0..12]
 */
    APCM_quantization_xmaxc_to_exp_mant((short) xmaxc, mantExpon);
    mant = mantExpon[0];
    expon = mantExpon[1];
    /* This computation uses the fact that the decoded version of xmaxc
 * can be calculated by using the expononent and the mantissa part of
 * xmaxc (logarithmic table).
 * So, this method avoids any division and uses only a scaling
 * of the RPE samples by a function of the expononent. A direct
 * multiplication by the inverse of the mantissa (NRFAC[0..7]
 * found in table 4.5) gives the 3 bit coded version xMc[0..12]
 * of the RPE samples.
 */
    /* Direct computation of xMc[0..12] using table 4.5
 */
    // DEBUG assert( expon <= 4096 && expon >= -4096);
    // DEBUG assert( mant >= 0 && mant <= 7 );
    temp1 = 6 - expon; /* normalization by the expononent */
    temp2 = Table.gsm_NRFAC[mant]; /* inverse mantissa */
    for (i = 0; i <= 12; i++)
      {
        // DEBUG assert(temp1 >= 0 && temp1 < 16);
        temp = xM[i] << temp1;
        temp = Util.GSM_MULT((short) temp, (short) temp2);
        temp = Util.SASR_W((short) temp, (short) 12);
        GSMobj.xmc[xMcIdx + i] = (short) (temp + 4); /* see note below */
      }
    /* NOTE: This equation is used to make all the xMc[i] positive.
 */
    mantExpon[0] = mant;
    mantExpon[1] = expon;
    GSMobj.xmaxc[xmaxcIdx] = (short) xmaxc;
  }
  /* 4.2.16 */

  static void APCM_inverse_quantization(Main GSMobj, short xMcIdx, /* [0..12]			IN 	*/
    short mant, short expon, short[] xMp) /* [0..12]			OUT 	*/
    /*
 * This part is for decoding the RPE sequence of coded xMc[0..12]
 * samples to obtain the xMp[0..12] array. Table 4.6 is used to get
 * the mantissa of xmaxc (FAC[0..7]).
 */
  {
    int i;
    int xMpIdx = 0;
    short temp, temp1, temp2, temp3;
    assert (mant >= 0 && mant <= 7);
    temp1 = Table.gsm_FAC[mant]; /* see 4.2-15 for mant */
    temp2 = Util.gsm_sub((short) 6, expon); /* see 4.2-15 for exp */
    temp3 = Util.gsm_asl((short) 1, Util.gsm_sub(temp2, (short) 1));
    for (i = 13; i > 0; i--)
      {
        // DEBUG assert( *xMc <= 7 && *xMc >= 0 ); 	/* 3 bit unsigned */
        /* temp = gsm_sub( *xMc++ << 1, 7 ); */
        temp = (short) ((GSMobj.xmc[xMcIdx++] << 1) - 7); /* restore sign */
        // DEBUG assert( temp <= 7 && temp >= -7 ); 	/* 4 bit signed */
        temp <<= 12; /* 16 bit signed */
        temp = (short) Util.GSM_MULT_R(temp1, temp);
        temp = (short) Util.GSM_ADD(temp, temp3);
        xMp[xMpIdx++] = Util.gsm_asr(temp, temp2);
      }
  }
  /* 4.2.17 */

  static void RPE_grid_positioning(Main GSMobj, short McIdx, /* grid position	IN	*/
    short[] xMp, /* [0..12]		IN	*/
    short epIdx) /* [0..39]		OUT	*/
    /*
 * This procedure computes the reconstructed long term residual signal
 * ep[0..39] for the LTP analysis filter. The inputs are the Mc
 * which is the grid position selection and the xMp[0..12] decoded
 * RPE samples which are upsampled by a factor of 3 by inserting zero
 * values.
 */
  {
    int i = 13;
    int xMpIdx = 0;
    int Mc = GSMobj.Mc[McIdx];
    // DEBUG assert(0 <= Mc && Mc <= 3);
    switch (Mc)
      {
        case 3:
          GSMobj.e[epIdx++] = 0;
          do
            {
              GSMobj.e[epIdx++] = 0;
              GSMobj.e[epIdx++] = 0;
              GSMobj.e[epIdx++] = xMp[xMpIdx++];
              --i;
            }
          while (i != 0);
          break;
        case 2:
          do
            {
              GSMobj.e[epIdx++] = 0;
              GSMobj.e[epIdx++] = 0;
              GSMobj.e[epIdx++] = xMp[xMpIdx++];
              --i;
            }
          while (i != 0);
          break;
        case 1:
          do
            {
              GSMobj.e[epIdx++] = 0;
              GSMobj.e[epIdx++] = xMp[xMpIdx++];
              GSMobj.e[epIdx++] = 0;
              --i;
            }
          while (i != 0);
          break;
        case 0:
          do
            {
              GSMobj.e[epIdx++] = xMp[xMpIdx++];
              GSMobj.e[epIdx++] = 0;
              GSMobj.e[epIdx++] = 0;
              --i;
            }
          while (i != 0);
          break;
      }
    while (++Mc < 4)
      GSMobj.e[epIdx++] = 0;
    /*

		int i, k;
		for (k = 0; k <= 39; k++) ep[k] = 0;
		for (i = 0; i <= 12; i++) {
			ep[ Mc + (3*i) ] = xMp[i];
		}
		*/
  }
  /* 4.2.18 */
  /* This procedure adds the reconstructed long term residual signal
 * ep[0..39] to the estimated signal dpp[0..39] from the long term
 * analysis filter to compute the reconstructed short term residual
 * signal dp[-40..-1]; also the reconstructed short term residual
 * array dp[-120..-41] is updated.
 */

  public void Gsm_RPE_Encoding(Main GSMobj, int eIdx, int xmaxcIdx, int McIdx, int xMcIdx)
  {
    short[] x = new short[40];
    short[] xM = new short[13];
    short[] xMp = new short[13];
    short[] mantExpon = new short[2]; // Return mant and expon from APCM_quantization
    short mant;
    short expon;
    Weighting_filter(GSMobj, eIdx, x);
    RPE_grid_selection(GSMobj, x, xM, McIdx);
    APCM_quantization(GSMobj, xM, xMcIdx, mantExpon, xmaxcIdx);
    mant = mantExpon[0];
    expon = mantExpon[1];
    APCM_inverse_quantization(GSMobj, (short) xMcIdx, mant, expon, xMp);
    RPE_grid_positioning(GSMobj, (short) McIdx, xMp, (short) eIdx);
  }

  void Gsm_RPE_Decoding(
    /*-struct gsm_state	* S,-*/
    short xmaxcr, short Mcr, short[] xMcr, /* [0..12], 3 bits 		IN	*/
    short[] erp) /* [0..39]			OUT 	*/
  {
    //		short	expon, mant;
    //		short[]	xMp = new short[ 13 ];
    //		short[] mantExpon = new short[2]; // Return mant and expon from APCM_quantization
    //
    //		APCM_quantization_xmaxc_to_exp_mant( xmaxcr, expon );
    //		mant = mantExpon[0];
    //		expon = mantExpon[1];
    //		APCM_inverse_quantization( xMcr, mant, expon, xMp );
    //		RPE_grid_positioning( Mcr, xMp, erp );
  }
}
