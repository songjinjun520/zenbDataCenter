package com.zrt.zenb.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.alibaba.fastjson.JSONObject;
import com.zrt.zenb.controller.DevController;
import com.zrt.zenb.controller.service.TcpClient;
import com.zrt.zenb.entity.TcpMsg;
import com.zrt.zenb.entity.ZenbMsg;
import com.zrt.zenb.entity.enums.DataRequestType;

/**
 * @author intent
 */
public class DataCenter {
	private static String logTag = "DataCenter";
	
//	/**
//	 * 界面当前显示的数据类型
//	 */
//	public static DataRequestType requestType = DataRequestType.voip_record;
	
	public static int smsData = -1;
	public static int voipData = -1;
	public static int httpData = -1;
	public static int sjInfoData = -1;
	public static int sjAppData = -1;
	
	/**
	 * 取号/透传板卡个数（其余的为围栏板）
	 */
	public static int lteDevCount = 1;
	public static String[] devIpS = new String[]{"192.168.199.188"};
	/**
	 * NUC监听来自界面的消息（界面发给设备）
	 */
	public static int devLinsteningPort = 9201;
	/**
	 * NUC监听来自设备的消息（设备发给界面）
	 */
	public static int uiLinsteningPort = 9202;
	
	/**
	 * 监听来自zenb的消息
	 */
	public static int zenbDstLinsteningPort = 65000;
	/**
	 * zenb的监听端口
	 */
	public static int zenbSrcLinsteningPort = 63000;
	public static String zenbServerIp = "127.0.0.1";
	/**
	 * UI发往设备的消息
	 */
	public static ConcurrentLinkedQueue<Byte> recvFromUIData = new ConcurrentLinkedQueue<>();
	/**
	 * 设备发往UI的消息（包含基带板控制消息和数据业务消息）
	 */
	public static ConcurrentLinkedQueue<TcpMsg> recvFromDevData = new ConcurrentLinkedQueue<>();
	
//	/**
//	 * 发往zenb的控制消息
//	 */
//	public static ConcurrentLinkedQueue<ZenbMsg> recvFromUiZenbData = new ConcurrentLinkedQueue<>();
//	/**
//	 * 从zenb收到的消息
//	 */
//	public static ConcurrentLinkedQueue<TcpMsg> sendToUiZenbData = new ConcurrentLinkedQueue<>();

	private static DatagramSocket socketFromDev = null;
	private static DatagramSocket socketFromZenb = null;
	
	/**
     * 0 监控转发  1 侦码（IMSI） 2 取号（手机号）
     */
    private static int workMode = 0;
	
    /**
     * 本次启动的案例ID
     */
    public static int recordSummaryId = 0;

	/**
	 * 跟UI之间的TCP连接是否成功
	 */
    public static boolean uiTcpConnect = false;

    public static TcpClient tcpClient = null;
	public static DevController devController = null;

	public static byte getDevIndex(String devIpArgs) {
    	for(int i = 0; i < devIpS.length; i ++) {
    		if(devIpArgs.equals(devIpS[i])) {
    			return (byte)i;
    		}
    	}
    	System.err.println("error device ip [" + devIpArgs + "]");
    	return -1;
    }
    public static String getDevIp(int devIndex) {
    	try {
			return devIpS[devIndex];
		}
		catch (Exception e) {
			e.printStackTrace();
			
			return "";
		}
    }
    
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
    
	public static DatagramSocket getSocketFromDev() {
		return socketFromDev;
	}
	public static void setSocketFromDev(DatagramSocket socketFromDev1) {
		socketFromDev = socketFromDev1;
	}
	
	public static DatagramSocket getSocketFromZenb() {
		return socketFromZenb;
	}
	public static void setSocketFromZenb(DatagramSocket socketFromZenb) {
		DataCenter.socketFromZenb = socketFromZenb;
	}
	public static void send(DataRequestType requestType, String responseStr, int hisQuery) {
		if(null != responseStr && responseStr.trim().length() > 0) {
			JSONObject jObject = new JSONObject();
			jObject.put("requestType", requestType.ordinal());
			jObject.put("dataStr", responseStr);
			jObject.put("hisQuery", hisQuery);
//			logger.info("send[" + jObject.toJSONString() + "]");
			TcpMsg tcpMsg = new TcpMsg(jObject.toJSONString(), requestType);
			recvFromDevData.add(tcpMsg);
		}
	}
	
	/**
	 * @return workMode 0 监控转发  1 侦码（IMSI） 2 取号（手机号）
	 */
	public static int getWorkMode() {
        return workMode;
    }

    /**
     * @param workMode 0 监控转发  1 侦码（IMSI） 2 取号（手机号）
     */
    public static void setWorkMode(int workMode1) {
        workMode = workMode1;
    }
    
}
