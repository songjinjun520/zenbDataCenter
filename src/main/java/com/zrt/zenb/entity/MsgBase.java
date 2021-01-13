/**
 * 
 */
package com.zrt.zenb.entity;

import com.zrt.zenb.entity.enums.DataRequestType;

/**
 * @author PGW
 *
 */
public abstract class MsgBase {
        
	protected int dataType = DataRequestType.sms_record.ordinal();

	protected String gatherTime = "";
	protected int summaryId = 0;
	protected String summaryName = "";
	protected String imsi = "";
	protected String msisdn = "";
    /**
     * 
     */
    public MsgBase() {
            // TODO Auto-generated constructor stub
    }

    public int getDataType() {
            return dataType;
    }

    public void setDataType(int dataType) {
            this.dataType = dataType;
    }

    public DataRequestType getDataTypeVo() {
            return DataRequestType.getDataRequestType(dataType);
    }

    public void setDataTypeVO(DataRequestType dataType) {
            this.dataType = dataType.ordinal();
    }

	public String getGatherTime() {
		return gatherTime;
	}

	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public int getSummaryId() {
		return summaryId;
	}

	public void setSummaryId(int summaryId) {
		this.summaryId = summaryId;
	}

	public String getSummaryName() {
		return summaryName;
	}

	public void setSummaryName(String summaryName) {
		this.summaryName = summaryName;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
    
    
}
