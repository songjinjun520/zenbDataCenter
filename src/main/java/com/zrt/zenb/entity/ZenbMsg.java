package com.zrt.zenb.entity;

import com.zrt.common.util.ByteUtils;
import com.zrt.zenb.entity.enums.ZenbCommandType;

public class ZenbMsg {

	/**
	 * 报文长度.包括自身的2字节
	 */
	private int msgLen = 0;
	/**
	 * 0—控制管理命令（M转发就用这个类型命令）
	 * 其它—后面需要实时记录时补充
	 */
	private int msgType = 0;
	
	/**
	 * 命令序号：（1开始递增，0跳过，用于检查）
	 */
	private int seq = 0;
	/**
	 * 管理控制命令类型：
	 * 0—停止数据采集（目前默认是启动不停止采集）
	 * 1—开始数据采集
	 * 2—心跳（双方定时单方面互发，不做应答）
	 * 3—启动M转发
	 * 4—停止M转发
	 * 5—查询M转发状态
	 */
	private ZenbCommandType commandType = ZenbCommandType.STOP_DATA_GATHER;
	/**
	 * 请求应答：0—请求，1—应答
	 */
	private int reqRes = 0;
	
	/**
	 * 消息参数 多字节
	 */
	private byte[] msgContent = null;
	
	public ZenbMsg() {
		
	}

	public ZenbMsg(byte[] data) {
		int srcPos = 0;
		byte[] msgLenBytes = {0, 0, 0, 0};
		System.arraycopy(data, srcPos, msgLenBytes, 0, 2);
		srcPos += 2;
		msgLen = ByteUtils.bytes2int(msgLenBytes);
		
		byte[] msgTypeBytes = {0, 0, 0, 0};
		System.arraycopy(data, srcPos, msgTypeBytes, 0, 2);
		srcPos += 2;
		
		byte[] msgContentTotal = new byte[msgLen];
		System.arraycopy(data, srcPos, msgContentTotal, 0, msgLen);
		
		byte[] commandSeqBytes = {0, 0, 0, 0};
		System.arraycopy(msgContentTotal, 0, commandSeqBytes, 0, 4);
		seq = ByteUtils.bytes2int(commandSeqBytes);
		
		byte[] commandTypeBytes = {0, 0, 0, 0};
		System.arraycopy(msgContentTotal, 4, commandTypeBytes, 0, 2);
		commandType = ZenbCommandType.getZenbCommandType(ByteUtils.bytes2int(commandTypeBytes));
		
		byte[] reqResBytes = {0, 0, 0, 0};
		System.arraycopy(msgContentTotal, 6, reqResBytes, 0, 2);
		reqRes = ByteUtils.bytes2int(commandTypeBytes);
		
		msgContent = new byte[msgLen - 12];
		System.arraycopy(msgContentTotal, 8, msgContent, 0, msgContent.length);
		
	}
	
	public int getMsgLen() {
		return msgLen;
	}

	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public ZenbCommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(ZenbCommandType commandType) {
		this.commandType = commandType;
	}

	public int getReqRes() {
		return reqRes;
	}

	public void setReqRes(int reqRes) {
		this.reqRes = reqRes;
	}

	public byte[] getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(byte[] msgContent) {
		this.msgContent = msgContent;
	}
	
	public byte[] getBytes(){
        byte[] result = new byte[msgLen];

        int dstPos = 0;
        byte[] msgLenBytes = ByteUtils.int2bytes(msgLen);
        System.arraycopy(msgLenBytes, dstPos, result, 0, 2);
        dstPos += 2;

        byte[] msgTypeBytes = ByteUtils.int2bytes(msgType);
        System.arraycopy(msgTypeBytes, 0, result, dstPos, 2);
        dstPos += 2;

        byte[] commandSeqBytes = ByteUtils.int2bytes(seq);
        System.arraycopy(commandSeqBytes, 0, result, dstPos, 4);
        dstPos += 4;

        byte[] commandTypeBytes = ByteUtils.int2bytes(commandType.ordinal());
        System.arraycopy(commandTypeBytes, 0, result, dstPos, 2);
        dstPos += 2;

        byte[] reqResBytes = ByteUtils.int2bytes(reqRes);
        System.arraycopy(reqResBytes, 0, result, dstPos, 2);
        dstPos += 2;

        System.arraycopy(msgContent, 0, result, dstPos, msgContent.length);

        return result;
    }
	
}
