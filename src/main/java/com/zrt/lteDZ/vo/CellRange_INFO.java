package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;


public class CellRange_INFO
{
    private int PCIStart;
    private int PCIRange;

    public CellRange_INFO() {}

    public CellRange_INFO(byte[] src) {
        int srcPos = 0;
        byte[] PCIStartBytes = new byte[4];
        System.arraycopy(src, srcPos, PCIStartBytes, 2, 2);
        this.PCIStart = ByteUtils.bytes2int_big_endian2(PCIStartBytes);
        srcPos += 2;
        byte[] PCIRangeBytes = new byte[4];
        System.arraycopy(src, srcPos, PCIRangeBytes, 2, 2);
        this.PCIRange = ByteUtils.bytes2int_big_endian2(PCIRangeBytes);
        srcPos += 2;
    }


    public String toString() { return "[PCI起始值：" + this.PCIStart + ", PCI范围：" + this.PCIRange + "]"; }
}
