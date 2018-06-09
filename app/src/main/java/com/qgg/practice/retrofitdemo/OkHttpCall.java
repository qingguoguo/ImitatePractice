package com.qgg.practice.retrofitdemo;

import android.util.Log;

import java.io.IOException;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/8
 * @describe :
 */

public class OkHttpCall<T> implements Call<T> {

    private ServiceMethod mServiceMethod;
    private Object[] mArgs;

    public OkHttpCall(ServiceMethod serviceMethod, Object[] args) {
        mServiceMethod = serviceMethod;
        mArgs = args;
    }

    @Override
    public void enqueue(final Callback<T> callback) {
        // 发起网络请求
        //Log.e("TAG", "发起网络请求....");

        okhttp3.Call call = mServiceMethod.createNewCall(mArgs);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(OkHttpCall.this, e);
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.e("TAG", "发起网络请求....onResponse : " + response);
                if (callback != null) {
                    Response<T> tResponse = new Response<>();
                    tResponse.body = mServiceMethod.parseBody(response.body());
                    callback.onResponse(OkHttpCall.this, tResponse);
                }
            }
        });

    }
}
