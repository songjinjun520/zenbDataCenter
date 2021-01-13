package com.zrt.lteDZ.vo;

public class AlarmNotify extends BaseMsg {

    byte alarmID;
    byte alarmClearFlag;
    String description;

    public AlarmNotify(byte[] src, String spType) {
        super(src, spType);

        int srcPos = 4;

        alarmID = src[srcPos ++];
        alarmClearFlag = src[srcPos ++];

        int msgLen = getMsgLen();
        int descLen = msgLen - 6;
        if(descLen > 0){
            byte[] descBytes = new byte[descLen];
            System.arraycopy(src, srcPos, descBytes, 0, descLen);
            description = new String(descBytes).trim();
        }
    }
    public AlarmNotify(byte[] src) {
        super(src);

        int srcPos = 4;

        alarmID = src[srcPos ++];
        alarmClearFlag = src[srcPos ++];

        int msgLen = getMsgLen();
        int descLen = msgLen - 6;
        if(descLen > 0){
            byte[] descBytes = new byte[descLen];
            System.arraycopy(src, srcPos, descBytes, 0, descLen);
            description = new String(descBytes).trim();
        }
    }

    public byte getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(byte alarmID) {
        this.alarmID = alarmID;
    }

    public byte getAlarmClearFlag() {
        return alarmClearFlag;
    }

    public void setAlarmClearFlag(byte alarmClearFlag) {
        this.alarmClearFlag = alarmClearFlag;
    }

    public String getAlarmIDDesc() {
        String alarmIdDesc = "";
        switch (alarmID){
            case 1:
                alarmIdDesc = "ALARM_AIR_SYNC_LOST";
                break;
            case 2:
                alarmIdDesc = "ALARM_RRC_CONN_SUCC_RATIO_LOW";
                break;
            case 3:
                alarmIdDesc = " ALARM_CPU_HIGH";
                break;
            case 4:
                alarmIdDesc = "ALARM_REBOOT";
                break;
            case 5:
                alarmIdDesc = "ALARM_BOARD_TEMPERATURE_HIGH";
                break;
            case 6:
                alarmIdDesc = "ALARM_GPS_FAILED_TO_LOCK";
                break;
            case 7:
                alarmIdDesc = "ALARM_CELL_SETUP_FAILED";
                break;
            case 8:
                alarmIdDesc = "ALARM_RRC_CONN_FAILED_Exc_15";
                break;
            case 10:
                alarmIdDesc = "SCTP链路故障";
                break;
            default:
                alarmIdDesc = "NO DESC";
                break;
        }
        if(null != description && description.trim().length() > 0){
            alarmIdDesc += "[" + description + "]";
        }

        return alarmIdDesc;
    }

    @Override
    public String toString() {
        return (alarmClearFlag == 0 ? "提交告警[" : "清除告警[") + getAlarmIDDesc() + "]";
    }
}
