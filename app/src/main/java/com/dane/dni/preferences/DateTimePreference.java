package com.dane.dni.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.dane.dni.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Dane on 11/2/2015.
 */
public class DateTimePreference extends DialogPreference {
    private Calendar calendar;
    private TimePicker timePicker = null;
    private DatePicker datePicker = null;

    public DateTimePreference(Context ctxt) {
        this(ctxt, null);
    }

    public DateTimePreference(Context ctxt, AttributeSet attrs) {
        this(ctxt, attrs, android.R.attr.dialogPreferenceStyle);
    }

    public DateTimePreference(Context ctxt, AttributeSet attrs, int defStyle) {
        super(ctxt, attrs, defStyle);
        setDialogLayoutResource(R.layout.date_time_picker);
        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
        calendar = new GregorianCalendar();
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        datePicker = (DatePicker) v.findViewById(R.id.datePicker);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }

    void updateTime() {
        calendar.setTimeInMillis(System.currentTimeMillis() + getPersistedLong(0L));
        setSummary(getSummary());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
            calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            calendar.set(Calendar.MONTH, datePicker.getMonth());
            calendar.set(Calendar.YEAR, datePicker.getYear());

            setSummary(getSummary());
            long delta = calendar.getTimeInMillis() - System.currentTimeMillis();
            if (callChangeListener(delta)) {
                persistLong(delta);
                notifyChanged();
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        long currentTimeInMillis = System.currentTimeMillis();
        long delta = 0;
        if (defaultValue != null) {
            delta = Long.parseLong((String) defaultValue);
        }
        if (restoreValue) {
            calendar.setTimeInMillis(currentTimeInMillis + getPersistedLong(delta));
        } else {
            calendar.setTimeInMillis(delta + currentTimeInMillis);
            persistLong(delta);
        }
        setSummary(getSummary());
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    public CharSequence getSummary() {
        if (calendar == null) {
            return null;
        }
        Date date = new Date(calendar.getTimeInMillis());
        return DateFormat.getTimeFormat(getContext()).format(date)
                + " "
                + DateFormat.getDateFormat(getContext()).format(date);
    }
}
