package com.dane.dni;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlarmActivity extends Activity {

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
}
