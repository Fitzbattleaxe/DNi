package com.dane.dni.alarms.external;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.dane.dni.R;
import com.dane.dni.alarms.AlarmActivity;
import com.dane.dni.alarms.AlarmData;
import com.dane.dni.common.data.DniDateTime;
import com.dane.dni.watch.Watch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dane on 5/25/2016.
 */
public class CustomAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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

        boolean useTimeOffsetpreferenceTimeDelta =
                !preferences.getBoolean("system_time", false);
        long preferenceTimeDelta;
        if (useTimeOffsetpreferenceTimeDelta) {
            preferenceTimeDelta = preferences.getLong("custom_date_time", 0L);
        } else {
            preferenceTimeDelta = 0;
        }

        AlarmData alarmData = AlarmData.unmarshall(
                intent.getByteArrayExtra("com.dane.dni.alarmData"));

        Set<String> rawAlarmDataSet = preferences.getStringSet("custom_alarm_data",
                new HashSet<String>());
        Map<Integer, AlarmData> alarmDataMap = new HashMap<>();
        for (String rawAlarmData : rawAlarmDataSet) {
            AlarmData parsedAlarmData = AlarmData.fromStringRepresentation(rawAlarmData);
            alarmDataMap.put(parsedAlarmData.getAlarmId(), parsedAlarmData);
        }

        int alarmId = alarmData.getAlarmId();
        if (alarmDataMap.containsKey(alarmId)) {
            AlarmManager alarmManager =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            long time = System.currentTimeMillis();
            DniDateTime dniDateTime = DniDateTime.now(time + preferenceTimeDelta);
            AlarmActivity.registerAlarmWithOS(alarmDataMap.get(alarmId), dniDateTime,
                    preferenceTimeDelta, alarmManager, context);
        }
    }
}
