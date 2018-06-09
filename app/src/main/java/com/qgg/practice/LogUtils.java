package com.qgg.practice;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/23 on 22:11
 * 描述:
 */
public class LogUtils {

    private static final String TAG = "LogUtils";
    private static Boolean isDebug = null;

    /**
     * cannot be instantiated
     */
    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 是否需要打印日志，可以在application的onCreate函数里面初始化
     */
    public static void syncIsDebug(Context context) {
        if (isDebug == null) {
            isDebug = context.getApplicationInfo() != null && (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }

    public static boolean isDebug() {
        return isDebug != null && isDebug.booleanValue();
        //return isDebug == null ? false : isDebug.booleanValue();
    }

    /**
     * 下面四个是默认tag的函数
     */
    public static void i(String msg) {
        if (isDebug()) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug()) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug()) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (isDebug()) {
            Log.v(TAG, msg);
        }
    }

    /**
     * 下面是传入自定义tag的函数
     */
    public static void i(String tag, String msg) {
        if (isDebug()) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug()) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug()) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug()) {
            Log.v(tag, msg);
        }
    }
}
