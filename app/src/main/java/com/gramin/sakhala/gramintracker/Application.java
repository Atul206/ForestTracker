package com.gramin.sakhala.gramintracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.util.Log;

import com.cloudrail.si.interfaces.CloudStorage;
import com.cloudrail.si.services.GoogleDrive;
import com.google.firebase.auth.FirebaseUser;
import com.gramin.sakhala.gramintracker.helper.LocaleHelper;
import com.gramin.sakhala.gramintracker.service.UploadAlarmReceiver;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;

import java.io.File;
import java.io.InputStream;

/**
 * Created by atulsakhala on 04/08/18.
 */

public class Application extends android.app.Application {


    public static Application mApplication;

    public static Application getInstance(){
        return mApplication;
    }

    public PendingIntent pendingIntent;

    private static FirebaseUser user;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        DateTimeZone.setProvider(new UTCProvider());
        JodaTimeAndroid.init(this);
        DateTimeZone.setProvider(new UTCProvider());
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    private FirebaseUser getFirebaseUser(){
        return this.user;
    }


}
