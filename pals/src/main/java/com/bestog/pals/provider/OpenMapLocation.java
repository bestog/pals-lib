package com.bestog.pals.provider;

import android.content.Context;

import com.bestog.pals.objects.GeoResult;
import com.bestog.pals.objects.ProviderResponse;
import com.bestog.pals.objects.Wifi;
import com.bestog.pals.utils.CommonUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Class: Open Wlan Map
 * Link: https://openwifi.su
 *
 * @author bestog
 */
public class OpenMapLocation extends LocationProvider {

    private final String _apiUrl = "http://openwlanmap.org/getpos.php";
    private final String _requestUrl;

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public OpenMapLocation(Context ctx) {
        super(LocationProvider.PROVIDER_OPENMAP, ctx);
        _requestUrl = _apiUrl;
    }

    /**
     * request action
     *
     * @return ProviderResponse
     */
    @Override
    public ProviderResponse requestAction() {
        String request = "";
        List<Wifi> wifiSpots = getWifiSpots();
        for (Wifi wifi : wifiSpots) {
            request += wifi.mac + "\r\n";
        }
        return CommonUtils.httpRequest(_requestUrl, request, "POST", "application/x-www-form-urlencoded, *.*");
    }

    /**
     * request result
     *
     * @param response String
     */
    @Override
    public void requestResult(String response) {
        if (!response.isEmpty()) {
            Properties properties = new Properties();
            try {
                properties.load(new ByteArrayInputStream(response.getBytes("UTF-8")));
                if (!properties.isEmpty()) {
                    setLatitude(Double.parseDouble(properties.getProperty("lat", LocationProvider.NULL)));
                    setLongitude(Double.parseDouble(properties.getProperty("lon", LocationProvider.NULL)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    /**
     * @param response String
     * @return boolean
     */
    @Override
    protected boolean submitResult(String response) {
        // @todo
        return true;
    }

}