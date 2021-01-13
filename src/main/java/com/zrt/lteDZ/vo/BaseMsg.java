package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;
import com.zrt.lteDZ.vo.enums.MsgType;

/**
 * @author intent
 */
public class BaseMsg {
    public static final int baseMsgLen = 4;
    private String ipStr;
    private MsgType msgType;
    private int reserved;
    private int msgLen = 0;

    public BaseMsg(){

    }

    public BaseMsg(String ipStr) { this.ipStr = ipStr; }

    public BaseMsg(MsgType msgType) { this.msgType = msgType; }

    public BaseMsg(MsgType msgType, String ipStr) {
        this.msgType = msgType;
        this.ipStr = ipStr;
    }

    public BaseMsg(byte[] src) {
        int srcPos = 0;
        this.msgType = MsgType.valueOf2(ByteUtils.unsignedByteToInt(src[srcPos++]));

        this.reserved = src[srcPos++];

        byte[] msgLenBytes = new byte[4];
        System.arraycopy(src, srcPos, msgLenBytes, 2, 2);
        this.msgLen = ByteUtils.bytes2int_big_endian2(msgLenBytes);
        srcPos += 2;
    }

    public BaseMsg(byte[] src, String ipStr) {
        this.ipStr = ipStr;

        int srcPos = 0;
        this.msgType = MsgType.valueOf2(ByteUtils.unsignedByteToInt(src[srcPos++]));

        this.reserved = src[srcPos++];

        byte[] msgLenBytes = new byte[4];
        System.arraycopy(src, srcPos, msgLenBytes, 2, 2);
        this.msgLen = ByteUtils.bytes2int_big_endian2(msgLenBytes);
        srcPos += 2;
    }

    public byte[] getBytes() {
        int destPos = 0;
        byte[] result = new byte[4];

        result[destPos++] = (byte)this.msgType.value();

        destPos++;

        byte[] msgLenBytes = ByteUtils.int2bytes(this.msgLen);
        System.arraycopy(msgLenBytes, 0, result, destPos, 2);
        destPos += 2;

        return result;
    }

    public MsgType getMsgType() { return this.msgType; }

    public void setMsgType(MsgType msgType) { this.msgType = msgType; }

    public int getMsgLen() { return this.msgLen; }

    public void setMsgLen(int msgLen) { this.msgLen = msgLen; }

    public String getIpStr() { return this.ipStr; }

    public void setIpStr(String ipStr) { this.ipStr = ipStr; }
}

