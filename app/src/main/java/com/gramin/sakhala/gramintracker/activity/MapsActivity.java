package com.gramin.sakhala.gramintracker.activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.gramin.sakhala.gramintracker.R;
import com.gramin.sakhala.gramintracker.dto.LocationProcessedDto;
import com.gramin.sakhala.gramintracker.dto.PendingFileDto;
import com.gramin.sakhala.gramintracker.helper.LocaleHelper;
import com.gramin.sakhala.gramintracker.service.GPSTrackerService;
import com.gramin.sakhala.gramintracker.service.UploadAlarmReceiver;
import com.gramin.sakhala.gramintracker.util.Prefs;
import com.rivigo.sdk.database.DatabaseHandler;
import com.rivigo.sdk.database.LocationModel;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;

public class MapsActivity extends GPSTrackingBaseActivity implements OnMapReadyCallback {

    public static final String ACTION_START = "com.gramin.sakhala.gramintracker.START";

    public static final String ACTION_STOP = "ccom.gramin.sakhala.gramintracker.STOP";

    public static final int PENDING_POD_BROADCAST_REQUEST_CODE = 0x4;

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int AUTO_HIDE_DELAY_MILLIS = 300;
    public static final String MAP_ACTIVITY_ALARM_RECEIVER = "";
    List<LatLng> latLngs;
    Button trackBtn;
    CoordinatorLayout main;
    TextView distanceM;
    LinearLayout llBottomSheet;
    Activity activity;
    TextView areaBottom;
    TextView distanceBottom;
    TextView kmlFileNameBottom;
    TextView titleBottom;
    String formData;
    Double area = 0.0d;
    String fileName = "";
    String locationStrForMyMap = "";
    String locationStrForGoogleEarth = "";
    FloatingActionButton fab;
    BottomSheetBehavior bottomSheetBehavior;
    PendingIntent pendingIntent;
    AlarmManager manager;
    Context context;
    private GoogleMap mMap;
    String departMent = "";
    private DatabaseHandler db;
    private String ImagefileName = "";
    private Handler handler;
    private Runnable runnable;

    private static DecimalFormat df2 = new DecimalFormat(".###");

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            //final TextView responseFromService = (TextView) findViewById(R.id.responseFromService);

            //.setText(intent.getCharSequenceExtra("data"));
            Log.d(TAG, "Reciever from service");

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        activity = this;
        context = this;

        bindContent();
        LocaleHelper.setLocale(this, Prefs.getLocaleLang(getApplicationContext()));
        db = new DatabaseHandler(this);

        if (getIntent() != null) {
            formData = getIntent().getStringExtra("form_data");
            departMent = getIntent().getStringExtra("department");
        }
        LocaleHelper.setLocale(this, Prefs.getLocaleLang(getApplicationContext()));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final IntentFilter myFilter = new IntentFilter(GPSTrackerService.ACTION_FROM_SERVICE);

