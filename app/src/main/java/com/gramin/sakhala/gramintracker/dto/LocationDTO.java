package com.gramin.sakhala.gramintracker.dto;


import java.io.Serializable;

/**
 * Created by ravi.kumar on 12/8/2016.
 */

public class LocationDTO implements Serializable {

    private Long id;
    private String name;
    private String code;
    private Double latitude;
    private Double longitude;
    private String contactNo;
    private String address;
    private Long inTime;
    private Long outTime;
    private Long vehicleNodeTrackingId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getInTime() {
        return inTime;
    }

    public void setInTime(Long inTime) {
        this.inTime = inTime;
    }

    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }

    public Long getVehicleNodeTrackingId() {
        return vehicleNodeTrackingId;
    }

    public void setVehicleNodeTrackingId(Long vehicleNodeTrackingId) {
        this.vehicleNodeTrackingId = vehicleNodeTrackingId;
    }
}
