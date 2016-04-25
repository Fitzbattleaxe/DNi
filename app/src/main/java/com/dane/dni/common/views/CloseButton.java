package com.dane.dni.common.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Button;

import com.dane.dni.common.views.CircularButton;

/**
 * Created by Dane on 3/27/2016.
 */
public class CloseButton extends CircularButton {

    public CloseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setText("\uE14C");
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }
}
