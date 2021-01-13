package com.zrt.zenb.controller.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrt.common.util.ByteUtils;

import com.zrt.lteDZ.vo.AlarmNotify;
import com.zrt.lteDZ.vo.BaseMsg;
import com.zrt.lteDZ.vo.CellInfoMsg;
import com.zrt.lteDZ.vo.FreqScanMsg;
import com.zrt.lteDZ.vo.ImsiRptMsg;
import com.zrt.lteDZ.vo.ImsiSetMsg;
import com.zrt.lteDZ.vo.ImsiTeidRptMsg;
import com.zrt.lteDZ.vo.ParamSetMsg;
import com.zrt.lteDZ.vo.RFStatusRspMsg;
import com.zrt.lteDZ.vo.StatusRptMsg;
import com.zrt.lteDZ.vo.SyncStatusRptMsg;
import com.zrt.lteDZ.vo.TrafficControlMsg;
import com.zrt.lteDZ.vo.enums.MsgType;
import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.common.StringUtils;
import com.zrt.zenb.controller.DevController;
import com.zrt.zenb.entity.DataRequest;
import com.zrt.zenb.entity.TcpMsg;
import com.zrt.zenb.entity.ZenbMsg;
import com.zrt.zenb.entity.enums.DataRequestType;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author intent
 */
@Slf4j
public class TcpClient implements Runnable {

    private String logTag = this.getClass().getSimpleName();

    private String devIp = "127.0.0.1";
    private int devPort = 4028;

    Socket clientSocket = null;
    InputStream is;
    OutputStream os;

    boolean uiWorking = true;
    boolean devConnect = false;
    ConcurrentLinkedQueue<TcpMsg> requestMsgQueue = new ConcurrentLinkedQueue<>();
    /**
     * UI收到设备的消息
     */
    private ConcurrentLinkedQueue<Byte> recvFromDevData = new ConcurrentLinkedQueue<>();

    /**
     * 没收到NUC返回的TCP_CONNECT消息（携带band信息）时，其他消息先不处理，避免找设备时下标异常
     */
    boolean tcpConnectAck = false;

    DevController devController = DataCenter.devController;

    public TcpClient(){
        new Thread(new ReceiveRunnable()).start();
        new Thread(new SendRunnable()).start();
        new Thread(new MsgDealRunnable()).start();
    }

