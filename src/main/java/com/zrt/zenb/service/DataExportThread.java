package com.zrt.zenb.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.zrt.zenb.MainPro;
import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.common.DateUtil;
import com.zrt.zenb.common.JxlsUtils;
import com.zrt.zenb.entity.DataExport;
import com.zrt.zenb.entity.ImsiRecord;
import com.zrt.zenb.entity.PMsgSjApp;
import com.zrt.zenb.entity.PMsgSms;
import com.zrt.zenb.entity.PMsgVoip;
import com.zrt.zenb.entity.RecordSummary;
import com.zrt.zenb.entity.TcpMsg;
import com.zrt.zenb.entity.enums.DataRequestType;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class DataExportThread {

	int startTime = -1;
	int endTime = -1;
	String likeQueryKey = "";
	String password = "password";
	
	String imsiPercent = "10";
	String voipPercent = "25";
	String smsPercent = "40";
	String sjAppPercent = "55";
	String filePercent = "70";
	
	public DataExportThread(int startTime, int endTime, String likeQueryKey, String password) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.likeQueryKey = likeQueryKey;
		this.password = password;
	}
	
	Runnable dataExportRunnable = new Runnable() {
		@Override
		public void run() {
			JSONObject exportDataCountJo = new JSONObject();
			
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
			exportDataCountJo.put("IMSI", imsiRecords.size());
			DataExport imsiData = new DataExport();
			imsiData.setData(imsiRecords);
			imsiData.setStartTime(DateUtil.getDateString(startTime, DateUtil.FORMAT4));
			imsiData.setEndTime(DateUtil.getDateString(endTime, DateUtil.FORMAT4));
			System.out.println("数据导出IMSI查询完成");
			DataCenter.recvFromDevData.add(new TcpMsg(imsiPercent, DataRequestType.DATA_EXPORT_PROGRESS));
			
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
			exportDataCountJo.put("通话", callRecords2.size());
			callData.setData(callRecords2);
			callData.setStartTime(DateUtil.getDateString(startTime, DateUtil.FORMAT4));
			callData.setEndTime(DateUtil.getDateString(endTime, DateUtil.FORMAT4));
			System.out.println("数据导出voip查询完成");
			DataCenter.recvFromDevData.add(new TcpMsg(voipPercent, DataRequestType.DATA_EXPORT_PROGRESS));
			
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
			exportDataCountJo.put("短信", smsRecords.size());
			smsData.setData(smsRecords);
			smsData.setStartTime(DateUtil.getDateString(startTime, DateUtil.FORMAT4));
			smsData.setEndTime(DateUtil.getDateString(endTime, DateUtil.FORMAT4));
			System.out.println("数据导出sms查询完成");
			DataCenter.recvFromDevData.add(new TcpMsg(smsPercent, DataRequestType.DATA_EXPORT_PROGRESS));
			
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
			exportDataCountJo.put("手机APP", sjAppRecords.size());
			sjAppData.setData(sjAppRecords);
			sjAppData.setStartTime(DateUtil.getDateString(startTime, DateUtil.FORMAT4));
			sjAppData.setEndTime(DateUtil.getDateString(endTime, DateUtil.FORMAT4));
			System.out.println("数据导出sjApp查询完成");
			DataCenter.recvFromDevData.add(new TcpMsg(sjAppPercent, DataRequestType.DATA_EXPORT_PROGRESS));
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("imsi", imsiData);
			model.put("voip", callData);
			model.put("sms", smsData);
			model.put("app", sjAppData);
			
			String exportMouldPath = ("./data_template_all.xls");
			System.out.println("exportMouldPath[" + exportMouldPath + "]");
			
//			String rptFileName = "数据[" + startTime + "~" + endTime + "](" + likeQueryKey + ").xls";
			String rptFileName = "data_file_out.xls";
			String xlsFilePath = "./" + rptFileName;
			try {
				OutputStream os = new FileOutputStream(xlsFilePath);
				JxlsUtils.exportExcel(exportMouldPath, os, model);
				os.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			DataCenter.recvFromDevData.add(new TcpMsg(filePercent, DataRequestType.DATA_EXPORT_PROGRESS));
			
			System.out.println("数据导出为excel完成");
			
			// 生成的压缩文件
			ZipParameters zipParameters = new ZipParameters();

			String zipFileName = "data_export_" + System.currentTimeMillis()/1000 + ".zip";
			ZipFile zipFile = null;
			if(null != password && password.trim().length() > 0){
				zipParameters.setEncryptFiles(true);
				zipParameters.setEncryptionMethod(EncryptionMethod.AES);
				// Below line is optional. AES 256 is used by default. You can override it to use AES 128. AES 192 is supported only for extracting.
				zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
				zipFile = new ZipFile(zipFileName, password.trim().toCharArray());
			}
			else {
				zipParameters.setEncryptFiles(false);
				zipFile = new ZipFile(zipFileName);
			}

			try {
				zipFile.addFile(xlsFilePath, zipParameters);
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				File f = new File("./" + zipFileName);
				FileInputStream fis = new FileInputStream(f);
				byte[] tempBytes = new byte[1024];
				int totalLen = 0;
				int readLen = 0;
				int totalLen2 = fis.available();
				while((readLen = fis.read(tempBytes)) > 0) {
					byte[] contentBytes = new byte[readLen];
					System.arraycopy(tempBytes, 0, contentBytes, 0, readLen);
					totalLen += readLen;
					DataCenter.recvFromDevData.add(new TcpMsg(contentBytes, DataRequestType.DATA_EXPORT_F_DATA));
					
					int fileTransmitPercent = 30 * totalLen / totalLen2;
					DataCenter.recvFromDevData.add(new TcpMsg(String.valueOf(fileTransmitPercent + 70), DataRequestType.DATA_EXPORT_PROGRESS));
				}
				exportDataCountJo.put("文件总大小", totalLen2 / 1000 + "KB");
				System.out.println("传输文件总长度：" + totalLen2);
				DataCenter.recvFromDevData.add(new TcpMsg(exportDataCountJo.toString(), DataRequestType.DATA_EXPORT_F_DATA_END));
				
				fis.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	public void start() {
		Thread thread = new Thread(dataExportRunnable);
		thread.start();
	}
	
}
