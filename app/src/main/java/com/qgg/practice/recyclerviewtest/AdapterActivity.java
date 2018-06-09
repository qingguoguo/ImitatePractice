package com.qgg.practice.recyclerviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.qgg.practice.R;
import com.qgg.practice.view.recyclerview.CommonViewHolder;
import com.qgg.practice.view.recyclerview.GridItemDecoration;
import com.qgg.practice.view.recyclerview.RecyclerViewCommonAdapter;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemClickListener;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/10 10:32
 * @Describe :
 */
public class AdapterActivity extends AppCompatActivity {

    private RecyclerViewCommonAdapter<String> mAdapter;
    private RecyclerView mRecyclerView;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adapter);
        initData();
        initAdapter();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridItemDecoration(this, R.drawable.item_decoration));

        //mRecyclerView.addItemDecoration(new GridItemDecoration(this, R.drawable.item_decoration));

        mRecyclerView.setAdapter(mAdapter);
    }

    private void initAdapter() {
        mAdapter = new RecyclerViewCommonAdapter<String>(this, R.layout.item_home, mData) {

            @Override
            protected void convert(CommonViewHolder holder, String dataBean, int position) {
                //绑定数据
                holder.setText(R.id.id_num, dataBean);
            }
        };

        mAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(AdapterActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(AdapterActivity.this, "长按" + position, Toast.LENGTH_SHORT).show();
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
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
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
