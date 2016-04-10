package com.dane.dni.common.data;

import java.util.Objects;

/**
 * Created by Dane on 8/16/2015.
 */
public class DniDateTime implements Comparable<DniDateTime> {
    private static final double SECS_TO_PRORAHN = 1.392857389;
    private static final double EPOCH_OFFSET = 218082198186.06136478628394597259088094624739790930598997;

    private static final int ONE_GORAHN = 25;
    private static final int ONE_TAHVO = 25 * ONE_GORAHN;
    private static final int ONE_PAHRTAHVO = 5 * ONE_TAHVO;
    private static final int ONE_GAHRTAHVO = 5 *  ONE_PAHRTAHVO;
    private static final int ONE_YAHR = 5 * ONE_GAHRTAHVO;
    private static final int ONE_VAILEE = 29 * ONE_YAHR;
    private static final int ONE_HAHR = 10 * ONE_VAILEE;

    private static final String[] VAILEE_NAMES = {
            "Leefo",
            "Leebro",
            "Leesahn",
            "Leetar",
            "Leevot",
            "Leevofo",
            "Leevobro",
            "Leevosahn",
            "Leevotar",
            "Leenovoo"
    };

    public enum Vailee {
        LEEFO,
        LEEBRO,
        LEESAHN,
        LEETAR,
        LEEVOT,
        LEEVOFO,
        LEEVOBRO,
        LEEVOSAHN,
        LEEVOTAR,
        LEENOVOO
    }

    public enum Unit {
        HAHR("Hahr"),
        VAILEE("Vailee"),
        NAMED_VAILEE("Vailee"),
        YAHR("Yahr"),
        GAHRTAHVO("Gahrtahvo"),
        PAHRTAHVO("Pahrtahvo"),
        TAHVO("Tahvo"),
        GORAHN("Gorahn"),
        PRORAHN("Prorahn");

        String displayName;

        Unit(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }


    private boolean isTimeSet;

    private long timeInMillis;
    private long timeInProrahntee;

    private int prorahntee;
    private int gorahntee;
    private int tahvotee;
    private int pahrtahvotee;
    private int gahrtahvotee;
    private int yahrtee;
    private int vaileetee;
    private int hahrtee;

    public static DniDateTime now() {
        return now(System.currentTimeMillis());
    }

    public static DniDateTime now(long timeInMillis) {
        DniDateTime dniDateTime = new DniDateTime();
        dniDateTime.setTimeInMillis(timeInMillis);
        return dniDateTime;
    }

    public static DniDateTime now(
            long hahr,
            long vailee,
            long yahr,
            long gahrtahvo,
            long pahrtahvo,
            long tahvo,
            long gorahn,
            long prorahn) {
        DniDateTime dniDateTime = new DniDateTime();
        long totalProrahntee = prorahn
                + ONE_GORAHN * gorahn
                + ONE_TAHVO * tahvo
                + ONE_PAHRTAHVO * pahrtahvo
                + ONE_GAHRTAHVO * gahrtahvo
                + ONE_YAHR * yahr
                + ONE_VAILEE * vailee
                + ONE_HAHR * hahr;
        dniDateTime.setTimeInProrahntee(totalProrahntee);
        return dniDateTime;
    }

    public void setTimeInProrahntee(long timeInProrahntee) {
        if (this.timeInProrahntee == timeInProrahntee
            && isTimeSet) {
            return;
        }
        this.timeInMillis = toMillis(timeInProrahntee);
        this.timeInProrahntee = timeInProrahntee;
        computeFields();
        isTimeSet = true;
    }

    public void setTimeInMillis(long timeInMillis) {
        if (this.timeInMillis == timeInMillis
                && isTimeSet) {
            return;
        }
        this.timeInMillis = timeInMillis;
        this.timeInProrahntee = toProrahntee(timeInMillis);
        computeFields();
        isTimeSet = true;
    }

    private static long toProrahntee(long millis) {
        return (long) Math.floor(millis / SECS_TO_PRORAHN / 1000 + EPOCH_OFFSET);
    }

    private static long toMillis(long prorahntee) {
        return (long) Math.floor((prorahntee - EPOCH_OFFSET) * SECS_TO_PRORAHN * 1000);
    }

    public static String getVaileeName(Integer vailee) {
        return VAILEE_NAMES[vailee - 1];
    }

