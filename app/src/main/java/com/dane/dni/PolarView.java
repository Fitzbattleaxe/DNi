package com.dane.dni;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dane on 8/21/2015.
 */
public class PolarView extends RelativeLayout {
    private LinkedHashMap<DniDateTime.Unit, HandView> hands =
            new LinkedHashMap<DniDateTime.Unit, HandView>();
    private PolarChromeView chrome;

    private static final float DEVELOPMENT_WIDTH = 384.0f;

    public PolarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        float scale = dpWidth  / DEVELOPMENT_WIDTH;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PolarView,
                0, 0);

        try {
            List<DniDateTime.Unit> units = new LinkedList<DniDateTime.Unit>();
            String unitString = a.getString(R.styleable.PolarView_units);
            if (unitString != null && !unitString.isEmpty()) {
                for (String unit : unitString.split(",")) {
                    units.add(DniDateTime.Unit.valueOf(unit.toUpperCase()));
                }
            }
            addHands(units,
                    scale * a.getDimension(R.styleable.PolarView_centerCircleRadius, 0.0f),
                    scale * a.getDimension(R.styleable.PolarView_ringWidth, 0.0f),
                    scale * a.getDimension(R.styleable.PolarView_ringGap, 0.0f),
                    scale * a.getDimension(R.styleable.PolarView_chromeCircleRadius, 0.0f),
                    scale * a.getDimension(R.styleable.PolarView_chromeBarRadius, 0.0f),
                    scale * a.getDimension(R.styleable.PolarView_chromeBarWidth, 0.0f),
                    scale * a.getDimension(R.styleable.PolarView_chromeOuterRingPadding, 0.0f),
                    scale * a.getDimension(R.styleable.PolarView_chromeBackgroundPadding, 0.0f),
                    scale * a.getDimension(R.styleable.PolarView_counterSize, 0.0f));
        } finally {
            a.recycle();
        }
    }

    private void addHands(List<DniDateTime.Unit> units,
                          float innerCircleRadius,
                          float ringWidth,
                          float ringGap,
                          float chromeCircleRadius,
                          float chromeBarRadius,
                          float chromeBarWidth,
                          float chromeOuterRingPadding,
                          float chromeBackgroundPadding,
                          float numberSize) {
        addHands(units);

        chrome.setSizeParams(chromeCircleRadius, chromeBarRadius, chromeBarWidth,
                chromeOuterRingPadding, chromeBackgroundPadding,
                units.size(), innerCircleRadius, ringWidth, ringGap);

        float curInner = innerCircleRadius;
        for (HandView hand : hands.values()) {
            hand.setRadii(curInner, curInner + ringWidth, numberSize);
            curInner = curInner + ringWidth + ringGap;
        }
    }

    public void addHands(List<DniDateTime.Unit> desiredUnits) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        chrome = new PolarChromeView(this.getContext());
        chrome.setLayoutParams(layoutParams);
        this.addView(chrome);
        hands = new LinkedHashMap<DniDateTime.Unit, HandView>();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        for (DniDateTime.Unit unit : desiredUnits) {
            HandView handView = new HandView(this.getContext(), unit);
            handView.setLayoutParams(layoutParams);
            this.addView(handView);
            hands.put(unit, handView);
        }
    }

    public void addClock(DniDateTime dniDateTime) {
        for (HandView handView : hands.values()) {
            handView.setDniDateTime(dniDateTime);
        }
    }

    public void setUpEasing(Float[] easingValues, int easingPoints) {
        for (HandView handView : hands.values()) {
            handView.setUpEasing(easingValues, easingPoints);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        int side = Math.min(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
//        int numHands = hands.size();
//        if (numHands == 0) {
//            return;
//        }
//        int innerCircleRadius = side / 15;
//        int chromeCircleRadius = ( 4 * innerCircleRadius) / (8 * numHands);
//        int chromeLineOvershoot = chromeCircleRadius;
//        int chromeLineWidth = chromeCircleRadius;
//        int circleSpacing = (side / 40 * 4) / numHands;
//        int circleWidth = (side / 2 - chromeLineOvershoot - innerCircleRadius
//                - circleSpacing * (numHands + 1)) / numHands;
//        int curInner = innerCircleRadius;
//        for (HandView hand : hands.values()) {
//            hand.setRadii(curInner, curInner + circleWidth);
//            curInner = curInner + circleWidth + circleSpacing;
//        }
//        chrome.setSizeParams(chromeCircleRadius,
//                innerCircleRadius + numHands*circleWidth + (numHands - 1)*circleSpacing
//                        + chromeLineOvershoot,
//                chromeLineWidth);
    }

    public void updateTime() {
        for (HandView hand : hands.values()) {
            hand.updateTime();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (MeasureSpec.getSize(widthMeasureSpec) < MeasureSpec.getSize(heightMeasureSpec)) {
//            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
//        } else {
//            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
//        }
//        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int h = resolveSizeAndState(parentWidth, heightMeasureSpec, 0);
//        for (HandView handView : hands.values()) {
//            handView.measure(widthMeasureSpec, widthMeasureSpec);
//        }
//        setMeasuredDimension(parentWidth, parentWidth);
    }
}
