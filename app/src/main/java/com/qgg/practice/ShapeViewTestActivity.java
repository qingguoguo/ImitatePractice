package com.qgg.practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ShapeViewTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_view_test);
//        final ShapeView shapeView = (ShapeView) findViewById(R.id.shape_view);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            shapeView.exchange();
//                        }
//                    });
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }).start();
    }
}
