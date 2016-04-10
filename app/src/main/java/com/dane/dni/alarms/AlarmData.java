package com.dane.dni.alarms;

import com.dane.dni.common.data.DniDateTime;

/**
 * Created by Dane on 2/4/2016.
 */
public class AlarmData {

    private Integer month;
    private Integer day;
    private Integer shift;
    private Integer hour;
    private Integer quarter;
    private Integer minute;
    private Integer second;
    private boolean enabled;

    private int alarmId;

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
        return value == null ? "*" : value.toString();
    }

    public static AlarmData fromStringRepresentation(String rawData) {
        String[] parts = rawData.split(":");
        if (parts.length != 9) {
            throw new RuntimeException("Invalid alarm format");
        }
        return new AlarmData(
                fromStringValue(parts[0]),
                fromStringValue(parts[1]),
                fromStringValue(parts[2]),
                fromStringValue(parts[3]),
                fromStringValue(parts[4]),
                fromStringValue(parts[5]),
                fromStringValue(parts[6]),
                Boolean.valueOf(parts[7]),
                Integer.parseInt(parts[8]));
    }

    private static Integer fromStringValue(String value) {
        return value.equals("*") ? null : Integer.parseInt(value);
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

    public Integer getMonth() { return month; }

    public Integer getDay() { return day; }

    public Integer getShift() {
        return shift;
    }

    public Integer getHour() {
        return hour;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public Integer getMinute() {
        return minute;
    }

    public Integer getSecond() {
        return second;
    }

    public Integer getAlarmId() {
        return alarmId;
    }
}
