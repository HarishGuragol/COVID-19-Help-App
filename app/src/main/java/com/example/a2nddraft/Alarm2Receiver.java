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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Alarm2Receiver extends BroadcastReceiver {

    Context mcontext;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        String aadtr = intent.getStringExtra("ADDR");

        mcontext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_MAX);
        }

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, "CHANNEL_1");
        NotificationCompat.Builder notification_2 =
                new NotificationCompat.Builder(context, "CHANNEL_1");
        NotificationCompat.Builder notification_3 =
                new NotificationCompat.Builder(context, "CHANNEL_1");

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notification
                .setSmallIcon(R.drawable.mainicon) // can use any other icon
                .setContentTitle("REMINDER!")
                .setContentText("Your will Expire in 5 mins!")
                .setSound(sound)
                .setVibrate(new long[] {Notification.DEFAULT_VIBRATE})
                .setPriority(NotificationCompat.PRIORITY_MAX);

        notification_2
                .setSmallIcon(R.drawable.mainicon) // can use any other icon
                .setContentTitle("REMINDER!")
                .setContentText("Your Pass is Expired")
                .setSound(sound)
                .setVibrate(new long[] {Notification.DEFAULT_VIBRATE})
                .setPriority(NotificationCompat.PRIORITY_MAX);

        notification_3
                .setSmallIcon(R.drawable.mainicon) // can use any other icon
                .setContentTitle("Things to Do")
                .setContentText("Take a bath and cleanse everything you bought")
                .setSound(sound)
                .setVibrate(new long[] {Notification.DEFAULT_VIBRATE})
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());

        try {
            Thread.sleep(1000*60*5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        notificationManager.notify(1, notification_2.build());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(aadtr);
        ref.removeValue();
        /*Query q = ref.equalTo(aadtr);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap:dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        try {
            Thread.sleep(1000*60*2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        notificationManager.notify(1, notification_3.build());

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
