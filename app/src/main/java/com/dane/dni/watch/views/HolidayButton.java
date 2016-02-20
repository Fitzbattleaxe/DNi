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
import android.widget.Button;

/**
 * Created by Dane on 12/22/2015.
 */
public class HolidayButton extends Button {

    private Paint circlePaint;
    private Paint backgroundPaint1;
    private Paint backgroundPaint2;
    private float circleRadius;
    private Rect bounds;

    public HolidayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.rgb(244, 242, 239));

        backgroundPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint1.setColor(Color.rgb(255, 255, 255));

        backgroundPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint2.setColor(Color.rgb(196, 194, 188));

        this.setTextScaleX(-1.0f);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/MaterialIcons-Regular.ttf"));
        this.setTextColor(Color.rgb(119, 112, 109));
        this.setText("\uE8E1");
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        double squaredDistFromCenter =
                Math.pow(x - bounds.centerX(), 2) + Math.pow(y - bounds.centerY(), 2);
        if (squaredDistFromCenter > Math.pow(circleRadius, 2)) {
            return false;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bounds = canvas.getClipBounds();
        float availableHeight = bounds.height();
        float backgroundPadding = availableHeight / 30;
        circleRadius = availableHeight / 2  - backgroundPadding;
        float fontSize = circleRadius * 1.5f;

        float backgroundMajorAxis = circleRadius + backgroundPadding;

        RectF minorOval = new RectF();
        minorOval.set(-circleRadius, -circleRadius,
                circleRadius, circleRadius);
        RectF majorOval = new RectF();
        majorOval.set(-circleRadius, -backgroundMajorAxis,
                circleRadius, backgroundMajorAxis);

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
        path.addCircle(0, 0, circleRadius, Path.Direction.CCW);
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, circlePaint);

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        this.setShadowLayer(4, 0, availableHeight / 35, Color.rgb(199, 197, 194));

        super.onDraw(canvas);
    }
}
