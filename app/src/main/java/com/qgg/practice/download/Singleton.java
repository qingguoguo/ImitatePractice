package com.qgg.practice.download;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/30
 * @describe :
 */

public abstract class Singleton<T> {

    private T mInstance;

    protected abstract T create();

    public final T get() {
        if (mInstance == null) {
            synchronized (Singleton.class) {
                if (mInstance == null) {
                    mInstance = create();
                }
            }
        }
        return mInstance;
    }
}
