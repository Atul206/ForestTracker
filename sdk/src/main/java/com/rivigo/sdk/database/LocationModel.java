package com.rivigo.sdk.database;

import java.text.DecimalFormat;

/**
 * Created by Atul on 27-03-2017.
 */

public class LocationModel {
    int _id;
    String _source_id;
    String _latitude;
    String _longitude;
    String _speed;
    String _dataClient;
    String _date_time;
    String _payload;


    private static DecimalFormat df2 = new DecimalFormat(".#######");

    public LocationModel(String _id, String _source_id, String _latitude, String _longitude, String _speed, String _dataClient, String _date_time, String payload) {
        this._id = Integer.valueOf(_id);
        this._source_id = _source_id;
        this._latitude = _latitude;
        this._longitude = _longitude;
        this._speed = _speed;
        this._dataClient = _dataClient;
        this._date_time = _date_time;
        this._payload = payload;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_source_id() {
        return _source_id;
    }

    public void set_source_id(String _source_id) {
        this._source_id = _source_id;
    }

    public String get_latitude() {
        return df2.format(Double.valueOf(_latitude));
    }

    public void set_latitude(String _latitude) {
        this._latitude = _latitude;
    }

    public String get_longitude() {
        return df2.format(Double.valueOf(_longitude));
    }

    public void set_longitude(String _longitude) {
        this._longitude = _longitude;
    }

    public String get_speed() {
        return _speed;
    }

    public void set_speed(String _speed) {
        this._speed = _speed;
    }

    public String get_dataClient() {
        return _dataClient;
    }

    public void set_dataClient(String _dataClient) {
        this._dataClient = _dataClient;
    }

    public String get_date_time() {
        return _date_time;
    }

    public void set_date_time(String _date_time) {
        this._date_time = _date_time;
    }

    public String get_payload() {
        return _payload;
    }

    public void set_payload(String _payload) {
        this._payload = _payload;
    }
}
