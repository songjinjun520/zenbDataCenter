package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;


public class RFStatusRspMsg extends BaseMsg {
    private int rfStatus;

    public RFStatusRspMsg(String spType) {
        super(spType);
    }


    public RFStatusRspMsg(byte[] src, String spType) {
        super(src, spType);

        int srcPos = 4;

        byte[] rfStatusBytes = new byte[4];
        System.arraycopy(src, srcPos, rfStatusBytes, 0, 4);
        this.rfStatus = ByteUtils.bytes2int_big_endian2(rfStatusBytes);
    }

    public RFStatusRspMsg(byte[] src) {
        super(src);

        int srcPos = 4;

        byte[] rfStatusBytes = new byte[4];
        System.arraycopy(src, srcPos, rfStatusBytes, 0, 4);
        this.rfStatus = ByteUtils.bytes2int_big_endian2(rfStatusBytes);
    }

    @Override
    public String toString() {
    	 return (this.rfStatus == 0) ? "射频关闭" : "射频打开";
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  RFStatusRspMsg){
            RFStatusRspMsg rfStatusRspMsg = (RFStatusRspMsg) obj;
            if(rfStatusRspMsg.getRfStatus() == this.getRfStatus()){
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

    public int getRfStatus() {
        return this.rfStatus;
    }
}
