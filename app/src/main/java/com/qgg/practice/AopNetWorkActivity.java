package com.qgg.practice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class AopNetWorkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aop_net_work);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {

                    }
                };
                Looper.loop();
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

//    @CheckNet
//    public void tvClick(View view) {
//        Toast.makeText(this, "打哈哈点击我了", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this,AopNetWorkActivity.class));
//    }
}
