package com.dane.dni;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.R;

/**
 * Created by Dane on 8/22/2015.
 */
public class HandView extends View {

    private int outerRadius;
    private int innerRadius;
    private DniDateTime.Unit unit;
    private DniDateTime dniDateTime;

    private int lastTime;

    private Paint paint;

    public HandView(Context context, DniDateTime.Unit unit) {
        super(context);
        this.unit = unit;
        innerRadius = 0;
        outerRadius = 0;
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#8b7758"));//getResources().getColor(R.color.holo_green_light));
        paint.setStyle(Paint.Style.FILL);
    }

    public void setRadii(int innerRadius, int outerRadius) {
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
    }

    public void setDniDateTime(DniDateTime dniDateTime) {
        this.dniDateTime = dniDateTime;
    }

    public void updateTime() {
        int curTime = dniDateTime.getNum(unit);
        if (curTime != lastTime) {
            this.invalidate();
            lastTime = curTime;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect bounds = canvas.getClipBounds();
        Path path = new Path();
        RectF outerOval = new RectF();
        outerOval.set(-outerRadius, -outerRadius, outerRadius, outerRadius);
        RectF innerOval = new RectF();
        innerOval.set(-innerRadius, -innerRadius, innerRadius, innerRadius);
        float angle = 360.0f * dniDateTime.getNum(unit) / (1.0f * dniDateTime.getMax(unit));
        path.arcTo(innerOval, -90 + angle, -angle);
        path.arcTo(outerOval, -90, angle);
        path.close();
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, paint);
    }
}
