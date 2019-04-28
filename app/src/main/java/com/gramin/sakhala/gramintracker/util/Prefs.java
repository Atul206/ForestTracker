package com.gramin.sakhala.gramintracker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gramin.sakhala.gramintracker.dto.LocationDTO;
import com.gramin.sakhala.gramintracker.dto.PendingFileDto;
import com.gramin.sakhala.gramintracker.service.GPSTrackerService;
import com.gramin.sakhala.gramintracker.service.UploadService;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Atul on 24-11-2016.
 */

public class Prefs implements Constant {
    private static final String TAG = Prefs.class.getSimpleName();
    private static final String AUTH_TOKEN = "auth_token";
    private static final String WB_DATA = "wb_data";
    private static final String ALL_USER = "all_user";
    private static final String GPS_LOCATION = "gps_location";
    private static final String GPS_REGISTER = "gps_register";
    private static final String PENDIND_POD_LIST = "pending_pod_list";
    private static final String MOCKED_DATA = "moked_data";
    private static final String MAP_MOCKED_DATA = "map_moked_data";
    private static String ENABLED = "enabled";
    private static String UPDATE_FREQ = "update_freq";
    private static String LOCALE_LANG = "locale_lang";
    private static String SKIP_COUNT = "skip_count";
    private static String IS_PILOT_PAYOUT_ENABLED = "IS_PILOT_PAYOUT_ENABLED";
    private static String CHALLAN_SHOW_CASE_COUNT = "CHALLAN_SHOW_CASE_COUNT";
    private static String EWAY_BILL_CONFIG = "EWAY_BILL_CONFIG";
    private static String CAMPAIGN_DATA = "CAMPAIGN_DATA";
    private static String PILOT_CONFIG = "PILOT_CONFIG";
    private static String ASSET_UPDATE_DTO = "ASSET_UPDATE_DTO";
    private static List<PendingFileDto> pendingPOD;


    public static SharedPreferences get(final Context context) {
        return context.getSharedPreferences("com.gramin.sakhala.gramintracker",
                Context.MODE_PRIVATE);
    }

    public static String getPref(final Context context, String pref,
                                 String def) {
        SharedPreferences prefs = Prefs.get(context);
        String val = prefs.getString(pref, def);

        if (val == null || val.equals("") || val.equals("null"))
            return def;
        else
            return val;
    }


    public static boolean getPrefBoolean(final Context context, String pref,
                                         boolean def) {
        SharedPreferences prefs = Prefs.get(context);
        boolean val = prefs.getBoolean(pref, def);
        return val;
    }

    public static void clearPreference(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public static void putPref(final Context context, String pref,
                               String val) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(pref, val);
        editor.apply();
    }


    public static boolean getEnabled(final Context context) {
        String e = Prefs.getPref(context, ENABLED, "false");
        return e.equals("true");
    }


    public static void putEnabled(final Context context, boolean enabled) {
        Prefs.putPref(context, ENABLED, (enabled ? "true" : "false"));
    }


    public static String getLocaleLang(Context context) {
        return Prefs.getPref(context, LOCALE_LANG, null);
    }

    public static void putLocaleLang(Context context, String lang) {
        Prefs.putPref(context, LOCALE_LANG, lang);
    }

   /* public static void saveGPSLocation(Context context, LocationDTO locationDTOMap) {
        Type type = new TypeToken<LocationDTO>() {
        }.getType();
        String responseString = new Gson().toJson(locationDTOMap, type);
        Prefs.putPref(context, GPS_LOCATION, responseString);
    }

    public static LocationDTO getGPSLocation(Context context) {
        String responseString = Prefs.getPref(context, GPS_LOCATION, null);
        Type type = new TypeToken<LocationDTO>() {
        }.getType();
        LocationDTO locationDTOMap = new Gson().fromJson(responseString, type);
        if (locationDTOMap == null) {
            locationDTOMap = new LocationDTO();
            locationDTOMap.setLatitude(0d);
            locationDTOMap.setLongitude(0d);
        }
        return locationDTOMap;
    }

    public static boolean isGPSRegister(GPSTrackingBaseActivity gpsTrackingBaseActivity) {
        return getPrefBoolean(gpsTrackingBaseActivity, GPS_REGISTER, false);
    }*/

    public static String getFuzData(final Context context) {
        if (context == null) return null;
        SharedPreferences preferences = Prefs.get(context);
        return preferences.getString(MOCKED_DATA, null);
    }

    public static Map<String, String> getFuzDataMap(final Context context) {
        if (context == null) return null;
        SharedPreferences preferences = Prefs.get(context);
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return new Gson().fromJson(preferences.getString(MAP_MOCKED_DATA, "NA"),type);
    }

    public static void putFuzDataMap(final Context context, Map<String, String> data) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(MAP_MOCKED_DATA, new Gson().toJson(data));
        editor.apply();

    }


    public static void putPilotPayoutEnabled(final Context context, boolean fuzData) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IS_PILOT_PAYOUT_ENABLED, fuzData);
        editor.apply();

    }

    public static boolean isPilotPayoutEnabled(final Context context) {
        if (context == null) return true;
        SharedPreferences preferences = Prefs.get(context);
        return preferences.getBoolean(IS_PILOT_PAYOUT_ENABLED, false);
    }


    public static void putChallanShowCaseCount(final Context context, int count) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(CHALLAN_SHOW_CASE_COUNT, count);
        editor.apply();

    }

    public static int getChallanShowCaseCount(final Context context) {
        if (context == null) return 0;
        SharedPreferences preferences = Prefs.get(context);
        return preferences.getInt(CHALLAN_SHOW_CASE_COUNT, 0);
    }

    public static void putFuzData(final Context context, String fuzData) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(MOCKED_DATA, fuzData);
        editor.apply();

    }

    public static void deleteFuzData(final Context context) {
        if (context == null) return;
        SharedPreferences settings = context.getSharedPreferences("com.rivigo.pilot.app", Context.MODE_PRIVATE);
        settings.edit().remove(MOCKED_DATA).apply();

    }

    public static void startLocation(GPSTrackerService gpsTrackerService, LocationDTO locationDTO) {
        SharedPreferences prefs = Prefs.get(gpsTrackerService);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(GPS_LOCATION, new Gson().toJson(locationDTO));
        editor.apply();
    }

    public static void addPendingPOD(Context uploadService, List<PendingFileDto> pendingFileDtos) {
        SharedPreferences prefs = Prefs.get(uploadService);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PENDIND_POD_LIST,  new Gson().toJson(pendingFileDtos));
        editor.apply();
    }

    public static List<PendingFileDto> getPendingPOD(Context context) {
        if (context == null) return null;
        SharedPreferences preferences = Prefs.get(context);
        String data = preferences.getString(PENDIND_POD_LIST, null);
        Gson gson = new Gson();
        return gson.fromJson(data, new TypeToken<ArrayList<PendingFileDto>>() {
        }.getType());
    }

    public static LocationDTO getLastLocation(GPSTrackerService gpsTrackerService) {
        if (gpsTrackerService == null) return null;
        SharedPreferences preferences = Prefs.get(gpsTrackerService);
        String data = preferences.getString(GPS_LOCATION, null);
        Gson gson = new Gson();
        return gson.fromJson(data, new TypeToken<ArrayList<LocationDTO>>() {
        }.getType());
    }

    public static String getOffDutyDate(long milliseconds) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd-MMMM-yyyy, HH:mm ");
        return dateTimeFormatter.print(milliseconds);
    }
}


