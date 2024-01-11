package com.example.finaltest.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.finaltest.R;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String ChannelId = "my_channel_id";
    private static final String ChannelName = "my_notification_channel";


    @Override
    public void onReceive(Context context, Intent intent) {
        var when = System.currentTimeMillis();

        var notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.createNotificationChannel(
                new NotificationChannel(
                        ChannelId,
                        ChannelName,
                        NotificationManager.IMPORTANCE_DEFAULT));

        var notificationBuilder = new NotificationCompat.Builder(context, ChannelId);
        var pendingIntent = getPendingIntent(context);
        notificationBuilder.setContentIntent(pendingIntent);

        notificationBuilder.setSmallIcon(R.drawable.notification_rocket_picture)
                .setContentTitle("Do you want to reach your goals?")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Check today's todos!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true).setWhen(when);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private PendingIntent getPendingIntent(Context context) {
        var notifyIntent = new Intent(context, HomeActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(
                context, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }
}
