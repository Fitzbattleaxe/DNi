package com.dane.dni;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dane on 8/21/2015.
 */
public class PolarView extends RelativeLayout {

    private Map<DniDateTime.Unit, HandView> hands;


    public PolarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addHands();
    }

    public PolarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addHands();
    }

    private void addHands() {
        hands = new HashMap<DniDateTime.Unit, HandView>();
        for (DniDateTime.Unit unit : DniDateTime.Unit.values()) {
            HandView handView = new HandView(this.getContext(), unit);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
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

    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(parentWidth, Math.round(parentWidth));
    }
}
