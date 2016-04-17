package com.dane.dni.alarms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.dane.dni.R;
import com.dane.dni.alarms.views.DniAlarmUnitPicker;

/**
 * Created by Dane on 1/21/2016.
 */
public class DniTimePicker extends DialogFragment {

    public interface DniTimePickerListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    DniTimePickerListener listener;

    DniAlarmUnitPicker monthPicker;
    DniAlarmUnitPicker dayPicker;
    DniAlarmUnitPicker shiftPicker;
    DniAlarmUnitPicker hourPicker;
    DniAlarmUnitPicker quarterPicker;
    DniAlarmUnitPicker minutePicker;
    DniAlarmUnitPicker secondPicker;

    int alarmId;
    Bundle bundle;

 /*   @Override
    public void setArguments(Bundle bundle) {
        this.bundle = bundle;
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_setter, null);

        bundle = getArguments();
        monthPicker = (DniAlarmUnitPicker) view.findViewById(R.id.monthPicker);
        dayPicker = (DniAlarmUnitPicker) view.findViewById(R.id.dayPicker);
        shiftPicker = (DniAlarmUnitPicker) view.findViewById(R.id.shiftPicker);
        hourPicker = (DniAlarmUnitPicker) view.findViewById(R.id.hourPicker);
        quarterPicker = (DniAlarmUnitPicker) view.findViewById(R.id.quarterPicker);
        minutePicker = (DniAlarmUnitPicker) view.findViewById(R.id.minutePicker);
        secondPicker = (DniAlarmUnitPicker) view.findViewById(R.id.secondPicker);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set alarm time")
                .setView(view)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(DniTimePicker.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeClick(DniTimePicker.this);
                    }
                });
        setTime(bundle.getInt("month"),
                bundle.getInt("day"),
                bundle.getInt("shift"),
                bundle.getInt("hour"),
                bundle.getInt("quarter"),
                bundle.getInt("minute"),
                bundle.getInt("second"));
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (DniTimePickerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DniTimePickerListener");
        }
    }

    public void setTime(int month, int day, int shift, int hour, int quarter, int minute,
                        int second) {
        monthPicker.setValue(Integer.toString(month));
        dayPicker.setValue(Integer.toString(day));
        shiftPicker.setValue(Integer.toString(shift));
        hourPicker.setValue(Integer.toString(hour));
        quarterPicker.setValue(Integer.toString(quarter));
        minutePicker.setValue(Integer.toString(minute));
        secondPicker.setValue(Integer.toString(second));
    }

    public Integer getMonth() { return monthPicker.getValue(); }

    public Integer getDay() { return dayPicker.getValue(); }

    public Integer getShift() {
        return shiftPicker.getValue();
    }

    public Integer getHour() {
        return hourPicker.getValue();
    }

    public Integer getQuarter() {
        return quarterPicker.getValue();
    }

    public Integer getMinute() {
        return minutePicker.getValue();
    }

    public Integer getSecond() {
        return secondPicker.getValue();
    }

    public int getAlarmId() {
        return bundle.getInt("alarmId");
    }
}
