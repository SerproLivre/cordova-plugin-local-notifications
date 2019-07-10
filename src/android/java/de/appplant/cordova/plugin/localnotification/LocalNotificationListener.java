package de.appplant.cordova.plugin.localnotification;

import org.json.JSONObject;

public interface LocalNotificationListener {
    void run(JSONObject data);
}
