package com.dane.dni;

import java.io.Serializable;

/**
 * Created by Dane on 12/7/2015.
 */
public class DniHoliday {
    private String name;
    private long vailee;
    private long yahr;

    public DniHoliday(String name, long vailee, long yahr) {
        this.name = name;
        this.vailee = vailee;
        this.yahr = yahr;
    }

    public String getName() {
        return name;
    }

    public long getVailee() {
        return vailee;
    }

    public long getYahr() {
        return yahr;
    }
}
