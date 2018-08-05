package com.gramin.sakhala.gramintracker.callback;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by atulsakhala on 01/08/18.
 */

public interface OnLoginTrigger {
    void onSuccess(FirebaseUser user);
    void onFailure(String errorMessage);
}
