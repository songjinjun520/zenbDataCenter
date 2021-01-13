package com.zrt.zenb.controller.vo.jsonMsg;

import com.zrt.zenb.controller.vo.enums.MsgType;
import lombok.Data;

/**
 * MQ与后台间JSON消息通用字段
 * @author intent
 */
@Data
public class BaseMsg {
    MsgType msgType;
    String devId;
    int modId;
    long msgTime;

    public BaseMsg(){
        msgTime = System.currentTimeMillis();
    }

}
