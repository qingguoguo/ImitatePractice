package com.qgg.practice.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.qgg.practice.view.kgslidingmenu.ScreenUtils;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/5
 * @describe :自定义 ViewPager兼容侧滑
 */
public class CusTouchViewPager extends ViewPager {
    public CusTouchViewPager(Context context) {
        super(context);
    }

    public CusTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int curPosition;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                curPosition = this.getCurrentItem();
                int count = this.getAdapter().getCount();
                Log.i("TAG", "curPosition:=" + curPosition);
                //当ViewPager页面在最后一页和第0页的时候，由父View拦截触摸事件
                //if (curPosition == count - 1 || curPosition == 0) {
                //只处理左半边屏幕的事件
                if (curPosition == 0 && ev.getX() < ScreenUtils.getScreenWidth(getContext()) / 2) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //其他情况，由自己拦截触摸事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