        registerReceiver(mReceiver, myFilter);

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Snackbar.make(findViewById(R.id.main_track), "Please provide location permission", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    int permission = PermissionChecker.checkSelfPermission(activity, ACCESS_FINE_LOCATION);

                    if (permission != PermissionChecker.PERMISSION_GRANTED) {
                        Snackbar.make(findViewById(R.id.main_track), "Please provide location permission", Snackbar.LENGTH_SHORT).show();
                        return;
                    } else {
                        // permission not granted, you decide what to do
                    }
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        Snackbar.make(findViewById(R.id.main_track), "Please provide contacts permission", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    int permission = PermissionChecker.checkSelfPermission(activity, READ_CONTACTS);
                    if (permission != PermissionChecker.PERMISSION_GRANTED) {
                        Snackbar.make(findViewById(R.id.main_track), "Please provide contacts permission", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (!GPSTrackerService.isRunning()) {
                    trackBtn.setBackgroundColor(getResources().getColor(R.color.textColor));
                    trackBtn.setText(getString(R.string.Stop));
                    trackBtn.setTextColor(getResources().getColor(R.color.colorBtn));
                    db.deleteByGreaterThanZero();
                    setup(0);
                    Prefs.putFuzData(activity, formData);
                    zoomBottomSheetAnimation(llBottomSheet);
                    handler.postDelayed(runnable, 1000);
                } else {
                    setup(1);
                    trackBtn.setText(getString(R.string.Start));
                    trackBtn.setTextColor(getResources().getColor(android.R.color.white));
                    trackBtn.setBackgroundColor(getResources().getColor(R.color.colorBtn));
                    calculateData();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Snackbar.make(findViewById(R.id.main_track), "Please provide Camera permission", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    int permission = PermissionChecker.checkSelfPermission(activity, CAMERA);
                    if (permission != PermissionChecker.PERMISSION_GRANTED) {
                        Snackbar.make(findViewById(R.id.main_track), "Please provide Camera permission", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                onMapReady(mMap);
                handler.postDelayed(this, 1000);
            }
        };
        handler = new Handler();

        if (GPSTrackerService.isRunning()) {
            trackBtn.setBackgroundColor(getResources().getColor(R.color.textColor));
            trackBtn.setText(getString(R.string.Stop));
            trackBtn.setTextColor(getResources().getColor(R.color.colorBtn));
            zoomBottomSheetAnimation(llBottomSheet);
            handler.postDelayed(runnable, 1000);
        } else {
            zoomFabAnimation(fab);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            File forestDir = new File(Environment.getExternalStorageDirectory() + "/forest_tracker/data/cache/image/");
            if (!forestDir.exists()) {
                forestDir.mkdirs();
            }
            ImagefileName = "";
            ImagefileName = ImagefileName + departMent + "_" + DateTime.now() + ".jpg";
            new BackGroundImageTask(imageBitmap).execute();

        }
    }

    private void bindContent() {
        trackBtn = (Button) findViewById(R.id.startAndStopTrack);
        main = (CoordinatorLayout) findViewById(R.id.main_track);
        distanceM = (TextView) findViewById(R.id.distance_m);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        bindBottomSheet();
        initAlaram();
    }

    private void bindBottomSheet() {
        llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        areaBottom = findViewById(R.id.area_bottom);
        kmlFileNameBottom = findViewById(R.id.kml_bottom);
        distanceBottom = findViewById(R.id.distance_bottom);
        titleBottom = findViewById(R.id.title_bottom);

// init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

// change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        /*bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);*/

// set the peek height
        bottomSheetBehavior.setPeekHeight(100);

// set hideable or not
        bottomSheetBehavior.setHideable(false);

        //bottomSheetBehavior.setState(STATE_COLLAPSED);

// set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        zoomFabAnimation(fab);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        latLngs = calculateLocation();
        mMap = googleMap;
        if (latLngs != null && latLngs.size() > 0) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latLngs.get(0).latitude, latLngs.get(0).longitude))      // Sets the center of the map to location user
                    .zoom(20)                   // Sets the zoom
                    // Sets the orientation of the camera to east
                    // Sets the tilt of the camera to 30 degrees
                    .build();
            // Add a marker in Sydney and move the camera
            mMap.addMarker(new MarkerOptions().position(latLngs.get(0)).title(SphericalUtil.computeArea(latLngs) + " / " + SphericalUtil.computeLength(latLngs)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(0)));
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            PolylineOptions polyLineOptions = new PolylineOptions();
            polyLineOptions.width(3.0f);
            polyLineOptions.geodesic(true);
            polyLineOptions.color(getResources().getColor(android.R.color.black));
            polyLineOptions.addAll(latLngs);
            Polyline polyline = mMap.addPolyline(polyLineOptions);
            polyline.setGeodesic(true);
        } else {
            latLngs.add(new LatLng(28.456274, 77.069509));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latLngs.get(0).latitude, latLngs.get(0).longitude))      // Sets the center of the map to location user
                    .zoom(20)                   // Sets the zoom
                    // Sets the orientation of the camera to east
                    // Sets the tilt of the camera to 30 degrees
                    .build();
            // Add a marker in Sydney and move the camera
            mMap.addMarker(new MarkerOptions().position(latLngs.get(0)).title(SphericalUtil.computeArea(latLngs) + " / " + SphericalUtil.computeLength(latLngs)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(0)));
            //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            PolylineOptions polyLineOptions = new PolylineOptions();
            polyLineOptions.width(3.0f);
            polyLineOptions.geodesic(true);
            polyLineOptions.color(getResources().getColor(android.R.color.black));
            polyLineOptions.addAll(latLngs);
            Polyline polyline = mMap.addPolyline(polyLineOptions);
            polyline.setGeodesic(true);
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private List<LatLng> calculateLocation() {
        List<LatLng> lngs = new ArrayList<>();
        List<LocationModel> locationModelList = db.getAllLocation();
        for (int i = 0; i < locationModelList.size(); i++) {
            lngs.add(new LatLng(Double.valueOf(locationModelList.get(i).get_latitude()),
                    Double.valueOf(locationModelList.get(i).get_longitude())));
        }

        return lngs;
    }

    void sendToService(CharSequence text) {

        Log.d("BroadcastActivity", "Sending message to service: " + text);

        final Intent intent = new Intent(GPSTrackerService.ACTION_TO_SERVICE);

        intent.putExtra("data", text);

        sendBroadcast(intent);

    }

    private void calculateData() {
        handler.removeCallbacks(runnable);
        List<LocationModel> modelList = db.getAllLocation();
        db.close();
        List<LatLng> lngs = new ArrayList<>();
        double distance = 0;
        for (int i = 0; i < modelList.size(); i++) {
            lngs.add(new LatLng(Double.valueOf(modelList.get(i).get_latitude()),
                    Double.valueOf(modelList.get(i).get_longitude())));
            locationStrForMyMap = locationStrForMyMap + modelList.get(i).get_latitude() + "," + modelList.get(i).get_longitude() + "\n";
            locationStrForGoogleEarth = locationStrForGoogleEarth + modelList.get(i).get_longitude() + "," + modelList.get(i).get_latitude() + "\n";
        }

        distance = SphericalUtil.computeLength(lngs);

        /*startLoc.setLatitude(Double.valueOf(modelList.get(modelList.size() - 1).get_latitude()));
        endLoc.setLatitude(Double.valueOf(modelList.get(0).get_longitude()));
        double x = (startLoc.distanceTo(endLoc)/360.0) * (180 + endLoc.getLongitude());
        double y = (startLoc.distanceTo(endLoc)/180.0) * (90 - endLoc.getLatitude());
        locationProcessedDtos.add(new LocationProcessedDto(x, y, Double.valueOf(startLoc.distanceTo(endLoc))));
        */

        area = SphericalUtil.computeArea(lngs);
        Log.d(TAG, "" + area);
        Log.d(TAG, "" + distance);

        Intent intent = new Intent(this, ConclusionActivity.class);
        intent.putExtra("distance", String.valueOf(distance));
        intent.putExtra("area", String.valueOf(area));
        //intent.putExtra("location", locationStr);
        intent.putExtra("form_data", Prefs.getFuzData(activity));
        //startActivity(intent);
        distanceM.setText(getString(R.string.distance) + " " + df2.format((distance/1000)) + " Km");
        mMap.clear();
        onMapReady(mMap);
        zoomFabAnimation(fab);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        titleBottom.setText(getString(R.string.deparmanet_str));
        /*if(HARYAALI_SCHEME) {
            titleBottom.setText(getString(R.string.deparmanet_str));
        }else{
            titleBottom.setText(getString(R.string.scheme_secon));
        }*/

        distanceBottom.setText(getString(R.string.distance) + " " + df2.format((distance/1000)) + " Km");
        areaBottom.setText(getString(R.string.area) + " " + df2.format((area / 10000)) + " hac");

        fileName = "";
        fileName = fileName + departMent + distanceBottom.getText().toString() + "_" + DateTime.now().plusHours(5).plusMinutes(30) + "_" + ".kml";


        kmlFileNameBottom.setText(getString(R.string.kml) + " : " + fileName);



        initKmlForGoogleEarth();
        //initKmlForMyMap();
    }

    private void zoomFabAnimation(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.ZoomOut).duration(AUTO_HIDE_DELAY_MILLIS).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(Animator animator) {
                    view.setVisibility(View.GONE);
                    llBottomSheet.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomIn).duration(AUTO_HIDE_DELAY_MILLIS).playOn(llBottomSheet);
                }
            }).playOn(view);
        }
    }

    private void zoomBottomSheetAnimation(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.ZoomOut).duration(AUTO_HIDE_DELAY_MILLIS).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(Animator animator) {
                    view.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomIn).duration(AUTO_HIDE_DELAY_MILLIS).playOn(fab);
                }
            }).playOn(view);
        }
    }

    private void initKmlForMyMap() {
        String kml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "  <Placemark>\n" +
                "    <name>" + formData + " : Area : " + area + "Sqm" + "</name>\n" +
                "    <Polygon>\n" +
                "      <extrude>1</extrude>\n" +
                "      <altitudeMode>relativeToGround</altitudeMode>\n" +
                "      <outerBoundaryIs>\n" +
                "        <LinearRing>\n" +
                "          <coordinates>\n" +
                locationStrForMyMap +
                "          </coordinates>\n" +
                "        </LinearRing>\n" +
                "      </outerBoundaryIs>\n" +
                "    </Polygon>\n" +
                "  </Placemark>\n" +
                "</kml>";

        Log.d("KML file", kml);

        File forestDir = new File(Environment.getExternalStorageDirectory() + "/forest_tracker/data/cache/");

        if (!forestDir.exists()) {
            forestDir.mkdirs();
        }
        new BackGroundTask(kml).execute();

    }


    private void initKmlForGoogleEarth() {
        String kml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "  <Placemark>\n" +
                "    <name>" + formData + " : Area : " + area + "Sqm" + "</name>\n" +
                "    <Polygon>\n" +
                "      <extrude>1</extrude>\n" +
                "      <altitudeMode>relativeToGround</altitudeMode>\n" +
                "      <outerBoundaryIs>\n" +
                "        <LinearRing>\n" +
                "          <coordinates>\n" +
                locationStrForGoogleEarth +
                "          </coordinates>\n" +
                "        </LinearRing>\n" +
                "      </outerBoundaryIs>\n" +
                "    </Polygon>\n" +
                "  </Placemark>\n" +
                "</kml>";

        Log.d("KML file", kml);

        File forestDir = new File(Environment.getExternalStorageDirectory() + "/forest_tracker/data/cache/");

        if (!forestDir.exists()) {
            forestDir.mkdirs();
        }
        new BackGroundTask(kml).execute();

    }

    public void postData() {
        if (manager == null) {
            manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1 * 60 * 1000, pendingIntent);
        }
    }

    public void initAlaram() {
        Intent alarmIntent = new Intent(this, UploadAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, PENDING_POD_BROADCAST_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    class BackGroundTask extends AsyncTask<String, Void, Void> {

        String kml = "";

        public BackGroundTask(String kml) {
            this.kml = kml;
        }

        @Override
        protected Void doInBackground(String... strings) {
            File myExternalFile = new File(Environment.getExternalStorageDirectory() + "/forest_tracker/data/cache/" + fileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(myExternalFile);
                fos.write(kml.getBytes());
                fos.close();
                List<PendingFileDto> pendingFileDtos = Prefs.getPendingPOD(activity);
                if (pendingFileDtos != null) {
                    pendingFileDtos.add(new PendingFileDto(myExternalFile.getPath(), fileName));
                } else {
                    pendingFileDtos = new ArrayList<>();
                    pendingFileDtos.add(new PendingFileDto(myExternalFile.getPath(), fileName));
                }
                Prefs.addPendingPOD(activity, pendingFileDtos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            postData();
        }
    }

    class BackGroundImageTask extends AsyncTask<String, Void, Void> {

        Bitmap bitmap;

        public BackGroundImageTask(Bitmap kml) {
            this. bitmap = kml;
        }

        @Override
        protected Void doInBackground(String... strings) {
            File myExternalFile = new File(Environment.getExternalStorageDirectory() + "/forest_tracker/data/cache/image/" + ImagefileName);
            OutputStream stream = null;
            try {
                stream = new FileOutputStream(myExternalFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                stream.flush();
                stream.close();
                List<PendingFileDto> pendingFileDtos = Prefs.getPendingPOD(activity);
                if (pendingFileDtos != null) {
                    pendingFileDtos.add(new PendingFileDto(myExternalFile.getPath(), ImagefileName));
                } else {
                    pendingFileDtos = new ArrayList<>();
                    pendingFileDtos.add(new PendingFileDto(myExternalFile.getPath(), ImagefileName));
                }
                Prefs.addPendingPOD(activity, pendingFileDtos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            postData();
        }
    }



}
