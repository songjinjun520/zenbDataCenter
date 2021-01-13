package com.zrt.lteDZ.vo.enums;

public enum BandWidth {
    _5M,
    _10M,
    _20M,
    _15M;

    public static BandWidth valueOf2(int value) {
        for (int i = 0; i < BandWidth.values().length; i ++) {
            BandWidth temp = BandWidth.values()[i];
            if (temp.ordinal() == value) {
                return temp;
            }
        }

        return null;
    }

}

