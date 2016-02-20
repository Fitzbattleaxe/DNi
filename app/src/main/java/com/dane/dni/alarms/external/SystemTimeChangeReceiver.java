package com.dane.dni.alarms.external;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.dane.dni.common.data.DniDateTime;
import com.dane.dni.common.data.DniHoliday;
import com.dane.dni.common.utils.HolidayXmlParser;
import com.dane.dni.R;
import com.dane.dni.watch.Watch;

import java.util.List;

/**
 * Created by Dane on 12/10/2015.
 */
public class SystemTimeChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.TIME_SET")) {
            boolean holidayAlarmsEnabled = PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("holiday_alarms", false);
            if (holidayAlarmsEnabled) {
                try {
                    List<DniHoliday> holidays = new HolidayXmlParser().getHolidays(
                            context.getResources().openRawResource(R.raw.holidays));
                    AlarmManager alarmManager =
                            (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    Watch.disableHolidayAlarms(holidays, alarmManager, context);

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
