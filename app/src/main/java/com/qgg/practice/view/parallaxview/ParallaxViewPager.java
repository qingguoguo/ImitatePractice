package com.qgg.practice.view.parallaxview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.qgg.practice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/15
 * @describe :
 */

public class ParallaxViewPager extends ViewPager {

    private List<Fragment> mFragmentList;

    public ParallaxViewPager(Context context) {
        this(context, null);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFragmentList = new ArrayList<>();
    }

    /**
     * 设置布局
     *
     * @param layoutIds
     * @param fragmentManager
     */
    public void setLayout(int[] layoutIds, FragmentManager fragmentManager) {
        mFragmentList.clear();
        for (int i = 0; i < layoutIds.length; i++) {
            mFragmentList.add(ParallaxFragment.newInstance(layoutIds[i]));
        }
        setAdapter(new ParallaxViewPagerAdapter(fragmentManager));
        SimpleOnPageChangeListener simpleOnPageChangeListener = new SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ParallaxFragment outFragment = (ParallaxFragment) mFragmentList.get(position);
                List<View> outViewList = outFragment.getViewList();
                for (View view : outViewList) {
                    ParallaxTag tag = (ParallaxTag) view.getTag(R.id.parallax_tag);
                    view.setTranslationX(-tag.translationXIn * positionOffsetPixels);
                    view.setTranslationY(-tag.translationYIn * positionOffsetPixels);
                }

                try {
                    ParallaxFragment inFragment = (ParallaxFragment) mFragmentList.get(position + 1);
                    List<View> inViewList = inFragment.getViewList();
                    for (View view : inViewList) {
                        ParallaxTag tag = (ParallaxTag) view.getTag();
                        view.setTranslationX((getWidth() - tag.translationXOut) * positionOffsetPixels);
                        view.setTranslationY((getWidth() - tag.translationYOut) * positionOffsetPixels);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        addOnPageChangeListener(simpleOnPageChangeListener);
    }

    /**
     * 适配器
     */
    class ParallaxViewPagerAdapter extends FragmentPagerAdapter {

        public ParallaxViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
