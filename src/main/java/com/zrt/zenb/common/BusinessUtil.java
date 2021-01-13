package com.zrt.zenb.common;

import com.zrt.zenb.entity.enums.SpType;

/**
 * 
 * @name BusinessUitl
 * @author PGW
 * @since 2013-1-22
 */
public class BusinessUtil {
	/**
	 * 将消息的字节数据结构转换为字符串存数据库
	 * @param src
	 * @return
	 */
	public static String reEncodeString(byte[] src) {
		char[] ca = new char[src.length];
		for (int i = 0; i < src.length; i++) {
			ca[i] = (char) src[i];
		}
		return new String(ca);
	}

	/**
	 * 将数据库字符串列内容转换为消息的字节数据结构
	 * @param string
	 * @return
	 */
	public static byte[] reDecodeString(String string) {
		char[] rc = string.toCharArray();
		byte[] rb = new byte[rc.length];
		for (int i = 0; i < rc.length; i++) {
			rb[i] = (byte) rc[i];
		}
		return rb;
	}

	/**
	 * 根据频点值得到BandName验证频点输入正确性
	 * @param earfcn
	 * @return
	 */
	public static int validateEarfcn(int earfcn) {
		if (earfcn >= 0 && earfcn <= 599) {
			return 1;
		} else if (earfcn >= 600 && earfcn <= 1199) {
			return 2;
		} else if (earfcn >= 1200 && earfcn <= 1949) {
			return 3;
		} else if (earfcn >= 1950 && earfcn <= 2399) {
			return 4;
		} else if (earfcn >= 2400 && earfcn <= 2649) {
			return 5;
		} else if (earfcn >= 2650 && earfcn <= 2749) {
			return 6;
		} else if (earfcn >= 2750 && earfcn <= 3449) {
			return 7;
		} else if (earfcn >= 3450 && earfcn <= 3799) {
			return 8;
		} else if (earfcn >= 3800 && earfcn <= 4149) {
			return 9;
		} else if (earfcn >= 4150 && earfcn <= 4749) {
			return 10;
		} else if (earfcn >= 4750 && earfcn <= 4949) {
			return 11;
		} else if (earfcn >= 5010 && earfcn <= 5179) {
			return 12;
		} else if (earfcn >= 5180 && earfcn <= 5279) {
			return 13;
		} else if (earfcn >= 5280 && earfcn <= 5379) {
			return 14;
		} else if (earfcn >= 5730 && earfcn <= 5849) {
			return 17;
		} else if (earfcn >= 5850 && earfcn <= 5999) {
			return 18;
		} else if (earfcn >= 6000 && earfcn <= 6149) {
			return 19;
		} else if (earfcn >= 6150 && earfcn <= 6449) {
			return 20;
		} else if (earfcn >= 6450 && earfcn <= 6599) {
			return 21;
		} else if (earfcn >= 6600 && earfcn <= 7399) {
			return 22;
		} else if (earfcn >= 7500 && earfcn <= 7699) {
			return 23;
		} else if (earfcn >= 7700 && earfcn <= 8039) {
			return 24;
		} else if (earfcn >= 8040 && earfcn <= 8689) {
			return 25;
		} else if (earfcn >= 36000 && earfcn <= 36199) {
			return 33;
		} else if (earfcn >= 36200 && earfcn <= 36349) {
			return 34;
		} else if (earfcn >= 36350 && earfcn <= 36949) {
			return 35;
		} else if (earfcn >= 36950 && earfcn <= 37549) {
			return 36;
		} else if (earfcn >= 37550 && earfcn <= 37749) {
			return 37;
		} else if (earfcn >= 37750 && earfcn <= 38249) {
			return 38;
		} else if (earfcn >= 38250 && earfcn <= 38649) {
			return 39;
		} else if (earfcn >= 38650 && earfcn <= 39649) {
			return 40;
		} else if (earfcn >= 39650 && earfcn <= 41589) {
			return 41;
		} else if (earfcn >= 41590 && earfcn <= 43589) {
			return 42;
		} else if (earfcn >= 43590 && earfcn <= 45589) {
			return 43;
		}

		return -1;
	}

	public static SpType getSptypeFormImsi(String imsiStr){
		if(imsiStr.startsWith("46001")){
			return SpType.PLMN_CU_4G;
		}
		else if(imsiStr.startsWith("46011")){
			return SpType.PLMN_TE_4G;
		}
		else {
			return SpType.PLMN_CM_4G;
		}
	}

}
