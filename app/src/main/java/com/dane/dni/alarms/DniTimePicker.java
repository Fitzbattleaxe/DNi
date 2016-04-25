package com.dane.dni.alarms;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.dane.dni.R;
import com.dane.dni.alarms.views.DniAlarmUnitPicker;

/**
 * Created by Dane on 1/21/2016.
 */
public class DniTimePicker extends DialogFragment implements View.OnClickListener {

    public interface DniTimePickerListener {
        public void onAlarmSet(DialogFragment dialog);
        public void onAlarmDelete(DialogFragment dialog);
    }

    DniTimePickerListener listener;

    DniAlarmUnitPicker monthPicker;
    DniAlarmUnitPicker dayPicker;
    DniAlarmUnitPicker shiftPicker;
    DniAlarmUnitPicker hourPicker;
    DniAlarmUnitPicker quarterPicker;
    DniAlarmUnitPicker minutePicker;
    DniAlarmUnitPicker secondPicker;

    View setButton;
    View deleteButton;

    Bundle bundle;

    public static DniTimePicker newInstance(Bundle bundle) {
        DniTimePicker frag = new DniTimePicker();
        frag.setArguments(bundle);
        return frag;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder
//                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        listener.onAlarmSet(DniTimePicker.this);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        listener.onAlarmDelete(DniTimePicker.this);
//                    }
//                });
//        return builder.create();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_setter, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow()
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bundle = getArguments();
        monthPicker = (DniAlarmUnitPicker) view.findViewById(R.id.monthPicker);
        dayPicker = (DniAlarmUnitPicker) view.findViewById(R.id.dayPicker);
        shiftPicker = (DniAlarmUnitPicker) view.findViewById(R.id.shiftPicker);
        hourPicker = (DniAlarmUnitPicker) view.findViewById(R.id.hourPicker);
        quarterPicker = (DniAlarmUnitPicker) view.findViewById(R.id.quarterPicker);
        minutePicker = (DniAlarmUnitPicker) view.findViewById(R.id.minutePicker);
        secondPicker = (DniAlarmUnitPicker) view.findViewById(R.id.secondPicker);

        View cancelButton = view.findViewById(R.id.alarmSetterClose);
        cancelButton.setOnClickListener(this);

        setButton = view.findViewById(R.id.alarmSetterSet);
        setButton.setOnClickListener(this);

        deleteButton = view.findViewById(R.id.alarmDelete);
        deleteButton.setOnClickListener(this);

        setTime(bundle.getInt("month"),
                bundle.getInt("day"),
                bundle.getInt("shift"),
                bundle.getInt("hour"),
                bundle.getInt("quarter"),
                bundle.getInt("minute"),
                bundle.getInt("second"));

        return view;
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

    @Override
    public void onClick(View v) {
        if (setButton.equals(v)) {
            listener.onAlarmSet(DniTimePicker.this);
        }
        if (deleteButton.equals(v)) {
            listener.onAlarmDelete(DniTimePicker.this);
        }
        getDialog().dismiss();
    }
}
