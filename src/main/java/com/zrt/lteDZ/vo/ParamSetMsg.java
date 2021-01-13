package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;
import com.zrt.lteDZ.vo.enums.BandWidth;
import com.zrt.lteDZ.vo.enums.ControlType;
import com.zrt.lteDZ.vo.enums.LteSysMode;
import com.zrt.lteDZ.vo.enums.MsgType;
import com.zrt.lteDZ.vo.enums.PrachMode;
import com.zrt.zenb.common.StringUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * @author intent
 */
@Slf4j
public class ParamSetMsg extends BaseMsg {
    public static int msgLen = 104;

    private String PLMN;
    private int ARFCN;
    private int TAC;
    private int phyCellId;
    private int cellId;
    private ControlType controlType;
    private int gsmARFCN;
    private int dlulconfig;
    private int specialFrameConfig;
    private int syncARFCN;
    private LteSysMode systemMode;
    private BandWidth bandwidth;
    private int txgain;
    private int rxgain;
    private boolean gpssync;
    private int gpsoffset;
    private PrachMode prachMode;
    private int lteARFCN;
    private int tacUpdateTimer;
    private int RxLevMin;
    private int releaseCause;
    private String secPLMNList;

    public ParamSetMsg(String spType) {
        super(spType);
        setMsgType(MsgType.IPCELL_SET_REQ);
        setMsgLen(msgLen);
    }

