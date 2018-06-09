package com.qgg.practice.view.tabmenuview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/14 19:19
 * @Describe :
 * <p>
 * 动画在执行的情况下就不要在响应动画事件
 * 打开和关闭 变化tab的显示 ，传到外面去修改
 * 当菜单是打开的状态 不执行动画只切换显示内容
 */
public class TabMenuScreenView extends LinearLayout implements View.OnClickListener {
    private DataSetObserver mDataSetObserver;
    private Context mContext;
    /**
     * 头部用来存放 Tab
     */
    private LinearLayout mMenuTabView;
    /**
     * FrameLayout 用来存放 = 阴影（View） + 菜单内容布局(FrameLayout)
     */
    private FrameLayout mContentView;
    /**
     * 菜单用来存放菜单内容
     */
    private FrameLayout mMenuContainerView;
    private View mShadowView;
    private int mShadowColor = 0x88888888;
    private BaseMenuAdapter mAdapter;
    /**
     * 内容菜单的高度
     */
    private int mMenuContainerHeight;
    /**
     * 当前打开的位置
     */
    private int mCurrentPosition = -1;
    private long DURATION_TIME = 350;
    /**
     * 动画是否在执行
     */
    private boolean mAnimatorExecute;

    public TabMenuScreenView(Context context) {
        this(context, null);
    }

    public TabMenuScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabMenuScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();
    }

    /**
     * 布局实例化 （组合控件）
     */
    private void initLayout() {
        // 创建布局 ，再加载
        setOrientation(VERTICAL);
        mMenuTabView = new LinearLayout(mContext);
        mMenuTabView.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        addView(mMenuTabView);
        // 创建 FrameLayout 用来存放阴影（View） + 菜单内容布局(FrameLayout)
        mContentView = new FrameLayout(mContext);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        mContentView.setLayoutParams(params);
        addView(mContentView);
        // 创建阴影
        mShadowView = new View(mContext);
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setAlpha(0);
        mShadowView.setOnClickListener(this);
        mShadowView.setVisibility(GONE);
        mContentView.addView(mShadowView);
        // 创建菜单存放菜单内容
        mMenuContainerView = new FrameLayout(mContext);
        mMenuContainerView.setBackgroundColor(Color.WHITE);
        // 解决FrameLayout事件穿透
        mMenuContainerView.setClickable(true);
        mContentView.addView(mMenuContainerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("TAG", "onMeasure");
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //多次调用会导致bug
        if (mMenuContainerHeight == 0 && height > 0) {
            // 内容的高度是整个View的75%
            mMenuContainerHeight = (int) (height * 0.75f);
            ViewGroup.LayoutParams params = mMenuContainerView.getLayoutParams();
            params.height = mMenuContainerHeight;
            mMenuContainerView.setLayoutParams(params);
            // 初始化阴影不显示 ，内容也不显示的，把它移上去
            mMenuContainerView.setTranslationY(-mMenuContainerHeight);
        }
    }

    /**
     * 设置 Adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseMenuAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("适配器不能为 null ！");
        }

        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        this.mAdapter = adapter;
        mDataSetObserver = new MenuDataSetObserver();
        mAdapter.registerDataSetObserver(mDataSetObserver);
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            // 获取Tab
            View tabView = mAdapter.getTabView(i, mMenuTabView);
            mMenuTabView.addView(tabView);
            LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            tabView.setLayoutParams(params);
            // 设置tab的点击事件
            setTabClick(tabView, i);
            // 获取菜单的内容
            View menuView = mAdapter.getMenuView(i, mMenuContainerView);
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);
        }
    }

    /**
     * 设置tab的点击
     *
     * @param tabView
     * @param position
     */
    private void setTabClick(final View tabView, final int position) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition == -1) {
                    // 关闭的话就打开
                    openMenu(position, tabView);
                } else {
                    if (mCurrentPosition == position) {
                        // 打开了就关闭
                        closeMenu();
                    } else {
                        // 切换一下显示内容即可
                        View currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                        currentMenu.setVisibility(View.GONE);
                        mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
                        mCurrentPosition = position;

                        currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                        currentMenu.setVisibility(View.VISIBLE);
                        mAdapter.menuOpen(mMenuTabView.getChildAt(mCurrentPosition));
                    }
                }
            }
        });
    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {
        Toast.makeText(getContext(), "点击了关闭", Toast.LENGTH_SHORT).show();
        if (mAnimatorExecute) {
            return;
        }
        // 关闭动画  位移动画  透明度动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView, "translationY", 0, -mMenuContainerHeight);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 1f, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(DURATION_TIME);
        animatorSet.playTogether(translationAnimator, alphaAnimator);
        // 等关闭动画执行完 才隐藏当前菜单
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View menuView = mMenuContainerView.getChildAt(mCurrentPosition);
                mAnimatorExecute = false;
                mCurrentPosition = -1;
                mShadowView.setVisibility(GONE);
                if (menuView == null) {
                    return;
                }
                menuView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                //防止空指针异常
                if (mCurrentPosition == -1) {
                    return;
                }
                mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
            }
        });
        mAnimatorExecute = true;
        animatorSet.start();
    }

    /**
     * 打开菜单
     *
     * @param position
     * @param tabView
     */
    private void openMenu(final int position, final View tabView) {
        if (mAnimatorExecute) {
            return;
        }
        // 获取当前位置显示菜单，菜单显示出来
        View menuView = mMenuContainerView.getChildAt(position);
        menuView.setVisibility(View.VISIBLE);
        mShadowView.setVisibility(View.VISIBLE);

        // 开启动画  位移动画  透明度动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView, "translationY", -mMenuContainerHeight, 0);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(DURATION_TIME);
        animatorSet.playTogether(translationAnimator, alphaAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorExecute = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                // 把当前的tab 传到外面
                mAdapter.menuOpen(tabView);
            }
        });
        mAnimatorExecute = true;
        mCurrentPosition = position;
        animatorSet.start();
    }

    @Override
    public void onClick(View v) {
        closeMenu();
    }

    class MenuDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            closeMenu();
        }
    }
}
