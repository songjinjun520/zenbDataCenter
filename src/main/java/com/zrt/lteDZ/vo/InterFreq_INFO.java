package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;


public class InterFreq_INFO {
    private int ARFCN;
    private int priority;
    private int NInterFreqCellsCount;
    private int[] NInterFreqCellsInfo;

    public InterFreq_INFO() {
    }

    public InterFreq_INFO(byte[] src) {
        int srcPos = 0;
        byte[] ARFCNBytes = new byte[4];
        System.arraycopy(src, srcPos, ARFCNBytes, 2, 2);
        this.ARFCN = ByteUtils.bytes2int_big_endian2(ARFCNBytes);
        srcPos += 2;
        this.priority = src[srcPos++];
        this.NInterFreqCellsCount = src[srcPos++];
        this.NInterFreqCellsInfo = new int[this.NInterFreqCellsCount];
        for (int i = 0; i < this.NInterFreqCellsCount; i++) {
            byte[] tempBytes = new byte[4];
            System.arraycopy(src, srcPos, tempBytes, 2, 2);
            this.NInterFreqCellsInfo[i] = ByteUtils.bytes2int_big_endian2(tempBytes);
            srcPos += 2;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.NInterFreqCellsCount && i < 16; i++) {
            if (i == 0) {
                sb.append(this.NInterFreqCellsInfo);
            } else {

                sb.append(", " + this.NInterFreqCellsInfo);
            }
        }
        return "频点" + this.ARFCN + " 优先级：" + this.priority + " 异频小区数目：" + this.NInterFreqCellsCount + "[" + sb.toString() + "]";
    }
}
