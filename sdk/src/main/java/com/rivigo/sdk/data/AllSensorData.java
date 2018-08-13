package com.rivigo.sdk.data;

import java.io.Serializable;

/**
 * Created by priyaranjan on 26/7/16.
 */
public class AllSensorData implements Serializable {
    private RawAccelerometer rawAccelerometer;
    private RawGyrometer rawGyrometer;
    private long date;

    public RawAccelerometer getRawAccelerometer() {
        return rawAccelerometer;
    }

    public void setRawAccelerometer(RawAccelerometer rawAccelerometer) {
        this.rawAccelerometer = rawAccelerometer;
    }

    public RawGyrometer getRawGyrometer() {
        return rawGyrometer;
    }

    public void setRawGyrometer(RawGyrometer rawGyrometer) {
        this.rawGyrometer = rawGyrometer;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{" +
                "\"rawAccelerometer\":" + rawAccelerometer + ","+
                "\"rawGyrometer\":" + rawGyrometer  + "," +
                "\"date\":" + date +
                "}";
    }
}
