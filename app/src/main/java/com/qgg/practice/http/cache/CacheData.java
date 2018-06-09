package com.qgg.practice.http.cache;

import java.io.Serializable;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/4
 * @describe :CaCheData数据类
 * <p>
 * <p>
 * 缓存在数据库是一个键值对
 * key唯一只能是url拼接参数，value是返回的json
 * 有两个地方可以处理缓存
 * 1.在回调的httpCallBack,业务逻辑掺杂在一起
 * 不符合单一职责的原则 httpCallBack只用来回调
 * 数据缓存逻辑是根据项目来的 不同的项目缓存逻辑不同
 * 2.在网络引擎里面做缓存
 */

public class CacheData implements Serializable {
    private String mUrlKey;
    private String mJsonResult;

    public CacheData() {
    }

    public CacheData(String urlToMD5, String jsonResult) {
        this.mUrlKey = urlToMD5;
        mJsonResult = jsonResult;
    }

    public String getJsonResult() {
        return mJsonResult;
    }
}
