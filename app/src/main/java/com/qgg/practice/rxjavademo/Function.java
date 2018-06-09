package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/5
 * @describe :
 */

public interface Function<T, U> {

    U apply(T t);
}
