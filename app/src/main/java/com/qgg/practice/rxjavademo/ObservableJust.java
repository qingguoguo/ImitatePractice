package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/4
 * @describe :
 */

public class ObservableJust<T> extends Observable<T> {

    private T item;

    public ObservableJust(T item) {
        this.item = item;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        //代理
        ScalarDisposable sd = new ScalarDisposable(observer, item);
        sd.run();
    }

    public class ScalarDisposable {
        private Observer<? super T> mObserver;
        private T mItem;

        public ScalarDisposable(Observer<? super T> observer, T item) {
            mObserver = observer;
            mItem = item;
        }

        public void run() {
            mObserver.onSubscribe();
            try {
                mObserver.onNext(mItem);
                mObserver.onComplete();
            } catch (Exception e) {
                mObserver.onError(e);
            }
        }
    }
}
