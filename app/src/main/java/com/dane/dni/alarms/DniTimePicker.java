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

/**
 * Created by Dane on 1/21/2016.
 */
public class DniTimePicker extends DialogFragment {

    public interface DniTimePickerListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    DniTimePickerListener listener;

    NumberPicker shiftPicker;
    NumberPicker hourPicker;
    NumberPicker quarterPicker;
    NumberPicker minutePicker;
    NumberPicker secondPicker;

    int alarmId;
    Bundle bundle;

 /*   @Override
    public void setArguments(Bundle bundle) {
        this.bundle = bundle;
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dni_time_picker, null);

        bundle = getArguments();
        shiftPicker = (NumberPicker) view.findViewById(R.id.shiftPicker);
        hourPicker = (NumberPicker) view.findViewById(R.id.hourPicker);
        quarterPicker = (NumberPicker) view.findViewById(R.id.quarterPicker);
        minutePicker = (NumberPicker) view.findViewById(R.id.minutePicker);
        secondPicker = (NumberPicker) view.findViewById(R.id.secondPicker);

        initPicker(shiftPicker, 0, 4);
        initPicker(hourPicker, 0, 4);
        initPicker(quarterPicker, 0, 4);
        initPicker(minutePicker, 0, 24);
        initPicker(secondPicker, 0, 24);

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
        setTime(bundle.getInt("shift"),
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

    public void setTime(int shift, int hour, int quarter, int minute, int second) {
        shiftPicker.setValue(shift);
        hourPicker.setValue(hour);
        quarterPicker.setValue(quarter);
        minutePicker.setValue(minute);
        secondPicker.setValue(second);
    }

    public int getShift() {
        return shiftPicker.getValue();
    }

    public int getHour() {
        return hourPicker.getValue();
    }

    public int getQuarter() {
        return quarterPicker.getValue();
    }

    public int getMinute() {
        return minutePicker.getValue();
    }

    public int getSecond() {
        return secondPicker.getValue();
    }

    public int getAlarmId() {
        return bundle.getInt("alarmId");
    }

    private void initPicker(NumberPicker picker, int min, int max) {
        picker.setWrapSelectorWheel(true);
        picker.setMinValue(min);
        picker.setMaxValue(max);
    }
}
