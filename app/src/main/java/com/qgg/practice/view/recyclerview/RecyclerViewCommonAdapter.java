package com.qgg.practice.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qgg.practice.view.recyclerview.clicklistener.OnItemClickListener;
import com.qgg.practice.view.recyclerview.clicklistener.OnItemLongClickListener;

import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/9
 * @describe :RecyclerView的万能Adapter
 * <p>
 * View.inflate(mContext, mLayoutId, null);
 * mLayoutInflater.inflate(mLayoutId, parent);
 * mLayoutInflater.inflate(mLayoutId, parent, false);
 */

public abstract class RecyclerViewCommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    private int mLayoutId;
    private List<T> mData;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MultiTypeSupport<T> mMultiTypeSupport;

    public RecyclerViewCommonAdapter(Context context, int layoutId, List<T> data) {
        this.mLayoutId = layoutId;
        this.mData = data;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public RecyclerViewCommonAdapter(Context context, MultiTypeSupport<T> multiTypeSupport, List<T> data) {
        this.mLayoutId = -1;
        this.mContext = context;
        this.mMultiTypeSupport = multiTypeSupport;
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getLayoutId(mData.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("CommonAdapter", "onCreateViewHolder viewType : " + viewType + " , ViewGroup : " + parent);

        if (mMultiTypeSupport != null) {
            mLayoutId = viewType;
        }

        View itemView = mLayoutInflater.inflate(mLayoutId, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommonViewHolder holder, final int position) {
        convert(holder, mData.get(position), position);
        Log.e("CommonAdapter", "onBindViewHolder position : " + position + " , CommonViewHolder : " + holder);
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        if (mItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemLongClick(holder.itemView, position);
                }
            });
        }
    }

    /**
     * 要实现的绑定数据的方法
     *
     * @param holder   holder
     * @param dataBean 数据
     * @param position position
     */
    protected abstract void convert(CommonViewHolder holder, T dataBean, int position);

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }
}
