package com.gramin.sakhala.gramintracker.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.gramin.sakhala.gramintracker.R;
import com.gramin.sakhala.gramintracker.callback.OnLoginTrigger;
import com.gramin.sakhala.gramintracker.dto.PendingFileDto;
import com.gramin.sakhala.gramintracker.helper.FirebaseHelper;
import com.gramin.sakhala.gramintracker.helper.LocaleHelper;
import com.gramin.sakhala.gramintracker.service.GPSTrackerService;
import com.gramin.sakhala.gramintracker.service.UploadAlarmReceiver;
import com.gramin.sakhala.gramintracker.util.Prefs;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.gramin.sakhala.gramintracker.activity.MapsActivity.PENDING_POD_BROADCAST_REQUEST_CODE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnLoginTrigger {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_LOCATION_CONTACTS = 1;
    private static final int REQUEST_WRITE_STORAGE = 2;
    private static final int REQUEST_CAMERA = 3;

    private static final int RC_SIGN_IN = 1;

    private static final String TAG = FirebaseHelper.class.getSimpleName();

    private  GoogleSignInOptions gso;

    private GoogleApiClient mGoogleClientApi;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mListener;

    private Button language;

    private String ImagefileName;


    PendingIntent pendingIntent;
    AlarmManager manager;


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Button mEmailSignInButton;
    RelativeLayout firstScheme;
    RelativeLayout secondScheme;
    RelativeLayout thirdScheme;
    ImageView mEmailSignInGoogleIco;
    Activity activity;

    private OnLoginTrigger onLoginTrigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        onLoginTrigger = this;
        activity = this;
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        language = (Button) findViewById(R.id.language);
        populateAutoComplete();
        initAlaram();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    signIn();
                    return true;
                }
                return false;
            }
        });

        if(LocaleHelper.getLanguage(activity).equals("hi")){
            language.setText(getString(R.string.english));
            Prefs.putLocaleLang(activity ,"hi");
        }else{
            language.setText(getString(R.string.hindi));
            Prefs.putLocaleLang(activity, "en");
        }

        language.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LocaleHelper.getLanguage(activity).equals("hi")) {
                    LocaleHelper.setLocale(activity, "en");
                }else{
                    LocaleHelper.setLocale(activity, "hi");
                }

                //It is required to recreate the activity to reflect the change in UI.
                recreate();
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInGoogleIco = (ImageView) findViewById(R.id.google_icon);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        firstScheme = (RelativeLayout) findViewById(R.id.btn_first);

        secondScheme = (RelativeLayout) findViewById(R.id.btn_second);

        thirdScheme = (RelativeLayout) findViewById(R.id.btn_third);

        firstScheme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(LoginActivity.this, DivisionActivity.class);
                    intent.putExtra("title",getString(R.string.deparmanet_str));
                    startActivity(intent);
                }else{
                    Snackbar.make(findViewById(R.id.main_layout), "Please login !", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        secondScheme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(LoginActivity.this, DivisionActivity.class);
                    intent.putExtra("title",getString(R.string.scheme_secon));
                    startActivity(intent);
                }else{
                    Snackbar.make(findViewById(R.id.main_layout), "Please login !", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        thirdScheme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

        mListener = firebaseAuth -> {

            if(firebaseAuth.getCurrentUser() != null) {
                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //finish();
            }
        };
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        initGSignInstace();
        initGoogleClientApiInstace();


        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(mListener);

        if(mAuth.getCurrentUser() != null) {
            mEmailSignInButton.setVisibility(View.INVISIBLE);
            mEmailSignInButton.setEnabled(false);
            mEmailSignInGoogleIco.setVisibility(View.INVISIBLE);
        }

        if(GPSTrackerService.isRunning()){
            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
            intent.putExtra("title",getString(R.string.deparmanet_str));
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayLocationSettingsRequest(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        if (!mayRequestStorage()) {
            return;
        }

        if (!mayRequestLocation()) {
            return;
        }

        if (!mayRequestCamera()) {
            return;
        }



        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(CAMERA)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                        }
                    });
        } else {
            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
        }

        return false;
    }

    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CONTACTS);
        }

        return false;
    }

    private boolean mayRequestStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                        }
                    });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

        return false;
    }

    private boolean mayRequestContacts(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }

        if (requestCode == REQUEST_LOCATION_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }

        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            signInUserWithEmailAndPassword(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    signInUserWithEmailAndPassword(mEmail, mPassword);
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            createUserWithEmailAndPassword(mEmail, mPassword);
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void createUserWithEmailAndPassword(String email, String password){
    }

    private void signInUserWithEmailAndPassword(String email, String password){
    }

    @Override
    public void onSuccess(FirebaseUser user) {
        Log.d(TAG, "Successfully login");
    }

    @Override
    public void onFailure(String errorMessage) {
        Log.d(TAG, errorMessage);
        showProgress(false);
        mPasswordView.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
    }


    public void initGSignInstace(){
        if(null == gso) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
        }
    }

    public void initGoogleClientApiInstace(){
        if(null == mGoogleClientApi){
            mGoogleClientApi = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    Snackbar.make(findViewById(R.id.main_layout), "Something went wrong!!.", Snackbar.LENGTH_SHORT).show();

                }
            })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleClientApi);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        }


        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            File forestDir = new File(Environment.getExternalStorageDirectory() + "/forest_tracker/data/cache/image/");
            if (!forestDir.exists()) {
                forestDir.mkdirs();
            }
            ImagefileName = "";
            ImagefileName = ImagefileName + "assets" + "_" + DateTime.now() + ".jpg";
            new BackGroundImageTask(imageBitmap).execute();

        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        if(account == null || account.getId() == null) {
            Snackbar.make(findViewById(R.id.main_layout), "Please sign in first", Snackbar.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Success.", Snackbar.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                            //finish();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(getString(R.string.login))
                .setMessage(user.getEmail() + " login successfully !")
                .setIcon(R.drawable.tree_nav_ico)
                .show();

        mEmailSignInButton.setVisibility(View.INVISIBLE);
        mEmailSignInButton.setEnabled(false);
        mEmailSignInGoogleIco.setVisibility(View.INVISIBLE);
    }

    public void displayLocationSettingsRequest(final Context context) {
        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(TAG, "All location settings are satisfied.");
                        googleApiClient.disconnect();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Toast.makeText(HomeActivity.this, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ", Toast.LENGTH_SHORT).show();
                        try {
                            status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            //Toast.makeText(HomeActivity.this, "PendingIntent unable to execute request.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Toast.makeText(HomeActivity.this, "Location settings are inadequate, and cannot be fixed here. Dialog not created.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
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


}

