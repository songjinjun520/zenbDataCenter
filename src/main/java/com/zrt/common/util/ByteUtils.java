package com.zrt.common.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.Formatter;

import com.zrt.zenb.common.DataCenter;

public class ByteUtils {
    /**
     * 灏嗕竴涓崟瀛楄妭鐨刡yte杞崲鎴�32浣嶇殑int
     *
     * @param b
     *            byte
     * @return convert result
     */
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * 灏嗕竴涓崟瀛楄妭鐨凚yte杞崲鎴愬崄鍏繘鍒剁殑鏁�
     *
     * @param b
     *            byte
     * @return convert result
     */
    public static String byteToHex(byte b) {
        int i = b & 0xFF;
        return Integer.toHexString(i);
    }

    public static byte intToUnsignedByte(int intValue) {
        byte byteValue = 0;
        int temp = intValue % 256;
        if (intValue < 0) {
            byteValue = (byte) (temp < -128 ? 256 + temp : temp);
        } else {
            byteValue = (byte) (temp > 127 ? temp - 256 : temp);
        }

        return byteValue;
    }

    /**
     * 灏嗕竴涓�4byte鐨勬暟缁勮浆鎹㈡垚32浣嶇殑int
     *
     * @param buf
     *            bytes buffer
     * @param byte[]涓紑濮嬭浆鎹㈢殑浣嶇疆
     * @return convert result
     */
    public static long bytes2long(byte[] buf) {
        // int firstByte = 0;
        // int secondByte = 0;
        // int thirdByte = 0;
        // int fourthByte = 0;
        // int index = pos;
        // firstByte = (0x000000FF & ((int) buf[index]));
        // secondByte = (0x000000FF & ((int) buf[index + 1]));
        // thirdByte = (0x000000FF & ((int) buf[index + 2]));
        // fourthByte = (0x000000FF & ((int) buf[index + 3]));
        // index = index + 4;
        // return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 |
        // fourthByte)) & 0xFFFFFFFFL;

        byte[] tempByte = new byte[4];
        tempByte[0] = buf[3];
        tempByte[1] = buf[2];
        tempByte[2] = buf[1];
        tempByte[3] = buf[0];

        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;

        firstByte = (0x000000FF & ((int) tempByte[0]));
        secondByte = (0x000000FF & ((int) tempByte[1]));
        thirdByte = (0x000000FF & ((int) tempByte[2]));
        fourthByte = (0x000000FF & ((int) tempByte[3]));

        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    public static int bytes2int(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i ++) {
            value += (b[i] & 0xff) << (8 * i);
        }
        return value;
    }

