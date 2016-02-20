package com.dane.dni.watch.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Dane on 8/23/2015.
 */
public class PolarChromeView extends View {

    private float circleRadius;
    private float lineRadius;
    private float lineWidth;
    private float outerRingPadding;
    private float backgroundPadding;
    private int numRings;
    private float innerCircleRadius;
    private float ringWidth;
    private  float ringGap;

    private Paint smallCirclePaint;
    private Paint largerCirclePaint;
    private Paint ringPaint;
    private Paint gapPaint;
    private Paint backgroundPaint1;
    private Paint backgroundPaint2;

    public PolarChromeView(Context context) {
        super(context);
    }

    public void setColors(int smallCircleColor, int largeCircleColor, int ringColor, int gapColor,
                          int backgroundColor1, int backgroundColor2) {
        smallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        smallCirclePaint.setColor(smallCircleColor);
        largerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        largerCirclePaint.setColor(largeCircleColor);
        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setColor(ringColor);
        gapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gapPaint.setColor(gapColor);
        backgroundPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint1.setColor(backgroundColor1);
        backgroundPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint2.setColor(backgroundColor2);
    }

    public void setSizeParams(float circleRadius, float lineRadius, float lineWidth,
                              float outerRingPadding, float backgroundPadding,
                              int numRings, float innerCircleRadius, float ringWidth, float ringGap) {
        this.circleRadius = circleRadius;
        this.lineRadius = lineRadius;
        this.lineWidth = lineWidth;
        this.outerRingPadding = outerRingPadding;
        this.backgroundPadding = backgroundPadding;
        this.numRings = numRings;
        this.innerCircleRadius = innerCircleRadius;
        this.ringWidth = ringWidth;
        this.ringGap = ringGap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect bounds = canvas.getClipBounds();

        float backgroundMinorAxis = innerCircleRadius + numRings*ringWidth
                + (numRings - 1)*ringGap + outerRingPadding;
        float backgroundMajorAxis = backgroundMinorAxis + backgroundPadding;

        RectF minorOval = new RectF();
        minorOval.set(-backgroundMinorAxis, -backgroundMinorAxis,
                backgroundMinorAxis, backgroundMinorAxis);
        RectF majorOval = new RectF();
        majorOval.set(-backgroundMinorAxis, -backgroundMajorAxis,
                backgroundMinorAxis, backgroundMajorAxis);

        Path path = new Path();
        path.arcTo(majorOval, -180, 180);
        path.arcTo(minorOval, 0, -180);
        path.close();
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, backgroundPaint1);

        path = new Path();
        path.arcTo(minorOval, 180, -180);
        path.arcTo(majorOval, 0, 180);
        path.close();
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, backgroundPaint2);


        path = new Path();
        path.addCircle(0, 0, innerCircleRadius - ringGap, Path.Direction.CCW);
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, largerCirclePaint);

        float curInner = innerCircleRadius;
        for (int i = 0; i < numRings; i++) {
            path = new Path();
            path.addCircle(0, 0, curInner, Path.Direction.CCW);
            path.addCircle(0, 0, curInner - ringGap, Path.Direction.CW);
            path.offset(bounds.centerX(), bounds.centerY());
            canvas.drawPath(path, gapPaint);

            path = new Path();
            float padding = (i == numRings - 1) ? outerRingPadding : 0.0f;
            path.addCircle(0, 0, curInner + ringWidth + padding, Path.Direction.CCW);
            path.addCircle(0, 0, curInner, Path.Direction.CW);
            path.offset(bounds.centerX(), bounds.centerY());
            canvas.drawPath(path, ringPaint);

            curInner = curInner + ringWidth + ringGap;
        }

        path = new Path();
        path.addCircle(0, 0, circleRadius, Path.Direction.CCW);
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, smallCirclePaint);
        path = new Path();
        path.addRect(-lineWidth / 2, -lineRadius, lineWidth / 2, 0, Path.Direction.CCW);
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, smallCirclePaint);
    }
}
