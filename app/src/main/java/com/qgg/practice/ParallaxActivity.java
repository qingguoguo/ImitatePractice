package com.qgg.practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qgg.practice.view.parallaxview.ParallaxViewPager;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/15 12:48
 * @Describe :
 */
public class ParallaxActivity extends AppCompatActivity {

    private ParallaxViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax);

        mViewPager = (ParallaxViewPager) findViewById(R.id.parallax_vp);
        mViewPager.setLayout(new int[]{R.layout.fragment_page_first,
                R.layout.fragment_page_second,R.layout.fragment_page_third},getSupportFragmentManager());
    }
}
