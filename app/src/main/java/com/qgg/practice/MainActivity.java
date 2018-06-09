package com.qgg.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qgg.practice.retrofitdemo.RetrofitTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn_main);

    }

    public void practice(View view) {
        //getContext().startActivity(new Intent(getContext(), ViewPagerActivity.class));
        //getContext().startActivity(new Intent(getContext(), TestActivity.class));
        //getContext().startActivity(new Intent(getContext(), ViewDragHelpActivity.class));
        //getContext().startActivity(new Intent(getContext(), LockViewActivity.class));
        //getContext().startActivity(new Intent(getContext(), RecyclerViewTestActivity.class));
        //getContext().startActivity(new Intent(getContext(), ShapeViewTestActivity.class));
        //getContext().startActivity(new Intent(getContext(), TabMenuViewActivity.class));
        //getContext().startActivity(new Intent(getContext(), LoadingCircleActivity.class));
        //getContext().startActivity(new Intent(getContext(), ParallaxActivity.class));
        //getContext().startActivity(new Intent(getContext(), RotateLoadingViewActivity.class));
        //getContext().startActivity(new Intent(getContext(), AopNetWorkActivity.class));
        //getContext().startActivity(new Intent(getContext(), NavigationBarTestActivity.class));
        //getContext().startActivity(new Intent(getContext(), TestRjDemo1Activity.class));
        //getContext().startActivity(new Intent(getContext(), TestRxLoginActivity.class));
        startActivity(new Intent(this, RetrofitTestActivity.class));
    }

    public void practiceDraw7(View view) {
        startActivity(new Intent(this, PracticeDraw7Activity.class));
    }
}
