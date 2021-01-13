package com.zrt.zenb.controller.vo.jsonMsg;

import com.zrt.zenb.controller.vo.enums.MsgType;
import com.zrt.zenb.controller.vo.jsonMsg.BaseMsg;
import lombok.Data;

/**
 * @author intent
 */
@Data
public class MsisdnRpt extends BaseMsg {
    String imsi;
    String imei;
    String msisdn;
    String mobileInfo;

    public MsisdnRpt(){
        msgType = MsgType.MSISDN_RPT;
    }

}
