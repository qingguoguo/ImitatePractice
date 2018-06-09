package com.qgg.practice.rxjavatest;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.qgg.practice.R;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TestRjDemo2Activity extends AppCompatActivity {

    private static final String TAG = "TestRjActivity";
    private ImageView mImage;
    private String url = "http://img.taopic.com/uploads/allimg/130331/240460-13033106243430.jpg";
    private Api api;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rxjava);
        mImage = (ImageView) findViewById(R.id.image);

        Retrofit retrofit = RetrofitProvider.get();
        api = retrofit.create(Api.class);

        api.getTop250(1, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        Log.d(TAG, "注册成功");
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                        return api.getTop250(1, 1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        Log.d(TAG, "登录成功");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "登录失败");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void register() {
        api.getTop250(1, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        Log.d(TAG, "注册成功");
                        login();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "注册失败");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void login() {
        api.getTop250(2, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        Log.d(TAG, "登录成功");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "登录失败");
                    }
                });
    }

//     Observable.create(new ObservableOnSubscribe<Integer>() {
//        @Override
//        public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//            Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
//            Log.d(TAG, "emit 1");
//            emitter.onNext(1);
//            Log.d(TAG, "emit 2");
//            emitter.onNext(2);
//            Log.d(TAG, "emit 3");
//            emitter.onNext(3);
//
//            Log.d(TAG, "emit 4");
//            emitter.onNext(4);
//            emitter.onComplete();
//            Log.d(TAG, "emit 5");
//            emitter.onNext(5);
//            Log.d(TAG, "emit 6");
//            emitter.onNext(6);
//
//        }
//    }).subscribeOn(Schedulers.newThread())
//            .subscribeOn(Schedulers.io())
//            .subscribeOn(AndroidSchedulers.mainThread())
////                .observeOn(AndroidSchedulers.mainThread())
////                .observeOn(Schedulers.io())
//            .subscribe(new Consumer<Integer>() {
//        @Override
//        public void accept(Integer integer) throws Exception {
//            Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
//            Log.d(TAG, "" + integer);
//        }
//    });
}