    protected void computeFields() {
        hahrtee = (int) (timeInProrahntee / ONE_HAHR);
        vaileetee = (int) ((timeInProrahntee % ONE_HAHR) / ONE_VAILEE);
        yahrtee = (int) ((timeInProrahntee % ONE_VAILEE) / ONE_YAHR);
        gahrtahvotee = (int) ((timeInProrahntee % ONE_YAHR) / ONE_GAHRTAHVO);
        pahrtahvotee = (int) ((timeInProrahntee % ONE_GAHRTAHVO) / ONE_PAHRTAHVO);
        tahvotee = (int) ((timeInProrahntee % ONE_PAHRTAHVO) / ONE_TAHVO);
        gorahntee = (int) ((timeInProrahntee % ONE_TAHVO) / ONE_GORAHN);
        prorahntee = (int) (timeInProrahntee % ONE_GORAHN);
    }

    public int getMax(Unit unit) {
        switch (unit) {
            case VAILEE:
                return 10;
            case YAHR:
                return 29;
            case GAHRTAHVO:
                return 5;
            case PAHRTAHVO:
                return 5;
            case TAHVO:
                return 5;
            case GORAHN:
                return 25;
            case PRORAHN:
                return 25;
            default:
                return -1;
        }
    }

    public Unit getLarger(Unit unit) {
        switch (unit) {
            case VAILEE:
                return Unit.HAHR;
            case YAHR:
                return Unit.VAILEE;
            case GAHRTAHVO:
                return Unit.YAHR;
            case PAHRTAHVO:
                return Unit.GAHRTAHVO;
            case TAHVO:
                return Unit.PAHRTAHVO;
            case GORAHN:
                return Unit.TAHVO;
            case PRORAHN:
                return Unit.GORAHN;
            default:
                throw new RuntimeException("no larger unit");
        }
    }

    public int getNum(Unit unit) {
        switch (unit) {
            case HAHR:
                return getHahrtee();
            case VAILEE:
                return getVaileetee();
            case YAHR:
                return getYahrtee();
            case GAHRTAHVO:
                return getGahrtahvotee();
            case PAHRTAHVO:
                return getPahrtahvotee();
            case TAHVO:
                return getTahvotee();
            case GORAHN:
                return getGorahntee();
            case PRORAHN:
                return getProrahntee();
            default:
                return -1;
        }
    }

    public int getProrahntee() {
        return prorahntee;
    }

    public int getGorahntee() {
        return gorahntee;
    }

    public int getTahvotee() {
        return tahvotee;
    }

    public int getPahrtahvotee() {
        return pahrtahvotee;
    }

    public int getGahrtahvotee() { return gahrtahvotee; }

    public int getYahrtee() {
        return yahrtee + 1;
    }

    public int getVaileetee() {
        return vaileetee + 1;
    }

    public String getVaileeName() {
        return VAILEE_NAMES[vaileetee];
    }

    public int getHahrtee() {
        return hahrtee;
    }

    public long getSystemTimeInMillis() {
        return timeInMillis;
    }

    @Override
    public int compareTo(DniDateTime another) {
        int i = Integer.compare(hahrtee, another.hahrtee);
        if (i != 0) return i;

        i = Integer.compare(vaileetee, another.vaileetee);
        if (i != 0) return i;

        i = Integer.compare(yahrtee, another.yahrtee);
        if (i != 0) return i;

        i = Integer.compare(gahrtahvotee, another.gahrtahvotee);
        if (i != 0) return i;

        i = Integer.compare(pahrtahvotee, another.pahrtahvotee);
        if (i != 0) return i;

        i = Integer.compare(tahvotee, another.tahvotee);
        if (i != 0) return i;

        i = Integer.compare(gorahntee, another.gorahntee);
        if (i != 0) return i;

        i = Integer.compare(prorahntee, another.prorahntee);
        return i;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DniDateTime) {
            DniDateTime otherDataTime = (DniDateTime) other;
            return this.gahrtahvotee == otherDataTime.gahrtahvotee
                    && this.gorahntee == otherDataTime.gorahntee
                    && this.hahrtee == otherDataTime.hahrtee
                    && this.pahrtahvotee == otherDataTime.pahrtahvotee
                    && this.prorahntee == otherDataTime.prorahntee
                    && this.tahvotee == otherDataTime.tahvotee
                    && this.vaileetee == otherDataTime.vaileetee
                    && this.yahrtee == otherDataTime.yahrtee;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                gahrtahvotee,
                gorahntee,
                hahrtee,
                pahrtahvotee,
                prorahntee,
                tahvotee,
                vaileetee,
                yahrtee);
    }
}
