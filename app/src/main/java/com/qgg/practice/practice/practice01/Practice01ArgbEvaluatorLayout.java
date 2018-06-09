package com.qgg.practice.practice.practice01;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.qgg.practice.R;
import com.qgg.practice.retrofitdemo.RetrofitTestActivity;
import com.qgg.practice.view.ColorTrackTextView;

public class Practice01ArgbEvaluatorLayout extends RelativeLayout {
    Practice01ArgbEvaluatorView view;
    Button animateBt;

    public Practice01ArgbEvaluatorLayout(Context context) {
        super(context);
    }

    public Practice01ArgbEvaluatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice01ArgbEvaluatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        view = (Practice01ArgbEvaluatorView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);
//
//        animateBt.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000, 0xff00ff00);
//                // 在这里使用 ObjectAnimator.setEvaluator() 来设置 ArgbEvaluator，修复闪烁问题
//                animator.setInterpolator(new LinearInterpolator());
//                animator.setEvaluator(new ArgbEvaluator());
//                animator.setDuration(2000);
//                animator.start();
//            }
//        });
        final ColorTrackTextView trackTextView = (ColorTrackTextView) findViewById(R.id.color_track_tv);
        //trackTextView.setPosition(0.1f);
        ValueAnimator trackValueAnimator = ObjectAnimator.ofFloat(0, 1);
        trackValueAnimator.setDuration(3000);
        trackValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                trackTextView.setPosition((Float) animation.getAnimatedValue());
            }
        });
        trackValueAnimator.start();

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //getContext().startActivity(new Intent(getContext(), ViewPagerActivity.class));
                //getContext().startActivity(new Intent(getContext(), TestActivity.class));
                //getContext().startActivity(new Intent(getContext(), ViewDragHelpActivity.class));
                //getContext().startActivity(new Intent(getContext(), LockViewActivity.class));
                //getContext().startActivity(new Intent(getContext(), RecyclerViewTestActivity.class));
                //getContext().startActivity(new Intent(getContext(), ShapeViewTestActivity.class));
                //getContext().startActivity(new Intent(getContext(), TabMenuViewActivity.class));
                //getContext().startActivity(new Intent(getContext(), LoadingCircleActivity.class));
                //getContext().startActivity(new Intent(getContext(), ParallaxActivity.class));
                //getContext().startActivity(new Intent(getContext(), RotateLoadingViewActivity.class));
                //getContext().startActivity(new Intent(getContext(), AopNetWorkActivity.class));
                //getContext().startActivity(new Intent(getContext(), NavigationBarTestActivity.class));
                //getContext().startActivity(new Intent(getContext(), TestRjDemo1Activity.class));
                //getContext().startActivity(new Intent(getContext(), TestRxLoginActivity.class));
                getContext().startActivity(new Intent(getContext(), RetrofitTestActivity.class));
//                trackTextView.setOrientati(ColorTrackTextView.Orientation.right_to_left);
//
//                ValueAnimator trackValueAnimator = ObjectAnimator.ofFloat(0, 1);
//                trackValueAnimator.setDuration(3000);
//                trackValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        trackTextView.setPosition((Float) animation.getAnimatedValue());
//                    }
//                });
//                trackValueAnimator.start();
            }
        });
    }
}
