package com.qgg.practice.view.recyclerview.clicklistener;

import android.view.View;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/10
 * @describe :
 */

public interface OnItemLongClickListener {

    /**
     * Item长按事件
     *
     * @param view
     * @param position
     * @return
     */
    boolean onItemLongClick(View view, int position);
}
