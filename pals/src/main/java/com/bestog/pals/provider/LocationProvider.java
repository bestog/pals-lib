package com.bestog.pals.provider;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.bestog.pals.objects.Cell;
import com.bestog.pals.objects.GeoResult;
import com.bestog.pals.objects.ProviderResponse;
import com.bestog.pals.objects.Wifi;
import com.bestog.pals.utils.CellScanner;
import com.bestog.pals.utils.WifiScanner;

import java.util.List;

/**
 * Class: Location Provider - abstract
 *
 * @author bestog
 */
public abstract class LocationProvider {

    public static final String PROVIDER_MOZILLA = "MozillaLocation";
    public static final String PROVIDER_OPENBMAP = "OpenBMapLocation";
    public static final String PROVIDER_OPENCELLID = "OpenCellIDLocation";
    public static final String PROVIDER_GOOGLE = "GoogleLocation";
    public static final String PROVIDER_OPENMAP = "OpenMapLocation";
    static final int UNKNOWN_CELLID = -1;
    static final String NULL = "0.0d";
    private final String title;
    private final CellScanner cellScanner;
    private final WifiScanner wifiScanner;
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private int accuracy = 0;

    LocationProvider(String _title, Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        TelephonyManager cellManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        cellScanner = new CellScanner(cellManager);
        wifiScanner = new WifiScanner(wifiManager);
        title = _title;
    }

    public GeoResult result() {
        return new GeoResult(getLatitude(), getLongitude(), getAccuracy());
    }

    public void request() {
        ProviderResponse response = requestAction();
        if (requestValidation(response)) {
            requestResult(response.response);
        }
    }

    public boolean submit(GeoResult position) {
        ProviderResponse response = submitAction(position);
        return submitValidation(response) && submitResult(response.response);
    }

    protected abstract ProviderResponse requestAction();

    protected abstract boolean requestValidation(ProviderResponse response);

    protected abstract void requestResult(String response);

    protected abstract ProviderResponse submitAction(GeoResult position);

    protected abstract boolean submitValidation(ProviderResponse response);

    protected abstract boolean submitResult(String response);

    private double getLatitude() {
        return latitude;
    }

    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double getLongitude() {
        return longitude;
    }

    void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private int getAccuracy() {
        return accuracy;
    }

    void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    List<Cell> getCellTowers() {
        return cellScanner.getCells();
    }

    List<Wifi> getWifiSpots() {
        return wifiScanner.getSpots();
    }

    public String getTitle() {
        return title;
    }
}
