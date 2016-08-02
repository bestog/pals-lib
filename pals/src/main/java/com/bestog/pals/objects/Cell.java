package com.bestog.pals.objects;

/**
 * Class: Cell
 *
 * @author bestog
 */
public class Cell {

    public int cid;
    public int lac;
    public int mcc;
    public int mnc;
    public int asu;
    public int psc;
    public int dbm;
    public int lvl;
    public int rssi;
    public String rad;
    public boolean reg;

    public Cell() {
    }

    /**
     * Sepcific constructor
     *
     * @param cid int
     * @param lac int
     * @param mcc int
     * @param mnc int
     */
    public Cell(int cid, int lac, int mcc, int mnc) {
        this.cid = cid;
        this.lac = lac;
        this.mcc = mcc;
        this.mnc = mnc;
    }
}
