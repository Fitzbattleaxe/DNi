package com.dane.dni.alarms.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import com.dane.dni.alarms.AlarmData;
import com.dane.dni.common.data.DniDateTime;

import java.util.List;

/**
 * Created by Dane on 4/10/2016.
 */
public class AlarmTimeDisplay extends TextView {

    private static final String[] secondarySubstrings = new String[] {
            " : ",
            ",",
    };

    private static String[] wildcardStrings = new String[DniDateTime.Unit.values().length];
    static {
        for (int i = 0; i < wildcardStrings.length; i++) {
            wildcardStrings[i] = DniDateTime.Unit.values()[i].getDisplayName();
        }
    }

    private List<DniDateTime.Unit> units;
    private String displayFormat;
    private int secondaryColor;
    private float secondarySizeScale;
    private AlarmData alarmData;

    public AlarmTimeDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/EBGaramond-Regular.ttf"));
        secondaryColor = Color.rgb(107, 102, 98);
        secondarySizeScale = 0.7f;
    }

    public void setData(List<DniDateTime.Unit> units, String displayFormat, AlarmData alarmData) {
        this.units = units;
        this.displayFormat = displayFormat;
        this.alarmData = alarmData;

        Object[] formatArgs = new Object[units.size()];
        int i = 0;
        for (DniDateTime.Unit unit : units) {
            Object arg;
            if (unit.equals(DniDateTime.Unit.NAMED_VAILEE)) {
                arg = alarmData.getMonth();
                if ((Integer) arg != -1) {
                    arg = DniDateTime.getVaileeName((Integer) arg + 1);
                }
            } else if (unit.equals(DniDateTime.Unit.YAHR)) {
                arg = alarmData.getNum(unit) + 1;
            } else {
                arg = alarmData.getNum(unit);
            }
            if (arg.equals(-1)) {
                arg = unit.getDisplayName();
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
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(secondarySizeScale);
                finalString.setSpan(sizeSpan, curIndex, curIndex + target.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastIndex = curIndex;
                curIndex = unformattedString.indexOf(target, lastIndex + 1);
            }
        }
        for (String target : wildcardStrings) {
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
        this.setText(finalString);
    }
}
