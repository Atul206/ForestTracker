package com.rivigo.sdk.data;

/**
 * Created by gauravk on 27/6/16.
 */
public class Geofence extends DataPoint {
    public double centerLatitude;
    public double centerLongitude;
    public double radius;
    public transient String id;




    @Override
    public String toString() {
        return "Geofence{" +
                "centerLatitude=" + centerLatitude +
                ", centerLongitude=" + centerLongitude +
                ", radius=" + radius +
                ", id='" + id + '\'' +
                '}';
    }
}
