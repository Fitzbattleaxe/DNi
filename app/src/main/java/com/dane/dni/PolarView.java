package com.dane.dni;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dane on 8/21/2015.
 */
public class PolarView extends RelativeLayout {
    private LinkedHashMap<DniDateTime.Unit, HandView> hands =
            new LinkedHashMap<DniDateTime.Unit, HandView>();
    private PolarChromeView chrome;


    public PolarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PolarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addHands(List<DniDateTime.Unit> desiredUnits) {
        hands = new LinkedHashMap<DniDateTime.Unit, HandView>();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        for (DniDateTime.Unit unit : desiredUnits) {
            HandView handView = new HandView(this.getContext(), unit);
            handView.setLayoutParams(layoutParams);
            this.addView(handView);
            hands.put(unit, handView);
        }
        chrome = new PolarChromeView(this.getContext());
        chrome.setLayoutParams(layoutParams);
        this.addView(chrome);
    }

    public void addClock(DniDateTime dniDateTime) {
        for (HandView handView : hands.values()) {
            handView.setDniDateTime(dniDateTime);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int side = Math.min(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
        int numHands = hands.size();
        if (numHands == 0) {
            return;
        }
        int innerCircleRadius = side / 15;
        int chromeCircleRadius = ( 4 * innerCircleRadius) / (8 * numHands);
        int chromeLineOvershoot = chromeCircleRadius;
        int chromeLineWidth = chromeCircleRadius;
        int circleSpacing = (side / 40 * 4) / numHands;
        int circleWidth = (side / 2 - chromeLineOvershoot - innerCircleRadius
                - circleSpacing * (numHands + 1)) / numHands;
        int curInner = innerCircleRadius;
        for (HandView hand : hands.values()) {
            hand.setRadii(curInner, curInner + circleWidth);
            curInner = curInner + circleWidth + circleSpacing;
        }
        chrome.setSizeParams(chromeCircleRadius,
                innerCircleRadius + numHands*circleWidth + (numHands - 1)*circleSpacing
                        + chromeLineOvershoot,
                chromeLineWidth);
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
