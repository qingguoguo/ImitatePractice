package com.qgg.practice.retrofitdemo;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/8
 * @describe :
 */

class RequestBuilder {

    private ParameterHandler[] mParameterHandlers;
    private Object[] mArgs;
    private HttpUrl.Builder mHttpUrlBuilder;

    public RequestBuilder(String url, String relativeUrl, ParameterHandler[] parameterHandlers, Object[] args) {
        mParameterHandlers = parameterHandlers;
        mArgs = args;
        mHttpUrlBuilder = HttpUrl.parse(url + relativeUrl).newBuilder();
    }

    public Request build() {
        for (int i = 0; i < mParameterHandlers.length; i++) {
            mParameterHandlers[i].apply(this, mArgs[i]);
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(mHttpUrlBuilder.build());

        return requestBuilder.build();
    }

    public void addQueryName(String key, String value) {
        mHttpUrlBuilder.addQueryParameter(key, value);
    }
}
