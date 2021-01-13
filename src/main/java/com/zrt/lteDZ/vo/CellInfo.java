package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;

public class CellInfo {
    private int CELL_ID;
    private String PLMN;
    private int ARFCN;
    private int phyCellId;
    private int TAC;
    private int RSRP;
    private int priority;
    private int dlulconfig;
    private int specialFrameConfig;
    private int bandwidth;
    private int SIBFlag;
    private int SIB4Flag;
    private int SIB5Flag;
    private int Reserved1;
    private int Reserved2;
    private int NIntraFreqCellsCount;
    private int[] nIntraFreqCells = new int[16];
    private int NInterFreqsCount;
    private InterFreq_INFO[] nInterFreqInfo = new InterFreq_INFO[8];
    private int NBlackCellsCount;
    private BlackCell_INFO[] nBlackCellInfo = new BlackCell_INFO[8];

    private int MIBFlag;

    private int SIB1Flag;

    private int SIB2Flag;
    private int SIB3Flag;

    public CellInfo() {}

    public CellInfo(byte[] src) {
        int srcPos = 0;

        byte[] cellIdBytes = new byte[4];
        System.arraycopy(src, srcPos, cellIdBytes, 0, 4);
        this.CELL_ID = ByteUtils.bytes2int_big_endian2(cellIdBytes);
        srcPos += 4;
        byte[] plmnBytes = new byte[6];
        System.arraycopy(src, srcPos, plmnBytes, 0, 6);
        this.PLMN = (new String(plmnBytes)).trim();
        srcPos += 6;

        byte[] ARFCNBytes = new byte[4];
        System.arraycopy(src, srcPos, ARFCNBytes, 2, 2);
        this.ARFCN = ByteUtils.bytes2int_big_endian2(ARFCNBytes);
        srcPos += 2;
        byte[] phyCellIdBytes = new byte[4];
        System.arraycopy(src, srcPos, phyCellIdBytes, 2, 2);
        this.phyCellId = ByteUtils.bytes2int_big_endian2(phyCellIdBytes);
        srcPos += 2;
        byte[] TACBytes = new byte[4];
        System.arraycopy(src, srcPos, TACBytes, 2, 2);
        this.TAC = ByteUtils.bytes2int_big_endian2(TACBytes);
        srcPos += 2;
        this.RSRP = src[srcPos++];
        this.priority = src[srcPos++];
        this.dlulconfig = src[srcPos++];
        this.specialFrameConfig = src[srcPos++];
        this.bandwidth = src[srcPos++];
        this.SIBFlag = src[srcPos++];
        this.SIB4Flag = src[srcPos++];
        this.SIB5Flag = src[srcPos++];






        this.MIBFlag = ByteUtils.getIntFromBit(this.SIBFlag, 31, 1);
        this.SIB1Flag = ByteUtils.getIntFromBit(this.SIBFlag, 30, 1);
        this.SIB2Flag = ByteUtils.getIntFromBit(this.SIBFlag, 29, 1);
        this.SIB3Flag = ByteUtils.getIntFromBit(this.SIBFlag, 28, 1);

        srcPos += 4;
        srcPos += 4;
        byte[] NIntraFreqCellsCountBytes = new byte[4];
        System.arraycopy(src, srcPos, NIntraFreqCellsCountBytes, 0, 4);
        this.NIntraFreqCellsCount = ByteUtils.bytes2int_big_endian2(NIntraFreqCellsCountBytes);
        srcPos += 4;
        for (int i = 0; i < 16; i++) {
            byte[] tempBytes = new byte[4];
            System.arraycopy(src, srcPos, tempBytes, 2, 2);
            this.nIntraFreqCells[i] = ByteUtils.bytes2int_big_endian2(tempBytes);
            srcPos += 2;
        }
        byte[] NInterFreqsCountBytes = new byte[4];
        System.arraycopy(src, srcPos, NInterFreqsCountBytes, 0, 4);
        this.NInterFreqsCount = ByteUtils.bytes2int_big_endian2(NInterFreqsCountBytes);
        srcPos += 4;
        for (int i = 0; i < 8; i++) {
            byte[] temp = new byte[36];
            System.arraycopy(src, srcPos, temp, 0, 36);
            this.nInterFreqInfo[i] = new InterFreq_INFO(temp);
            srcPos += 36;
        }

        byte[] NBlackCellsCountBytes = new byte[4];
        System.arraycopy(src, srcPos, NBlackCellsCountBytes, 0, 4);
        this.NBlackCellsCount = ByteUtils.bytes2int_big_endian2(NBlackCellsCountBytes);
        srcPos += 4;
        for (int i = 0; i < 8; i++) {
            byte[] temp = new byte[20];
            System.arraycopy(src, srcPos, temp, 0, 20);
            this.nBlackCellInfo[i] = new BlackCell_INFO(temp);
            srcPos += 20;
        }
    }


