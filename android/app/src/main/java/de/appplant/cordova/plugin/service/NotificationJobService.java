package de.appplant.cordova.plugin.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        PersistableBundle extras = params.getExtras();
        Bundle bundle = new Bundle(extras);

        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtras(bundle);

        startService(intent);

        jobFinished(params, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        Intent intent = new Intent(this, NotificationService.class);

        intent.putExtra("title", "ERROR");
        intent.putExtra("text", "The Job was stopped!");

        startService(intent);

        return false;
    }
}
