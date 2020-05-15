package com.example.a2nddraft;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    Context mcontext;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        mcontext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_MAX);
        }

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, "CHANNEL_1");

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notification
                .setSmallIcon(R.drawable.mainicon) // can use any other icon
                .setContentTitle("REMINDER!")
                .setContentText("Wash your hands thoroughly and have some Water")
                .setSound(sound)
                .setVibrate(new long[] {Notification.DEFAULT_VIBRATE})
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void makeNotificationChannel(String channel_1, String example_channel, int importanceDefault) {
        NotificationChannel channel = new NotificationChannel(channel_1, example_channel, importanceDefault);
        channel.setVibrationPattern(new long[] {Notification.DEFAULT_VIBRATE});
        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);

        NotificationManager notificationManager = (NotificationManager)mcontext.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }
}
