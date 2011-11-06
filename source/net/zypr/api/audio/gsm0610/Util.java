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

public class Util
{
  //***************************************************
  // Constants
  //***************************************************
  // Define the magic number for audio files
  public static final byte GSM_MAGIC = 0xD; /* 13 kbit/s RPE-LTP */
  // Integer limits
  public static final short MIN_WORD = (-32767 - 1);
  public static final short MAX_WORD = 32767;
  public static final int MIN_LONGWORD = (-2147483647 - 1);
  public static final int MAX_LONGWORD = 2147483647;
  //***************************************************
  // Methods
  //***************************************************
  //	#define	saturate(x) 	\
  //	((x) < MIN_WORD ? MIN_WORD : (x) > MAX_WORD ? MAX_WORD: (x))

  private static long saturate(long x)
  {
    return ((x) < (long) MIN_WORD ? (long) MIN_WORD : (x) > (long) MAX_WORD ? (long) MAX_WORD : (x));
  }

  public static short gsm_add(short a, short b)
  {
    long sum = (long) a + (long) b;
    return (short) saturate(sum);
  }

  public static short gsm_sub(short a, short b)
  {
    long diff = (long) a - (long) b;
    return (short) saturate(diff);
  }

  public static short gsm_mult(short a, short b)
  {
    if (a == (short) MIN_WORD && b == (short) MIN_WORD)
      return (short) MAX_WORD;
    return (short) SASR_L(((long) a * (long) b), (short) 15);
  }

  public static short gsm_mult_r(short a, short b)
  {
    if (b == (short) MIN_WORD && a == (short) MIN_WORD)
      return (short) MAX_WORD;
    else
      {
        long prod = (long) a * (long) b + 16384;
        prod >>= 15;
        return (short) (prod & 0xFFFF);
      }
  }

  public static short gsm_abs(short a)
  {
    return (short) (a < 0 ? (a == (short) MIN_WORD ? (short) MAX_WORD : -a) : a);
  }

  public static long gsm_L_mult(short a, short b)
  {
    assert (a != (short) MIN_WORD || b != (short) MIN_WORD);
    return ((long) a * (long) b) << 1;
  }

  public static long gsm_L_add(long a, long b)
  {
    if (a < 0)
      {
        if (b >= 0)
          return a + b;
        else
          {
            long A = (long) -(a + 1) + (long) -(b + 1);
            return A >= (long) MAX_LONGWORD ? (long) MIN_LONGWORD : -(long) A - 2;
          }
      }
    else if (b <= 0)
      return a + b;
    else
      {
        long A = (long) a + (long) b;
        return A > (long) MAX_LONGWORD ? (long) MAX_LONGWORD : A;
      }
  }

