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

public class Main
{
  //***************************************************
  // How to use this GSM 6.10 Encoder
  //***************************************************
  // First call GSM_Encoder.Init();
  // Call multiple times with audio samples: GSM_Encoder.Encode(...)
  //***************************************************	
  //***************************************************
  // Initialize
  //***************************************************
  // Initialize instance

  public Main()
  {
    super();
    initState();
  }

  public void Init()
  {
    initState();
  }
  //***************************************************
  // Objects
  //***************************************************
  public Preprocess theGSP_Preprocess = new Preprocess();
  public Lpc theGSM_Lpc = new Lpc();
  public ShortTerm theGSM_ShortTerm = new ShortTerm();
  public LongTerm theGSM_LongTerm = new LongTerm();
  public Rpe theGSM_Rpe = new Rpe();
  //***************************************************
  // Frame storage and parameters
  //***************************************************
  /* [0..7] LAR coefficients OUT */
  public short LARc[] = new short[8];
  /* [0..3] LTP lag OUT */
  public short Nc[] = new short[4];
  /* [0..3] coded LTP gain OUT */
  public short Mc[] = new short[4];
  /* [0..3] RPE grid selection OUT */
  public short bc[] = new short[4];
  /* [0..3] Coded maximum amplitude OUT */
  public short xmaxc[] = new short[4];
  /* [13*4] normalized RPE samples OUT */
  public short xmc[] = new short[13 * 4];
  //******************************************
  // State Variables
  //******************************************
  public short[] dp0 = new short[280];
  public short z1; /* preprocessing.c, Offset_com. */
  public long L_z2; /* Offset_com. */
  public int mp; /* Preemphasis	*/
  public short[] u = new short[8]; /* short_term_aly_filter.c	*/
  public short[][] LARpp = new short[2][8]; /* */
  public short j; /* */
  public short ltp_cut; /* long_term.c, LTP crosscorr. */
  public short nrp; /* 40 */ /* long_term.c, synthesis	*/
  public short[] v = new short[9]; /* short_term.c, synthesis	*/
  public short msr; /* decoder.c,	Postprocessing	*/
  public char verbose; /* only used if !NDEBUG		*/
  public char fast; /* only used if FAST		*/
  public char wav_fmt; /* only used if WAV49 defined	*/
  //	public unsigned char	frame_index;	/* odd/even chaining	*/
  //	public unsigned char	frame_chain;	/* half-byte to carry forward	*/
  /* Moved here from code.c where it was defined as static */
  public short[] e = new short[50];

  public void initState()
  {
    java.util.Arrays.fill(dp0, (short) 0);
    java.util.Arrays.fill(u, (short) 0);
    java.util.Arrays.fill(LARpp[0], (short) 0);
    java.util.Arrays.fill(LARpp[1], (short) 0);
    java.util.Arrays.fill(v, (short) 0);
    java.util.Arrays.fill(e, (short) 0);
    z1 = 0;
    L_z2 = 0;
    mp = 0;
    j = 0;
    ltp_cut = 0;
    nrp = 40; // Initial value = 40
    msr = 0;
    verbose = 0;
    fast = 0;
    wav_fmt = 0;
  }
  // ********************************************************************
  // E n c o d e
  // ********************************************************************
  // GSM 6.10 Encodes 160 samples at a time. You must pass 160
  // samples to encoder at time and pad buffer if needed. Each
  // sample is 16 bits so the buffer size is 320 bytes.
  // The return buffer will be 33 bytes long of the encoded data.
  //
  // There is no return status because there is no failure mode. GSM
  // does not care what is in the source buffer.
  //
  // Sample use:
  // 		source 		= new short[160];
  // 		GSMFrame	= new byte[33];
  // 		< ... fill up audio_data buffer ...>
  //		Encoder.Encode(source, GSMFrame);
  // ******************************************************************** 		

