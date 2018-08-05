package com.gramin.sakhala.gramintracker.helper;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by atulsakhala on 01/08/18.
 */

public class GoogleSignHelper {

    private static GoogleSignInOptions gso;
    private static String RequestIdToken = "test";

    private static GoogleApiClient mGoogleApiClient;
    public static GoogleSignInOptions getGSignInstace(){
        if(null == gso) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(RequestIdToken)
                    .requestEmail()
                    .build();
        }

        return gso;
    }

}
