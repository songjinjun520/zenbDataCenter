package com.zrt.lteDZ.vo;

import javax.sound.midi.SysexMessage;

import com.zrt.common.util.ByteUtils;
import com.zrt.zenb.common.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImsiRptMsg extends BaseMsg {
    private String reserved;
    private String imsi;
    private int RSRQ;

    public ImsiRptMsg(byte[] src, String spType) {
        super(src, spType);

        int srcPos = 4;

        srcPos += 6;

        byte[] imsiBytes = new byte[16];
        System.arraycopy(src, srcPos, imsiBytes, 0, 16);
        this.imsi = (new String(imsiBytes)).trim();
        srcPos += 16;

        byte[] RSRQBytes = new byte[4];
        System.arraycopy(src, srcPos, RSRQBytes, 0, 4);
        this.RSRQ = ByteUtils.bytes2int_big_endian2(RSRQBytes);
        if(this.RSRQ > 0) {
        	this.RSRQ = - this.RSRQ;
        }
        srcPos += 4;
    }

    public ImsiRptMsg(byte[] src) {
        super(src);

        int srcPos = 4;

        srcPos += 6;

        byte[] imsiBytes = new byte[16];
        System.arraycopy(src, srcPos, imsiBytes, 0, 16);
        this.imsi = (new String(imsiBytes)).trim();
        srcPos += 16;

        byte[] RSRQBytes = new byte[4];
        System.arraycopy(src, srcPos, RSRQBytes, 0, 4);
        this.RSRQ = ByteUtils.bytes2int_big_endian2(RSRQBytes);

        log.info("[" + imsi + "] [" + StringUtils.getByteArrayString(RSRQBytes) + "][" + RSRQ + "]");
        if(RSRQ > 0){
            RSRQ = -RSRQ;
        }

        srcPos += 4;

    }

    @Override
    public String toString() {
        return String.valueOf(this.imsi) + " " + this.RSRQ;
    }


    public String getImsi() {
        return this.imsi;
    }


    public void setImsi(String imsi) {
        this.imsi = imsi;
    }


    public int getRSRQ() {
        return this.RSRQ;
    }


    public void setRSRQ(int rSRQ) {
        this.RSRQ = rSRQ;
    }
}
