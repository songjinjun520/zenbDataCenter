package com.zrt.zenb.controller.vo.jsonMsg;

import com.zrt.zenb.controller.vo.enums.MsgType;
import com.zrt.zenb.controller.vo.jsonMsg.BaseMsg;
import lombok.Data;

/**
 * @author intent
 */
@Data
public class ImsiRpt extends BaseMsg {
    String imsi;
    int rsrq;

    public ImsiRpt(){
        msgType = MsgType.IPCELL_IMSI_REPORT;
    }

}
