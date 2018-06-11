package com.qgg.practice.view.banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义无限轮播viewpager
 */
public class BannerViewPager extends ViewPager {

    private static final String TAG = "BannerViewPager";

    /**
     * 自定义 BannerViewPager - 自定义的Adapter
     */
    private BannerAdapter mAdapter;

    /**
     * 实现自动轮播 - 发送消息的msgWhat
     */
    private final int SCROLL_MSG = 0x0011;

    /**
     * 实现自动轮播 - 页面切换间隔时间
     */
    private int mCutDownTime = 3500;

    /**
     * 改变ViewPager切换的速率 - 自定义的页面切换的Scroller
     */
    private BannerScroller mScroller;

    /**
     * 实现自动轮播 - 发送消息Handler
     */
    private Handler mHandler;

    /**
     * 内存优化 --> 当前Activity内存优化 --> 复用的View
     */
    private Activity mActivity;
    private List<View> mConvertViews;

    /**
     * 是否可以滚动
     */

    private boolean mScrollAble = true;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;

        try {
            //改变ViewPager切换的速率
            Field field = ViewPager.class.getDeclaredField("mScroller");
            mScroller = new BannerScroller(context);
            field.setAccessible(true);
            field.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mConvertViews = new ArrayList<>();
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 每隔*s后切换到下一页
                setCurrentItem(getCurrentItem() + 1);
                // 不断循环执行
                startRoll();
            }
        };
    }

    /**
     * 设置切换页面动画持续的时间
     */
    public void setScrollerDuration(int scrollerDuration) {
        mScroller.setScrollerDuration(scrollerDuration);
    }

    /**
     * 设置自定义的BannerAdapter
     */
    public void setAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;
        //设置父ViewPager的adapter
        setAdapter(new BannerPagerAdapter());
    }

    /**
     * 实现自动轮播
     */
    public void startRoll() {
        // adapter不能是空
        if (mAdapter == null) {
            return;
        }

        // 判断是不是只有一条数据
        mScrollAble = mAdapter.getCount() != 1;
        if (mScrollAble && mHandler != null) {
            // 清除消息
            mHandler.removeMessages(SCROLL_MSG);
            // 消息  延迟时间  让用户自定义  有一个默认  3500
            mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
        }
    }

    /**
     * 销毁Handler停止发送 解决内存泄漏
     */
    @Override
    protected void onDetachedFromWindow() {
        if (mHandler != null) {
            // 销毁Handler的生命周期
            mHandler.removeMessages(SCROLL_MSG);
            // 解除绑定
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            mHandler = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * Handler
     */
    @Override
    protected void onAttachedToWindow() {
        Log.e(TAG, "onAttachedToWindow: ");
        if (mAdapter != null) {
            initHandler();
            startRoll();
            // 管理Activity的生命周期
            mActivity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
        super.onAttachedToWindow();
    }

    /**
     * 给ViewPager设置适配器
     */
    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 创建ViewPager item回调的方法
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // Adapter 设计模式为了完全让用户自定义
            View bannerItemView = mAdapter.getView(position % mAdapter.getCount(), getConvertView());
            // 添加ViewPager里面
            container.addView(bannerItemView);
            bannerItemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 回调点击监听
                    if (mListener != null) {
                        mListener.click(position % mAdapter.getCount());
                    }
                }
            });
            return bannerItemView;
        }

        /**
         * 销毁条目回调的方法
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            mConvertViews.add((View) object);
        }
    }

    /**
     * 获取复用界面
     */
    public View getConvertView() {
        for (int i = 0; i < mConvertViews.size(); i++) {
            if (mConvertViews.get(i).getParent() == null) {
                return mConvertViews.get(i);
            }
        }
        return null;
    }

    /**
     * 设置点击回调监听
     */
    private BannerItemClickListener mListener;

    public void setOnBannerItemClickListener(BannerItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 点击回调监听
     */
    public interface BannerItemClickListener {
        void click(int position);
    }

    /**
     * 管理Activity的生命周期
     */
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks =
            new DefaultActivityLifecycleCallbacks() {
                @Override
                public void onActivityResumed(Activity activity) {
                    // 是不是监听的当前Activity的生命周期
                    // Log.e(TAG, "activity --> " + activity + "  context-->" + getContext())
                    if (activity == mActivity) {
                        // 开启轮播
                        startRoll();
                        // mHandler.sendEmptyMessageDelayed(mCutDownTime, SCROLL_MSG);
                    }
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    if (activity == mActivity) {
                        // 停止轮播
                        mHandler.removeMessages(SCROLL_MSG);
                    }
                }
            };
}
