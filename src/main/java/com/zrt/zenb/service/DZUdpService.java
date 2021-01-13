package com.zrt.zenb.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.alibaba.fastjson.JSONObject;
import com.zrt.common.util.ByteUtils;
import com.zrt.lteDZ.vo.AlarmNotify;
import com.zrt.lteDZ.vo.ImsiRptMsg;
import com.zrt.lteDZ.vo.ImsiSetMsg;
import com.zrt.lteDZ.vo.Imsi_INFO;
import com.zrt.lteDZ.vo.IpsecKeyGetAckMsg;
import com.zrt.lteDZ.vo.StatusRptMsg;
import com.zrt.lteDZ.vo.enums.ControlType;
import com.zrt.lteDZ.vo.enums.MsgType;
import com.zrt.zenb.MainPro;
import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.common.DateUtil;
import com.zrt.zenb.common.StringUtils;
import com.zrt.zenb.entity.ImsiRecord;
import com.zrt.zenb.entity.LogMsg;
import com.zrt.zenb.entity.PMsgSjInfo;
import com.zrt.zenb.entity.TcpMsg;
import com.zrt.zenb.entity.enums.DataRequestType;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理分包粘包
 * LTE板子不能和UI直接交互。需要做消息透传。
 * 收到UI发来的，直接转发给设备
 * 收到设备发来的，直接转发给UI
 * 
 * @author intent
 *
 */
@Slf4j
public class DZUdpService {
	
	private String logTag = this.getClass().getSimpleName();
	
	String commentFileDir = "./comment/";

	private static DatagramSocket socketFromDev = null;

	/**
	 * IMSI接入更新时间（30s内的重复接入不入库）
	 */
	private static Map<String, Long> imsiUpdateTime = new HashMap<>();
	/**
	 * IMSI接入次数计数
	 */
	private static Map<String, Integer> imsiCountMap = new HashMap<>();

	/**
	 * 首次插入该IMSI时的记录ID
	 */
	private static Map<String, Integer> imsiIdMap = new HashMap<>();

	private long lastTrafficReportMsgTime = System.currentTimeMillis();

	public static List<String> simImsiList = new ArrayList<>();

