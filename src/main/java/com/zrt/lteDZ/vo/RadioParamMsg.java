package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;
import com.zrt.lteDZ.vo.enums.MsgType;


public class RadioParamMsg extends BaseMsg {
    private int Pa;
    private int Pb;
    private int RefPowerInSib2;
    private int RefPowerInPhy;
    private int PreambleInitRecvTargetPwr;
    private int PowerRampingStep;
    private int PreambleTransMax;
    private int deltaPreambleMsg3;
    private int Pmax;
    private int P0NominalPUSCH;
    private int P0NominalPUCCH;

    public RadioParamMsg(String spType) {
        super(spType);
    }


    public RadioParamMsg(MsgType msgType, String spType) {
        super(msgType, spType);
    }


    public RadioParamMsg(byte[] src, String spType) {
        super(src, spType);

        int srcPos = 4;

        byte[] PaBytes = new byte[4];
        System.arraycopy(src, srcPos, PaBytes, 0, 4);
        this.Pa = ByteUtils.bytes2int_big_endian2(PaBytes);
        srcPos += 4;

        byte[] PbBytes = new byte[4];
        System.arraycopy(src, srcPos, PbBytes, 0, 4);
        this.Pb = ByteUtils.bytes2int_big_endian2(PbBytes);
        srcPos += 4;
        byte[] RefPowerInSib2Bytes = new byte[4];
        System.arraycopy(src, srcPos, RefPowerInSib2Bytes, 0, 4);
        this.RefPowerInSib2 = ByteUtils.bytes2int_big_endian2(RefPowerInSib2Bytes);
        srcPos += 4;

        byte[] RefPowerInPhyBytes = new byte[4];
        System.arraycopy(src, srcPos, RefPowerInPhyBytes, 0, 4);
        this.RefPowerInPhy = ByteUtils.bytes2int_big_endian2(RefPowerInPhyBytes);
        srcPos += 4;
        byte[] PreambleInitRecvTargetPwrBytes = new byte[4];
        System.arraycopy(src, srcPos, PreambleInitRecvTargetPwrBytes, 0, 4);
        this.PreambleInitRecvTargetPwr = ByteUtils.bytes2int_big_endian2(PreambleInitRecvTargetPwrBytes);
        srcPos += 4;
        byte[] PowerRampingStepBytes = new byte[4];
        System.arraycopy(src, srcPos, PowerRampingStepBytes, 0, 4);
        this.PowerRampingStep = ByteUtils.bytes2int_big_endian2(PowerRampingStepBytes);
        srcPos += 4;
        byte[] PreambleTransMaxBytes = new byte[4];
        System.arraycopy(src, srcPos, PreambleTransMaxBytes, 0, 4);
        this.PreambleTransMax = ByteUtils.bytes2int_big_endian2(PreambleTransMaxBytes);
        srcPos += 4;
        byte[] deltaPreambleMsg3Bytes = new byte[4];
        System.arraycopy(src, srcPos, deltaPreambleMsg3Bytes, 0, 4);
        this.deltaPreambleMsg3 = ByteUtils.bytes2int_big_endian2(deltaPreambleMsg3Bytes);
        srcPos += 4;
        byte[] PmaxBytes = new byte[4];
        System.arraycopy(src, srcPos, PmaxBytes, 0, 4);
        this.Pmax = ByteUtils.bytes2int_big_endian2(PmaxBytes);
        srcPos += 4;
        byte[] P0NominalPUSCHBytes = new byte[4];
        System.arraycopy(src, srcPos, P0NominalPUSCHBytes, 0, 4);
        this.P0NominalPUSCH = ByteUtils.bytes2int_big_endian2(P0NominalPUSCHBytes);
        srcPos += 4;
        byte[] P0NominalPUCCHBytes = new byte[4];
        System.arraycopy(src, srcPos, P0NominalPUCCHBytes, 0, 4);
        this.P0NominalPUCCH = ByteUtils.bytes2int_big_endian2(P0NominalPUCCHBytes);
        srcPos += 4;
    }


