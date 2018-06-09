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
import com.qgg.practice.view.recyclerview.MultiTypeSupport;
import com.qgg.practice.view.recyclerview.RecyclerViewCommonAdapter;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemClickListener;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeSupportActivity extends AppCompatActivity {

    private RecyclerViewCommonAdapter<ChatBean> mAdapter;
    private RecyclerView mRecyclerView;
    private List<ChatBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multi_type_support_click);
        initData();
        initAdapter();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridItemDecoration(this, R.drawable.item_decoration));

        //mRecyclerView.addItemDecoration(new GridItemDecoration(this, R.drawable.item_decoration));

        mRecyclerView.setAdapter(mAdapter);
    }

    private void initAdapter() {
        mAdapter = new RecyclerViewCommonAdapter<ChatBean>(this, new MultiTypeSupport<ChatBean>() {
            @Override
            public int getLayoutId(ChatBean chatBean) {
                if (chatBean.getIsMe() == 0) {
                    return R.layout.item_me;
                } else {
                    return R.layout.item_friend;
                }
            }
        }, mData) {
            @Override
            protected void convert(CommonViewHolder holder, ChatBean dataBean, int position) {
                //绑定数据
                holder.setText(R.id.id_num, dataBean.getChatContent());
            }
        };

        mAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MultiTypeSupportActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(MultiTypeSupportActivity.this, "长按" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    protected void initData() {
        mData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mData.add(new ChatBean("聊天内容" + i, i % 3));
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

    class ChatBean {
        private String mChatContent;
        private int mIsMe;

        public ChatBean(String chatContent, int isMe) {
            mChatContent = chatContent;
            mIsMe = isMe;
        }

        public String getChatContent() {
            return mChatContent;
        }

        public int getIsMe() {
            return mIsMe;
        }
    }
}
