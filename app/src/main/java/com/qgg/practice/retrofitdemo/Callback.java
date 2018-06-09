package com.qgg.practice.retrofitdemo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/8
 * @describe :
 */

public interface Callback<T> {

    void onResponse(Call<T> call, Response<T> response);


    void onFailure(Call<T> call, Throwable t);
}
