package com.zrt.zenb.entity;

import com.zrt.zenb.common.DateUtil;

public class LogMsg {
	public int id = 0;
	public String logTime;
    public String logMsg;

    private static int autoAddId = 0;
    
    public LogMsg(){
    	id = autoAddId ++;
    }

    public LogMsg(String logMsgStr){
        this.logTime = DateUtil.getDateString(DateUtil.FORMAT1_4_2);
        this.logMsg = logMsgStr;
        id = autoAddId ++;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getLogMsg() {
        return logMsg;
    }

    public void setLogMsg(String logMsg) {
        this.logMsg = logMsg;
    }

    @Override
    public String toString() {
        return logTime + " " + logMsg;
    }
}
