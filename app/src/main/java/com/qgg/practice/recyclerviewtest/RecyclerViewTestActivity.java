package com.qgg.practice.recyclerviewtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qgg.practice.R;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/8 19:09
 * @Describe :
 */
public class RecyclerViewTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_test);
    }

    /**
     * 基本使用
     *
     * @param view
     */
    public void baseUseClick(View view) {
        startActivity(new Intent(this, BaseUseActivity.class));
    }

    /**
     * 万能适配器使用
     *
     * @param view
     */
    public void commonAdapterClick(View view) {
        startActivity(new Intent(this, AdapterActivity.class));
    }

    /**
     * 多布局支持
     *
     * @param view
     */
    public void multiTypeSupportClick(View view) {
        startActivity(new Intent(this, MultiTypeSupportActivity.class));
    }

    /**
     * 添加头部底部
     *
     * @param view
     */
    public void addHeadFoot(View view) {
        startActivity(new Intent(this, WrapAdapterActivity.class));
    }

    /**
     * 自定义WrapRecyclerView
     *
     * @param view
     */
    public void WrapRecyclerView(View view) {
        startActivity(new Intent(this, WrapRecyclerViewActivity.class));
    }

    /**
     * 侧滑删除
     *
     * @param view
     */
    public void dragRemove(View view) {
        startActivity(new Intent(this, DragRemoveActivity.class));
    }

    /**
     * 下拉刷新
     *
     * @param view
     */
    public void refreshClick(View view) {
        startActivity(new Intent(this, RefreshActivity.class));
    }
}
