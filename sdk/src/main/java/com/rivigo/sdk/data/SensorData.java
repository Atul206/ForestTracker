package com.rivigo.sdk.data;

/**
 * Created by gauravk on 30/6/16.
 */
public class SensorData {
    private String tripCode;

    private RawAccelerometer rawAccelerometer;

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public RawAccelerometer getRawAccelerometer() {
        return rawAccelerometer;
    }

    public void setRawAccelerometer(RawAccelerometer rawAccelerometer) {
        this.rawAccelerometer = rawAccelerometer;
    }
}
