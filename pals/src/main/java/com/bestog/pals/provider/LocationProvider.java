package com.bestog.pals.provider;

import android.content.Context;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.bestog.pals.objects.Cell;
import com.bestog.pals.objects.Wifi;
import com.bestog.pals.utils.CellScanner;
import com.bestog.pals.objects.GeoResult;
import com.bestog.pals.utils.WifiScanner;

import java.util.HashMap;
import java.util.List;

/**
 * Class: Location Provider - abstract
 *
 * @author bestog
 */
public abstract class LocationProvider {

    /**
     * Location-Provider
     */
    public static final String PROVIDER_MOZILLA = "MozillaLocation";
    public static final String PROVIDER_OPENBMAP = "OpenBMapLocation";
    public static final String PROVIDER_OPENCELLID = "OpenCellIDLocation";
    public static final String PROVIDER_GOOGLE = "GoogleLocation";
    public static final String PROVIDER_OPENMAP = "OpenMapLocation";

    /**
     * Scanner-Classes
     */
    private CellScanner cellScanner;
    private WifiScanner wifiScanner;

    /**
     * Location-Provider properties
     */
    private String title = "";
    private String ownToken = null;
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private int accuracy = 0;

    /**
     * Constructor
     *
     * @param _title Location-Provider title
     * @param _token Own token for location-provider
     * @param ctx    ApplicationContext
     */
    LocationProvider(String _title, String _token, Context ctx) {
        title = _title;
        ownToken = _token;
        bootstrap(ctx);
    }

    /**
     * Constructor
     *
     * @param _title Location-Provider title
     * @param ctx    ApplicationContext
     */
    LocationProvider(String _title, Context ctx) {
        title = _title;
        bootstrap(ctx);
    }

    /**
     * Helper for bootstrap
     *
     * @param ctx Context
     */
    private void bootstrap(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        TelephonyManager cellManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        cellScanner = new CellScanner(cellManager);
        wifiScanner = new WifiScanner(wifiManager);
    }

    /**
     * Returns the result from request
     *
     * @return GeoResult
     */
    public GeoResult result() {
        return new GeoResult(latitude, longitude, accuracy);
    }

    /**
     * Request to location-service
     */
    public void request() {
        HashMap<String, String> response = requestAction();
        if (requestValidation(response)) {
            requestResult(response.get("response"));
        }
    }

    /**
     * Make a request to location-service
     *
     * @return HashMap Response from location-service
     */
    protected abstract HashMap<String, String> requestAction();

    /**
     * Validate the response from location-service
     *
     * @param response HashMap Response from location-service
     * @return boolean valid?
     */
    protected abstract boolean requestValidation(HashMap<String, String> response);

    /**
     * Save response in location-provider
     *
     * @param response String Response from location-service
     */
    protected abstract void requestResult(String response);

    /**
     * Submit to location-service
     *
     * @param position GPS-Location
     * @return boolean valid?
     */
    public boolean submit(Location position) {
        HashMap<String, String> response = submitAction(position);
        return (submitValidation(response));
    }

    /**
     * Make a submit to location-service
     *
     * @param position GPS-Location
     * @return HashMap Response from location-service
     */
    protected abstract HashMap<String, String> submitAction(Location position);

    /**
     * Validate the response from location-service
     *
     * @param response HashMap Response from location-service
     * @return boolean valid?
     */
    protected abstract boolean submitValidation(HashMap<String, String> response);

    /**
     * Set latitude
     *
     * @param latitude Coordinate in decimal degree format
     */
    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Set longitude
     *
     * @param longitude Coordinate in decimal degree format
     */
    void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Set accuracy
     *
     * @param accuracy Accuracy in meters
     */
    void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Get all near celltowers
     *
     * @return ArrayList
     */
    List<Cell> getCellTowers() {
        return cellScanner.getCells();
    }

    /**
     * Get all near wifi-spots
     *
     * @return ArrayList
     */
    List<Wifi> getWifiSpots() {
        return wifiScanner.getSpots();
    }

    /**
     * Get location-provider title
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get own location-service-token
     *
     * @return String
     */
    public String getOwnToken() {
        return ownToken;
    }
}
