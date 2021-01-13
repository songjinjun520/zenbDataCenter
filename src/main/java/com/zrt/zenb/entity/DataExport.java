package com.zrt.zenb.entity;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

public class DataExport {
	private String sheetName;
    private String startTime;
    private String endTime;
    private List<?> data;
    
    public DataExport() {
    	
    }

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}


	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
    
}
