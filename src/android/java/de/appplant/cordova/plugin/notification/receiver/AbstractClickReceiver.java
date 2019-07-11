/*
 * Apache 2.0 License
 *
 * Copyright (c) Sebastian Katzer 2017
 *
 * This file contains Original Code and/or Modifications of Original Code
 * as defined in and that are subject to the Apache License
 * Version 2.0 (the 'License'). You may not use this file except in
 * compliance with the License. Please obtain a copy of the License at
 * http://opensource.org/licenses/Apache-2.0/ and read it before using this
 * file.
 *
 * The Original Code and all software distributed under the License are
 * distributed on an 'AS IS' basis, WITHOUT WARRANTY OF ANY KIND, EITHER
 * EXPRESS OR IMPLIED, AND APPLE HEREBY DISCLAIMS ALL SUCH WARRANTIES,
 * INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, QUIET ENJOYMENT OR NON-INFRINGEMENT.
 * Please see the License for the specific language governing rights and
 * limitations under the License.
 */

package de.appplant.cordova.plugin.notification.receiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import de.appplant.cordova.plugin.notification.Manager;
import de.appplant.cordova.plugin.notification.Notification;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static de.appplant.cordova.plugin.notification.action.Action.CLICK_ACTION_ID;
import static de.appplant.cordova.plugin.notification.action.Action.EXTRA_ID;

/**
 * Abstract content receiver activity for local notifications. Creates the
 * local notification and calls the event functions for further proceeding.
 *
 * <b>PS:</b> Inherit from {@link android.app.Service} class, insteadof {@link android.app.IntentService}
 * to starts this service in background/foreground.
 *
 * @link https://stackoverflow.com/questions/6422319/start-service-from-notification
 */
abstract public class AbstractClickReceiver extends JobIntentService {

    // Holds a reference to the intent to handle.
    private Intent intent;


//    public AbstractClickReceiver() {
//
//    }
//
////    @Override
////    public int onStartCommand(Intent intent, int flags, int startId) {
////
////        onHandleIntent(intent);
////        return START_STICKY;
////    }
////
////    @Override
////    public IBinder onBind(Intent intent) {
////        // We don't provide binding, so return null
////        return null;
////    }

    /**
     * Called when local notification was clicked to launch the main intent.
     *
     * <b>PS:</b> This method is only present in class IntentService
     * but, it is still here for migration purpposes
     *
     * @see #onStartCommand(Intent, int, int)
     */
    protected void onHandleIntent(Intent intent) {
        this.intent        = intent;

        if (intent == null)
            return;

        Bundle bundle      = intent.getExtras();
        Context context    = getApplicationContext();

        if (bundle == null)
            return;

        int toastId        = bundle.getInt(Notification.EXTRA_ID);
        Notification toast = Manager.getInstance(context).get(toastId);

        onClick(toast, bundle);
        this.intent = null;
    }

    /**
     * Called when local notification was clicked by the user.
     *
     * @param notification Wrapper around the local notification.
     * @param bundle The bundled extras.
     */
    abstract public void onClick (Notification notification, Bundle bundle);

    /**
     * The invoked action.
     */
    protected String getAction() {
        return getIntent().getExtras().getString(EXTRA_ID, CLICK_ACTION_ID);
    }

    /**
     * Getter for the received intent.
     */
    protected Intent getIntent() {
        return intent;
    }

    /**
     * Launch main intent from package.
     */
    protected void launchApp() {
        Context context = getApplicationContext();
        String pkgName  = context.getPackageName();

        Intent intent = context
                .getPackageManager()
                .getLaunchIntentForPackage(pkgName);

        if (intent == null)
            return;

        intent.addFlags(
              FLAG_ACTIVITY_REORDER_TO_FRONT
            | FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }

}
