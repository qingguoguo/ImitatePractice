package com.qgg.practice;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.qgg.practice.view.rotateloadingview.RotateLoadingView;

public class RotateLoadingViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_loading_view);
        final RotateLoadingView mRotateLoadingView = (RotateLoadingView) findViewById(R.id.rotate_loading_view);
        mRotateLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRotateLoadingView.setOnEndListener(new RotateLoadingView.OnEndListener() {
            @Override
            public void onEnd() {
                Toast.makeText(RotateLoadingViewActivity.this,"动画执行完毕",Toast.LENGTH_SHORT).show();

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRotateLoadingView.setOnStop();
            }
        }, 3000);
    }
}
