package de.appplant.cordova.plugin.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

import de.appplant.cordova.plugin.R;
import de.appplant.cordova.plugin.localnotification.ClickReceiver;
import de.appplant.cordova.plugin.notification.Options;
import de.appplant.cordova.plugin.notification.action.Action;
import de.appplant.cordova.plugin.notification.receiver.ClickBroadcastReceiver;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class NotificationService extends IntentService {

    protected NotificationManager notificationManager;
    protected long[] vibrationPattern = new long[]{100, 200, 300, 400, 500};
    protected Bundle defaulExtras = new Bundle();

    // Holds a reference to the intent to handle.
    protected Intent intent;

    // To generate unique request codes
    protected final Random random = new Random();

    private static final String CHANNEL_ID = "default";
    private static final String GROUP_TAG = "LocalNotification";
    private static final int NOTIFICATION_ID = 100089;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotificationService() {
        super(NotificationService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        defaulExtras.putString("title", "Test");
        defaulExtras.putString("text", "A basic test message");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            addChannel();
        }

        if (notificationManager == null) {

            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE); //NotificationManagerCompat.from(getBaseContext());
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent == null)
            return;

        this.intent = intent;

        int amount = this.intent.getIntExtra("amount", 1);
        if (amount > 1) {
            for (int i = 0; i < amount; i++) {
                show();
            }
        } else {

            show();
        }

    }

    /**
     * @link https://developer.android.com/training/notify-user/build-notification
     */
    protected void show() {

        if (this.intent != null) {
            Bundle customExtras = this.intent.getExtras();
            if (customExtras != null && !customExtras.isEmpty()) {
                defaulExtras.putAll(customExtras);
            }
        }

        Intent intent = new Intent(this, ClickBroadcastReceiver.class)
                .putExtra(Action.EXTRA_ID, Action.CLICK_ACTION_ID)
                .putExtra(Options.EXTRA_LAUNCH, true);
//                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int reqCode = random.nextInt();

        PendingIntent contentIntent = PendingIntent.getBroadcast(
                this, reqCode, intent, FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(defaulExtras.getString("title"))
                .setContentText(defaulExtras.getString("text"))
                .setGroup(GROUP_TAG)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void addChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.RED);
        channel.setVibrationPattern(vibrationPattern);

        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }
}
