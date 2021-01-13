/**
 *
 */
package com.zrt.zenb.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;

import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.common.DateUtil;
import com.zrt.zenb.entity.ImsiRecord;
import com.zrt.zenb.entity.PMsgHttp;
import com.zrt.zenb.entity.PMsgSjApp;
import com.zrt.zenb.entity.PMsgSjInfo;
import com.zrt.zenb.entity.PMsgSms;
import com.zrt.zenb.entity.PMsgVoip;
import com.zrt.zenb.entity.RecordSummary;

/**
 * @author PGW
 *
 */
public class ZenbDao {
	private SqlSessionTemplate sqlSession;
	 
	public SqlSessionTemplate getSqlSession() {
		return sqlSession;
	}
 
	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	
    public int insertPMsgSmss(List<PMsgSms> lists) {
    	for(PMsgSms temp : lists) {
    		temp.setPid(DataCenter.recordSummaryId);
    		try {
    			sqlSession.insert("sms.insert", temp);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
    	}
        return 1;
    }
    
    public static Map<String, Integer> sjInfoImsiIdMap = new HashMap<>();
    
    public int insertPMsgSjInfo(PMsgSjInfo temp) {
    	temp.setPid(DataCenter.recordSummaryId);
    	String strImsi = temp.getStrimsi();
    	String strMsisdn = temp.getStrmsisdn();
    	if(sjInfoImsiIdMap.containsKey(strImsi)) {
    		int dbId = sjInfoImsiIdMap.get(strImsi);
    		temp.setId(dbId);
    		if(StringUtils.isNotBlank(strMsisdn)) {
    			sqlSession.update("sjInfo.updateByPrimaryKeySelective", temp);
    		}
    		
    		return -1;
    	}
    	else {
    		sqlSession.insert("sjInfo.insert", temp);
    		int id = temp.getId();
    		sjInfoImsiIdMap.put(strImsi, id);
    		return id;
    	}
    }
    
    public int insertPMsgVoip(List<PMsgVoip> lists) {
    	for(PMsgVoip temp : lists) {
    		temp.setPid(DataCenter.recordSummaryId);
    		sqlSession.insert("voip.insert", temp);
    	}
        
        return 1;
    }
    
    public List<ImsiRecord> insertImsi(List<ImsiRecord> lists) {
    	for(ImsiRecord temp : lists) {
    		temp.setSummaryId(DataCenter.recordSummaryId);
    		sqlSession.insert("imsi.insertImsi", temp);
    	}
        
        return lists;
    }

    public void updateImsiCount(ImsiRecord imsiRecord){
		sqlSession.update("imsi.updateImsiCount", imsiRecord);
	}
    
    public int insertPMsgHttp(List<PMsgHttp> lists) {
        return 1;
    }
    
    public int insertPMsgSjApp(List<PMsgSjApp> lists) {
    	for(PMsgSjApp temp : lists) {
    		temp.setPid(DataCenter.recordSummaryId);
    		sqlSession.insert("sjApp.insert", temp);
    	}
        
        return 1;
    }
    
    public List<PMsgSms> selectPMsgSms(int startId, int startTime, int endTime, String likeQueryKey, List<Integer> summaryIds){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	if(startId > 0) {    		
    		paramMap.put("id", startId);
    	}
    	paramMap.put("starttime", startTime);
    	if(endTime > 0) {
    		paramMap.put("endTime", endTime);
    	}
    	if(null != likeQueryKey && likeQueryKey.trim().length() > 0) {
    		paramMap.put("likeQueryKey", likeQueryKey);
    	}
    	if(null != summaryIds && summaryIds.size() > 0) {
    		StringBuffer sb = new StringBuffer();
    		sb.append("t.PID in (");
    		int index = 0;
    		for (Integer summaryId : summaryIds) {
    			if(index == 0) {
    				sb.append(summaryId);
    			}
    			else {
    				sb.append(", " + summaryId);
    			}
    			index += 1;
			}
    		sb.append(")");
    		
    		paramMap.put("summaryIds", sb.toString());
    	}
    	return sqlSession.selectList("sms.selectSmsRecord", paramMap);
    }
    
    public List<PMsgVoip> selectPMsgVoip(int startId, int startTime, int endTime, String likeQueryKey, List<Integer> summaryIds){
    	Map<String, Object> paramMap = new HashMap<>();
    	if(startId > 0) {
    		paramMap.put("id", startId);
    	}
    	paramMap.put("starttime", startTime);
    	if(endTime > 0) {
    		paramMap.put("endTime", endTime);
    	}
    	if(null != likeQueryKey && likeQueryKey.trim().length() > 0) {
    		paramMap.put("likeQueryKey", likeQueryKey);
    	}
    	if(null != summaryIds && summaryIds.size() > 0) {
    		StringBuffer sb = new StringBuffer();
    		sb.append("t.PID in (");
    		int index = 0;
    		for (Integer summaryId : summaryIds) {
    			if(index == 0) {
    				sb.append(summaryId);
    			}
    			else {
    				sb.append(", " + summaryId);
    			}
    			index += 1;
			}
    		sb.append(")");
    		
    		paramMap.put("summaryIds", sb.toString());
    	}
    	return sqlSession.selectList("voip.selectVoipRecord", paramMap);
    }
    public List<PMsgHttp> selectPMsgHttp(int startId, int startTime, int endTime, String likeQueryKey){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("id", startId);
    	paramMap.put("starttime", startTime);
    	if(endTime > 0) {
    		paramMap.put("endTime", endTime);
    	}
    	if(null != likeQueryKey && likeQueryKey.trim().length() > 0) {
    		paramMap.put("likeQueryKey", likeQueryKey);
    	}
//    	return mapper.selectHttpRecord(paramMap);
    	return new ArrayList<PMsgHttp>();
    }
    public List<PMsgSjInfo> selectPMsgSjInfo(int startId, int startTime, int endTime, String likeQueryKey, List<Integer> summaryIds){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("id", startId);
    	paramMap.put("starttime", startTime);
    	if(endTime > 0) {
    		paramMap.put("endTime", endTime);
    	}
    	if(null != likeQueryKey && likeQueryKey.trim().length() > 0) {
    		paramMap.put("likeQueryKey", likeQueryKey);
    	}
    	if(null != summaryIds && summaryIds.size() > 0) {
    		StringBuffer sb = new StringBuffer();
    		sb.append("PID in (");
    		int index = 0;
    		for (Integer summaryId : summaryIds) {
    			if(index == 0) {
    				sb.append(summaryId);
    			}
    			else {
    				sb.append(", " + summaryId);
    			}
    			index += 1;
			}
    		sb.append(")");
    		
    		paramMap.put("summaryIds", sb.toString());
    	}
    	return sqlSession.selectList("sjInfo.selectSjInfoRecord", paramMap);
    }
    public List<PMsgSjApp> selectPMsgSjApp(int startId, int startTime, int endTime, String likeQueryKey, List<Integer> summaryIds){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	if(startId > 0) {
    		paramMap.put("id", startId);
    	}
    	paramMap.put("starttime", startTime);
    	if(endTime > 0) {
    		paramMap.put("endTime", endTime);
    	}
    	if(null != likeQueryKey && likeQueryKey.trim().length() > 0) {
    		paramMap.put("likeQueryKey", likeQueryKey);
    	}
    	if(null != summaryIds && summaryIds.size() > 0) {
    		StringBuffer sb = new StringBuffer();
    		sb.append("t.PID in (");
    		int index = 0;
    		for (Integer summaryId : summaryIds) {
    			if(index == 0) {
    				sb.append(summaryId);
    			}
    			else {
    				sb.append(", " + summaryId);
    			}
    			index += 1;
			}
    		sb.append(")");
    		
    		paramMap.put("summaryIds", sb.toString());
    	}
    	return sqlSession.selectList("sjApp.selectSjAppRecord", paramMap);
    }
    
    public List<ImsiRecord> selectMsgImsi(int startId, int startTime, int endTime, String likeQueryKey, List<Integer> summaryIds){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	if(startId > 0) {    		
    		paramMap.put("id", startId);
    	}
    	String startTimeStr = DateUtil.getDateString(startTime, DateUtil.FORMAT1);
    	paramMap.put("starttime", startTimeStr);
    	System.out.println("imsiQuery[" + startTimeStr + "]");
    	if(endTime > 0) {
    		String endTimeStr = DateUtil.getDateString(endTime, DateUtil.FORMAT1);
    		paramMap.put("endTime", endTimeStr);
    		System.out.println("imsiQuery[" + endTimeStr + "]");
    	}
    	if(null != likeQueryKey && likeQueryKey.trim().length() > 0) {
    		paramMap.put("likeQueryKey", likeQueryKey);
    	}
    	if(null != summaryIds && summaryIds.size() > 0) {
    		StringBuffer sb = new StringBuffer();
    		sb.append("summaryId in (");
    		int index = 0;
    		for (Integer summaryId : summaryIds) {
    			if(index == 0) {
    				sb.append(summaryId);
    			}
    			else {
    				sb.append(", " + summaryId);
    			}
    			index += 1;
			}
    		sb.append(")");
    		
    		paramMap.put("summaryIds", sb.toString());
    	}
    	return sqlSession.selectList("imsi.selectImsi", paramMap);
    }
    
    public PMsgSjInfo selectSjInfoByMsisdn(String msisdn) {
    	return sqlSession.selectOne("sjInfo.selectImsiByMsisdn", "%" + msisdn + "%");
    }
    
    public PMsgSjInfo selectSjInfoByImsi(String imsi) {
    	List<PMsgSjInfo> sjInfoList = sqlSession.selectList("sjInfo.selectSjInfoByImsi", imsi);
    	PMsgSjInfo lastVo = null;
    	for(PMsgSjInfo temp : sjInfoList) {
    		if(StringUtils.isNotBlank(temp.getStrmsisdn())) {
    			return temp;
    		}
    		lastVo = temp;
    	}
    	return lastVo;
    }
    
    public List<PMsgSjInfo> selectSjInfoByImsi(List<String> imsis) {
    	return sqlSession.selectList("sjInfo.selectSjInfoByImsis", imsis);
    }
    
    public void truncateHisData() {
    	sqlSession.update("sms.truncateData");
    	sqlSession.update("voip.truncateData");
    	sqlSession.update("sjInfo.truncateData");
    	sqlSession.update("sjApp.truncateData");
    	sqlSession.update("imsi.truncateData");
		sqlSession.update("busi.truncateData");
    }
    
    public List<String> deleteHisData(int startTime, int endTime) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("starttime", startTime);
    	paramMap.put("endTime", endTime);
    	List<String> voipEntityFileNames = sqlSession.selectList("voip.selectVoipEntityFileName", paramMap);
    	
    	sqlSession.delete("sms.deleteHisData", paramMap);
    	sqlSession.delete("voip.deleteHisData", paramMap);
    	sqlSession.delete("sjInfo.deleteHisData", paramMap);
    	sqlSession.delete("sjApp.deleteHisData", paramMap);
    	sqlSession.delete("imsi.deleteHisData", paramMap);
    	
    	return voipEntityFileNames;
    }
    
    public int insertRecordSummary(RecordSummary recordSummary){
    	sqlSession.insert("busi.insertRecordSummary", recordSummary);
    	return recordSummary.getId();
    }
    
    public List<RecordSummary> selectRecordSummary(String likeKey) {
    	return sqlSession.selectList("busi.selectRecordSummary", "%" + likeKey + "%");
    }
    
    
}
