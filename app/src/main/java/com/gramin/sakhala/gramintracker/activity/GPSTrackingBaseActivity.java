package com.gramin.sakhala.gramintracker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.LocationServices;
import com.gramin.sakhala.gramintracker.service.GPSTrackerService;
import com.rivigo.sdk.manager.GoogleLocationManager;


/**
 * Created by Atul on 24-11-2016.
 */

public  abstract class GPSTrackingBaseActivity extends AppCompatActivity {

    private static final String TAG = GPSTrackingBaseActivity.class.getSimpleName();
    Messenger mService = null;
    GPSTrackerService mGpsTrackerService;

    boolean mIsBound;
    LocationServices locationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setup(int i) {

        switch (i) {
            case 0:
                if (!GPSTrackerService.isRunning()) {
                    startService(new Intent(GPSTrackingBaseActivity.this, GPSTrackerService.class));
                    doBindService();
                } else if (GPSTrackerService.isRunning()) {
                    doBindService();
                }
                break;
            case 1:
                doUnbindService();
                break;
        }

    }


    void doBindService() {
        if (GPSTrackerService.isRunning()) {
            //doUnbindService();
           // bindService(new Intent(this, GPSTrackerService.class), null, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        }
    }

    public void doUnbindService() {
        if(GPSTrackerService.isRunning()) {
            GPSTrackerService.serviceStop();
            stopService(new Intent(GPSTrackingBaseActivity.this, GPSTrackerService.class));
            GoogleLocationManager.getInstance().stopLocationUpdates();
        }
        mIsBound = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}