package com.qgg.practice.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/2
 * @describe :封装网络请求工具类 底层封装，baselibarary不写业务逻辑代码
 */

public class HttpUtils {

    private final static int POST_TYPE = 0x0010;
    private final static int GET_TYPE = 0x0022;

    private String mUrl;
    private int mType;
    private Context mContext;
    private boolean mCache;
    private Map<String, Object> mParams;

    /**
     * 默认网络请求框架用 OkHttp
     */
    private static IHttpEngine mIHttpEngine = null;

    /**
     * 构造方法 private，推荐使用with()方法
     *
     * @param context
     */
    private HttpUtils(Context context) {
        mContext = context;
        mParams = new HashMap<>();
    }

    /**
     * 可在application中初始化网络请求框架
     *
     * @param httpEngine
     */
    public static void initEngine(IHttpEngine httpEngine) {
        mIHttpEngine = httpEngine;
    }

    /**
     * 可在某个网络请求中更改网络请求框架
     *
     * @param httpEngine
     */
    public HttpUtils exchangeEngine(IHttpEngine httpEngine) {
        HttpUtils.mIHttpEngine = httpEngine;
        return this;
    }

    /**
     * 创建HttpUtils
     *
     * @param context
     * @return
     */
    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils post() {
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get() {
        mType = GET_TYPE;
        return this;
    }

    public HttpUtils url(String url) {
        mUrl = url;
        return this;
    }

    /**
     * 添加多个参数
     *
     * @param params
     * @return
     */
    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    /**
     * 添加一个参数
     *
     * @param key
     * @param value
     * @return
     */
    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils cache(boolean isCache) {
        mCache = isCache;
        return this;
    }

    /**
     * 执行请求，传入 EngineCallBack
     *
     * @param callBack
     */
    public void execute(EngineCallBack callBack) {
        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALLBACK;
        }
        callBack.onPreExecute(mContext, mParams);

        //执行网络请求方法判断
        if (mType == POST_TYPE) {
            post(mUrl, mParams, callBack);
        }

        if (mType == GET_TYPE) {
            get(mUrl, mParams, callBack);
        }
    }

    /**
     * 传默认的 EngineCallBack
     */
    public void execute() {
        execute(null);
    }

    /**
     * get()方法 实际调用的是网络框架的请求方法
     *
     * @param url
     * @param params
     * @param callBack
     */
    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mIHttpEngine.get(mCache, mContext, url, params, callBack);
    }

    /**
     * post()方法 实际调用的是网络框架的请求方法
     *
     * @param url
     * @param params
     * @param callBack
     */
    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mIHttpEngine.post(mCache, mContext, url, params, callBack);
    }

    /**
     * 取消网络请求
     */
    public void cancel() {
        mIHttpEngine.cancel();
    }

    /**
     * 拼接参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    /**
     * 解析一个类上面的泛型类信息
     */
    public static Class<?> analysisClazzInfo(Object object) {
        //getGenericSuperclass()获得带有泛型的父类
        //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型
        //ParameterizedType参数化类型，即泛型
        //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}
