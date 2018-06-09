package com.qgg.practice.view.taglayout;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/3
 * @describe :
 */

public abstract class FlowCommonAdapter<T> extends FlowBaseAdapter {

    private List<T> mData;
    private Context mContext;
    private int mLayoutId;

    public FlowCommonAdapter(Context context, List<T> data, @LayoutRes int layoutId) {
        mData = data;
        mContext = context;
        mLayoutId = layoutId;
    }

    public abstract void convert(FlowHolder holder, T item, int position);

    @Override
    public View getView(int position, ViewGroup parent) {
        FlowHolder flowHolder = new FlowHolder(mContext, parent, mLayoutId);
        convert(flowHolder, mData.get(position), position);
        return flowHolder.getConvertView();
    }

    @Override
    public int getCounts() {
        return mData.size();
    }

    public class FlowHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        public FlowHolder(Context context, ViewGroup parent, int layoutId) {
            this.mViews = new SparseArray<View>();
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        }

        public FlowHolder setText(int viewId, CharSequence text) {
            TextView tv = getView(viewId);
            tv.setText(text);
            return this;
        }

        @SuppressWarnings({"hiding", "unchecked"})
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 设置点击事件
         *
         * @return
         */
        public FlowHolder setOnClickListener(int viewId, View.OnClickListener clickListener) {
            getView(viewId).setOnClickListener(clickListener);
            return this;
        }

        /**
         * 设置条目的点击事件
         *
         * @return
         */
        public FlowHolder setItemClick(View.OnClickListener clickListener) {
            mConvertView.setOnClickListener(clickListener);
            return this;
        }

        public View getConvertView() {
            return mConvertView;
        }
    }
}

