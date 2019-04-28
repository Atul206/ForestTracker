package com.gramin.sakhala.gramintracker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.gramin.sakhala.gramintracker.dto.LocationDTO;
import com.gramin.sakhala.gramintracker.dto.SensorGpsDTO;
import com.gramin.sakhala.gramintracker.util.Constant;
import com.gramin.sakhala.gramintracker.util.Prefs;
import com.rivigo.sdk.database.DatabaseHandler;
import com.rivigo.sdk.database.LocationModel;
import com.rivigo.sdk.manager.GoogleLocationListener;
import com.rivigo.sdk.manager.GoogleLocationManager;
import com.rivigo.sdk.mqtt.MQTTManager;

import org.joda.time.DateTime;


/**
 * Created by Atul on 23-11-2016.
 */

public class GPSTrackerService extends Service implements GoogleLocationListener, Constant {

    private static final String TAG = "GPSTrackerService";

    public static final String ACTION_FROM_SERVICE = "com.gramin.sakhala.gramintracker.FROM_SERVICE";

    public static final String ACTION_TO_SERVICE = "com.gramin.sakhala.gramintracker.TO_SERVICE";

    private static final String UNAVAIALABLE = "NA";
    private static boolean isRunning = false;

    private Long freqSeconds;
    private AlarmManager alarmManager;
    private PendingIntent pendingAlarm;
    private GoogleLocationManager mGoogleLocationManager;
    private Integer pilotId;
    private DatabaseHandler db;

    private boolean officerStartInspection = true;

