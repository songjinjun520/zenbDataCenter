package com.zrt.zenb.controller.vo.enums;


/**
 * 与MQ间json消息类型定义
 */
public enum MsgType {
    /**
     * 参数配置
     */
    IPCELL_SET_REQ(0xD0),
    IPCELL_SET_ACK(0xD1),

    /**
     * 配置查询
     */
    IPCELL_GET_REQ(0xD2),
    IPCELL_GET_ACK(0xD3),

    /**
     * 设备重启
     */
    IPCELL_REBOOT(0xD4),

    IPCELL_IMSI_REPORT(0xD5),

    IPCELL_IMIS_TEID_REPORT(0xB5),

    STATUS_GET_REQ(0x11),
    STATUS_GET_ACK(0X12),

    LOG_GET_REQ(0X13),
    LOG_GET_ACK(0x14),
    MSISDN_RPT(0x15),
    HEARTBEAT(0x16),
    RESERVED;

    private final int value;


    public int value() {
        return this.value;
    }


    MsgType() {
        this.value = ordinal();
    }


    MsgType(int value) {
        this.value = value;
    }

    public static MsgType valueOf2(int value) {
        for (int i = 0; i < MsgType.values().length; i ++) {
            MsgType temp = MsgType.values()[i];
            if (temp.value == value) {
                return temp;
            }
        }

        return RESERVED;
    }
}
