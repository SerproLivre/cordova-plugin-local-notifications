package de.appplant.cordova.plugin.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.appplant.cordova.plugin.localnotification.ClickReceiver;

public class ClickBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ClickReceiver.enqueueWork(context, intent);
    }
}
