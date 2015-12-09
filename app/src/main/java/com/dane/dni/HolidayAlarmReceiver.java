package com.dane.dni;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Dane on 12/8/2015.
 */
public class HolidayAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String holidayName = intent.getStringExtra("holiday_name");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(holidayName)
                .setSmallIcon(R.drawable.ic_alarm_notification)
                .setContentText("Happy holidays!")
                .setAutoCancel(true);
        Intent resultIntent = new Intent(context, Watch.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.notify(holidayName, 0, builder.build());
    }
}
