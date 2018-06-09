package com.qgg.practice.view.recyclerview.refresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/11
 * @describe :上拉加载更多的辅助类
 */
public abstract class LoadViewCreator {

    /**
     * 获取上拉加载更多的View
     *
     * @param context 上下文
     * @param parent  RecyclerView
     */
    public abstract View getLoadView(Context context, ViewGroup parent);

    /**
     * 正在上拉
     *
     * @param currentDragHeight 当前拖动的高度
     * @param loadViewHeight    总的加载高度
     * @param currentLoadStatus 当前状态
     */
    public abstract void onPull(int currentDragHeight, int loadViewHeight, int currentLoadStatus);

    /**
     * 正在加载中
     */
    public abstract void onLoading();

    /**
     * 停止加载
     */
    public abstract void onStopLoad();
}