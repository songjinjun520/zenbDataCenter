package com.zrt.zenb.controller.vo;

import com.zrt.lteDZ.vo.AlarmNotify;
import com.zrt.lteDZ.vo.BaseMsg;
import com.zrt.lteDZ.vo.ImsiSetMsg;
import com.zrt.lteDZ.vo.Imsi_INFO;
import com.zrt.lteDZ.vo.ParamSetMsg;
import com.zrt.lteDZ.vo.RFControlMsg;
import com.zrt.lteDZ.vo.RFStatusRspMsg;
import com.zrt.lteDZ.vo.StatusRptMsg;
import com.zrt.lteDZ.vo.SyncStatusRptMsg;
import com.zrt.lteDZ.vo.TrafficControlMsg;
import com.zrt.lteDZ.vo.enums.ControlType;
import com.zrt.lteDZ.vo.enums.LteSysMode;
import com.zrt.lteDZ.vo.enums.MsgType;
import com.zrt.zenb.common.BusinessUtil;
import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.controller.interf.CallBackOpr;
import com.zrt.zenb.controller.service.TcpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author intent
 */
@Slf4j
public class DZDev {
    private String logTag = this.getClass().getSimpleName();

    private String ipStr;
    /**
     * 根据IP配置顺序指定的设备序号
     */
    private int devIndex;
    private ParamSetMsg cfgMsg = null;

    private int bandName = 0;
    /**
     * 是否小区建立成功（就绪）
     */
    private String statusStr = "";
    /**
     * 射频状态
     */
    private int rfStatus = -1;

    /**
     * 是否已经加载过最新配置
     */
    private boolean paramLoaded = false;
    /**
     * 射频状态是否已读取
     */
    private boolean rfStatusLoaded = false;
    /**
     * 输入框的值。相对于从设备读取的值
     */
    private int ARFCN = 0;
    /**
     * 输入框的值。相对于从设备读取的值
     */
    private int phyCellId = 0;
    /**
     * 输入框的值。相对于从设备读取的值
     */
    private boolean gpssync = false;

    /**
     * 移动46000
     * 联通46001
     * 电信46011
     */
    private String plmn = "46000";
    /**
     * 转发方式 0 关闭转发  1 仅转发  2 转发并回传（M转发）
     */
    private int trafficCtlValue = 1;
    /**
     * 监控范围 0 全部 1 仅目标 （M转发时必须仅目标）
     */
    private byte trafficModeValue = 0;
    /**
     * 基站重启成功的标识<br>
     *     -1 未发送重启动作
     *     0 发送重启命令
     *     1 重启成功
     */
    private int rebootStatus = -1;

    /**
     * 是否需要重启基站（频点、PCI、GPS同步方式改变需要重启基站生效）
     */
    private boolean needReboot = false;
    /**
     * 配置重发次数
     */
    private int cfgResendCount = 0;

    /**
     * 配置重发次数限制5次
     */
    private final int cfgResendCountLimit = 5;
    /**
     * 重启基站已耗时（s）
     */
    private int rebootTimeLost = 0;

    /**
     * 基站重启超时时间（180s=3分钟）
     */
    public static final int rebootTimeout = 240;

    /**
     * 设备当前配置项状态（网络参数配置，基站重启，转发方式配置，监控范围配置，目标配置，射频启动）
     */
    private String cfgStatusStr = "";

    private StatusRptMsg statusRptMsg = null;
    private RFStatusRspMsg rfStatusRspMsg = null;
    private SyncStatusRptMsg syncStatusRptMsg = null;
    private ImsiSetMsg imsiSetMsg = null;
    private TrafficControlMsg trafficMsg = null;
    private TrafficControlMsg trafficModeMsg = null;
    private AlarmNotify alarmNotify = null;


    private long rebootTime = 0;
    private boolean cfgReq = false;
    private boolean rebootReq = false;
    private boolean trafficCtrlReq = false;
    private boolean trafficModeReq = false;
    private boolean tgtReq = false;
    private boolean rfReq = false;

    private boolean cfgAck = false;
    private boolean rebootAck = false;
    private boolean trafficCtrlAck = false;
    private boolean trafficModeAck = false;
    private boolean tgtAck = false;
    private boolean rfAck = false;

    /**
     * 设备 状态--是否完成  集合
     */
    private Map<DevStatus, CtrlStatus> devStatus = new HashMap<>();

