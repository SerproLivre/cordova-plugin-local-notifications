package de.appplant.cordova.plugin.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;

import de.appplant.cordova.plugin.service.NotificationJobService;

public class NotificationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleNotification();
        }
    }

    /**
     * Create a Job to show notifications in background
     * This serviçe is recommended like best practices to performance,
     * described in oficial Android "guideline"
     *
     * @link https://medium.com/google-developers/scheduling-jobs-like-a-pro-with-jobscheduler-286ef8510129
     * @link https://stackoverflow.com/questions/45692181/android-job-scheduler-schedule-job-to-execute-immediately-and-exactly-once
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleNotification() {

        PersistableBundle bundle = new PersistableBundle();

        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName component = new ComponentName(this, NotificationJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, component)
                .setMinimumLatency(4000)
                .setExtras(bundle)
                .build();

        jobScheduler.schedule(jobInfo);

    }

}
