package com.example.rajztanfolyam;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "Rajztanfolyam app";
    private int notId = 0;

    private Context mContext;
    private NotificationManager mManager;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Rajztanfolyam app", NotificationManager.IMPORTANCE_HIGH);

        channel.enableVibration(true);
        channel.setDescription("Értesítés a rajztanfolyam appból");
        this.mManager.createNotificationChannel(channel);
    }

    public void sendNotification(String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Rajztanfolyam app")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        this.mManager.notify(notId, builder.build());
    }

}
