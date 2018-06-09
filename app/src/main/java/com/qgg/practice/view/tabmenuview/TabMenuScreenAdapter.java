package com.qgg.practice.view.tabmenuview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qgg.practice.R;

import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/14 19:21
 * @Describe :
 */
public class TabMenuScreenAdapter<T> extends BaseMenuAdapter {

    private Context mContext;
    private List<T> mDataList;

    public TabMenuScreenAdapter(Context context, List<T> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView tabView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_screen_tab, parent, false);
        tabView.setText((String) mDataList.get(position));
        tabView.setTextColor(Color.BLACK);
        return tabView;
    }

    @Override
    public View getMenuView(int position, ViewGroup parent) {
        // 实际开发过程中，不同的位置显示的布局不一样
        final TextView menuView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_screen_menu, parent, false);
        menuView.setText((String) mDataList.get(position));
        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataSetObservable.notifyChanged();
            }
        });
        return menuView;
    }

    @Override
    public void menuClose(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.BLACK);
    }

    @Override
    public void menuOpen(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.RED);
    }
}
