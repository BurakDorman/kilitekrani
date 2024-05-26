package com.example.kilitekrani;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.drawable.Drawable;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatternView extends View {

    private static final int GRID_SIZE = 3;
    private static final int ICON_SIZE = 100;
    private static final int LINE_WIDTH = 10;

    private Paint paint;
    private List<Point> points;
    private List<Point> selectedPoints;
    private Drawable[] fruitIcons;
    private int[] fruitIndex;
    private Path path;
    private OnPatternListener onPatternListener;

    public PatternView(Context context) {
        super(context);
        init(context);
    }

    public PatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(0xFF000000); // Siyah renk
        paint.setStrokeWidth(LINE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0)); // Kesik çizgi efekti

        points = new ArrayList<>();
        selectedPoints = new ArrayList<>();
        path = new Path();

        // Meyve ikonlarını yükleyin
        fruitIcons = new Drawable[9];
        fruitIcons[0] = ContextCompat.getDrawable(context, R.drawable.apple);
        fruitIcons[1] = ContextCompat.getDrawable(context, R.drawable.banana);
        fruitIcons[2] = ContextCompat.getDrawable(context, R.drawable.cherry);
        fruitIcons[3] = ContextCompat.getDrawable(context, R.drawable.grape);
        fruitIcons[4] = ContextCompat.getDrawable(context, R.drawable.lemon);
        fruitIcons[5] = ContextCompat.getDrawable(context, R.drawable.orange);
        fruitIcons[6] = ContextCompat.getDrawable(context, R.drawable.peach);
        fruitIcons[7] = ContextCompat.getDrawable(context, R.drawable.strawberry);
        fruitIcons[8] = ContextCompat.getDrawable(context, R.drawable.watermelon);

        fruitIndex = new int[9];
        shuffleFruitIcons();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPoints(w, h);
    }

    private void initPoints(int viewWidth, int viewHeight) {
        points.clear();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                points.add(new Point((i + 1) * (viewWidth / (GRID_SIZE + 1)), (j + 1) * (viewHeight / (GRID_SIZE + 1))));
            }
        }
    }

    private void shuffleFruitIcons() {
        List<Integer> iconIndices = new ArrayList<>();
        for (int i = 0; i < fruitIcons.length; i++) {
            iconIndices.add(i);
        }
        Collections.shuffle(iconIndices);
        for (int i = 0; i < fruitIcons.length; i++) {
            fruitIndex[i] = iconIndices.get(i);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIcons(canvas);
        drawPath(canvas);
    }

    private void drawIcons(Canvas canvas) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            Drawable icon = fruitIcons[fruitIndex[i]];
            int left = (int) (point.x - ICON_SIZE / 2);
            int top = (int) (point.y - ICON_SIZE / 2);
            int right = (int) (point.x + ICON_SIZE / 2);
            int bottom = (int) (point.y + ICON_SIZE / 2);
            icon.setBounds(left, top, right, bottom);
            icon.draw(canvas);
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
            if (Math.sqrt(Math.pow((point.x - x), 2) + Math.pow((point.y - y), 2)) <= ICON_SIZE / 2) {
                return point;
            }
        }
        return null;
    }

    public String getPattern() {
        StringBuilder patternBuilder = new StringBuilder();
        for (Point point : selectedPoints) {
            patternBuilder.append(fruitIndex[points.indexOf(point)]);
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
