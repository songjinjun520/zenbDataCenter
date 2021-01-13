package com.zrt.lteDZ.vo;

import com.zrt.lteDZ.vo.enums.MsgType;

public class RFControlMsg extends BaseMsg {
    private boolean rfEnable;

    public RFControlMsg(boolean rfEnable, String ipStr) {
        super(ipStr);
        setMsgType(MsgType.IPCELL_RF_CONTROL_REQ);
        setMsgLen(4);
        this.rfEnable = rfEnable;
    }

    public RFControlMsg(boolean rfEnable) {
        super();
        setMsgType(MsgType.IPCELL_RF_CONTROL_REQ);
        setMsgLen(4);
        this.rfEnable = rfEnable;
    }

    @Override
    public byte[] getBytes() {
        byte[] dest = new byte[8];
        int destPos = 0;
        byte[] superBytes = super.getBytes();
        System.arraycopy(superBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] rfEnableBytes = new byte[4];
        rfEnableBytes[3] = this.rfEnable ? (byte)1 : (byte)0;
        System.arraycopy(rfEnableBytes, 0, dest, destPos, 4);
        destPos += 4;

        return dest;
    }
}
