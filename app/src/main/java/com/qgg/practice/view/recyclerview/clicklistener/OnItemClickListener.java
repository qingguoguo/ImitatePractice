package com.qgg.practice.view.recyclerview.clicklistener;

import android.view.View;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/10
 * @describe :
 */

public interface OnItemClickListener {
    /**
     * Item点击事件
     *
     * @param view
     * @param position
     */
    void onItemClick(View view, int position);
}