    /**
     * 网络参数配置成功
     */
    private CallBackOpr paramCfgAckCallBack = null;
    /**
     * 基站重启成功
     */
    private CallBackOpr bsRebootAckCallBack = null;
    /**
     * 转发方式配置成功
     */
    private CallBackOpr trafficCfgAckCallBack = null;
    /**
     * 监控范围配置成功
     */
    private CallBackOpr trafficModeCfgAckCallBack = null;
    /**
     * 目标配置成功
     */
    private CallBackOpr tgtCfgAckCallBack = null;
    /**
     * 射频配置成功
     */
    private CallBackOpr rfCfgAckCallBack = null;

    TcpClient lteUdpService = DataCenter.tcpClient;

    public DZDev(){
        paramCfgAckCallBack = this::bsReboot;
        bsRebootAckCallBack = this::trafficSetup;
        trafficCfgAckCallBack = this::trafficModeSetup;
        trafficModeCfgAckCallBack = this::tgtCfg;
        tgtCfgAckCallBack = () -> rfSetup(true);

        devStatus.put(DevStatus.starting, CtrlStatus.unknown);
        devStatus.put(DevStatus.ipsec_connecting, CtrlStatus.unknown);
        devStatus.put(DevStatus.cell_starting, CtrlStatus.unknown);
        devStatus.put(DevStatus.alarm, CtrlStatus.unknown);
    }

    public String getIpStr() {
        return ipStr;
    }

    public void setIpStr(String ipStr) {
        this.ipStr = ipStr;
    }

    public int getDevIndex() {
        return devIndex;
    }

    public void setDevIndex(int devIndex) {
        this.devIndex = devIndex;
    }

    public ParamSetMsg getCfgMsg() {
        return cfgMsg;
    }

    public void setCfgMsg(ParamSetMsg cfgMsg) {
        log.info("setCfgMsg[" + ipStr + "][" + cfgMsg + "]");
        this.cfgMsg = cfgMsg;
        this.bandName = BusinessUtil.validateEarfcn(cfgMsg.getARFCN());
        this.plmn = cfgMsg.getPLMN();
        setParamLoaded(true);
    }

    public int getBandName() {
        return bandName;
    }

    public void setBandName(int bandName) {
        this.bandName = bandName;
    }

    public String getStatusStr() {
        return getDevStatus().getDesc();
    }

    public int getRfStatus() {
        return rfStatus;
    }

    public void setRfStatus(int rfStatus) {
        this.rfStatus = rfStatus;
    }

    public boolean isParamLoaded() {
        return paramLoaded;
    }

    public void setParamLoaded(boolean paramLoaded) {
        this.paramLoaded = paramLoaded;
    }

    public boolean isRfStatusLoaded() {
        return rfStatusLoaded;
    }

    public void setRfStatusLoaded(boolean rfStatusLoaded) {
        this.rfStatusLoaded = rfStatusLoaded;
    }

    public int getARFCN() {
        return ARFCN;
    }

    public void setARFCN(int ARFCN) {
        this.ARFCN = ARFCN;
    }

    public int getPhyCellId() {
        return phyCellId;
    }

    public void setPhyCellId(int phyCellId) {
        this.phyCellId = phyCellId;
    }

    public boolean isGpssync() {
        return gpssync;
    }

    public void setGpssync(boolean gpssync) {
        this.gpssync = gpssync;
    }

    public int getRebootTimeLost() {
        return rebootTimeLost;
    }

    /**
     * 如果3个都为false，表示设备初始化，则将0置为true<br/>
     * 如果前2个为true，则表示设备完成断电重启，则将3置为true
     */
    public void setDevPrepared(){
        log.info("setDevPrepared[" + ipStr + "][" + rebootStatus + "][" + rebootReq + "]");
        if(rebootStatus == 0){
            rebootStatus = 1;
            if(rebootReq){
                // 重启命令发送之后的30s之后的心跳消息才作为重启成功的参照依据
                long nowTime = System.currentTimeMillis();
                if((nowTime - rebootTime) / 1000 > 30){
                    rebootAck = true;
                }
            }
        }
    }

    public void setIpcellSetAck(BaseMsg ipcellSetAck) {
        log.info("网络参数设置成功了！");
        if(cfgReq){
            cfgAck = true;
        }
    }

    public StatusRptMsg getStatusRptMsg() {
        return statusRptMsg;
    }