  public static long gsm_L_sub(long a, long b)
  {
    if (a >= 0)
      {
        if (b >= 0)
          return a - b;
        else
          {
            /* a>=0, b<0 */
            long A = (long) a + -(b + 1);
            return A >= (long) MAX_LONGWORD ? (long) MAX_LONGWORD : (A + 1);
          }
      }
    else if (b <= 0)
      return a - b;
    else
      {
        /* a<0, b>0 */
        long A = (long) -(a + 1) + b;
        return A >= (long) MAX_LONGWORD ? (long) MIN_LONGWORD : -(long) A - 1;
      }
  }
  private static final short bitoff[] =
  { 8, 7, 6, 6, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

  public static short gsm_norm(long a)
    /*
 * the number of left shifts needed to normalize the 32 bit
 * variable L_var1 for positive values on the interval
 *
 * with minimum of
 * minimum of 1073741824 (01000000000000000000000000000000) and
 * maximum of 2147483647 (01111111111111111111111111111111)
 *
 *
 * and for negative values on the interval with
 * minimum of -2147483648 (-10000000000000000000000000000000) and
 * maximum of -1073741824 ( -1000000000000000000000000000000).
 *
 * in order to normalize the result, the following
 * operation must be done: L_norm_var1 = L_var1 << norm( L_var1 );
 *
 * (That's 'ffs', only from the left, not the right..)
 */
  {
    // DEBUG assert(a != 0);
    if (a < 0)
      {
        if (a <= -1073741824)
          return 0;
        a = ~a;
      }
    short rtn = 0;
    if ((a & (long) 0xffff0000) != 0)
      {
        if ((a & (long) 0xff000000) != 0)
          rtn = (short) (-1 + bitoff[(int) (0xFF & (a >> 24))]);
        else
          rtn = (short) (7 + bitoff[(int) (0xFF & (a >> 16))]);
      }
    else
      {
        if ((a & (long) 0xff00) != 0)
          rtn = (short) (15 + bitoff[(int) (0xFF & (a >> 8))]);
        else
          rtn = (short) (23 + bitoff[(int) (0xFF & a)]);
      }
    return rtn;
  }
  //	public long gsm_L_asl (long a, int n)
  //	{
  //		if (n >= 32) return 0;
  //		if (n <= -32) return -(a < 0);
  //		if (n < 0) return gsm_L_asr(a, -n);
  //		return a << n;
  //	}

  public static short gsm_asr(short a, int n)
  {
    if (n >= 16)
      return (short) -(a < 0 ? 1 : 0);
    if (n <= -16)
      return 0;
    if (n < 0)
      return (short) (a << -n);
    return SASR_W(a, (short) n);
  }

  public static short gsm_asl(short a, int n)
  {
    if (n >= 16)
      return 0;
    if (n <= -16)
      return (short) -(a < 0 ? 1 : 0);
    if (n < 0)
      return gsm_asr(a, -n);
    return (short) (a << n);
  }
  //	public long gsm_L_asr (long a, int n)
  //	{
  //		if (n >= 32) return -(a < 0);
  //		if (n <= -32) return 0;
  //		if (n < 0) return a << -n;
  //	
  //		return SASR_L (a, (short) n);
  //	}
  /*
	**	short gsm_asr (short a, int n)
	**	{
	**		if (n >= 16) return -(a < 0);
	**		if (n <= -16) return 0;
	**		if (n < 0) return a << -n;
	**	
	**	#	ifdef	SASR_W
	**			return a >> n;
	**	#	else
	**			if (a >= 0) return a >> n;
	**			else return -(short)( -(uword)a >> n );
	**	#	endif
	**	}
	**	
	*/
  /*
 * (From p. 46, end of section 4.2.5)
 *
 * NOTE: The following lines gives [sic] one correct implementation
 * of the div(num, denum) arithmetic operation. Compute div
 * which is the integer division of num by denum: with denum
 * >= num > 0
 */

  public static short gsm_div(short num, short denum)
  {
    long L_num = num;
    long L_denum = denum;
    short div = 0;
    int k = 15;
    /* The parameter num sometimes becomes zero.
 * Although this is explicitly guarded against in 4.2.5,
 * we assume that the result should then be zero as well.
 */
    /* assert(num != 0); */
    // DEBUG assert(num >= 0 && denum >= num);
    if (num == 0)
      return 0;
    while (k-- > 0)
      {
        div <<= 1;
        L_num <<= 1;
        if (L_num >= L_denum)
          {
            L_num -= L_denum;
            div++;
          }
      }
    return div;
  }
  //******************************************
  // Shift Utilities
  //******************************************
  /* Signed arithmetic shift right. */

  public static short SASR_W(short x, short by)
  {
    return (short) (x >> by);
  }

  public static long SASR_L(long x, short by)
  {
    return (x >> by);
  }

  public static int GSM_MULT_R(short a, short b)
  {
    return (int) ((((long) (a)) * ((long) (b)) + (long) 16384) >> 15);
  }

  public static int GSM_MULT(short a, short b)
  {
    return (int) ((((long) (a)) * ((long) (b))) >> 15);
  }

  public static int GSM_L_MULT(short a, short b)
  {
    return (int) (((long) (a)) * ((long) (b)) << 1);
  }

  public static int GSM_L_ADD(long a, long b)
  {
    long utmp;
    if (a < 0 && b < 0)
      {
        utmp = (long) -((a) + 1) + (long) -((b) + 1);
        return (utmp >= (long) Util.MAX_LONGWORD) ? Util.MIN_LONGWORD : -(int) utmp - 2;
      }
    ;
    if (a > 0 && b > 0)
      {
        utmp = (long) a + (long) b;
        return (utmp >= (long) Util.MAX_LONGWORD) ? Util.MAX_LONGWORD : (int) utmp;
      }
    ;
    return (int) (a + b);
  }

  public static int GSM_ADD(short a, short b)
  {
    int ltmp;
    ltmp = ((int) a) + ((int) b);
    if (ltmp >= Util.MAX_WORD)
      return Util.MAX_WORD;
    if (ltmp <= Util.MIN_WORD)
      return Util.MIN_WORD;
    return ltmp;
  }

  public static int GSM_SUB(short a, short b)
  {
    long ltmp;
    ltmp = ((long) a) - ((long) b);
    if (ltmp >= Util.MAX_WORD)
      ltmp = Util.MAX_WORD;
    else if (ltmp <= Util.MIN_WORD)
      ltmp = Util.MIN_WORD;
    return (int) ltmp;
  }

  public static short GSM_ABS(short a)
  {
    if (a > 0)
      return a;
    if (a == Util.MIN_WORD)
      return Util.MAX_WORD;
    return (short) -a;
  }
}
