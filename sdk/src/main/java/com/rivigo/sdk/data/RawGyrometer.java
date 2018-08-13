package com.rivigo.sdk.data;

import java.io.Serializable;

/**
 * Created by priyaranjan on 26/7/16.
 */

public class RawGyrometer implements Serializable {

    private double xRotation;
    private double yRotation;
    private double zRotation;
    private double accuracy;

    public RawGyrometer() {
    }

    public RawGyrometer(double xRotation, double yRotation, double zRotation, double accuracy) {
        this.xRotation = xRotation;
        this.yRotation = yRotation;
        this.zRotation = zRotation;
        this.accuracy = accuracy;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }


    public double getxRotation() {
        return xRotation;
    }

    public void setxRotation(double xRotation) {
        this.xRotation = xRotation;
    }

    public double getyRotation() {
        return yRotation;
    }

    public void setyRotation(double yRotation) {
        this.yRotation = yRotation;
    }

    public double getzRotation() {
        return zRotation;
    }

    public void setzRotation(double zRotation) {
        this.zRotation = zRotation;
    }

    @Override
    public String toString() {
        return  "{ " +
                "\"xRotation\":" + "\"" +  xRotation + "\"," +
                "\"yRotation\":" + "\"" +  yRotation + "\"," +
                "\"zRotation\":" + "\"" + zRotation + "\"," +
                "\"accuracy\":" + accuracy +
                "}";
    }
}
