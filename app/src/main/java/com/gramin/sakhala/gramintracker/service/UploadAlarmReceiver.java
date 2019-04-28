package com.gramin.sakhala.gramintracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gramin.sakhala.gramintracker.activity.MapsActivity;
import com.gramin.sakhala.gramintracker.dto.PendingFileDto;
import com.gramin.sakhala.gramintracker.util.Prefs;

import java.util.HashMap;
import java.util.List;

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

        List<PendingFileDto> pendingFileDtos = Prefs.getPendingPOD(context);
        if (pendingFileDtos == null
                || pendingFileDtos.isEmpty()) {
            Intent returnIntent = new Intent(MapsActivity.MAP_ACTIVITY_ALARM_RECEIVER);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
            localBroadcastManager.sendBroadcast(returnIntent);

        }


    }
}
