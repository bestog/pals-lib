package com.bestog.pals.provider;

import android.content.Context;
import android.location.Location;

import com.bestog.pals.objects.Cell;
import com.bestog.pals.objects.Wifi;
import com.bestog.pals.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Class: Google Service
 * Link: https://developers.google.com/maps/documentation/geolocation
 *
 * @author bestog
 */
public class GoogleLocation extends LocationProvider {

    private final String _apiUrl = "https://www.googleapis.com/geolocation/v1/geolocate?key=";
    private final String _apiToken = "AIzaSyADSJOy4rbDynZ5bllYYMg4OS0I87v1uMo";
    private final String _requestUrl;

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public GoogleLocation(Context ctx) {
        super(LocationProvider.PROVIDER_GOOGLE, ctx);
        _requestUrl = _apiUrl + _apiToken;
    }

    /**
     * Constructor with specific token
     *
     * @param ctx   Context
     * @param token String Access-Token
     */
    public GoogleLocation(Context ctx, String token) {
        super(LocationProvider.PROVIDER_GOOGLE, token, ctx);
        _requestUrl = _apiUrl + token;
    }

    /**
     * Convert a CellInfo in a specific format
     *
     * @param cell Cell CellInfo
     * @return JSONObject
     */
    private static JSONObject convertCell(Cell cell) {
        JSONObject result = new JSONObject();
        try {
            result.put("mobileCountryCode", cell.mnc);
            result.put("mobileNetworkCode", cell.mcc);
            result.put("locationAreaCode", cell.lac);
            result.put("cellId", cell.cid);
            result.put("signalStrength", cell.dbm);
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
    private static JSONObject convertWifi(Wifi wifi) {
        JSONObject result = new JSONObject();
        try {
            result.put("macAddress", wifi.mac);
            result.put("channel", wifi.channel);
            result.put("frequency", wifi.freq);
            result.put("signalStrength", wifi.signal);
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return result;
    }

    /**
     * request Action
     *
     * @return HashMap
     */
    @Override
    public HashMap<String, String> requestAction() {
        JSONObject request = new JSONObject();
        List<Cell> cellTowers = getCellTowers();
        List<Wifi> wifiSpots = getWifiSpots();
        try {
            JSONArray cellArray = new JSONArray();
            for (Cell cell : cellTowers) {
                cellArray.put(convertCell(cell));
            }
            request.put("cellTowers", cellArray);
            JSONArray wifiArray = new JSONArray();
            for (Wifi wifi : wifiSpots) {
                wifiArray.put(convertWifi(wifi));
            }
            request.put("wifiAccessPoints", wifiArray);
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return CommonUtils.httpRequest(_requestUrl, request.toString(), "POST", "application/json;charset=utf-8");
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
            if (jResponse.has("location")) {
                JSONObject jsonObject = jResponse.getJSONObject("location");
                super.setLatitude(jsonObject.getDouble("lat"));
                super.setLongitude(jsonObject.getDouble("lng"));
                float accuracy = Float.parseFloat(jResponse.getString("accuracy"));
                super.setAccuracy(Math.round(accuracy));
            }
        } catch (JSONException e) {
            // @todo better logging
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
        if (response.containsKey("response")) {
            try {
                JSONObject jResponse = new JSONObject(response.get("response"));
                return (!jResponse.has("error"));
            } catch (JSONException e) {
                // @todo better logging
                e.printStackTrace();
            }
        }
        return false;
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
     * @param response String
     * @return boolean
     */
    @Override
    public boolean submitValidation(HashMap<String, String> response) {
        return true;
    }
}