    public static int bytes2int(byte[] src, int srcPos, int len){
        int result = -1;

        try {
            if(len <= 4){
                byte[] intBytes = {0, 0, 0, 0};
                System.arraycopy(src, srcPos, intBytes, 0, len);
                result = ByteUtils.bytes2int(intBytes);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static int bytes2int_big_endian2(byte[] src, int srcPos, int len){
        int result = -1;

        try {
            if(len <= 4){
                byte[] intBytes = {0, 0, 0, 0};
                System.arraycopy(src, srcPos, intBytes, 4 - len, len);
//				System.out.println("");
//				for(int i = 0; i < 4; i ++){
//					System.out.print(intBytes[i] + " ");
//				}
//				System.out.println("");
                result = ByteUtils.bytes2int_big_endian2(intBytes);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static int bytes2int_big_endian(byte[] src, int srcPos, int len){
        int result = -1;

        try {
            if(len <= 4){
                byte[] intBytes = {0, 0, 0, 0};
                System.arraycopy(src, srcPos, intBytes, 4 - len, len);
                System.out.println("");
                for(int i = 0; i < 4; i ++){
                    System.out.print(intBytes[i] + " ");
                }
                System.out.println("");
                result = ByteUtils.bytes2int_big_endian(intBytes);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

//	public static int bytes2int_big_endian(byte[] b){
//		int value = 0;
//		for(int i = 0; i < 4; i ++){
////			value += (b[3 - i] & 0xff) << 8 * (3 - i);
//			value += (b[i] & 0xff) << 8 * (3 - i);
//		}
//
//		return value;
//	}
    /**
     * 鏆備粎鐢ㄤ簬W
     * @param b
     * @return
     */
    public static int bytes2int_big_endian(byte[] b){
        int value = 0;
        value += (b[0] & 0xff) << 8;
        value += (b[1] & 0xff) << 8 * 0;
        value += (b[2] & 0xff) << 8 * 3;
        value += (b[3] & 0xff) << 8 * 2;

        return value;
    }

    public static int bytes2int_big_endian2(byte[] b){
        int value = 0;
        value += (b[0] & 0xff) << 8 * 3;
        value += (b[1] & 0xff) << 8 * 2;
        value += (b[2] & 0xff) << 8 * 1;
        value += (b[3] & 0xff) << 8 * 0;

        return value;
    }

    public static int bytes2UnsignShort(byte[] b) {
        int tempA = -1;
        int tempB = -1;
        if (b[0] < 0) {
            tempA = unsignedByteToInt((byte) b[0]);
        } else {
            tempA = b[0];
        }
        if (b[1] < 0) {
            tempB = unsignedByteToInt((byte) b[1]);
        } else {
            tempB = b[1];
        }
        if (tempA > 0 || tempB > 0) {
            return (tempB << 8) + tempA;
        } else {
            return ((b[1] << 8) | b[0] & 0xff);
        }
    }

    public static int bytes2UnsignShort2(byte[] b){
        int value = 0;
        value += (b[0] & 0xff) << 8 * 1;
        value += (b[1] & 0xff) << 8 * 0;

        return value;
    }

    public static short bytes2short(byte[] b) {
        short re = 0;
        for (int i = 0; i < 2; i++) {
            re <<= 8;
            re += b[i];
        }
        return re;
    }

    public static String byte2Hex(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (byte b : buf) {
            if (b == 0) {
                sb.append("00");
            } else if (b == -1) {
                sb.append("FF");
            } else {
                String str = Integer.toHexString(b).toUpperCase();
                // sb.append(a);
                if (str.length() == 8) {
                    str = str.substring(6, 8);
                } else if (str.length() < 2) {
                    str = "0" + str;
                }
                sb.append(str);

            }
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 32浣峣nt杞琤yte[] 10 杞崲缁撴灉{10, 0, 0, 0}
     *
     * */
    public static byte[] int2bytes(int res) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);// 鏈�浣庝綅
        targets[1] = (byte) ((res >> 8) & 0xff);// 娆′綆浣�
        targets[2] = (byte) ((res >> 16) & 0xff);// 娆￠珮浣�
        targets[3] = (byte) (res >>> 24);// 鏈�楂樹綅,鏃犵鍙峰彸绉汇��
        return targets;
    }

    /**
     * 澶х妯″紡锛屼竴鑸綉缁滀紶杈撶敤杩欑鏍煎紡<br>
     * 10杞崲缁撴灉{0, 0, 0, 10}
     * @param res
     * @return
     */
    public static byte[] int2bytes_big_endian(int res){
        byte[] targets = new byte[4];
        targets[0] = (byte) ((res >>> 24) & 0xff);
        targets[1] = (byte) ((res >>> 16) & 0xff);
        targets[2] = (byte) ((res >>> 8) & 0xff);
        targets[3] = (byte) (res & 0xff);
        return targets;
    }

    public static byte[] long2bytes(long s) {
        byte[] targets = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * short绫诲瀷杞崲鎴恇yte[] 2 杞崲缁撴灉{2, 0}
     */
    public static byte[] short2bytes(short num) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            b[i] = (byte) (num >>> (i * 8));
        }
        return b;
    }

    /**
     * 灏�16浣嶇殑short杞崲鎴恇yte鏁扮粍 2 杞崲缁撴灉{0, 2}
     * */
    // public static byte[] short2bytes2(short s) {
    // byte[] targets = new byte[2];
    // for (int i = 0; i < 2; i++) {
    // int offset = (targets.length - 1 - i) * 8;
    // targets[i] = (byte) ((s >>> offset) & 0xff);
    // }
    // return targets;
    // }

    public static byte[] float2bytes(float v) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        byte[] ret = new byte[4];
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(v);
        bb.get(ret);
        return ret;
    }

    // byte[]杞琭loat
    public static float bytes2float(byte[] v) {
        // ByteBuffer bb = ByteBuffer.wrap(v);
        // FloatBuffer fb = bb.asFloatBuffer();
        // return fb.get();
        //
        int accum = 0;
        for (int shiftBy = 0; shiftBy < 4; shiftBy++) {
            accum |= (v[shiftBy] & 0xff) << shiftBy * 8;
        }

        float result = Float.intBitsToFloat(accum);

        DecimalFormat df = new DecimalFormat("0.00");

        return Float.parseFloat(df.format(result));
    }

    // float杞琤yte[]
    public static byte[] floatToByte(float v) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        byte[] ret = new byte[4];
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(v);
        bb.get(ret);
        return ret;
    }

    // byte[]杞琭loat
    // public static float byteToFloat(byte[] v){
    // ByteBuffer bb = ByteBuffer.wrap(v);
    // FloatBuffer fb = bb.asFloatBuffer();
    // return fb.get();
    // }

    public static String number2HexString(Number inputNubmer) {
        StringBuffer buffer = new StringBuffer();
        Formatter format = new Formatter(buffer);
        format.format("0x%X", inputNubmer);
        return buffer.toString();
    }

    // public static byte[] str2cbcd(String s) {
    // if (s.length() % 2 != 0) {
    // s = "0" + s;
    // }
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // char[] cs = s.toCharArray();
    // for (int i = 0; i < cs.length; i += 2) {
    // int high = cs[i] - 48;
    // int low = cs[i + 1] - 48;
    // baos.write(high << 4 | low);
    // }
    // return baos.toByteArray();
    // }

    static byte ch2bcd(char ch) {
        if (ch >= '0' && ch <= '9')
            return (byte) (ch - '0');

        return 0;
    }

    public static byte[] str2bcd(String str1) {
        char[] str = str1.toCharArray();
        byte[] bcd = new byte[8];
        int i = 0;
        int resultI = 0;
        int len = str.length;
        while (i < len) {
            byte v = 0;
            if (i + 1 < len) {
                v = ch2bcd(str[i + 1]);
            } else {
                v = 0x0f;
            }
            v <<= 4;
            v |= (ch2bcd(str[i]) & 0x0f);

            bcd[resultI] = v;

            i += 2;
            resultI++;
        }

        for (int j = 8; j > resultI; j--) {
            bcd[j - 1] = 0xF;
        }

        return bcd;
    }

    public static byte[] str2bcd_DX(String str1) {
        char[] str = str1.toCharArray();
        byte[] bcd = new byte[9];
        int i = 0;
        int resultI = 0;
        int len = str.length;
        while (i < len) {
            byte v = 0;
            if (i + 1 < len) {
                v = ch2bcd(str[i + 1]);
            } else {
                v = 0x0f;
            }
            v <<= 4;
            v |= (ch2bcd(str[i]) & 0x0f);

            bcd[resultI] = v;

            i += 2;
            resultI++;
        }

        for (int j = 9; j > resultI; j--) {
            bcd[j - 1] = 0xF;
        }

        return bcd;
    }

    public static String bcd2str(byte[] bcd) {
        int i = 0;
        StringBuffer sb = new StringBuffer();
        while (i < 8) {
            byte d1 = (byte) (bcd[i] & 0x0f);
            byte d2 = (byte) ((bcd[i] & 0xf0) >> 4);
            if (d1 == 0x0f)
                break;
            sb.append(d1);
            if (d2 == 0x0f)
                break;
            sb.append(d2);
            i++;
        }

        return sb.toString();
    }

    public static String bcd2str_DX(byte[] bcd) {

        int i = 0;
        StringBuffer sb = new StringBuffer();
        while (i < 9) {
            int temp = bcd[i] & 0xff;
            byte d1 = (byte) (temp & 0x0f);
            byte d2 = (byte) ((temp & 0xf0) >> 4);
            if (d1 == 0x0f)
                break;
            sb.append(d1);
            if (d2 == 0x0f)
                break;
            sb.append(d2);
            i++;
        }

        return sb.toString();
    }

    // public static String cbcd2string(byte[] b) {
    // StringBuffer sb = new StringBuffer();
    // for (int i = 0; i < b.length; i++) {
    // int h = ((b[i]&0xff) >> 4) + 48;
    // sb.append((char) h);
    // int l = (b[i] & 0x0f) + 48;
    // sb.append((char) l);
    // }
    // return sb.toString();
    // }

    public static void main(String[] args) {
        // byte[] bcd = str2cbcd("123451234589635");
        System.out.println("max short : " + Short.MAX_VALUE + " " + Short.MIN_VALUE);
        byte[] bcd = str2bcd("");
        for (int i = 0; i < bcd.length; i++) {
            System.out.print(bcd[i] + " ");
        }
        System.out.println("" + Byte.decode("0x00"));
        byte[] bcd2 = {Byte.decode("0x00"), 0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00};
        System.out.println(bcd2str(bcd2));

        byte[] floatBytes = float2bytes(41.5f);
        for (byte temp : floatBytes) {
            System.out.print(temp + " ");
        }
        System.out.println("bytes2float");
        byte[] floatBytes2 = {0, 0, 38, 66};
        System.out.println(bytes2float(floatBytes2));

        System.out.println("=========short2byte===========");
        byte[] intBytes = int2bytes(10);
        System.out.println("=========byte2short===========");
        byte[] intBytes2 = {0, 4};
        System.out.println(bytes2UnsignShort2(intBytes2));
        System.out.println(bytes2UnsignShort(intBytes2));

        int[] aRGB = {86, 120, 154, 188};
        int color_val = 0;

        color_val = setIntToBit(aRGB[0], color_val, 0, 8);
        System.out.println("color_val : " + color_val);
        color_val = setIntToBit(aRGB[1], color_val, 8, 8);
        color_val = setIntToBit(aRGB[2], color_val, 16, 8);
        color_val = setIntToBit(aRGB[3], color_val, 24, 8);

        System.out.println("putIntToBit : " + color_val);
        System.out.println(Integer.toBinaryString(color_val));
        System.out
                .println("getIntFromBit : " + getIntFromBit(color_val, 24, 8));

        int color_val2 = 0;
        color_val2 = color_val2 | (aRGB[3]);
        color_val2 = color_val2 | (aRGB[2] << 8);
        color_val2 = color_val2 | (aRGB[1] << 16);
        color_val2 = color_val2 | (aRGB[0] << 24);
        System.out.println("putIntToBitReg : " + color_val2);

        int msg_type_8 = 8;
        int _32BitBuff = 0;
        _32BitBuff = ByteUtils.setIntToBit(msg_type_8, _32BitBuff, 0, 8);
        System.out.println("_32BitBuff : " + _32BitBuff);

        int frmInt = 469766145;
        System.out.println("type : " + getIntFromBit(frmInt, 0, 8));

        System.out.println(intToUnsignedByte(240));

//		short shortValue = Short.MIN_VALUE;
        short shortValue = -32;
        System.out.println("shortValue " + shortValue);
        System.out.println(short2int(shortValue));

        System.out.println("=" + ByteUtils.bcd2str(new byte[]{(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff}) + "=");
        int t = 512;
        short s = (short)t;
        System.out.println("int 寮哄埗杞崲涓簊hort锛�" + s);

        int fileLen = 10;
//		byte[] fileLenBytes = {0x4f, 0x0b, 0x00, 0x00};
        byte[] fileLenBytes = {0x0, 0x0, 0x0, 0x65};
//		byte[] fileLenBytes = {(byte)0xff, (byte)0xff, (byte)0xff, (byte)0x8a};
//        byte[] fileLenBytes = {0, 0, 0, (byte)0xa0};
        byte[] fileLenBytes2 = int2bytes_big_endian(fileLen);
        System.out.println("========" + bytes2int(fileLenBytes));
//		System.out.println("========" + Integer.decode("0x00000b4f"));
        System.out.println("====bytes2int_big_endian2====" + bytes2int_big_endian2(fileLenBytes));

        StringBuilder frameBuilder = new StringBuilder();
        frameBuilder.append("D072+005X00220501020E0000010004333332A-");
        int checksum = 0;
        for (int i = 0; i < frameBuilder.length(); i++) {
            checksum += frameBuilder.charAt(i);
        }
        checksum = ByteUtils.getIntFromBit(checksum, 16, 16);
        System.out.println("checksum : " + String.format("%1$04X", checksum));

        byte testByteHex = -1;
        System.out.println(String.format("%02x", testByteHex));

        byte[] arfcnBytes = {111, 0};
        System.out.println("arfcnBytes : " + bytes2UnsignShort2(arfcnBytes));

        char x = '/';
        System.out.println(Integer.toHexString(0xffff & x));

        String aStr = "2f";
        System.out.println((byte)Integer.parseInt(aStr, 16));

//        String ahar = "123abc涓浗 ";
//        try {
//         byte[] b = ahar.getBytes();
//         String str = " ";
//         for (int i = 0; i < b.length; i++) {
//          Integer I = new Integer(b[i]);
//          String strTmp = I.toHexString(b[i]);
//          if (strTmp.length() > 2)
//           strTmp = strTmp.substring(strTmp.length() - 2);
//          str = str + strTmp;
//         }
//         System.out.println(str.toUpperCase());
//        } catch (Exception e) {
//         e.printStackTrace();
//        }

        byte[] charBytes = {(byte)0xe4, (byte)0xb8, (byte)0xad, (byte)0xe5, (byte)0x9b, (byte)0xbd, 0x20};
        try {
            System.out.println(new String(charBytes, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] charBytes2 = {4, -32, 79, -1, -48, 60, 2, 80, 25, -1, -3, -1, -3, 5, 16, 104, 5, -65, -1, -48, 81, 4, -97, -1, -48, 99, -1, -3};

        try {
            System.out.println(new String(charBytes2, "unicode"));
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("鍛靛懙鍛靛懙");
            String smsContentStr = "鍛靛懙鍛靛懙";
            byte[] smsContentData = ByteUtils.relocateUnicodeBytes(smsContentStr.getBytes("unicode"));
            for(int i = 0; i < smsContentData.length; i ++){
                System.out.println(smsContentData[i] + " ");
            }
            System.out.println("");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String help = "a help";
        byte[] helpBytes = help.getBytes();
        for(int i = 0; i < helpBytes.length; i ++){
            System.out.print(helpBytes[i] + " ");
        }
        System.out.println("=============1=====");
        long testLong = 4294967295l;
        byte[] longBytes = long2bytes(testLong);
        for(int i = 0; i < longBytes.length; i ++){
            System.out.print(longBytes[i] + " ");
        }

        // 0 0 0 28 -66 -103 29 -25
        // 0 0 0 1c be  99   1d e7
        System.out.println(bytes2int_big_endian2(new byte[]{0, 0, 37, -15}));
        System.out.println(bytes2UnsignShort2(new byte[]{37, -15}));
        System.out.println(102&0xff);

//        System.out.println(bytes2int_big_endian(new byte[]{0, 0, 62, -122}));
        System.out.println(String.format("%1$02X", 255));
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Long.decode("0xffffffff"));

//        short testShort = 58;
//        byte[] shortBytes = short2bytes(testShort);
//        System.out.println("short bytes 2 int : " + byte2int(shortBytes));

        System.out.println(Integer.toHexString(16433));


        char a = 'F';
        System.out.println((byte)a);

        byte[] testTrim = "46001FF".getBytes();
        System.out.println(new String(testTrim).trim().replaceAll("F", ""));

        System.out.println(bytes2UnsignShort(new byte[]{35, 38}));

        System.out.println((byte)Integer.parseInt("92", 16));


        byte[] mccBytesExample = new byte[]{0x34, 0x36, 0x30};
//        System.out.println("mcc[" + bytes2int(mccBytesExample) + "]");
//        System.out.println("mcc[" + bytes2int_big_endian(mccBytesExample) + "]");
//        System.out.println("mcc[" + bytes2int_big_endian2(mccBytesExample) + "]");


        String mccStr = "460";
        char mccChar0 = mccStr.charAt(0);
        char mccChar1 = mccStr.charAt(1);
        char mccChar2 = mccStr.charAt(2);

        int mcc0 = (int)mccChar0;
        System.out.println("mcc0[" + mcc0 + "]");

        int mccHex0 = (mccChar0 - 0x30) + (mccChar1 - 0x30) * 16;
        int mccHex1 = mccChar2 - 0x30;

        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toHexString(mccHex0));
        sb.append(Integer.toHexString(mccHex1));
        System.out.println("mccHex[" + sb.toString() + "]");

        System.out.println(intToUnsignedByte(170));

        System.out.println(Integer.toHexString(-86));

        
        byte[] testBytes = {0, 0, 1, 0};
        System.out.println(bytes2int_big_endian(testBytes));
        
    }

    /**
     * 灏嗕竴涓猧nt鍊兼寜浣嶅瓨鍏ョ洰鏍噄nt鍊肩殑鎸囧畾浣�
     * @param sourceValue 闇�瑕佽浆鎹负浣嶅苟瀛樺叆鐨刬nt鍊�
     * @param targetValue 璁剧疆鎸囧畾浣嶄负婧恑nt鍊肩殑浣嶅��
     * @param bitStartIndex 鐩爣int鍊间腑浣嶈捣濮嬩綅缃�
     * @param bitLength 婧恑nt鍊奸渶瑕佷繚瀛樼殑浣嶉暱搴�
     * @return
     */
    public static int setIntToBit(int sourceValue, int targetValue,
                                  int bitStartIndex, int bitLength) {

        int temp = sourceValue << (32 - bitLength);
        temp = temp >>> bitStartIndex;

        return targetValue | temp;
    }

    /**
     * 浠�32浣嶇殑sourceValue涓彇鍑篵itLength浣嶉暱搴︿綔涓篿nt鍊艰繑鍥�
     * @param sourceValue
     * @param bitStartIndex 浠庤浣嶅紑濮嬪彇
     * @param bitLength 鍙栧嚭鐨勯暱搴�
     * @return
     */
    public static int getIntFromBit(int sourceValue, int bitStartIndex,
                                    int bitLength) {
        int returnValue = sourceValue << bitStartIndex;

        returnValue = returnValue >>> (32 - bitLength);

        return returnValue;
    }

    /**
     *
     * @param sourceValue
     * @param bitStartIndex
     * @param bitLength
     * @return
     */
    public static byte[] relocateUnicodeBytes(byte[] strBytes) {
        if (strBytes == null) {
            return null;
        }
        byte[] swapBytes = new byte[strBytes.length];

        for (int i = 0; i + 1 < strBytes.length; i = i + 2) {
            swapBytes[i] = strBytes[i + 1];
            swapBytes[i + 1] = strBytes[i];
        }

        return swapBytes;
    }

    public static int short2int(short shortValue){
        return shortValue & 0x0FFFF;
    }

    /**
     * 閫氳繃byte鏁扮粍鍙栧埌short
     *
     * @param b
     * @param index
     *            绗嚑浣嶅紑濮嬪彇
     * @return
     */
    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
    }
    
    public static String arrayBytesToString(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
//                sb.append(Integer.toHexString(data[i]));
            } else {
//                sb.append(" " + Integer.toHexString(data[i]));
            	sb.append(" ");
            }
            sb.append(String.format("%1$02x", data[i]));
        }
        return sb.toString();
    }
    
    public static String arrayBytesToString(Byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
//                sb.append(Integer.toHexString(data[i]));
            } else {
//                sb.append(" " + Integer.toHexString(data[i]));
            	sb.append(" ");
            }
            sb.append(String.format("%1$02x", data[i]));
        }
        return sb.toString();
    }

    /**
     * @param data 需要加解密的内容
     * @param seedBytes 加解密密钥 4字节
     * @return 加解密结果
     */
    public static byte[] xorEncode(byte[] data, byte[] seedBytes){
    	if (data == null || data.length == 0 || seedBytes == null || seedBytes.length == 0) {
            return data;
        }

        byte[] result = new byte[data.length];

        // 使用密钥字节数组循环加密或解密
        for (int i = 0; i < data.length; i++) {
            // 数据与密钥异或, 再与循环变量的低8位异或（增加复杂度）
            result[i] = (byte) (data[i] ^ seedBytes[i % seedBytes.length] ^ (i & 0xFF));
        }

        return result;
    }
        
}
