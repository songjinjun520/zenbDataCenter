package com.zrt.zenb.controller.vo.jsonMsg;

import com.zrt.zenb.controller.vo.enums.MsgType;
import lombok.Data;

/**
 * 与后台服务间心跳
 * @author intent
 */
@Data
public class HeartBeat extends BaseMsg {
    int lteCount = 0;

    public HeartBeat(){
        msgType = MsgType.HEARTBEAT;
    }
}
