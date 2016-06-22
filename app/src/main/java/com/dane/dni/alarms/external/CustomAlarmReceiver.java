package com.dane.dni.alarms.external;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.dane.dni.R;
import com.dane.dni.watch.Watch;

/**
 * Created by Dane on 5/25/2016.
 */
public class CustomAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("Be alarmed!")
                .setSmallIcon(R.drawable.notific_icn)
                .setContentText("The alarm fired!")
                .setAutoCancel(true);
        Intent resultIntent = new Intent(context, Watch.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.toString(intent.getIntExtra("com.dane.dni.alarmId", 0)),
                0, builder.build());
    }
}
