package com.capacitorjs.plugins.localnotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.getcapacitor.Logger;

public class NotificationActionWithoutOpeningAppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        var localNotificationsPlugin = LocalNotificationsPlugin.getLocalNotificationsInstance();
        if (localNotificationsPlugin != null) {
            localNotificationsPlugin.handleNotificationActionWithoutOpeningAppNotificationPerformed(intent);
        }else {
            Log.w(Logger.tags("LN"), "LocalNotificationsPlugin instance is null, cannot handle notification action");
            // TODO what to do if the application is not running? launch activity?
        }
    }
}
