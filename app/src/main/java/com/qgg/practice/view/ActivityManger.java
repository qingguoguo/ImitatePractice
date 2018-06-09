package com.qgg.practice.view;

import android.app.Activity;

import java.util.Stack;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/23
 * @describe : Activity管理类
 */

public class ActivityManger {

    private static volatile ActivityManger mInstance;
    private Stack<Activity> mActivityStack;

    private ActivityManger() {
        mActivityStack = new Stack<>();
    }

    public ActivityManger getInstance() {
        if (mInstance == null) {
            synchronized (ActivityManger.class) {
                if (mInstance == null) {
                    mInstance = new ActivityManger();
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加到管理列表
     *
     * @param activity
     */
    public void attach(Activity activity) {
        if (activity == null) {
            return;
        }
        mActivityStack.add(activity);
    }

    /**
     * 从管理列表移除
     *
     * @param activity
     */
    public void detach(Activity activity) {
        if (activity == null) {
            return;
        }
        for (int i = 0; i < mActivityStack.size(); i++) {
            if (mActivityStack.get(i).equals(activity)) {
                mActivityStack.remove(i);
            }
        }
    }

    /**
     * 根据类名关闭当前的Activity
     *
     * @param clazz
     */
    public void finish(Class<Activity> clazz) {
        if (clazz == null) {
            return;
        }
        for (int i = 0; i < mActivityStack.size(); i++) {
            Activity activity = mActivityStack.get(i);
            if (activity.getClass().getCanonicalName().equals(clazz.getCanonicalName())) {
                activity.finish();
            }
        }
    }

    /**
     * 关闭当前的Activity
     *
     * @param activity
     */
    public void finish(Activity activity) {
        if (activity == null) {
            return;
        }
        for (int i = 0; i < mActivityStack.size(); i++) {
            if (mActivityStack.get(i).equals(activity)) {
                activity.finish();
            }
        }
    }

    /**
     * 退出应用
     */
    public void exitApplication() {
        mActivityStack.clear();
    }

    /**
     * 获取顶部Activity
     *
     * @return
     */
    public Activity getTopActivity() {
        return mActivityStack.lastElement();
    }
}