    public ParamSetMsg(byte[] src) {
        super(src);
        log.info("msgLen[" + getMsgLen() + "]");
        log.info(StringUtils.getByteArrayString(src));

        // 新接口 2.15.5
        if(getMsgLen() == 24){
            int srcPos = 4;
            byte[] freqBytes = new byte[4];
            System.arraycopy(src, srcPos, freqBytes, 2, 2);
            this.ARFCN = ByteUtils.bytes2int_big_endian2(freqBytes);
            srcPos += 2;

            byte[] pciBytes = new byte[4];
            System.arraycopy(src, srcPos, pciBytes, 2, 2);
            this.phyCellId = ByteUtils.bytes2int_big_endian2(pciBytes);
            srcPos += 2;

            byte[] bandwidthBytes = new byte[4];
            System.arraycopy(src, srcPos, bandwidthBytes, 0, 4);
            int bandwidthInt = ByteUtils.bytes2int_big_endian2(bandwidthBytes);
            this.bandwidth = BandWidth.valueOf2(bandwidthInt);
            srcPos += 4;

            byte[] txgainBytes = new byte[4];
            System.arraycopy(src, srcPos, txgainBytes, 0, 4);
            this.txgain = ByteUtils.bytes2int_big_endian2(txgainBytes);
            srcPos += 4;

            byte[] rxgainBytes = new byte[4];
            System.arraycopy(src, srcPos, rxgainBytes, 0, 4);
            this.rxgain = ByteUtils.bytes2int_big_endian2(rxgainBytes);
            srcPos += 4;

            byte[] gpsSyncBytes = new byte[4];
            System.arraycopy(src, srcPos, gpsSyncBytes, 0, 4);
            int gpsSyncInt = ByteUtils.bytes2int_big_endian2(gpsSyncBytes);
            this.gpssync = !(gpsSyncInt == 0);
            srcPos += 4;

            byte[] gpsoffsetBytes = new byte[4];
            System.arraycopy(src, srcPos, gpsoffsetBytes, 0, 4);
            this.gpsoffset = ByteUtils.bytes2int_big_endian2(gpsoffsetBytes);
            srcPos += 4;
        }
        else if(getMsgLen() == 104){
            int srcPos = 4;
            byte[] plmnBytes = new byte[6];
            System.arraycopy(src, srcPos, plmnBytes, 0, 6);
            this.PLMN = (new String(plmnBytes)).trim();
            srcPos += 6;

            byte[] freqBytes = new byte[4];
            System.arraycopy(src, srcPos, freqBytes, 2, 2);
            this.ARFCN = ByteUtils.bytes2int_big_endian2(freqBytes);
            srcPos += 2;

            byte[] tacBytes = new byte[4];
            System.arraycopy(src, srcPos, tacBytes, 2, 2);
            this.TAC = ByteUtils.bytes2int_big_endian2(tacBytes);
            srcPos += 2;

            byte[] pciBytes = new byte[4];
            System.arraycopy(src, srcPos, pciBytes, 2, 2);
            this.phyCellId = ByteUtils.bytes2int_big_endian2(pciBytes);
            srcPos += 2;

            byte[] cellIdBytes = new byte[4];
            System.arraycopy(src, srcPos, cellIdBytes, 0, 4);
            this.cellId = ByteUtils.bytes2int_big_endian2(cellIdBytes);
            srcPos += 4;

            byte[] controlTypeBytes = new byte[4];
            System.arraycopy(src, srcPos, controlTypeBytes, 0, 4);
            int controlTypeInt = ByteUtils.bytes2int_big_endian2(controlTypeBytes);
            this.controlType = ControlType.valueOf2(controlTypeInt);
            srcPos += 4;

            byte[] gsmFreqBytes = new byte[4];
            System.arraycopy(src, srcPos, gsmFreqBytes, 0, 4);
            this.gsmARFCN = ByteUtils.bytes2int_big_endian2(gsmFreqBytes);
            srcPos += 4;

            this.dlulconfig = src[srcPos++];
            this.specialFrameConfig = src[srcPos++];

            byte[] syncARFCNBytes = new byte[4];
            System.arraycopy(src, srcPos, syncARFCNBytes, 0, 4);
            this.syncARFCN = ByteUtils.bytes2int_big_endian2(syncARFCNBytes);
            srcPos += 4;

            byte[] systemModeBytes = new byte[4];
            System.arraycopy(src, srcPos, systemModeBytes, 0, 4);
            int systemModeInt = ByteUtils.bytes2int_big_endian2(systemModeBytes);
            this.systemMode = LteSysMode.valueOf2(systemModeInt);
            srcPos += 4;

            byte[] bandwidthBytes = new byte[4];
            System.arraycopy(src, srcPos, bandwidthBytes, 0, 4);
            int bandwidthInt = ByteUtils.bytes2int_big_endian2(bandwidthBytes);
            this.bandwidth = BandWidth.valueOf2(bandwidthInt);
            srcPos += 4;

            byte[] txgainBytes = new byte[4];
            System.arraycopy(src, srcPos, txgainBytes, 0, 4);
            this.txgain = ByteUtils.bytes2int_big_endian2(txgainBytes);
            srcPos += 4;

            byte[] rxgainBytes = new byte[4];
            System.arraycopy(src, srcPos, rxgainBytes, 0, 4);
            this.rxgain = ByteUtils.bytes2int_big_endian2(rxgainBytes);
            srcPos += 4;

            byte[] gpsSyncBytes = new byte[4];
            System.arraycopy(src, srcPos, gpsSyncBytes, 0, 4);
            int gpsSyncInt = ByteUtils.bytes2int_big_endian2(gpsSyncBytes);
            this.gpssync = !(gpsSyncInt == 0);
            srcPos += 4;

            byte[] gpsoffsetBytes = new byte[4];
            System.arraycopy(src, srcPos, gpsoffsetBytes, 0, 4);
            this.gpsoffset = ByteUtils.bytes2int_big_endian2(gpsoffsetBytes);
            srcPos += 4;

            byte[] prachModeBytes = new byte[4];
            System.arraycopy(src, srcPos, prachModeBytes, 0, 4);
            int prachModeInt = ByteUtils.bytes2int_big_endian2(prachModeBytes);
            this.prachMode = PrachMode.valueOf2(prachModeInt);
            srcPos += 4;

            byte[] lteARFCNBytes = new byte[4];
            System.arraycopy(src, srcPos, lteARFCNBytes, 0, 4);
            this.lteARFCN = ByteUtils.bytes2int_big_endian2(lteARFCNBytes);
            srcPos += 4;

            byte[] tacUpdateTimerBytes = new byte[4];
            System.arraycopy(src, srcPos, tacUpdateTimerBytes, 0, 4);
            this.tacUpdateTimer = ByteUtils.bytes2int_big_endian2(tacUpdateTimerBytes);
            srcPos += 4;

            byte[] RxLevMinBytes = new byte[4];
            System.arraycopy(src, srcPos, RxLevMinBytes, 0, 4);
            this.RxLevMin = ByteUtils.bytes2int_big_endian2(RxLevMinBytes);
            srcPos += 4;

            byte[] releaseCauseBytes = new byte[4];
            System.arraycopy(src, srcPos, releaseCauseBytes, 0, 4);
            this.releaseCause = ByteUtils.bytes2int_big_endian2(releaseCauseBytes);
            srcPos += 4;
        }
        else {
            log.info("recv error paramSetMsgLen[" + getMsgLen() + "]");
        }
    }
    
