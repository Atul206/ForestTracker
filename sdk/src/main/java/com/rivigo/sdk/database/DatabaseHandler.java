package com.rivigo.sdk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rivigo.sdk.data.AllSensorData;
import com.rivigo.sdk.data.RawAccelerometer;
import com.rivigo.sdk.data.RawGyrometer;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 27-03-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper implements CrudLocation, CrudSensors, CrudJson, CrudPolicy, DatabaseConstant {

    private SQLiteDatabase sqLiteDatabaseTable;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabaseTable = sqLiteDatabase;
        String CREATE_BODY_TABLE = "CREATE TABLE " + TABLE_JSON +
                "("
                + KEY_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                "," + KEY_JSON_TIME + " TEXT" +
                "," + KEY_BODY + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_BODY_TABLE);

        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION +
                "("
                + KEY_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                "," + KEY_SOURCE_ID + " TEXT" +
                "," + KEY_LATITUDE + " TEXT" +
                "," + KEY_LONGITUDE + " TEXT" +
                "," + KEY_SPEED + " TEXT" +
                "," + KEY_DATA_CLIENT + " TEXT" +
                "," + KEY_LOCATION_TIMESTAMP + " TEXT" +
                "," + KEY_PAYLOAD + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_LOCATION_TABLE);


        String CREATE_SENSOR_TABLE = "CREATE TABLE " + TABLE_SENSORS +
                "("
                + KEY_SENSOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                "," + KEY_SOURCE_ID + " TEXT" +
                "," + KEY_FOREIGN_ID + " INTEGER" +
                "," + KEY_SENSOR_TIME + " TEXT" +
                "," + KEY_X_ROTATION + " TEXT" +
                "," + KEY_Y_ROTATION + " TEXT" +
                "," + KEY_Z_ROTATION + " TEXT" +
                "," + KEY_GYRO_ACCURACY + " TEXT" +
                "," + KEY_X_ACC + " TEXT" +
                "," + KEY_Y_ACC + " TEXT" +
                "," + KEY_Z_ACC + " TEXT" +
                "," + KEY_ACC_ACCURACY + " TEXT" +
                "," + "FOREIGN KEY (_location_id) REFERENCES " + TABLE_LOCATION + "(_id)" + ")";
        sqLiteDatabase.execSQL(CREATE_SENSOR_TABLE);

        String CREATE_POLICY_TABLE = "CREATE TABLE " + TABLE_POLICY +
                "("
                + KEY_POLICY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                "," + KEY_POLICY_TITLE + " TEXT" +
                "," + KEY_POLICY_CONTENT + " TEXT" +
                "," + KEY_NOTICE_TYPE + " TEXT" +
                "," + KEY_SOURCE + " TEXT" +
                "," + KEY_CREATED_TIMESTAMP + " TEXT" +
                "," + KEY_LAST_UPDATED_TIMESTAMP + " TEXT" +
                "," + KEY_POLICY_LANGUAGE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_POLICY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_JSON);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSORS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_POLICY);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void addLocation(LocationModel locationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SOURCE_ID, String.valueOf(locationModel.get_source_id()));
        values.put(KEY_DATA_CLIENT, String.valueOf(locationModel.get_dataClient()));
        values.put(KEY_SPEED, String.valueOf(locationModel.get_speed()));
        values.put(KEY_LATITUDE, String.valueOf(locationModel.get_latitude()));
        values.put(KEY_LONGITUDE, String.valueOf(locationModel.get_longitude()));
        values.put(KEY_LOCATION_TIMESTAMP, String.valueOf(locationModel.get_date_time()));
        values.put(KEY_PAYLOAD, String.valueOf(locationModel.get_payload()));

        db.insert(TABLE_LOCATION, null, values);
    }

    @Override
    public LocationModel getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        LocationModel locationModel = null;
        Cursor cursor = db.query(TABLE_LOCATION, new String[]{KEY_LOCATION_ID, KEY_SOURCE_ID, KEY_LATITUDE, KEY_LONGITUDE, KEY_SPEED, KEY_DATA_CLIENT, KEY_LOCATION_TIMESTAMP, KEY_PAYLOAD}, KEY_LOCATION_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                locationModel = new LocationModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }


        return locationModel;
    }

    @Override
    public List<LocationModel> getAllLocation() {
        List<LocationModel> locationModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATION + " order by " + KEY_LOCATION_TIMESTAMP + " asc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    LocationModel locationModel = new LocationModel(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_SOURCE_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)),
                            cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)),
                            cursor.getString(cursor.getColumnIndex(KEY_SPEED)),
                            cursor.getString(cursor.getColumnIndex(KEY_DATA_CLIENT)),
                            cursor.getString(cursor.getColumnIndex(KEY_LOCATION_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex(KEY_PAYLOAD))
                    );
                    locationModelList.add(locationModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }

        return locationModelList;
    }

    @Override
    public LocationModel lastInsertLocation() {
        LocationModel locationModel = null;
        String selectQuery = "SELECT * FROM " + TABLE_LOCATION + " order by " + KEY_LOCATION_ID + " desc LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    locationModel = new LocationModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        return locationModel;
    }

    @Override
    public int getLocationCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        try {

        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        return cursor.getCount();
    }


    @Override
    public int updateLocation(LocationModel locationModel) {
        return 0;
    }

    @Override
    public void deleteLocation(LocationModel locationModel) {

        String selectQuery = "DELETE FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        /*SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATION, KEY_LOCATION_ID + " = ?",
                new String[]{String.valueOf(locationModel.get_id())});*/
    }


    @Override
    public void addSensor(SensorsModel sensorsModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOREIGN_ID, Long.valueOf(sensorsModel.get_location_id()));
        values.put(KEY_SENSOR_TIME, String.valueOf(sensorsModel.get_date()));

        values.put(KEY_X_ROTATION, String.valueOf(sensorsModel.get_xRotation()));
        values.put(KEY_Y_ROTATION, String.valueOf(sensorsModel.get_yRotation()));
        values.put(KEY_Z_ROTATION, String.valueOf(sensorsModel.get_zRotation()));
        values.put(KEY_GYRO_ACCURACY, String.valueOf(sensorsModel.get_gyro_accuracy()));

        values.put(KEY_X_ACC, String.valueOf(sensorsModel.get_accelerationX()));
        values.put(KEY_Y_ACC, String.valueOf(sensorsModel.get_accelerationY()));
        values.put(KEY_Z_ACC, String.valueOf(sensorsModel.get_accelerationZ()));
        values.put(KEY_ACC_ACCURACY, String.valueOf(sensorsModel.get_acc_accuracy()));

        db.insert(TABLE_SENSORS, null, values);
    }

    @Override
    public SensorsModel getSensors(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        SensorsModel sensorsModel = null;
        Cursor cursor = db.query(TABLE_SENSORS, new String[]{KEY_SENSOR_ID, KEY_FOREIGN_ID, KEY_SENSOR_TIME, KEY_X_ROTATION, KEY_Y_ROTATION, KEY_Z_ROTATION, KEY_GYRO_ACCURACY, KEY_X_ACC, KEY_Y_ACC, KEY_Z_ACC, KEY_ACC_ACCURACY}, KEY_FOREIGN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                sensorsModel = new SensorsModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }

        return sensorsModel;
    }

    @Override
    public List<SensorsModel> getAllSensors() {
        List<SensorsModel> sensorsModels = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SENSORS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SensorsModel sensorsModel = new SensorsModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
                    sensorsModels.add(sensorsModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        return sensorsModels;
    }

    @Override
    public int getSensorsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SENSORS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    @Override
    public int updateSensors(SensorsModel SensorsModel) {
        return 0;
    }

    @Override
    public void deleteSensors(SensorsModel sensorsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SENSORS, KEY_FOREIGN_ID + " = ?",
                new String[]{String.valueOf(sensorsModel.get_id())});
    }

    @Override
    public List<AllSensorData> getAllSensorsById(int id) {
        List<AllSensorData> allSensorDatas = new ArrayList<>();
        String countQuery = "SELECT  * FROM " + TABLE_SENSORS + " WHERE " + KEY_FOREIGN_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    //SensorsModel sensorsModel = new SensorsModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
                    AllSensorData allSensorData = new AllSensorData();
                    allSensorData.setRawGyrometer(new RawGyrometer(Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_X_ROTATION))),
                            Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_Y_ROTATION))),
                            Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_Z_ROTATION))),
                            Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_GYRO_ACCURACY)))));

                    allSensorData.setRawAccelerometer(new RawAccelerometer(Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_X_ACC))),
                            Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_Y_ACC))),
                            Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_Z_ACC))),
                            Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_ACC_ACCURACY)))));

                    allSensorData.setDate(Long.valueOf(cursor.getString(cursor.getColumnIndex(KEY_SENSOR_TIME))));
                    allSensorDatas.add(allSensorData);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        return allSensorDatas;
    }

    @Override
    public void addJson(String jsonModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BODY, jsonModel);
        values.put(KEY_JSON_TIME, String.valueOf(new DateTime().toDate().getTime()));
        db.insert(TABLE_JSON, null, values);
    }

    @Override
    public JsonModel getJson(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        JsonModel jsonModel = null;
        Cursor cursor = db.query(TABLE_JSON, new String[]{KEY_JSON_ID, KEY_JSON_TIME, KEY_BODY}, KEY_JSON_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                jsonModel = new JsonModel(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        return jsonModel;
    }

    @Override
    public List<JsonModel> getAllJson() {
        List<JsonModel> jsonModelist = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_JSON;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    jsonModelist.add(new JsonModel(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        return jsonModelist;
    }

    @Override
    public int getJsonCount() {
        String countQuery = "SELECT  * FROM " + TABLE_JSON;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        try {
            if (cursor != null) {
                count = cursor.getCount();
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }

        return count;
    }

    @Override
    public int updateJson(JsonModel jsonModel) {
        return 0;
    }

    @Override
    public void deleteJson(int deleteUptoId) {
        String selectQuery = "DELETE FROM " + TABLE_JSON + " where _id  < " + deleteUptoId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }

    }

    @Override
    public void deleteJsonById(int deleteId) {
        String selectQuery = "DELETE FROM " + TABLE_JSON + " where _id = " + deleteId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
    }

    @Override
    public List<JsonModel> getTopJson() {
        String selectQuery = "SELECT * FROM " + TABLE_JSON + " order by " + KEY_JSON_TIME + " asc";
        List<JsonModel> jsonModel = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    jsonModel.add(new JsonModel(cursor.getString(cursor.getColumnIndex(KEY_JSON_ID)), cursor.getString(cursor.getColumnIndex(KEY_JSON_TIME)), cursor.getString(cursor.getColumnIndex(KEY_BODY))));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        return jsonModel;
    }

    public void deleteSensorsById(LocationModel locationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SENSORS, KEY_FOREIGN_ID + " = ?", new String[]{String.valueOf(locationModel.get_id())});
    }

    @Override
    public void addPolicy(PolicyModel policyModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_POLICY_ID, String.valueOf(policyModel.getId()));
        values.put(KEY_POLICY_TITLE, String.valueOf(policyModel.getTitle()));
        values.put(KEY_POLICY_CONTENT, String.valueOf(policyModel.getContent()));
        values.put(KEY_NOTICE_TYPE, String.valueOf(policyModel.getNoticeType()));
        values.put(KEY_CREATED_TIMESTAMP, String.valueOf(policyModel.getCreatedTimeStamp()));
        values.put(KEY_LAST_UPDATED_TIMESTAMP, String.valueOf(policyModel.getLastUpdatedTimeStamp()));
        values.put(KEY_POLICY_LANGUAGE, String.valueOf(policyModel.getLanguage()));

        db.insert(TABLE_POLICY, null, values);
    }

    @Override
    public PolicyModel getLatestPolicy() {
        String selectQuery = "SELECT * FROM " + TABLE_POLICY + " order by " + KEY_LAST_UPDATED_TIMESTAMP + " asc";
        List<PolicyModel> policyModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    policyModels.add(new PolicyModel(cursor.getLong(cursor.getColumnIndex(KEY_POLICY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_TITLE)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_CONTENT)),
                            cursor.getString(cursor.getColumnIndex(KEY_NOTICE_TYPE)),
                            cursor.getString(cursor.getColumnIndex(KEY_SOURCE)),
                            cursor.getString(cursor.getColumnIndex(KEY_CREATED_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATED_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_LANGUAGE))));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        if (policyModels != null && policyModels.size() > 0) {
            return policyModels.get(0);
        }
        return null;
    }

    @Override
    public PolicyModel getLatestPolicyByLanguage(String languageCode) {
        String selectQuery = "SELECT * FROM " + TABLE_POLICY + " where _language=" + languageCode + " order by " + KEY_LAST_UPDATED_TIMESTAMP + " asc";
        List<PolicyModel> policyModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    policyModels.add(new PolicyModel(cursor.getLong(cursor.getColumnIndex(KEY_POLICY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_TITLE)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_CONTENT)),
                            cursor.getString(cursor.getColumnIndex(KEY_NOTICE_TYPE)),
                            cursor.getString(cursor.getColumnIndex(KEY_SOURCE)),
                            cursor.getString(cursor.getColumnIndex(KEY_CREATED_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATED_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_LANGUAGE))));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        if (policyModels != null && policyModels.size() > 0) {
            return policyModels.get(0);
        }
        return null;
    }

    @Override
    public ArrayList<PolicyModel> getPolicyList() {
        String selectQuery = "SELECT * FROM " + TABLE_POLICY + " order by " + KEY_LAST_UPDATED_TIMESTAMP + " asc";
        ArrayList<PolicyModel> policyModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    policyModels.add(new PolicyModel(cursor.getLong(cursor.getColumnIndex(KEY_POLICY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_TITLE)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_CONTENT)),
                            cursor.getString(cursor.getColumnIndex(KEY_NOTICE_TYPE)),
                            cursor.getString(cursor.getColumnIndex(KEY_SOURCE)),
                            cursor.getString(cursor.getColumnIndex(KEY_CREATED_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATED_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_LANGUAGE))));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        return policyModels;
    }

    @Override
    public PolicyModel getPolicyById(long id) {
        String selectQuery = "SELECT * FROM " + TABLE_POLICY + " where _id=" + id;
        ArrayList<PolicyModel> policyModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    policyModels.add(new PolicyModel(cursor.getLong(cursor.getColumnIndex(KEY_POLICY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_TITLE)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_CONTENT)),
                            cursor.getString(cursor.getColumnIndex(KEY_NOTICE_TYPE)),
                            cursor.getString(cursor.getColumnIndex(KEY_SOURCE)),
                            cursor.getString(cursor.getColumnIndex(KEY_CREATED_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATED_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex(KEY_POLICY_LANGUAGE))));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
        if (policyModels != null && policyModels.size() > 0) {
            return policyModels.get(0);
        }
        return null;
    }

    public boolean tableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        String query = "SELECT COUNT(*) FROM  sqlite_master WHERE type =  \'table\' " + " AND name = \'" + tableName + "\'";
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void policyDeleteByID(long id) {
        String selectQuery = "DELETE FROM " + TABLE_POLICY + " where _id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
    }

    public void deleteByGreaterThanZero() {
        String selectQuery = "DELETE FROM " + TABLE_LOCATION + " where _id > 0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                cursor.close();
            }
        } finally {
            if (cursor != null)
                cursor.close();  // RIGHT: ensure resource is always recovered
        }
    }
}
