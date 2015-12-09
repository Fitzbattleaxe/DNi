package com.dane.dni;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.View;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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
    long preferenceTimeDelta = 0;
    final Handler tickHandler = new Handler();

    List<DniHoliday> holidays;

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

            if (sharedPreferences.getBoolean("holiday_alarms", false)) {
                disableHolidayAlarms();
                enableHolidayAlarms();
            }
        }

        if (key.equals("holiday_alarms")) {
            if (sharedPreferences.getBoolean("holiday_alarms", false)) {
                enableHolidayAlarms();
            } else {
                disableHolidayAlarms();
            }
        }
    }

    private void disableHolidayAlarms() {
        Context context = this.getApplicationContext();
        for (int i = 0; i < holidays.size() ; i++) {
            Intent intent = new Intent(context, HolidayAlarmReceiver.class);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(context, i, intent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }

    private void enableHolidayAlarms() {
        List<Pair<DniHoliday, Long>> nextHolidayTimes = new ArrayList<>();
        for (DniHoliday holiday : holidays) {
            long currentHahr = dniDateTime.getHahrtee();
            DniDateTime holidayDateTime = DniDateTime.now(
                    currentHahr, holiday.getVailee(), holiday.getYahr(), 0, 0, 0, 0, 0);
            long currentHahrHolidayInMillis = holidayDateTime.getSystemTimeInMillis();
            if (currentHahrHolidayInMillis > dniDateTime.getSystemTimeInMillis()) {
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
        Context context = this.getApplicationContext();
        int intentId = 0;
        for (Pair<DniHoliday, Long> holidayPair : nextHolidayTimes) {
            Intent intent = new Intent(context, HolidayAlarmReceiver.class)
                    .putExtra("holiday_name", holidayPair.first.getName())
                    .setAction("com.dane.dni.ACTION_NOTIFY_FOR_HOLIDAY");
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(context, intentId++, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, holidayPair.second, pendingIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
