package com.bestog.pals.provider;

import android.content.Context;
import android.util.Log;

import com.bestog.pals.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Class: Openbmap
 * Link: http://www.radiocells.org
 *
 * @author bestog
 * @version 1.0
 */
public class OpenBMapLocation extends LocationProvider {

    private final String _apiUrl = "http://www.radiocells.org/geolocation/geolocate";
    private final String _requestUrl;

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public OpenBMapLocation(Context ctx) {
        super(LocationProvider.PROVIDER_OPENBMAP, ctx);
        _requestUrl = _apiUrl;
    }

    /**
     * Convert a CellInfo in a specific format
     *
     * @param cell HashMap CellInfo
     * @return JSONObject
     */
    private static JSONObject convertCell(HashMap<String, String> cell) {
        JSONObject result = new JSONObject();
        try {
            result.put("cellId", cell.get("cid"));
            result.put("locationAreaCode", cell.get("lac"));
            result.put("mobileCountryCode", cell.get("mcc"));
            result.put("mobileNetworkCode", cell.get("mnc"));
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Convert a WifiSpot in a specific format
     *
     * @param wifi HashMap Wifi
     * @return JSONObject
     */
    private static JSONObject convertWifi(HashMap<String, String> wifi) {
        JSONObject result = new JSONObject();
        try {
            result.put("macAddress", wifi.get("key"));
            result.put("signalStrength", "-54");
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return result;
    }

    /**
     * request Action
     *
     * @return String
     */
    @Override
    public String requestAction() {
        JSONObject request = new JSONObject();
        List<HashMap<String, String>> cellTowers = getCellTowers();
        List<HashMap<String, String>> _wifiSpots = getWifiSpots();
        JSONArray wifiArray = new JSONArray();
        for (HashMap<String, String> wifi : _wifiSpots) {
            wifiArray.put(convertWifi(wifi));
        }
        JSONArray cellArray = new JSONArray();
        for (HashMap<String, String> cell : cellTowers) {
            if (!cell.get("cid").equals(LocationProvider.UNKNOWN_CELLID)) {
                cellArray.put(convertCell(cell));
            }
        }
        try {
            request.put("wifiAccessPoints", wifiArray);
            request.put("cellTowers", cellArray);
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return CommonUtils.getRequest(_requestUrl, request.toString(), "POST", "application/json;charset=utf-8");
    }

    /**
     * request result
     *
     * @param response String
     */
    @Override
    public void requestResult(String response) {
        try {
            JSONObject jResponse = new JSONObject(response);
            if (!jResponse.has("error")) {
                if (jResponse.has("location") && jResponse.getJSONObject("location").has("lat")) {
                    if (jResponse.has("location") && jResponse.getDouble("accuracy") < 30000.0d) {
                        JSONObject jsonObject = jResponse.getJSONObject("location");
                        setLatitude(jsonObject.getDouble("lat"));
                        setLongitude(jsonObject.getDouble("lng"));
                        setAccuracy(jResponse.getInt("accuracy"));
                    }
                }
            }
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
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
    public String submitAction() {
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
