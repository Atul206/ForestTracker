package com.gramin.sakhala.gramintracker.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.gramin.sakhala.gramintracker.Application;
import com.gramin.sakhala.gramintracker.R;
import com.gramin.sakhala.gramintracker.dto.PendingFileDto;
import com.gramin.sakhala.gramintracker.helper.LocaleHelper;
import com.gramin.sakhala.gramintracker.service.UploadAlarmReceiver;
import com.gramin.sakhala.gramintracker.util.Prefs;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConclusionActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView area, length, file;
    public static final int PENDING_POD_BROADCAST_REQUEST_CODE = 0x4;

    String kmlFile;

    String formData = "Forest Track";
    String distance;
    String areaStr;
    String locations = "";
    Context context;
    String fileName;
    PendingIntent pendingIntent;
    AlarmManager manager;


    GoogleMap.SnapshotReadyCallback snapshotReadyCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusion);
        bindContent();
        LocaleHelper.setLocale(this, Prefs.getLocaleLang(getApplicationContext()));
        context = this;

        if (getIntent() != null) {
            distance = getIntent().getStringExtra("distance");
            areaStr = getIntent().getStringExtra("area");
            locations = getIntent().getStringExtra("location");
            formData = getIntent().getStringExtra("form_data");
        }

        fileName = "Forest_" + DateTime.now() + ".kml";
        file.setText(fileName);
        length.setText(distance + " m.");
        area.setText(areaStr + " Sqm.");

        snapshotReadyCallback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {

            }
        };
        initKml();

    }

    private void bindContent() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        area = (TextView) findViewById(R.id.areaName);
        length = (TextView) findViewById(R.id.lengthName);
        file = (TextView) findViewById(R.id.fileName);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.conclusion));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        initAlaram();
    }

    private void initKml() {
        String kml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "  <Placemark>\n" +
                "    <name>" + formData + " : Area : " + areaStr + "Sqm" + "</name>\n" +
                "    <Polygon>\n" +
                "      <extrude>1</extrude>\n" +
                "      <altitudeMode>relativeToGround</altitudeMode>\n" +
                "      <outerBoundaryIs>\n" +
                "        <LinearRing>\n" +
                "          <coordinates>\n" +
                locations +
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

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
                List<PendingFileDto> pendingFileDtos = Prefs.getPendingPOD(context);
                if (pendingFileDtos != null) {
                    pendingFileDtos.add(new PendingFileDto(myExternalFile.getPath(), fileName));
                } else {
                    pendingFileDtos = new ArrayList<>();
                    pendingFileDtos.add(new PendingFileDto(myExternalFile.getPath(), fileName));
                }
                Prefs.addPendingPOD(context, pendingFileDtos);
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

    public void postData(){
        if (manager == null) {
            manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1* 60 * 1000, pendingIntent);
        }
    }

    public void initAlaram() {
        Intent alarmIntent = new Intent(this, UploadAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, PENDING_POD_BROADCAST_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
