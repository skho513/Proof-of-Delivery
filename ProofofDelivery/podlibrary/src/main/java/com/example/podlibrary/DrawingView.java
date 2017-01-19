package com.example.podlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Nance on 18/1/2017.
 */

public class DrawingView extends View {
    public int width;
    public int height;
    private Path path;
    private Paint paintPen;

    public interface DrawStateListener {
        void onDrawStarted();
    }

    DrawStateListener listener;

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        path = new Path();
        paintPen = new Paint();
        paintPen.setAntiAlias(true);
        paintPen.setDither(true);
        paintPen.setColor(Color.BLACK);
        paintPen.setStyle(Paint.Style.STROKE);
        paintPen.setStrokeCap(Paint.Cap.ROUND);
        paintPen.setStrokeWidth(12);
    }

    public void setDrawStateListener (DrawStateListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paintPen);
        canvas.drawColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                if (!path.isEmpty() && listener != null) {
                    listener.onDrawStarted();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void clearView() {
        path.reset();
        invalidate();
    }
}
