package com.bestog.pals.utils;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Class: WifiScanner
 * Collect all information from the smartphone to surrounding wireless networks and prepares them.
 *
 * @author bestog
 */
public class WifiScanner {

    private final WifiManager _wifiManager;

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
     * Get Wi-Fi networks from the information from the phone and store in list
     *
     * @return ArrayList
     */
    public List<HashMap<String, String>> getSpots() {
        List<ScanResult> scanResultList = _wifiManager.getScanResults();
        List<HashMap<String, String>> result = new ArrayList<>();
        for (ScanResult wifi : scanResultList) {
            HashMap<String, String> item = new HashMap<>();
            item.put("signal", String.valueOf(wifi.level));
            item.put("frequency", String.valueOf(wifi.frequency));
            item.put("channel", String.valueOf(getChannel(wifi.frequency)));
            item.put("key", wifi.BSSID);
            result.add(item);
        }
        return result;
    }

}
