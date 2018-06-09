package com.qgg.practice.view.rotateloadingview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

import com.qgg.practice.R;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/15
 * @describe :
 * <p>
 * 实现思路
 * 旋转动画 + 聚拢动画
 * <p>
 * 关键在于圆心的确定，如何确定好六个圆，后面就好实现
 * 聚拢动画并不是通过位移实现，而是改变半径
 */
public class RotateLoadingView extends View {

    private int[] mColorArray;
    private Paint mPaint;
    private float mAngleAnimatedValue;
    private float mRadiusPercent;
    private float mCenterX;
    private float mCenterY;
    private OnEndListener mOnEndListener;
    /**
     * 圆心在所在圆的半径
     */
    private float mCentreCircleRadius;
    /**
     * 彩色小圆的半径
     */
    private float mColorCircleRadius;
    private float mHoleCircleRadius;
    private float mPercentAngle;
    private float mHoleAnimatedValueRadius;
    private LoadingAnimationState mLoadingState;

    public RotateLoadingView(Context context) {
        this(context, null);
    }

    public RotateLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setBackgroundColor(Color.WHITE);
        mRadiusPercent = 1;
        mColorArray = getResources().getIntArray(R.array.splash_circle_colors);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPercentAngle = (float) (2 * Math.PI / mColorArray.length);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mCenterX = getWidth() / 2;
            mCenterY = getHeight() / 2;
            mCentreCircleRadius = mCenterX / 2;
            mColorCircleRadius = mCentreCircleRadius / 10;
            mHoleCircleRadius = (float) Math.sqrt(Math.pow(mCenterX, 2) + Math.pow(mCenterY, 2));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("RotateLoadingView", "onDraw: ");
        if (mLoadingState == null) {
            mLoadingState = new RotateState();
        }
        mLoadingState.onDraw(canvas);
    }

    private abstract class LoadingAnimationState {
        public abstract void onDraw(Canvas canvas);
    }

    /**
     * 旋转动画
     */
    private class RotateState extends LoadingAnimationState {
        private ValueAnimator mValueAnimator;

        public RotateState() {
            mValueAnimator = startAnimationRotate();
        }

        @Override
        public void onDraw(Canvas canvas) {
            drawColorCirCle(canvas);
        }

        public void cancel() {
            mValueAnimator.cancel();
        }
    }

    /**
     * 聚拢动画
     */
    private class TogetherState extends LoadingAnimationState {
        public TogetherState() {
            startAnimationTogether();
        }

        @Override
        public void onDraw(Canvas canvas) {
            drawColorCirCle(canvas);
        }
    }

    /**
     * 圆形洞扩散动画
     */
    private class DisperseState extends LoadingAnimationState {

        public DisperseState() {
            startAnimationCircleDisperse();
            setBackgroundColor(Color.TRANSPARENT);
        }

        @Override
        public void onDraw(Canvas canvas) {
            // 从mHoleCircleRadius - 0
            float strokeWidth = mHoleCircleRadius - mHoleAnimatedValueRadius;
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.STROKE);
            // 从0 -mHoleCircleRadius
            float radius = mHoleCircleRadius - strokeWidth / 2;
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);
        }
    }

    /**
     * 绘制小圆
     *
     * @param canvas
     */
    private void drawColorCirCle(Canvas canvas) {
        for (int i = 0; i < mColorArray.length; i++) {
            mPaint.setColor(mColorArray[i]);
            double dx = Math.sin(mPercentAngle * i + mAngleAnimatedValue) * mCentreCircleRadius;
            double dy = Math.cos(mPercentAngle * i + mAngleAnimatedValue) * mCentreCircleRadius;
            canvas.drawCircle(mCenterX + (float) dx * mRadiusPercent, mCenterY + (float) dy * mRadiusPercent
                    , mColorCircleRadius, mPaint);
        }
    }

    /**
     * 旋转动画
     */
    private ValueAnimator startAnimationRotate() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float) (2 * Math.PI), 0);
        valueAnimator.setDuration(2000);
        // 线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAngleAnimatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setRepeatCount(-1);
        valueAnimator.start();
        return valueAnimator;
    }

    /**
     * 聚拢动画
     */
    private void startAnimationTogether() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0.1f);
        valueAnimator.setDuration(2000);
        // 插值器会先反方向回弹
        valueAnimator.setInterpolator(new AnticipateInterpolator(8));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRadiusPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingState = new DisperseState();
            }
        });
        valueAnimator.start();
    }

    /**
     * 圆形散开动画
     */
    private void startAnimationCircleDisperse() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mHoleCircleRadius);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHoleAnimatedValueRadius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnEndListener != null) {
                    mOnEndListener.onEnd();
                    setVisibility(INVISIBLE);
                }
            }
        });
        valueAnimator.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(INVISIBLE);
        this.clearAnimation();
        ViewGroup parent = (ViewGroup) this.getParent();
        if (parent != null) {
            parent.removeView(this);
        }
    }

    /**
     * 停止旋转动画
     */
    public void setOnStop() {
        if (mLoadingState != null && mLoadingState instanceof RotateState) {
            RotateState rotateState = (RotateState) mLoadingState;
            rotateState.cancel();
            mLoadingState = new TogetherState();
        }
    }

    public void setOnEndListener(OnEndListener onEndListener) {
        mOnEndListener = onEndListener;
    }

    /**
     * 动画完成回调
     */
    public interface OnEndListener {
        /**
         * 动画结束
         */
        void onEnd();
    }
}