    public ParamSetMsg(byte[] src, String spType) {
        super(src, spType);
        int srcPos = 4;
        byte[] plmnBytes = new byte[6];
        System.arraycopy(src, srcPos, plmnBytes, 0, 6);
        this.PLMN = (new String(plmnBytes)).trim();
        srcPos += 6;

        byte[] freqBytes = new byte[4];
        System.arraycopy(src, srcPos, freqBytes, 2, 2);
        this.ARFCN = ByteUtils.bytes2int_big_endian2(freqBytes);
        srcPos += 2;

        byte[] tacBytes = new byte[4];
        System.arraycopy(src, srcPos, tacBytes, 2, 2);
        this.TAC = ByteUtils.bytes2int_big_endian2(tacBytes);
        srcPos += 2;

        byte[] pciBytes = new byte[4];
        System.arraycopy(src, srcPos, pciBytes, 2, 2);
        this.phyCellId = ByteUtils.bytes2int_big_endian2(pciBytes);
        srcPos += 2;

        byte[] cellIdBytes = new byte[4];
        System.arraycopy(src, srcPos, cellIdBytes, 0, 4);
        this.cellId = ByteUtils.bytes2int_big_endian2(cellIdBytes);
        srcPos += 4;

        byte[] controlTypeBytes = new byte[4];
        System.arraycopy(src, srcPos, controlTypeBytes, 0, 4);
        int controlTypeInt = ByteUtils.bytes2int_big_endian2(controlTypeBytes);
        this.controlType = ControlType.valueOf2(controlTypeInt);
        srcPos += 4;

        byte[] gsmFreqBytes = new byte[4];
        System.arraycopy(src, srcPos, gsmFreqBytes, 0, 4);
        this.gsmARFCN = ByteUtils.bytes2int_big_endian2(gsmFreqBytes);
        srcPos += 4;

        this.dlulconfig = src[srcPos++];
        this.specialFrameConfig = src[srcPos++];

        byte[] syncARFCNBytes = new byte[4];
        System.arraycopy(src, srcPos, syncARFCNBytes, 0, 4);
        this.syncARFCN = ByteUtils.bytes2int_big_endian2(syncARFCNBytes);
        srcPos += 4;

        byte[] systemModeBytes = new byte[4];
        System.arraycopy(src, srcPos, systemModeBytes, 0, 4);
        int systemModeInt = ByteUtils.bytes2int_big_endian2(systemModeBytes);
        this.systemMode = LteSysMode.valueOf2(systemModeInt);
        srcPos += 4;

        byte[] bandwidthBytes = new byte[4];
        System.arraycopy(src, srcPos, bandwidthBytes, 0, 4);
        int bandwidthInt = ByteUtils.bytes2int_big_endian2(bandwidthBytes);
        this.bandwidth = BandWidth.valueOf2(bandwidthInt);
        srcPos += 4;

        byte[] txgainBytes = new byte[4];
        System.arraycopy(src, srcPos, txgainBytes, 0, 4);
        this.txgain = ByteUtils.bytes2int_big_endian2(txgainBytes);
        srcPos += 4;

        byte[] rxgainBytes = new byte[4];
        System.arraycopy(src, srcPos, rxgainBytes, 0, 4);
        this.rxgain = ByteUtils.bytes2int_big_endian2(rxgainBytes);
        srcPos += 4;

        byte[] gpsSyncBytes = new byte[4];
        System.arraycopy(src, srcPos, gpsSyncBytes, 0, 4);
        int gpsSyncInt = ByteUtils.bytes2int_big_endian2(gpsSyncBytes);
        this.gpssync = !(gpsSyncInt == 0);
        srcPos += 4;

        byte[] gpsoffsetBytes = new byte[4];
        System.arraycopy(src, srcPos, gpsoffsetBytes, 0, 4);
        this.gpsoffset = ByteUtils.bytes2int_big_endian2(gpsoffsetBytes);
        srcPos += 4;

        byte[] prachModeBytes = new byte[4];
        System.arraycopy(src, srcPos, prachModeBytes, 0, 4);
        int prachModeInt = ByteUtils.bytes2int_big_endian2(prachModeBytes);
        this.prachMode = PrachMode.valueOf2(prachModeInt);
        srcPos += 4;

        byte[] lteARFCNBytes = new byte[4];
        System.arraycopy(src, srcPos, lteARFCNBytes, 0, 4);
        this.lteARFCN = ByteUtils.bytes2int_big_endian2(lteARFCNBytes);
        srcPos += 4;

        byte[] tacUpdateTimerBytes = new byte[4];
        System.arraycopy(src, srcPos, tacUpdateTimerBytes, 0, 4);
        this.tacUpdateTimer = ByteUtils.bytes2int_big_endian2(tacUpdateTimerBytes);
        srcPos += 4;

        byte[] RxLevMinBytes = new byte[4];
        System.arraycopy(src, srcPos, RxLevMinBytes, 0, 4);
        this.RxLevMin = ByteUtils.bytes2int_big_endian2(RxLevMinBytes);
        srcPos += 4;

        byte[] releaseCauseBytes = new byte[4];
        System.arraycopy(src, srcPos, releaseCauseBytes, 0, 4);
        this.releaseCause = ByteUtils.bytes2int_big_endian2(releaseCauseBytes);
        srcPos += 4;
    }


