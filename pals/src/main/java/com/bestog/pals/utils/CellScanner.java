package com.bestog.pals.utils;

import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

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

    /**
     * Constructor
     *
     * @param cellManager CellManager
     */
    public CellScanner(TelephonyManager cellManager) {
        this.telephonyManager = cellManager;
    }

    /**
     * Get Cellinfo from phone and store in Arraylist
     *
     * @return ArrayList
     */
    @SuppressWarnings("deprecation")
    public List<Cell> getCells() {
        List<Cell> collection = new ArrayList<>();
        String operator = telephonyManager.getNetworkOperator();
        Cell cell;
        int mcc, mnc;
        if (operator != null && operator.length() > 3) {
            mcc = Integer.valueOf(operator.substring(0, 3));
            mnc = Integer.valueOf(operator.substring(3));
        } else {
            mcc = 0;
            mnc = 0;
        }

        CellLocation cellLocation = telephonyManager.getCellLocation();
        if (cellLocation != null) {
            if (cellLocation instanceof GsmCellLocation) {
                GsmCellLocation cellInfoGsm = (GsmCellLocation) cellLocation;
                cell = new Cell(cellInfoGsm.getCid(), cellInfoGsm.getLac(), mcc, mnc);
                cell.psc = cellInfoGsm.getPsc();
                collection.add(cell);
            }
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<NeighboringCellInfo> neighboringCells = telephonyManager.getNeighboringCellInfo();
            if (neighboringCells != null) {
                for (NeighboringCellInfo c : neighboringCells) {
                    cell = new Cell(c.getCid(), c.getLac(), mcc, mnc);
                    cell.dbm = (-1 * 113 + 2 * c.getRssi());
                    cell.rad = getTypeAsString(c.getNetworkType());
                    cell.psc = c.getPsc();
                    cell.rssi = c.getRssi();
                    collection.add(cell);
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            List<CellInfo> cellsRawList = telephonyManager.getAllCellInfo();
            if (cellsRawList != null) {
                for (CellInfo c : cellsRawList) {
                    cell = new Cell();
                    if (c instanceof CellInfoGsm) {
                        // GSM
                        CellIdentityGsm cellIdentityGsm = ((CellInfoGsm) c).getCellIdentity();
                        CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) c).getCellSignalStrength();
                        cell.cid = cellIdentityGsm.getCid();
                        cell.lac = cellIdentityGsm.getLac();
                        cell.mcc = cellIdentityGsm.getMcc();
                        cell.mnc = cellIdentityGsm.getMnc();
                        cell.asu = cellSignalStrengthGsm.getAsuLevel();
                        cell.dbm = cellSignalStrengthGsm.getDbm();
                        cell.lvl = cellSignalStrengthGsm.getLevel();
                        cell.reg = c.isRegistered();
                        cell.rad = "gsm";
                    } else if (c instanceof CellInfoCdma) {
                        // CDMA
                        CellIdentityCdma cellIdentityCdma = ((CellInfoCdma) c).getCellIdentity();
                        CellSignalStrengthCdma cellSignalStrengthCdma = ((CellInfoCdma) c).getCellSignalStrength();
                        cell.cid = cellIdentityCdma.getBasestationId();
                        cell.lac = cellIdentityCdma.getNetworkId();
                        cell.mcc = mcc;
                        cell.mnc = mnc;
                        cell.asu = cellSignalStrengthCdma.getAsuLevel();
                        cell.dbm = cellSignalStrengthCdma.getDbm();
                        cell.lvl = cellSignalStrengthCdma.getLevel();
                        cell.reg = c.isRegistered();
                        cell.rad = "cdma";
                    } else if (c instanceof CellInfoLte) {
                        // LTE
                        CellIdentityLte cellIdentityLte = ((CellInfoLte) c).getCellIdentity();
                        CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) c).getCellSignalStrength();
                        cell.cid = cellIdentityLte.getCi();
                        cell.lac = cellIdentityLte.getTac();
                        cell.mcc = cellIdentityLte.getMcc();
                        cell.mnc = cellIdentityLte.getMnc();
                        cell.asu = cellSignalStrengthLte.getAsuLevel();
                        cell.dbm = cellSignalStrengthLte.getDbm();
                        cell.lvl = cellSignalStrengthLte.getLevel();
                        cell.reg = c.isRegistered();
                        cell.rad = "lte";
                    } else if (c instanceof CellInfoWcdma && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        // WCDMA
                        CellIdentityWcdma cellIdentityWcdma = ((CellInfoWcdma) c).getCellIdentity();
                        CellSignalStrengthWcdma cellSignalStrengthWcdma = ((CellInfoWcdma) c).getCellSignalStrength();
                        cell.cid = cellIdentityWcdma.getCid();
                        cell.lac = cellIdentityWcdma.getLac();
                        cell.mcc = cellIdentityWcdma.getMcc();
                        cell.mnc = cellIdentityWcdma.getMnc();
                        cell.asu = cellSignalStrengthWcdma.getAsuLevel();
                        cell.dbm = cellSignalStrengthWcdma.getDbm();
                        cell.lvl = cellSignalStrengthWcdma.getLevel();
                        cell.reg = c.isRegistered();
                        cell.rad = "cdma";
                    }
                    collection.add(cell);
                }
            }
        }
        return collection;
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
                out = "cdma";
                break;
            default:
                out = "";
        }
        return out;
    }
}
