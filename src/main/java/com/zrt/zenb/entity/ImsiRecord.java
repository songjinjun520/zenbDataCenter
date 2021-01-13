package com.zrt.zenb.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.zrt.zenb.entity.enums.DataRequestType;

public class ImsiRecord extends MsgBase implements Serializable {

    private int id;
    private int summaryId;
    private int bandName;
    private String imsi;
    private int rsrq = 0;
    private int RSRQ = 0;
    private String gatherTime;
    /**
     * 一次启动过程中接入次数。对应数据库字段res02
     */
    private int gatherCount = 0;

    private long gatherTime2 = 0;
    private long updateTime = 0;

    /**
     * 是否活跃IMSI
     * 0 不活跃
     * 1 活跃
     * 2 采集板卡上报的IMSI（自己板卡）
     */
    private int activeType = 1;

    /**
     * 目标
     */
    private boolean isTgt = false;

    private String msisdn = "";
    private String imei = "";
    private String sjDevInfo = "";

    /**
     * 对应数据库res03字段  一次启动中 0 首次接入， 1 重复接入
     */
    private int dataType = 0;

    public ImsiRecord(){
        setDataTypeVO(DataRequestType.imsi_record);
    }

    public int getBandName() {
        return bandName;
    }

    public void setBandName(int bandName) {
        this.bandName = bandName;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public int getRSRQ() {
        return RSRQ;
    }

    public void setRSRQ(int RSRQ) {
        this.RSRQ = RSRQ;
    }

    public String getGatherTime(String formatType) {
        if(updateTime > 0){
            SimpleDateFormat df = new SimpleDateFormat(formatType);
            return df.format(updateTime);
        }
        else {
            return "";
        }
    }

    public int getGatherCount() {
        return gatherCount;
    }

    public void setGatherCount(int gatherCount) {
        this.gatherCount = gatherCount;
    }

    public long getGatherTime2() {
        return gatherTime2;
    }

    public void setGatherTime2(long gatherTime2) {
        this.gatherTime2 = gatherTime2;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return
     * 是否活跃IMSI
     * 0 不活跃
     * 1 活跃
     * 2 采集板卡上报的IMSI（自己板卡）
     */
    public int getActiveType() {
        return activeType;
    }

    /**
     * @param activeType 是否活跃IMSI
     * 0 不活跃
     * 1 活跃
     * 2 采集板卡上报的IMSI（自己板卡）
     */
    public void setActiveType(int activeType) {
        this.activeType = activeType;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(int summaryId) {
        this.summaryId = summaryId;
    }

    public void setGatherTime(String gatherTime) {
        this.gatherTime = gatherTime;
    }

    public boolean isTgt() {
        return isTgt;
    }

    public void setTgt(boolean tgt) {
        isTgt = tgt;
    }

    public int getRsrq() {
		return rsrq;
	}

	public void setRsrq(int rsrq) {
		this.rsrq = rsrq;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSjDevInfo() {
		return sjDevInfo;
	}

	public void setSjDevInfo(String sjDevInfo) {
		this.sjDevInfo = sjDevInfo;
	}

	public String getGatherTime() {
		return gatherTime;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImsiRecord that = (ImsiRecord) o;
        return imsi.equals(that.imsi);
    }

    @Override
    public int getDataType() {
        return dataType;
    }

    @Override
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
}