    private void startConnect(){
        try {
            clientSocket = new Socket(devIp, devPort);
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();

            devConnect = true;

            log.info("TCP connect true.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(uiWorking){
            if(devConnect){
                // ADD_LOG消息当作心跳消息。只os.write可以检测出断线。pw.println检测不出来。
                // 这个作为心跳消息
                DataRequest dataRequest = new DataRequest();
                dataRequest.setDataRequestType(DataRequestType.ADD_LOG);
                dataRequest.setLikeQueryKey(String.valueOf(System.currentTimeMillis()));
                if(null != os){
                    TcpMsg tcpMsg = new TcpMsg(JSONObject.toJSONString(dataRequest), DataRequestType.ADD_LOG);
                    try {
                        byte[] msgBytes = tcpMsg.getBytes();
                        // 生成1000~9000的随机数作为加密密钥
                        Random random = new Random(System.currentTimeMillis());
                        int randomInt = random.nextInt(4000) + 1000;
                        int randomInt2 = random.nextInt(4000) + 5000;
                        byte[] seedBytes1 = ByteUtils.int2bytes(randomInt);
                        byte[] seedBytes2 = ByteUtils.int2bytes(randomInt2);
                        byte[] seedBytes = new byte[4];
                        System.arraycopy(seedBytes1, 0, seedBytes, 0, 2);
                        System.arraycopy(seedBytes2, 0, seedBytes, 2, 2);
                        byte[] encode2Bytes = ByteUtils.xorEncode(msgBytes, seedBytes);

                        byte[] resultMsgBytes = new byte[msgBytes.length + 4];
                        System.arraycopy(seedBytes, 0, resultMsgBytes, 0, 4);
                        System.arraycopy(encode2Bytes, 0, resultMsgBytes, 4, encode2Bytes.length);

                        log.info("sendData2.heartBeat[" + StringUtils.getByteArrayString(resultMsgBytes) + "]");

                        os.write(resultMsgBytes);
                        os.flush();
                    } catch (Exception e) {
                        devConnect = false;
                        devDisConnect();
                        e.printStackTrace();
                    }
                }
            }
            else {
                startConnect();
            }

//            try {
//                if(null != clientSocket){
            // TCP粘包导致0xff可能被添加到收消息的前面，导致消息解析失败。改为回车应该可以避免问题。
            // 暂时先不发了。一般为了稳定，2种处理方法，1，单独开一个socket连接  2，心跳消息
////                    clientSocket.sendUrgentData(0xFF);
//                    clientSocket.sendUrgentData(0x0a);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                devConnect = false;
//                startConnect();
//            }

            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    byte[] readBuffer = new byte[2048];
    class ReceiveRunnable implements Runnable {
        // 接收线程
        @Override
        public void run() {
            while(true) {
                try {
                    if(null != is && devConnect){
                        int readLen = is.read(readBuffer);
                        if(readLen > 0) {
                            for(int i = 0; i < readLen && i < readBuffer.length; i ++) {
                                recvFromDevData.offer(readBuffer[i]);
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
                catch (Exception e) {
                    e.printStackTrace();

                    devConnect = false;

                    devDisConnect();
                }
            }
        }
    }

    class SendRunnable implements Runnable {
        // 发送线程
        @Override
        public void run() {
            while(true) {
                TcpMsg tcpMsg = requestMsgQueue.poll();
                if(null != tcpMsg && devConnect){
                    try {
                        if(null != os) {
//                            log.info("send msg to dev.[" + tcpMsg.getMsgType() + "]");
//                            LogFileUtils.setAppendFile("TcpClient-->send msg[" + tcpMsg.getMsgType() + "] to dev.");
                            byte[] msgBytes = tcpMsg.getBytes();
                            log.info("sendData1[" + StringUtils.getByteArrayString(msgBytes) + "]");

                            // 生成1000~9000的随机数作为加密密钥
                            Random random = new Random(System.currentTimeMillis());
                            int randomInt = random.nextInt(4000) + 1000;
                            int randomInt2 = random.nextInt(4000) + 5000;
                            byte[] seedBytes1 = ByteUtils.int2bytes(randomInt);
                            byte[] seedBytes2 = ByteUtils.int2bytes(randomInt2);
                            byte[] seedBytes = new byte[4];
                            System.arraycopy(seedBytes1, 0, seedBytes, 0, 2);
                            System.arraycopy(seedBytes2, 0, seedBytes, 2, 2);
                            byte[] encode2Bytes = ByteUtils.xorEncode(msgBytes, seedBytes);

                            byte[] resultMsgBytes = new byte[msgBytes.length + 4];
                            System.arraycopy(seedBytes, 0, resultMsgBytes, 0, 4);
                            System.arraycopy(encode2Bytes, 0, resultMsgBytes, 4, encode2Bytes.length);

                            log.info("sendData2[" + StringUtils.getByteArrayString(resultMsgBytes) + "]");
                            os.write(resultMsgBytes);
                            os.flush();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();

                        devConnect = false;
                        devDisConnect();
                    }
                }
                else {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
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
     * 处理从设备收到的消息。
     *
     * @author intent
     *
     */
    class MsgDealRunnable implements Runnable {
        @Override
        public void run() {
            while(true) {
                boolean msgEnd = false;
                while(! msgEnd) {
                    Byte data = recvFromDevData.poll();
                    if(null != data) {
                        if(pos < 4) {
                            seedBytes[pos] = data;

                            pos ++;
                        }
                        else if(pos < 8) {
                            msgLenBytes[pos - 4] = data;
                            if(pos == 7) {
                                byte[] realMsgLenBytes = ByteUtils.xorEncode(msgLenBytes, seedBytes);
                                msgLen = ByteUtils.bytes2int(realMsgLenBytes);
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

                    log.info("recv1[" + StringUtils.getByteArrayString(allMsg) + "]");

                    byte[] msgContentBytes2 = ByteUtils.xorEncode(allMsg, seedBytes);
                    List<Byte> msgList2 = new ArrayList<>();
                    for(int i = 4; i < msgContentBytes2.length; i ++) {
                        msgList2.add(msgContentBytes2[i]);
                    }

                    log.info("recv2[" + StringUtils.getByteArrayString(msgContentBytes2) + "]");

                    int msgListSize = msgList2.size();
                    if(msgListSize > 2){
                        byte tail1 = msgList2.get(msgListSize - 2);
                        byte tail2 = msgList2.get(msgListSize - 1);
                        if(tail1 != TcpMsg.msgTails[0] || tail2 != TcpMsg.msgTails[1]){
                            log.info("消息结尾标识不正确，重置连接");

                            devConnect = false;
                            devDisConnect();

                            continue;
                        }
                    }

                    TcpMsg tcpMsg = new TcpMsg(msgList2);
                    DataRequestType requestType2 = tcpMsg.getMsgType();
//                    log.info("requestType2[" + requestType2 + "]");
                    int devIndex = tcpMsg.getDevIndex();
                    byte[] msgBytes = tcpMsg.getMsgBodyBytes();
                    switch (requestType2) {
                        // 收到设备的控制消息
                        case transmission:
                            BaseMsg baseMsg = new BaseMsg(msgBytes);
                            MsgType msgType = baseMsg.getMsgType();
                            if(tcpConnectAck){
                                dzMsgDeal(msgType, msgBytes, devIndex);
                            }
                            else {
                                log.info("还未读到基带板信息，丢掉基站消息处理[" + msgType + "]");
                            }
                            break;
                        case zenb_msg:
                        case DATA_EXPORT_PROGRESS:
                        case DATA_EXPORT_F_DATA:
                        case DATA_EXPORT_F_DATA_END:
                            break;
                        default:
                            try {
                                dataMsgDeal(msgBytes);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                // 消息结构异常，表示连接异常，重置连接
                else {

                }

                seedBytes = new byte[4];
                msgLenBytes = new byte[4];
                msgLen = 0;
                pos = 0;

                msgList.clear();
            }
        }
    }

    private void dzMsgDeal(MsgType msgType, byte[] msgBytes, int devIndex){
        if(devIndex < 0){
            log.info("错误的设备下标[" + devIndex + "][" + msgType + "]");
            return ;
        }

        switch (msgType){
            case IPCELL_HEARTBEAT:

                break;

            case IPCELL_GET_ACK:
                ParamSetMsg bsParamMsg = new ParamSetMsg(msgBytes);
                log.info("bsParamMsg[" + bsParamMsg.toString() + "]");
                devController.dispDevParam(bsParamMsg, devIndex);
                break;
            case IPCELL_STATUS_REPORT:
                log.info("dzMsgDeal.IPCELL_STATUS_REPORT");

                StatusRptMsg statusRptMsg = new StatusRptMsg(msgBytes);
                devController.dispDevStatus(statusRptMsg, devIndex);
                break;
            case IPCELL_IMSI_REPORT:
                ImsiRptMsg imsiRptMsg = new ImsiRptMsg(msgBytes);
                devController.dispImsiRpt(imsiRptMsg, devIndex);
                break;
            case IPCELL_IMIS_TEID_REPORT:
                ImsiTeidRptMsg imsiTeidRptMsg = new ImsiTeidRptMsg(msgBytes);
                devController.dispImsiStatusRpt(imsiTeidRptMsg, devIndex);
                break;
            case IPCELL_RF_STATUS_GET_ACK:
                RFStatusRspMsg rfStatusMsg = new RFStatusRspMsg(msgBytes);
                log.info("dzMsgDeal.IPCELL_RF_STATUS_GET_ACK[" + rfStatusMsg + "]");
                devController.dispRFStatusRpt(rfStatusMsg, devIndex);
                break;
            case IPCELL_NMM_SYNC_GET_ACK:
                break;
            case IPCELL_NMM_CELL_INFO_GET_ACK:
                break;
            case IPCELL_NMM_FREQ_GET_ACK:
                break;
            case IPCELL_IMSI_DEL_RESP:
                log.info("目标删除成功");
                break;
            case IPCELL_IMSI_INQ_RESP:
                ImsiSetMsg imsiSetMsg = new ImsiSetMsg(msgBytes);
                break;
            case IPCELL_TRAFFIC_CTRL_GET_ACK:
            case IPCELL_TRAFFIC_MONITOR_MODE_GET_ACK:
                TrafficControlMsg controlModeMsg = new TrafficControlMsg(msgBytes);
                break;
            case IPCELL_ALARM_NOTIFY:
                log.info("dzMsgDeal.IPCELL_ALARM_NOTIFY");
                AlarmNotify alarmNotify = new AlarmNotify(msgBytes);
                devController.dispLogMsg(alarmNotify, devIndex);
                break;
            case IPCELL_SET_ACK:
            case IPCELL_IMSI_SET_RESP:
            case IPCELL_RELEASE_ALL_UE_ACK:
            case IPCELL_TRAFFIC_CTRL_SET_ACK:
            case IPCELL_RF_CONTROL_ACK:
            case IPCELL_TRAFFIC_MONITOR_MODE_SET_ACK:
            default:
                log.info("基础类型消息[" + msgType + "]");
                break;
        }
    }

    private void dataMsgDeal(byte[] msgBytes) throws Exception {
        String readLine = new String(msgBytes);
        log.info("readLine[" + readLine + "]");
        JSONObject jsonObject = JSONObject.parseObject(readLine);
        int requestType = jsonObject.getInteger("requestType");
        String msgStr = jsonObject.getString("dataStr");
        int hisQuery = jsonObject.getInteger("hisQuery");
//        log.info("recv data msg [" + DataRequestType.getDataRequestType(requestType) + "][" + msgStr + "]");
        // TCP连接时，从设备里读取band数量
        if(requestType == DataRequestType.TCP_CONNECT.ordinal()){
            JSONObject msgJo = JSONObject.parseObject(msgStr);
            devController.dispSysCfg(msgJo);

            tcpConnectAck = true;
        }
        else if(requestType == DataRequestType.IMSI_MSISDN_MAP.ordinal()){
            JSONObject imsiMsisdnJo = JSONObject.parseObject(msgStr);
            devController.dispImsiMsisdn(imsiMsisdnJo);
        }
        else {
            JSONArray msgJa = JSONArray.parseArray(msgStr);
            devController.dispData(msgJa, requestType);
        }
    }

    private void devDisConnect(){
        log.info("通知界面连接断开，正在重置连接");
    }

    /**
     * @param requestStr 消息体·
     * @param msgType
     */
    public void sendRequest(String requestStr, DataRequestType msgType){
        if(devConnect){
            log.info("sendRequest1[" + requestStr + "][" + msgType + "]");
            requestMsgQueue.offer(new TcpMsg(requestStr, msgType));
        }
    }

    public void stop(){
        log.info("TcpClient stop");
        devConnect = false;
        uiWorking = false;
        try {
            if(null != clientSocket){
                clientSocket.close();
            }
            if(null != is){
                is.close();
            }
			if(null != os){
				os.close();
			}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(BaseMsg baseMsg, int devIndex) {
        if(devConnect && tcpConnectAck){
            log.info("sendMsg[" + baseMsg.getMsgType() + "][transmission]");
            this.requestMsgQueue.offer(new TcpMsg(devIndex, baseMsg.getBytes(), DataRequestType.transmission));
        }
    }

    public void sendMsg(ZenbMsg zenbMsg) {
        if(devConnect){
            log.info("sendMsg[zenb_msg]");
            this.requestMsgQueue.offer(new TcpMsg(-1, zenbMsg.getBytes(), DataRequestType.zenb_msg));
        }
    }
}
