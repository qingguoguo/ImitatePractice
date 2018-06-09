package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/4
 * @describe :
 */

public interface ObservableSource<T> {


    void subscribe(Observer<? super T> observer);
}
