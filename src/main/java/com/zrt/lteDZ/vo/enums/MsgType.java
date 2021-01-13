package com.zrt.lteDZ.vo.enums;


/**
 *
 */
public enum MsgType {
    IPCELL_HEARTBEAT(0xC0),


//    IPCELL_TAC_UPDATE_REQ('?'),
//    IPCELL_TAC_UPDATE_ACK('?'),


    IPCELL_RF_CONTROL_REQ(0xC3),
    IPCELL_RF_CONTROL_ACK(0xC4),


    IPCELL_STATUS_REPORT(0xC5),


    IPCELL_RF_STATUS_GET_REQ(0xC6),
    IPCELL_RF_STATUS_GET_ACK(0xC7),


//    IPCELL_RADIOPARA_SET_REQ('?'),
//    IPCELL_RADIOPARA_SET_ACK('?'),
//
//
//    IPCELL_RADIOPARA_GET_REQ('?'),
//    IPCELL_RADIOPARA_GET_ACK('?'),
//
//
//    IPCELL_UPGRADE_REQ('?'),
//    IPCELL_UPGRADE_ACK('?'),


    IPCELL_SET_REQ(0xD0),
    IPCELL_SET_ACK(0xD1),


    IPCELL_GET_REQ(0xD2),
    IPCELL_GET_ACK(0xD3),


    IPCELL_REBOOT(0xD4),


    IPCELL_IMSI_REPORT(0xD5),


    IPCELL_NMM_FREQ_SET_REQ(0xD6),
    IPCELL_NMM_FREQ_SET_ACK(0xD7),


    IPCELL_NMM_FREQ_GET_REQ(0xD8),
    IPCELL_NMM_FREQ_GET_ACK(0xD9),


    IPCELL_NMM_SYNC_GET_REQ(0xDA),
    IPCELL_NMM_SYNC_GET_ACK(0xDB),


    IPCELL_NMM_CELL_INFO_GET_REQ(0xDC),
    IPCELL_NMM_CELL_INFO_GET_ACK(0xDD),


    IPCELL_IMSI_SET_REQ(0xE0),
    IPCELL_IMSI_SET_RESP(0xE1),


    IPCELL_IMSI_DEL_REQ(0xE2),
    IPCELL_IMSI_DEL_RESP(0xE3),


    IPCELL_IMSI_INQ_REQ(0xE4),
    IPCELL_IMSI_INQ_RESP(0xE5),


//    IPCELL_IMSI_UE_RSRP_REPORT('?'),
//
//
//    IPCELL_PA_CONTROL_REQ('?'),
//    IPCELL_PA_CONTROL_RESP('锟斤拷'),
//
//
//    IPCELL_PA_SET_REQ('锟斤拷'),
//    IPCELL_PA_SET_RESP('锟斤拷'),
//
//
//    IPCELL_PA_LNAINFO_INQ_REQ('?'),
//    IPCELL_PA_LNAINFO_INQ_RESP('锟斤拷'),
//
//
//    IPCELL_ALL_HOLDUE_REQ('?'),
//    IPCELL_ALL_HOLDUE_ACK('?'),


    IPCELL_TRAFFIC_CTRL_SET_REQ(0xB0),
    IPCELL_TRAFFIC_CTRL_SET_ACK(0xB1),


    IPCELL_TRAFFIC_CTRL_GET_REQ(0xB2),
    IPCELL_TRAFFIC_CTRL_GET_ACK(0xB3),


    IPCELL_TRAFFIC_REPORT(0xB4),


    IPCELL_IMIS_TEID_REPORT(0xB5),


    IPCELL_TRAFFIC_MONITOR_MODE_SET_REQ(0xB6),
    IPCELL_TRAFFIC_MONITOR_MODE_SET_ACK(0xB7),


    IPCELL_TRAFFIC_MONITOR_MODE_GET_REQ(0xB8),
    IPCELL_TRAFFIC_MONITOR_MODE_GET_ACK(0xB9),


    IPCELL_RELEASE_ALL_UE_REQ(0xBA),
    IPCELL_RELEASE_ALL_UE_ACK(0xBB),


    IPCELL_NAS_SMS(0xBE),
    /**
     * 鍛婅
     */
    IPCELL_ALARM_NOTIFY(0xBF),

    /**
     * NAS鐭俊鎷︽埅璁剧疆
     */
    IPCELL_NAS_SMS_HACK_SET_REQ(0xA1),
    /**
     * NAS鐭俊鎷︽埅璁剧疆搴旂瓟
     */
    IPCELL_NAS_SMS_HACK_SET_ACk(0xA2),
    /**
     * NAS鐭俊鎷︽埅鏌ヨ
     */
    IPCELL_NAS_SMS_HACK_GET_REQ(0xA3),
    /**
     * NAS鐭俊鎷︽埅鏌ヨ搴旂瓟
     */
    IPCELL_NAS_SMS_HACK_GET_ACK(0xA4),

    /**
     * IPSEC瀵嗛挜鏌ヨ
     */
    IPCELL_IPSEC_KEY_GET_REQ(0xA5),
    IPCELL_IPSEC_KEY_GET_ACK(0xA6),
    
    SOCKET_DISLINKED,
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
