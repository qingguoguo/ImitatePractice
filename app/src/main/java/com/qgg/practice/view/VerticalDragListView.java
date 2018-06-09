package com.qgg.practice.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/6
 * @describe :仿汽车之家折叠列表
 */
public class VerticalDragListView extends FrameLayout {

    ViewDragHelper mViewDragHelper;
    private ViewDragHelper.Callback mViewDragHelperCallBack;
    private View menuView;
    private View mDragListView;
    private int menuHeight;
    private float mMotionEventY;
    /**
     * 菜单是否打开
     */
    private boolean menuIsOpen;

    public VerticalDragListView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            menuHeight = menuView.getHeight();
        }
    }

    private void init() {
        mViewDragHelperCallBack = new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //指定子View是否可以拖动
                return mDragListView == child;
            }

            //只能上下滑动，不能左右滑动不复写这个方法
//            @Override
//            public int clampViewPositionHorizontal(View child, int left, int dx) {
//                return left;
//            }
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (child == menuView) {
                    return 0;
                }
                if (child == mDragListView) {
                    if (top > menuHeight) {
                        top = menuHeight;
                    }

                    if (top < 0) {
                        top = 0;
                    }
                }
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //Log.e("VerticalDragListView", "onViewReleased,menuHeight / 2:" + menuHeight / 2 + " ,releasedChild:" + releasedChild + " , xvel: " + xvel + " ,yvel:" + yvel);
                //没有拖动的时候 回调此方法
                if (releasedChild != mDragListView) {
                    return;
                }
                if (mDragListView.getTop() > menuHeight / 2) {
                    //滚动到菜单高度，相当于打开
                    mViewDragHelper.settleCapturedViewAt(0, menuHeight);
                    menuIsOpen = true;
                    invalidate();
                } else {
                    //滚动到0位置，相当于关闭
                    mViewDragHelper.settleCapturedViewAt(0, 0);
                    menuIsOpen = false;
                    invalidate();
                }
            }
        };
        mViewDragHelper = ViewDragHelper.create(this, 0.8f, mViewDragHelperCallBack);
    }

    /**
     * 设置自动回弹需要重写该方法
     */
    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException(this + "有且仅能有两个子View");
        }
        menuView = getChildAt(0);
        mDragListView = getChildAt(1);
    }

    /**
     * ListView请求拦截了
     * requestDisallowInterceptTouchEvent
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //菜单打开状态，拦截
        if (menuIsOpen) {
            return true;
        }
        //向下滑动拦截不给ListView处理，向上滑动不拦截
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMotionEventY = y;
                mViewDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                //下滑拦截 && 滚动到了顶部，不让ListView处理,自己处理
                if ((y - mMotionEventY) > 0 && !canChildScrollUp()) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mViewDragHelper.processTouchEvent(ev);
        return true;
    }

    /**
     * 判断是否滚动到了最顶部,还能不能向上滚
     *
     * @return
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mDragListView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mDragListView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mDragListView, -1) || mDragListView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mDragListView, -1);
        }
    }
}
