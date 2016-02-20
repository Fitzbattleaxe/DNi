package com.dane.dni.alarms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.widget.ListView;

import com.dane.dni.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlarmActivity extends Activity implements DniTimePicker.DniTimePickerListener {

    private ListView alarmListView;
    private AlarmListAdapter alarmListAdapter;
    private List<AlarmData> alarmDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        initView();
    }

    private void initView() {
        alarmDataList = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> rawAlarmDataSet =
            new HashSet();
        rawAlarmDataSet.add("3:1:2:14:21:true");
        /*preferences.getStringSet("custom_alarm_data",
                new HashSet<String>());*/
        for (String rawAlarmData : rawAlarmDataSet) {
            alarmDataList.add(AlarmData.fromStringRepresentation(rawAlarmData));
        }
        alarmListView = (ListView) findViewById(R.id.alarmList);
        alarmListAdapter = new AlarmListAdapter(this, R.layout.alarm_list_item, alarmDataList);
        alarmListView.setAdapter(alarmListAdapter);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        DniTimePicker dniTimePicker = (DniTimePicker) dialog;
        AlarmData oldAlarmData = alarmListAdapter.getItem(dniTimePicker.getAlarmId());
        AlarmData newAlarmData = new AlarmData(
                dniTimePicker.getShift(),
                dniTimePicker.getHour(),
                dniTimePicker.getQuarter(),
                dniTimePicker.getMinute(),
                dniTimePicker.getSecond(),
                oldAlarmData.isEnabled());
        alarmListAdapter.insert(newAlarmData, dniTimePicker.getAlarmId());
        if (oldAlarmData.isEnabled()) {

        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
