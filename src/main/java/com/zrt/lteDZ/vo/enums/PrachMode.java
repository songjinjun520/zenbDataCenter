package com.zrt.lteDZ.vo.enums;


public enum PrachMode {
    PRACHMODE_0,


    PRACHMODE_1,


    PRACHMODE_HIGH_SPEED;

    public static PrachMode valueOf2(int value) {
        for (int i = 0; i < PrachMode.values().length; i ++) {
            PrachMode temp = PrachMode.values()[i];
            if (temp.ordinal() == value) {
                return temp;
            }
        }

        return null;
    }
}
