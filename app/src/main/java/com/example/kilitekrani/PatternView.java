package com.example.kilitekrani;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class PatternView extends View {

    private static final int GRID_SIZE = 3;
    private static final int POINT_RADIUS = 30;
    private static final int LINE_WIDTH = 10;

    private Paint paint;
    private List<Point> points;
    private List<Point> selectedPoints;
    private Path path;
    private OnPatternListener onPatternListener;

    public PatternView(Context context) {
        super(context);
        init();
    }

    public PatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFF000000); // Siyah renk
        paint.setStrokeWidth(LINE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        points = new ArrayList<>();
        selectedPoints = new ArrayList<>();
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPoints(w, h);
    }

    private void initPoints(int viewWidth, int viewHeight) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                points.add(new Point((i + 1) * (viewWidth / (GRID_SIZE + 1)), (j + 1) * (viewHeight / (GRID_SIZE + 1))));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPoints(canvas);
        drawPath(canvas);
    }

    private void drawPoints(Canvas canvas) {
        for (Point point : points) {
            paint.setStyle(selectedPoints.contains(point) ? Paint.Style.FILL : Paint.Style.STROKE);
            canvas.drawCircle(point.x, point.y, POINT_RADIUS, paint);
        }
    }

    private void drawPath(Canvas canvas) {
        if (!path.isEmpty()) {
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Point point = getSelectedPoint(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                selectedPoints.clear();
                if (point != null) {
                    selectedPoints.add(point);
                    path.moveTo(point.x, point.y);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (point != null && !selectedPoints.contains(point)) {
                    selectedPoints.add(point);
                    path.lineTo(point.x, point.y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (onPatternListener != null) {
                    onPatternListener.onPatternDetected(getPattern());
                }
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    private Point getSelectedPoint(float x, float y) {
        for (Point point : points) {
            if (Math.sqrt(Math.pow((point.x - x), 2) + Math.pow((point.y - y), 2)) <= POINT_RADIUS) {
                return point;
            }
        }
        return null;
    }

    public String getPattern() {
        StringBuilder patternBuilder = new StringBuilder();
        for (Point point : selectedPoints) {
            patternBuilder.append(points.indexOf(point));
        }
        return patternBuilder.toString();
    }

    public void clearPattern() {
        path.reset();
        selectedPoints.clear();
        invalidate();
    }

    public void setOnPatternListener(OnPatternListener listener) {
        this.onPatternListener = listener;
    }

    public interface OnPatternListener {
        void onPatternDetected(String pattern);
    }

    private static class Point {
        float x;
        float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
