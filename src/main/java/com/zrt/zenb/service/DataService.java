/**
 *
 */
package com.zrt.zenb.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.zrt.zenb.MainPro;
import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.common.FileUtils;
import com.zrt.zenb.common.StringUtils;
import com.zrt.zenb.entity.PMsgHttp;
import com.zrt.zenb.entity.PMsgSjApp;
import com.zrt.zenb.entity.PMsgSjInfo;
import com.zrt.zenb.entity.PMsgSms;
import com.zrt.zenb.entity.PMsgVoip;
import com.zrt.zenb.entity.enums.DataRequestType;

/**
 * @author PGW
 *
 */
@Slf4j
public class DataService implements Runnable {
	private static Logger logger = Logger.getLogger(DataService.class);
	
	/**
	 * 全数字时间秒数正则验证
	 */
	String numberRegex = "^[0-9-]+$";
	Pattern numberPattern = Pattern.compile(numberRegex);
	
	private boolean running;

	private String dataPath;

	private String targetPath;

	private int checkInterval;
	private File lastDealMillisecondDir = null;
	private String lastDealMillisecondDirName = "";
	private List<String> hasDealedFileNames = new ArrayList<>();
    
    /**
     *
     */
    public DataService(String dataPath, String targetPath, int checkInterval) {
        running = true;
        this.dataPath = dataPath;
        this.targetPath = targetPath;
        this.checkInterval = checkInterval;
        
        // 第一次启动时，得到最大的毫秒数目录，后面的目录处理，从这个目录开始
        File baseDir = new File(dataPath);
        File[] dirs = baseDir.listFiles();
        if(null != dirs) {
        	for (File dir : dirs) {
        		if (!dir.isDirectory()) {
        			continue;
        		}
        		
        		String tempDirName = dir.getName();
        		// 进入时间秒数目录
        		Matcher match = numberPattern.matcher(tempDirName);
        		if(match.matches()) {
        			if(tempDirName.compareTo(lastDealMillisecondDirName) > 0) {
        				lastDealMillisecondDirName = tempDirName;
        				lastDealMillisecondDir = dir;
        			}
        		}
        	}
        }
        
        log.info("dataPath[" + dataPath + "]");
        log.info("targetPath[" + targetPath + "]");
        log.info("checkInterval[" + checkInterval + "]");
        log.info("lastDealMillisecondDirName[" + lastDealMillisecondDirName + "]");
    }

