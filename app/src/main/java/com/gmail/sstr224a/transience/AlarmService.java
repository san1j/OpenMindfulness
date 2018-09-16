package com.gmail.sstr224a.transience;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class AlarmService extends BroadcastReceiver {

    // on receiving the intent at the specified, time, display a notification to the user
    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("default", "channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("description");
            notificationManager.createNotificationChannel(channel);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.ic_track_changes_black_24dp)
                .setContentTitle("Hey there!")
                .setContentText("Time for some happy thoughts")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(0, notificationBuilder.build());

    }
}
