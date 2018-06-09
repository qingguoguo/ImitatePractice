package com.qgg.practice.http;


import android.content.Context;

import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/2
 * @describe :网络请求回调
 */

public interface EngineCallBack {

    /**
     * 开始执行，执行前回调方法
     *
     * @param context
     * @param params
     */
    void onPreExecute(Context context, Map<String, Object> params);

    /**
     * 网络请求成功的回调方法
     *
     * @param result
     * @return
     */
    void onSuccess(String result);

    /**
     * 网络请求失败的回调方法
     *
     * @param e
     */
    void onError(Exception e);

    /**
     * 网络请求取消的回调方法
     */
    void onCancel();

    EngineCallBack DEFAULT_CALLBACK = new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onSuccess(String result) {
        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onCancel() {

        }
    };
}
