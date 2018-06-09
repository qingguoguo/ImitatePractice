package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/6
 * @describe :
 */

public class ObservableObserveOn<T> extends Observable<T> {

    private Observable<T> observable;
    private Schedulers schedulers;

    public ObservableObserveOn(Observable<T> observable, Schedulers schedulers) {
        this.observable = observable;
        this.schedulers = schedulers;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        observable.subscribe(new ObserverOnObserver<T>(observer, schedulers));
    }

    private class ObserverOnObserver<T> implements Observer<T>, Runnable {
        private Observer<? super T> observer;
        private Schedulers schedulers;
        private T t;

        public ObserverOnObserver(Observer<? super T> observer, Schedulers schedulers) {
            this.observer = observer;
            this.schedulers = schedulers;
        }

        @Override
        public void onSubscribe() {
            observer.onSubscribe();
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }

        @Override
        public void onNext(T t) {
            this.t = t;
            schedulers.scheduleDirect(this);
        }

        @Override
        public void run() {
            observer.onNext(t);
        }
    }
}
