package com.dane.dni;

/**
 * Created by Dane on 8/16/2015.
 */
public class DniDateTime {
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

    enum Unit {
        HAHR,
        VAILEE,
        NAMED_VAILEE,
        YAHR,
        GAHRTAHVOTEE,
        PAHRTAHVO,
        TAHVO,
        GORAHN,
        PRORAHN
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
            case GAHRTAHVOTEE:
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

    public int getNum(Unit unit) {
        switch (unit) {
            case HAHR:
                return hahrtee;
            case VAILEE:
                return vaileetee;
            case YAHR:
                return yahrtee;
            case GAHRTAHVOTEE:
                return gahrtahvotee;
            case PAHRTAHVO:
                return pahrtahvotee;
            case TAHVO:
                return tahvotee;
            case GORAHN:
                return gorahntee;
            case PRORAHN:
                return prorahntee;
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
        return vaileetee;
    }

    public String getVaileeName() {
        return VAILEE_NAMES[vaileetee];
    }

    public int getHahrtee() {
        return hahrtee;
    }

    public String getFormattedString() {
        return String.format("%d DE\n%02d %s %d\n%d:%d:%02d:%02d",
                getHahrtee(),
                getYahrtee(),
                getVaileeName(),
                getGahrtahvotee(),
                getPahrtahvotee(),
                getTahvotee(),
                getGorahntee(),
                getProrahntee());
//        return String.format("%02d:%02d:%02d:%02d, %s %d, %d DE",
//                getPahrtahvotee(),
//                getTahvotee(),
//                getGorahntee(),
//                getProrahntee(),
//                getVaileeName(),
//                getYahrtee(),
//                getHahrtee());
    }
}