    private Location lastLocation = null;



    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "BroadcastService" +  "Service received: ");

            if(intent != null) {
                sendToActivity(intent);
            }

        }

    };


    public static boolean isRunning() {
        return isRunning;
    }

    public static void serviceStop() {
        if (isRunning()) {
            isRunning = false;
        }
    }

    public static void createInstance() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        freqSeconds = initSetup();
        isRunning = true;

        /*we're registered here, Acc and Gyro:@param x, @param y, @param z
         * Log all the sensors working properly
         * */

        db = new DatabaseHandler(this);


        mGoogleLocationManager = GoogleLocationManager.getInstance(getApplicationContext(), this);
        mGoogleLocationManager.startLocationUpdates();

        /* we don't need to be exact in our frequency, try to conserve at least
         * a little battery */
        try {
            //alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            //Intent i = new Intent(this, AlarmBroadcast.class);
            //pendingAlarm = PendingIntent.getBroadcast(this, 0, i, 0);
            //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), freqSeconds*1000, pendingAlarm);

        } catch (Exception e) {
            isRunning = false;
            e.printStackTrace();
        }

    }

    Long initSetup() {


        int freqSeconds = 0;
        String freqString = Constant.POLLING_FREQUENCY;

        if (freqSeconds < 1) {
            isRunning = false;
        }



        final IntentFilter myFilter = new IntentFilter(ACTION_TO_SERVICE);

        registerReceiver(mReceiver, myFilter);

        this.lastLocation = null;

        return Long.valueOf(freqSeconds);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();

        try {
//            LocationManager locationManager = (LocationManager)
//                    this.getSystemService(Context.LOCATION_SERVICE);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            mGoogleLocationManager.stopLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //if (pendingAlarm != null) {
          //  alarmManager.cancel(pendingAlarm);
        //}
        //mGoogleLocationManager.clearContext();
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * Putting into database table "LOCATION_TABLE"
     *
     * @param location
     */
    void sendLocation(Location location) {
        SensorGpsDTO sensorGpsDTO = new SensorGpsDTO();
        if (location == null) {
            location = new Location("location");
        } else {
            sensorGpsDTO.setLatitude(location.getLatitude());
            sensorGpsDTO.setLongitude(location.getLongitude());
            sensorGpsDTO.setSpeed(location.getSpeed());
        }
        sensorGpsDTO.setDateTime(DateTime.now().getMillis());
        sensorGpsDTO.setDataClient("RIVIGO_PILOT");
        sensorGpsDTO.setSourceId("RIVIGO_PILOT:" + pilotId);
        sensorGpsDTO.setNetworkCarrier("NA");
        sensorGpsDTO.setBatteryLevel("NA");
        sensorGpsDTO.setPayload(sensorGpsDTO.getBatteryLevel() + "," + sensorGpsDTO.getNetworkCarrier());
        preProcessingData(sensorGpsDTO);
    }

    /**
     * return: void
     * method: processing and compatible with @{@link DatabaseHandler} @Location_table
     *
     * @param sensorGpsDTO
     */
    void preProcessingData(SensorGpsDTO sensorGpsDTO) {
        String sourceId = null, lat = UNAVAIALABLE, lng = UNAVAIALABLE, speed = UNAVAIALABLE, dataC = null, dateTime = null, payload = null;
        if (sensorGpsDTO.getSourceId() != null)
            sourceId = String.valueOf(sensorGpsDTO.getSourceId());
        if (sensorGpsDTO.getLatitude() != null)
            lat = String.valueOf(sensorGpsDTO.getLatitude());
        if (sensorGpsDTO.getLongitude() != null)
            lng = String.valueOf(sensorGpsDTO.getLongitude());
        if (sensorGpsDTO.getSpeed() != null)
            speed = String.valueOf(sensorGpsDTO.getSpeed());
        if (sensorGpsDTO.getDataClient() != null)
            dataC = String.valueOf(sensorGpsDTO.getDataClient());
        if (sensorGpsDTO.getDateTime() != null)
            dateTime = String.valueOf(sensorGpsDTO.getDateTime());
        if (sensorGpsDTO.getPayload() != null)
            payload = sensorGpsDTO.getPayload();


        LocationModel locationModel = new LocationModel("0",
                "RIVIGO_PILOT:" + pilotId,
                lat,
                lng,
                speed,
                dataC,
                dateTime,
                payload);

        db.addLocation(locationModel);
        Log.d(MQTTManager.TAG, String.valueOf(locationModel));
        //GoogleLocationManager.getInstance().stopLocationUpdates();
    }

    /**
     * This method call when Alarm wakeup and try to accumulate all data from the database table "Location_table"
     * and put into "Json table" for in memory queue process.
     */


    private void saveLocation(Location location) {
        this.lastLocation = location;
    }

    private Location getLastLocation(){
       return this.lastLocation;
    }

    private boolean isLocationValid(Location lastLocation, Location currentLocation){
        if(lastLocation == null) {
            this.lastLocation = currentLocation;
            return true;
        }
        double distance = SphericalUtil.computeDistanceBetween(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())
                ,new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
        Toast.makeText(this, ""+distance, Toast.LENGTH_SHORT).show();
        if(distance <= 10.0){
            this.lastLocation = currentLocation;
            return true;
        }else{
            this.lastLocation = currentLocation;
            return false;
        }
    }

    private void calculcateAndSendDistanceAndArea(Location location){
        if(lastLocation == null) {
            return;
        }
        if(location == null) {
            return;
        }
        double distanceTo  = this.lastLocation.distanceTo(location);
        final Intent intent = new Intent(GPSTrackerService.ACTION_FROM_SERVICE);

        intent.putExtra("currentLat", String.valueOf(location.getLatitude()));
        intent.putExtra("currentLng", String.valueOf(location.getLongitude()));

        intent.putExtra("lastLat", String.valueOf(lastLocation.getLatitude()));
        intent.putExtra("lastLng", String.valueOf(lastLocation.getLongitude()));

        sendBroadcast(intent, "com.gramin.sakhala.gramintracker.broadcast.Manifest.permission.ALLOW");
    }

    @Override
    public void onLocationChange(Location location) {
        if(isLocationValid(getLastLocation(), location)) {
            Log.d(MQTTManager.TAG, "NEW LOCATION COME");
            sendLocation(location);
            //calculcateAndSendDistanceAndArea(location);
            saveLocation(location);
        }

    }

    private void sendToActivity(Intent intent){

        Log.d("BroadcastService", "Sending message to activity: ");

        sendBroadcast(intent, "com.gramin.sakhala.gramintracker.broadcast.Manifest.permission.ALLOW");
    }
}