    long time1 = System.currentTimeMillis();
    @Override
    public void run() {
        while (running) {
            try {
                // 休眠时间
                Thread.sleep(checkInterval);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 检查目标目录，列举所有文件
            File baseDir = new File(dataPath);
            
//                        FileUtils.copyFolder(dataPath, targetPath);
            
            File[] dirs = baseDir.listFiles();
            String dirName = "";
            if(null == dirs) {
//                        	System.err.println(dataPath + " has no file. continue wait.");
            	continue;
            }
            boolean hasDealXmlFile = false;
            for (File dir : dirs) {
                if (! dir.isDirectory()) {
                    continue;
                }
                
                dirName = dir.getName();
                
                // 进入时间秒数目录
                Matcher match = numberPattern.matcher(dirName);
                if(match.matches()) {
                	System.err.println("currentDirName[" + dirName + "]");
                	if(dirName.compareTo(lastDealMillisecondDirName) >= 0) {
                		System.err.println("---------deal currentDirName[" + dirName + "]");
                		millisecondDirRead(dir);
                		FileUtils.deleteDir(lastDealMillisecondDir);
                		lastDealMillisecondDirName = dirName;
                		lastDealMillisecondDir = dir;
                		hasDealXmlFile = true;
                	}
                }
                // 进入VOIP目录
                // 进入hour目录
                // 进入volte目录
                // 进入时间秒数目录
            }
            long time2 = System.currentTimeMillis();
            if(hasDealXmlFile) {
            	logger.info("本次文件处理耗时[" + (time2 - time1) + "ms]");
            	System.err.println("xml deal time[" + (time2 - time1) + "ms]");
            }
            time1 = time2;
//            FileUtils.deleteOnlyFile(baseDir);
        }
    }

    public void stop() {
            running = false;
    }
    
    /**
     * 毫秒数目录的处理
     * // 进入Entity目录
        // 分拣协议
        // 入库
        // 移动目录

        // 进入xml目录
        // 分拣协议
        // 入库
        // 移动目录
     * @param millisecondDir /data/192.168.1.1/a13/1566197410
     */
    private void millisecondDirRead(File millisecondDirFile) {
    	File[] fileDirs = millisecondDirFile.listFiles();
    	for (File dirFile : fileDirs) {
    		if (!dirFile.isDirectory()) {
    			continue;
    		}
    		String dirName = dirFile.getName();
    		if(dirName.equals("entity")) {
    			
    		}
    		else if(dirName.equals("xml")) {
    			xmlDirRead(dirFile);
    		}
    	}
    	
    	
    }
    
    /**
     * @param xmlDirFile /data/192.168.1.1/a13/1566197410/xml
     */
    private void xmlDirRead(File xmlDirFile) {
    	File[] fileDirs = xmlDirFile.listFiles();
    	for (File bcpFile : fileDirs) {
    		Boolean bcpDealed = false;
    		if (bcpFile.isDirectory()) {
    			continue;
    		}
    		String bcpFileName = bcpFile.getName();
    		if(hasDealedFileNames.contains(bcpFileName)) {
    			continue;
    		}
//        		String bcpFilePath = bcpFile.getParent();
    		System.out.println("bcpFilePath[" + bcpFile.getPath() + "] bcpFileName[" + bcpFileName + "]");
    		if(bcpFileName.indexOf("smsinfo") > 0) {
//        			System.out.println("--------------smsinfo----------------");
    			List<PMsgSms> smslist = new ArrayList<>();
    			try {
					InputStreamReader isr = new InputStreamReader(new FileInputStream(bcpFile), "UTF-8");  
					BufferedReader br = new BufferedReader(isr, 81920);
					String readLineStr = "";
					while((readLineStr = br.readLine()) != null) {
						readLineStr = readLineStr.trim();
//							System.out.println(readLineStr);
						String[] contentStrs = readLineStr.split("\t");
						if(contentStrs.length >= PMsgSms.fileContentsLen) {
							PMsgSms sms = new PMsgSms(contentStrs);
							smslist.add(sms);
						}
					}
					br.close();
					isr.close();
					
					MainPro.dao.insertPMsgSmss(smslist);
					DataCenter.send(DataRequestType.sms_record, JSONArray.toJSONString(smslist), 0);
					
					bcpDealed = true;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
    			
    		}
    		else if(bcpFileName.indexOf("mobinfo") > 0) {
//        			System.out.println("--------------mobinfo----------------");
    			List<PMsgSjInfo> datalist = new ArrayList<>();
    			try {
					FileReader fr = new FileReader(bcpFile);
					BufferedReader br = new BufferedReader(fr);
					String readLineStr = "";
					while((readLineStr = br.readLine()) != null) {
						readLineStr = readLineStr.trim();
//							System.out.println(readLineStr);
						String[] contentStrs = readLineStr.split("\t");
						if(contentStrs.length >= PMsgSjInfo.fileContentsLen) {
							PMsgSjInfo data = new PMsgSjInfo(contentStrs);
							datalist.add(data);
							String msisdnStr = data.getStrmsisdn();
							// 如果为取号模式，并且已经取到了MSISDN，则将该IMSI置为释放目标
							if(! StringUtils.isBlank(msisdnStr)) {
								String imsiStr = data.getStrimsi();
								
								DZUdpService.forceSendImsiMsisdn(imsiStr, msisdnStr);
								
								if(DataCenter.getWorkMode() == 2) {
									DZUdpService.releaseImsi(imsiStr);
								}
							}
						}
					}
					br.close();
					fr.close();
					List<PMsgSjInfo> tempList = new ArrayList<>();
					for(PMsgSjInfo temp : datalist) {
						MainPro.dao.insertPMsgSjInfo(temp);
						tempList.add(temp);
					}
					DataCenter.send(DataRequestType.sjInfo_record, JSONArray.toJSONString(tempList), 0);
					
					bcpDealed = true;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
    		}
    		else if(bcpFileName.indexOf("sjapp") > 0) {
//        			System.out.println("--------------sjapp----------------");
    			List<PMsgSjApp> datalist = new ArrayList<>();
    			try {
					InputStreamReader isr = new InputStreamReader(new FileInputStream(bcpFile), "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					String readLineStr = "";
					while((readLineStr = br.readLine()) != null) {
						readLineStr = readLineStr.trim();
//							System.out.println(readLineStr);
						String[] contentStrs = readLineStr.split("\t");
//							System.out.println("contentStrs.length[" + contentStrs.length + "]");
						if(contentStrs.length >= PMsgSjApp.fileContentsLen) {
							PMsgSjApp data = new PMsgSjApp(contentStrs);
							datalist.add(data);
						}
					}
					br.close();
					isr.close();
					
					MainPro.dao.insertPMsgSjApp(datalist);
					DataCenter.send(DataRequestType.sjApp_record, JSONArray.toJSONString(datalist), 0);
					
					bcpDealed = true;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
    		}
    		else if(bcpFileName.indexOf("volte") > 0) {
    			List<PMsgVoip> datalist = new ArrayList<>();
    			try {
					FileReader fr = new FileReader(bcpFile);
					BufferedReader br = new BufferedReader(fr);
					String readLineStr = "";
					while((readLineStr = br.readLine()) != null) {
						readLineStr = readLineStr.trim();
//							System.out.println(readLineStr);
						String[] contentStrs1 = readLineStr.split("\t");
						String[] contentStrs = new String[PMsgVoip.fileContentsLen];
						System.arraycopy(contentStrs1, 0, contentStrs, 0, contentStrs1.length);
						PMsgVoip data = new PMsgVoip(contentStrs);
						if(data.getStrfromusername().trim().length() > 0
								|| data.getStrfromphonenum().trim().length() > 0
								|| data.getStrtousername().trim().length() > 0
								|| data.getStrtophonenum().trim().length() > 0) {
							datalist.add(data);
						}
						else {
							System.err.println("voip from and to all null");
						}
					}
					br.close();
					fr.close();
					
					MainPro.dao.insertPMsgVoip(datalist);
					DataCenter.send(DataRequestType.voip_record, JSONArray.toJSONString(datalist), 0);
					
					bcpDealed = true;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
    		}
    		else if(bcpFileName.indexOf("httpget") > 0) {
    			List<PMsgHttp> datalist = new ArrayList<>();
    			try {
					FileReader fr = new FileReader(bcpFile);
					BufferedReader br = new BufferedReader(fr);
					String readLineStr = "";
					while((readLineStr = br.readLine()) != null) {
						readLineStr = readLineStr.trim();
//							System.out.println(readLineStr);
						String[] contentStrs = readLineStr.split("\t");
//							System.out.println("contentStrs.length[" + contentStrs.length + "]");
						if(contentStrs.length >= PMsgHttp.fileContentsLen) {
							PMsgHttp data = new PMsgHttp(contentStrs);
							datalist.add(data);
						}
					}
					br.close();
					fr.close();
					
					MainPro.dao.insertPMsgHttp(datalist);
					
					bcpDealed = true;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
    		}
    		else {
    			System.err.println(bcpFileName + " no deal.");
    			bcpDealed = true;
    		}
    		// 处理一个，复制删除一个
    		// 将该bcp文件复制到usr对应目录下。
    		if(bcpDealed) {
    			hasDealedFileNames.add(bcpFileName);
    			FileUtils.moveFile(bcpFile);
    		}
    	}
    }
        
}