    public void setStatusRptMsg(StatusRptMsg statusRptMsg) {
        this.statusRptMsg = statusRptMsg;
        if(statusRptMsg.getStatus() == 12){
            log.info("设备准备重启");
            reinitParams(true);
            rebootStatus = 0;
        }
    }

    public RFStatusRspMsg getRfStatusRspMsg() {
        return rfStatusRspMsg;
    }

    public void setRfStatusRspMsg(RFStatusRspMsg rfStatusRspMsg) {
        this.rfStatusRspMsg = rfStatusRspMsg;
        int rfStatus = rfStatusRspMsg.getRfStatus();
        log.info("dispRFStatusRspMsg[" + rfStatus + "] lteDevIndex[" + ipStr + "]");
        setRfStatus(rfStatus);
        setRfStatusLoaded(true);
    }

    public SyncStatusRptMsg getSyncStatusRptMsg() {
        return syncStatusRptMsg;
    }

    public void setSyncStatusRptMsg(SyncStatusRptMsg syncStatusRptMsg) {
        this.syncStatusRptMsg = syncStatusRptMsg;
    }

    public void setImsiSetAck(BaseMsg imsiSetAck) {
        log.info("目标设置成功了！[" + ipStr + "]");
        tgtAck = true;
    }

    public ImsiSetMsg getImsiSetMsg() {
        return imsiSetMsg;
    }

    public void setImsiSetMsg(ImsiSetMsg imsiSetMsg) {
        this.imsiSetMsg = imsiSetMsg;
    }

    public void setTrafficCtrlSetAck(BaseMsg trafficCtrlSetAck) {
        log.info("流量转发方式设置成功！");
        if(trafficCtrlReq){
            trafficCtrlAck = true;
        }
    }

    public TrafficControlMsg getTrafficMsg() {
        return trafficMsg;
    }

    public void setTrafficMsg(TrafficControlMsg trafficMsg) {
        this.trafficMsg = trafficMsg;
    }

    public void setTrafficMonitorModeSetAck(BaseMsg trafficMonitorModeSetAck) {
        log.info("监控范围设置成功！");
        if(trafficModeReq){
            trafficModeAck = true;
        }
    }

    public TrafficControlMsg getTrafficModeMsg() {
        return trafficModeMsg;
    }

    public void setTrafficModeMsg(TrafficControlMsg trafficModeMsg) {
        this.trafficModeMsg = trafficModeMsg;
    }

    public void setRfCtrlAck(BaseMsg rfCtrlAck) {
        log.info("射频设置成功！");
        if(rfReq){
            rfAck = true;
        }
    }

    public AlarmNotify getAlarmNotify() {
        return alarmNotify;
    }

    public void setAlarmNotify(AlarmNotify alarmNotify) {
        this.alarmNotify = alarmNotify;
    }

