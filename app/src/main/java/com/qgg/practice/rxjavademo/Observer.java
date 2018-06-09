package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/4
 * @describe :
 */

public interface  Observer<T> {
    void onSubscribe();

    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}
