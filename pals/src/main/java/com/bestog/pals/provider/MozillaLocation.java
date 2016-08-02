package com.bestog.pals.provider;

import android.content.Context;
import android.location.Location;

import com.bestog.pals.objects.Cell;
import com.bestog.pals.objects.Wifi;
import com.bestog.pals.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Class: Mozilla Location Service
 * Link: https://location.services.mozilla.com
 *
 * @author bestog
 */
public class MozillaLocation extends LocationProvider {

    private final String _requestUrl = "https://location.services.mozilla.com/v1/geolocate?key=";
    private final String _requestApiUrl;
    private final String _submitUrl = "https://location.services.mozilla.com/v2/geosubmit?key=";
    private final String _submitApiUrl;
    private final String _accessToken = "8a677f74-e6e6-43ed-b0db-9926efd0bbe4";

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public MozillaLocation(Context ctx) {
        super(LocationProvider.PROVIDER_MOZILLA, ctx);
        _requestApiUrl = _requestUrl + _accessToken;
        _submitApiUrl = _submitUrl + _accessToken;
    }

    /**
     * Constructor with specific token
     *
     * @param ctx   Context
     * @param token String Access-Token
     */
    public MozillaLocation(Context ctx, String token) {
        super(LocationProvider.PROVIDER_MOZILLA, token, ctx);
        _requestApiUrl = _requestUrl + token;
        _submitApiUrl = _submitUrl + token;
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
        return CommonUtils.httpRequest(_requestApiUrl, request.toString(), "POST", "application/json;charset=utf-8");
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
     * @param response String
     * @return boolean
     */
    @Override
    protected boolean requestValidation(HashMap<String, String> response) {
        try {
            JSONObject jResponse = new JSONObject(response.get("response"));
            return (!jResponse.has("error"));
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
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
        JSONObject reports = new JSONObject();
        JSONObject report = new JSONObject();
        List<Cell> cellTowers = getCellTowers();
        List<Wifi> wifiSpots = getWifiSpots();
        try {
            JSONArray cellArray = new JSONArray();
            for (Cell cell : cellTowers) {
                cellArray.put(convertCell(cell));
            }
            report.put("cellTowers", cellArray);
            JSONArray wifiArray = new JSONArray();
            for (Wifi wifi : wifiSpots) {
                wifiArray.put(convertWifi(wifi));
            }
            report.put("wifiAccessPoints", wifiArray);
            report.put("timestamp", (new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())).getTime());
            report.put("position", convertPosition(position));
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(report);
            reports.put("items", jsonArray);
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return CommonUtils.httpRequest(_submitApiUrl, reports.toString(), "POST", "application/json;charset=utf-8");
    }

    /**
     * validate a submit
     *
     * @param response String
     * @return boolean
     */
    @Override
    public boolean submitValidation(HashMap<String, String> response) {
        return response.isEmpty();
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
            result.put("mobileCountryCode", cell.mcc);
            result.put("mobileNetworkCode", cell.mnc);
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
     * Convert position to a valid format.
     *
     * @param position Location
     * @return JSONObject
     */
    private static JSONObject convertPosition(Location position) {
        JSONObject result = new JSONObject();
        try {
            result.put("latitude", position.getLatitude());
            result.put("longitude", position.getLongitude());
            result.put("accuracy", position.getAccuracy());
            result.put("altitude", position.getAltitude());
            result.put("speed", position.getSpeed());
            result.put("source", "gps");
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return result;
    }
}
