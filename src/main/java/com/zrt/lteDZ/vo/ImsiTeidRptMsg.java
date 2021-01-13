package com.zrt.lteDZ.vo;


import com.zrt.zenb.common.StringUtils;

public class ImsiTeidRptMsg extends BaseMsg {
    String logTag = this.getClass().getSimpleName();

    private int tunnelNumber;
    private String imsi;
    private int[] bRabDlTEID;
    private int[] bRabUlTEID;

    public ImsiTeidRptMsg(byte[] src) {
        super(src, "");

        int srcPos = 4;

        srcPos ++;

        byte[] imsiBytes = new byte[15];
        System.arraycopy(src, srcPos, imsiBytes, 0, 15);

        char[] imsiChars = new char[15];
        int i = 0;
        StringBuffer sb = new StringBuffer();
        for(byte temp : imsiBytes){
            imsiChars[i ++] = (char)temp;
            sb.append(temp);
        }

        this.imsi = sb.toString();
        srcPos += 15;

        System.out.println("imsi[" + imsi + "]");
    }

    public String getImsi() {
        return this.imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public static void main(String[] args){
        byte[] src = {(byte)0xb5,0x01,0x00,(byte)0x90,0x02,0x04,0x06,0x00,0x00,0x00,0x01,0x00,0x01,0x04,0x01,0x04,0x09,0x07,0x03,0x04,0x01,0x00,0x00,0x08,0x01,0x00,0x00,0x10,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte)0x82,0x2a,(byte)0xbf,0x76,(byte)0x82,0x2a,(byte)0xc1,0x5a,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        ImsiTeidRptMsg rptMsg = new ImsiTeidRptMsg(src);


    }

}