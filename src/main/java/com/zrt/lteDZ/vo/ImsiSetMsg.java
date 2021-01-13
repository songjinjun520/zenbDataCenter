package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;
import com.zrt.lteDZ.vo.enums.MsgType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImsiSetMsg
        extends BaseMsg {
    private String logTag = this.getClass().getSimpleName();

    private int imsiNum;
    private Imsi_INFO[] imsi_INFOs = new Imsi_INFO[50];

    public ImsiSetMsg(MsgType msgType) {
        super(msgType);
    }

    public ImsiSetMsg(String ipStr) {
        super(ipStr);
        setMsgType(MsgType.IPCELL_IMSI_SET_REQ);
    }
    public ImsiSetMsg(byte[] data) {
        super(data);

        int srcPos = 4;

        byte[] imsiNumBytes = new byte[4];
        System.arraycopy(data, srcPos, imsiNumBytes, 0, 4);
        srcPos += 4;
        imsiNum = ByteUtils.bytes2int_big_endian2(imsiNumBytes);
        log.info("imsiNum[" + imsiNum + "]");
        for (int i = 0; i < imsiNum && i < imsi_INFOs.length; i++) {
            try {
                byte[] temp = new byte[20];
                System.arraycopy(data, srcPos, temp, 0, 20);
                srcPos += 20;
                imsi_INFOs[i] = new Imsi_INFO(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ImsiSetMsg(MsgType msgType, String ipStr) {
        super(msgType, ipStr);
    }

    public ImsiSetMsg(byte[] data, String ipStr) {
        super(data, ipStr);

        int srcPos = 4;

        byte[] imsiNumBytes = new byte[4];
        System.arraycopy(data, srcPos, imsiNumBytes, 0, 4);
        srcPos += 4;
        imsiNum = ByteUtils.bytes2int_big_endian2(imsiNumBytes);
        for (int i = 0; i < imsiNum && i < imsi_INFOs.length; i++) {
            try {
                byte[] temp = new byte[20];
                System.arraycopy(data, srcPos, temp, 0, 20);
                srcPos += 20;
                imsi_INFOs[i] = new Imsi_INFO(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public byte[] getBytes() {
        int msgLen = 4 + this.imsiNum * 20;
        setMsgLen(msgLen);

        byte[] superBytes = super.getBytes();
        byte[] dest = new byte[4 + msgLen];

        int destPos = 0;
        System.arraycopy(superBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] imsiNumBytes = ByteUtils.int2bytes_big_endian(this.imsiNum);
        System.arraycopy(imsiNumBytes, 0, dest, destPos, 4);
        destPos += 4;

        for (int i = 0; i < this.imsiNum; i++) {
            byte[] temp = this.imsi_INFOs[i].getBytes();
            System.arraycopy(temp, 0, dest, destPos, 20);
            destPos += 20;
        }

        return dest;
    }


    public int getImsiNum() {
        return this.imsiNum;
    }


    public void setImsiNum(int imsiNum) {
        this.imsiNum = imsiNum;
    }


    public Imsi_INFO[] getImsi_INFOs() {
        return this.imsi_INFOs;
    }


    public void setImsi_INFOs(Imsi_INFO[] imsi_INFOs) {
        this.imsi_INFOs = imsi_INFOs;
    }
}