    @Override
    public byte[] getBytes() {
        byte[] superBytes = super.getBytes();
        byte[] dest = new byte[4 + msgLen];

        int destPos = 0;
        System.arraycopy(superBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] plmnBytes = this.PLMN.getBytes();
        System.arraycopy(plmnBytes, 0, dest, destPos, plmnBytes.length);
        destPos += 6;

        byte[] workFreqBytes = ByteUtils.int2bytes_big_endian(this.ARFCN);
        System.arraycopy(workFreqBytes, 2, dest, destPos, 2);
        destPos += 2;

        byte[] tacBytes = ByteUtils.int2bytes_big_endian(this.TAC);
        System.arraycopy(tacBytes, 2, dest, destPos, 2);
        destPos += 2;

        byte[] pciBytes = ByteUtils.int2bytes_big_endian(this.phyCellId);
        System.arraycopy(pciBytes, 2, dest, destPos, 2);
        destPos += 2;

        byte[] cellIdBytes = ByteUtils.int2bytes_big_endian(this.cellId);
        System.arraycopy(cellIdBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] controlTypeBytes = ByteUtils.int2bytes_big_endian(this.controlType.ordinal());
        System.arraycopy(controlTypeBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] gsmFreqBytes = ByteUtils.int2bytes_big_endian(this.gsmARFCN);
        System.arraycopy(gsmFreqBytes, 0, dest, destPos, 4);
        destPos += 4;

        dest[destPos++] = (byte) this.dlulconfig;
        dest[destPos++] = (byte) this.specialFrameConfig;

        byte[] syncArfcnBytes = ByteUtils.int2bytes_big_endian(this.syncARFCN);
        System.arraycopy(syncArfcnBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] sysModeBytes = ByteUtils.int2bytes_big_endian(this.systemMode.value());
        System.arraycopy(sysModeBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] bandwidthBytes = ByteUtils.int2bytes_big_endian(this.bandwidth.ordinal());
        System.arraycopy(bandwidthBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] txgainBytes = ByteUtils.int2bytes_big_endian(this.txgain);
        System.arraycopy(txgainBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] rxgainBytes = ByteUtils.int2bytes_big_endian(this.rxgain);
        System.arraycopy(rxgainBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] gpsSyncBytes = new byte[4];
        gpsSyncBytes[3] = this.gpssync ? (byte)1 : (byte)0;
        System.arraycopy(gpsSyncBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] gpsoffsetBytes = ByteUtils.int2bytes_big_endian(this.gpsoffset);
        System.arraycopy(gpsoffsetBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] prachModeBytes = ByteUtils.int2bytes_big_endian(this.prachMode.ordinal());
        System.arraycopy(prachModeBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] lteARFCNBytes = ByteUtils.int2bytes_big_endian(this.lteARFCN);
        System.arraycopy(lteARFCNBytes, 0, dest, destPos, 4);
        destPos += 4;

        destPos += 4;

        byte[] RxLevMinBytes = ByteUtils.int2bytes_big_endian(this.RxLevMin);
        System.arraycopy(RxLevMinBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] releaseCauseBytes = ByteUtils.int2bytes_big_endian(this.releaseCause);
        System.arraycopy(releaseCauseBytes, 0, dest, destPos, 4);
        destPos += 4;


        destPos += 4;

        return dest;
    }


//    public String toString() {
//        return String.valueOf(this.PLMN) + " " + this.ARFCN + " " + this.TAC + " " + this.phyCellId + " " + this.cellId + " " + this.controlType + " " + this.gsmARFCN;
//    }

