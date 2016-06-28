package com.dane.dni.alarms.external;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dane.dni.alarms.AlarmActivity;
import com.dane.dni.alarms.AlarmData;
import com.dane.dni.common.data.DniDateTime;
import com.dane.dni.common.data.DniHoliday;
import com.dane.dni.common.utils.HolidayXmlParser;
import com.dane.dni.R;
import com.dane.dni.watch.Watch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Dane on 12/10/2015.
 */
public class AlarmBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean holidayAlarmsEnabled = preferences.getBoolean("holiday_alarms", false);
            boolean useTimeOffsetPreferenceTimeDelta =
                    !PreferenceManager.getDefaultSharedPreferences(context)
                            .getBoolean("system_time", false);
            long preferenceTimeDelta;
            if (useTimeOffsetPreferenceTimeDelta) {
                preferenceTimeDelta = PreferenceManager.getDefaultSharedPreferences(context)
                        .getLong("custom_date_time", 0L);
            } else {
                preferenceTimeDelta = 0;
            }
            AlarmManager alarmManager =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            long time = System.currentTimeMillis();
            DniDateTime dniDateTime = DniDateTime.now(time + preferenceTimeDelta);
            if (holidayAlarmsEnabled) {
                try {
                    List<DniHoliday> holidays = new HolidayXmlParser().getHolidays(
                            context.getResources().openRawResource(R.raw.holidays));
                    Watch.enableHolidayAlarms(holidays,
                            dniDateTime,
                            preferenceTimeDelta,
                            alarmManager,
                            context);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            Set<String> rawAlarmDataSet = preferences.getStringSet("custom_alarm_data",
                    new HashSet<String>());
            List<AlarmData> alarmDataList = new ArrayList<>();
            for (String rawAlarmData : rawAlarmDataSet) {
                alarmDataList.add(AlarmData.fromStringRepresentation(rawAlarmData));
            }
            AlarmActivity.registerAllAlarmsWithOs(alarmDataList, dniDateTime,
                    preferenceTimeDelta, alarmManager, context);
        }
    }
}