    public int getCELL_ID() { return this.CELL_ID; }



    public void setCELL_ID(int cELL_ID) { this.CELL_ID = cELL_ID; }



    public String getPLMN() { return this.PLMN; }



    public void setPLMN(String pLMN) { this.PLMN = pLMN; }



    public int getARFCN() { return this.ARFCN; }



    public void setARFCN(int aRFCN) { this.ARFCN = aRFCN; }



    public int getPhyCellId() { return this.phyCellId; }



    public void setPhyCellId(int phyCellId) { this.phyCellId = phyCellId; }



    public int getTAC() { return this.TAC; }



    public void setTAC(int tAC) { this.TAC = tAC; }



    public int getRSRP() { return this.RSRP; }



    public void setRSRP(int rSRP) { this.RSRP = rSRP; }



    public int getPriority() { return this.priority; }



    public void setPriority(int priority) { this.priority = priority; }



    public int getDlulconfig() { return this.dlulconfig; }



    public void setDlulconfig(int dlulconfig) { this.dlulconfig = dlulconfig; }



    public int getSpecialFrameConfig() { return this.specialFrameConfig; }



    public void setSpecialFrameConfig(int specialFrameConfig) { this.specialFrameConfig = specialFrameConfig; }



    public int getBandwidth() { return this.bandwidth; }



    public void setBandwidth(int bandwidth) { this.bandwidth = bandwidth; }



    public int getSIBFlag() { return this.SIBFlag; }



    public void setSIBFlag(int sIBFlag) { this.SIBFlag = sIBFlag; }



    public int getSIB4Flag() { return this.SIB4Flag; }



    public void setSIB4Flag(int sIB4Flag) { this.SIB4Flag = sIB4Flag; }



    public int getSIB5Flag() { return this.SIB5Flag; }



    public void setSIB5Flag(int sIB5Flag) { this.SIB5Flag = sIB5Flag; }



    public int getReserved1() { return this.Reserved1; }



    public void setReserved1(int reserved1) { this.Reserved1 = reserved1; }



    public int getReserved2() { return this.Reserved2; }



    public void setReserved2(int reserved2) { this.Reserved2 = reserved2; }



    public int getNIntraFreqCellsCount() { return this.NIntraFreqCellsCount; }



    public void setNIntraFreqCellsCount(int nIntraFreqCellsCount) { this.NIntraFreqCellsCount = nIntraFreqCellsCount; }



    public int[] getnIntraFreqCells() { return this.nIntraFreqCells; }



    public void setnIntraFreqCells(int[] nIntraFreqCells) { this.nIntraFreqCells = nIntraFreqCells; }



    public int getNInterFreqsCount() { return this.NInterFreqsCount; }



    public void setNInterFreqsCount(int nInterFreqsCount) { this.NInterFreqsCount = nInterFreqsCount; }



    public InterFreq_INFO[] getnInterFreqInfo() { return this.nInterFreqInfo; }



    public void setnInterFreqInfo(InterFreq_INFO[] nInterFreqInfo) { this.nInterFreqInfo = nInterFreqInfo; }



    public int getNBlackCellsCount() { return this.NBlackCellsCount; }



    public void setNBlackCellsCount(int nBlackCellsCount) { this.NBlackCellsCount = nBlackCellsCount; }



    public BlackCell_INFO[] getnBlackCellInfo() { return this.nBlackCellInfo; }



    public void setnBlackCellInfo(BlackCell_INFO[] nBlackCellInfo) { this.nBlackCellInfo = nBlackCellInfo; }



    public int getMIBFlag() { return this.MIBFlag; }



    public int getSIB1Flag() { return this.SIB1Flag; }



    public int getSIB2Flag() { return this.SIB2Flag; }



    public int getSIB3Flag() { return this.SIB3Flag; }
}
