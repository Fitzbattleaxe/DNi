package com.dane.dni;

import android.content.Context;
import android.view.View;

/**
 * Created by Dane on 8/22/2015.
 */
public class HandView extends View {

    private int radius;
    private DniDateTime.Unit unit;
    private DniDateTime dniDateTime;

    public HandView(Context context, DniDateTime.Unit unit) {
        super(context);
        this.unit = unit;
        radius = 0;
    }

    public void setDniDateTime(DniDateTime dniDateTime) {
        this.dniDateTime = dniDateTime;
    }
}
