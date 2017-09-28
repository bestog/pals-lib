package com.bestog.pals.provider;


import android.content.Context;

import com.bestog.pals.objects.Cell;
import com.bestog.pals.objects.GeoResult;
import com.bestog.pals.objects.ProviderResponse;
import com.bestog.pals.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
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
    private final String _submitUrl = "http://www.opencellid.org/measure/uploadJson";

    /**
     * Constructor
     *
     * @param ctx   Context
     * @param token String
     */
    public OpenCellIDLocation(Context ctx, String token) {
        super(LocationProvider.PROVIDER_OPENCELLID, ctx);
        _requestUrl = _apiUrl + (token != null ? token : _apiToken);
    }

    /**
     * request action
     *
     * @return ProviderResponse
     */
    @Override
    public ProviderResponse requestAction() {
        List<Cell> cellTowers = getCellTowers();
        double[] d = {0.0d, 0.0d, 0.0d};
        Collection<double[]> list = new ArrayList<>();
        for (Cell cellInfo : cellTowers) {
            String url = _requestUrl + "&mcc=" + cellInfo.mcc + "&mnc=" + cellInfo
                    .mnc + "&cellid=" + cellInfo.cid + "&lac=" + cellInfo
                    .lac + "&format=json";
            ProviderResponse response = CommonUtils.httpRequest(url, "", "GET", "application/json;charset=utf-8");
            boolean status = requestValidation(response);
            if (status) {
                try {
                    JSONObject out = new JSONObject(response.response);
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
        return new ProviderResponse("", "");
    }

    /**
     * request result
     *
     * @param response String
     */
    @Override
    public void requestResult(String response) {
        // @todo
    }

    /**
     * validate result
     *
     * @param response ProviderResponse
     * @return boolean
     */
    @Override
    protected boolean requestValidation(ProviderResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(response.response);
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
