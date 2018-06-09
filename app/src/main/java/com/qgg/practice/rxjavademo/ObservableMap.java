package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/4
 * @describe :
 */

public class ObservableMap<T, U> extends Observable<U> {
    Observable<T> observable;
    Function<T, U> function;

    public ObservableMap(Observable<T> observable, Function<T, U> function) {
        this.observable = observable;
        this.function = function;
    }

    @Override
    public void subscribeActual(Observer<? super U> observer) {
        this.observable.subscribe(new MapObserver<T,U>(observer, function));
    }

    private class MapObserver<T,U> implements Observer<T> {

        Observer<? super U> observer;
        Function<T, U> function;

        public MapObserver(Observer<? super U> observer, Function<T, U> function) {
            this.observer = observer;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            observer.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            U apply = function.apply(t);
            observer.onNext(apply);
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
