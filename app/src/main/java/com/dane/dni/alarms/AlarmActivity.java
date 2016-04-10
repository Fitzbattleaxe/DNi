package com.dane.dni.alarms;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dane.dni.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AlarmActivity extends AppCompatActivity implements DniTimePicker.DniTimePickerListener {

    private ListView alarmListView;
    private AlarmListAdapter alarmListAdapter;
    private List<AlarmData> alarmDataList;

    private Typeface typeface;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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

        alarmListView = (ListView) findViewById(R.id.alarmList);
        alarmListAdapter = new AlarmListAdapter(
                this, R.layout.alarm_list_item, alarmDataList, getSupportFragmentManager(), this);
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
                    AlarmData newAlarmData = new AlarmData(
                            1, 1, 0, 0, 0, 0, 0, true, generateAlarmId());
                    alarmListAdapter.add(newAlarmData);
                    updateAlarmPreferences();
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

    public void deleteAlarm(int alarmId) {
        AlarmData oldAlarmData = null;
        for (AlarmData alarmData : alarmDataList) {
            if (alarmData.getAlarmId() == alarmId) {
                oldAlarmData = alarmData;
                break;
            }
        }
        alarmListAdapter.remove(oldAlarmData);
        updateAlarmPreferences();
    }

    public void setEnabled(int alarmId, boolean isEnabled) {
        AlarmData oldAlarmData = null;
        for (AlarmData alarmData : alarmDataList) {
            if (alarmData.getAlarmId() == alarmId) {
                oldAlarmData = alarmData;
                break;
            }
        }
        AlarmData newAlarmData = new AlarmData(
                oldAlarmData.getMonth(), oldAlarmData.getDay(),
                oldAlarmData.getShift(), oldAlarmData.getHour(), oldAlarmData.getQuarter(),
                oldAlarmData.getMinute(), oldAlarmData.getSecond(), isEnabled, alarmId);
        int alarmPosition = alarmListAdapter.getPosition(oldAlarmData);
        alarmListAdapter.insert(newAlarmData, alarmPosition);
        alarmListAdapter.remove(oldAlarmData);
        updateAlarmPreferences();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
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
        int alarmPosition = alarmListAdapter.getPosition(oldAlarmData);
        alarmListAdapter.insert(newAlarmData, alarmPosition);
        alarmListAdapter.remove(oldAlarmData);
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
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
