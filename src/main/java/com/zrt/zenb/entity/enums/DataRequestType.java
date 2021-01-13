package com.zrt.zenb.entity.enums;

public enum DataRequestType {

	/**
	 * 透传消息（基带板跟界面之间通信消息）
	 */
	transmission,
	/**
	 * 和zenb之间的消息。消息内容见ZenbMsg.
	 */
	zenb_msg,
	sms_record,
	voip_record,
	http_record,
	sjInfo_record,
	sjApp_record,
	/**
	 * 界面退出，将保存的ID信息清零。下次界面连接上时，可以取到本次采集的数据
	 */
	UI_QUIT,
	/**
	 * 界面连接
	 */
	TCP_CONNECT,
	/**
	 * 设备点击启动发射
	 */
	DEV_START,
	/**
	 * 设备点击“停止”停止发射
	 */
	DEV_STOP,
	/**
	 * 添加设备日志
	 */
	ADD_LOG,
	/**
	 * 查询设备日志
	 */
	GET_LOG,
	/**
	 * 数据导出请求
	 */
	DATA_EXPORT,
	/**
	 * 返回数据导出进度
	 */
	DATA_EXPORT_PROGRESS,
	/**
	 * 导出zip文件内容传输
	 */
	DATA_EXPORT_F_DATA,
	/**
	 * 导出zip文件传输结束
	 */
	DATA_EXPORT_F_DATA_END,
	/**
	 * 发送IMSI和手机号码对应关系
	 */
	IMSI_MSISDN_MAP,
	/**
	 * IMSI记录查询
	 */
	imsi_record,
	imsi_record2;
	
	public static DataRequestType getDataRequestType(int dataRequestType){
		for(DataRequestType temp : DataRequestType.values()){
			if(temp.ordinal() == dataRequestType){
				return temp;
			}
		}

		return sms_record;
	}
}
