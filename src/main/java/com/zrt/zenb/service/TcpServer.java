package com.zrt.zenb.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.DatagramChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrt.common.util.ByteUtils;
import com.zrt.zenb.MainPro;
import com.zrt.zenb.ZenbBase;
import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.common.DateUtil;
import com.zrt.zenb.common.JxlsUtils;
import com.zrt.zenb.dao.ZenbDao;
import com.zrt.zenb.entity.DataExport;
import com.zrt.zenb.entity.DataRequest;
import com.zrt.zenb.entity.ImsiRecord;
import com.zrt.zenb.entity.LogMsg;
import com.zrt.zenb.entity.PMsgHttp;
import com.zrt.zenb.entity.PMsgSjApp;
import com.zrt.zenb.entity.PMsgSjInfo;
import com.zrt.zenb.entity.PMsgSms;
import com.zrt.zenb.entity.PMsgVoip;
import com.zrt.zenb.entity.RecordSummary;
import com.zrt.zenb.entity.TcpMsg;
import com.zrt.zenb.entity.enums.DataRequestType;

/**
 * 接收和处理android UI的连接和数据传输请求
 * 
 * @author intent
 *
 */
@Slf4j
public class TcpServer implements Runnable {

	ServerSocket serverSocket = null;
	Socket connectSocket = null;
	InputStream is = null;
	OutputStream os = null;
	byte[] readBuffer = new byte[2048];
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private boolean devRFOpen = false;
	/**
	 * 将设备告警消息保存在内存里
	 * 每次重新启动设备时清除数据
	 * 这样可以保证一次启动过程中，不管APP UI如何操作（退出，重启），日志消息都可以保存下来
	 * 不管APP什么时候查看日志数据，都可以看到本次开机启动后的所有日志
	 */
	public static List<LogMsg> logList = new ArrayList<>();
	
