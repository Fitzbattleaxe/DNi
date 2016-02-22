package com.dane.dni.alarms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.dane.dni.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AlarmActivity extends FragmentActivity implements DniTimePicker.DniTimePickerListener {

    private ListView alarmListView;
    private AlarmListAdapter alarmListAdapter;
    private List<AlarmData> alarmDataList;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        initView();
    }

    private void initView() {
        alarmDataList = new ArrayList<>();
        Set<String> rawAlarmDataSet = preferences.getStringSet("custom_alarm_data",
                new HashSet<String>());
        for (String rawAlarmData : rawAlarmDataSet) {
            alarmDataList.add(AlarmData.fromStringRepresentation(rawAlarmData));
        }
        alarmListView = (ListView) findViewById(R.id.alarmList);
        alarmListAdapter = new AlarmListAdapter(
                this, R.layout.alarm_list_item, alarmDataList, getSupportFragmentManager(), this);
        alarmListView.setAdapter(alarmListAdapter);

        Button newAlarmButton = (Button) findViewById(R.id.newAlarm);
        newAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmDataList.size() < 50) {
                    AlarmData newAlarmData = new AlarmData(
                            0, 0, 0, 0, 0, true, generateAlarmId());
                    alarmListAdapter.add(newAlarmData);
                    updateAlarmPreferences();
                }
            }
        });
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
