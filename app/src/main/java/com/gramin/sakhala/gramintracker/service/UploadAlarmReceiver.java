package com.gramin.sakhala.gramintracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by atulsakhala on 12/08/18.
 */

public class UploadAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = UploadAlarmReceiver.class.getSimpleName();

    public UploadAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {Log.v(TAG, "Hey See I am getting called in every two minute!!!");
        Intent newIntent = new Intent(context, UploadService.class);
        context.startService(newIntent);

       /* HashMap hashMap = Prefs.getPendingPOD(context);
        if (hashMap == null
                || hashMap.isEmpty()) {
           *//* Intent returnIntent = new Intent(MainActivity.MAIN_ACTIVITY_ALARM_RECEIVER);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
            localBroadcastManager.sendBroadcast(returnIntent);*//*

        }*/


    }
}
