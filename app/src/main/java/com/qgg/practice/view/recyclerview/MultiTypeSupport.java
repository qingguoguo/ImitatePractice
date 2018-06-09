package com.qgg.practice.view.recyclerview;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/10
 * @describe :
 */

public interface MultiTypeSupport<T> {
    /**
     * item多类型支持
     *
     * @param t
     * @return
     */
    int getLayoutId(T t);
}
