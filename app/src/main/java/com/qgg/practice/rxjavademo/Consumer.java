package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/5
 * @describe :
 */

public interface Consumer<T> {
    void onNext(T item);
}
