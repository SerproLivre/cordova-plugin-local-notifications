package de.appplant.cordova.plugin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import de.appplant.cordova.plugin.localnotification.LocalNotification;
import de.appplant.cordova.plugin.localnotification.LocalNotificationListener;
import de.appplant.cordova.plugin.service.NotificationService;

public class MainActivity extends AppCompatActivity {

    protected Button btnNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context ctx = this;

        /**
         * Register an event called when local notification was clicked by the user
         * <b>PS:</b> Now, if a notification was received when the app is in background or foreground
         */
        LocalNotification.on("click", new LocalNotificationListener() {
            @Override
            public void run(JSONObject data) {
                Intent intent = new Intent(ctx, AnotherActivity.class);
                startActivity(intent);
            }
        });

        btnNotify = findViewById(R.id.btnNotify);
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, NotificationService.class);
                ctx.startService(intent);
            }
        });
    }
}
