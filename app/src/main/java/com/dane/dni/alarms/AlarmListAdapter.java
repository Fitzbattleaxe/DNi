package com.dane.dni.alarms;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.dane.dni.R;

import java.util.List;

/**
 * Created by Dane on 2/4/2016.
 */
public class AlarmListAdapter extends ArrayAdapter<AlarmData> {

    private final LayoutInflater mInflater;
    private int mResource;
    private FragmentManager fm;
    private final AlarmActivity alarmActivity;

    public AlarmListAdapter(Context context, int resource, List<AlarmData> objects,
                            FragmentManager fm, AlarmActivity alarmActivity) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        this.fm = fm;
        this.alarmActivity = alarmActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    private View createViewFromResource(LayoutInflater inflater, int position, View convertView,
                                        ViewGroup parent, int resource) {
        View view;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        final AlarmData alarmData = getItem(position);

        TextView alarmDisplay = (TextView) view.findViewById(R.id.alarmDisplay);
        alarmDisplay.setText(String.format("%d : %d : %d : %02d : %02d",
                alarmData.getShift(), alarmData.getHour(), alarmData.getQuarter(),
                alarmData.getMinute(), alarmData.getSecond()));

        Switch enabledSwitch = (Switch) view.findViewById(R.id.enableAlarm);
        enabledSwitch.setChecked(alarmData.isEnabled());

        enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarmActivity.setEnabled(alarmData.getAlarmId(), isChecked);
            }
        });

        Button deleteButton = (Button) view.findViewById(R.id.alarmDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmActivity.deleteAlarm(alarmData.getAlarmId());
            }
        });

        alarmDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DniTimePicker dniTimePicker = new DniTimePicker();
                Bundle bundle = new Bundle();
                bundle.putInt("alarmId", alarmData.getAlarmId());
                bundle.putInt("shift", alarmData.getShift());
                bundle.putInt("hour", alarmData.getHour());
                bundle.putInt("quarter", alarmData.getQuarter());
                bundle.putInt("minute", alarmData.getMinute());
                bundle.putInt("second", alarmData.getSecond());
                dniTimePicker.show(fm, "AlarmDialogFragment");
                dniTimePicker.setArguments(bundle);
            }
        });

        return view;
    }
}
