package com.qgg.practice.rxjavatest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.qgg.practice.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class TestRjDemo3Activity extends AppCompatActivity {

    private static final String TAG = "TestRjActivity";
    private static final String ENDPOINT = "";
    private ImageView mImage;
    String url = "http://img.taopic.com/uploads/allimg/130331/240460-13033106243430.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rxjava);
        mImage = (ImageView) findViewById(R.id.image);

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);

                Log.d(TAG, "emit 4");
                emitter.onNext(4);
                emitter.onComplete();
                Log.d(TAG, "emit 5");
                emitter.onNext(5);
                Log.d(TAG, "emit 6");
                emitter.onNext(6);

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "" + integer);
            }
        });
//                .subscribe(new Observer<Integer>() {
//            private Disposable mDisposable;
//
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "subscribe");
//                mDisposable = d;
//            }
//
//            @Override
//            public void onNext(Integer value) {
//                Log.d(TAG, "" + value);
//                if (3 == value) {
//                    Log.d(TAG, "dispose");
//                    mDisposable.dispose();
//                    Log.d(TAG, "isDisposed :" + mDisposable.isDisposed());
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "error");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "complete");
//            }
//        });
    }
}
