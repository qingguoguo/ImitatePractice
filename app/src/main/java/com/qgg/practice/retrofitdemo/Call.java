package com.qgg.practice.retrofitdemo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/8
 * @describe :
 */

public interface Call<T> {

    void enqueue(Callback<T> callback);
}
