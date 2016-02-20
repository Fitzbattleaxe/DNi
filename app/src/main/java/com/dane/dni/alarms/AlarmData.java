package com.dane.dni.alarms;

/**
 * Created by Dane on 2/4/2016.
 */
public class AlarmData {

    private int shift;
    private int hour;
    private int quarter;
    private int minute;
    private int second;
    private boolean enabled;

    public AlarmData(int shift, int hour, int quarter, int minute, int second, boolean enabled) {
        this.shift = shift;
        this.hour = hour;
        this.quarter = quarter;
        this.minute = minute;
        this.second = second;
        this.enabled = enabled;
    }

    public String toStringRepresentation() {
        return String.format("%d:%d:%d:%d:%d:%b", shift, hour, quarter, minute, second, enabled);
    }

    public static AlarmData fromStringRepresentation(String rawData) {
        String[] parts = rawData.split(":");
        return new AlarmData(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]),
                Boolean.valueOf(parts[0]));
    }

    public boolean isEnabled() {
        return enabled;
    }

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
}
