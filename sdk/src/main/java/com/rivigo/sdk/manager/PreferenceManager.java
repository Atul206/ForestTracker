package com.rivigo.sdk.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by gauravk on 29/6/16.
 */
public class PreferenceManager {
    private final static String TAG = PreferenceManager.class.getSimpleName();
    private static final String KEY_SDK_KEY = "IsWaitingForSms";
    private static final String KEY_PILOT_ID = "IsWaitingForSms";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context _context;
    private String sharedPreferenceFileName;
    private int MODE = 0;

    public PreferenceManager(Context context) {
        this._context = context;
        if (context != null) {
            sharedPreferences = context.getSharedPreferences(sharedPreferenceFileName, MODE);
            editor = sharedPreferences.edit();
        }
    }

    public String getSDKKey() {
        if (sharedPreferences == null) {
            return null;
        }
        Log.v(TAG, sharedPreferences.getString(KEY_SDK_KEY, null));
        return sharedPreferences.getString(KEY_SDK_KEY, null);
    }

    public void setSDKKey(String accesstoken) {
        editor.putString(KEY_SDK_KEY, accesstoken);
        editor.commit();
    }

    public String getKeyPilotId() {
        if (sharedPreferences == null) {
            return null;
        }
        Log.v(TAG, sharedPreferences.getString(KEY_PILOT_ID, null));
        return sharedPreferences.getString(KEY_PILOT_ID, null);
    }

    public void setKeyPilotId(String accesstoken) {
        editor.putString(KEY_PILOT_ID, accesstoken);
        editor.commit();
    }

}
