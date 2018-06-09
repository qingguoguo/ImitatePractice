package com.qgg.practice.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/7
 * @describe :
 */

public class LockView extends View {

    private int mOffsetY;
    private int mOffsetX;
    private int mSquareWidth;
    private Point[][] mPoints;

    private Paint mLinePaint;
    private Paint mPressedPaint;
    private Paint mErrorPaint;
    private Paint mNormalPaint;
    private Paint mArrowPaint;

    private int mOuterPressedColor = 0xff8cbad8;
    private int mInnerPressedColor = 0xff0596f6;
    private int mOuterNormalColor = 0xffd9d9d9;
    private int mInnerNormalColor = 0xff929292;
    private int mOuterErrorColor = 0xff901032;
    private int mInnerErrorColor = 0xffea0945;

    private boolean mIsTouchPoint;
    private int mCircleRadius;
    private List<Point> mTouchPointList = new ArrayList<>();

    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPoints(context);
        initPaint();
    }

    private void initPaint() {
        //设置 paint 颜色
        mLinePaint = new Paint();
        mLinePaint.setColor(mInnerPressedColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeWidth((mCircleRadius / 9));
        // 按下的画笔
        mPressedPaint = new Paint();
        mPressedPaint.setStyle(Paint.Style.STROKE);
        mPressedPaint.setAntiAlias(true);
        mPressedPaint.setStrokeWidth((mCircleRadius / 6));
        // 错误的画笔
        mErrorPaint = new Paint();
        mErrorPaint.setStyle(Paint.Style.STROKE);
        mErrorPaint.setAntiAlias(true);
        mErrorPaint.setStrokeWidth((mCircleRadius / 6));
        // 默认的画笔
        mNormalPaint = new Paint();
        mNormalPaint.setStyle(Paint.Style.STROKE);
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setStrokeWidth((mCircleRadius / 6));
        // 箭头的画笔
        mArrowPaint = new Paint();
        mArrowPaint.setColor(mInnerPressedColor);
        mArrowPaint.setStyle(Paint.Style.STROKE);
        mArrowPaint.setAntiAlias(true);
        mArrowPaint.setStrokeWidth((mCircleRadius / 6));
    }

    private void initPoints(Context context) {
        mPoints = new Point[3][3];
        int screenWidth = getScreenWidth(context);
        int screenHeight = getScreenHeight(context);
        if (screenHeight >= screenWidth) {
            //竖屏
            mSquareWidth = screenWidth / 4;
            mOffsetY = (screenHeight - screenWidth) / 2;
        } else {
            //横屏
            mSquareWidth = screenHeight / 4;
            mOffsetX = (screenWidth - screenHeight) / 2;
        }
        Log.e("LockView", "init, mOffsetX:" + mOffsetX + " ,mOffsetY:" + mOffsetY);
        //******************屏幕分四份**************************//
//        int centreX, centreY;
//        for (int i = 0; i < 3; i++) {
//            centreY = mOffsetY + mSquareWidth * (i + 1);
//            for (int j = 0; j < 3; j++) {
//                centreX = mOffsetX + mSquareWidth * (j + 1);
//                mPoints[i][j] = new Point(centreX, centreY, j + 1 + 3 * (i));
//            }
//        }
        //******************屏幕分三份**************************//

        int mWidth = screenWidth / 6;
        mCircleRadius = mWidth / 2;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mPoints[i][j] = new Point(mOffsetX + mWidth * (1 + j * 2),
                        mOffsetY + mWidth * (1 + i * 2), j + 1 + 3 * (i));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawShow(canvas);
    }

    private void drawShow(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Point point = mPoints[i][j];
                Log.e("LockView", "onDraw, point:" + point);
                if (point.getStatus() == Point.STATUS_NORMAL) {
                    mNormalPaint.setColor(mOuterNormalColor);
                    canvas.drawCircle(point.centreX, point.centreY, mCircleRadius, mNormalPaint);

                    mNormalPaint.setColor(mInnerNormalColor);
                    canvas.drawCircle(point.centreX, point.centreY, mCircleRadius / 6, mNormalPaint);

                } else if (point.getStatus() == Point.STATUS_PRESSED) {
                    mPressedPaint.setColor(mOuterPressedColor);
                    canvas.drawCircle(point.centreX, point.centreY, mCircleRadius, mPressedPaint);

                    mPressedPaint.setColor(mInnerPressedColor);
                    canvas.drawCircle(point.centreX, point.centreY, mCircleRadius / 6, mPressedPaint);

                } else if (point.getStatus() == Point.STATUS_ERROR) {
                    mErrorPaint.setColor(mOuterErrorColor);
                    canvas.drawCircle(point.centreX, point.centreY, mCircleRadius, mErrorPaint);
                    mErrorPaint.setColor(mInnerErrorColor);
                    canvas.drawCircle(point.centreX, point.centreY, mCircleRadius / 6, mErrorPaint);

                }
            }
        }
        //画线
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        int size = mTouchPointList.size();
        if (size == 0) {
            return;
        }
        Point lastPoint = mTouchPointList.get(0);
        for (int i = 1; i < size; i++) {
            Point point = mTouchPointList.get(i);
            drawLine(lastPoint, point, canvas);
            lastPoint = point;
        }

        //移动的点不在圆内才画线
        if (!checkInRound(lastPoint, mCircleRadius / 4, mMovePointX, mMovePointY) && mIsTouchPoint) {
            drawLine(lastPoint, new Point(mMovePointX, mMovePointY), canvas);
        }
    }

    private void drawLine(Point lastPoint, Point point, Canvas canvas) {
        int i = point.centreX - lastPoint.centreX;
        int j = point.centreY - lastPoint.centreY;
        double sinA = (i) / Math.sqrt(i * i + j * j);
        double cosA = (j) / Math.sqrt(i * i + j * j);
        int dx = (int) (sinA * mCircleRadius / 6);
        int dy = (int) (cosA * mCircleRadius / 6);

        int sx = (int) (sinA * mCircleRadius / 6);
        int sy = (int) (cosA * mCircleRadius / 6);

        canvas.drawLine(lastPoint.centreX + dx, lastPoint.centreY + dy,
                point.centreX - sx, point.centreY - sy, mLinePaint);
    }

    float mMovePointX;
    float mMovePointY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        mMovePointX = x;
        mMovePointY = y;
        Point point = getPoint(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (point != null) {
                    mIsTouchPoint = true;
                    mTouchPointList.add(point);
                    point.setStatus(Point.STATUS_PRESSED);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //按在点上移动,判断移动点是否在圆内
                if (mIsTouchPoint) {
                    if (point != null) {
                        if (!mTouchPointList.contains(point)) {
                            point.setStatus(Point.STATUS_PRESSED);
                            mTouchPointList.add(point);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsTouchPoint = false;
                String password = getPassword();
                if (mOnUnlockedListener != null) {
                    boolean unlocked = mOnUnlockedListener.unlocking(password);
                    if (!unlocked) {
                        unlockingError();
                    }
                }
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTouchPointList.clear();
                        clearStatus();
                        invalidate();
                    }
                }, 300);

                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    private void clearStatus() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mPoints[i][j].setStatus(Point.STATUS_NORMAL);
            }
        }
    }

    private void unlockingError() {
        for (Point point : mTouchPointList) {
            point.setStatus(Point.STATUS_ERROR);
        }
    }

    /**
     * 获取按下位置所在的圆
     */
    private Point getPoint(float x, float y) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boolean b = checkInRound(mPoints[i][j], mCircleRadius, x, y);
                if (b) {
                    return mPoints[i][j];
                }
            }
        }
        return null;
    }

    /**
     * 判断按下的点是否在圆内
     */
    private boolean checkInRound(Point point, float r, float x, float y) {
        return (x - point.centreX) * (x - point.centreX) + (y - point.centreY) * (y - point.centreY) < r * r;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * Dip into pixels
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private onUnlockedListener mOnUnlockedListener;

    public void setOnUnlockedListener(onUnlockedListener onUnlockedListener) {
        mOnUnlockedListener = onUnlockedListener;
    }

    public String getPassword() {
        StringBuilder password = new StringBuilder();
        for (Point point : mTouchPointList) {
            password.append(point.index);
        }
        return password.toString();
    }

    public interface onUnlockedListener {
        boolean unlocking(String password);
    }

    class Point {
        private int centreX;
        private int centreY;
        private int index;
        private int mStatus;
        private static final int STATUS_NORMAL = 0;
        private static final int STATUS_PRESSED = 1;
        private static final int STATUS_ERROR = 2;

        public Point(float centreX, float centreY) {
            this.centreX = (int) centreX;
            this.centreY = (int) centreY;
        }

        public Point(int centreX, int centreY, int index) {
            this.centreX = centreX;
            this.centreY = centreY;
            this.index = index;
        }

        public void setStatus(int status) {
            mStatus = status;
        }

        public int getStatus() {
            return mStatus;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (centreX != point.centreX) return false;
            if (centreY != point.centreY) return false;
            if (index != point.index) return false;
            return mStatus == point.mStatus;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "centreX=" + centreX +
                    ", centreY=" + centreY +
                    ", index=" + index +
                    ", mStatus=" + mStatus +
                    '}';
        }
    }
}
