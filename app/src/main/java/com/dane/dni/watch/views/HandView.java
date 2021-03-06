package com.dane.dni.watch.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.view.View;

import com.dane.dni.common.data.DniDateTime;
import com.dane.dni.common.utils.DniNumberUtil;
import com.dane.dni.watch.views.utils.GradientShiftingColor;

/**
 * Created by Dane on 8/22/2015.
 */
public class HandView extends View {

    private float outerRadius;
    private float innerRadius;
    private DniDateTime.Unit unit;
    private DniDateTime dniDateTime;
    private DniDateTime offsetDniDateTime;

    private int lastTime = -1;

    private Paint paint;
    private Paint textPaint;
    private Matrix gradientMatrix;
    private GradientShiftingColor gradientShiftingColor;

    RectF outerOval;
    RectF innerOval;

    private Float[] easingValues;
    private int easingPoints;
    private long tickStartMsec;
    private long msecSinceTick;
    private boolean ticking;
    private float angle;
    private float lastAngle;
    private float backAngle;
    private float lastBackAngle;
    private float colorPosition;
    private float lastColorPosition;

    private boolean useDniNums;

    public HandView(Context context, DniDateTime.Unit unit) {
        super(context);
        this.unit = unit;
        innerRadius = 0;
        outerRadius = 0;
        tickStartMsec = 0;
        msecSinceTick = 0;
        ticking = false;
    }

    public void setUpEasing(Float[] easingValues, int easingPoints) {
        this.easingValues = easingValues;
        this.easingPoints = easingPoints;
    }

    public void setTextPaint(Paint textPaint, boolean useDniNums) {
        this.textPaint = textPaint;
        this.useDniNums = useDniNums;
    }

    public void setRadii(float innerRadius, float outerRadius) {
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;

        outerOval = new RectF();
        outerOval.set(-outerRadius, -outerRadius, outerRadius, outerRadius);
        innerOval = new RectF();
        innerOval.set(-innerRadius, -innerRadius, innerRadius, innerRadius);
    }

    public void setDniDateTime(DniDateTime dniDateTime) {
        this.dniDateTime = dniDateTime;
    }

    public void setOffsetDniDateTime(DniDateTime offsetDniDateTime) {
        this.offsetDniDateTime = offsetDniDateTime;
    }


    public void updateTime() {
        int curTime = offsetDniDateTime.getNum(unit);
        long curTimeMsec = System.currentTimeMillis();
        if (curTime != lastTime) {

            if (curTime < lastTime) {
                backAngle = 360.0f;
            } else {
                lastBackAngle = 0;
                backAngle = 0;
            }

            ticking = true;
            lastTime = curTime;
            tickStartMsec = curTimeMsec;
            lastAngle = angle;
            float arcProgress = lastTime / (1.0f * offsetDniDateTime.getMax(unit));
            angle = 360.0f * arcProgress;

            if (gradientShiftingColor != null) {
                lastColorPosition = colorPosition < 1.0 ? colorPosition : 0;
                DniDateTime.Unit largerUnit = offsetDniDateTime.getLarger(unit);
                if (gradientShiftingColor.useLargerUnitPosition()) {
                    int largerCurTime = offsetDniDateTime.getNum(largerUnit);
                    int largerMaxTime = offsetDniDateTime.getMax(largerUnit);
                    colorPosition = arcProgress > 0
                            ? (largerCurTime + arcProgress) / (1.0f * largerMaxTime) : 1.0f;
                } else {
                    colorPosition = arcProgress > 0 ? arcProgress : 1.0f;
                }
            }

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
        if (easingValues == null) {
          return;
        }

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
        angleDiff = angleDiff >= 0 ? angleDiff : 360 + angleDiff;
        float drawAngle = (lastAngle + angleMultiplier*angleDiff);
        if (drawAngle > 360.0f) {
            drawAngle = drawAngle - 360.0f;
        }

        float backAngleDiff = backAngle - lastBackAngle;
        float drawBackAngle = (lastBackAngle + angleMultiplier*backAngleDiff);


        if (drawAngle == 360.0f && drawBackAngle == 0.0f) {
            path.addOval(innerOval, Path.Direction.CW);
            path.addOval(outerOval, Path.Direction.CCW);
        } else if (drawAngle >= drawBackAngle) {
            path.arcTo(innerOval, -90 + drawAngle, -(drawAngle - drawBackAngle));
            path.arcTo(outerOval, -90 + drawBackAngle, drawAngle - drawBackAngle);
        } else {
            path.arcTo(innerOval, -90 + drawAngle, drawBackAngle - drawAngle - 360.0f);
            path.arcTo(outerOval, -90 + drawBackAngle, 360.0f - drawBackAngle + drawAngle);
        }
        path.close();
        path.offset(bounds.centerX(), bounds.centerY());

        if (paint.getShader() instanceof SweepGradient && gradientMatrix == null) {
            gradientMatrix = new Matrix();

            gradientMatrix.preRotate(-90.0f, 0, 0);
            gradientMatrix.postTranslate(bounds.centerX(), bounds.centerY());
            paint.getShader().setLocalMatrix(gradientMatrix);
        } else if (gradientShiftingColor != null) {
            float colorDiff = colorPosition - lastColorPosition;
            float drawColorPosition = lastColorPosition + angleMultiplier*colorDiff;
            paint.setColor(gradientShiftingColor.getColor(drawColorPosition));
        }

        canvas.drawPath(path, paint);

        if (useDniNums) {
            float textX = bounds.centerX() + textPaint.getTextSize() * 0.4f;
            float textY = bounds.centerY() - innerRadius - (outerRadius - innerRadius) * 0.2f;
            canvas.drawText(DniNumberUtil.convertToDni(
                    dniDateTime.getNum(unit)), textX, textY, textPaint);
        } else {
            float textX = bounds.centerX() + textPaint.getTextSize() * 0.2f;
            float textY = bounds.centerY() - innerRadius - (outerRadius - innerRadius) * 0.3f;
            canvas.drawText(Integer.toString(dniDateTime.getNum(unit)),
                    textX, textY, textPaint);
        }
    }

    public void setupColor(String colorString) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (colorString.startsWith("c")) {
            String trimmedColorString = colorString.substring(2, colorString.length() - 1);
            paint.setColor(Color.parseColor(trimmedColorString));
        } else if (colorString.startsWith("g") || colorString.startsWith("s")
                || colorString.startsWith("t")) {
            String trimmedColorString = colorString.substring(2, colorString.length() - 1);
            String[] colorSegments = trimmedColorString.split(";");
            int[] colors = new int[colorSegments.length];
            float[] positions = new float[colorSegments.length];
            int index = 0;
            for (String colorSegment : colorSegments) {
                String[] colorPair = colorSegment.split(":");
                int color = Color.parseColor(colorPair[0]);
                float position = Float.parseFloat(colorPair[1]);
                colors[index] = color;
                positions[index] = position;
                index++;
            }
            if (colorString.startsWith("g")) {
                SweepGradient sweepGradient = new SweepGradient(0, 0, colors, positions);
                paint.setShader(sweepGradient);
            } else {
                gradientShiftingColor =
                        new GradientShiftingColor(colors, positions, colorString.startsWith("s"));
            }
        }
        paint.setStyle(Paint.Style.FILL);
    }
}
