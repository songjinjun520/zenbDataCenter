package com.zrt.lteDZ.vo;


public class ImsiRsrpRptMsg extends BaseMsg {
    private String reserved;
    private String imsi;
    private int RSRQ;

    public ImsiRsrpRptMsg(byte[] src, String spType) {
        super(spType);
    }
}
