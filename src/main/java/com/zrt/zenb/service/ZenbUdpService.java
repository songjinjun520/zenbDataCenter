package com.zrt.zenb.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zrt.common.util.ByteUtils;
import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.entity.TcpMsg;
import com.zrt.zenb.entity.ZenbMsg;
import com.zrt.zenb.entity.enums.DataRequestType;
import lombok.extern.slf4j.Slf4j;

/**
 * 与zenbsever通信
 * 
 * @author intent
 *
 */
@Slf4j
public class ZenbUdpService {
	
	private String logTag = this.getClass().getSimpleName();
	
	private DatagramSocket socketFromZenb = null;
	
	private int recvFromDev = 0;
	
	public ZenbUdpService() {
		try {
			socketFromZenb = new DatagramSocket(DataCenter.zenbDstLinsteningPort);
//			new Thread(sendRunnable).start();
			new Thread(recvRunnable).start();
			
			DataCenter.setSocketFromZenb(socketFromZenb);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
//	private Runnable sendRunnable = new Runnable() {
//        public void run() {
//        	while(true) {
//        		boolean sended = false;
//        		ZenbMsg zenbMsg = DataCenter.recvFromUiZenbData.poll();
//				if(zenbMsg != null && null != socketFromZenb) {
//					byte[] data = zenbMsg.getMsgContent();
//                    try {
//                        InetAddress addr = InetAddress.getByName("127.0.0.1");
//                        DatagramPacket sendPacket = new DatagramPacket(data, data.length, addr, DataCenter.zenbSrcLinsteningPort);
//                        socketFromZenb.send(sendPacket);
//                        sended = true;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//				}
//				
//				if(! sended) {
//					try {
//						Thread.sleep(100L);
//					}
//					catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//        	}
//        }
//	};
	
    private Runnable recvRunnable = new Runnable() {
        @Override
		public void run() {
            while (true) {
            	boolean recv = false;
                if (socketFromZenb != null) {
                	byte[] buffer = new byte[4096];
                	DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    try {
                    	socketFromZenb.receive(packet);
                    	int datalen = packet.getLength();
                    	byte[] data = packet.getData();
                    	byte[] realData = new byte[datalen];
                    	try {
							System.arraycopy(data, 0, realData, 0, datalen);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
                    	log.info("recv zenb_msg [" + ByteUtils.arrayBytesToString(realData) + "]");

                    	DataCenter.recvFromDevData.add(new TcpMsg(realData, DataRequestType.zenb_msg));
                    	
                        recv = true;
                    } catch (Exception e) {
                        e.printStackTrace();
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
}