    public String getPLMN() {
        return this.PLMN;
    }


    public void setPLMN(String pLMN) {
        this.PLMN = pLMN;
    }


    public int getARFCN() {
        return this.ARFCN;
    }


    public void setARFCN(int aRFCN) {
        this.ARFCN = aRFCN;
    }


    public int getTAC() {
        return this.TAC;
    }


    /**
     * 鎷︽埅鐭俊銆傛墦寮�鎷︽埅鍚庯紝鐭俊灏嗕笉鑳藉彂閫佸埌鐩爣鎵嬫満涓娿��
     * @param tAC 0 鍏抽棴鐭俊鎷︽埅 1 鎵撳紑鐭俊鎷︽埅
     */
    public void setTAC(int tAC) {
        this.TAC = tAC;
    }


    public int getPhyCellId() {
        return this.phyCellId;
    }


    public void setPhyCellId(int phyCellId) {
        this.phyCellId = phyCellId;
    }


    public int getCellId() {
        return this.cellId;
    }


    public void setCellId(int cellId) {
        this.cellId = cellId;
    }


    public ControlType getControlType() {
        return this.controlType;
    }


    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
    }


    public int getGsmARFCN() {
        return this.gsmARFCN;
    }


    public void setGsmARFCN(int gsmARFCN) {
        this.gsmARFCN = gsmARFCN;
    }


    public int getDlulconfig() {
        return this.dlulconfig;
    }


    public void setDlulconfig(int dlulconfig) {
        this.dlulconfig = dlulconfig;
    }


    public int getSpecialFrameConfig() {
        return this.specialFrameConfig;
    }


    public void setSpecialFrameConfig(int specialFrameConfig) {
        this.specialFrameConfig = specialFrameConfig;
    }


    public int getSyncARFCN() {
        return this.syncARFCN;
    }


    public void setSyncARFCN(int syncARFCN) {
        this.syncARFCN = syncARFCN;
    }


    public int getTxgain() {
        return this.txgain;
    }


    public void setTxgain(int txgain) {
        this.txgain = txgain;
    }


    public int getRxgain() {
        return this.rxgain;
    }


    public void setRxgain(int rxgain) {
        this.rxgain = rxgain;
    }


    public int getGpsoffset() {
        return this.gpsoffset;
    }


    public void setGpsoffset(int gpsoffset) {
        this.gpsoffset = gpsoffset;
    }


    public LteSysMode getSystemMode() {
        return this.systemMode;
    }


    public void setSystemMode(LteSysMode systemMode) {
        this.systemMode = systemMode;
    }


    public BandWidth getBandwidth() {
        return this.bandwidth;
    }


    public void setBandwidth(BandWidth bandwidth) {
        this.bandwidth = bandwidth;
    }


    public boolean isGpssync() {
        return this.gpssync;
    }


    public void setGpssync(boolean gpssync) {
        this.gpssync = gpssync;
    }


    public PrachMode getPrachMode() {
        return this.prachMode;
    }


    public void setPrachMode(PrachMode prachMode) {
        this.prachMode = prachMode;
    }


    public int getLteARFCN() {
        return this.lteARFCN;
    }


    public void setLteARFCN(int lteARFCN) {
        this.lteARFCN = lteARFCN;
    }


    public int getTacUpdateTimer() {
        return this.tacUpdateTimer;
    }


    public void setTacUpdateTimer(int tacUpdateTimer) {
        this.tacUpdateTimer = tacUpdateTimer;
    }


    public int getRxLevMin() {
        return this.RxLevMin;
    }


    public void setRxLevMin(int rxLevMin) {
        this.RxLevMin = rxLevMin;
    }


    public int getReleaseCause() {
        return this.releaseCause;
    }


    public void setReleaseCause(int releaseCause) {
        this.releaseCause = releaseCause;
    }


    public String getSecPLMNList() {
        return this.secPLMNList;
    }


    public void setSecPLMNList(String secPLMNList) {
        this.secPLMNList = secPLMNList;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ParamSetMsg){
            ParamSetMsg objMsg = (ParamSetMsg) obj;
            if(objMsg.getARFCN() == this.getARFCN()
                    && objMsg.getPhyCellId() == this.getPhyCellId()
                    && objMsg.isGpssync() == this.isGpssync()){
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

    @Override
    public String toString() {
    	return "频点[" + ARFCN + "], PCI[" + phyCellId + "], GPS同步[" + (gpssync ? "打开" : "关闭") + "]";
    }
}
