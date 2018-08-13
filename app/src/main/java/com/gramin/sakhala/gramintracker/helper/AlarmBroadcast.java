package com.gramin.sakhala.gramintracker.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gramin.sakhala.gramintracker.service.DataSyncService;
import com.rivigo.sdk.manager.GoogleLocationManager;

/**
 * Created by Atul on 24-11-2016.
 */

public class AlarmBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /* we now have a wake lock until we return */
        if(GoogleLocationManager.getInstance() != null) {
            GoogleLocationManager.getInstance().startLocationUpdates();
        }else{
            Log.d("AlarmBroadcast", "GPS Context getting null");
        }
        Intent dataSyncServiceIntent = new Intent(context, DataSyncService.class);
        context.sendBroadcast(new Intent());
        context.startService(dataSyncServiceIntent);

    }
}
