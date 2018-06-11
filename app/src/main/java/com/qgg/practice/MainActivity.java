package com.qgg.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qgg.practice.retrofitdemo.RetrofitTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn_main);

    }

    public void practice(View view) {

//        startActivity(new Intent(this, ViewPagerActivity.class));
//        startActivity(new Intent(this, TestActivity.class));
//        startActivity(new Intent(this, ViewDragHelpActivity.class));
//        startActivity(new Intent(this, LockViewActivity.class));
//        startActivity(new Intent(this, RecyclerViewTestActivity.class));
//        startActivity(new Intent(this, ShapeViewTestActivity.class));
//        startActivity(new Intent(this, TabMenuViewActivity.class));
//        startActivity(new Intent(this, LoadingCircleActivity.class));
//        startActivity(new Intent(this, ParallaxActivity.class));
//        startActivity(new Intent(this, RotateLoadingViewActivity.class));
//        startActivity(new Intent(this, AopNetWorkActivity.class));
//        startActivity(new Intent(this, NavigationBarTestActivity.class));
//        startActivity(new Intent(this, TestRjDemo1Activity.class));
//        startActivity(new Intent(this, TestRxLoginActivity.class));

        startActivity(new Intent(this, RetrofitTestActivity.class));
    }

    public void practiceDraw7(View view) {
        //startActivity(new Intent(this, PracticeDraw7Activity.class));
        startActivity(new Intent(this, ParallaxActivity.class));
        Glide.with(this).load("").into(new ImageView(this));
    }
}
