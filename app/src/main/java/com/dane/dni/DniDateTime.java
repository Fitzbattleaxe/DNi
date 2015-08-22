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
    private static final int ONE_YAHR = 25 * ONE_PAHRTAHVO;
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

    private boolean isTimeSet;

    private long timeInMillis;
    private long timeInProrahntee;

    private int prorahntee;
    private int gorahntee;
    private int tahvotee;
    private int pahrtahvotee;
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
        return Math.round(millis / SECS_TO_PRORAHN / 1000 + EPOCH_OFFSET);
    }

    protected void computeFields() {
        hahrtee = (int) (timeInProrahntee / ONE_HAHR);
        vaileetee = (int) ((timeInProrahntee % ONE_HAHR) / ONE_VAILEE);
        yahrtee = (int) ((timeInProrahntee % ONE_VAILEE) / ONE_YAHR);
        pahrtahvotee = (int) ((timeInProrahntee % ONE_YAHR) / ONE_PAHRTAHVO);
        tahvotee = (int) ((timeInProrahntee % ONE_PAHRTAHVO) / ONE_TAHVO);
        gorahntee = (int) ((timeInProrahntee % ONE_TAHVO) / ONE_GORAHN);
        prorahntee = (int) (timeInProrahntee % ONE_GORAHN);
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
        return String.format("%02d:%02d:%02d:%02d, %s %d, %d DE",
                getPahrtahvotee(),
                getTahvotee(),
                getGorahntee(),
                getProrahntee(),
                getVaileeName(),
                getYahrtee(),
                getHahrtee());
    }
}
