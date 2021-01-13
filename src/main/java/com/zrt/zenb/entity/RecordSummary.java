package com.zrt.zenb.entity;

public class RecordSummary {

	private int id;
	private String summaryName;
	/**
	 * 启动时间
	 */
	private String res01;
	private String res02;
	private String res03;
	
	public RecordSummary() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSummaryName() {
		return summaryName;
	}

	public void setSummaryName(String summaryName) {
		this.summaryName = summaryName;
	}

	public String getRes01() {
		return res01;
	}

	/**
	 * @param res01 启动时间
	 */
	public void setRes01(String res01) {
		this.res01 = res01;
	}

	public String getRes02() {
		return res02;
	}

	public void setRes02(String res02) {
		this.res02 = res02;
	}

	public String getRes03() {
		return res03;
	}

	public void setRes03(String res03) {
		this.res03 = res03;
	}
	
}
