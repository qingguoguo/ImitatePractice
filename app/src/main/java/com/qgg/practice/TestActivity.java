package com.qgg.practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qgg.practice.view.TouchView;
import com.qgg.practice.view.taglayout.FlowCommonAdapter;
import com.qgg.practice.view.taglayout.TagLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/2 11:34
 * @Describe :
 */
public class TestActivity extends AppCompatActivity {
    TextView tv;
    TagLayout mTagLayout;
    TouchView mTouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testctivity);
        testMethod();
        testMethodTwo();
    }

    private void testMethodTwo() {

    }

    private void testMethod() {
        //        tv = (TextView) findViewById(R.id.tv);
//        LettersBar lettersBar = (LettersBar) findViewById(R.id.letters_bar);
//        lettersBar.setSelectOnTouchListen(new LettersBar.SelectOnTouchListen() {
//            @Override
//            public void onTouch(String letter, boolean isUp) {
//                if (!isUp) {
//                    tv.setText(letter);
//                    tv.setVisibility(View.VISIBLE);
//                } else {
//                    tv.setText("");
//                    tv.setVisibility(View.GONE);
//                }
//            }
//        });

        mTagLayout = (TagLayout) findViewById(R.id.tag_layout);
        final List<String> list = new ArrayList<>();
        list.add("111");
        list.add("11dfsdfsdfsdf1");
        list.add("fdgs");
        list.add("11d1");
        list.add("1fdg11");
        list.add("fdddddd");
        list.add("111");
        list.add("freerwar");
        list.add("1411");
        list.add("1fgfg11");
        list.add("gdsaf d sf ssdf ");
        list.add("111");
        list.add("sfa dsa s sda sds ");
        list.add("fgsdfdf");
        list.add("dsf ds  sda dssf ds");
        list.add("11sfdsfdsfsdf1");
        list.add("111");

//        mTagLayout.setAdapter(new FlowBaseAdapter() {
//            @Override
//            public View getView(int position, ViewGroup parent) {
//                TextView textView = (TextView) LayoutInflater.from(getBaseContext()).
//                        inflate(R.layout_loading_view.tag_item, parent, false);
//                textView.setText(list.get(position));
//                return textView;
//            }
//
//            @Override
//            public int getCounts() {
//                return list.size();
//            }
//        });

        mTagLayout.setAdapter(new FlowCommonAdapter<String>(
                this, list, R.layout.tag_item) {
            @Override
            public void convert(FlowHolder holder, final String item, final int position) {
                holder.setText(R.id.tag_item_tv, list.get(position));

                holder.setItemClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TestActivity.this, item, Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        mTouchView = (TouchView) findViewById(R.id.touch_view);
        mTouchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("TouchView", "View OnTouchListener：" + event.getAction());
                return false;
            }
        });

        mTouchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TouchView", "View OnClickListener");
            }
        });
    }
}