	public DZUdpService() {
		try {
			socketFromDev = new DatagramSocket(DataCenter.uiLinsteningPort);
			new Thread(msisdnQueryRunnable).start();
			new Thread(recvFromDevRunnable).start();
			
			DataCenter.setSocketFromDev(socketFromDev);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private Runnable recvFromDevRunnable = new Runnable() {
        @Override
		public void run() {
            while (true) {
            	boolean recv = false;
                if (socketFromDev != null) {
                	byte[] buffer = new byte[4096];
                	DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    try {
                    	socketFromDev.receive(packet);
                    	int datalen = packet.getLength();
                    	byte[] data = packet.getData();
                    	String devIpStr = packet.getAddress().getHostAddress();
                    	byte[] realData = new byte[datalen];
                    	try {
							System.arraycopy(data, 0, realData, 0, datalen);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
                    	
                    	MsgType msgType = MsgType.valueOf2(ByteUtils.unsignedByteToInt(data[0]));
                    	if(msgType == MsgType.IPCELL_ALARM_NOTIFY) {
                    		 AlarmNotify alarmNotify = new AlarmNotify(data, null);
                    		 LogMsg logStr = new LogMsg("[" + devIpStr + "]" + alarmNotify.toString());
                    		 TcpServer.logList.add(logStr);
                    		 System.out.println(logStr + " [" + TcpServer.logList.size() + "]");
                    	}
                    	else if(msgType == MsgType.IPCELL_IMSI_REPORT) {
                    		ImsiRptMsg imsiRptMsg = new ImsiRptMsg(data, "");
                    		String imsiStr = imsiRptMsg.getImsi();
                    		if(! imsiNoMsisdnList.contains(imsiStr)) {
                    			imsiNoMsisdnList.add(imsiStr);
                    		}
                    		
                    		// 如果为侦码模式，则发送该IMSI释放的目标消息
                    		if(DataCenter.getWorkMode() == 1) {
                    			releaseImsi(imsiStr);
                    		}
                    		// 如果为取号模式，并且数据库中已有对应号码，则发送该IMSI释放目标的消息
                    		else if(DataCenter.getWorkMode() == 2) {
                    			if(imsiMsisdnMap.containsKey(imsiStr)) {
                    				releaseImsi(imsiStr);
                    			}
                    		}

							ImsiRecord msgImsi = new ImsiRecord();
							// IMSI掉线超过30s的再上线则将记录插入数据库
							boolean insertFlag = true;
							// 更新第1条IMSI数据的接入次数
							boolean updateImsiCountFlag = false;
							if(imsiUpdateTime.containsKey(imsiStr)) {
								long lastTime = imsiUpdateTime.get(imsiStr);
								if((System.currentTimeMillis() - lastTime) / 1000 < 30) {
									insertFlag = false;
								}
								else {
									updateImsiCountFlag = true;
									msgImsi.setDataType(1);
									imsiCountMap.put(imsiStr, imsiCountMap.get(imsiStr) + 1);
								}
							}
							else {
								imsiCountMap.put(imsiStr, 1);
							}
							imsiUpdateTime.put(imsiStr, System.currentTimeMillis());
							if(insertFlag) {
                    			msgImsi.setImsi(imsiStr);
                    			msgImsi.setRsrq(imsiRptMsg.getRSRQ());
                    			msgImsi.setGatherTime(DateUtil.getDateString());
								msgImsi.setGatherCount(imsiCountMap.get(imsiStr));
                    			List<ImsiRecord> imsiList = new ArrayList<>();
                    			imsiList.add(msgImsi);
								List<ImsiRecord> resultList = MainPro.dao.insertImsi(imsiList);
								if(imsiCountMap.get(imsiStr) == 1){
									imsiIdMap.put(imsiStr, resultList.get(0).getId());
								}
                    			if(updateImsiCountFlag){
									msgImsi.setId(imsiIdMap.get(imsiStr));
									MainPro.dao.updateImsiCount(msgImsi);
								}
                    		}
                    	}
                    	else if(msgType == MsgType.IPCELL_STATUS_REPORT) {
                    		StatusRptMsg statusRptMsg = new StatusRptMsg(data, null);
                    		LogMsg logStr = new LogMsg("[" + devIpStr + "]" + statusRptMsg.toString());
                   		 	TcpServer.logList.add(logStr);
                   		 	
                   		 	int rptStatus = statusRptMsg.getStatus();
                   		 	String comment = statusRptMsg.getComment();
                   		 	log.info("logList.size[" + TcpServer.logList.size() + "]" + logStr);
                   		 	if(rptStatus == 3) {
                   		 		if(null != comment && comment.trim().length() > 0) {
                   		 			writeToFile(comment);
                   		 		}
                   		 		else {
                   		 			writeToFile("没有comment上报");
                   		 		}
                   		 	}
                    	}
                    	else if(msgType == MsgType.IPCELL_IPSEC_KEY_GET_ACK) {
                    		IpsecKeyGetAckMsg ipsecKeyGetAckMsg = new IpsecKeyGetAckMsg(data, null);
                    		String comment = ipsecKeyGetAckMsg.getComment();
                    		if(null != comment && comment.trim().length() > 0) {
               		 			writeToFile(comment);
               		 		}
                    		else {
               		 			writeToFile("没有comment上报");
               		 		}
                    	}
//                    	else if(msgType == MsgType.IPCELL_GET_ACK) {
//                    		System.err.println(devIpStr + " [IPCELL_GET_ACK]");
//                    	}
                    	
                    	if(realData[0] != -43 && realData[0] != -64) {
//                    		System.out.println("recvFromDev[" + recvFromDev + "][" + devIpStr + "][" + arrayBytesToString(realData) + "]");
							log.info(logTag + " recvFromDev[" + devIpStr + "][" + ByteUtils.arrayBytesToString(realData) + "]");
                    	}
                    	// 板卡版本问题会导致很多IPCELL_TRAFFIC_REPORT消息，每秒200条，
						// 导致其他消息阻塞，这里简单处理超过1分钟的才上报到界面。
                    	if(msgType == MsgType.IPCELL_TRAFFIC_REPORT){
                    		long nowtTime = System.currentTimeMillis();
                    		if((nowtTime - lastTrafficReportMsgTime) / 1000 > 60){
                    			if(DataCenter.uiTcpConnect){
									DataCenter.recvFromDevData.add(new TcpMsg(devIpStr, realData));
								}
								lastTrafficReportMsgTime = nowtTime;
							}
						}
                    	else {
							if(DataCenter.uiTcpConnect){
								DataCenter.recvFromDevData.add(new TcpMsg(devIpStr, realData));
							}
						}

                        recv = true;
                    } catch (Exception e) {
                        e.printStackTrace();
//                        System.err.println("ReceiverMsgRunnable receive Exception2");
						log.info(logTag + " recvFromDevRunnable receive Exception2");
                    }
                }
                if(! recv) {
                	try {
                		Thread.sleep(100L);
                	} catch (Exception e) {
                		e.printStackTrace();
                	}
                }
            }
        }
    };

    public static void releaseImsi(String imsiStr) {
    	// 只给取号/透传板卡发送白名单IMSI
    	for(int i = 0; i < DataCenter.lteDevCount; i ++) {
			String devIpTemp = DataCenter.devIpS[i];
			try {
				ImsiSetMsg imsiSetMsg = new ImsiSetMsg(devIpTemp);
				imsiSetMsg.setImsiNum(1);
				Imsi_INFO imsi_INFO = new Imsi_INFO(imsiStr);
				imsi_INFO.setMode(ControlType.GATHER);
				imsiSetMsg.setImsi_INFOs(new Imsi_INFO[] {imsi_INFO});
				
				byte[] imsiSetBytes = imsiSetMsg.getBytes();
				InetAddress addr = InetAddress.getByName(devIpTemp);
				DatagramPacket sendPacket = new DatagramPacket(imsiSetBytes, imsiSetBytes.length, addr, DataCenter.devLinsteningPort);
				socketFromDev.send(sendPacket);

				log.info("set imsi["+ imsiStr + "] gather[" + ByteUtils.arrayBytesToString(imsiSetBytes) + "]");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
    
    List<Byte> msgList = new ArrayList<>();
    
    private void writeToFile(String comment) {
    	File dirFile = new File(commentFileDir);
    	dirFile.mkdirs();
    	File[] files = dirFile.listFiles();
    	File newFile = new File(commentFileDir + "/" + DateUtil.getDateString(DateUtil.sdf2));
    	if(null == files || files.length < 20) {
    		try {
				newFile.createNewFile();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
    		
    		BufferedWriter bw;
			try {
				FileWriter fw = new FileWriter(newFile);
				bw = new BufferedWriter(fw);
				bw.write(DateUtil.getDateString());
				bw.newLine();
				bw.append(comment);
				bw.flush();
				bw.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	else {
    		long minModified = System.currentTimeMillis();
    		File minModifiedFile = null;
    		for(File temp : files) {
    			long lastModified = temp.lastModified();
    			if(lastModified < minModified) {
    				minModified = lastModified;
    				minModifiedFile = temp;
    			}
    		}
    		if(null != minModifiedFile) {
    			BufferedWriter bw;
    			try {
    				FileWriter fw = new FileWriter(minModifiedFile);
    				bw = new BufferedWriter(fw);
    				bw.newLine();
    				bw.newLine();
    				bw.newLine();
    				bw.write(DateUtil.getDateString());
    				bw.newLine();
    				bw.append(comment);
    				bw.flush();
    				bw.close();
    			}
    			catch (Exception e) {
    				e.printStackTrace();
    			}
    			minModifiedFile.renameTo(newFile);
    		}
    	}
    }
    
    private static Map<String, String> imsiMsisdnMap = new HashMap<>();
    private static CopyOnWriteArrayList<String> imsiNoMsisdnList = new CopyOnWriteArrayList<>();
    
    private Runnable msisdnQueryRunnable = new Runnable() {
		@Override
		public void run() {
			while(true) {
				List<String> temp = new ArrayList<>();
				
				for(String imsi : imsiNoMsisdnList) {
					if(imsiMsisdnMap.containsKey(imsi)) {
						forceSendImsiMsisdn(imsi, imsiMsisdnMap.get(imsi));
					}
					else {
						temp.add(imsi);
					}
				}
				if(temp.size() > 0) {
					List<PMsgSjInfo> sjInfos = MainPro.dao.selectSjInfoByImsi(temp);
					for(PMsgSjInfo sjInfo : sjInfos) {
						String strImsi = sjInfo.getStrimsi();
						String msisdn = sjInfo.getStrmsisdn();
						if(! StringUtils.isBlank(msisdn)) {
							forceSendImsiMsisdn(strImsi, msisdn);
						}
					}
					try {
						Thread.sleep(1000);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						Thread.sleep(100);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
    };
    
    public static void forceSendImsiMsisdn(String imsi, String msisdn) {
    	if(msisdn.startsWith("86")) {
    		msisdn = msisdn.substring(2);
		}
    	
    	JSONObject jo = new JSONObject();
		jo.put("imsi", imsi);
		jo.put("msisdn", msisdn);
		DataCenter.send(DataRequestType.IMSI_MSISDN_MAP, jo.toString(), 0);
		
		imsiNoMsisdnList.remove(imsi);
		imsiMsisdnMap.put(imsi, msisdn);
		
		if(DataCenter.getWorkMode() == 2) {
			releaseImsi(imsi);
		}
    }
    
    public static void clearData() {
    	imsiNoMsisdnList.clear();
    	imsiUpdateTime.clear();
    	imsiCountMap.clear();
    }
    
}