    /**
     * @return 如果频点、PCI、GPS同步方式三者有1个改变了，则需要配置并重启基站，否则，不需要配置
     */
    public boolean needCfgAndReboot(){
        if(null != cfgMsg){
            int oldArfcn = cfgMsg.getARFCN();
            int oldPci = cfgMsg.getPhyCellId();
            boolean gpsSync = cfgMsg.isGpssync();
            String oldPlmn = cfgMsg.getPLMN();

            log.info("oldParam[" + oldArfcn + "][" + oldPci + "][" + gpsSync + "][" + oldPlmn + "]");

            int newArfcn = getARFCN();
            int newPci = getPhyCellId();
            boolean newGpsSync = isGpssync();

            log.info("newParam[" + newArfcn + "][" + newPci + "][" + newGpsSync + "][" + plmn + "]");

//            if(oldArfcn != newArfcn
//                    || oldPci != newPci
//                    || gpsSync != newGpsSync
//                    || ! oldPlmn.equals(plmn)){
            if(oldPci != newPci
                    || gpsSync != newGpsSync){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    ScheduledExecutorService cfgSetupHandler = Executors.newScheduledThreadPool(4);

    /**
     * @param needCal 是否需要判定是否需要发送配置。参数重发则不需要判断
     */
    public void cfgSetup(boolean needCal){
        log.info("基站参数配置[" + ipStr + "]");
        boolean needCfg = true;
        if(needCal){
            needCfg = needCfgAndReboot();
        }

        reinitParams(true);

        needReboot = needCfg;

        if(needCfg){
            try {
                ParamSetMsg cfgMsg = getCfgMsg();
                cfgMsg.setARFCN(getARFCN());
                cfgMsg.setPhyCellId(getPhyCellId());
                cfgMsg.setGpssync(gpssync);
                cfgMsg.setMsgType(MsgType.IPCELL_SET_REQ);

                log.info("基站参数配置[" + ipStr + "][" + cfgMsg + "]消息长度[" + cfgMsg.getMsgLen() + "]");

                if(null != lteUdpService){
                    lteUdpService.sendMsg(cfgMsg, devIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            cfgReq = true;
            cfgStatusStr = "基站参数配置";
            log.info("dev[" + this + "][" + cfgStatusStr + "]");

            cfgResendCount ++;

            cfgSetupHandler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    log.info("检查一下配置是否有回复[" + ipStr + "]，没回复，重发配置，最大重发次数5次[" + cfgAck + "]");
                    if(cfgAck){
                        cfgResendCount = 0;
                        if(null != paramCfgAckCallBack){
                            paramCfgAckCallBack.callbackOpr();
                        }
                        cfgSetupHandler.shutdown();
                    }
                    else {
                        if(cfgResendCount >= cfgResendCountLimit){
                            log.info("网络参数配置无响应");
                            cfgSetupHandler.shutdown();
                        }
                        else {
                            cfgSetup(false);
                        }
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
        else {
            log.info("参数配置不变，不需要重发配置[" + ipStr + "]");
            cfgAck = true;
            if(null != paramCfgAckCallBack){
                paramCfgAckCallBack.callbackOpr();
            }
        }
    }

    public void bsReboot(){
        log.info("重启基站[" + ipStr + "]");

        if(needReboot){
            if(null != lteUdpService){
                lteUdpService.sendMsg(new BaseMsg(MsgType.IPCELL_REBOOT), devIndex);
            }
            cfgStatusStr = "重启基站";
            log.info("dev[" + this + "][" + cfgStatusStr + "]");
            rebootStatus = 0;
            rebootReq = true;
            rebootTime = System.currentTimeMillis();
            cfgSetupHandler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    log.info("检查重启是否成功[" + ipStr + "]，没成功，继续等待，直到超时[" + rebootAck + "]");
                    if(rebootAck){
                        rebootTimeLost = 0;

                        if(null != bsRebootAckCallBack){
                            bsRebootAckCallBack.callbackOpr();
                        }
                        cfgSetupHandler.shutdown();
                    }
                    else {
                        if(rebootTimeLost >= rebootTimeout){
                            log.info("基站重启超时。。。");
                            cfgSetupHandler.shutdown();
                        }
                        else {
                            log.info("基站重启等待次数[" + rebootTimeLost + "]");
                            rebootTimeLost ++;
                        }
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
        else {
            log.info("不需要重启基站[" + ipStr + "]");
            rebootAck = true;

            if(null != bsRebootAckCallBack){
                bsRebootAckCallBack.callbackOpr();
            }
        }
    }

    /**
     * 转发方式设置 0 关闭 1 仅转发 2 转发并回传（M转发）
     */
    public void trafficSetup(){
        if(null != lteUdpService){
            log.info("转发开关设置。值为[" + trafficCtlValue + "] 目标个数：[0]");

            TrafficControlMsg trafficControlMsg = new TrafficControlMsg(trafficCtlValue);
            trafficControlMsg.setMsgType(MsgType.IPCELL_TRAFFIC_CTRL_SET_REQ);

            lteUdpService.sendMsg(trafficControlMsg, devIndex);
            trafficCtrlReq = true;
            cfgStatusStr = "转发开关设置[仅转发]";
            log.info("dev[" + this + "][" + cfgStatusStr + "]");
        }

        cfgSetupHandler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("检查转发方式配置是否有回复[" + ipStr + "]，没回复，重发配置，最大重发次数5次[" + trafficCtrlAck + "]");
                if(trafficCtrlAck){
                    cfgResendCount = 0;

                    if(null != trafficCfgAckCallBack){
                        trafficCfgAckCallBack.callbackOpr();
                    }
                    cfgSetupHandler.shutdown();
                }
                else {
                    if(cfgResendCount >= cfgResendCountLimit){
                        log.info("转发方式配置无响应");
                        cfgSetupHandler.shutdown();
                    }
                    else {
                        trafficSetup();
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * 监控范围设置 0 全部 1 仅目标
     */
    public void trafficModeSetup(){
        log.info("监控范围设置");

        if(null != lteUdpService){
            log.info("转发模式设置[" + trafficModeValue + "]");
            TrafficControlMsg trafficControlMsg = new TrafficControlMsg(trafficModeValue);
            trafficControlMsg.setMsgType(MsgType.IPCELL_TRAFFIC_MONITOR_MODE_SET_REQ);
            lteUdpService.sendMsg(trafficControlMsg, devIndex);
            trafficModeReq = true;
            cfgStatusStr = "转发模式设置";
            log.info("dev[" + this + "][" + cfgStatusStr + "]");
        }

        cfgSetupHandler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("检查监控范围配置是否有回复[" + ipStr + "]，没回复，重发配置，最大重发次数5次[" + trafficModeAck + "]");
                if(trafficModeAck){
                    cfgResendCount = 0;

                    if(null != trafficModeCfgAckCallBack){
                        trafficModeCfgAckCallBack.callbackOpr();
                    }
                    cfgSetupHandler.shutdown();
                }
                else {
                    if(cfgResendCount >= cfgResendCountLimit){
                        log.info("监控范围配置无响应");
                        cfgSetupHandler.shutdown();
                    }
                    else {
                        trafficModeSetup();
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void tgtCfg(){
        log.info("目标设置[" + ipStr + "]");

        ImsiSetMsg imsiDelMsg = new ImsiSetMsg(MsgType.IPCELL_IMSI_DEL_REQ);
        imsiDelMsg.setImsiNum(0);
        if(null != lteUdpService){
            lteUdpService.sendMsg(imsiDelMsg, devIndex);
        }
        log.info("删除设备里所有的目标[" + ipStr + "]");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tgtAck = true;
        tgtReq = true;
        cfgStatusStr = "目标设置";
        log.info("dev[" + this + "][" + cfgStatusStr + "]");

        cfgSetupHandler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("检查目标配置是否有回复[" + ipStr + "]，没回复，重发配置，最大重发次数5次。重发[" + cfgResendCount + "]次。[" + tgtAck + "]");
                if(tgtAck){
                    cfgResendCount = 0;

                    if(null != tgtCfgAckCallBack){
                        tgtCfgAckCallBack.callbackOpr();
                    }
                    cfgSetupHandler.shutdown();
                }
                else {
                    if(cfgResendCount >= cfgResendCountLimit){
                        log.info("目标配置无响应");
                        cfgSetupHandler.shutdown();
                    }
                    else {
                        tgtCfg();
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void rfSetup(final boolean rfOpen){
        rfAck = false;

        log.info("基站[" + ipStr + "]射频[" + (rfOpen ? "打开]" : "关闭]"));

        RFControlMsg rfControlMsg = new RFControlMsg(rfOpen);
        lteUdpService.sendMsg(rfControlMsg, devIndex);
        rfReq = true;

        setRfStatusLoaded(false);
        setRfStatus(-1);

        cfgStatusStr = "射频[" + (rfOpen ? "打开]" : "关闭]");

        cfgSetupHandler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("检查射频配置是否有回复[" + ipStr + "]，没回复，重发配置，最大重发次数5次[" + rfAck + "]");
                if(rfAck){
                    cfgResendCount = 0;

                    if(null != rfCfgAckCallBack){
                        rfCfgAckCallBack.callbackOpr();
                    }
                    cfgSetupHandler.shutdown();

                    if(! rfOpen){
                        reinitParams(false);
                    }
                }
                else {
                    if(cfgResendCount >= cfgResendCountLimit){
                        log.info("目标配置无响应");
                        cfgSetupHandler.shutdown();
                    }
                    else {
                        rfSetup(rfOpen);
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void reinitParams(boolean clearStatus){
        log.info("reinitParams[" + ipStr + "]");

        rebootStatus = -1;

        needReboot = false;
        cfgResendCount = 0;

        rebootTimeLost = 0;

        cfgStatusStr = "";

        cfgReq = false;
        trafficCtrlReq = false;
        trafficModeReq = false;
        tgtReq = false;
        rfReq = false;

        cfgAck = false;
        trafficCtrlAck = false;
        trafficModeAck = false;
        tgtAck = false;
        rfAck = false;

        if(clearStatus){
            devStatus.put(DevStatus.ipsec_connecting, CtrlStatus.unknown);
            devStatus.put(DevStatus.cell_starting, CtrlStatus.unknown);
            devStatus.put(DevStatus.starting, CtrlStatus.unknown);
            devStatus.put(DevStatus.alarm, CtrlStatus.unknown);
        }
    }

    public int getTrafficCtlValue() {
        return trafficCtlValue;
    }

    public void setTrafficCtlValue(int trafficCtlValue) {
        this.trafficCtlValue = trafficCtlValue;
    }

    public byte getTrafficModeValue() {
        return trafficModeValue;
    }

    public void setTrafficModeValue(byte trafficModeValue) {
        this.trafficModeValue = trafficModeValue;
    }

    public String getCfgStatusStr() {
        return cfgStatusStr;
    }

    public boolean isCfgAck() {
        return cfgAck;
    }

    public boolean isRebootAck() {
        return rebootAck;
    }

    public boolean isTrafficCtrlAck() {
        return trafficCtrlAck;
    }

    public boolean isTrafficModeAck() {
        return trafficModeAck;
    }

    public boolean isTgtAck() {
        return tgtAck;
    }

    public boolean isRfAck() {
        return rfAck;
    }

    public DevStatus getDevStatus() {
//        log.info("");
//        log.info("");
//        log.info("");
//        for(DevStatus status : devStatus.keySet()){
//            log.info(status + "--" + devStatus.get(status));
//        }

        if(devStatus.get(DevStatus.alarm) == CtrlStatus.start){
            return DevStatus.alarm;
        }
        else {
            if(devStatus.get(DevStatus.cell_starting) == CtrlStatus.ok){
                return DevStatus.prepared;
            }
            else if(devStatus.get(DevStatus.cell_starting) == CtrlStatus.start){
                return DevStatus.cell_starting;
            }
            else if(devStatus.get(DevStatus.ipsec_connecting) == CtrlStatus.ok){
                return DevStatus.ipsec_success;
            }
            else if(devStatus.get(DevStatus.ipsec_connecting) == CtrlStatus.start){
                return DevStatus.ipsec_connecting;
            }
            else if(devStatus.get(DevStatus.starting) == CtrlStatus.ok){
                return DevStatus.starting_success;
            }
            else if(devStatus.get(DevStatus.starting) == CtrlStatus.start){
                return DevStatus.starting;
            }
        }

        return DevStatus.starting;
    }

    /**
     * @param devStatusArg  状态
     * @param ctrlStatus start 流程开始  ok 流程成功
     * @return 如果有状态变化，则通知界面刷新
     */
    public boolean setDevStatus(DevStatus devStatusArg, CtrlStatus ctrlStatus) {
        boolean updateFlag = true;
        if(devStatus.get(devStatusArg) == ctrlStatus){
            updateFlag = false;
        }
        else {
            this.devStatus.put(devStatusArg, ctrlStatus);
        }

        return updateFlag;
    }

    public enum CtrlStatus {
        /**
         * 未知
         */
        unknown,
        /**
         * 流程开始
         */
        start,
        /**
         * 流程成功
         */
        ok;
    }

    public enum DevStatus {
        /**
         * 初始化中（未收到系统上电完成）
         */
        starting,
        starting_success,
        /**
         * 建链中（IPSEC）
         */
        ipsec_connecting,
        ipsec_success,
        /**
         * 小区建立中
         */
        cell_starting,
        /**
         * 小区建立成功
         */
        prepared,
        /**
         * 告警
         */
        alarm;

        public String getDesc(){
            String statusDesc = "";
            switch (this){
                case starting:
                    statusDesc = "初始化中";
                    break;
                case starting_success:
                    statusDesc = "初始化完成";
                    break;
                case ipsec_connecting:
                    statusDesc = "建链中";
                    break;
                case ipsec_success:
                    statusDesc = "建链完成";
                    break;
                case cell_starting:
                    statusDesc = "小区建立中";
                    break;
                case prepared:
                    statusDesc = "已就绪";
                    break;
                case alarm:
                    statusDesc = "告警";
                    break;
                default:
                    break;
            }
            return statusDesc;
        }
    }

    public String getPlmn() {
        return plmn;
    }

    public void setPlmn(String plmn) {
        this.plmn = plmn;
    }

    public boolean isFddFromPlmn(){
        return "46001".equals(plmn) || "46011".equals(plmn);
    }

    public boolean isFddFromArfcn(){
        if(ARFCN < 3800){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "DZDev{" +
                "ipStr='" + ipStr + '\'' +
                '}';
    }
}
