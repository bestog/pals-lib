package com.bestog.pals.provider;

import android.content.Context;

import com.bestog.pals.objects.Cell;
import com.bestog.pals.objects.GeoResult;
import com.bestog.pals.objects.ProviderResponse;
import com.bestog.pals.objects.Wifi;
import com.bestog.pals.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Class: Google Service
 * Link: https://developers.google.com/maps/documentation/geolocation
 *
 * @author bestog
 */
public class GoogleLocation extends LocationProvider {

    private final String _apiUrl = "https://www.googleapis.com/geolocation/v1/geolocate?key=";
    private final String _apiToken = "AIzaSyBe-r1t3sGkAiSpfXsI1jSeaf_mKXDVoNw";
    private final String _requestUrl;

    /**
     * Constructor
     *
     * @param ctx   Context
     * @param token String
     */
    public GoogleLocation(Context ctx, String token) {
        super(LocationProvider.PROVIDER_GOOGLE, ctx);
        _requestUrl = _apiUrl + (token != null ? token : _apiToken);
    }

    /**
     * Convert a cell in a specific format
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
            if (cell.dbm != 0) {
                result.put("signalStrength", cell.dbm);
            }
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Convert a wifiSpot in a specific format
     *
     * @param wifi Wifi WifiInfo
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
     * @return ProviderResponse
     */
    @Override
    public ProviderResponse requestAction() {
        JSONObject request = new JSONObject();
        List<Cell> cellTowers = getCellTowers();
        List<Wifi> wifiSpots = getWifiSpots();
        try {
            JSONArray cellArray = new JSONArray();
            for (Cell cell : cellTowers) {
                if (cell.cid != LocationProvider.UNKNOWN_CELLID) {
                    cellArray.put(convertCell(cell));
                }
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

    /**
     * validate result
     *
     * @param response ProviderResponse
     * @return boolean
     */
    @Override
    public boolean requestValidation(ProviderResponse response) {
        // @todo
        return true;
    }

    /**
     * submit a location
     *
     * @param position GeoResult
     * @return ProviderResponse
     */
    @Override
    public ProviderResponse submitAction(GeoResult position) {
        // @todo
        return new ProviderResponse("", "");
    }

    /**
     * validate a submit
     *
     * @param response ProviderResponse
     * @return boolean
     */
    @Override
    public boolean submitValidation(ProviderResponse response) {
        // @todo
        return true;
    }
}
