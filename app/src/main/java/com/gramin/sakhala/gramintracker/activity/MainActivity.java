package com.gramin.sakhala.gramintracker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.gramin.sakhala.gramintracker.R;
import com.gramin.sakhala.gramintracker.helper.LocaleHelper;
import com.gramin.sakhala.gramintracker.service.GPSTrackerService;
import com.gramin.sakhala.gramintracker.util.Prefs;

public class MainActivity extends GPSTrackingBaseActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;

    private Toolbar toolbar;

    private EditText forestArea;
    private EditText officerName;
    private EditText location;
    private EditText compartmentNumber;
    private EditText plantingArea;
    private EditText plantingYear;
    private EditText plantingPlants;

    private Button trackStartButton;

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

        trackStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmpty();
            }
        });
        //mAuth.addAuthStateListener(mListener);

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

        if (TextUtils.isEmpty(officerName.getText())) {
            officerName.setError(getString(R.string.error_invalid_field));
            focusView = officerName;
            cancel = true;
        }

        if (TextUtils.isEmpty(location.getText())) {
            location.setError(getString(R.string.error_invalid_field));
            focusView = location;
            cancel = true;
        }

        if (TextUtils.isEmpty(compartmentNumber.getText())) {
            compartmentNumber.setError(getString(R.string.error_invalid_field));
            focusView = compartmentNumber;
            cancel = true;
        }

        if (TextUtils.isEmpty(plantingArea.getText())) {
            plantingArea.setError(getString(R.string.error_invalid_field));
            focusView = plantingArea;
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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            String formData = getString(R.string.forestArea) + " : " + forestArea.getText().toString()  + ", " +
                    getString(R.string.office_name) + " : " + officerName.getText().toString()  + ", " +
                    getString(R.string.location) + " : " + location.getText().toString()  + ", " +
                    getString(R.string.compartmentNo) + " : " + compartmentNumber.getText().toString()  + ", " +
                    getString(R.string.plantingArea) + " : " + plantingArea.getText().toString()  + ", " +
                    getString(R.string.plantingYear) + " : " + plantingYear.getText().toString()  + ", " +
                    getString(R.string.plantingPlants) + " : " + plantingPlants.getText().toString()  + ", ";
            Intent intent = new Intent(MainActivity.this, TrackingActivity.class);
            intent.putExtra("form_data", formData);
            startActivity(intent);
        }
    }


    private void bindContent(Intent intent){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        forestArea = (EditText)findViewById(R.id.forestArea);
        officerName = (EditText)findViewById(R.id.offcerName);
        location = (EditText)findViewById(R.id.location);
        compartmentNumber = (EditText)findViewById(R.id.compartmentNo);
        plantingArea = (EditText)findViewById(R.id.plantingArea);
        plantingYear = (EditText)findViewById(R.id.plantingYear);
        plantingPlants = (EditText)findViewById(R.id.plantingPlants);

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
