package com.qgg.practice.view.loadingcircleview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/14
 * @describe :
 */
public class LoadingCircleView extends RelativeLayout {

    private BaseCircleView mLeftCircleView;
    private BaseCircleView mRightCircleView;
    private BaseCircleView mMiddleCircleView;
    private int mDuration;
    private float mDistance;
    boolean isStopAnimation;

    public LoadingCircleView(Context context) {
        this(context, null);
    }

    public LoadingCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setBackgroundColor(Color.parseColor("#ffffff"));
        mDuration = 1000;
        mDistance = dpToPx(60);

        mLeftCircleView = getCircleView(Color.RED);
        mRightCircleView = getCircleView(Color.GREEN);
        mMiddleCircleView = getCircleView(Color.BLUE);

        addView(mLeftCircleView);
        addView(mRightCircleView);
        addView(mMiddleCircleView);
        post(new Runnable() {
            @Override
            public void run() {
                startAnimationScatter();
            }
        });
    }

    /**
     * 创建CircleView
     *
     * @param color
     * @return
     */
    private BaseCircleView getCircleView(int color) {
        BaseCircleView baseCircleView = new BaseCircleView(getContext());
        baseCircleView.changeColor(color);
        LayoutParams layoutParams = new LayoutParams(dpToPx(10), dpToPx(10));
        layoutParams.addRule(CENTER_IN_PARENT);
        baseCircleView.setLayoutParams(layoutParams);
        return baseCircleView;
    }

    /**
     * 往外动画
     */
    private void startAnimationScatter() {
        if (isStopAnimation) {
            return;
        }
        ObjectAnimator leftTranslationX = ObjectAnimator.ofFloat(mLeftCircleView, "translationX", 0, -mDistance);
        ObjectAnimator rightTranslationX = ObjectAnimator.ofFloat(mRightCircleView, "translationX", 0, mDistance);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(leftTranslationX, rightTranslationX);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startAnimationTogether();
            }
        });
        animatorSet.start();
    }

    /**
     * 往里动画
     */
    private void startAnimationTogether() {
        if (isStopAnimation) {
            return;
        }
        ObjectAnimator leftTranslationX = ObjectAnimator.ofFloat(mLeftCircleView, "translationX", -mDistance, 0);
        ObjectAnimator rightTranslationX = ObjectAnimator.ofFloat(mRightCircleView, "translationX", mDistance, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(leftTranslationX, rightTranslationX);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeColor();
                startAnimationScatter();
            }
        });
        animatorSet.start();
    }

    /**
     * 更换颜色
     */
    private void changeColor() {
        int leftCircleViewColor = mLeftCircleView.getColor();
        int middleCircleViewColor = mMiddleCircleView.getColor();
        int rightCircleViewColor = mRightCircleView.getColor();

        mMiddleCircleView.changeColor(leftCircleViewColor);
        mRightCircleView.changeColor(middleCircleViewColor);
        mLeftCircleView.changeColor(rightCircleViewColor);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(INVISIBLE);
        isStopAnimation = true;
        mLeftCircleView.clearAnimation();
        mMiddleCircleView.clearAnimation();
        mRightCircleView.clearAnimation();
        removeAllViews();
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStopAnimation = true;
    }

    private int dpToPx(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                getResources().getDisplayMetrics());
    }
}
