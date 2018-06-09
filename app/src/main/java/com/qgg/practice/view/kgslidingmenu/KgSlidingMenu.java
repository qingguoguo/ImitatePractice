package com.qgg.practice.view.kgslidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import com.qgg.practice.R;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/4
 * @describe :
 */

public class KgSlidingMenu extends HorizontalScrollView {

    private int menuRightWidth;
    private View mContentView;
    private View menuView;
    /**
     * 是否拦截子View的事件
     */
    private boolean mIsInterceptTouch;
    /**
     * 手势处理类
     */
    private GestureDetector mGestureDetector;

    public KgSlidingMenu(Context context) {
        this(context, null);
    }

    public KgSlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KgSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //1.获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KgSlidingMenu);
        menuRightWidth = typedArray.getDimensionPixelSize(R.styleable.KgSlidingMenu_menuRightWidth,
                dip2px(context, 40));
        typedArray.recycle();
        menuRightWidth = getScreenWidth(context) - menuRightWidth;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.e("onFling", "velocityX: " + velocityX + "  velocityY:" + velocityY);
                //7.快速滑动监听
                //如果打开状态，快速左滑就关闭
                //左右滑动的距离小于上下滑动的距离 不做处理
                if (Math.abs(velocityX) < Math.abs(velocityY)) {
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
                //左右滑动的距离大于上下滑动的距离
                if (isOpenMenu()) {
                    if (velocityX < 0 && Math.abs(velocityX) > 100) {
                        closeMenu();
                        return true;
                    }
                } else {
                    //如果关闭状态，快速右滑，就打开
                    if (velocityX > 0) {
                        openMenu();
                        return true;
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //XML布局文件解析完毕
        //右侧内容页面的宽度是屏幕的宽度
        //左侧菜单的宽度是 屏幕的宽度-内容页面缩放后的宽度

        //2.给菜单和内容设置宽度
        ViewGroup container = (ViewGroup) getChildAt(0);
        if (container.getChildCount() != 2) {
            throw new RuntimeException(this + "只能放置两个子View!");
        }
        menuView = container.getChildAt(0);
        mContentView = container.getChildAt(1);

        ViewGroup.LayoutParams menuViewParams = menuView.getLayoutParams();
        ViewGroup.LayoutParams contentViewParams = mContentView.getLayoutParams();

        menuViewParams.width = menuRightWidth;
        contentViewParams.width = getScreenWidth(getContext());
        menuView.setLayoutParams(menuViewParams);
        mContentView.setLayoutParams(contentViewParams);
        //在这里调用 scrollTo才有用
        //3.scrollTo(menuRightWidth, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //3.初始化进来是菜单是关闭状态,需要在布局onLayout后才能滑动
        scrollTo(menuRightWidth, 0);
    }

    /**
     * 判断menu状态，true是打开，false关闭
     *
     * @return
     */
    private boolean isOpenMenu() {
        if (getScrollX() > menuRightWidth / 2) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 打开菜单
     */
    private void openMenu() {
        smoothScrollTo(0, 0);
    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {
        smoothScrollTo(menuRightWidth, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsInterceptTouch = false;
        //6.菜单打开状态需要拦截右侧内容页面的touch的事件，关闭菜单
        if (isOpenMenu()) {
            if (ev.getX() > menuRightWidth) {
                mIsInterceptTouch = true;
                closeMenu();
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //6.1 onInterceptTouchEvent拦截后，会走onTouchEvent，会影响到菜单的开闭状态
        if (mIsInterceptTouch) {
            return true;
        }

        //7.1 快速滑动监听 要消费掉事件
        boolean b = mGestureDetector.onTouchEvent(ev);
        if (b) {
            return true;
        }

        //4.手指抬起判断，关闭或打开状态
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (isOpenMenu()) {
                //打开菜单
                openMenu();
            } else {
                //关闭菜单
                closeMenu();
            }
            //需要返回true,HorizontalScrollView重写了onTouchEvent
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("TAG", "onScrollChanged ,l: " + l + " , t:" + t + " , oldl:" + oldl + " , oldt:" + oldt);
        //5.处理缩放和透明度,根据滑动距离改变缩放
        //scale 范围是1-0 打开 scale=0，关闭 scale=1
        float scale = 1f * l / menuRightWidth;
        //5.1设置内容页面 的缩放 设定范围最小是0.7f 最大是1f
        float rightScale = 0.7f + 0.3f * scale;
        //改变缩放中心
        ViewCompat.setPivotX(mContentView, 0);
        ViewCompat.setPivotY(mContentView, mContentView.getHeight() / 2);
        //设置缩放
        ViewCompat.setScaleX(mContentView, rightScale);
        ViewCompat.setScaleY(mContentView, rightScale);

        //5.2设置菜单的透明度,缩放,位移 表现出抽屉效果
        float menuScale = 1 - 0.3f * scale;
        float menuAlpha = 1 - 0.5f * scale;
        //设置缩放，透明度
        ViewCompat.setScaleX(menuView, menuScale);
        ViewCompat.setScaleY(menuView, menuScale);
        ViewCompat.setAlpha(menuView, menuAlpha);
        ViewCompat.setTranslationX(menuView, 0.25f * l);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * Dip into pixels
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
