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
 * Class: Mozilla Location Service
 * Link: https://location.services.mozilla.com
 *
 * @author bestog
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
     * @param ctx   Context
     * @param token String
     */
    public MozillaLocation(Context ctx, String token) {
        super(LocationProvider.PROVIDER_MOZILLA, ctx);
        _requestApiUrl = _requestUrl + (token != null ? token : _requestToken);
        _submitApiUrl = _submitUrl + _submitToken;
    }

    /**
     * request action
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
                    JSONObject result = new JSONObject();
                    try {
                        result.put("mobileCountryCode", cell.mnc);
                        result.put("mobileNetworkCode", cell.mcc);
                        result.put("locationAreaCode", cell.lac);
                        result.put("cellId", cell.cid);
                        if (cell.dbm != 0) {
                            result.put("signalStrength", cell.dbm);
                        }
                    } catch (JSONException e) {
                        // @todo better logging
                        e.printStackTrace();
                    }
                    cellArray.put(result);
                }
            }
            request.put("cellTowers", cellArray);
            JSONArray wifiArray = new JSONArray();
            for (Wifi wifi : wifiSpots) {
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
                wifiArray.put(result);
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
    protected boolean requestValidation(ProviderResponse response) {
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
        JSONObject submit = new JSONObject();
        JSONObject item = new JSONObject();
        List<Cell> cellTowers = getCellTowers();
        List<Wifi> wifiSpots = getWifiSpots();

        try {
            JSONObject infos = new JSONObject();
            infos.put("latitude", position.getLatitude());
            infos.put("longitude", position.getLongitude());
            infos.put("accuracy", position.getAccuracy());
            infos.put("source", "gps");

            item.put("timestamp", System.currentTimeMillis() / 1000);
            item.put("position", infos);

            JSONArray cellArray = new JSONArray();
            for (Cell cell : cellTowers) {
                if (cell.cid != LocationProvider.UNKNOWN_CELLID) {
                    JSONObject result = new JSONObject();
                    try {
                        result.put("mobileCountryCode", cell.mnc);
                        result.put("mobileNetworkCode", cell.mcc);
                        result.put("locationAreaCode", cell.lac);
                        result.put("cellId", cell.cid);
                        if (cell.dbm != 0) {
                            result.put("signalStrength", cell.dbm);
                        }
                    } catch (JSONException e) {
                        // @todo better logging
                        e.printStackTrace();
                    }
                    cellArray.put(result);
                }
            }
            item.put("cellTowers", cellArray);
            JSONArray wifiArray = new JSONArray();
            for (Wifi wifi : wifiSpots) {
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
                wifiArray.put(result);
            }
            item.put("wifiAccessPoints", wifiArray);
            submit.put("items", (new JSONArray()).put(item));
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return CommonUtils.httpRequest(_submitApiUrl, submit.toString(), "POST", "application/json;charset=utf-8");
    }

    /**
     * validate a submit
     *
     * @param response ProviderResponse
     * @return boolean
     */
    @Override
    public boolean submitValidation(ProviderResponse response) {
        // Successful requests return a HTTP 200 response with a body of an empty JSON object.
        if (response.response.length() > 0) {
            try {
                JSONObject check = new JSONObject(response.response);
                return check.length() == 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
