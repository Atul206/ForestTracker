package com.rivigo.sdk.data;


/**
 * Created by gauravk on 27/6/16.
 */
public class GPS extends DataPoint {
    public double latitude;
    public double longitude;
    public int altitude;
    public double rawSpeed = -1.0D;
    public int heading = -1;
    public int course = -1;
    public int horizontalAccuracy = -1;
    public int verticalAccuracy = -1;
    public double distance = 0d;
    double averageSpeed = 0D;

    public static double distanceBetween(double lat1, double long1, double lat2, double long2) {
        double latDiff = Math.toRadians(lat2 - lat1);
        double longDiff = Math.toRadians(long2 - long1);
        double distance = Math.sin(latDiff / 2.0D) * Math.sin(latDiff / 2.0D) + Math.sin(longDiff / 2.0D) * Math.sin(longDiff / 2.0D) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        return 2.0D * Math.atan2(Math.sqrt(distance), Math.sqrt(1.0D - distance)) * 6371000.0D;
    }

    public double distanceFrom(GPS previousGPSData) {
        if (previousGPSData == null)
            return 0d;

        double latDiff = Math.toRadians(this.latitude - previousGPSData.latitude);
        double longDiff = Math.toRadians(this.longitude - previousGPSData.longitude);
        double distance = Math.sin(latDiff / 2.0D) * Math.sin(latDiff / 2.0D) + Math.sin(longDiff / 2.0D) * Math.sin(longDiff / 2.0D) * Math.cos(Math.toRadians(previousGPSData.latitude)) * Math.cos(Math.toRadians(this.latitude));
        return 2.0D * Math.atan2(Math.sqrt(distance), Math.sqrt(1.0D - distance)) * 6371000.0D;

    }

    public double estimatedAverageSpeedFrom(GPS paramGPS) {
        if(paramGPS==null)
            return 0;
        double d1 = distanceFrom(paramGPS);
        double d2 = Math.abs(this.timestamp - paramGPS.timestamp) / 1000.0D;
        return d1 / d2;
    }

    public boolean equals(Object paramObject) {
        if ((paramObject instanceof GPS)) {
            GPS localGPS = (GPS) paramObject;
            return (super.equals(localGPS)) && (this.latitude == localGPS.latitude) && (this.longitude == localGPS.longitude) && (this.altitude == localGPS.altitude) && (this.rawSpeed == localGPS.rawSpeed) && (this.heading == localGPS.heading) && (this.course == localGPS.course) && (this.horizontalAccuracy == localGPS.horizontalAccuracy) && (this.verticalAccuracy == localGPS.verticalAccuracy);
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode() ^ Double.valueOf(this.latitude).hashCode() ^ Double.valueOf(this.longitude).hashCode() ^ Integer.valueOf(this.altitude).hashCode() ^ Double.valueOf(this.rawSpeed).hashCode() ^ Integer.valueOf(this.heading).hashCode() ^ Integer.valueOf(this.course).hashCode() ^ Integer.valueOf(this.horizontalAccuracy).hashCode() ^ Integer.valueOf(this.verticalAccuracy).hashCode();
    }

    public int uploadSizeBytes() {
        return 48;
    }

    public void enrichDistance(GPS previousGPSData) {
        this.distance = distanceFrom(previousGPSData);
        this.averageSpeed = estimatedAverageSpeedFrom(previousGPSData);

    }

    @Override
    public String toString() {
        return "GPS{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", rawSpeed=" + rawSpeed +
                ", heading=" + heading +
                ", course=" + course +
                ", horizontalAccuracy=" + horizontalAccuracy +
                ", verticalAccuracy=" + verticalAccuracy +
                '}';
    }
}
