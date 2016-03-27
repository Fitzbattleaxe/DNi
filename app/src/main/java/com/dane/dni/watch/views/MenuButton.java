package com.dane.dni.watch.views;

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
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;

import com.dane.dni.common.views.CircularButton;

import java.io.IOException;

/**
 * Created by Dane on 11/1/2015.
 */
public class MenuButton extends CircularButton {

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setText("\uE3C7");
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }
}
