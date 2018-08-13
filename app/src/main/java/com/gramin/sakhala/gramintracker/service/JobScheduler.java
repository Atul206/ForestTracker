package com.gramin.sakhala.gramintracker.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

/**
 * Created by atulsakhala on 05/08/18.
 */

public class JobScheduler extends JobService {

    private static final String TAG = "SyncService";

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //Intent service = new Intent(getApplicationContext(), .class);
        //getApplicationContext().startService(null);
        //Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }
}
