package com.gramin.sakhala.gramintracker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.gramin.sakhala.gramintracker.R;
import com.gramin.sakhala.gramintracker.helper.LocaleHelper;
import com.gramin.sakhala.gramintracker.service.GPSTrackerService;
import com.gramin.sakhala.gramintracker.util.Prefs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by atulsakhala on 17/08/18.
 */

public class SecondScheme extends GPSTrackingBaseActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;

    private Toolbar toolbar;

    private EditText forestArea;
    private EditText location;
    private EditText plantingYear;
    private EditText plantingPlants;
    private EditText SubforestArea;
    private EditText locationHelper;
    private EditText beet;
    private EditText forestType;
    private EditText estimate_area;
    private EditText plantinghelp;
    private EditText livePlants;
    private EditText khasraNo;
    private EditText officerName;
    private EditText plantingYearBetween;
    private EditText plantingType;
    private TextView title;

    private Button trackStartButton;

    private Map<String, String> data;

    private String subDivision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindContent(getIntent());
        LocaleHelper.setLocale(this, Prefs.getLocaleLang(getApplicationContext()));
        //mAuth = FirebaseAuth.getInstance();
       /* mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        };*/

        if(getIntent() != null) {
            title.setText(getIntent().getStringExtra("title"));
            subDivision = getIntent().getStringExtra("division_name");
        }

        if(GPSTrackerService.isRunning()){
            Intent intent = new Intent(SecondScheme.this, MapsActivity.class);
            intent.putExtra("form_data", Prefs.getFuzData(this));
            startActivity(intent);
        }

        Prefs.putFuzData(this, null);
        trackStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmpty();
            }
        });
        //mAuth.addAuthStateListener(mListener);

        data = new HashMap<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void isEmpty(){
        View focusView = null;
        boolean cancel = false;
        if (TextUtils.isEmpty(forestArea.getText())) {
            forestArea.setError(getString(R.string.error_invalid_field));
            focusView = forestArea;
            cancel = true;
        }

        if (TextUtils.isEmpty(SubforestArea.getText())) {
            SubforestArea.setError(getString(R.string.error_invalid_field));
            focusView = SubforestArea;
            cancel = true;
        }

        if (TextUtils.isEmpty(location.getText())) {
            location.setError(getString(R.string.error_invalid_field));
            focusView = location;
            cancel = true;
        }

        if (TextUtils.isEmpty(plantingYear.getText())) {
            plantingYear.setError(getString(R.string.error_invalid_field));
            focusView = plantingYear;
            cancel = true;
        }

        if (TextUtils.isEmpty(plantingPlants.getText())) {
            plantingPlants.setError(getString(R.string.error_invalid_field));
            focusView = plantingPlants;
            cancel = true;
        }

        if (TextUtils.isEmpty(locationHelper.getText())) {
            locationHelper.setError(getString(R.string.error_invalid_field));
            focusView = locationHelper;
            cancel = true;
        }

        if (TextUtils.isEmpty(beet.getText())) {
            beet.setError(getString(R.string.error_invalid_field));
            focusView = beet;
            cancel = true;
        }

        if (TextUtils.isEmpty(estimate_area.getText())) {
            estimate_area.setError(getString(R.string.error_invalid_field));
            focusView = estimate_area;
            cancel = true;
        }

        if (TextUtils.isEmpty(livePlants.getText())) {
            livePlants.setError(getString(R.string.error_invalid_field));
            focusView = livePlants;
            cancel = true;
        }


        if (TextUtils.isEmpty(khasraNo.getText())) {
            khasraNo.setError(getString(R.string.error_invalid_field));
            focusView = khasraNo;
            cancel = true;
        }


        if (TextUtils.isEmpty(officerName.getText())) {
            officerName.setError(getString(R.string.error_invalid_field));
            focusView = officerName;
            cancel = true;
        }

        if (TextUtils.isEmpty(plantingYearBetween.getText())) {
            plantingYearBetween.setError(getString(R.string.error_invalid_field));
            focusView = plantingYearBetween;
            cancel = true;
        }

        if (TextUtils.isEmpty(forestType.getText())) {
            forestType.setError(getString(R.string.error_invalid_field));
            focusView = forestType;
            cancel = true;
        }

        if (TextUtils.isEmpty(plantingType.getText())) {
            plantingType.setError(getString(R.string.error_invalid_field));
            focusView = plantingType;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            String formData = getString(R.string.forestArea) + " : " + forestArea.getText().toString()  + ", " +
                    getString(R.string.SubforestArea) + " : " + SubforestArea.getText().toString()  + ", " +
                    getString(R.string.location) + " : " + location.getText().toString()  + ", " +
                    getString(R.string.plantingYear) + " : " + plantingYear.getText().toString()  + ", " +
                    getString(R.string.plantingPlants) + " : " + plantingPlants.getText().toString()  + ", "+
                    getString(R.string.beet) + " : " + beet.getText().toString()  + ", " +
                    getString(R.string.location_helper) + " : " + locationHelper.getText().toString()  + ", " +
                    getString(R.string.estimate_area) + " : " + estimate_area.getText().toString()  + ", " +
                    getString(R.string.live_plants) + " : " + livePlants.getText().toString()  + ", " +
                    getString(R.string.khasra_no) + " : " + khasraNo.getText().toString() + " , " +
                    getString(R.string.year_between) + " : " + plantingYearBetween.getText().toString() + " , " +
                    getString(R.string.forest_type) + " : " + forestType.getText().toString() + " , " +
                    getString(R.string.office_name) + " : " + officerName.getText().toString() + " , " +
            getString(R.string.planting_type) + " : " + plantingType.getText().toString();


            data.put(getString(R.string.forestArea), forestArea.getText().toString());
            data.put(getString(R.string.SubforestArea) , SubforestArea.getText().toString());
            data.put(getString(R.string.plantingYear) , plantingYear.getText().toString());
            data.put( getString(R.string.plantingPlants) , plantingPlants.getText().toString());
            data.put(getString(R.string.beet) , beet.getText().toString());
            data.put(getString(R.string.location_helper) , locationHelper.getText().toString());
            data.put(getString(R.string.forest_type) , forestType.getText().toString());
            data.put(getString(R.string.estimate_area), estimate_area.getText().toString());
            data.put(getString(R.string.live_plants), livePlants.getText().toString());
            data.put(getString(R.string.platinghelp), plantinghelp.getText().toString());
            data.put(getString(R.string.khasra_no) , khasraNo.getText().toString());
            data.put(getString(R.string.year_between), plantingYearBetween.getText().toString());
            data.put(getString(R.string.office_name), officerName.getText().toString());
            data.put(getString(R.string.planting_type) , plantingType.getText().toString());

            Prefs.putFuzDataMap(this, data);

            Intent intent = new Intent(SecondScheme.this, MapsActivity.class);
            intent.putExtra("form_data", formData);
            intent.putExtra("department", getString(R.string.scheme_secon));
            intent.putExtra("division_name", subDivision);
            startActivity(intent);
        }
    }


    private void bindContent(Intent intent){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        forestArea = (EditText)findViewById(R.id.forestArea);
        SubforestArea = (EditText)findViewById(R.id.SubforestArea);
        location = (EditText)findViewById(R.id.location);
        plantingYear = (EditText)findViewById(R.id.plantingYear);
        plantingPlants = (EditText)findViewById(R.id.plantingPlants);
        plantingType = (EditText)findViewById(R.id.plantingType);


        beet = (EditText)findViewById(R.id.beet);
        locationHelper = (EditText)findViewById(R.id.location_helper);
        forestType = (EditText)findViewById(R.id.forestType);
        estimate_area = (EditText)findViewById(R.id.estimate_area);
        livePlants = (EditText)findViewById(R.id.livePlants);
        plantinghelp = (EditText)findViewById(R.id.plantinghelp);
        officerName = (EditText)findViewById(R.id.nameOfficer);
        khasraNo = (EditText)findViewById(R.id.khasra_no);
        plantingYearBetween = (EditText)findViewById(R.id.planting_year_between);


        title = (TextView)findViewById(R.id.detail_title);

        trackStartButton = (Button) findViewById(R.id.trackButton);

        if(intent != null) {
            intent.getStringExtra("title");
            toolbar.setTitle(intent.getStringExtra("title"));
        }else {
            toolbar.setTitle(getString(R.string.forest_track));
        }
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
