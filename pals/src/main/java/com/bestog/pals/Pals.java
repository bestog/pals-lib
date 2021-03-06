package com.bestog.pals;

import android.content.Context;
import android.os.AsyncTask;

import com.bestog.pals.interfaces.IRequest;
import com.bestog.pals.interfaces.ISubmit;
import com.bestog.pals.objects.GeoResult;
import com.bestog.pals.provider.GoogleLocation;
import com.bestog.pals.provider.LocationProvider;
import com.bestog.pals.provider.MozillaLocation;
import com.bestog.pals.provider.OpenBMapLocation;
import com.bestog.pals.provider.OpenCellIDLocation;
import com.bestog.pals.provider.OpenMapLocation;
import com.bestog.pals.utils.GPSPosition;
import com.bestog.pals.utils.Trilateration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Privacy Aware Location Service
 * For more informations: http://bestog.github.io/pals-lib
 *
 * @author bestog
 */
public class Pals {
    private final Context context;
    private final Map<String, LocationProvider> enabledProviders = new HashMap<>();
    private String trilaterateAlg = Trilateration.ALG_SIMPLE;
    // private final Map<String, AsyncTask> taskPool = new HashMap<>();
    // Request
    private final List<GeoResult> resultList = new ArrayList<>();
    private int requestPoolCount = 0;
    // Submit
    // private final List<Boolean> submitList = new ArrayList<>();
    private int submitPoolCount = 0;
    // Listener
    private IRequest requestListener;
    private ISubmit submitListener;

    /**
     * Create Pals-Lib instance
     *
     * @param ctx Context
     */
    public Pals(Context ctx) {
        context = ctx;
    }

    /**
     * Enable provider (own token)
     *
     * @param provider String Provider-Name
     * @param token    String Access-Token
     */
    public void enableProvider(String provider, String token) {
        try {
            Class lp = Class.forName("com.bestog.pals.provider." + provider);
            LocationProvider locationProvider = null;
            if (enabledProviders.containsKey(provider)) {
                return;
            }
            switch (provider) {
                case LocationProvider.PROVIDER_MOZILLA:
                    locationProvider = new MozillaLocation(context, token);
                    break;
                case LocationProvider.PROVIDER_GOOGLE:
                    locationProvider = new GoogleLocation(context, token);
                    break;
                case LocationProvider.PROVIDER_OPENCELLID:
                    locationProvider = new OpenCellIDLocation(context, token);
                    break;
                case LocationProvider.PROVIDER_OPENMAP:
                    locationProvider = new OpenMapLocation(context);
                    break;
                case LocationProvider.PROVIDER_OPENBMAP:
                    locationProvider = new OpenBMapLocation(context);
                    break;
            }
            if (locationProvider != null && lp != null) {
                enabledProviders.put(provider, (LocationProvider) lp.cast(locationProvider));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enable provider (fallback-token)
     *
     * @param provider String Provider-Name
     */
    public void enableProvider(String provider) {
        enableProvider(provider, null);
    }

    /**
     * Disable provider
     *
     * @param provider Provider
     */
    public void disableProvider(String provider) {
        if (enabledProviders.containsKey(provider)) {
            enabledProviders.remove(provider);
        }
    }

    /**
     * Get enabled providers as string-list
     *
     * @return String[]
     */
    public String[] getEnabledProviderList() {
        return enabledProviders.keySet().toArray(new String[enabledProviders.size()]);
    }

    /**
     * Get enabled providers as map
     *
     * @return HashMap
     */
    public Map<String, LocationProvider> getEnabledProviders() {
        return enabledProviders;
    }

    /**
     * Is provider enabled?
     *
     * @param provider Provider
     * @return boolean
     */
    public boolean isProviderEnabled(String provider) {
        return enabledProviders.containsKey(provider);
    }

    /**
     * Run the requestPool for Geolocation
     *
     * @param listener RequestListener
     */
    public void request(IRequest listener) {
        this.requestListener = listener;
        for (LocationProvider lp : enabledProviders.values()) {
            AsyncTask asyncTask = new RequestATask().execute(lp);
            // taskPool.put(lp.getTitle(), asyncTask);
            requestPoolCount++;
        }
    }

    /**
     * Run the submitPool for Submitting
     *
     * @param listener SubmitListener
     */
    public void submit(ISubmit listener) {
        GPSPosition gpsPosition = new GPSPosition(context);
        this.submitListener = listener;
        gpsPosition.getPosition(new GPSPosition.Listener() {
            @Override
            public void onComplete(GeoResult geoResult, boolean valid) {
                for (LocationProvider lp : enabledProviders.values()) {
                    AsyncTask asyncTask = new SubmitATask(geoResult).execute(lp);
                    // taskPool.put(lp.getTitle(), asyncTask);
                    submitPoolCount++;
                }
            }
        });
    }

    /**
     * If requestAction complete?
     *
     * @param geoResult Coordinates and Accuracy
     * @param available valid geoResult?
     */
    private void requestComplete(GeoResult geoResult, boolean available) {
        --requestPoolCount;
        if (available) {
            GeoResult formatResult = new GeoResult();
            formatResult.setLatitude(geoResult.getLatitude());
            formatResult.setLongitude(geoResult.getLongitude());
            formatResult.setAccuracy(geoResult.getAccuracy());
            resultList.add(formatResult);
        }
        if (requestListener != null && requestPoolCount == 0) {
            GeoResult endResult = trilaterateResults(resultList);
            requestListener.onComplete(endResult, true);
        }
    }

    private void submitComplete(boolean success, boolean valid) {
        --submitPoolCount;
        if (success) {
            // submitList.add(valid);
        }
        if (submitListener != null && submitPoolCount == 0) {
            submitListener.onComplete(valid);
        }
    }

    /**
     * Set a trilaterate Algorithm
     *
     * @param alg string
     */
    public void setTrilaterateAlg(String alg) {
        trilaterateAlg = alg;
    }

    /**
     * Calculate all Points to an endresult
     *
     * @param geoResultList List
     * @return GeoResult
     */
    private GeoResult trilaterateResults(List<GeoResult> geoResultList) {
        GeoResult out = new GeoResult();
        switch (trilaterateAlg) {
            case Trilateration.ALG_SIMPLE:
                out = Trilateration.simple(geoResultList);
                break;
            case Trilateration.ALG_WEIGHTS:
                out = Trilateration.weights(geoResultList);
                break;
        }
        return out;
    }

    /**
     * Request AsyncTask - Run Time
     */
    private class RequestATask extends AsyncTask<LocationProvider, String, GeoResult> {
        @Override
        protected GeoResult doInBackground(LocationProvider... lp) {
            lp[0].request();
            return lp[0].result();
        }

        @Override
        protected void onPostExecute(GeoResult geoResult) {
            requestComplete(geoResult, (geoResult.getLatitude() != null && geoResult.getLongitude() != null));
        }
    }

    /**
     * Submit AsyncTask - Run Time
     */
    private class SubmitATask extends AsyncTask<LocationProvider, String, Boolean> {

        private final GeoResult gpsPosition;

        SubmitATask(GeoResult position) {
            gpsPosition = position;
        }

        @Override
        protected Boolean doInBackground(LocationProvider... lp) {
            return lp[0].submit(gpsPosition);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            submitComplete(result, true);
        }
    }
}


