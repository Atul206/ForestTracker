package com.gramin.sakhala.gramintracker.service;

import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by atulsakhala on 05/08/18.
 */

public class JobUtil {

        // schedule the start of the service every 10 - 30 seconds
        public static void scheduleJob(Context context) {
            ComponentName serviceComponent = new ComponentName(context, JobScheduler.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            builder.setMinimumLatency(1 * 1000); // wait at least
            builder.setOverrideDeadline(3 * 1000); // maximum delay
            //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            //builder.setRequiresDeviceIdle(true); // device should be idle
            builder.setRequiresCharging(false); // we don't care if the device is charging or not
            //JobScheduler jobScheduler = context.getSystemService(JobScheduler.class.getName());
            //jobScheduler.schedule(builder.build());
        }


}
