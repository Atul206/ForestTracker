package com.rivigo.sdk.database;

/**
 * Created by Atul on 27-03-2017.
 */

public class SensorsModel {
    double _accelerationX;
    double _accelerationY;
    double _accelerationZ;
    double _acc_accuracy;
    long _id;
    long _location_id;
    long _date;
    double _xRotation;
    double _yRotation;
    double _zRotation;
    double _gyro_accuracy;

    public SensorsModel(String _id, String _location_id, String _date, String _xRotation, String _yRotation, String _zRotation, String _gyro_accuracy, String _accelerationX, String _accelerationY, String _accelerationZ, String _acc_accuracy) {
        this._id = Long.valueOf(_id);
        this._location_id = Long.valueOf(_location_id);
        this._date = Long.valueOf(_date);
        this._xRotation = Double.valueOf(_xRotation);
        this._yRotation = Double.valueOf(_yRotation);
        this._zRotation = Double.valueOf(_zRotation);
        this._gyro_accuracy = Double.valueOf(_gyro_accuracy);
        this._accelerationX = Double.valueOf(_accelerationX);
        this._accelerationY = Double.valueOf(_accelerationY);
        this._accelerationZ = Double.valueOf(_accelerationZ);
        this._acc_accuracy = Double.valueOf(_acc_accuracy);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_location_id() {
        return _location_id;
    }

    public void set_location_id(long _location_id) {
        this._location_id = _location_id;
    }

    public long get_date() {
        return _date;
    }

    public void set_date(long _date) {
        this._date = _date;
    }

    public double get_xRotation() {
        return _xRotation;
    }

    public void set_xRotation(double _xRotation) {
        this._xRotation = _xRotation;
    }

    public double get_yRotation() {
        return _yRotation;
    }

    public void set_yRotation(double _yRotation) {
        this._yRotation = _yRotation;
    }

    public double get_zRotation() {
        return _zRotation;
    }

    public void set_zRotation(double _zRotation) {
        this._zRotation = _zRotation;
    }

    public double get_gyro_accuracy() {
        return _gyro_accuracy;
    }

    public void set_gyro_accuracy(int _gyro_accuracy) {
        this._gyro_accuracy = _gyro_accuracy;
    }

    public double get_accelerationX() {
        return _accelerationX;
    }

    public void set_accelerationX(double _accelerationX) {
        this._accelerationX = _accelerationX;
    }

    public double get_accelerationY() {
        return _accelerationY;
    }

    public void set_accelerationY(double _accelerationY) {
        this._accelerationY = _accelerationY;
    }

    public double get_accelerationZ() {
        return _accelerationZ;
    }

    public void set_accelerationZ(double _accelerationZ) {
        this._accelerationZ = _accelerationZ;
    }

    public double get_acc_accuracy() {
        return _acc_accuracy;
    }

    public void set_acc_accuracy(int _acc_accuracy) {
        this._acc_accuracy = _acc_accuracy;
    }
}
