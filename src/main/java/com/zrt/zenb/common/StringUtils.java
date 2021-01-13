package com.zrt.zenb.common;

/**
 * @author intent
 */
public class StringUtils {

	public static boolean isBlank(String str) {
		if(null == str || str.equals("null") || str.trim().length() == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public static String getByteArrayString(byte[] data){
		StringBuffer sb = new StringBuffer();

		if(null != data){
			for(int i = 0; i < data.length; i ++){
				if(i == 0){

				}
//				else if((i % 8 == 0)){
//					sb.append("|");
//				}
				else if(i > 0){
					sb.append(" ");
				}

//				sb.append(String.format("%1$02x", com.zrt.gsmRCMsgSend.utils.ByteUtils.unsignedByteToInt(data[i])));
//				sb.append(String.format("%1$-4d", data[i]));
				sb.append(String.format("%1$02x", data[i]));
//				sb.append(data[i]);
			}
		}

		return sb.toString();
	}
	
}
