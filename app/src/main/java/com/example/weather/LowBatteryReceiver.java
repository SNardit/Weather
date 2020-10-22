package com.example.weather;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;


import androidx.core.app.NotificationCompat;

public class LowBatteryReceiver extends BroadcastReceiver {
    private int messageId = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Broadcast Receiver")
                .setContentText("Low battery");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());

    }
}
