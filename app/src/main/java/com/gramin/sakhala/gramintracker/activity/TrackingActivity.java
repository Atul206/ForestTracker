package com.gramin.sakhala.gramintracker.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.gramin.sakhala.gramintracker.R;
import com.gramin.sakhala.gramintracker.dto.LocationProcessedDto;
import com.gramin.sakhala.gramintracker.helper.LocaleHelper;
import com.gramin.sakhala.gramintracker.service.GPSTrackerService;
import com.gramin.sakhala.gramintracker.util.Prefs;
import com.rivigo.sdk.database.DatabaseHandler;
import com.rivigo.sdk.database.LocationModel;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TrackingActivity extends GPSTrackingBaseActivity {

    Button trackBtn;
    RelativeLayout main;
    String formData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        bindContent();
        if(getIntent() != null) {
            formData = getIntent().getStringExtra("form_data");
        }
        LocaleHelper.setLocale(this, Prefs.getLocaleLang(getApplicationContext()));
        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    return;
                }
                if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.main_track), "Please provide location permission", Snackbar.LENGTH_SHORT).show();
                    return ;
                }

                if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.main_track), "Please provide storage permission", Snackbar.LENGTH_SHORT).show();
                    return ;
                }

                if (checkSelfPermission(READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.main_track), "Please provide contacts permission", Snackbar.LENGTH_SHORT).show();
                    return ;
                }

                if(!GPSTrackerService.isRunning()) {
                    trackBtn.setBackgroundColor(getResources().getColor(R.color.textColor));
                    trackBtn.setText(getString(R.string.Stop));
                    trackBtn.setTextColor(getResources().getColor(R.color.colorBtn));
                    setup(0);
                }else{
                    setup(1);
                    trackBtn.setText(getString(R.string.Start));
                    trackBtn.setTextColor(getResources().getColor(android.R.color.white));
                    trackBtn.setBackgroundColor(getResources().getColor(R.color.colorBtn));
                    calculateData();
                }
            }
        });

        if(GPSTrackerService.isRunning()){
            trackBtn.setBackgroundColor(getResources().getColor(R.color.textColor));
            trackBtn.setText(getString(R.string.Stop));
            trackBtn.setTextColor(getResources().getColor(R.color.colorBtn));
        }
    }

    private void bindContent(){
        trackBtn = (Button) findViewById(R.id.startAndStopTrack);
        main = (RelativeLayout) findViewById(R.id.main_track);
    }

    private void calculateData(){
        DatabaseHandler db = new DatabaseHandler(this);
        List<LocationModel> modelList = db.getAllLocation();
        List<LocationProcessedDto> locationProcessedDtos = new ArrayList<>();
        Location startLoc = new Location("startLoc");
        Location endLoc = new Location("endLoc");
        double distance = 0;
        String locationStr = "";
        for(int i = 0; i < modelList.size(); i++) {
            if(i == modelList.size() - 1){
                startLoc.setLatitude(Double.valueOf(modelList.get(i).get_latitude()));
                startLoc.setLongitude(Double.valueOf(modelList.get(i).get_longitude()));
                endLoc.setLatitude(Double.valueOf(modelList.get(0).get_latitude()));
                endLoc.setLongitude(Double.valueOf(modelList.get(0).get_longitude()));
                double x = (startLoc.distanceTo(endLoc)/360.0) * (180 + endLoc.getLongitude());
                double y = (startLoc.distanceTo(endLoc)/180.0) * (90 - endLoc.getLatitude());
                locationProcessedDtos.add(new LocationProcessedDto(x, y, Double.valueOf(startLoc.distanceTo(endLoc))));
                distance = distance + startLoc.distanceTo(endLoc);

            }else {
                startLoc.setLatitude(Double.valueOf(modelList.get(i).get_latitude()));
                startLoc.setLongitude(Double.valueOf(modelList.get(i).get_longitude()));
                endLoc.setLatitude(Double.valueOf(modelList.get(i + 1).get_latitude()));
                endLoc.setLongitude(Double.valueOf(modelList.get(i + 1).get_longitude()));
                double x = (startLoc.distanceTo(endLoc) / 360.0) * (180 + endLoc.getLongitude());
                double y = (startLoc.distanceTo(endLoc) / 180.0) * (90 - endLoc.getLatitude());
                locationProcessedDtos.add(new LocationProcessedDto(x, y, Double.valueOf(startLoc.distanceTo(endLoc))));
                distance = distance + startLoc.distanceTo(endLoc);
            }
            locationStr = locationStr + modelList.get(i).get_longitude() + " , " + modelList.get(i).get_latitude() + "\n";
        }

        db.deleteByGreaterThanZero();
        /*startLoc.setLatitude(Double.valueOf(modelList.get(modelList.size() - 1).get_latitude()));
        endLoc.setLatitude(Double.valueOf(modelList.get(0).get_longitude()));
        double x = (startLoc.distanceTo(endLoc)/360.0) * (180 + endLoc.getLongitude());
        double y = (startLoc.distanceTo(endLoc)/180.0) * (90 - endLoc.getLatitude());
        locationProcessedDtos.add(new LocationProcessedDto(x, y, Double.valueOf(startLoc.distanceTo(endLoc))));
        */
        double area = 0;
        for(int i = 0; i < locationProcessedDtos.size(); i++) {
            if(i == locationProcessedDtos.size() - 1) {
                area = area + (locationProcessedDtos.get(i).getX()*locationProcessedDtos.get(0).getY() - locationProcessedDtos.get(i).getY()*locationProcessedDtos.get(0).getX());
            }else {
                area = area + (locationProcessedDtos.get(i).getX() * locationProcessedDtos.get(i + 1).getY() - locationProcessedDtos.get(i + 1).getY() * locationProcessedDtos.get(i).getX());
            }
        }
        area = area/(2);

        Intent intent = new Intent(this, ConclusionActivity.class);
        intent.putExtra("distance", String.valueOf(distance));
        intent.putExtra("area", String.valueOf(area));
        intent.putExtra("location", locationStr);
        intent.putExtra("form_data", formData);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
