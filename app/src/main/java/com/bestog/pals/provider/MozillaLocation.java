package com.bestog.pals.provider;

import android.content.Context;

import com.bestog.pals.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Class: Mozilla Location Service
 * Link: https://location.services.mozilla.com
 *
 * @author bestog
 * @version 1.0
 */
public class MozillaLocation extends LocationProvider {

    private final String _requestUrl = "https://location.services.mozilla.com/v1/geolocate?key=";
    private final String _requestToken = "8a677f74-e6e6-43ed-b0db-9926efd0bbe4";
    private final String _requestApiUrl;
    private final String _submitUrl = "https://location.services.mozilla.com/v2/geosubmit?key=";
    private final String _submitToken = "8a677f74-e6e6-43ed-b0db-9926efd0bbe4";
    private final String _submitApiUrl;

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public MozillaLocation(Context ctx) {
        super(LocationProvider.PROVIDER_MOZILLA, ctx);
        _requestApiUrl = _requestUrl + _requestToken;
        _submitApiUrl = _submitUrl + _submitToken;
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
            result.put("mobileCountryCode", Integer.parseInt(cell.get("mnc")));
            result.put("mobileNetworkCode", Integer.parseInt(cell.get("mcc")));
            result.put("locationAreaCode", Integer.parseInt(cell.get("lac")));
            result.put("cellId", Integer.parseInt(cell.get("cid")));
            if (cell.containsKey("dbm")) {
                result.put("signalStrength", Integer.parseInt(cell.get("dbm")));
            }
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
            result.put("channel", Integer.parseInt(wifi.get("channel")));
            result.put("frequency", Integer.parseInt(wifi.get("frequency")));
            result.put("signalStrength", Integer.parseInt(wifi.get("signal")));
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
        List<HashMap<String, String>> wifiSpots = getWifiSpots();
        try {
            JSONArray cellArray = new JSONArray();
            for (HashMap<String, String> cell : cellTowers) {
                if (!cell.get("cid").equals(LocationProvider.UNKNOWN_CELLID)) {
                    cellArray.put(convertCell(cell));
                }
            }
            request.put("cellTowers", cellArray);
            JSONArray wifiArray = new JSONArray();
            for (HashMap<String, String> wifi : wifiSpots) {
                wifiArray.put(convertWifi(wifi));
            }
            request.put("wifiAccessPoints", wifiArray);
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return CommonUtils.getRequest(_requestApiUrl, request.toString(), "POST", "application/json;charset=utf-8");
    }

    /**
     * request result
     *
     * @param response String
     */
    @Override
    public void requestResult(String response) {
        if (requestValidation(response)) {
            try {
                JSONObject jResponse = new JSONObject(response);
                if (!jResponse.has("error")) {
                    if (jResponse.has("location")) {
                        JSONObject jsonObject = jResponse.getJSONObject("location");
                        super.setLatitude(jsonObject.getDouble("lat"));
                        super.setLongitude(jsonObject.getDouble("lng"));
                        super.setAccuracy(jResponse.getInt("accuracy"));
                    }
                }
            } catch (JSONException e) {
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
    protected boolean requestValidation(String response) {
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
