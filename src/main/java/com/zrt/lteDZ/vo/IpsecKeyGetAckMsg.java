package com.zrt.lteDZ.vo;

public class IpsecKeyGetAckMsg extends BaseMsg {

    private String comment = "";

    public IpsecKeyGetAckMsg(String spType) {
        super(spType);
    }

    public IpsecKeyGetAckMsg(byte[] src, String spType) {
        super(src, spType);
        int srcPos = 4;

        int commentLen = getMsgLen();
        byte[] valueBytes = new byte[commentLen];
        System.arraycopy(src, srcPos, valueBytes, 0, commentLen);
        this.comment = new String(valueBytes).trim();
    }

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
