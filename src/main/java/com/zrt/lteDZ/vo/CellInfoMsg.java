package com.zrt.lteDZ.vo;

import com.zrt.common.util.ByteUtils;


public class CellInfoMsg
        extends BaseMsg
{
    private int reserved;
    private int result;
    private int cellsCount;
    private CellInfo[] cellInfos;

    public CellInfoMsg(String spType) { super(spType); }


    public CellInfoMsg(byte[] src, String spType) {
        super(src, spType);

        int srcPos = 4;

        srcPos += 4;

        byte[] resultBytes = new byte[4];
        System.arraycopy(src, srcPos, resultBytes, 2, 2);
        this.result = ByteUtils.bytes2int_big_endian2(resultBytes);
        srcPos += 2;

        byte[] cellsCountBytes = new byte[4];
        System.arraycopy(src, srcPos, cellsCountBytes, 2, 2);
        this.cellsCount = ByteUtils.bytes2int_big_endian2(cellsCountBytes);
        srcPos += 2;

        this.cellInfos = new CellInfo[this.cellsCount];
        for (int i = 0; i < this.cellsCount; i++) {
            try {
                byte[] temp = new byte[524];
                System.arraycopy(src, srcPos, temp, 0, 524);
                this.cellInfos[i] = new CellInfo(temp);
                srcPos += 524;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public int getResult() { return this.result; }



    public void setResult(int result) { this.result = result; }



    public int getCellsCount() { return this.cellsCount; }



    public void setCellsCount(int cellsCount) { this.cellsCount = cellsCount; }



    public CellInfo[] getCellInfos() { return this.cellInfos; }



    public void setCellInfos(CellInfo[] cellInfos) { this.cellInfos = cellInfos; }


    public static void main(String[] args) {
        byte[] testBytes = { -35, 4, 32, 3, -23, 2, 6, -67, 74, 3, 52, 54, 48, 48, 48, -106, 1, -71, 112, 61, 35, 2, 6, 100, 3, 6, -67, 74, 1, 52, 54, 48, 48, 48, -106, 1, -69, 112, 61, 32, 2, 6, 100, 3 };
        CellInfoMsg cellInfoMsg = new CellInfoMsg(testBytes, null);
        System.out.println("cellsCount : " + cellInfoMsg.getCellsCount());
    }
}
