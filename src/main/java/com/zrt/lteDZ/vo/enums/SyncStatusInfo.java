package com.zrt.lteDZ.vo.enums;


public enum SyncStatusInfo {
    INIT(1),
    INIT_SYNC_START(2),
    INIT_SYNC_END(3),
    TRACK_SYNC_START(4),
    TRACK_SYNC_END(5),
    SYNC_LOST(6);

    private final int value;


    public int value() {
        return this.value;
    }


    SyncStatusInfo() {
        this.value = ordinal();
    }


    SyncStatusInfo(int value) {
        this.value = value;
    }

    public static SyncStatusInfo valueOf2(int value) {
        for (int i = 0; i < SyncStatusInfo.values().length; i ++) {
            SyncStatusInfo temp = SyncStatusInfo.values()[i];
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

    public String toString() {
    	if (this == INIT) {
            return "初始状态";
        }
        if (this == INIT_SYNC_START) {
            return "初始同步开始";
        }
        if (this == INIT_SYNC_END) {
            return "初始同步完成";
        }
        if (this == TRACK_SYNC_START) {
            return "跟踪同步开始";
        }
        if (this == TRACK_SYNC_END) {
            return "跟踪同步完成";
        }
        if (this == SYNC_LOST) {
            return "失去同步";
        }
        return "RESERVED";
    }
}
