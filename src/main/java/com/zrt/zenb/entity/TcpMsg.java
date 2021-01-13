package com.zrt.zenb.entity;

import java.util.List;

import com.zrt.common.util.ByteUtils;
import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.entity.enums.DataRequestType;

/**
 * 只要碰到TCP，必须做包头尾长度判断。一定会有粘包，碎包问题。什么readLine不管用。
 * @author intent
 * 
 * UI和设备TCP消息结构
 * 长度包含类型和IP的2个字节，包含结束符2个字节，但不包含自身的4个字节
 * 消息长度[4] + 类型[1] + IP[1] + 消息体[n] + 结束符[2]
 * 
 */
public class TcpMsg {
	/**
	 * 类型[1] + IP[1] + 结束符[2]
	 */
	public static final int baseMsgLen = 1 + 1 + 2;
	
	private String dstIp = "";

	private int devIndex = 0;
	/**
	 * 包含头尾的整体消息。
	 */
	private byte[] msgBytes = null;
	
	/**
	 * 消息体部分。（字节结构或者json字符串结构）
	 */
	private byte[] msgBodyBytes = null;
	
	public static byte[] msgTails = {0x0d, 0x0a};
	
	private DataRequestType msgType = DataRequestType.transmission;
	
	public TcpMsg() {
		
	}
	
	/**
	 * 
	 * @param content 消息体json格式字符串
	 */
	public TcpMsg(String content, DataRequestType requestType) {
		msgBodyBytes = content.getBytes();
		
		msgBytes = new byte[4 + baseMsgLen + msgBodyBytes.length];
		int destPos = 0;
		
		int msgLen = baseMsgLen + msgBodyBytes.length;
		byte[] msgLenByte = ByteUtils.int2bytes(msgLen);
		System.arraycopy(msgLenByte, 0, msgBytes, destPos, 4);
		destPos += 4;
		
		msgBytes[destPos ++] = (byte)requestType.ordinal();
		destPos ++;
		
		System.arraycopy(msgBodyBytes, 0, msgBytes, destPos, msgBodyBytes.length);
		destPos += msgBodyBytes.length;
		
		System.arraycopy(msgTails, 0, msgBytes, destPos, 2);
	}
	
	/**
	 * 
	 * @param content 消息体json格式字符串
	 */
	public TcpMsg(byte[] msgBodyBytesArgs, DataRequestType requestType) {
		msgBodyBytes = msgBodyBytesArgs;
		
		msgBytes = new byte[4 + baseMsgLen + msgBodyBytes.length];
		int destPos = 0;
		
		int msgLen = baseMsgLen + msgBodyBytes.length;
		byte[] msgLenByte = ByteUtils.int2bytes(msgLen);
		System.arraycopy(msgLenByte, 0, msgBytes, destPos, 4);
		destPos += 4;
		
		msgBytes[destPos ++] = (byte)requestType.ordinal();
		destPos ++;
		System.arraycopy(msgBodyBytes, 0, msgBytes, destPos, msgBodyBytes.length);
		destPos += msgBodyBytes.length;
		
		System.arraycopy(msgTails, 0, msgBytes, destPos, 2);
	}
	
	public TcpMsg(byte[] msgBytes) {
	}
	
	public TcpMsg(List<Byte> msgByteList) {
		int index = 0;
		byte msgType1 = msgByteList.get(index ++);
		msgType = DataRequestType.getDataRequestType(msgType1);
		devIndex = msgByteList.get(index ++);
		dstIp = DataCenter.getDevIp(devIndex);
		
		msgBytes = new byte[msgByteList.size()];
		for(int i = 0; i < msgByteList.size(); i ++) {
			msgBytes[i] = msgByteList.get(i);
		}
		
//		System.err.println("TcpMsg constructor[" + ByteUtils.arrayBytesToString(msgBytes) + "]");
		
		msgBodyBytes = new byte[msgByteList.size() - 4];
		for(int i = 0; i < msgBodyBytes.length; i ++) {
			msgBodyBytes[i] = msgByteList.get(i + 2);
		}
	}
	
	public TcpMsg(String devIpStr, byte[] realData) {
		msgBytes = new byte[4 + baseMsgLen + realData.length];
    	int destPos = 0;
    	
    	int msgLen = baseMsgLen + realData.length;
		byte[] msgLenByte = ByteUtils.int2bytes(msgLen);
		System.arraycopy(msgLenByte, 0, msgBytes, destPos, 4);
		destPos += 4;
		
    	msgBytes[destPos ++] = (byte)DataRequestType.transmission.ordinal();
    	msgBytes[destPos ++] = DataCenter.getDevIndex(devIpStr);
    	
    	System.arraycopy(realData, 0, msgBytes, destPos, realData.length);
    	destPos += realData.length;
    	
    	System.arraycopy(msgTails, 0, msgBytes, destPos, 2);
	}

	/**
	 * 使用接口消息构造TCP消息。添加头尾
	 * @param devIndex
	 * @param realData 接口消息结构
	 */
	public TcpMsg(int devIndex, byte[] realData, DataRequestType dataRequestType) {
		msgBytes = new byte[4 + baseMsgLen + realData.length];
		int destPos = 0;

		int msgLen = baseMsgLen + realData.length;
		byte[] msgLenByte = ByteUtils.int2bytes(msgLen);
		System.arraycopy(msgLenByte, 0, msgBytes, destPos, 4);
		destPos += 4;

		msgBytes[destPos ++] = (byte)dataRequestType.ordinal();
		msgBytes[destPos ++] = (byte)devIndex;

		System.arraycopy(realData, 0, msgBytes, destPos, realData.length);
		destPos += realData.length;

		System.arraycopy(msgTails, 0, msgBytes, destPos, 2);

		msgType = dataRequestType;
	}

	public byte[] getBytes() {
		return msgBytes;
	}

	public DataRequestType getMsgType() {
		return msgType;
	}

	public void setMsgType(DataRequestType msgType) {
		this.msgType = msgType;
	}

	public byte[] getMsgBodyBytes() {
		return msgBodyBytes;
	}

	public void setMsgBodyBytes(byte[] msgBodyBytes) {
		this.msgBodyBytes = msgBodyBytes;
	}

	public String getDstIp() {
		return dstIp;
	}

	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}

	public int getDevIndex() {
		return devIndex;
	}

	public void setDevIndex(int devIndex) {
		this.devIndex = devIndex;
	}
}
