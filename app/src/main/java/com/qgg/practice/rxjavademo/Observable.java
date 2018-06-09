package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/4
 * @describe :被观察者
 */

public abstract class Observable<T> implements ObservableSource<T> {

    public static <T> Observable<T> just(T item) {
        return onAssembly(new ObservableJust<T>(item));
    }

    @Override
    public void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }

    public abstract void subscribeActual(Observer<? super T> observer);

    private static <T> Observable<T> onAssembly(Observable<T> source) {
        return source;
    }

    public void subscribe(Consumer<T> onNext) {
        subscribe(onNext, null, null);
    }

    public void subscribe(Consumer<T> onNext, Object o, Object o1) {
        subscribe(new LambdaObserver<T>(onNext));
    }

    public <U> Observable<U> map(Function<T,U> function) {
        return onAssembly(new ObservableMap<T,U>(this,function));
    }

    public Observable<T> subscribeOn(Schedulers schedulers) {
        return new ObservableSchedulers<T>(this,schedulers);
    }

    public Observable<T> observeOn(Schedulers schedulers) {
        return new ObservableObserveOn<T>(this,schedulers);
    }
}
