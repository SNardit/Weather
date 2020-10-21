package com.example.weather;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class ChangeConnectionReceiver extends BroadcastReceiver {

    private int messageId = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo currentNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

        assert currentNetworkInfo != null;
        if(currentNetworkInfo.isConnected()){
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.connected), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.not_connected), Toast.LENGTH_LONG).show();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.broadcast_receiver))
                .setContentText(context.getString(R.string.no_internet_connection));
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}
