package com.bestog.pals.objects;

/**
 * Class: Cell
 *
 * @author bestog
 */
public class Cell {

    // Cell-Identification number
    public int cid;
    // Location Area Code
    public int lac;
    // Mobile Country Code
    public int mcc;
    // Mobile Network Code
    public int mnc;
    // Mobile phone signal
    public int asu;
    // Primary scrambling code, only for UMTS
    public int psc;
    // Decibel Milliwatt
    public int dbm;
    // signal level
    public int lvl;
    // Receive Signal Strength Indicator
    public int rssi;
    // Cellular radio system
    public String rad;
    // Registered
    public boolean reg;

    /**
     * Default constructor
     */
    public Cell() {
    }

    /**
     * Specific constructor
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
