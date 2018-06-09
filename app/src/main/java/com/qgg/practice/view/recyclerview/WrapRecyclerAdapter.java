package com.qgg.practice.view.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.qgg.practice.view.recyclerview.clicklistener.OnItemClickListener;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemLongClickListener;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/10
 * @describe :
 */

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private int startHeadKey = 1000000;
    private int startFootKey = 2000000;
    private SparseArray<View> mHeadViews;
    private SparseArray<View> mFootViews;

    public WrapRecyclerAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
        mHeadViews = new SparseArray<>();
        mFootViews = new SparseArray<>();
    }

    @Override
    public int getItemCount() {
        return mHeadViews.size() + mAdapter.getItemCount() + mFootViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        int headNum = mHeadViews.size();
        if (isHeadPosition(position)) {
            //返回mHeadViews index的key值
            return mHeadViews.keyAt(position);
        }

        if (isFootPosition(position)) {
            //返回mFootViews index的key值
            position = position - mHeadViews.size() - mAdapter.getItemCount();
            return mFootViews.keyAt(position);
        }

        position = position - headNum;
        return mAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //头部和底部不需要绑定数据
        if (isHeadPosition(position) || isFootPosition(position)) {
            return;
        }
        //调整后的position
        final int adjustPosition = position - mHeadViews.size();
        mAdapter.onBindViewHolder(holder, adjustPosition);
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.itemView, adjustPosition);
                }
            });
        }
        if (mItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemLongClick(holder.itemView, adjustPosition);
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //头部 viewType 返回头部View
        if (isHeadViewType(viewType)) {
            return createHeadFootViewHolder(mHeadViews.get(viewType));
        }
        //底部 viewType 返回底部View
        if (isFootViewType(viewType)) {
            return createHeadFootViewHolder(mFootViews.get(viewType));
        }

        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    private RecyclerView.ViewHolder createHeadFootViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
        };
    }

    /**
     * 是否头部 ViewType
     *
     * @param viewType
     * @return
     */
    private boolean isFootViewType(int viewType) {
        return mFootViews.indexOfKey(viewType) >= 0;
    }

    /**
     * 是否头部 ViewType
     *
     * @param viewType
     * @return
     */
    private boolean isHeadViewType(int viewType) {
        return mHeadViews.indexOfKey(viewType) >= 0;
    }

    /**
     * 是否头部 position
     *
     * @param position
     * @return
     */
    private boolean isHeadPosition(int position) {
        return position < mHeadViews.size();
    }

    /**
     * 是否底部 position
     *
     * @param position
     * @return
     */
    private boolean isFootPosition(int position) {
        return position >= (mHeadViews.size() + mAdapter.getItemCount());
    }

    public void addHeadView(View headView) {
        if (mHeadViews.indexOfValue(headView) == -1) {
            mHeadViews.put(startHeadKey++, headView);
            notifyDataSetChanged();
        }
    }

    public void removeHeadView(View headView) {
        int index = mHeadViews.indexOfValue(headView);
        if (index >= 0) {
            mHeadViews.removeAt(index);
            notifyDataSetChanged();
        }
    }

    public void addFootView(View footView) {
        if (mFootViews.indexOfValue(footView) == -1) {
            mFootViews.put(startFootKey++, footView);
            notifyDataSetChanged();
        }
    }

    public void removeFootView(View footView) {
        int index = mFootViews.indexOfValue(footView);
        if (index >= 0) {
            mFootViews.removeAt(index);
            notifyDataSetChanged();
        }
    }

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     *
     * @param recycler
     */
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isHeadPosition(position) || isFootPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }
}
