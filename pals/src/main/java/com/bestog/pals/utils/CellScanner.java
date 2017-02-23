package com.bestog.pals.utils;

import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

import com.bestog.pals.objects.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: CellScanner
 * Collect all information from the smartphone to surrounding cell towers and prepares them.
 *
 * @author bestog
 */
public class CellScanner {

    private final TelephonyManager telephonyManager;

    public CellScanner(TelephonyManager cellManager) {
        this.telephonyManager = cellManager;
    }

    /**
     * If MAX_INTEGER or incorrect values are supplied, convert to valid format.
     *
     * @param nr Number
     * @return int Valid number
     */
    private static int parse(int nr) {
        return nr < Integer.MAX_VALUE ? nr : -1;
    }

    /**
     * Assign the type CellInfo correctly
     *
     * @param type Typ
     * @return String
     */
    private static String getTypeAsString(int type) {
        String out;
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
                out = "gsm";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                out = "umts";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                out = "lte";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                out = "cmda";
                break;
            default:
                out = "";
        }
        return out;
    }

    /**
     * Get Cell Info from the information from the phone and store in list
     *
     * @return ArrayList
     */
    public List<Cell> getCells() {
        List<Cell> result = new ArrayList<>();
        Cell tmpCell;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
            for (CellInfo item : cellInfos) {
                tmpCell = new Cell();
                CellInfoGsm cellInfoGsm = (CellInfoGsm) item;
                CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
                CellSignalStrengthGsm cellStrength = cellInfoGsm.getCellSignalStrength();
                tmpCell.cid = parse(cellIdentity.getCid());
                tmpCell.lac = parse(cellIdentity.getLac());
                tmpCell.mnc = parse(cellIdentity.getMnc());
                tmpCell.mcc = parse(cellIdentity.getMcc());
                tmpCell.asu = parse(cellStrength.getAsuLevel());
                tmpCell.dbm = parse(cellStrength.getDbm());
                tmpCell.lvl = parse(cellStrength.getLevel());
                tmpCell.reg = cellInfoGsm.isRegistered();
                result.add(tmpCell);
            }
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<NeighboringCellInfo> cellInfoList = telephonyManager.getNeighboringCellInfo();
            String networkOperator = telephonyManager.getNetworkOperator();
            for (NeighboringCellInfo cellInfo : cellInfoList) {
                tmpCell = new Cell();
                tmpCell.cid = parse(cellInfo.getCid());
                tmpCell.lac = parse(cellInfo.getLac());
                tmpCell.mcc = Integer.parseInt(networkOperator.substring(3));
                tmpCell.mnc = Integer.parseInt(networkOperator.substring(0, 3));
                tmpCell.dbm = -1 * 113 + 2 * cellInfo.getRssi();
                tmpCell.rad = getTypeAsString(cellInfo.getNetworkType());
                result.add(tmpCell);
            }
        }
        return result;
    }
}
