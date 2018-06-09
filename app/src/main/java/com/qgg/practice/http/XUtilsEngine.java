package com.qgg.practice.http;

import android.content.Context;


import com.qgg.practice.LogUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.HttpUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/3
 * @describe :
 */

public class XUtilsEngine implements IHttpEngine {
    private static final String TAG = "XUtilsEngine";
    private HttpHandler mHttpHandler;

    @Override
    public void get(final boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        //发送请求 设置超时
        HttpUtils httpUtils = new HttpUtils(15000);
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.configDefaultHttpCacheExpiry(0);
        httpUtils.configRequestRetryCount(0);
        RequestParams requestParams = getParams("GET", params);
        LogUtils.i(TAG, "Get请求路径：" + url);
        //HttpRequest.HttpMethod method, String url, RequestParams params, RequestCallBack<T> callBack
        mHttpHandler = httpUtils.send(HttpRequest.HttpMethod.GET, url, requestParams, new RequestCallBack<String>() {
            @Override
            public Object getUserTag() {
                return super.getUserTag();
            }

            @Override
            public void setUserTag(Object userTag) {
                super.setUserTag(userTag);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCancelled() {
                super.onCancelled();
                callBack.onCancel();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String resultJson = responseInfo.result;
                LogUtils.i(TAG, "Get返回结果：" + resultJson);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    @Override
    public void post(final boolean cache, Context context, String url, Map<String, Object> params, EngineCallBack callBack) {

    }

    @Override
    public void cancel() {
        mHttpHandler.cancel();
    }

    private RequestParams getParams(String httpMethod, Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<>(12);
        }
        List<String> list = new ArrayList<>();
        RequestParams params = new RequestParams();

        // 请求主体
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            list.add(entry.getKey() + "=" + entry.getValue());
            if ("GET".equals(httpMethod)) {
                params.addQueryStringParameter(entry.getKey(), String.valueOf(entry.getValue()));
            } else {
                params.addBodyParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return params;
    }
}
