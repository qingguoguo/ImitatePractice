package com.qgg.practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qgg.practice.view.tabmenuview.TabMenuScreenAdapter;
import com.qgg.practice.view.tabmenuview.TabMenuScreenView;

import java.util.Arrays;
import java.util.List;

public class TabMenuViewActivity extends AppCompatActivity {
    private TabMenuScreenView mTabMenuScreenView;
    private String[] mItems = {"类型", "品牌", "价格", "更多"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_menu_view);

        mTabMenuScreenView = (TabMenuScreenView) findViewById(R.id.list_data_screen_view);
        List<String> stringList = Arrays.asList(mItems);
        mTabMenuScreenView.setAdapter(new TabMenuScreenAdapter(this, stringList));
    }
}
