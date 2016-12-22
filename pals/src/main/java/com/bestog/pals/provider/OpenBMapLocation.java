package com.bestog.pals.provider;

import android.content.Context;
<<<<<<< Updated upstream:pals/src/main/java/com/bestog/pals/provider/OpenBMapLocation.java
import android.location.Location;
=======
>>>>>>> Stashed changes:pals/src/main/java/com/bestog/pals/provider/OpenBMapLocation.java

import com.bestog.pals.objects.Cell;
import com.bestog.pals.objects.Wifi;
import com.bestog.pals.utils.CommonUtils;
import com.bestog.pals.utils.GeoResult;

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
     * request Action
     *
     * @return HashMap
     */
    @Override
    public HashMap<String, String> requestAction() {
        // @todo change algorithm
        JSONObject request = new JSONObject();
        List<Cell> cellTowers = getCellTowers();
        List<Wifi> wifiSpots = getWifiSpots();
        JSONArray wifiArray = new JSONArray();
        for (Wifi wifi : wifiSpots) {
            wifiArray.put(convertWifi(wifi));
        }
        JSONArray cellArray = new JSONArray();
        for (Cell cell : cellTowers) {
            cellArray.put(convertCell(cell));
        }
        try {
            request.put("wifiAccessPoints", wifiArray);
            request.put("cellTowers", cellArray);
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
                if (jResponse.has("location") && jResponse.getJSONObject("location").has("lat")) {
                    if (jResponse.has("location")) {
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
     * @param response HashMap
     * @return boolean
     */
    @Override
    public boolean requestValidation(HashMap<String, String> response) {
        if (response.get("response").isEmpty()) {
            try {
                new JSONObject(response.get("response"));
                return true;
            } catch (JSONException e) {
                return false;
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
<<<<<<< Updated upstream:pals/src/main/java/com/bestog/pals/provider/OpenBMapLocation.java
    public HashMap<String, String> submitAction(Location position) {
        return new HashMap<>();
=======
    public String submitAction(GeoResult position) {
        return "";
>>>>>>> Stashed changes:pals/src/main/java/com/bestog/pals/provider/OpenBMapLocation.java
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

<<<<<<< Updated upstream:pals/src/main/java/com/bestog/pals/provider/OpenBMapLocation.java
    /**
     * Convert a CellInfo in a specific format
     *
     * @param cell HashMap CellInfo
     * @return JSONObject
     */
    private static JSONObject convertCell(Cell cell) {
        JSONObject result = new JSONObject();
        try {
            result.put("cellId", cell.cid);
            result.put("locationAreaCode", cell.lac);
            result.put("mobileCountryCode", cell.mcc);
            result.put("mobileNetworkCode", cell.mnc);
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
            result.put("signalStrength", wifi.signal);
        } catch (JSONException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return result;
=======
    @Override
    protected boolean submitResult(String response) {
        return true;
>>>>>>> Stashed changes:pals/src/main/java/com/bestog/pals/provider/OpenBMapLocation.java
    }
}
