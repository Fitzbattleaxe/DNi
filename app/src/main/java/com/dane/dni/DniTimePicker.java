package com.dane.dni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

/**
 * Created by Dane on 1/21/2016.
 */
public class DniTimePicker extends FrameLayout {

    NumberPicker shiftPicker;
    NumberPicker hourPicker;
    NumberPicker quarterPicker;
    NumberPicker minutePicker;
    NumberPicker secondPicker;

    public DniTimePicker(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dni_time_picker, this, true);

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
    }

    public void setTime(int shift, int hour, int quarter, int minute, int second) {
        shiftPicker.setValue(shift);
        hourPicker.setValue(hour);
        quarterPicker.setValue(quarter);
        minutePicker.setValue(minute);
        secondPicker.setValue(second);
    }

    private void initPicker(NumberPicker picker, int min, int max) {
        picker.setWrapSelectorWheel(true);
        picker.setMinValue(min);
        picker.setMaxValue(max);
    }
}
