package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;
import com.zrt.zenb.common.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author intent
 */
@Slf4j
public class TrafficControlMsg extends BaseMsg {
    /**
     * 0=关闭转发及回传
     * 1=仅转发
     * 2=转发并等待回传
     */
    private int value;
    /**
     * 只在value=2的时候有效：
     * 0=add，1=delete
     */
    private int actionType = 0;
    private int imsiNum;
    private Imsi_INFO[] imsiInfos;

    public TrafficControlMsg(int value, String ipStr) {
        super(ipStr);
        this.value = value;
    }

    public TrafficControlMsg(int value) {
        this.value = value;
    }

    public TrafficControlMsg(byte[] src){
        super(src);
        int srcPos = 4;
        log.info("TrafficControlMsg", "recv.src[" + StringUtils.getByteArrayString(src) + "]");
        value = src[srcPos ++];
        if(value == 2){
            try {
                byte[] imsiNumBytes = new byte[4];

                System.arraycopy(src, srcPos, imsiNumBytes, 2, 2);
                imsiNum = ByteUtils.bytes2int_big_endian(imsiNumBytes);
                if(imsiNum > 100){
                    imsiNumBytes = new byte[4];
                    System.arraycopy(src, srcPos, imsiNumBytes, 0, 2);
                    imsiNum = ByteUtils.bytes2int(imsiNumBytes);
                }
                srcPos += 2;
                log.info("imsiNum[" + imsiNum + "]");
                imsiInfos = new Imsi_INFO[imsiNum];
                for(int i = 0; i < imsiNum; i ++){
                    byte[] imsiInfoBytes = new byte[Imsi_INFO.msgLen];
                    System.arraycopy(src, srcPos, imsiInfoBytes, 0, Imsi_INFO.msgLen);
                    srcPos += Imsi_INFO.msgLen;

                    imsiInfos[i] = new Imsi_INFO(imsiInfoBytes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public byte[] getBytes() {
        setMsgLen(4 + imsiNum * Imsi_INFO.msgLen);

        byte[] dest = new byte[4 + 4 + imsiNum * Imsi_INFO.msgLen];
        int destPos = 0;
        byte[] superBytes = super.getBytes();
        System.arraycopy(superBytes, 0, dest, destPos, 4);
        destPos += 4;

        dest[destPos ++] = (byte)value;
        dest[destPos ++] = (byte)actionType;

        byte[] valueBytes = ByteUtils.int2bytes(imsiNum);
        System.arraycopy(valueBytes, 0, dest, destPos, 2);
        destPos += 2;
        for(int i = 0; i < imsiNum; i ++){
            Imsi_INFO temp = imsiInfos[i];
            byte[] imsiInfoBytes = temp.getBytes();
            System.arraycopy(imsiInfoBytes, 0, dest, destPos, Imsi_INFO.msgLen);
            destPos += Imsi_INFO.msgLen;
        }

        return dest;
    }

    public TrafficControlMsg(byte[] src, String devIp){
        super(src, devIp);
        int srcPos = 4;

        value = src[srcPos ++];
        if(value == 2){
            try {
                byte[] imsiNumBytes = new byte[4];
                System.arraycopy(src, srcPos, imsiNumBytes, 2, 2);
                imsiNum = ByteUtils.bytes2int(imsiNumBytes);
                srcPos += 2;

                imsiInfos = new Imsi_INFO[imsiNum];
                for(int i = 0; i < imsiNum; i ++){
                    byte[] imsiInfoBytes = new byte[Imsi_INFO.msgLen];
                    System.arraycopy(src, srcPos, imsiInfoBytes, 0, Imsi_INFO.msgLen);
                    srcPos += Imsi_INFO.msgLen;

                    imsiInfos[i] = new Imsi_INFO(imsiInfoBytes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getImsiNum() {
        return imsiNum;
    }

    public void setImsiNum(int imsiNum) {
        this.imsiNum = imsiNum;
    }

    public Imsi_INFO[] getImsiInfos() {
        return imsiInfos;
    }

    public void setImsiInfos(Imsi_INFO[] imsiInfos) {
        this.imsiInfos = imsiInfos;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  TrafficControlMsg){
            TrafficControlMsg trafficControlMsg = (TrafficControlMsg) obj;
            if(trafficControlMsg.getValue() == this.getValue()){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
    	String trafficCtrlStr  = "关闭";
        if(value == 1){
            trafficCtrlStr = "仅转发";
        }
        else if(value == 2){
            trafficCtrlStr = "转发并回传。目标数[" + imsiNum + "]";
        }
        return "转发开关[" + trafficCtrlStr + "]";
    }
}
