package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/5
 * @describe :
 */

public class LambdaObserver<T> implements Observer<T> {

    private Consumer<T> mTConsumer;

    public LambdaObserver(Consumer<T> onNext) {
        mTConsumer = onNext;
    }

    @Override
    public void onSubscribe() {

    }

    @Override
    public void onNext(T t) {
        mTConsumer.onNext(t);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
