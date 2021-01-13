package com.zrt.zenb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrt.lteDZ.vo.*;
import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.controller.vo.DZDev;
import com.zrt.zenb.entity.ImsiRecord;
import com.zrt.zenb.entity.LogMsg;
import com.zrt.zenb.entity.MsgBase;
import com.zrt.zenb.entity.PMsgSjInfo;
import com.zrt.zenb.entity.enums.DataRequestType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台服务和设备交互控制
 * @author intent
 */
@Slf4j
public class DevController implements JsonKeyConst {

    List<DZDev> devData = new ArrayList<>();

    public void initDevInfos(){
        String[] devIps = DataCenter.devIpS;
        for(int i = 0; i < devIps.length; i ++){
            DZDev dzDev = new DZDev();
            dzDev.setDevIndex(i);
            dzDev.setIpStr(devIps[i]);
            devData.add(dzDev);

        }
    }


    /**
     * 参数配置设置
     * @param modIndex
     * @param arfcn
     * @param pci
     * @param gpsSync
     * @param gpsOffset
     */
    public void devCfg(int modIndex, int arfcn, int pci, boolean gpsSync, int gpsOffset){



    }

    /**
     * 4.	配置查询
     * @param bsParamMsg
     * @param devIndex
     */
    public void dispDevParam(ParamSetMsg bsParamMsg, int devIndex){
        devData.get(devIndex).setCfgMsg(bsParamMsg);
    }

    /**
     * 6.	状态查询
     * @param statusRptMsg
     * @param devIndex
     */
    public void dispDevStatus(StatusRptMsg statusRptMsg, int devIndex){

    }

    /**
     * 8.	IMSI号码上报
     * @param imsiRptMsg
     * @param devIndex
     */
    public void dispImsiRpt(ImsiRptMsg imsiRptMsg, int devIndex){

    }

    /**
     * 9.	IMSI状态上报
     * @param imsiStatusRptMsg
     * @param devIndex
     */
    public void dispImsiStatusRpt(ImsiTeidRptMsg imsiStatusRptMsg, int devIndex){

    }

    public void dispRFStatusRpt(RFStatusRspMsg rfStatusMsg, int devIndex){

    }

    public void dispLogMsg(AlarmNotify alarmNotify, int devIndex){

    }

    public void dispSysCfg(JSONObject msgJo){

    }

    /**
     * 10.	手机号码上报
     * @param msgJo
     */
    public void dispImsiMsisdn(JSONObject msgJo){

    }

    public void dispData(JSONArray msgJa, int requestType){
        List<MsgBase> msgObjList = new ArrayList<>();
        List<LogMsg> logMsgs = new ArrayList<>();
        boolean isLogMsg = false;
        for(int i = 0; i < msgJa.size(); i ++){
            JSONObject tempJo = (JSONObject)msgJa.get(i);
            if(requestType == DataRequestType.GET_LOG.ordinal()){
                LogMsg logMsg = tempJo.toJavaObject(LogMsg.class);
                logMsgs.add(logMsg);
                log.info(logMsg.toString());
                isLogMsg = true;
            }
            else if(requestType == DataRequestType.sms_record.ordinal()){
            }
            else if(requestType == DataRequestType.voip_record.ordinal()){
            }
            else if(requestType == DataRequestType.http_record.ordinal()){
            }
            else if(requestType == DataRequestType.sjInfo_record.ordinal()){
                PMsgSjInfo sjInfo = JSONObject.toJavaObject(tempJo, PMsgSjInfo.class);
                sjInfo.setDataTypeVO(DataRequestType.sjInfo_record);
                msgObjList.add(sjInfo);
            }
            else if(requestType == DataRequestType.sjApp_record.ordinal()){
            }
            else if(requestType == DataRequestType.imsi_record.ordinal()){
                ImsiRecord imsiRecord = JSONObject.toJavaObject(tempJo, ImsiRecord.class);
                imsiRecord.setGatherCount(0);
                imsiRecord.setDataTypeVO(DataRequestType.imsi_record);
                msgObjList.add(imsiRecord);
            }
        }
    }
}
