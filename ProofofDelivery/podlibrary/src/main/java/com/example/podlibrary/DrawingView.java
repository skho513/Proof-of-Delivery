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
 * Custom view for drawing
 *
 * @author Nance
 */
public class DrawingView extends View {
    /**
     * Listener to be implemented by classes that needs to know states of this view
     */
    public interface DrawStateListener {
        /**
         * Invoked when drawing has started
         *
         * @param view where the drawing started
         */
        void onDrawStarted(DrawingView view);
    }

    private Path path;
    private Paint paintPen;
    private DrawStateListener listener;

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        path = new Path();
        paintPen = new Paint();
        paintPen.setAntiAlias(true);
        paintPen.setDither(true);
        paintPen.setColor(Color.BLACK);
        paintPen.setStyle(Paint.Style.STROKE);
        paintPen.setStrokeCap(Paint.Cap.ROUND);
        paintPen.setStrokeWidth(12);
    }

    /**
     * Sets a {@link DrawStateListener}
     *
     * @param listener to attach
     */
    public void setDrawStateListener(DrawStateListener listener) {
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
                notifyDrawStart();
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                invalidate();
                break;
        }
        return true;
    }

    private void notifyDrawStart() {
        if (!path.isEmpty() && listener != null) {
            listener.onDrawStarted(this);
        }
    }

    public void clearView() {
        path.reset();
        invalidate();
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }
}
