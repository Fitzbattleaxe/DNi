package com.dane.dni.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dane.dni.R;
import com.dane.dni.alarms.external.CustomAlarmReceiver;
import com.dane.dni.common.data.DniDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AlarmActivity extends AppCompatActivity implements DniTimePicker.DniTimePickerListener {

    private ListView alarmListView;
    private AlarmListAdapter alarmListAdapter;
    private List<AlarmData> alarmDataList;

    private Typeface typeface;

    private SharedPreferences preferences;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        alarmManager =
                (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        initView();
    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        alarmDataList = new ArrayList<>();
        Set<String> rawAlarmDataSet = preferences.getStringSet("custom_alarm_data",
                new HashSet<String>());
        for (String rawAlarmData : rawAlarmDataSet) {
            try {
                alarmDataList.add(AlarmData.fromStringRepresentation(rawAlarmData));
            } catch (RuntimeException e) {

            }
        }

        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/EBGaramond-Regular.ttf");

        final FragmentManager fm = getSupportFragmentManager();
        alarmListView = (ListView) findViewById(R.id.alarmList);
        alarmListAdapter = new AlarmListAdapter(
                this, R.layout.alarm_list_item, alarmDataList, fm, this);
        alarmListView.setAdapter(alarmListAdapter);

        CheckBox holidayAlarmCheckbox = (CheckBox) findViewById(R.id.enableHolidaysCheck);
        holidayAlarmCheckbox.setChecked(preferences.getBoolean("holiday_alarms", false));
        holidayAlarmCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox holidayAlarmCheckbox = (CheckBox) v;
                boolean isChecked = holidayAlarmCheckbox.isChecked();
                preferences.edit().putBoolean("holiday_alarms", isChecked).apply();
            }
        });
        ((TextView) findViewById(R.id.holidayAlarmsEnabledText)).setTypeface(typeface);

        View newAlarmButton = findViewById(R.id.newAlarm);
        newAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmDataList.size() < 50) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("alarmId", generateAlarmId());
                    bundle.putInt("month", 0);
                    bundle.putInt("day", 0);
                    bundle.putInt("shift", 0);
                    bundle.putInt("hour", 0);
                    bundle.putInt("quarter", 0);
                    bundle.putInt("minute", 0);
                    bundle.putInt("second", 0);
                    bundle.putBoolean("isNewAlarm", true);
                    DniTimePicker dniTimePicker = DniTimePicker.newInstance(bundle);
                    dniTimePicker
                            .setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme);
                    dniTimePicker.setArguments(bundle);
                    dniTimePicker.show(fm, "AlarmDialogFragment");
                }
            }
        });
        ((TextView) findViewById(R.id.newAlarmButtonText)).setTypeface(typeface);
    }

    private int generateAlarmId() {
        int randomId = new Random().nextInt(1000);
        Set<Integer> alarmIds = new HashSet<>();
        for (AlarmData alarmData : alarmDataList) {
            alarmIds.add(alarmData.getAlarmId());
        }
        while (alarmIds.contains(randomId)) {
            randomId = ++randomId % 1000;
        }
        return randomId;
    }

    @Override
    public void onAlarmSet(DialogFragment dialog) {
        DniTimePicker dniTimePicker = (DniTimePicker) dialog;
        int alarmId = dniTimePicker.getAlarmId();
        AlarmData oldAlarmData = null;
        for (AlarmData alarmData : alarmDataList) {
            if (alarmData.getAlarmId() == alarmId) {
                oldAlarmData = alarmData;
                break;
            }
        }
        AlarmData newAlarmData = new AlarmData(
                dniTimePicker.getMonth(),
                dniTimePicker.getDay(),
                dniTimePicker.getShift(),
                dniTimePicker.getHour(),
                dniTimePicker.getQuarter(),
                dniTimePicker.getMinute(),
                dniTimePicker.getSecond(),
                oldAlarmData != null
                    ? oldAlarmData.isEnabled()
                    : true,
                alarmId);
        int alarmPosition;
        if (oldAlarmData != null) {
            alarmPosition = alarmListAdapter.getPosition(oldAlarmData);
            alarmListAdapter.remove(oldAlarmData);
            deregisterAlarmWithOS(oldAlarmData,
                    alarmManager, this);
        }  else {
            alarmPosition = alarmListAdapter.getCount();
        }
        alarmListAdapter.insert(newAlarmData, alarmPosition);

        boolean useTimeOffsetpreferenceTimeDelta =
                !PreferenceManager.getDefaultSharedPreferences(this)
                        .getBoolean("system_time", false);
        long preferenceTimeDelta;
        if (useTimeOffsetpreferenceTimeDelta) {
            preferenceTimeDelta = PreferenceManager.getDefaultSharedPreferences(this)
                    .getLong("custom_date_time", 0L);
        } else {
            preferenceTimeDelta = 0;
        }
        ComponentName alarmReceiver = new ComponentName(this, CustomAlarmReceiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(alarmReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        long time = System.currentTimeMillis();
        DniDateTime dniDateTime = DniDateTime.now(time + preferenceTimeDelta);
        registerAlarmWithOS(newAlarmData, dniDateTime, preferenceTimeDelta, alarmManager, this);
        updateAlarmPreferences();
    }

    private void updateAlarmPreferences() {
        Set<String> alarmSet = new HashSet<>();
        for (AlarmData alarmData : alarmDataList) {
            alarmSet.add(alarmData.toStringRepresentation());
        }
        preferences.edit().putStringSet("custom_alarm_data", alarmSet).apply();
    }

    @Override
    public void onAlarmDelete(DialogFragment dialog) {
        DniTimePicker dniTimePicker = (DniTimePicker) dialog;
        int alarmId = dniTimePicker.getAlarmId();
        AlarmData oldAlarmData = null;
        for (AlarmData alarmData : alarmDataList) {
            if (alarmData.getAlarmId() == alarmId) {
                oldAlarmData = alarmData;
                break;
            }
        }
        if (oldAlarmData == null) {
            return;
        }
        alarmListAdapter.remove(oldAlarmData);
        deregisterAlarmWithOS(oldAlarmData,
                alarmManager, this);

        if (alarmDataList.isEmpty()) {
            ComponentName alarmReceiver = new ComponentName(this, CustomAlarmReceiver.class);
            PackageManager pm = this.getPackageManager();
            pm.setComponentEnabledSetting(alarmReceiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }

        updateAlarmPreferences();
    }

    public static void deregisterAllAlarmsWithOs(
            Iterable<AlarmData> alarmDataList,
            AlarmManager alarmManager,
            Context context) {
        for (AlarmData alarm : alarmDataList) {
            deregisterAlarmWithOS(alarm, alarmManager, context);
        }
    }

    public static void registerAllAlarmsWithOs(
            Iterable<AlarmData> alarmDataList,
            DniDateTime dniDateTime,
            long preferenceTimeDelta,
            AlarmManager alarmManager,
            Context context) {
        for (AlarmData alarm : alarmDataList) {
            registerAlarmWithOS(alarm, dniDateTime, preferenceTimeDelta, alarmManager, context);
        }
    }

    public static void deregisterAlarmWithOS(
            AlarmData alarmData,
            AlarmManager alarmManager,
            Context context) {
        Intent intent = new Intent(context, CustomAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmData.getAlarmId(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public static void registerAlarmWithOS(
            AlarmData alarmData,
            DniDateTime dniDateTime,
            long preferenceTimeDelta,
            AlarmManager alarmManager,
            Context context) {
        long nextAlarmTimeInMillis =
                getNextAlarmTimeInMillis(alarmData, dniDateTime, preferenceTimeDelta);

        Intent intent = new Intent(context, CustomAlarmReceiver.class)
                .putExtra("com.dane.dni.alarmData", AlarmData.marshall(alarmData))
                .putExtra("com.dane.dni.alarmId", alarmData.getAlarmId())
                .setAction("com.dane.dni.ACTION_NOTIFY_FOR_CUSTOM_ALARM");
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, alarmData.getAlarmId(), intent, 0);
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP, nextAlarmTimeInMillis, pendingIntent);
    }

    @VisibleForTesting
    static long getNextAlarmTimeInMillis(AlarmData alarmData, DniDateTime dniDateTime,
                                         long preferenceTimeDelta) {
        Map<DniDateTime.Unit, Integer> completeTime =
                new HashMap<>();

        List<DniDateTime.Unit> unitsToProcess = new LinkedList<>();
        for (DniDateTime.Unit unit : AlarmData.REVERSE_SORTED_ALARM_UNITS) {
            unitsToProcess.add(unit);
        }

        List<DniDateTime.Unit> unsetUnits = new LinkedList<>();
        unsetUnits.add(0, DniDateTime.Unit.HAHR);
        completeTime.put(
                DniDateTime.Unit.HAHR, dniDateTime.getZeroIndexedNum(DniDateTime.Unit.HAHR));
        while (!unitsToProcess.isEmpty()) {
            DniDateTime.Unit unit = unitsToProcess.remove(0);
            int alarmUnitTime = alarmData.getNum(unit);
            int curUnitTime = dniDateTime.getZeroIndexedNum(unit);
            if (alarmUnitTime > -1) {
                if (curUnitTime <= alarmUnitTime) {
                    completeTime.put(unit, alarmUnitTime);
                } else {
                    completeTime.put(unit, alarmUnitTime);
                    while (!unitsToProcess.isEmpty()) {
                        unit = unitsToProcess.remove(0);
                        alarmUnitTime = alarmData.getNum(unit);
                        if (alarmUnitTime > -1) {
                            completeTime.put(unit, alarmUnitTime);
                        } else {
                            completeTime.put(unit, 0);
                        }
                    }
                }
            } else {
                unsetUnits.add(0, unit);
                completeTime.put(unit, curUnitTime);
            }
        }

        DniDateTime nextAlarmTime;
        long nextAlarmTimeInMillis = 0;
        while (!unsetUnits.isEmpty()) {
            DniDateTime.Unit unsetUnit = unsetUnits.remove(0);
            int unitAlarmTime = completeTime.get(unsetUnit);
            boolean timeFound = false;
            for (int i = 0; i < dniDateTime.getMax(unsetUnit) - unitAlarmTime; i++) {
                completeTime.put(unsetUnit, unitAlarmTime + i);
                nextAlarmTime = DniDateTime.now(
                        completeTime.get(DniDateTime.Unit.HAHR),
                        completeTime.get(DniDateTime.Unit.VAILEE),
                        completeTime.get(DniDateTime.Unit.YAHR),
                        completeTime.get(DniDateTime.Unit.GAHRTAHVO),
                        completeTime.get(DniDateTime.Unit.PAHRTAHVO),
                        completeTime.get(DniDateTime.Unit.TAHVO),
                        completeTime.get(DniDateTime.Unit.GORAHN),
                        completeTime.get(DniDateTime.Unit.PRORAHN));
                nextAlarmTimeInMillis = nextAlarmTime.getSystemTimeInMillis();

                if (nextAlarmTimeInMillis
                        > dniDateTime.getSystemTimeInMillis() + 1000) {
                    timeFound = true;
                    break;
                }
            }
            if (timeFound) {
                break;
            }
            completeTime.put(unsetUnit, 0);
        }
        return nextAlarmTimeInMillis - preferenceTimeDelta;
    }
}
