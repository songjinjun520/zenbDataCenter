package com.zrt.zenb.entity;

import java.util.List;

import com.zrt.zenb.entity.enums.DataRequestType;

public class DataRequest {

	DataRequestType dataRequestType;
	int startId = -1;
	String likeQueryKey = "";
	List<Object> dataRecord;
	int startTime = -1;
	int endTime = -1;
	/**
	 * 0 监控转发  1 侦码（IMSI） 2 取号（手机号）
	 */
	int workMode = 0;
	String password = "";
	
	public DataRequest() {
		
	}

	public int getStartId() {
		return startId;
	}

	public void setStartId(int startId) {
		this.startId = startId;
	}

	public DataRequestType getDataRequestType() {
		return dataRequestType;
	}

	public void setDataRequestType(DataRequestType dataRequestType) {
		this.dataRequestType = dataRequestType;
	}

	public List<Object> getDataRecord() {
		return dataRecord;
	}

	public void setDataRecord(List<Object> dataRecord) {
		this.dataRecord = dataRecord;
	}
	public String getLikeQueryKey() {
		return likeQueryKey;
	}

	public void setLikeQueryKey(String likeQueryKey) {
		this.likeQueryKey = likeQueryKey;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public int getWorkMode() {
		return workMode;
	}

	/**
	 * @param workMode 0 监控转发  1 侦码（IMSI） 2 取号（手机号）
	 */
	public void setWorkMode(int workMode) {
		this.workMode = workMode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
