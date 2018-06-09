package com.qgg.practice.view.navigationbar;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/23
 * @describe :导航栏基类
 */

public class AbsNavigationBar<B extends AbsNavigationBar.Builder> implements INavigationBar {

    private B mBuilder;
    private View mNavigationBar;

    /**
     * protected修饰，不允许直接new 创建对象
     *
     * @param builder
     */
    protected AbsNavigationBar(B builder) {
        this.mBuilder = builder;
        createNavigationBar();
    }

    /**
     * 创建NavigationBar
     */
    @Override
    public void createNavigationBar() {
        mNavigationBar = LayoutInflater.from(mBuilder.getContext())
                .inflate(mBuilder.getLayoutRes(), mBuilder.getParent(), false);
        attachParent(mNavigationBar, mBuilder.getParent());
        attachNavigationBarParams();
    }

    @Override
    public void attachNavigationBarParams() {
        SparseArray<String> textSparseArray = mBuilder.getTextSparseArray();
        int textSize = textSparseArray.size();
        for (int i = 0; i < textSize; i++) {
            int viewId = textSparseArray.keyAt(0);
            TextView textView = findViewById(viewId);
            textView.setText(textSparseArray.valueAt(i));
        }

        SparseArray<View.OnClickListener> clickListenerSparseArray = mBuilder.getClickListenerSparseArray();
        int clickSize = clickListenerSparseArray.size();
        for (int i = 0; i < clickSize; i++) {
            int viewId = clickListenerSparseArray.keyAt(0);
            ((View) findViewById(viewId)).setOnClickListener(clickListenerSparseArray.valueAt(i));
        }
    }

    private <V> V findViewById(int viewId) {
        return (V) mNavigationBar.findViewById(viewId);
    }

    /**
     * 添加到父布局
     *
     * @param navigationBar
     * @param parent
     */
    @Override
    public void attachParent(View navigationBar, ViewGroup parent) {
        parent.addView(navigationBar, 0);
    }

    public B getBuilder() {
        return mBuilder;
    }

    public abstract static class Builder<B extends Builder> {
        @LayoutRes
        private int mLayoutRes;
        private ViewGroup mParent;
        private Context mContext;
        private SparseArray<String> mTextSparseArray;
        private SparseArray<View.OnClickListener> mClickListenerSparseArray;

        public int getLayoutRes() {
            return mLayoutRes;
        }

        public ViewGroup getParent() {
            return mParent;
        }

        public Context getContext() {
            return mContext;
        }

        public SparseArray<String> getTextSparseArray() {
            return mTextSparseArray;
        }

        public SparseArray<View.OnClickListener> getClickListenerSparseArray() {
            return mClickListenerSparseArray;
        }

        public Builder(Context context, int layoutRes, ViewGroup parent) {
            mLayoutRes = layoutRes;
            mParent = parent;
            mContext = context;
            mTextSparseArray = new SparseArray<>();
            mClickListenerSparseArray = new SparseArray<>();
        }

        public abstract AbsNavigationBar create();

        public B setText(int viewId, String text) {
            mTextSparseArray.put(viewId, text);
            return (B) this;
        }

        public B setOnClickListener(int viewId, View.OnClickListener onClickListener) {
            mClickListenerSparseArray.put(viewId, onClickListener);
            return (B) this;
        }
    }
}
