package com.example.court.finalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;

/**
 * Created by Kevin on 11/30/2017.
 */

class NotificationHelper extends ContextWrapper {
    private Context context = getApplicationContext();
    private NotificationManager notifManager;
    public static final String CHANNEL_ONE_ID = "1234";
    public static final String CHANNEL_ONE_NAME = "Channel One";
    Intent intent = new Intent(context, MainActivity.class);
    Intent intent1 = new Intent(context, MainActivity.class);
    //int requestID = (int) System.currentTimeMillis();   // Unique requestID to differentiate between various notification with same NotifId
    int flags = PendingIntent.FLAG_CANCEL_CURRENT;      // Cancel old intent and create new one
    PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, flags);
    PendingIntent iIntent = PendingIntent.getActivity(this, 1, intent, flags);
    PendingIntent sIntent = PendingIntent.getActivity(this, 1, intent1, flags);

    // Create your notification channels
    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                CHANNEL_ONE_NAME, notifManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(notificationChannel);
    }

    // Create the notification
    public Notification.Builder getNotification1(String title, String body) {
        return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)    // Change this so it snoozes
                .addAction(R.mipmap.ic_launcher, "Off", iIntent)
                .addAction(R.mipmap.ic_launcher, "Snooze", iIntent)
                .setAutoCancel(true);
    }

    public void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

    // Send your notifications to the NotificationManager system service
    private NotificationManager getManager() {
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notifManager;
    }
}