	public TcpServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			
			new Thread(new RecvRunnable()).start();
			new Thread(new SendRunnable()).start();
			new Thread(new MsgDealRunnable()).start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (null != serverSocket) {
			while (true) {
				try {
					connectSocket = serverSocket.accept();
					is = connectSocket.getInputStream();
					os = connectSocket.getOutputStream();
					DataCenter.uiTcpConnect = true;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	class RecvRunnable implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					if(null != is) {
//						System.out.println("is[" + is + "]");
						int readLen = is.read(readBuffer);
						if(readLen > 0) {
							byte[] readData1 = new byte[readLen];
							System.arraycopy(readBuffer, 0, readData1, 0, readLen);
							log.info("recv from UI msg[" + ByteUtils.arrayBytesToString(readData1) + "]");
							
							for(int i = 0; i < readData1.length; i ++) {
                    			DataCenter.recvFromUIData.offer(readData1[i]);
                    		}
							
							readBuffer = new byte[2048];
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
				catch (IOException e1) {
					e1.printStackTrace();
					if(null != is) {
						try {
							is.close();
							os.close();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						is = null;
					}
				}
				
				try {
					Thread.sleep(100);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 将设备发给界面的消息和数据业务响应消息发给UI
	 * @author intent
	 *
	 */
	class SendRunnable implements Runnable {
		@Override
		public void run() {
			while (true) {
				boolean sended = false;
				if(null != os) {
//					System.out.println("os[" + os + "]");
					TcpMsg data = DataCenter.recvFromDevData.poll();
					if(data != null) {
						byte[] msgBytes = data.getBytes();
						// 生成1000~9000的随机数作为加密密钥
                        Random random = new Random(System.currentTimeMillis());
                        int randomInt = random.nextInt(4000) + 1000;
                        int randomInt2 = random.nextInt(4000) + 5000;
                        byte[] seedBytes1 = ByteUtils.int2bytes(randomInt);
                        byte[] seedBytes2 = ByteUtils.int2bytes(randomInt2);
                        byte[] seedBytes = new byte[4];
                        System.arraycopy(seedBytes1, 0, seedBytes, 0, 2);
                        System.arraycopy(seedBytes2, 0, seedBytes, 2, 2);

                        log.info("send to UI msg1[" + msgBytes.length + "][" + ByteUtils.arrayBytesToString(msgBytes) + "]");
                        
                        byte[] encode2Bytes = ByteUtils.xorEncode(msgBytes, seedBytes);

                        byte[] resultMsgBytes = new byte[msgBytes.length + 4];
                        System.arraycopy(seedBytes, 0, resultMsgBytes, 0, 4);
                        System.arraycopy(encode2Bytes, 0, resultMsgBytes, 4, encode2Bytes.length);
						
                        log.info("send to UI msg2[" + encode2Bytes.length + "][" + ByteUtils.arrayBytesToString(resultMsgBytes) + "]");
                        
						try {
							os.write(resultMsgBytes);
							os.flush();
							sended = true;
						}
						catch (Exception e) {
							DataCenter.uiTcpConnect = false;
							e.printStackTrace();
							try {
								is.close();
								os.close();
							}
							catch (Exception e2) {
								e2.printStackTrace();
							}
							
							os = null;
						}
					}
				}
				if(! sended) {
					try {
						Thread.sleep(100);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	List<Byte> msgList = new ArrayList<>();
	byte[] seedBytes = new byte[4];
	byte[] msgLenBytes = new byte[4];
	int msgLen = 0;
	int pos = 0;
	
	/**
	 * 处理从UI收到的消息。如果是发往设备的，则用设备UDP发往设备。
	 * 
	 * @author intent
	 *
	 */
	class MsgDealRunnable implements Runnable {
		@Override
		public void run() {
			while(true) {
				boolean sended = false;
            	boolean msgEnd = false;
            	while(! msgEnd) {
            		Byte data = DataCenter.recvFromUIData.poll();
            		if(null != data) {
            			if(pos < 4) {
            				seedBytes[pos] = data;
            				
            				pos ++;
            			}
            			else if(pos < 8) {
            				msgLenBytes[pos - 4] = data;
            				if(pos == 7) {
//            					log.info("seedBytes[" + ByteUtils.arrayBytesToString(seedBytes) + "]");
            					byte[] realMsgLenBytes = ByteUtils.xorEncode(msgLenBytes, seedBytes);
            					msgLen = ByteUtils.bytes2int(realMsgLenBytes);
            					
            					log.info("recv from UI msgLen[" + msgLen + "] msgLenBytes[" + ByteUtils.arrayBytesToString(realMsgLenBytes) + "]");
            				}
            				
            				pos ++;
            			}
            			else if(pos < 8 + msgLen){
            				msgList.add(data);
            				pos ++;
            				
            				if(pos == 8 + msgLen) {
            					msgEnd = true;
            				}
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
            	            	
            	if(msgList.size() >= TcpMsg.baseMsgLen) {
            		byte[] allMsg = new byte[4 + msgList.size()];
            		System.arraycopy(msgLenBytes, 0, allMsg, 0, 4);
            		
            		for(int i = 0; i < msgList.size(); i ++) {
            			allMsg[i + 4] = msgList.get(i);
            		}
            		
            		log.info("recv from UI msg1[" + ByteUtils.arrayBytesToString(allMsg) + "]");
            		byte[] msgContentBytes2 = ByteUtils.xorEncode(allMsg, seedBytes);
            		log.info("recv from UI msg3[" + ByteUtils.arrayBytesToString(msgContentBytes2) + "]");
            		
            		List<Byte> msgList2 = new ArrayList<>();
            		for(int i = 4; i < msgContentBytes2.length; i ++) {
            			msgList2.add(msgContentBytes2[i]);
            		}
            		
            		TcpMsg tcpMsg = new TcpMsg(msgList2);
            		DataRequestType msgType = tcpMsg.getMsgType();
            		String dstIp = tcpMsg.getDstIp();
            		byte[] realData = tcpMsg.getMsgBodyBytes();
            		if(msgType != DataRequestType.ADD_LOG) {
            			log.info("recv from UI msgType[" + msgType + "][" + ByteUtils.arrayBytesToString(realData) + "]");
            		}
            		else {
            			log.info("recv from UI msgType[TCP心跳]");
            		}
            		switch (msgType) {
            			// 发往设备的消息
						case transmission:
//							System.err.println("recv transmission msg from UI [" + ByteUtils.arrayBytesToString(realData) + "]");
							try {
								InetAddress addr = InetAddress.getByName(dstIp);
								DatagramPacket sendPacket = new DatagramPacket(realData, realData.length, addr, DataCenter.devLinsteningPort);
								DataCenter.getSocketFromDev().send(sendPacket);
							}
							catch (Exception e1) {
								e1.printStackTrace();
							}
							break;
						case zenb_msg:
//							System.err.println("recv zenb_msg msg from UI [" + ByteUtils.arrayBytesToString(realData) + "]");
							try {
								InetAddress addr = InetAddress.getByName(DataCenter.zenbServerIp);
								DatagramPacket sendPacket = new DatagramPacket(realData, realData.length, addr, DataCenter.zenbSrcLinsteningPort);
								DataCenter.getSocketFromZenb().send(sendPacket);
							}
							catch (Exception e1) {
								e1.printStackTrace();
							}
							break;
						case UI_QUIT:
							System.out.println("UI_QUIT.reset cache data.");
							devRFOpen = false;
							break;
						case TCP_CONNECT:
							String readLine2 = new String(realData);
							JSONObject jo2 = JSONObject.parseObject(readLine2);
							DataRequest dataRequest2 = jo2.toJavaObject(DataRequest.class);
							String likeQueryKey2 = dataRequest2.getLikeQueryKey();
							String terminalMac = dataRequest2.getPassword();
							
							long uiTime = Long.parseLong(likeQueryKey2);
							long systemTime = System.currentTimeMillis();
							long timeInterv = systemTime - uiTime;
							log.info("uiTime[" + uiTime + "] sysTime[" + systemTime + "] interv[" + timeInterv + "]");
							// 如果时间差值在1小时以上，则用手机时间同步NUC系统时间
							if(Math.abs(timeInterv) > 3600000) {
								System.err.println("update system time to uiTime.");
								String result = df.format(uiTime);
								MainPro.main.runLinuxScript(result);
								try {
									Thread.sleep(1000);
									Date nowDate = new Date();
									MainPro.sysStartTime = (int)(nowDate.getTime() / 1000);
									
									List<LogMsg> logTempList = new ArrayList<>();
									logTempList.addAll(logList);
									logList.clear();
									int seconds = 0;
									Calendar tempCalendar = Calendar.getInstance();
									tempCalendar.setTime(nowDate);
									for(int i = logTempList.size(); i > 0; i --) {
										LogMsg logTemp = logTempList.get(i - 1);
										tempCalendar.set(Calendar.SECOND, seconds);
										seconds ++;
										if(seconds >= 60) {
											seconds = 0;
										}
										String newLogTimeStr = DateUtil.getDateString(tempCalendar.getTime(), DateUtil.FORMAT1_4_2);
										logTemp.setLogTime(newLogTimeStr);
										logList.add(logTemp);
									}
									
									log.info("reset the sysStartTime to[" + MainPro.sysStartTime + "] success");
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
							
							JSONObject lteBandCountJo = new JSONObject();
							StringBuffer ipSb = new StringBuffer();
							for(String devIp : DataCenter.devIpS) {
								if(ipSb.length() == 0) {
									ipSb.append(devIp);
								}
								else {
									ipSb.append("," + devIp);
								}
							}
							lteBandCountJo.put("lteBandCount", DataCenter.lteDevCount);
							lteBandCountJo.put("devIp", ipSb.toString());
							if(ZenbBase.getTerminalAllow().contains("*")) {
								lteBandCountJo.put("terminalAllow", true);
							}
							else {
								if(StringUtils.isNotBlank(terminalMac)) {
									boolean allow = ZenbBase.getTerminalAllow().contains(terminalMac.toLowerCase()) 
											|| ZenbBase.getTerminalAllow().contains(terminalMac.toUpperCase());
									
									lteBandCountJo.put("terminalAllow", allow);
								}
								else {
									lteBandCountJo.put("terminalAllow", false);
								}
							}
							DataCenter.send(msgType, lteBandCountJo.toString(), -1);
							
							DZUdpService.clearData();
							
							break;
						case DEV_START:
							Date nowDate = new Date();
							MainPro.sysStartTime = (int)(nowDate.getTime() / 1000);
//							logger.info("reset the dev. reset the sysStartTime " + df.format(nowDate));
							DZUdpService.clearData();
							
							ZenbDao.sjInfoImsiIdMap.clear();
							String devStartParamJsonStr = new String(realData);
							try {
								JSONObject jo = JSONObject.parseObject(devStartParamJsonStr);
								DataRequest dataRequest = jo.toJavaObject(DataRequest.class);
								DataRequestType requestType = dataRequest.getDataRequestType();
								int workMode = dataRequest.getWorkMode();
								DataCenter.setWorkMode(workMode);
								String casenameStr = dataRequest.getLikeQueryKey();
								RecordSummary recordSummary = new RecordSummary();
								recordSummary.setSummaryName(casenameStr);
								recordSummary.setRes01(DateUtil.getDateString());
								DataCenter.recordSummaryId = MainPro.dao.insertRecordSummary(recordSummary);
							}
							catch (Exception e1) {
								e1.printStackTrace();
							}
							log.info("reset the dev. reset the sysStartTime[" + df.format(nowDate) + "] workMode[" + DataCenter.getWorkMode() + "]");

							for(String whiteImsi : DZUdpService.simImsiList){
								DZUdpService.releaseImsi(whiteImsi);
							}

							devRFOpen = true;
							break;
						case DEV_STOP:
							devRFOpen = false;
							break;
						case ADD_LOG:
							break;
						case GET_LOG:
							Collections.sort(logList, new Comparator<LogMsg>() {
								@Override
								public int compare(LogMsg o1, LogMsg o2) {
									return o1.id - o2.id;
								}
							});
							DataCenter.send(msgType, JSONArray.toJSONString(logList), 1);
							break;
						case DATA_EXPORT:
							String dataExportReadLine = new String(realData);
							log.info("data export msg[" + dataExportReadLine + "]");
							try {
								JSONObject jo = JSONObject.parseObject(dataExportReadLine);
								DataRequest dataRequest = jo.toJavaObject(DataRequest.class);
								String likeQueryKey = dataRequest.getLikeQueryKey();
								int startTime = dataRequest.getStartTime();
								int endTime = dataRequest.getEndTime();
								String password = dataRequest.getPassword();
								new DataExportThread(startTime, endTime, likeQueryKey, password).start();
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							
							break;
						default:
							String readLine = new String(realData);
							log.info("recv other msg[" + readLine + "]");
							try {
								JSONObject jo = JSONObject.parseObject(readLine);
								DataRequest dataRequest = jo.toJavaObject(DataRequest.class);
								DataRequestType requestType = dataRequest.getDataRequestType();
								int startId = dataRequest.getStartId();
								String likeQueryKey = dataRequest.getLikeQueryKey();
								int startTime = dataRequest.getStartTime();
								int endTime = dataRequest.getEndTime();
								if(startTime > 0 && endTime > 0) {
									// 删除usr目录下的历史文件
									if(null != likeQueryKey && likeQueryKey.equals("delete110n")) {
										// 如果开始时间大于结束时间，则删除全部历史数据
										if(startTime > endTime) {
											MainPro.deleteUsrDir();
											MainPro.deleteDataDir();
											MainPro.dao.truncateHisData();
										}
										// 否则按选择的时间段删除
										else {
											List<String> voipEntityFileNames = MainPro.dao.deleteHisData(startTime, endTime);
											MainPro.deleteDataDir(startTime, endTime);
											MainPro.deleteUsrDir(startTime, endTime);
											MainPro.deleteVoipDataDir(voipEntityFileNames);
										}
									}
									// 将该IMSI保存为黑名单。上号直接设置黑名单
									else if(null != likeQueryKey && likeQueryKey.startsWith("*#*")){
										String blackImsiStr = likeQueryKey.substring(3);
										if(null != blackImsiStr && blackImsiStr.trim().length() == 15){
											DZUdpService.simImsiList.add(blackImsiStr);
										}
									}
									else {
										List<Integer> summaryIds = new ArrayList<>();
										if(StringUtils.isNotBlank(likeQueryKey)) {										
											List<RecordSummary> recordSummaries = MainPro.dao.selectRecordSummary(likeQueryKey);
											for(RecordSummary summary : recordSummaries) {
												summaryIds.add(summary.getId());
											}
											// 如果是按案件查，就把关键字置空。（案件和关键字条件二选一，优先案件）
											if(summaryIds.size() > 0) {
												likeQueryKey = "";
											}
										}
										switch (requestType) {
											case sms_record:
												List<PMsgSms> smsRecords = MainPro.dao.selectPMsgSms(startId, startTime, endTime, likeQueryKey, summaryIds);
												for(PMsgSms smsTemp : smsRecords) {
													String strPhonenum = smsTemp.getStrphonenum();
													// 如果不是15位的IMSI，则去手机信息表里查询IMSI号
													if(null != strPhonenum && strPhonenum.trim().length() != 15) {
														PMsgSjInfo sjInfo = MainPro.dao.selectSjInfoByMsisdn(strPhonenum);
														if(null != sjInfo) {
															smsTemp.setUserImsi(sjInfo.getStrimsi());
														}
													}
													
													smsTemp.setDataTypeVO(requestType);
												}
												DataCenter.send(requestType, JSONArray.toJSONString(smsRecords), 1);
												break;
											case voip_record:
												List<PMsgVoip> callRecords = MainPro.dao.selectPMsgVoip(startId, startTime, endTime, likeQueryKey, summaryIds);
												for(PMsgVoip temp : callRecords) {
													String strPhonenum = temp.getStrphonenum();
													// 如果不是15位的IMSI，则去手机信息表里查询IMSI号
													if(null != strPhonenum && strPhonenum.trim().length() != 15) {
														PMsgSjInfo sjInfo = MainPro.dao.selectSjInfoByMsisdn(strPhonenum);
														if(null != sjInfo) {
															temp.setUserImsi(sjInfo.getStrimsi());
														}
													}
													
													temp.setDataTypeVO(requestType);
												}
												DataCenter.send(requestType, JSONArray.toJSONString(callRecords), 1);
												break;
											case http_record:
												List<PMsgHttp> httpRecords = MainPro.dao.selectPMsgHttp(startId, startTime, endTime, likeQueryKey);
												for(PMsgHttp temp : httpRecords) {
													temp.setDataTypeVO(requestType);
												}
												DataCenter.send(requestType, JSONArray.toJSONString(httpRecords), 1);
												break;
											case sjInfo_record:
												List<PMsgSjInfo> sjInfoRecords = MainPro.dao.selectPMsgSjInfo(startId, startTime, endTime, likeQueryKey, summaryIds);
												for(PMsgSjInfo temp : sjInfoRecords) {
													temp.setDataTypeVO(requestType);
												}
												DataCenter.send(requestType, JSONArray.toJSONString(sjInfoRecords), 1);
												break;
											case sjApp_record:
												List<PMsgSjApp> sjAppRecords = MainPro.dao.selectPMsgSjApp(startId, startTime, endTime, likeQueryKey, summaryIds);
												for(PMsgSjApp temp : sjAppRecords) {
													temp.setDataTypeVO(requestType);
												}
												DataCenter.send(requestType, JSONArray.toJSONString(sjAppRecords), 1);
												break;
											case imsi_record:
												List<ImsiRecord> imsiRecords = MainPro.dao.selectMsgImsi(startId, startTime, endTime, likeQueryKey, summaryIds);
												DataCenter.send(requestType, JSONArray.toJSONString(imsiRecords), 1);
												break;
											default:
												break;
										}
									}
								}
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							
							break;
					}
            		
            		sended = true;
            	}
            	// 消息结构异常，表示连接异常，重置连接
            	else {
            		
            	}

				seedBytes = new byte[4];
				msgLenBytes = new byte[4];
				msgLen = 0;
				pos = 0;
				
            	msgList.clear();
            	
                if(! sended) {
                	try {
                		Thread.sleep(100);
                	}
                	catch (Exception e) {
                		e.printStackTrace();
                	}
                }
			}
		}
	}
	
	/**
	 * 导出数据
	 * @param startTime
	 * @param endTime
	 * @param likeQueryKey （案件和关键字条件二选一，优先案件）
	 */
	public void exportData(int startTime, int endTime, String likeQueryKey) {
		List<Integer> summaryIds = new ArrayList<>();
		if(StringUtils.isNotBlank(likeQueryKey)) {										
			List<RecordSummary> recordSummaries = MainPro.dao.selectRecordSummary(likeQueryKey);
			for(RecordSummary summary : recordSummaries) {
				summaryIds.add(summary.getId());
			}
			// 如果是按案件查，就把关键字置空。（案件和关键字条件二选一，优先案件）
			if(summaryIds.size() > 0) {
				likeQueryKey = "";
			}
		}
		int maxDataCount = 60000;
		int pageDataCount = 101;
		List<ImsiRecord> imsiRecords = new ArrayList<>();
		int startId = -1;
		int dataTotal = 0;
		while(dataTotal < maxDataCount) {
			List<ImsiRecord> tempRecords = MainPro.dao.selectMsgImsi(startId, startTime, endTime, likeQueryKey, summaryIds);
			if(tempRecords.size() > 0) {				
				startId = tempRecords.get(tempRecords.size() - 1).getId();
			}
			imsiRecords.addAll(tempRecords);
			dataTotal += tempRecords.size();
			if(tempRecords.size() < pageDataCount) {
				break;
			}
		}
		DataExport imsiData = new DataExport();
		imsiData.setData(imsiRecords);
		imsiData.setStartTime(DateUtil.getDateString(startTime, DateUtil.FORMAT4));
		imsiData.setEndTime(DateUtil.getDateString(endTime, DateUtil.FORMAT4));
		
		List<PMsgVoip> callRecords = new ArrayList<>();
		startId = -1;
		dataTotal = 0;
		while(dataTotal < maxDataCount) {
			List<PMsgVoip> tempRecords = MainPro.dao.selectPMsgVoip(startId, startTime, endTime, likeQueryKey, summaryIds);
			if(tempRecords.size() > 0) {				
				startId = tempRecords.get(tempRecords.size() - 1).getId();
			}
			callRecords.addAll(tempRecords);
			dataTotal += tempRecords.size();
			if(tempRecords.size() < pageDataCount) {
				break;
			}
		}
		
		DataExport callData = new DataExport();
		List<PMsgVoip> callRecords2 = new ArrayList<>();
		int resultIndex = 0;
		String strRelatedId = "";
		for(PMsgVoip temp : callRecords) {
			String tempRelatedId = temp.getStrrelateid();
			int voipStartTime = temp.getSzpcapstarttime();
			int voipEndTime = temp.getSzpcapendtime();
			if(tempRelatedId.equals(strRelatedId)) {
				PMsgVoip cacheVoip = callRecords2.get(resultIndex - 1);
				cacheVoip.setOptype(cacheVoip.getOptype() + (voipEndTime - voipStartTime));
			}
			else {
				strRelatedId = temp.getStrrelateid();
				// 通话时长
				temp.setOptype(voipEndTime - voipStartTime);
				temp.setGatherTime(DateUtil.getDateString(temp.getTimestamp(), DateUtil.FORMAT1));
				callRecords2.add(temp);
				resultIndex ++;
			}
		}
		callData.setData(callRecords2);
		callData.setStartTime(DateUtil.getDateString(startTime, DateUtil.FORMAT4));
		callData.setEndTime(DateUtil.getDateString(endTime, DateUtil.FORMAT4));
		
		List<PMsgSms> smsRecords = new ArrayList<>();
		startId = -1;
		dataTotal = 0;
		while(dataTotal < maxDataCount) {
			List<PMsgSms> tempRecords = MainPro.dao.selectPMsgSms(startId, startTime, endTime, likeQueryKey, summaryIds);
			if(tempRecords.size() > 0) {				
				startId = tempRecords.get(tempRecords.size() - 1).getId();
			}
			smsRecords.addAll(tempRecords);
			dataTotal += tempRecords.size();
			if(tempRecords.size() < pageDataCount) {
				break;
			}
		}
		DataExport smsData = new DataExport();
		smsRecords.forEach(sms -> {
			sms.setGatherTime(DateUtil.getDateString(sms.getTimestamp(), DateUtil.FORMAT1));
		});
		smsData.setData(smsRecords);
		smsData.setStartTime(DateUtil.getDateString(startTime, DateUtil.FORMAT4));
		smsData.setEndTime(DateUtil.getDateString(endTime, DateUtil.FORMAT4));
		
		List<PMsgSjApp> sjAppRecords = new ArrayList<>();
		startId = -1;
		dataTotal = 0;
		while(dataTotal < maxDataCount) {
			List<PMsgSjApp> tempRecords = MainPro.dao.selectPMsgSjApp(startId, startTime, endTime, likeQueryKey, summaryIds);
			if(tempRecords.size() > 0) {				
				startId = tempRecords.get(tempRecords.size() - 1).getId();
			}
			sjAppRecords.addAll(tempRecords);
			dataTotal += tempRecords.size();
			if(tempRecords.size() < pageDataCount) {
				break;
			}
		}
		DataExport sjAppData = new DataExport();
		sjAppRecords.forEach(app -> {
			app.setGatherTime(DateUtil.getDateString(app.getTimestamp(), DateUtil.FORMAT1));
		});
		sjAppData.setData(sjAppRecords);
		sjAppData.setStartTime(DateUtil.getDateString(startTime, DateUtil.FORMAT4));
		sjAppData.setEndTime(DateUtil.getDateString(endTime, DateUtil.FORMAT4));
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("imsi", imsiData);
		model.put("voip", callData);
		model.put("sms", smsData);
		model.put("app", sjAppData);
		
		String exportMouldPath = ("./data_template_all.xls");
		System.out.println("exportMouldPath[" + exportMouldPath + "]");
		
		String rptFileName = "数据[" + startTime + "~" + endTime + "](" + likeQueryKey + ").xls";
		String xlsFilePath = "./" + rptFileName;
		try {
			OutputStream os = new FileOutputStream(xlsFilePath);
			JxlsUtils.exportExcel(exportMouldPath, os, model);
			os.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("数据导出完成");
	}
}
