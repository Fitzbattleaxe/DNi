package com.dane.dni;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dane on 8/23/2015.
 */
public class TimeDisplay extends TextView {

    private List<DniDateTime.Unit> units;
    private String displayFormat;
    private DniDateTime clock;

    public TimeDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeDisplay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/EBGaramond-Regular.ttf"));
    }

    public void setUnits(List<DniDateTime.Unit> units, String displayFormat) {
        this.units = units;
        this.displayFormat = displayFormat;
    }

    public void setClock(DniDateTime clock) {
        this.clock = clock;
    }

    public void updateDisplay() {
        Object[] formatArgs = new Object[units.size()];
        int i = 0;
        for (DniDateTime.Unit unit : units) {
            Object arg;
            if (unit.equals(DniDateTime.Unit.NAMED_VAILEE)) {
                arg = clock.getVaileeName();
            } else {
                arg = clock.getNum(unit);
            }
            formatArgs[i] = arg;
            i++;
        }
        this.setText(String.format(displayFormat, formatArgs));
    }
}
