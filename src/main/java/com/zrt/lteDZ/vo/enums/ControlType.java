package com.zrt.lteDZ.vo.enums;


public enum ControlType {
    /**
     * 0 释放 进入黑名单
     */
    GATHER,
    _GSM,
    _LTE,
    /**
     * 3 保持UE 进入白名单
     */
    CONTROL;

    public static ControlType valueOf2(int value) {
        for (int i = 0; i < ControlType.values().length; i ++) {
            ControlType temp = ControlType.values()[i];
            if (temp.ordinal() == value) {
                return temp;
            }
        }

        return null;
    }

}
