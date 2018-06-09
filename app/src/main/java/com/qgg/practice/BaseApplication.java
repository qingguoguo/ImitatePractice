package com.qgg.practice;

import android.app.Application;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/28
 * @describe :
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.syncIsDebug(this);
    }
}
