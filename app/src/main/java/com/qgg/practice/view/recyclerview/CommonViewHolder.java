package com.qgg.practice.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/9
 * @describe : CommonViewHolder
 */

public class CommonViewHolder extends RecyclerView.ViewHolder {

    /**
     * 缓存View
     */
    private SparseArray<View> mSparseArray;

    public CommonViewHolder(View itemView) {
        super(itemView);
        Log.e("CommonAdapter", "CommonViewHolder构造方法  View : " + itemView);
        mSparseArray = new SparseArray<>();
    }

    /**
     * 泛型方法
     * 增加缓存，减少findViewById次数
     *
     * @param ViewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int ViewId) {

        View view = mSparseArray.get(ViewId);
        if (view == null) {
            view = itemView.findViewById(ViewId);
            mSparseArray.put(ViewId, view);
        }
        return (T) view;
    }

    /**
     * 通用功能封装，设置文本，设置item点击事件，设置图片
     */
    public RecyclerView.ViewHolder setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * @param viewId
     * @param text   fromHtml
     * @return
     */
    public RecyclerView.ViewHolder setText(int viewId, Spanned text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * @param viewId
     * @param resourceId
     * @return
     */
    public RecyclerView.ViewHolder setImageSrc(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    /**
     * 解耦加载网络图片
     *
     * @param viewId
     * @param imageLoader
     * @return
     */
    public RecyclerView.ViewHolder setImagePath(int viewId, HolderImageLoader imageLoader) {
        View view = getView(viewId);
        imageLoader.loadImage(view.getContext(), view, imageLoader.getPath());
        return this;
    }

    /**
     * childView设置点击事件
     *
     * @param childViewId
     * @param onClickListener
     */
    public void setChildClickListener(int childViewId, View.OnClickListener onClickListener) {
        getView(childViewId).setOnClickListener(onClickListener);
    }

    /**
     * childView设置长按事件
     *
     * @param childViewId
     * @param longClickListener
     */
    public void setChildOnLongClickListener(int childViewId, View.OnLongClickListener longClickListener) {
        getView(childViewId).setOnLongClickListener(longClickListener);
    }
}
