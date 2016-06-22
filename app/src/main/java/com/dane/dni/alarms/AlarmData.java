package com.dane.dni.alarms;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.dane.dni.common.data.DniDateTime;

/**
 * Created by Dane on 2/4/2016.
 */
public class AlarmData implements Parcelable {

    public static final Parcelable.Creator<AlarmData> CREATOR
            = new Parcelable.Creator<AlarmData>() {
        public AlarmData createFromParcel(Parcel in) {
            return new AlarmData(in);
        }

        public AlarmData[] newArray(int size) {
            return new AlarmData[size];
        }
    };

    private Integer month;
    private Integer day;
    private Integer shift;
    private Integer hour;
    private Integer quarter;
    private Integer minute;
    private Integer second;
    private boolean enabled;

    private int alarmId;

    public static DniDateTime.Unit[] SORTED_ALARM_UNITS =
            {
                    DniDateTime.Unit.PRORAHN,
                    DniDateTime.Unit.GORAHN,
                    DniDateTime.Unit.TAHVO,
                    DniDateTime.Unit.PAHRTAHVO,
                    DniDateTime.Unit.GAHRTAHVO,
                    DniDateTime.Unit.YAHR,
                    DniDateTime.Unit.VAILEE,
            };

    public static DniDateTime.Unit[] REVERSE_SORTED_ALARM_UNITS =
            {
                    DniDateTime.Unit.VAILEE,
                    DniDateTime.Unit.YAHR,
                    DniDateTime.Unit.GAHRTAHVO,
                    DniDateTime.Unit.PAHRTAHVO,
                    DniDateTime.Unit.TAHVO,
                    DniDateTime.Unit.GORAHN,
                    DniDateTime.Unit.PRORAHN,
            };

    public AlarmData(
            Integer month,
            Integer day,
            Integer shift,
            Integer hour,
            Integer quarter,
            Integer minute,
            Integer second,
            boolean enabled,
            int alarmId) {
            this.month = month;
            this.day = day;
            this.shift = shift;
            this.hour = hour;
            this.quarter = quarter;
            this.minute = minute;
            this.second = second;
            this.enabled = enabled;
            this.alarmId = alarmId;
    }

    public AlarmData(Parcel in) {
        this(in.readString());
    }

    public AlarmData(String stringRepresentation) {
        String[] parts = stringRepresentation.split(":");
        if (parts.length != 9) {
            throw new RuntimeException("Invalid alarm format");
        }
        this.month = fromStringValue(parts[0]);
        this.day = fromStringValue(parts[1]);
        this.shift = fromStringValue(parts[2]);
        this.hour = fromStringValue(parts[3]);
        this.quarter = fromStringValue(parts[4]);
        this.minute = fromStringValue(parts[5]);
        this.second = fromStringValue(parts[6]);
        this.enabled = Boolean.valueOf(parts[7]);
        this.alarmId = Integer.parseInt(parts[8]);
    }

    public String toStringRepresentation() {
        return String.format("%s:%s:%s:%s:%s:%s:%s:%b:%d",
                toStringValue(month),
                toStringValue(day),
                toStringValue(shift),
                toStringValue(hour),
                toStringValue(quarter),
                toStringValue(minute),
                toStringValue(second),
                enabled,
                alarmId);
    }

    private static String toStringValue(Integer value) {
        return value == -1 ? "*" : value.toString();
    }

    public static AlarmData fromStringRepresentation(String rawData) {
        return new AlarmData(rawData);
    }

    private static Integer fromStringValue(String value) {
        return value.equals("*") ? -1 : Integer.parseInt(value);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Integer getNum(DniDateTime.Unit unit) {
        switch (unit) {
            case VAILEE:
                return getMonth();
            case YAHR:
                return getDay();
            case GAHRTAHVO:
                return getShift();
            case PAHRTAHVO:
                return getHour();
            case TAHVO:
                return getQuarter();
            case GORAHN:
                return getMinute();
            case PRORAHN:
                return getSecond();
            default:
                return -1;
        }
    }

    public int getMonth() { return month; }

    public int getDay() { return day; }

    public int getShift() {
        return shift;
    }

    public int getHour() {
        return hour;
    }

    public int getQuarter() {
        return quarter;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getAlarmId() {
        return alarmId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toStringRepresentation());
    }
}
