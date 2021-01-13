package com.zrt.zenb.controller.vo.jsonMsg;

import com.zrt.zenb.controller.vo.jsonMsg.BaseMsg;
import lombok.Data;

/**
 * @author intent
 */
@Data
public class DevStatus extends BaseMsg {
    String syncStatus;
    String rfStatus;
}
