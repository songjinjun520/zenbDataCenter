package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;

public class StatusRptMsg extends BaseMsg {

    private int status = -1;
    private String comment = "";

    public StatusRptMsg(String spType) {
        super(spType);
    }

    public StatusRptMsg(byte[] src, String spType) {
        super(src, spType);
        int srcPos = 4;

        byte[] statusBytes = new byte[4];
        System.arraycopy(src, srcPos, statusBytes, 0, 4);
        this.status = ByteUtils.bytes2int_big_endian2(statusBytes);
        srcPos += 4;

        int commentLen = getMsgLen() - 4;
        byte[] valueBytes = new byte[commentLen];
        System.arraycopy(src, srcPos, valueBytes, 0, commentLen);
        this.comment = new String(valueBytes).trim();

    }

	public StatusRptMsg(byte[] src) {
		super(src);
		int srcPos = 4;

		byte[] statusBytes = new byte[4];
		System.arraycopy(src, srcPos, statusBytes, 0, 4);
		this.status = ByteUtils.bytes2int_big_endian2(statusBytes);
		srcPos += 4;

		int commentLen = getMsgLen() - 4;
//        System.out.println("UDPService.StatusRptMsg.commentLen[" + commentLen + "]");
		byte[] valueBytes = new byte[commentLen];
		System.arraycopy(src, srcPos, valueBytes, 0, commentLen);
		this.comment = new String(valueBytes).trim();

	}

    @Override
	public String toString() {
        String str = "";
        switch (status){
	        case 1:
	            str = "系统上电完成";
	            break;
	        case 2:
	            str = "IPSEC建链";
	            break;
	        case 3:
	            str = "IPSEC建链完成";
	            break;
	        case 4:
	            str = "GPS初始同步完成";
	            break;
	        case 5:
	            str = "GPS/宏网同步完成";
	            break;
	        case 6:
	            str = "开始扫频";
	            break;
	        case 7:
	            str = "扫频完成";
	            break;
	        case 8:
	            str = "SCTP链路建立";
	            break;
	        case 9:
	            str = "SCTP链路建立完成";
	            break;
	        case 10:
	            str = "小区建立中";
	            break;
	        case 11:
	            str = "小区建立完成[" + comment + "]";
	            break;
	        case 12:
	            str = "设备准备重启";
	            break;
	        case 13:
	            str = "小区参数重配[" + comment + "]";
	            break;
	        case 14:
	            str = "射频打开";
	            break;
	        case 15:
	            str = "射频关闭";
	            break;
	        case 16:
	            str = "小区准入模式-开放模式";
	            break;
	        case 17:
	            str = "小区准入模式-受限模式";
	            break;
	        case 18:
	            str = "数据转发-关";
	            break;
	        case 19:
	            str = "数据转发-开-全部转发";
	            break;
	        case 20:
	            str = "数据转发-开-指定转发";
	            break;
	        case 21:
	            str = "短信拦截使能";
	            break;
	        case 22:
	            str = "短信拦截禁止";
	            break;
            default:
            	break;
        }
        return str;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

    
    
}
