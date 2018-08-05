package com.gramin.sakhala.gramintracker.helper;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gramin.sakhala.gramintracker.callback.OnLoginTrigger;

/**
 * Created by atulsakhala on 01/08/18.
 */

public class FirebaseHelper {
    private static final String TAG = FirebaseHelper.class.getSimpleName();
    private static FirebaseAuth firebaseAuth;
}
