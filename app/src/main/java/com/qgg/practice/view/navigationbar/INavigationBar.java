package com.qgg.practice.view.navigationbar;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/23
 * @describe :
 */

public interface INavigationBar {

    void createNavigationBar();

    void attachNavigationBarParams();

    void attachParent(View navigationBar, ViewGroup parent);
}
