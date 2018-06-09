package com.qgg.practice.view.tabmenuview;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/14 19:13
 * @Describe :筛选菜单的 Adapter
 */
public abstract class BaseMenuAdapter {

    protected final DataSetObservable mDataSetObservable = new DataSetObservable();

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * 总数
     *
     * @return
     */
    public abstract int getCount();

    /**
     * 获取当前的TabView
     *
     * @param position
     * @param parent
     * @return
     */
    public abstract View getTabView(int position, ViewGroup parent);

    /**
     * 获取当前的菜单内容
     *
     * @param position
     * @param parent
     * @return
     */
    public abstract View getMenuView(int position, ViewGroup parent);

    /**
     * 菜单打开
     *
     * @param tabView
     */
    public void menuOpen(View tabView) {

    }

    /**
     * 菜单关闭
     *
     * @param tabView
     */
    public void menuClose(View tabView) {

    }
}
