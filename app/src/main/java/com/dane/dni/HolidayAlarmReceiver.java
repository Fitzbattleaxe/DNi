package com.dane.dni;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ScaleDrawable;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Dane on 12/8/2015.
 */
public class HolidayAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DniHoliday holiday = (DniHoliday) intent.getParcelableExtra("com.dane.dni.holiday");
        String holidayName = holiday.getName();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(holidayName)
                .setSmallIcon(R.drawable.notific_icn)
                .setContentText("Happy holidays!")
                .setAutoCancel(true);
        Intent resultIntent = new Intent(context, Watch.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.notify(holidayName, 0, builder.build());

        boolean holidayAlarmsEnabled = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("holiday_alarms", false);

        // if alarms are still enabled, reschedule this one for the following year.
        if (holidayAlarmsEnabled) {
            AlarmManager alarmManager =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            DniDateTime dniDateTime = DniDateTime.now();
            DniDateTime holidayDateTime = DniDateTime.now(
                    dniDateTime.getHahrtee() + 1,
                    holiday.getVailee(), holiday.getYahr(), 0, 0, 0, 0, 0);
            Intent newIntent = new Intent(context, HolidayAlarmReceiver.class)
                    .putExtra("holiday", holiday)
                    .setAction("com.dane.dni.ACTION_NOTIFY_FOR_HOLIDAY");
            int intentId = intent.getIntExtra("com.dane.dni.intentId", 0);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(context, intentId, newIntent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    holidayDateTime.getSystemTimeInMillis(),
                    pendingIntent);
        }

    }
}
