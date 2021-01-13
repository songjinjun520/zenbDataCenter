package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;
import com.zrt.lteDZ.vo.enums.ControlType;


public class Imsi_INFO {
    public static int msgLen = 20;

    private String IMSI;
    private int rbOffset = 0;
    private ControlType mode = ControlType.CONTROL;

    public Imsi_INFO(){

    }

    public Imsi_INFO(String imsi){
        this.IMSI = imsi;
    }

    public byte[] getBytes() {
        byte[] dest = new byte[msgLen];
        int destPos = 0;
        byte[] imsiBytes = this.IMSI.getBytes();
        System.arraycopy(imsiBytes, 0, dest, destPos, imsiBytes.length);
        destPos += 16;

        byte[] rbOffsetBytes = {0, 0};
        System.arraycopy(rbOffsetBytes, 0, dest, destPos, 2);
        destPos += 2;

        byte[] modeBytes = ByteUtils.int2bytes_big_endian(this.mode.ordinal());
        System.arraycopy(modeBytes, 2, dest, destPos, 2);
        destPos += 2;

        return dest;
    }

    public Imsi_INFO(byte[] src){
        int srcPos = 0;
        byte[] imsiBytes = new byte[16];
        System.arraycopy(src, srcPos, imsiBytes, 0, 16);
        IMSI = new String(imsiBytes).trim();
        srcPos += 16;

        byte[] modeBytes = new byte[4];
        System.arraycopy(src, srcPos, modeBytes, 0, 4);
        int modeInt = ByteUtils.bytes2int_big_endian2(modeBytes);
        mode = ControlType.valueOf2(modeInt);

    }

    public String getIMSI() {
        return this.IMSI;
    }


    public void setIMSI(String iMSI) {
        this.IMSI = iMSI;
    }


    public ControlType getMode() {
        return this.mode;
    }


    public void setMode(ControlType mode) {
        this.mode = mode;
    }
}
