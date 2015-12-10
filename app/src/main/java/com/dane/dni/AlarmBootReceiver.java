package com.dane.dni;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import java.util.List;

/**
 * Created by Dane on 12/10/2015.
 */
public class AlarmBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            boolean holidayAlarmsEnabled = PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("holiday_alarms", false);
            if (holidayAlarmsEnabled) {
                try {
                    boolean useTimeOffsetpreferenceTimeDelta =
                            !PreferenceManager.getDefaultSharedPreferences(context)
                                    .getBoolean("system_time", false);
                    long preferenceTimeDelta;
                    if (useTimeOffsetpreferenceTimeDelta) {
                        preferenceTimeDelta = PreferenceManager.getDefaultSharedPreferences(context)
                                .getLong("custom_date_time", 0L);
                    } else {
                        preferenceTimeDelta = 0;
                    }

                    List<DniHoliday> holidays = new HolidayXmlParser().getHolidays(
                            context.getResources().openRawResource(R.raw.holidays));
                    AlarmManager alarmManager =
                            (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Watch.enableHolidayAlarms(holidays,
                            DniDateTime.now(),
                            preferenceTimeDelta,
                            alarmManager,
                            context);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