  public void Encode(short[] source, // Input audio data
    byte[] GSMFrame) // GSM output
  {
    short Idx = 0;
    Coder(source);
    GSMFrame[Idx++] = (byte) ((Util.GSM_MAGIC << 4) | ((LARc[0] >> 2) & 0xF)); /* 1 */
    GSMFrame[Idx++] = (byte) (((LARc[0] & 0x3) << 6) | (LARc[1] & 0x3F)); /* 2 */
    GSMFrame[Idx++] = (byte) (((LARc[2] & 0x1F) << 3) | ((LARc[3] >> 2) & 0x7)); /* 3 */
    GSMFrame[Idx++] = (byte) (((LARc[3] & 0x3) << 6) | ((LARc[4] & 0xF) << 2) | ((LARc[5] >> 2) & 0x3)); /* 4 */
    GSMFrame[Idx++] = (byte) (((LARc[5] & 0x3) << 6) | ((LARc[6] & 0x7) << 3) | (LARc[7] & 0x7)); /* 5 */
    GSMFrame[Idx++] = (byte) (((Nc[0] & 0x7F) << 1) | ((bc[0] >> 1) & 0x1)); /* 6 */
    GSMFrame[Idx++] = (byte) (((bc[0] & 0x1) << 7) | ((Mc[0] & 0x3) << 5) | ((xmaxc[0] >> 1) & 0x1F)); /* 7 */
    GSMFrame[Idx++] = (byte) (((xmaxc[0] & 0x1) << 7) | ((xmc[0] & 0x7) << 4) | ((xmc[1] & 0x7) << 1) | ((xmc[2] >> 2) & 0x1)); /* 8 */
    GSMFrame[Idx++] = (byte) (((xmc[2] & 0x3) << 6) | ((xmc[3] & 0x7) << 3) | (xmc[4] & 0x7)); /* 9 */
    GSMFrame[Idx++] = (byte) (((xmc[5] & 0x7) << 5) | ((xmc[6] & 0x7) << 2) | ((xmc[7] >> 1) & 0x3)); /* 10 */
    GSMFrame[Idx++] = (byte) (((xmc[7] & 0x1) << 7) | ((xmc[8] & 0x7) << 4) | ((xmc[9] & 0x7) << 1) | ((xmc[10] >> 2) & 0x1));
    GSMFrame[Idx++] = (byte) (((xmc[10] & 0x3) << 6) | ((xmc[11] & 0x7) << 3) | (xmc[12] & 0x7));
    GSMFrame[Idx++] = (byte) (((Nc[1] & 0x7F) << 1) | ((bc[1] >> 1) & 0x1));
    GSMFrame[Idx++] = (byte) (((bc[1] & 0x1) << 7) | ((Mc[1] & 0x3) << 5) | ((xmaxc[1] >> 1) & 0x1F));
    GSMFrame[Idx++] = (byte) (((xmaxc[1] & 0x1) << 7) | ((xmc[13] & 0x7) << 4) | ((xmc[14] & 0x7) << 1) | ((xmc[15] >> 2) & 0x1)); /* 15 */
    GSMFrame[Idx++] = (byte) (((xmc[15] & 0x3) << 6) | ((xmc[16] & 0x7) << 3) | (xmc[17] & 0x7));
    GSMFrame[Idx++] = (byte) (((xmc[18] & 0x7) << 5) | ((xmc[19] & 0x7) << 2) | ((xmc[20] >> 1) & 0x3));
    GSMFrame[Idx++] = (byte) (((xmc[20] & 0x1) << 7) | ((xmc[21] & 0x7) << 4) | ((xmc[22] & 0x7) << 1) | ((xmc[23] >> 2) & 0x1));
    GSMFrame[Idx++] = (byte) (((xmc[23] & 0x3) << 6) | ((xmc[24] & 0x7) << 3) | (xmc[25] & 0x7));
    GSMFrame[Idx++] = (byte) (((Nc[2] & 0x7F) << 1) | ((bc[2] >> 1) & 0x1)); /* 20 */
    GSMFrame[Idx++] = (byte) (((bc[2] & 0x1) << 7) | ((Mc[2] & 0x3) << 5) | ((xmaxc[2] >> 1) & 0x1F));
    GSMFrame[Idx++] = (byte) (((xmaxc[2] & 0x1) << 7) | ((xmc[26] & 0x7) << 4) | ((xmc[27] & 0x7) << 1) | ((xmc[28] >> 2) & 0x1));
    GSMFrame[Idx++] = (byte) (((xmc[28] & 0x3) << 6) | ((xmc[29] & 0x7) << 3) | (xmc[30] & 0x7));
    GSMFrame[Idx++] = (byte) (((xmc[31] & 0x7) << 5) | ((xmc[32] & 0x7) << 2) | ((xmc[33] >> 1) & 0x3));
    GSMFrame[Idx++] = (byte) (((xmc[33] & 0x1) << 7) | ((xmc[34] & 0x7) << 4) | ((xmc[35] & 0x7) << 1) | ((xmc[36] >> 2) & 0x1)); /* 25 */
    GSMFrame[Idx++] = (byte) (((xmc[36] & 0x3) << 6) | ((xmc[37] & 0x7) << 3) | (xmc[38] & 0x7));
    GSMFrame[Idx++] = (byte) (((Nc[3] & 0x7F) << 1) | ((bc[3] >> 1) & 0x1));
    GSMFrame[Idx++] = (byte) (((bc[3] & 0x1) << 7) | ((Mc[3] & 0x3) << 5) | ((xmaxc[3] >> 1) & 0x1F));
    GSMFrame[Idx++] = (byte) (((xmaxc[3] & 0x1) << 7) | ((xmc[39] & 0x7) << 4) | ((xmc[40] & 0x7) << 1) | ((xmc[41] >> 2) & 0x1));
    GSMFrame[Idx++] = (byte) (((xmc[41] & 0x3) << 6) | ((xmc[42] & 0x7) << 3) | (xmc[43] & 0x7)); /* 30 */
    GSMFrame[Idx++] = (byte) (((xmc[44] & 0x7) << 5) | ((xmc[45] & 0x7) << 2) | ((xmc[46] >> 1) & 0x3));
    GSMFrame[Idx++] = (byte) (((xmc[46] & 0x1) << 7) | ((xmc[47] & 0x7) << 4) | ((xmc[48] & 0x7) << 1) | ((xmc[49] >> 2) & 0x1));
    GSMFrame[Idx++] = (byte) (((xmc[49] & 0x3) << 6) | ((xmc[50] & 0x7) << 3) | (xmc[51] & 0x7)); /* 33 */
  }

