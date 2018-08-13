package com.rivigo.sdk.database;

/**
 * Created by atulsakhala on 19/02/18.
 */

 public interface DatabaseConstant {
    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "DataManager";

    // Location table name
    public static final String TABLE_LOCATION = "location";

    public static final String TABLE_SENSORS = "sensor";

    public static final String TABLE_JSON = "json_table";

    public static final String TABLE_POLICY = "policy_table";

    //RESPONSE Table Column names
    public static final String KEY_BODY = "body";
    public static final String KEY_JSON_ID = "_id";
    public static final String KEY_JSON_TIME = "_time";

    // Location Table Columns names
    public static final String KEY_LOCATION_ID = "_id";
    public static final String KEY_SOURCE_ID = "_source_id";
    public static final String KEY_LATITUDE = "_latitude";
    public static final String KEY_LONGITUDE = "_longitude";
    public static final String KEY_SPEED = "_speed";
    public static final String KEY_PAYLOAD = "_payload";
    public static final String KEY_DATA_CLIENT = "_dataClient";
    public static final String KEY_LOCATION_TIMESTAMP = "_date_time";


    // Sensor Table Columns names
    public static final String KEY_SENSOR_ID = "_id";
    public static final String KEY_FOREIGN_ID = "_location_id";
    public static final String KEY_SENSOR_TIME = "_time";

    public static final String KEY_X_ROTATION = "_xRotation";
    public static final String KEY_Y_ROTATION = "_yRotation";
    public static final String KEY_Z_ROTATION = "_zRotation";
    public static final String KEY_GYRO_ACCURACY = "_gyro_accuracy";

    public static final String KEY_X_ACC = "_accelerationX";
    public static final String KEY_Y_ACC = "_accelerationY";
    public static final String KEY_Z_ACC = "_accelerationZ";
    public static final String KEY_ACC_ACCURACY = "_acc_accuracy";


    //Policy table content
    public static final String KEY_POLICY_ID = "_id";
    public static final String KEY_POLICY_TITLE = "_title";
    public static final String KEY_POLICY_CONTENT = "_content";
    public static final String KEY_NOTICE_TYPE = "_notice_type";
    public static final String KEY_SOURCE = "_source";
    public static final String KEY_CREATED_TIMESTAMP = "_created_timestamp";
    public static final String KEY_LAST_UPDATED_TIMESTAMP = "_last_updated_timestamp";
    public static final String KEY_POLICY_LANGUAGE = "_language";

}
