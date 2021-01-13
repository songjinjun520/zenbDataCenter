package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;
import com.zrt.lteDZ.vo.enums.CellStatusInfo;
import com.zrt.lteDZ.vo.enums.SyncStatusInfo;


public class SyncStatusRptMsg extends BaseMsg {
    private int syncARFCN;
    private int syncPCI;
    private int syncStatus;
    private int rrcConnectionReqTotalNum;
    private int rrcConnectionSetupCompTimeoutNum;
    private int IdentityRespTimeoutNum;
    private int cellStatus;
    private int temperatureStatus;
    private int Reserved;
    private String equipmentid;
    private String softwareversion;
    private String time;
    private String latitude_deg;
    private String latitude_min;
    private String north_south;
    private String longitude_deg;
    private String longitude_min;
    private String east_west;
    private int system_mode;
    private int run_time;

    public SyncStatusRptMsg(String spType) {
        super(spType);
    }


    public SyncStatusRptMsg(byte[] src, String spType) {
        super(src, spType);
        int srcPos = 4;

        byte[] syncARFCNBytes = new byte[4];
        System.arraycopy(src, srcPos, syncARFCNBytes, 2, 2);
        this.syncARFCN = ByteUtils.bytes2int_big_endian2(syncARFCNBytes);
        srcPos += 2;
        byte[] syncPCIBytes = new byte[4];
        System.arraycopy(src, srcPos, syncPCIBytes, 2, 2);
        this.syncPCI = ByteUtils.bytes2int_big_endian2(syncPCIBytes);
        srcPos += 2;
        byte[] syncStatusBytes = new byte[4];
        System.arraycopy(src, srcPos, syncStatusBytes, 2, 2);
        this.syncStatus = ByteUtils.bytes2int_big_endian2(syncStatusBytes);
        srcPos += 2;
        byte[] rrcConnectionReqTotalNumBytes = new byte[4];
        System.arraycopy(src, srcPos, rrcConnectionReqTotalNumBytes, 0, 4);
        this.rrcConnectionReqTotalNum = ByteUtils.bytes2int_big_endian2(rrcConnectionReqTotalNumBytes);
        srcPos += 4;
        byte[] rrcConnectionSetupCompTimeoutNumBytes = new byte[4];
        System.arraycopy(src, srcPos, rrcConnectionSetupCompTimeoutNumBytes, 0, 4);
        this.rrcConnectionSetupCompTimeoutNum = ByteUtils.bytes2int_big_endian2(rrcConnectionSetupCompTimeoutNumBytes);
        srcPos += 4;
        byte[] IdentityRespTimeoutNumBytes = new byte[4];
        System.arraycopy(src, srcPos, IdentityRespTimeoutNumBytes, 0, 4);
        this.IdentityRespTimeoutNum = ByteUtils.bytes2int_big_endian2(IdentityRespTimeoutNumBytes);
        srcPos += 4;
        byte[] cellStatusBytes = new byte[4];
        System.arraycopy(src, srcPos, cellStatusBytes, 0, 4);
        this.cellStatus = ByteUtils.bytes2int_big_endian2(cellStatusBytes);
        srcPos += 4;
        byte[] temperatureStatusBytes = new byte[4];
        System.arraycopy(src, srcPos, temperatureStatusBytes, 0, 4);
        this.temperatureStatus = ByteUtils.bytes2int_big_endian2(temperatureStatusBytes);
        srcPos += 4;
        byte[] ReservedBytes = new byte[4];
        System.arraycopy(src, srcPos, ReservedBytes, 0, 4);
        this.Reserved = ByteUtils.bytes2int_big_endian2(ReservedBytes);
        srcPos += 4;
        byte[] equipmentidBytes = new byte[16];
        System.arraycopy(src, srcPos, equipmentidBytes, 0, 16);
        this.equipmentid = (new String(equipmentidBytes)).trim();
        srcPos += 16;
        byte[] softwareversionBytes = new byte[30];
        System.arraycopy(src, srcPos, softwareversionBytes, 0, 30);
        this.softwareversion = (new String(softwareversionBytes)).trim();
        srcPos += 30;
        byte[] timeBytes = new byte[20];
        System.arraycopy(src, srcPos, timeBytes, 0, 20);
        this.time = (new String(timeBytes)).trim();
        srcPos += 20;
        byte[] latitude_degBytes = new byte[6];
        System.arraycopy(src, srcPos, latitude_degBytes, 0, 6);
        this.latitude_deg = (new String(latitude_degBytes)).trim();
        srcPos += 6;
        byte[] latitude_minBytes = new byte[10];
        System.arraycopy(src, srcPos, latitude_minBytes, 0, 10);
        this.latitude_min = (new String(latitude_minBytes)).trim();
        srcPos += 10;
        this.north_south = new String(new byte[]{src[srcPos++]});
        byte[] longitude_degBytes = new byte[6];
        System.arraycopy(src, srcPos, longitude_degBytes, 0, 6);
        this.longitude_deg = (new String(longitude_degBytes)).trim();
        srcPos += 6;
        byte[] longitude_minBytes = new byte[10];
        System.arraycopy(src, srcPos, longitude_minBytes, 0, 10);
        this.longitude_min = (new String(longitude_minBytes)).trim();
        srcPos += 10;
        this.east_west = new String(new byte[]{src[srcPos++]});
        byte[] system_modeBytes = new byte[4];
        System.arraycopy(src, srcPos, system_modeBytes, 0, 4);
        this.system_mode = ByteUtils.bytes2int_big_endian2(system_modeBytes);
        srcPos += 4;
    }


