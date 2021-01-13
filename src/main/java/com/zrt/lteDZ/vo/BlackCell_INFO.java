package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;

public class BlackCell_INFO {
    private int ARFCN;
    private int NCellRangeCount;
    private CellRange_INFO[] nCellRangeInfo = new CellRange_INFO[4];


    public BlackCell_INFO() {}


    public BlackCell_INFO(byte[] src) {
        int srcPos = 0;
        byte[] ARFCNBytes = new byte[4];
        System.arraycopy(src, srcPos, ARFCNBytes, 2, 2);
        this.ARFCN = ByteUtils.bytes2int_big_endian2(ARFCNBytes);
        srcPos += 2;
        byte[] NCellRangeCountBytes = new byte[4];
        System.arraycopy(src, srcPos, NCellRangeCountBytes, 2, 2);
        this.NCellRangeCount = ByteUtils.bytes2int_big_endian2(NCellRangeCountBytes);
        srcPos += 2;
        this.nCellRangeInfo = new CellRange_INFO[this.NCellRangeCount];
        for (int i = 0; i < this.NCellRangeCount; i++) {
            byte[] tempBytes = new byte[4];
            System.arraycopy(src, srcPos, tempBytes, 0, 4);
            this.nCellRangeInfo[i] = new CellRange_INFO(tempBytes);
            srcPos += 4;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.NCellRangeCount && i < this.nCellRangeInfo.length; i++) {
            CellRange_INFO temp = this.nCellRangeInfo[i];
            sb.append(temp.toString());
        }
        return "频点：" + this.ARFCN + " PCI区间个数：" + this.NCellRangeCount + " " + sb.toString();
    }
}
