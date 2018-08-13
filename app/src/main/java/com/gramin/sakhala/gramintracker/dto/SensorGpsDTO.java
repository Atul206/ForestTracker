package com.gramin.sakhala.gramintracker.dto;

import com.rivigo.sdk.data.AllSensorData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Atul on 18-03-2017.
 */

public class SensorGpsDTO implements Serializable {
    Long dateTime;
    Double latitude;
    Double longitude;
    Float speed;
    String sourceId;
    String dataClient;
    String batteryLevel;
    String networkCarrier;
    String payload;
    List<AllSensorData> sensorData;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDataClient() {
        return dataClient;
    }

    public void setDataClient(String dataClient) {
        this.dataClient = dataClient;
    }

    public List<AllSensorData> getSensorData() {
        return sensorData;
    }

    public void setSensorData(List<AllSensorData> sensorData) {
        this.sensorData = sensorData;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getNetworkCarrier() {
        return networkCarrier;
    }

    public void setNetworkCarrier(String networkCarrier) {
        this.networkCarrier = networkCarrier;
    }

    /* @Override
    public String toString() {
        return "{" +
                "\"dateTime\":" + dateTime + "," +
                "\"latitude\":" + latitude + "," +
                "\"longitude\":" + longitude + "," +
                "\"speed\":" + speed + "," +
                "\"sourceId\":" + "\"" + sourceId + "\"" + "," +
                "\"dataClient\":" + "\"" + dataClient + "\"" + "," +
                "\"payload\":" + payload + "," +
                "\"sensorData\":" + sensorData +
                '}';
    }*/
}
