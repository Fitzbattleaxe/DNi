package com.dane.dni.watch.views;

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
import android.view.ViewGroup;
import android.widget.TextView;

import com.dane.dni.common.data.DniDateTime;

import java.util.List;

/**
 * Created by Dane on 8/23/2015.
 */
public class TimeDisplay extends TextView {

    private static final float DEVELOPMENT_HEIGHT = 632.0f;

    private static final String[] secondarySubstrings = new String[] {
            " : ",
            ",",
    };

    private static final String[] tertiarySubstrings = new String[] {
            " DE",
    };

    private List<DniDateTime.Unit> units;
    private String displayFormat;
    private DniDateTime clock;
    private int secondaryColor;
    private float secondarySizeScale;
    private float tertiarySizeScale;

    private float scale;

    public TimeDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/EBGaramond-Regular.ttf"));
        secondaryColor = Color.rgb(107, 102, 98);
        secondarySizeScale = 0.7f;
        tertiarySizeScale = 0.35f;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        scale = dpHeight  / DEVELOPMENT_HEIGHT;

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.getTextSize() * scale);
        this.setShadowLayer(this.getShadowRadius(), this.getShadowDx() * scale,
                this.getShadowDy() * scale, this.getShadowColor());
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) layoutParams;
        marginLayoutParams.setMargins(Math.round(marginLayoutParams.leftMargin * scale),
                Math.round(marginLayoutParams.topMargin * scale),
                Math.round(marginLayoutParams.rightMargin * scale),
                Math.round(marginLayoutParams.bottomMargin * scale));
        super.setLayoutParams(layoutParams);
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
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(secondarySizeScale);
                finalString.setSpan(sizeSpan, curIndex, curIndex + target.length(),
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
