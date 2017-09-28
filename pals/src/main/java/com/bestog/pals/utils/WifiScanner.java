package com.bestog.pals.utils;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.bestog.pals.objects.Wifi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class: WifiScanner
 * Collect all information from the smartphone to surrounding wireless networks and prepares them.
 *
 * @author bestog
 */
public class WifiScanner {

    private final WifiManager _wifiManager;

    /**
     * Constructor
     *
     * @param wifiManager Wifi-Manager
     */
    public WifiScanner(WifiManager wifiManager) {
        _wifiManager = wifiManager;
    }

    /**
     * Assign the channel to the frequency properly
     *
     * @param freq Frequenz
     * @return int
     */
    private static int getChannel(int freq) {
        ArrayList<Integer> channels = new ArrayList<>(
                Arrays.asList(0, 2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447,
                        2452, 2457, 2462, 2467, 2472, 2484));
        return channels.indexOf(freq);
    }

    /**
     * Is wifi enabled?
     *
     * @return boolean
     */
    private boolean isWifiEnabled() {
        if (_wifiManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && _wifiManager.isScanAlwaysAvailable()) {
                return true;
            } else if (_wifiManager.isWifiEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get wifi-networks from the phone-information and store in list
     *
     * @return ArrayList
     */
    public List<Wifi> getSpots() {
        List<Wifi> result = new ArrayList<>();
        Wifi tmp;
        if (isWifiEnabled()) {
            List<ScanResult> scanResultList = _wifiManager.getScanResults();
            for (ScanResult wifi : scanResultList) {
                tmp = new Wifi();
                tmp.signal = wifi.level;
                tmp.freq = wifi.frequency;
                tmp.channel = getChannel(wifi.frequency);
                tmp.mac = wifi.BSSID;
                result.add(tmp);
            }
        }
        return result;
    }

}
