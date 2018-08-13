package com.rivigo.sdk.data;

/**
 * Created by priyaranjan on 26/7/16.
 */
public class GyroData {
    private String tripCode;
    private RawGyrometer rawGyrometer;


    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public RawGyrometer getRawGyrometer() {
        return rawGyrometer;
    }

    public void setRawGyrometer(RawGyrometer rawGyrometer) {
        this.rawGyrometer = rawGyrometer;
    }

}
