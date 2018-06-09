package com.qgg.practice.recyclerviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.qgg.practice.R;
import com.qgg.practice.view.recyclerview.CommonViewHolder;
import com.qgg.practice.view.recyclerview.LinearItemDecoration;
import com.qgg.practice.view.recyclerview.RecyclerViewCommonAdapter;
import com.qgg.practice.view.recyclerview.WrapRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/10 10:32
 * @Describe :
 */
public class WrapAdapterActivity extends AppCompatActivity {

    private WrapRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adapter);
        initData();

        RecyclerView.Adapter adapter = new RecyclerViewCommonAdapter<String>(this, R.layout.item_home, mData) {
            @Override
            protected void convert(CommonViewHolder holder, String dataBean, int position) {
                //绑定数据
                holder.setText(R.id.id_num, dataBean);
            }
        };

        mAdapter = new WrapRecyclerAdapter(adapter);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new LinearItemDecoration(this, R.drawable.item_decoration));
        mRecyclerView.setAdapter(mAdapter);
        final View headView = LayoutInflater.from(this).inflate(R.layout.item_head, mRecyclerView, false);
        final View footView = LayoutInflater.from(this).inflate(R.layout.item_foot, mRecyclerView, false);
        mAdapter.addHeadView(headView);
        mAdapter.addHeadView(LayoutInflater.from(this).inflate(R.layout.item_head, mRecyclerView, false));
        mAdapter.addHeadView(LayoutInflater.from(this).inflate(R.layout.item_head, mRecyclerView, false));

        mAdapter.addFootView(footView);
        mAdapter.addFootView(LayoutInflater.from(this).inflate(R.layout.item_foot, mRecyclerView, false));

        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.removeHeadView(headView);
            }
        });
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.removeFootView(footView);
            }
        });
    }

    private void initAdapter() {

//        mAdapter.setItemClickListener(new RecyclerViewCommonAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(RecyclerView.Adapter<CommonViewHolder> parent, View view, int position) {
//                Toast.makeText(WrapAdapterActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mAdapter.setItemLongClickListener(new RecyclerViewCommonAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(RecyclerView.Adapter<CommonViewHolder> parent, View view, int position) {
//                Toast.makeText(WrapAdapterActivity.this, "长按" + position, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
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
                mAdapter.adjustSpanSize(mRecyclerView);
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
