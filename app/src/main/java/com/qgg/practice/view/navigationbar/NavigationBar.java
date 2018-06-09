package com.qgg.practice.view.navigationbar;

import android.content.Context;
import android.view.ViewGroup;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/23
 * @describe :可直接使用的导航栏
 */

public class NavigationBar extends AbsNavigationBar {

    protected NavigationBar(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbsNavigationBar.Builder<Builder> {
        public Builder(Context context, int layoutRes, ViewGroup parent) {
            super(context, layoutRes, parent);
        }

        @Override
        public NavigationBar create() {
            return new NavigationBar(this);
        }
    }
}
