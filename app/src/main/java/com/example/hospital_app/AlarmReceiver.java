package com.example.hospital_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //get bundle
        Bundle bundle = intent.getBundleExtra("bundle");
        // get the string alarmtitle and time
        String title = (String)bundle.getSerializable("alarmtitle");
        String time = (String)bundle.getSerializable("time");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        //pass title and time for the notification
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title,time);
        notificationHelper.getNotificationManager().notify(1,nb.build());
    }
}
