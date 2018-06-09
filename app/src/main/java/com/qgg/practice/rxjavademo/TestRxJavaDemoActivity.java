package com.qgg.practice.rxjavademo;


import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.qgg.practice.LogUtils;
import com.qgg.practice.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestRxJavaDemoActivity extends AppCompatActivity {

    private static final String TAG = "TestRxJavaDemoActivity";
    private String mUrl = "http://img.taopic.com/uploads/allimg/130331/240460-13033106243430.jpg";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rxjava);
        imageView = (ImageView) findViewById(R.id.image);

        Observable.just(mUrl)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) {
                        LogUtils.i(TAG, "apply1:" + Thread.currentThread().getName());
                        return getImageBitmap(s);
                    }
                })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) {
                        LogUtils.i(TAG, "apply2:" + Thread.currentThread().getName());
                        return createWatermark(bitmap, "rxJava");
                    }
                })
//                .map(new Function<Bitmap, Bitmap>() {
//                    @Override
//                    public Bitmap apply(Bitmap bitmap) {
//                        LogUtils.i(TAG, "apply3:" + Thread.currentThread().getName());
//                        return bitmap;
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void onNext(final Bitmap s) {
                        LogUtils.i(TAG, "onNext:" + s);
                        LogUtils.i(TAG, "onNext:" + Thread.currentThread().getName());
                        imageView.setImageBitmap(s);
                    }
                });

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new io.reactivex.functions.Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Toast.makeText(TestRxJavaDemoActivity.this,"有权限",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TestRxJavaDemoActivity.this,"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Bitmap getImageBitmap(String s) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(s);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            // 加水印
            //bitmap = createWatermark(bitmap, "RxJava2.0");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap createWatermark(Bitmap bitmap, String mark) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint p = new Paint();
        // 水印颜色
        p.setColor(Color.parseColor("#C5FF0000"));
        // 水印字体大小
        p.setTextSize(150);
        //抗锯齿
        p.setAntiAlias(true);
        //绘制图像
        canvas.drawBitmap(bitmap, 0, 0, p);
        //绘制文字
        canvas.drawText(mark, 0, h / 2, p);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bmp;
    }
}
