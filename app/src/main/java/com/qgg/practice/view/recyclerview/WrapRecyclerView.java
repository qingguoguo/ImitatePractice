package com.qgg.practice.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.qgg.practice.view.recyclerview.clicklistener.OnItemClickListener;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemLongClickListener;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/10
 * @describe :可添加头部，底部 View 的 RecyclerView
 */

public class WrapRecyclerView extends RecyclerView {

    private WrapRecyclerAdapter mWrapRecyclerAdapter;
    private AdapterDataObserver mAdapterDataObserver;
    private RecyclerView.Adapter mAdapter;
    /**
     * 增加一些通用功能
     * 空列表数据应该显示的空View
     * 正在加载数据页面
     */
    private View mEmptyView, mLoadingView;

    public WrapRecyclerView(Context context) {
        this(context, null);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAdapterDataObserver = new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter == null) {
                    return;
                }
                // 列表Adapter更新 包裹的mWrapRecyclerAdapter也需要更新
                if (mWrapRecyclerAdapter != mAdapter) {
                    mWrapRecyclerAdapter.notifyDataSetChanged();
                }
                dataChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                if (mAdapter == null) {
                    return;
                }
                mWrapRecyclerAdapter.notifyItemRangeChanged(positionStart, itemCount);
                dataChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                if (mAdapter == null) {
                    return;
                }
                mWrapRecyclerAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
                dataChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if (mAdapter == null) {
                    return;
                }
                mWrapRecyclerAdapter.notifyItemRangeInserted(positionStart, itemCount);
                dataChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                if (mAdapter == null) {
                    return;
                }
                mWrapRecyclerAdapter.notifyItemRangeRemoved(positionStart, itemCount);
                dataChanged();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                if (mAdapter == null) {
                    return;
                }
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
                dataChanged();
            }
        };
    }

    @Override
    public void setAdapter(Adapter adapter) {
        //防止多次设置 adapter
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
            mAdapter = null;
        }
        this.mAdapter = adapter;
        if (adapter instanceof WrapRecyclerAdapter) {
            mWrapRecyclerAdapter = (WrapRecyclerAdapter) adapter;
        } else {
            mWrapRecyclerAdapter = new WrapRecyclerAdapter(adapter);
        }
        super.setAdapter(mWrapRecyclerAdapter);
        mAdapter.registerAdapterDataObserver(mAdapterDataObserver);
        // 解决GridLayoutManger添加头部和底部也要占据一行
        mWrapRecyclerAdapter.adjustSpanSize(this);

        // 加载数据页面
        if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.setVisibility(View.GONE);
        }

        if (mItemClickListener != null) {
            mWrapRecyclerAdapter.setItemClickListener(mItemClickListener);
        }

        if (mItemLongClickListener != null) {
            mWrapRecyclerAdapter.setItemLongClickListener(mItemLongClickListener);
        }
    }

    public void addHeadView(View headView) {
        if (mWrapRecyclerAdapter == null) {
            return;
        }
        mWrapRecyclerAdapter.addHeadView(headView);
    }

    public void removeHeadView(View headView) {
        if (mWrapRecyclerAdapter == null) {
            return;
        }
        mWrapRecyclerAdapter.removeHeadView(headView);
    }

    public void addFootView(View footView) {
        if (mWrapRecyclerAdapter == null) {
            return;
        }
        mWrapRecyclerAdapter.addFootView(footView);
    }

    public void removeFootView(View footView) {
        if (mWrapRecyclerAdapter == null) {
            return;
        }
        mWrapRecyclerAdapter.removeFootView(footView);
    }

    /**
     * 添加一个空列表数据页面
     */
    public void addEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 添加一个正在加载数据的页面
     */
    public void addLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
        mLoadingView.setVisibility(View.VISIBLE);
    }

    /**
     * Adapter数据改变的方法
     */
    private void dataChanged() {
        if (mWrapRecyclerAdapter.getItemCount() == 0) {
            // 没有数据
            if (mEmptyView != null) {
                mEmptyView.setVisibility(VISIBLE);
            }
//            else {
//                mEmptyView.setVisibility(GONE);
//            }
        }
    }

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.setItemClickListener(itemClickListener);
        }
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
        if (mWrapRecyclerAdapter != null) {
            mItemLongClickListener = itemLongClickListener;
        }
    }
}
