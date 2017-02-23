package com.bestog.pals.utils;

import com.bestog.pals.objects.GeoResult;

import java.util.List;

/**
 * Trilateration
 */
public final class Trilateration {

    public static final String ALG_SIMPLE = "simple";
    public static final String ALG_WEIGHTS = "weights";

    /**
     * All coordinates are added together and it creates the final result (rudimentary)
     * http://www.geomidpoint.com/calculation.html = Point A
     *
     * @param resultList locations
     * @return GeoResult
     */
    public static GeoResult simple(Iterable<GeoResult> resultList) {
        GeoResult geoResult = new GeoResult();
        Double x = 0.0d, y = 0.0d, z = 0.0d;
        int acc = 0;
        int validPositions = 0;
        for (GeoResult item : resultList) {
            if (item.getLatitude() != null && item.getLongitude() != null) {
                Double latitude = item.getLatitude() * Math.PI / 180.0;
                Double longitude = item.getLongitude() * Math.PI / 180.0;
                x += StrictMath.cos(latitude) * StrictMath.cos(longitude);
                y += StrictMath.cos(latitude) * StrictMath.sin(longitude);
                z += StrictMath.sin(latitude);
                acc += item.getAccuracy();
                ++validPositions;
            }
        }
        if (validPositions > 0) {
            x /= (double) validPositions;
            y /= (double) validPositions;
            z /= (double) validPositions;
            Double lon = StrictMath.atan2(y, x);
            Double lat = StrictMath.atan2(z, Math.sqrt(x * x + y * y));
            geoResult.setLatitude(lat * 180.0 / Math.PI);
            geoResult.setLongitude(lon * 180.0 / Math.PI);
            geoResult.setAccuracy(acc / validPositions);
        }
        return geoResult;
    }

    public static GeoResult weights(List<GeoResult> geoResultList) {
        // @todo
        return new GeoResult();
    }


}
