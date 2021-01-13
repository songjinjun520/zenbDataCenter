package com.zrt.zenb.controller.vo.jsonMsg;

import com.zrt.zenb.controller.vo.jsonMsg.BaseMsg;
import lombok.Data;

/**
 * 设备参数配置
 * @author intent
 */
@Data
public class DevCfg extends BaseMsg {
    int arfcn;
    int pci;
    boolean gpsSync;
    int gpsOffset;

}
