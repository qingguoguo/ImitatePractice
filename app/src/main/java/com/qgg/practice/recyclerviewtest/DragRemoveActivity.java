package com.qgg.practice.recyclerviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.qgg.practice.R;
import com.qgg.practice.view.recyclerview.CommonViewHolder;
import com.qgg.practice.view.recyclerview.GridItemDecoration;
import com.qgg.practice.view.recyclerview.RecyclerViewCommonAdapter;
import com.qgg.practice.view.recyclerview.WrapRecyclerAdapter;
import com.qgg.practice.view.recyclerview.WrapRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragRemoveActivity extends AppCompatActivity {

    private RecyclerView.Adapter  mAdapter;
    private WrapRecyclerView mRecyclerView;
    private List<String> mData;
    private WrapRecyclerAdapter wrapRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_remove);
        initData();

        mAdapter = new RecyclerViewCommonAdapter<String>(this, R.layout.item_home, mData) {
            @Override
            protected void convert(CommonViewHolder holder, String dataBean, int position) {
                //绑定数据
                holder.setText(R.id.id_num, dataBean);
            }
        };

        mRecyclerView = (WrapRecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,5));
        mRecyclerView.addItemDecoration(new GridItemDecoration(this, R.drawable.item_decoration));
        wrapRecyclerAdapter = new WrapRecyclerAdapter(mAdapter);
        mRecyclerView.setAdapter(wrapRecyclerAdapter);
        wrapRecyclerAdapter.adjustSpanSize(mRecyclerView);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 获取触摸响应的方向 包含两个 1.拖动dragFlags 2.侧滑删除swipeFlags
                // ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
                int dragFlags;
                int swipeFlags;

                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    //GridLayoutManager 四个方向都可以拖动，不可以删除
                    dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    swipeFlags = 0;
                } else {
                    //LinearLayoutManager 可以上下拖动，左右滑动删除
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }

                // 不处理可传0
                // makeMovementFlags(int dragFlags, int swipeFlags)
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 拖动的时候会不断的回调这个方法，拖动的时候肯定需要不断的更新列表数据
                // 达到一边拖动列表并不断更新当前数据
                // 拖动过程中不断回调
                // 要替换位置，那么可以在这里写
                int fromPosition = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                //只是替换了位置，还要更新数据源
                mAdapter.notifyItemMoved(fromPosition, targetPosition);
                //Collections 工具类
                if (fromPosition > targetPosition) {
                    for (int i = fromPosition; i > targetPosition; i--) {
                        Collections.swap(mData, i, i - 1);
                    }
                } else {
                    for (int i = fromPosition; i < targetPosition; i++) {
                        Collections.swap(mData, i, i + 1);
                    }
                }
                //回调 onMoved
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 侧滑删除之后的回调方法，可做更新操作
                int position = viewHolder.getAdapterPosition();
                // adapter 更新notify当前位置删除
                // mAdapter.notifyDataSetChanged();
                // 带动画的更新 notifyItemRemoved
                mAdapter.notifyItemRemoved(position);
                mData.remove(position);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                // 动画执行完毕
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#EBEBEB"));
                ViewCompat.setTranslationX(viewHolder.itemView, 0);
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                // 状态发生改变 拖动状态，侧滑状态，会有界面复用的问题 clearView方法中修复
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.GRAY);
                }
            }
        });
        // attach RecyclerView
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addHeadView(LayoutInflater.from(this).inflate(R.layout.item_head, mRecyclerView, false));
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
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
                wrapRecyclerAdapter.adjustSpanSize(mRecyclerView);
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
