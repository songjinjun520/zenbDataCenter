package com.zrt.zenb.controller.vo.jsonMsg;

import com.zrt.zenb.controller.vo.enums.MsgType;
import com.zrt.zenb.controller.vo.jsonMsg.BaseMsg;
import lombok.Data;

/**
 * @author intent
 */
@Data
public class ImsiCqi5Rpt extends BaseMsg {
    String imsi;

    public ImsiCqi5Rpt(){
        msgType = MsgType.IPCELL_IMIS_TEID_REPORT;
    }

}
