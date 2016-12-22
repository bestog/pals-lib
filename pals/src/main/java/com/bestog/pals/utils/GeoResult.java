package com.bestog.pals.utils;

/**
 * Class: GeoResult
 *
 * @author bestog
 * @version 1.0
 */
public class GeoResult {

    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private int accuracy = 0;

    public GeoResult() {
    }

    public GeoResult(Double lat, Double lon, int acc) {
        latitude = lat;
        longitude = lon;
        accuracy = acc;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double lon) {
        this.longitude = lon;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int acc) {
        this.accuracy = acc;
    }

    @Override
    public String toString() {
        return "GeoResult{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", accuracy=" + accuracy +
                '}';
    }
}
