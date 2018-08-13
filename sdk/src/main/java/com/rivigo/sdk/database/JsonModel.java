package com.rivigo.sdk.database;

/**
 * Created by Atul on 28-03-2017.
 */

public class JsonModel {
    int _id;
    long _lastTimeStamp;
    String jsonObject;

    public JsonModel(String _id, String _lastTimeStamp, String jsonObject) {
        this._id = Integer.valueOf(_id);
        this._lastTimeStamp = Long.valueOf(_lastTimeStamp);
        this.jsonObject = jsonObject;
    }

    public JsonModel() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getJsonObject() {
        return jsonObject;
    }

    public long get_lastTimeStamp() {
        return _lastTimeStamp;
    }

    public void set_lastTimeStamp(long _lastTimeStamp) {
        this._lastTimeStamp = _lastTimeStamp;
    }

    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public String toString() {
        return "JsonModel{" +
                "_id=" + _id +
                ", jsonObject='" + jsonObject + '\'' +
                '}';
    }
}
