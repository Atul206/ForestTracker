package com.rivigo.sdk.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by atulsakhala on 05/09/17.
 */

public class GoogleLocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public final static int FAST_LOCATION_FREQUENCY = 1000;
    public final static int LOCATION_FREQUENCY = 3000;
    private static GoogleLocationManager mInstance = null;
    protected GoogleApiClient mGoogleApiClient;
    protected GoogleLocationListener listener;
    private LocationRequest mLocationRequest;
    private Context context;

    public GoogleLocationManager(Context context, GoogleLocationListener listener) {
        this.context = context;
        this.listener = listener;
        buildGoogleApiClient();
    }

    public static GoogleLocationManager getInstance(Context context, GoogleLocationListener listener) {
        if (null == mInstance) {
            mInstance = new GoogleLocationManager(context, listener);
        }

        return mInstance;
    }

    public static GoogleLocationManager getInstance() {
        return mInstance;
    }

    /**
     * destructor
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        stopLocationUpdates();
    }

    private synchronized void buildGoogleApiClient() {
        // setup googleapi client
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // setup location updates
        configRequestLocationUpdate();
    }

    /**
     * config request location update
     */
    private void configRequestLocationUpdate() {
        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_FREQUENCY)
                .setFastestInterval(FAST_LOCATION_FREQUENCY);
    }

    /**
     * request location updates
     */
    private void requestLocationUpdates() {

        try {
            PackageManager pm = context.getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION)
                    || pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
                    || pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK)) {
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_FREQUENCY)
                .setFastestInterval(FAST_LOCATION_FREQUENCY);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        );

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location currentLocation = getLastLocation();
        if(getLastLocation() == null){
            if (currentLocation != null) {
                LocationRequest mLocationRequest = LocationRequest.create();
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
                onLocationChanged(currentLocation);
            }
        }
    }

    /**
     * start location updates
     */
    public void startLocationUpdates() {
        // connect and force the updates
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    /**
     * removes location updates from the FusedLocationApi
     */
    public void stopLocationUpdates() {
        // stop updates, disconnect from google api
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }

    /**
     * get last available location
     *
     * @return last known location
     */
    public Location getLastLocation() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            // return last location
            return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            startLocationUpdates(); // start the updates
            return null;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // do location updates
        requestLocationUpdates();
    }


    @Override
    public void onConnectionSuspended(int i) {
        // connection to Google Play services was lost for some reason
        if (null != mGoogleApiClient) {
            mGoogleApiClient.connect(); // attempt to establish a new connection
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            listener.onLocationChange(location);
        }
    }


    public void clearContext(){
        mInstance = null;
    }

}
