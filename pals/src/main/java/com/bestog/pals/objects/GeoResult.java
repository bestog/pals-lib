package com.bestog.pals.objects;

/**
 * Class: GeoResult
 *
 * @author bestog
 */
public class GeoResult {

    private Double latitude = null;
    private Double longitude = null;
    private int accuracy = 0;

    public GeoResult() {
    }

    public GeoResult(double lat, double lon, int acc) {
        latitude = lat;
        longitude = lon;
        accuracy = acc;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public Double getLongitude() {
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
