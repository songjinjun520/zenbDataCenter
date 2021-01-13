package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;













public class FreqScanMsg
        extends BaseMsg
{
    private int freqCount;
    private int[] freqs;

    public FreqScanMsg(String spType) { super(spType); }


    public FreqScanMsg(byte[] src, String spType) {
        super(src, spType);

        int srcPos = 4;

        byte[] freqCountBytes = new byte[4];
        System.arraycopy(src, srcPos, freqCountBytes, 0, 4);
        this.freqCount = ByteUtils.bytes2int_big_endian2(freqCountBytes);
        srcPos += 4;

        this.freqs = new int[this.freqCount];
        for (int i = 0; i < this.freqCount; i++) {
            byte[] freqBytes = new byte[4];
            System.arraycopy(src, srcPos, freqBytes, 2, 2);
            this.freqs[i] = ByteUtils.bytes2int_big_endian2(freqBytes);
            srcPos += 2;
        }
    }

    @Override
    public byte[] getBytes() {
        setMsgLen(4 + this.freqCount * 2);

        byte[] superBytes = super.getBytes();
        byte[] dest = new byte[8 + this.freqCount * 2];

        int destPos = 0;
        System.arraycopy(superBytes, 0, dest, destPos, 4);
        destPos += 4;

        byte[] countBytes = ByteUtils.int2bytes_big_endian(this.freqCount);
        System.arraycopy(countBytes, 0, dest, destPos, 4);
        destPos += 4;

        for (int i = 0; i < this.freqCount; i++) {
            byte[] workFreqBytes = ByteUtils.int2bytes_big_endian(this.freqs[i]);
            System.arraycopy(workFreqBytes, 2, dest, destPos, 2);
            destPos += 2;
        }

        return dest;
    }


    public int getFreqCount() { return this.freqCount; }



    public void setFreqCount(int freqCount) { this.freqCount = freqCount; }



    public int[] getFreqs() { return this.freqs; }



    public void setFreqs(int[] freqs) { this.freqs = freqs; }
}
