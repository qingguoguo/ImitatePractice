package com.qgg.practice.view.recyclerview.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.qgg.practice.view.recyclerview.WrapRecyclerView;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/11 16:21
 * @Describe :下拉刷新的RecyclerView
 */
public class RefreshRecyclerView extends WrapRecyclerView {
    // 下拉刷新的辅助类
    private RefreshViewCreator mRefreshCreator;
    // 下拉刷新头部的高度
    private int mRefreshViewHeight = 0;
    // 下拉刷新的头部View
    private View mRefreshView;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 手指拖拽的阻力指数
    protected float mDragIndex = 0.35f;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前的状态
    private int mCurrentRefreshStatus;
    // 默认状态
    public int REFRESH_STATUS_NORMAL = 0x0011;
    // 下拉刷新状态
    public int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开刷新状态
    public int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // 正在刷新状态
    public int REFRESH_STATUS_REFRESHING = 0x0033;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 处理下拉刷新，同时考虑刷新列表的不同风格样式
     * 不直接添加 View，利用辅助类
     *
     * @param refreshCreator
     */
    public void addRefreshViewCreator(RefreshViewCreator refreshCreator) {
        this.mRefreshCreator = refreshCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    /**
     * 添加头部的刷新View
     */
    private void addRefreshView() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter != null && mRefreshCreator != null) {
            // 添加头部的刷新View
            View refreshView = mRefreshCreator.getRefreshView(getContext(), this);
            if (refreshView != null) {
                addHeadView(refreshView);
                this.mRefreshView = refreshView;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (mRefreshView != null && mRefreshViewHeight <= 0) {
                // 获取头部刷新View的高度
                mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                if (mRefreshViewHeight > 0) {
                    // 隐藏头部刷新的View  marginTop  留出1px防止无法判断是不是滚动到头部问题
                    setRefreshViewMarginTop(-mRefreshViewHeight + 1);
                }
            }
        }
    }

    /**
     * 设置 刷新View的 marginTop，默认隐藏状态，下拉显示出来
     *
     * @param marginTop
     */
    public void setRefreshViewMarginTop(int marginTop) {
        MarginLayoutParams params = (MarginLayoutParams) mRefreshView.getLayoutParams();
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }
        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ，写在dispatchTouchEvent，是因为如果处理了条目点击事件，
                // 就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //如果正在拖动 抬起的话就重置状态
                if (mCurrentDrag) {
                    restoreRefreshView();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 如果在最顶部才处理，否则不处理 或者正在刷新中
                if (canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING) {
                    // 如果没有到最顶端，什么都不处理
                    return super.onTouchEvent(e);
                }
                // 解决下拉刷新自动滚动
                if (mCurrentDrag) {
                    scrollToPosition(0);
                }
                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                // 如果是已经到达头部，并不断的下拉
                if (distanceY > 0) {
                    int marginTop = distanceY - mRefreshViewHeight;
                    //改变refreshView的marginTop的值
                    setRefreshViewMarginTop(marginTop);
                    //更新状态
                    updateRefreshStatus(marginTop);
                    mCurrentDrag = true;
                    return false;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 更新刷新的状态
     */
    private void updateRefreshStatus(int marginTop) {
        Log.e("updateRefreshStatus", "updateRefreshStatus,mRefreshViewHeight" + mRefreshViewHeight + ",marginTop:" + marginTop);
        if (marginTop <= -mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (marginTop < 0) {
            //下拉刷新状态
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {
            //下拉到顶了，变成松开刷新状态
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }

        if (mRefreshCreator != null) {
            mRefreshCreator.onPull(marginTop, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    /**
     * 松开手指或者更新完毕后 重置当前刷新状态
     */
    private void restoreRefreshView() {
        int currentTopMargin = ((MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        Log.e("updateRefreshStatus", "currentTopMargin：" + currentTopMargin);
        int finalTopMargin = -mRefreshViewHeight + 1;
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            finalTopMargin = 0;
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
            if (mRefreshCreator != null) {
                //正在刷新
                mRefreshCreator.onRefreshing();
            }
            if (mListener != null) {
                mListener.onRefresh();
            }
        }

        int distance = currentTopMargin - finalTopMargin;
        // 回弹到指定位置
        ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        mCurrentDrag = false;
    }

    /**
     * 判断是不是滚动到了最顶部，SwipeRefreshLayout的源码
     */
    public boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    /**
     * 停止刷新
     */
    public void onStopRefresh() {
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if (mRefreshCreator != null) {
            //停止刷新
            mRefreshCreator.onStopRefresh();
        }
    }

    /**
     * 处理刷新回调监听
     */
    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
