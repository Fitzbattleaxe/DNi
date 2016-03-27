package com.dane.dni.watch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Pair;
import android.view.View;

import com.dane.dni.alarms.AlarmActivity;
import com.dane.dni.alarms.external.AlarmBootReceiver;
import com.dane.dni.preferences.SettingsActivity;
import com.dane.dni.watch.views.utils.DampedHarmonicOscillator;
import com.dane.dni.common.data.DniDateTime;
import com.dane.dni.common.data.DniHoliday;
import com.dane.dni.alarms.external.HolidayAlarmReceiver;
import com.dane.dni.common.utils.HolidayXmlParser;
import com.dane.dni.R;
import com.dane.dni.watch.views.HolidayButton;
import com.dane.dni.watch.views.HolidayDialogFragment;
import com.dane.dni.watch.views.MenuButton;
import com.dane.dni.watch.views.PolarView;
import com.dane.dni.watch.views.TimeDisplay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Watch extends FragmentActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    DniDateTime dniDateTime;
    DniDateTime offsetDniDateTime;
    TimeDisplay yearDisplay;
    TimeDisplay monthDisplay;
    TimeDisplay timeDisplay;
    PolarView watch1;
    PolarView watch2;
    MenuButton menuButton;
    HolidayButton holidayButton;
    long preferenceTimeDelta = 0;
    final Handler tickHandler = new Handler();

    ArrayList<DniHoliday> holidays;

    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        dniDateTime = new DniDateTime();
        offsetDniDateTime = new DniDateTime();

        yearDisplay = (TimeDisplay) findViewById(R.id.year_display);
        yearDisplay.setClock(dniDateTime);
        List<DniDateTime.Unit> displayUnits = new LinkedList<DniDateTime.Unit>();
        displayUnits.add(DniDateTime.Unit.HAHR);
        yearDisplay.setUnits(displayUnits, "%d DE");

        monthDisplay = (TimeDisplay) findViewById(R.id.month_display);
        monthDisplay.setClock(dniDateTime);
        displayUnits = new LinkedList<DniDateTime.Unit>();
        displayUnits.add(DniDateTime.Unit.NAMED_VAILEE);
        displayUnits.add(DniDateTime.Unit.YAHR);
        displayUnits.add(DniDateTime.Unit.GAHRTAHVO);
        displayUnits.add(DniDateTime.Unit.PAHRTAHVO);
        monthDisplay.setUnits(displayUnits, "%s %02d, %d : %d");

        timeDisplay = (TimeDisplay) findViewById(R.id.time_display);
        timeDisplay.setClock(dniDateTime);
        displayUnits = new LinkedList<DniDateTime.Unit>();
        displayUnits.add(DniDateTime.Unit.TAHVO);
        displayUnits.add(DniDateTime.Unit.GORAHN);
        displayUnits.add(DniDateTime.Unit.PRORAHN);
        timeDisplay.setUnits(displayUnits, "%d : %02d : %02d");

        Float[] easingValues =
                new DampedHarmonicOscillator(-1.0f, 0.01f, 0.00f, 1.0f)
                        .getCachedFunctionValues(0, 800);

        watch2 = (PolarView)  findViewById(R.id.watch2);
        watch2.setUpEasing(easingValues, 800);
        watch2.addClock(dniDateTime);
        watch2.addOffsetClock(offsetDniDateTime);

        watch1 = (PolarView)  findViewById(R.id.watch1);
        watch1.setUpEasing(easingValues, 800);
        watch1.addClock(dniDateTime);
        watch1.addOffsetClock(offsetDniDateTime);

        menuButton = (MenuButton) findViewById(R.id.menuButton);
        holidayButton = (HolidayButton) findViewById(R.id.holidayButton);

        holidayButton.setOnClickListener(holidayOnClickListener);

        tickHandler.post(myRunnable);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        boolean useTimeOffsetpreferenceTimeDelta =
                !PreferenceManager.getDefaultSharedPreferences(this)
                        .getBoolean("system_time", false);

        if (useTimeOffsetpreferenceTimeDelta) {
            preferenceTimeDelta = PreferenceManager.getDefaultSharedPreferences(this)
                    .getLong("custom_date_time", 0L);
        }

        try {
            holidays = new HolidayXmlParser().getHolidays(
                    this.getApplicationContext().getResources().openRawResource(R.raw.holidays));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        alarmManager =
                (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    final View.OnClickListener holidayOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        FragmentManager fm = getSupportFragmentManager();
                        HolidayDialogFragment holidayDialogFragment =
                                HolidayDialogFragment.newInstance(dniDateTime, holidays);
                    holidayDialogFragment
                            .setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme);
                    Bundle bundle = new Bundle();
                    holidayDialogFragment.show(fm, "holiday_list");
                    }
            };

    final Runnable myRunnable = new Runnable() {
        public void run() {
            try {
                long time = System.currentTimeMillis();
                dniDateTime.setTimeInMillis(time + preferenceTimeDelta);
                offsetDniDateTime.setTimeInMillis(time + preferenceTimeDelta + 140);
                yearDisplay.updateDisplay();
                monthDisplay.updateDisplay();
                timeDisplay.updateDisplay();
                watch1.updateTime();
                watch2.updateTime();
            } catch (Exception e) {
                System.exit(-1);
            }
            tickHandler.postDelayed(this, 20);
        }
    };

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("dni_nums")) {
            boolean useDniNums = sharedPreferences.getBoolean("dni_nums", true);
            watch1.useDniNums(useDniNums);
            watch2.useDniNums(useDniNums);
        }

        if (key.equals("system_time") || key.equals("custom_date_time")) {
            boolean useTimeOffsetpreferenceTimeDelta =
                    !PreferenceManager.getDefaultSharedPreferences(this)
                            .getBoolean("system_time", false);

            if (useTimeOffsetpreferenceTimeDelta) {
                preferenceTimeDelta = PreferenceManager.getDefaultSharedPreferences(this)
                        .getLong("custom_date_time", 0L);
            } else {
                preferenceTimeDelta = 0;
            }
            long time = System.currentTimeMillis();
            dniDateTime.setTimeInMillis(time + preferenceTimeDelta);
            if (sharedPreferences.getBoolean("holiday_alarms", false)) {
                disableHolidayAlarms(holidays, alarmManager, this);
                enableHolidayAlarms(
                        holidays, dniDateTime, preferenceTimeDelta, alarmManager, this);
            }
        }

        if (key.equals("holiday_alarms")) {
            ComponentName alarmReceiver = new ComponentName(this, HolidayAlarmReceiver.class);
            ComponentName bootReceiver = new ComponentName(this, AlarmBootReceiver.class);
            PackageManager pm = this.getPackageManager();
            if (sharedPreferences.getBoolean("holiday_alarms", false)) {
                pm.setComponentEnabledSetting(alarmReceiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                pm.setComponentEnabledSetting(bootReceiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                enableHolidayAlarms(
                        holidays, dniDateTime, preferenceTimeDelta, alarmManager, this);
            } else {
                pm.setComponentEnabledSetting(alarmReceiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                pm.setComponentEnabledSetting(bootReceiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                disableHolidayAlarms(holidays, alarmManager, this);
            }
        }
    }

    public static void disableHolidayAlarms(
            List<DniHoliday> holidays,
            AlarmManager alarmManager,
            Context context) {
        for (int i = 0; i < holidays.size() ; i++) {
            Intent intent = new Intent(context, HolidayAlarmReceiver.class);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(context, i, intent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }

    public static void enableHolidayAlarms(
            List<DniHoliday> holidays,
            DniDateTime dniDateTime,
            long preferenceTimeDelta,
            AlarmManager alarmManager,
            Context context) {
        List<Pair<DniHoliday, Long>> nextHolidayTimes = new ArrayList<>();
        for (DniHoliday holiday : holidays) {
            long currentHahr = dniDateTime.getHahrtee();
            DniDateTime holidayDateTime = DniDateTime.now(
                    currentHahr, holiday.getVailee(), holiday.getYahr(), 0, 0, 0, 0, 0);
            long currentHahrHolidayInMillis = holidayDateTime.getSystemTimeInMillis();

            if (currentHahrHolidayInMillis
                    > dniDateTime.getSystemTimeInMillis()) {
                nextHolidayTimes.add(
                        Pair.create(holiday, currentHahrHolidayInMillis - preferenceTimeDelta));
            } else {
                holidayDateTime = DniDateTime.now(
                        currentHahr + 1, holiday.getVailee(), holiday.getYahr(), 0, 0, 0, 0, 0);
                nextHolidayTimes.add(
                        Pair.create(holiday,
                                holidayDateTime.getSystemTimeInMillis() - preferenceTimeDelta));
            }
        }
        int holidayId = 0;
        for (Pair<DniHoliday, Long> holidayPair : nextHolidayTimes) {
            Intent intent = new Intent(context, HolidayAlarmReceiver.class)
                    .putExtra("com.dane.dni.holiday", holidayPair.first)
                    .putExtra("com.dane.dni.holidayId", holidayId)
                    .setAction("com.dane.dni.ACTION_NOTIFY_FOR_HOLIDAY");
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(context, holidayId, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, holidayPair.second, pendingIntent);
            holidayId++;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
