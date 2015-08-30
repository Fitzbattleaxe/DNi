package com.dane.dni;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dane on 8/23/2015.
 */
public class TimeDisplay extends TextView {

    private static final String[] secondarySubstrings = new String[] {
            ":",
            ",",
    };

    private static final String[] tertiarySubstrings = new String[] {
      "DE"
    };

    private List<DniDateTime.Unit> units;
    private String displayFormat;
    private DniDateTime clock;
    private int secondaryColor;
    private float tertiarySizeScale;

    public TimeDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/EBGaramond-Regular.ttf"));
        secondaryColor = Color.rgb(107, 102, 98);
        tertiarySizeScale = 0.35f;
    }

    public TimeDisplay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/EBGaramond-Regular.ttf"));
        secondaryColor = Color.rgb(107, 102, 98);
        tertiarySizeScale = 0.35f;
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

        String unformattedString = String.format(displayFormat, formatArgs);
        SpannableStringBuilder finalString = new SpannableStringBuilder(unformattedString);
        for (String target : secondarySubstrings) {
            int lastIndex = -1;
            int curIndex = unformattedString.indexOf(target, lastIndex + 1);
            while (-1 != curIndex) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(secondaryColor);
                finalString.setSpan(colorSpan, curIndex, curIndex + target.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastIndex = curIndex;
                curIndex = unformattedString.indexOf(target, lastIndex + 1);
            }
        }
        for (String target : tertiarySubstrings) {
            int lastIndex = -1;
            int curIndex = unformattedString.indexOf(target, lastIndex + 1);
            while (-1 != curIndex) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(secondaryColor);
                finalString.setSpan(colorSpan, curIndex, curIndex + target.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(tertiarySizeScale);
                finalString.setSpan(sizeSpan, curIndex, curIndex + target.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastIndex = curIndex;
                curIndex = unformattedString.indexOf(target, lastIndex + 1);
            }
        }
        this.setText(finalString);
    }
}
