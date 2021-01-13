package com.zrt.zenb.entity.enums;

public enum ZenbCommandType {
	/**
     * 停止数据采集（目前默认是启动不停止采集）
     */
    STOP_DATA_GATHER,
    /**
     * 开始数据采集
     */
    START_DATA_GATHER,
    /**
     * 心跳（双方定时单方面互发，不做应答）
     */
    HEART_BEAT,
    /**
     * 启动M转发
     */
    START_M_MODE,
    /**
     * 停止M转发
     */
    STOP_M_MODE,
    /**
     * 查询M转发状态
     */
    GET_STATUS_M_MODE;
	
	public static ZenbCommandType getZenbCommandType(int zenbCommandType){
		for(ZenbCommandType temp : ZenbCommandType.values()){
			if(temp.ordinal() == zenbCommandType){
				return temp;
			}
		}

		return STOP_DATA_GATHER;
	}
	
}
