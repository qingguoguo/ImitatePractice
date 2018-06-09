package com.qgg.practice.http;

import android.content.Context;

import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/2
 * @describe : 定义网络请求的规范，相关方法
 */

public interface IHttpEngine {

    /**
     * get请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    void get(boolean cache, Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    void post(boolean cache, Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    /**
     * 取消网络请求
     */
    void cancel();

    //下载文件
    //上传文件
    //https添加证书
}
