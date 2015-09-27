package com.dane.dni;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;
import android.R;

import java.util.Map;

/**
 * Created by Dane on 8/22/2015.
 */
public class HandView extends View {

    private float outerRadius;
    private float innerRadius;
    private DniDateTime.Unit unit;
    private DniDateTime dniDateTime;

    private int lastTime;

    private Paint paint;
    private Paint textPaint;

    RectF outerOval;
    RectF innerOval;

    private Float[] easingValues;
    private int easingPoints;
    private long tickStartMsec;
    private long msecSinceTick;
    private boolean ticking;
    private float angle;
    private float lastAngle;

    public HandView(Context context, DniDateTime.Unit unit) {
        super(context);
        this.unit = unit;
        innerRadius = 0;
        outerRadius = 0;
        tickStartMsec = 0;
        msecSinceTick = 0;
        ticking = false;
        init();
    }

    public void setUpEasing(Float[] easingValues, int easingPoints) {
        this.easingValues = easingValues;
        this.easingPoints = easingPoints;
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(89, 75, 59));
        paint.setStyle(Paint.Style.FILL);
        textPaint = new Paint(
                Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.rgb(229, 228, 227));
        textPaint.setTypeface(
                Typeface.createFromAsset(this.getContext().getAssets(), "fonts/D_NI_SCR.TTF"));
    }

    public void setRadii(float innerRadius, float outerRadius, float textSize) {
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        textPaint.setTextSize(textSize);

        outerOval = new RectF();
        outerOval.set(-outerRadius, -outerRadius, outerRadius, outerRadius);
        innerOval = new RectF();
        innerOval.set(-innerRadius, -innerRadius, innerRadius, innerRadius);
    }

    public void setDniDateTime(DniDateTime dniDateTime) {
        this.dniDateTime = dniDateTime;
    }

    public void updateTime() {
        int curTime = dniDateTime.getNum(unit);
        long curTimeMsec = System.currentTimeMillis();
        if (curTime != lastTime) {
            ticking = true;
            lastTime = curTime;
            tickStartMsec = curTimeMsec;
            lastAngle = angle;
            angle = 360.0f * lastTime / (1.0f * dniDateTime.getMax(unit));
            msecSinceTick = curTimeMsec - tickStartMsec;
            this.invalidate();
        } else if (ticking) {
            msecSinceTick = curTimeMsec - tickStartMsec;
            if (msecSinceTick >= easingPoints) {
                ticking = false;
            }
            this.invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect bounds = canvas.getClipBounds();

        Path path = new Path();
        Float easingValue = msecSinceTick < easingValues.length ? easingValues[(int) msecSinceTick]
                : null;
        float angleMultiplier;
        if (easingValue != null && ticking) {
            angleMultiplier = easingValue + 1.0f;
        } else {
            angleMultiplier = 1.0f;
        }
        float angleDiff = angle - lastAngle;
       // angleDiff = angleDiff >= 0 ? angleDiff : 360 + angleDiff;
        float drawAngle = (lastAngle + angleMultiplier*angleDiff)/* % 360.0f*/;
        path.arcTo(innerOval, -90 + drawAngle, -drawAngle);
        path.arcTo(outerOval, -90, drawAngle);
        path.close();
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, paint);
        float textX = bounds.centerX() + textPaint.getTextSize() * 0.4f;
        float textY = bounds.centerY() - innerRadius - (outerRadius - innerRadius) * 0.2f;
        canvas.drawText(DniNumberUtil.convertToDni(lastTime), textX, textY, textPaint);
    }
}
