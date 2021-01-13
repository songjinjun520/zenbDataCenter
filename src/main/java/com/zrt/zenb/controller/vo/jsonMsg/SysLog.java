package com.zrt.zenb.controller.vo.jsonMsg;

import com.zrt.zenb.controller.vo.enums.MsgType;
import com.zrt.zenb.controller.vo.jsonMsg.BaseMsg;
import lombok.Data;

/**
 * @author intent
 */
@Data
public class SysLog extends BaseMsg {
    String logStr;

    public SysLog(){
        msgType = MsgType.LOG_GET_ACK;
    }

}
