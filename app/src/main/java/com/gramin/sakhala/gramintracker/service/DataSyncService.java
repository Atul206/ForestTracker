package com.gramin.sakhala.gramintracker.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;

import com.rivigo.sdk.Constant;
import com.rivigo.sdk.database.DatabaseHandler;
import com.rivigo.sdk.manager.SDKSensorManager;
import com.rivigo.sdk.mqtt.OnMQTTConnection;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Atul on 19-07-2017.
 */

public class DataSyncService extends IntentService implements Constant, OnMQTTConnection {

    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.vogella.android.service.receiver";
    private static final String TAG = DataSyncService.class.getSimpleName();
    private static final String UNAVAIALABLE = "NA";
    private static Boolean isHTTP = null;
    private static volatile PowerManager.WakeLock wakeLock;
    final ReentrantReadWriteLock updateLock = new ReentrantReadWriteLock();
    SDKSensorManager mSdkSensorManager;
    private DatabaseHandler db;
    private int result = Activity.RESULT_CANCELED;
    private AsyncTask httpPoster;
    private AsyncTask mqttPoster;

    public DataSyncService() {
        super("DownloadService");
    }

    public static void httpEnable() {
        isHTTP = true;
    }

    public static void httpDisable() {
        isHTTP = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        db = new DatabaseHandler(this);

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail() {

    }

    @Override
    public void onPublish() {

    }

    @Override
    public void onPublishFail() {

    }

    /**
     * This method call when Alarm wakeup and try to accumulate all data from the database table "Location_table"
     * and put into "Json table" for in memory queue process.
     */
}
