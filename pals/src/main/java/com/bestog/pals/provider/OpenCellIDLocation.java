package com.bestog.pals.provider;


import android.content.Context;
import android.location.Location;

import com.bestog.pals.objects.Cell;
import com.bestog.pals.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Class: OpenCellID
 * Link: http://opencellid.org
 *
 * @author bestog
 */
public class OpenCellIDLocation extends LocationProvider {

    private final String _apiUrl = "http://www.opencellid.org/cell/get?key=";
    private final String _apiToken = "a5fceaa4-afc5-4647-896f-c0794c8b3122";
    private final String _requestUrl;

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public OpenCellIDLocation(Context ctx) {
        super(LocationProvider.PROVIDER_OPENCELLID, ctx);
        _requestUrl = _apiUrl + _apiToken;
    }

    /**
     * Constructor with specific token
     *
     * @param ctx   Context
     * @param token String Access-Token
     */
    public OpenCellIDLocation(Context ctx, String token) {
        super(LocationProvider.PROVIDER_OPENCELLID, token, ctx);
        _requestUrl = _apiUrl + token;
    }

    /**
     * request Action
     *
     * @return HashMap
     */
    @Override
    public HashMap<String, String> requestAction() {
        List<Cell> cellTowers = getCellTowers();
        double[] d = {0.0d, 0.0d, 0.0d};
        Collection<double[]> list = new ArrayList<>();
        for (Cell cell : cellTowers) {
            String url = _requestUrl + "&mcc=" + cell.mcc + "&mnc=" + cell.mnc + "&cellid=" + cell.cid + "&lac=" + cell.lac + "&format=json";
            HashMap<String, String> response = CommonUtils.httpRequest(url, "", "GET", "application/json;charset=utf-8");
            if (requestValidation(response)) {
                try {
                    if (response.containsKey("response")) {
                        JSONObject out = new JSONObject(response.get("response"));
                        if (out.has("lat") && out.has("lon")) {
                            d[0] = Double.parseDouble(out.get("lat").toString());
                            d[1] = Double.parseDouble(out.get("lon").toString());
                            d[2] = Double.parseDouble(out.get("range").toString());
                            list.add(d);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        double lat = 0;
        double lon = 0;
        double range = 0;
        for (double[] result : list) {
            lat += result[0];
            lon += result[1];
            range += result[2];
        }
        HashMap<String, String> resultResponse = new HashMap<>();
        JSONObject tmp = new JSONObject();
        int listSize = list.size();
        try {
            if (listSize > 0) {
                tmp.put("lat", String.valueOf((lat / (double) listSize)));
                tmp.put("lon", String.valueOf(lon / (double) listSize));
                tmp.put("acc", (int) (range / (double) listSize));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resultResponse.put("response", tmp.toString());
        return resultResponse;
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
            super.setLatitude(jResponse.getDouble("lat"));
            super.setLongitude(jResponse.getDouble("lon"));
            float accuracy = Float.parseFloat(jResponse.getString("acc"));
            super.setAccuracy(Math.round(accuracy));
        } catch (JSONException e) {
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
        return true;
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
