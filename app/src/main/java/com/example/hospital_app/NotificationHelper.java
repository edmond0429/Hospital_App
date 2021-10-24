package com.example.hospital_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String chn1ID = "chn1ID";
    public static final String chn1Name = "channel 1";

    private NotificationManager mNotificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannel() {
        NotificationChannel channel1 = new NotificationChannel(chn1ID,chn1Name, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getNotificationManager().createNotificationChannel(channel1);
    }

    public NotificationManager getNotificationManager(){
        if(mNotificationManager == null){
            mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    //build a notification for alarm
    public NotificationCompat.Builder getChannel1Notification(String alarmtitle, String message){
        //create intent when user press the notification
        Intent resultIntent = new Intent(this,snoozePage.class);
        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chn1ID)
                .setContentTitle(alarmtitle)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_alarmring)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(1,builder.build());
        
        return builder;
    }
}
