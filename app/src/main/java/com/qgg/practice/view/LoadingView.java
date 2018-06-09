package com.qgg.practice.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.qgg.practice.R;
import com.qgg.practice.Utils;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/14
 * @describe :仿 58 数据加载 View
 */

public class LoadingView extends LinearLayout {
    private View mShadowView;
    private ShapeView mShapeView;
    private boolean mIsStopAnimator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    private void initLayout() {
        inflate(getContext(), R.layout.layout_loading_view, this);
        mShadowView = findViewById(R.id.shadow_view);
        mShapeView = (ShapeView) findViewById(R.id.shape_view);

        //onResume之后才调用
        post(new Runnable() {
            @Override
            public void run() {
                startAnimationDown();
            }
        });
    }

    /**
     * 下落动画
     */
    private void startAnimationDown() {
        if (mIsStopAnimator) {
            return;
        }
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, Utils.dpToPixel(90));
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mShadowView, "scaleX", 0.3f, 1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(350);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(translationY, scaleX);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShapeView.exchange();
                startAnimationUp();
            }
        });
        animatorSet.start();
    }

    /**
     * 上抛动画
     */
    private void startAnimationUp() {
        if (mIsStopAnimator) {
            return;
        }
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mShapeView, "translationY", Utils.dpToPixel(90), 0);
        ObjectAnimator rotation = getRotationAnimator();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mShadowView, "scaleX", 1, 0.3f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(350);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(translationY, scaleX, rotation);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startAnimationDown();
            }
        });
        animatorSet.start();
    }

    /**
     * 获取当前ShapeView的形状
     *
     * @return
     */
    private ObjectAnimator getRotationAnimator() {
        int angle = 180;
        if (mShapeView.getCurrentShape() == ShapeView.Shape.Triangle) {
            angle = 120;
        }
        return ObjectAnimator.ofFloat(mShapeView, "rotation", 0, angle);
    }

    @Override
    public void setVisibility(int visibility) {
        // 不需要去摆放和计算，少走一些系统的源码（View的绘制流程）
        super.setVisibility(INVISIBLE);
        mShapeView.clearAnimation();
        mShadowView.clearAnimation();
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
        removeAllViews();
        mIsStopAnimator = true;
    }
}
