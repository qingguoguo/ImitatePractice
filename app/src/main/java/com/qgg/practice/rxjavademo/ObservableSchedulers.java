package com.qgg.practice.rxjavademo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/6
 * @describe :
 */

class ObservableSchedulers<T> extends Observable<T> {
    private Observable<T> observable;
    private Schedulers schedulers;

    public ObservableSchedulers(Observable<T> observable, Schedulers schedulers) {
        this.observable = observable;
        this.schedulers = schedulers;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        schedulers.scheduleDirect(new SchedulerTask(observer));
    }

    private class SchedulerTask implements Runnable {
        private Observer<? super T> observer;

        public SchedulerTask(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void run() {
            observable.subscribe(this.observer);
        }
    }
}
