package com.zrt.lteDZ.vo.enums;


public enum LteSysMode {
    SCAN_FREQ(1),


    COMMON_MODE(2),


    AUTO_MODE(3),


    LOCATION_MODE(4);

    private final int value;


    public int value() {
        return this.value;
    }


    LteSysMode() {
        this.value = ordinal();
    }


    LteSysMode(int value) {
        this.value = value;
    }

    public static LteSysMode valueOf2(int value) {
        for (int i = 0; i < LteSysMode.values().length; i ++) {
            LteSysMode temp = LteSysMode.values()[i];
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }
}
