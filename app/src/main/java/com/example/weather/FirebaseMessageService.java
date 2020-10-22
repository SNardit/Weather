package com.example.weather;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FirebaseMessageService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(getString(R.string.push_message), Objects.requireNonNull(Objects.requireNonNull(remoteMessage.getNotification()).getBody()));
        String title = remoteMessage.getNotification().getTitle();
        if (title == null){
            title = getString(R.string.push_message);
        }
        String text = remoteMessage.getNotification().getBody();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(getString(R.string.push_message), getString(R.string.token) + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }


}
