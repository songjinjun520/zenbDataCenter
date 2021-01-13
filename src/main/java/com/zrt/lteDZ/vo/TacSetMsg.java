package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;

public class TacSetMsg extends BaseMsg {
    private int tac = 0;


    public TacSetMsg(String ipStr) {
        super(ipStr);
    }


    @Override
    public byte[] getBytes() {
        setMsgLen(4);

        byte[] superBytes = super.getBytes();
        byte[] dest = new byte[8];

        int destPos = 0;
        System.arraycopy(superBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] countBytes = ByteUtils.int2bytes_big_endian(this.tac);
        System.arraycopy(countBytes, 0, dest, destPos, 4);
        destPos += 4;

        return dest;
    }


    public int getTac() {
        return this.tac;
    }


    public void setTac(int tac) {
        this.tac = tac;
    }
}