    @Override
    public byte[] getBytes() {
        int msgLen = 44;
        setMsgLen(msgLen);

        byte[] superBytes = super.getBytes();
        byte[] dest = new byte[4 + msgLen];

        int destPos = 0;
        System.arraycopy(superBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] PaBytes = ByteUtils.int2bytes_big_endian(this.Pa);
        System.arraycopy(PaBytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] PbBytes = ByteUtils.int2bytes_big_endian(this.Pb);
        System.arraycopy(PbBytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] RefPowerInSib2Bytes = ByteUtils.int2bytes_big_endian(this.RefPowerInSib2);
        System.arraycopy(RefPowerInSib2Bytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] RefPowerInPhyBytes = ByteUtils.int2bytes_big_endian(this.RefPowerInPhy);
        System.arraycopy(RefPowerInPhyBytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] PreambleInitRecvTargetPwrBytes = ByteUtils.int2bytes_big_endian(this.PreambleInitRecvTargetPwr);
        System.arraycopy(PreambleInitRecvTargetPwrBytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] PowerRampingStepBytes = ByteUtils.int2bytes_big_endian(this.PowerRampingStep);
        System.arraycopy(PowerRampingStepBytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] PreambleTransMaxBytes = ByteUtils.int2bytes_big_endian(this.PreambleTransMax);
        System.arraycopy(PreambleTransMaxBytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] deltaPreambleMsg3Bytes = ByteUtils.int2bytes_big_endian(this.deltaPreambleMsg3);
        System.arraycopy(deltaPreambleMsg3Bytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] PmaxBytes = ByteUtils.int2bytes_big_endian(this.Pmax);
        System.arraycopy(PmaxBytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] P0NominalPUSCHBytes = ByteUtils.int2bytes_big_endian(this.P0NominalPUSCH);
        System.arraycopy(P0NominalPUSCHBytes, 0, dest, destPos, 4);
        destPos += 4;
        byte[] P0NominalPUCCHBytes = ByteUtils.int2bytes_big_endian(this.P0NominalPUCCH);
        System.arraycopy(P0NominalPUCCHBytes, 0, dest, destPos, 4);
        destPos += 4;

        return dest;
    }


    public int getPa() {
        return this.Pa;
    }


    public void setPa(int pa) {
        this.Pa = pa;
    }


    public int getPb() {
        return this.Pb;
    }


    public void setPb(int pb) {
        this.Pb = pb;
    }


    public int getRefPowerInSib2() {
        return this.RefPowerInSib2;
    }


    public void setRefPowerInSib2(int refPowerInSib2) {
        this.RefPowerInSib2 = refPowerInSib2;
    }


    public int getRefPowerInPhy() {
        return this.RefPowerInPhy;
    }


    public void setRefPowerInPhy(int refPowerInPhy) {
        this.RefPowerInPhy = refPowerInPhy;
    }


    public int getPreambleInitRecvTargetPwr() {
        return this.PreambleInitRecvTargetPwr;
    }


    public void setPreambleInitRecvTargetPwr(int preambleInitRecvTargetPwr) {
        this.PreambleInitRecvTargetPwr = preambleInitRecvTargetPwr;
    }


    public int getPowerRampingStep() {
        return this.PowerRampingStep;
    }


    public void setPowerRampingStep(int powerRampingStep) {
        this.PowerRampingStep = powerRampingStep;
    }


    public int getPreambleTransMax() {
        return this.PreambleTransMax;
    }


    public void setPreambleTransMax(int preambleTransMax) {
        this.PreambleTransMax = preambleTransMax;
    }


    public int getDeltaPreambleMsg3() {
        return this.deltaPreambleMsg3;
    }


    public void setDeltaPreambleMsg3(int deltaPreambleMsg3) {
        this.deltaPreambleMsg3 = deltaPreambleMsg3;
    }


    public int getPmax() {
        return this.Pmax;
    }


    public void setPmax(int pmax) {
        this.Pmax = pmax;
    }


    public int getP0NominalPUSCH() {
        return this.P0NominalPUSCH;
    }


    public void setP0NominalPUSCH(int p0NominalPUSCH) {
        this.P0NominalPUSCH = p0NominalPUSCH;
    }


    public int getP0NominalPUCCH() {
        return this.P0NominalPUCCH;
    }


    public void setP0NominalPUCCH(int p0NominalPUCCH) {
        this.P0NominalPUCCH = p0NominalPUCCH;
    }
}
