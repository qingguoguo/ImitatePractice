package com.qgg.practice.recyclerviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.qgg.practice.R;
import com.qgg.practice.view.recyclerview.CommonViewHolder;
import com.qgg.practice.view.recyclerview.LinearItemDecoration;
import com.qgg.practice.view.recyclerview.RecyclerViewCommonAdapter;
import com.qgg.practice.view.recyclerview.WrapRecyclerView;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemClickListener;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/8 19:09
 * @Describe :
 */
public class WrapRecyclerViewActivity extends AppCompatActivity {

    private RecyclerViewCommonAdapter mAdapter;
    private WrapRecyclerView mRecyclerView;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap_recycler);

        initData();
        mRecyclerView = (WrapRecyclerView) findViewById(R.id.recycle_view);
        mAdapter = new RecyclerViewCommonAdapter<String>(this, R.layout.item_home, mData) {
            @Override
            protected void convert(CommonViewHolder holder, String dataBean, int position) {
                //绑定数据
                holder.setText(R.id.id_num, dataBean);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new LinearItemDecoration(this, R.drawable.item_decoration));
        mRecyclerView.setAdapter(mAdapter);

        final View headView = LayoutInflater.from(this).inflate(R.layout.item_head, mRecyclerView, false);
        final View footView = LayoutInflater.from(this).inflate(R.layout.item_foot, mRecyclerView, false);
        mRecyclerView.addHeadView(headView);
        mRecyclerView.addHeadView(LayoutInflater.from(this).inflate(R.layout.item_head, mRecyclerView, false));
        mRecyclerView.addHeadView(LayoutInflater.from(this).inflate(R.layout.item_head, mRecyclerView, false));

        mRecyclerView.addFootView(footView);
        mRecyclerView.addFootView(LayoutInflater.from(this).inflate(R.layout.item_foot, mRecyclerView, false));

        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeHeadView(headView);
            }
        });
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeFootView(footView);
            }
        });

        mAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(WrapRecyclerViewActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
                mData.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter.setItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(WrapRecyclerViewActivity.this, "长按" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    protected void initData() {
        mData = new ArrayList<>();
        for (int i = 'Z'; i >= 'A'; i--) {
            mData.add("" + (char) i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_action_gridview:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
                break;
            case R.id.id_action_listview:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            default:
                break;
        }
        return true;
    }
}
