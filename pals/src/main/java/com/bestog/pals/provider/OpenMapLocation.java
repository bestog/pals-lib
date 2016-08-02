package com.bestog.pals.provider;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.bestog.pals.objects.Wifi;
import com.bestog.pals.utils.CommonUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Class: Open Wlan Map
 * Link: https://openwifi.su
 *
 * @author bestog
 */
public class OpenMapLocation extends LocationProvider {

    private final static String TAG = "OpenMapLocation";

    private final String _apiUrl = "http://openwlanmap.org/getpos.php";
    private final String _apiToken = "";
    private final String _requestUrl;

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public OpenMapLocation(Context ctx) {
        super(LocationProvider.PROVIDER_OPENMAP, ctx);
        _requestUrl = _apiUrl + _apiToken;
    }

    /**
     * request action
     *
     * @return HashMap
     */
    @Override
    public HashMap<String, String> requestAction() {
        StringBuilder request = new StringBuilder();
        List<Wifi> wifiSpots = getWifiSpots();
        for (Wifi wifi : wifiSpots) {
            request.append(wifi.mac).append("\r\n");
        }
        Log.i(TAG, "start request");
        return CommonUtils.httpRequest(_requestUrl, request.toString(), "POST", "application/x-www-form-urlencoded");
    }

    /**
     * request result
     *
     * @param response String
     */
    @Override
    public void requestResult(String response) {
        // @todo: Format-changing, update algorithm
        Log.i(TAG, "read response");
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(response.getBytes("UTF-8")));
            if (!properties.isEmpty()) {
                Log.i(TAG, "set coordinates");
                setLatitude(Double.parseDouble(properties.getProperty("lat", "0")));
                setLongitude(Double.parseDouble(properties.getProperty("lon", "0")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * validate result
     *
     * @param response HashMap
     * @return boolean
     */
    @Override
    public boolean requestValidation(HashMap<String, String> response) {
        Log.i(TAG, "validate response");
        return !response.get("response").isEmpty();
    }

    /**
     * submit a location
     *
     * @return HashMap
     */
    @Override
    public HashMap<String, String> submitAction(Location position) {
        return new HashMap<>();
    }

    /**
     * validate a submit
     *
     * @param response HashMap
     * @return boolean
     */
    @Override
    public boolean submitValidation(HashMap<String, String> response) {
        return true;
    }

}