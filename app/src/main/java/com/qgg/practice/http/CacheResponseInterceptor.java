package com.qgg.practice.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/30
 * @describe :
 */

public class CacheResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 设置过期时间30S
        Response response = chain.proceed(chain.request());

        response = response.newBuilder()
                .removeHeader("Cache-Control")
                .removeHeader("Pragma")
                .addHeader("Cache-Control", "max-age=30")
                .build();

        return response;
    }
}
