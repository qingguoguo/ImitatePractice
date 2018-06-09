package com.qgg.practice.view.taglayout;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/2
 * @describe :
 */

public abstract class FlowBaseAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    private final ArrayList<View> views = new ArrayList<>();

    public abstract View getView(int position, ViewGroup parent);

    public abstract int getCounts();


    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public void addViewAll(ViewGroup parent) {
        views.clear();
        for (int i = 0; i < getCounts(); i++) {
            views.add(getView(i, parent));
        }
    }

    public List<View> getData() {
        return views;
    }
}