  /*************************************************************************
   * The RPE-LTD coder works on a frame by frame basis. The length of
   * the frame is equal to 160 samples. Some computations are done
   * once per frame to produce at the output of the coder the
   * LARc[1..8] parameters which are the coded LAR coefficients and
   * also to realize the inverse filtering operation for the entire
   * frame (160 samples of signal d[0..159]). These parts produce at
   * the output of the coder:
   */
  //	short	* LARc,	/* [0..7] LAR coefficients		OUT	*/
  /*
 * Procedure 4.2.11 to 4.2.18 are to be executed four times per
 * frame. That means once for each sub-segment RPE-LTP analysis of
 * 40 samples. These parts produce at the output of the coder:
 */
  // Nc,	/* [0..3] LTP lag			OUT 	*/
  // bc,	/* [0..3] coded LTP gain		OUT 	*/
  // Mc,	/* [0..3] RPE grid selection		OUT */
  // xmaxc,/* [0..3] Coded maximum amplitude	OUT	*/
  // xMc	/* [13*4] normalized RPE samples	OUT	*/
  public void Coder(short[] source)
  {
    int k;
    int dpIdx = 120; // short[]	dp = dp0 + 120;	/* [ -120...-1 ] */
    int dppIdx = 120; // short[] dpp = dp;		/* [ 0...39 ] */
    int xmcIdx = 0;
    int NcIdx = 0;
    int bcIdx = 0;
    int xmaxcIdx = 0;
    int McIdx = 0;
    short[] so = new short[160];
    theGSP_Preprocess.process(this, source, so);
    theGSM_Lpc.Gsm_LPC_Analysis(this, so, LARc);
    theGSM_ShortTerm.Gsm_Short_Term_Analysis_Filter(this, LARc, so);
    for (k = 0; k <= 3; k++, xmcIdx += 13)
      {
        theGSM_LongTerm.Gsm_Long_Term_Predictor(this, so, /* d [0..39] IN	*/
            (k * 40), /* so Index */
            dpIdx, /* dp Index [-120..-1] IN	*/
            5, /* e Index [0..39] OUT	*/
            dppIdx, /* dp0 Index [0..39] OUT */
            NcIdx++, /* Nc Index */
            bcIdx++); /* bc Index */
        theGSM_Rpe.Gsm_RPE_Encoding(this, 5, /* e ][0..39][ IN/OUT */
            xmaxcIdx++, McIdx++, xmcIdx);
        /*
 * Gsm_Update_of_reconstructed_short_time_residual_signal
 *			( dpp, State->e + 5, dp );
 */
        for (int i = 0; i <= 39; i++)
          dp0[dpIdx + i] = (short) Util.GSM_ADD(e[5 + i], dp0[dppIdx + i]);
        dpIdx += 40;
        dppIdx += 40;
      }
    for (int i = 0; i < 120; i++)
      dp0[i] = dp0[i + 160];
  }
}
