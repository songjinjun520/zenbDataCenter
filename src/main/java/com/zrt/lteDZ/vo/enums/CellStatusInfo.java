package com.zrt.lteDZ.vo.enums;


public enum CellStatusInfo {
    CELL_INSTR_ING,
    CELL_INSTR_SUCCESS,
    CELL_INSTR_FAILURE;

    public static CellStatusInfo valueOf2(int value) {
        for (int i = 0; i < CellStatusInfo.values().length; i ++ ) {
            CellStatusInfo temp = CellStatusInfo.values()[i];
            if (temp.ordinal() == value) {
                return temp;
            }
        }

        return null;
    }


    public String toString() {
    	if (this == CELL_INSTR_ING) {
            return "小区正在建立";
        }
        if (this == CELL_INSTR_SUCCESS) {
            return "小区建立成功";
        }
        if (this == CELL_INSTR_FAILURE) {
            return "小区建立失败";
        }

        return "";
    }
}
