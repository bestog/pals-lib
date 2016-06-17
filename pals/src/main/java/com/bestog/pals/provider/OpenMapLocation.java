package com.bestog.pals.provider;

import android.content.Context;
import android.location.Location;

import com.bestog.pals.utils.CommonUtils;
import com.bestog.pals.utils.GeoResult;

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
     * request Action
     *
     * @return String
     */
    @Override
    public String requestAction() {
        String request = "";
        List<HashMap<String, String>> wifiSpots = getWifiSpots();
        for (HashMap<String, String> wifi : wifiSpots) {
            request += wifi.get("key") + "\r\n";
        }
        return CommonUtils.httpRequest(_requestUrl, request, "POST", "application/x-www-form-urlencoded, *.*");
    }

    /**
     * request result
     *
     * @param response String
     */
    @Override
    public void requestResult(String response) {
        if (!response.isEmpty()) {
            Properties properties = new Properties();
            try {
                properties.load(new ByteArrayInputStream(response.getBytes("UTF-8")));
                if (!properties.isEmpty()) {
                    setLatitude(Double.parseDouble(properties.getProperty("lat", LocationProvider.NULL)));
                    setLongitude(Double.parseDouble(properties.getProperty("lon", LocationProvider.NULL)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * validate result
     *
     * @param response String
     * @return boolean
     */
    @Override
    public boolean requestValidation(String response) {
        return true;
    }

    /**
     * submit a location
     *
     * @return String
     */
    @Override
    public String submitAction(Location position) {
        return "";
    }

    /**
     * validate a submit
     *
     * @param response String
     * @return boolean
     */
    @Override
    public boolean submitValidation(String response) {
        return true;
    }

}