    public int getSyncARFCN() {
        return this.syncARFCN;
    }


    public void setSyncARFCN(int syncARFCN) {
        this.syncARFCN = syncARFCN;
    }


    public int getSyncPCI() {
        return this.syncPCI;
    }


    public void setSyncPCI(int syncPCI) {
        this.syncPCI = syncPCI;
    }


    public int getSyncStatus() {
        return this.syncStatus;
    }


    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }


    public int getRrcConnectionReqTotalNum() {
        return this.rrcConnectionReqTotalNum;
    }


    public void setRrcConnectionReqTotalNum(int rrcConnectionReqTotalNum) {
        this.rrcConnectionReqTotalNum = rrcConnectionReqTotalNum;
    }


    public int getRrcConnectionSetupCompTimeoutNum() {
        return this.rrcConnectionSetupCompTimeoutNum;
    }


    public void setRrcConnectionSetupCompTimeoutNum(int rrcConnectionSetupCompTimeoutNum) {
        this.rrcConnectionSetupCompTimeoutNum = rrcConnectionSetupCompTimeoutNum;
    }


    public int getIdentityRespTimeoutNum() {
        return this.IdentityRespTimeoutNum;
    }


    public void setIdentityRespTimeoutNum(int identityRespTimeoutNum) {
        this.IdentityRespTimeoutNum = identityRespTimeoutNum;
    }


    public int getCellStatus() {
        return this.cellStatus;
    }


    public void setCellStatus(int cellStatus) {
        this.cellStatus = cellStatus;
    }


    public int getTemperatureStatus() {
        return this.temperatureStatus;
    }


    public void setTemperatureStatus(int temperatureStatus) {
        this.temperatureStatus = temperatureStatus;
    }


    public int getReserved() {
        return this.Reserved;
    }


    public void setReserved(int reserved) {
        this.Reserved = reserved;
    }


    public String getEquipmentid() {
        return this.equipmentid;
    }


    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }


    public String getSoftwareversion() {
        return this.softwareversion;
    }


    public void setSoftwareversion(String softwareversion) {
        this.softwareversion = softwareversion;
    }


    public String getTime() {
        return this.time;
    }


    public void setTime(String time) {
        this.time = time;
    }


    public String getLatitude_deg() {
        return this.latitude_deg;
    }


    public void setLatitude_deg(String latitude_deg) {
        this.latitude_deg = latitude_deg;
    }


    public String getLatitude_min() {
        return this.latitude_min;
    }


    public void setLatitude_min(String latitude_min) {
        this.latitude_min = latitude_min;
    }


    public String getNorth_south() {
        return this.north_south;
    }


    public void setNorth_south(String north_south) {
        this.north_south = north_south;
    }


    public String getLongitude_deg() {
        return this.longitude_deg;
    }


    public void setLongitude_deg(String longitude_deg) {
        this.longitude_deg = longitude_deg;
    }


    public String getLongitude_min() {
        return this.longitude_min;
    }


    public void setLongitude_min(String longitude_min) {
        this.longitude_min = longitude_min;
    }


    public String getEast_west() {
        return this.east_west;
    }


    public void setEast_west(String east_west) {
        this.east_west = east_west;
    }


    public int getSystem_mode() {
        return this.system_mode;
    }


    public void setSystem_mode(int system_mode) {
        this.system_mode = system_mode;
    }


    public int getRun_time() {
        return this.run_time;
    }


    public void setRun_time(int run_time) {
        this.run_time = run_time;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SyncStatusRptMsg){
            SyncStatusRptMsg syncStatusRptMsg = (SyncStatusRptMsg) obj;
            if(syncStatusRptMsg.getCellStatus() == this.getCellStatus()
                    && syncStatusRptMsg.getSyncStatus() == this.getSyncStatus()){

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
        SyncStatusInfo syncStatusInfo = SyncStatusInfo.valueOf2(this.getSyncStatus());
        CellStatusInfo cellStatusInfo = CellStatusInfo.valueOf2(this.getCellStatus());

        return syncStatusInfo.toString() + "  " + cellStatusInfo.toString();
    }
}
