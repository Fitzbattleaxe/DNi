package com.dane.dni.alarms.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dane.dni.R;
import com.dane.dni.common.data.DniDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dane on 4/16/2016.
 */
public class DniAlarmUnitPicker extends FrameLayout {
    private static final String UNSET_OPTIONS = "-";

    DniDateTime.Unit unit;
    List<String> options;
    Spinner picker;

    public DniAlarmUnitPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DniAlarmUnitPicker,
                0, 0);

        unit = DniDateTime.Unit.valueOf(a.getString(R.styleable.DniAlarmUnitPicker_unit));
        String[] optionsParts = a.getString(R.styleable.DniAlarmUnitPicker_options)
                .split(",");

        options = new ArrayList<>();
        options.add(UNSET_OPTIONS);
        options.addAll(Arrays.asList(optionsParts));

        inflate(context, R.layout.dni_alarm_unit_picker, this);

        TextView unitName = (TextView) this.findViewById(R.id.unitName);
        unitName.setText(unit.getDisplayName());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(options);

        picker = (Spinner) this.findViewById(R.id.unitPicker);
        picker.setAdapter(spinnerAdapter);

    }

    public void setValue(String value) {
        int index = 0;
        for (int i = 0; i < options.size(); i++) {
            if (value.equals(options.get(i))) {
                index = i;
            }
        }
        picker.setSelection(index);
    }

    public int getValue() {
        String item = (String) picker.getSelectedItem();
        if (item.equals(UNSET_OPTIONS)) {
            return -1;
        }
        return Integer.parseInt(item);
    }
}
