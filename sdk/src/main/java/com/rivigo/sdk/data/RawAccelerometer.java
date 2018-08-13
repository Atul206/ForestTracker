package com.rivigo.sdk.data;

import java.io.Serializable;

/**
 * Created by gauravk on 27/6/16.
 */
public class RawAccelerometer implements Serializable {
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public double accuracy;

    public RawAccelerometer(double accelerationX, double accelerationY, double accelerationZ, double accuracy) {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
        this.accelerationZ = accelerationZ;
        this.accuracy = accuracy;
    }

    public RawAccelerometer() {
    }

    public double getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(double accelerationX) {
        this.accelerationX = accelerationX;
    }

    public double getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(double accelerationY) {
        this.accelerationY = accelerationY;
    }

    public double getAccelerationZ() {
        return accelerationZ;
    }

    public void setAccelerationZ(double accelerationZ) {
        this.accelerationZ = accelerationZ;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    /*public RawAccelerometer() {
    }*/

   /* public RawAccelerometer(RawAccelerometer paramRawAccelerometer) {
        super(paramRawAccelerometer);
        this.accelerationX = paramRawAccelerometer.accelerationX;
        this.accelerationY = paramRawAccelerometer.accelerationY;
        this.accelerationZ = paramRawAccelerometer.accelerationZ;
    }*/

    /*public boolean equals(Object paramObject) {
        if ((paramObject instanceof RawAccelerometer)) {
            RawAccelerometer localRawAccelerometer = (RawAccelerometer) paramObject;
            return (super.equals(localRawAccelerometer)) && (this.accelerationX == localRawAccelerometer.accelerationX) && (this.accelerationY == localRawAccelerometer.accelerationY) && (this.accelerationZ == localRawAccelerometer.accelerationZ);
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode() ^ Double.valueOf(this.accelerationX).hashCode() ^ Double.valueOf(this.accelerationY).hashCode() ^ Double.valueOf(this.accelerationZ).hashCode();
    }


    public double accelerationXYZMagnitude() {
        return Math.sqrt(Math.pow(this.accelerationX, 2.0D) + Math.pow(this.accelerationY, 2.0D) + Math.pow(this.accelerationZ, 2.0D));
    }*/

    @Override
    public String toString() {
        return "{" +
                "\"accelerationX\":" + "\"" + accelerationX + "\"," +
                "\"accelerationY\":" + "\"" + accelerationY + "\"," +
                "\"accelerationZ\":" + "\"" + accelerationZ + "\"," +
                "\"accuracy\":" + accuracy +
                "}";
    }
}
