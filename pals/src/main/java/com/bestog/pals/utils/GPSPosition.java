package com.bestog.pals.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Calendar;

<<<<<<< Updated upstream
/**
 * Class: GPSPosition
 *
 * @author bestog
 */
=======
>>>>>>> Stashed changes
public class GPSPosition {
    /**
     * Variables
     */
    private final Context ctx;
    private Listener listener;

    /**
     * constructor
     *
     * @param context Activity
     */
    public GPSPosition(Context context) {
        ctx = context;
    }

    /**
     * Get coordinates
     *
     * @param listener Listener
     */
    public final void getPosition(Listener listener) {
        this.listener = listener;
        this.getFromGps();
    }

    /**
     * Get coordinates from GPS system
     */
    private void getFromGps() {
        final LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
            } catch (SecurityException e) {
                //@todo: better logging
<<<<<<< Updated upstream
                complete(null, false);
=======
                complete(new GeoResult(0.0d, 0.0d, 0), false);
>>>>>>> Stashed changes
                e.printStackTrace();
            }
            if (location != null
                    && location.getTime() > Calendar.getInstance().getTimeInMillis() - (long) (2 * 60
                    * 1000)) {
                double lat = CommonUtils.round(location.getLatitude(), 6);
                double lon = CommonUtils.round(location.getLongitude(), 6);
<<<<<<< Updated upstream
                complete(location, lat != 0 && lon != 0);
=======
                complete(new GeoResult(0.0d, 0.0d, 0), lat != 0 && lon != 0);
>>>>>>> Stashed changes
            } else {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0F,
                            new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    if (location != null) {
                                        getFromGps();
                                        try {
                                            locationManager.removeUpdates(this);
                                        } catch (SecurityException e) {
                                            e.printStackTrace();
                                            // @todo better logging
                                        }
                                    }
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {
                                }

                                @Override
                                public void onProviderEnabled(String provider) {
                                }

                                @Override
                                public void onProviderDisabled(String provider) {
                                }
                            });
                } catch (SecurityException e) {
<<<<<<< Updated upstream
                    complete(location, false);
=======
                    complete(new GeoResult(0.0d, 0.0d, 0), false);
>>>>>>> Stashed changes
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * if complete
     *
<<<<<<< Updated upstream
     * @param geoResult Location
     * @param available valid coordinates?
     */
    private void complete(Location geoResult, boolean available) {
=======
     * @param geoResult Coordinates
     * @param available valid coordinates?
     */
    private void complete(GeoResult geoResult, boolean available) {
>>>>>>> Stashed changes
        if (this.listener != null) {
            this.listener.onComplete(geoResult, available);
        }
    }

    /**
     * Interface Listener
     */
    public interface Listener {
<<<<<<< Updated upstream
        void onComplete(Location geoResult, boolean valid);
=======
        void onComplete(GeoResult geoResult, boolean valid);
>>>>>>> Stashed changes
    }
}

