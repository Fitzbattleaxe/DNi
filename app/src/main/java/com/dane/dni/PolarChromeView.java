package com.dane.dni;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Dane on 8/23/2015.
 */
public class PolarChromeView extends View {

    private int circleRadius;
    private int lineRadius;
    private int lineWidth;
    private Paint paint;

    public PolarChromeView(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(153, 149, 147));
    }

    public void setSizeParams(int circleRadius, int lineRadius, int lineWidth) {
        this.circleRadius = circleRadius;
        this.lineRadius = lineRadius;
        this.lineWidth = lineWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect bounds = canvas.getClipBounds();
        Path path = new Path();
        path.addCircle(0, 0, circleRadius, Path.Direction.CCW);
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, paint);
        path = new Path();
        path.addRect(-lineWidth / 2, -lineRadius, lineWidth / 2, 0, Path.Direction.CCW);
        path.offset(bounds.centerX(), bounds.centerY());
        canvas.drawPath(path, paint);
    }
}
