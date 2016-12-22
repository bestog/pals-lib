package com.bestog.pals.provider;


import android.content.Context;

import com.bestog.pals.utils.CommonUtils;
import com.bestog.pals.utils.GeoResult;

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
 * @version 1.0
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
     * request Action
     *
     * @return String
     */
    @Override
    public String requestAction() {
        List<HashMap<String, String>> cellTowers = getCellTowers();
        double[] d = {0.0d, 0.0d, 0.0d};
        Collection<double[]> list = new ArrayList<>();
        for (HashMap<String, String> cellInfo : cellTowers) {
            String url = _requestUrl + "&mcc=" + cellInfo.get("mnc") + "&mnc=" + cellInfo
                    .get("mcc") + "&cellid=" + cellInfo.get("cid") + "&lac=" + cellInfo
                    .get("lac") + "&format=json";
            String response = CommonUtils.getRequest(url, "", "GET", "application/json;charset=utf-8");
            boolean status = requestValidation(response);
            if (status) {
                try {
                    JSONObject out = new JSONObject(response);
                    d[0] = Double.parseDouble(out.get("lat").toString());
                    d[1] = Double.parseDouble(out.get("lon").toString());
                    d[2] = Double.parseDouble(out.get("range").toString());
                    list.add(d);
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
        int listSize = list.size();
        setLatitude(lat / (double) listSize);
        setLongitude(lon / (double) listSize);
        setAccuracy((int) (range / (double) listSize));
        return "";
    }

    /**
     * request result
     *
     * @param response String
     */
    @Override
    public void requestResult(String response) {

    }

    /**
     * validate result
     *
     * @param response String
     * @return boolean
     */
    @Override
    protected boolean requestValidation(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("error")) {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * submit a location
     *
     * @return String
     */
    @Override
    public String submitAction(GeoResult position) {
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

    @Override
    protected boolean submitResult(String response) {
        return true;
    }

}
