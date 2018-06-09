package com.qgg.practice.recyclerviewtest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.qgg.practice.R;
import com.qgg.practice.view.recyclerview.CommonViewHolder;
import com.qgg.practice.view.recyclerview.RecyclerGridSpaceDecoration;
import com.qgg.practice.view.recyclerview.RecyclerViewCommonAdapter;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemClickListener;
import com.qgg.practice.view.recyclerview.refresh.DefaultLoadCreator;
import com.qgg.practice.view.recyclerview.refresh.DefaultRefreshCreator;
import com.qgg.practice.view.recyclerview.refresh.LoadRefreshRecyclerView;
import com.qgg.practice.view.recyclerview.refresh.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RefreshActivity extends AppCompatActivity implements RefreshRecyclerView.OnRefreshListener, LoadRefreshRecyclerView.OnLoadMoreListener {

    private LoadRefreshRecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        mDatas = new ArrayList<>();
        initData();

        mRecyclerView = (LoadRefreshRecyclerView) findViewById(R.id.recycler_view);
        // 添加头部和底部刷新效果
        mRecyclerView.addRefreshViewCreator(new DefaultRefreshCreator());
        mRecyclerView.addLoadViewCreator(new DefaultLoadCreator());

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);

        // 设置正在获取数据页面和无数据页面
        mRecyclerView.addLoadingView(findViewById(R.id.load_view));
        mRecyclerView.addEmptyView(findViewById(R.id.empty_view));

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new RecyclerGridSpaceDecoration(20));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new HomeAdapter(RefreshActivity.this, mDatas);
                mRecyclerView.setAdapter(mAdapter);
            }
        }, 2000);

//        mAdapter = new HomeAdapter(RefreshActivity.this, mDatas);
//        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
        //mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
    }

    protected void initData() {
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.onStopRefresh();
            }
        }, 5000);
    }

    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                mRecyclerView.onStopLoad();
                mAdapter.notifyDataSetChanged();
            }
        }, 4000);
    }

    class HomeAdapter extends RecyclerViewCommonAdapter<String> {
        public HomeAdapter(Context context, List<String> data) {
            super(context, R.layout.item_home, data);
        }

        @Override
        protected void convert(CommonViewHolder holder, String dataBean, int position) {
            holder.setText(R.id.id_num, dataBean);
        }
    }
}
