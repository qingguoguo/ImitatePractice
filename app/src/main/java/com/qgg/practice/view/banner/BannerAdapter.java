package com.qgg.practice.view.banner;

import android.view.View;

public abstract class BannerAdapter {
    /**
     * 根据position获取View添加到viewpager
     */
    public abstract View getView(int position, View convertView);

    /**
     * 获取轮播子view的数量
     */
    public abstract int getCount();

    /**
     * 根据position获取广告位描述
     */
    public String getBannerDesc(int position) {
        return "";
    }
}
