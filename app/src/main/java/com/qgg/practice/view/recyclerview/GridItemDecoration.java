package com.qgg.practice.view.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/9
 * @describe :
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDrawable;

    public GridItemDecoration(Context context, int drawableResId) {
        mDrawable = ContextCompat.getDrawable(context, drawableResId);
    }

    /**
     * RecyclerView 的测量阶段 measureChild() 方法里回调
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //给底部留10个px的空间 判断不是最后一个item就画，但是
        //parent.getChildCount()是在不断变化的，换个思路
        //最后一列，最后一行不画分割线

        int bottom = mDrawable.getIntrinsicHeight();
        int right = mDrawable.getIntrinsicWidth();
        if (isLastColumn(parent, view)) {
            right = 0;
        }
        if (isLastRow(parent, view)) {
            bottom = 0;
        }
        outRect.bottom = bottom;
        outRect.right = right;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    /**
     * 绘制横向的分割线
     *
     * @param c
     * @param parent
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        Rect rect = new Rect();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();

            rect.left = childView.getLeft() - layoutParams.leftMargin;
            rect.right = childView.getRight() + mDrawable.getIntrinsicWidth() + layoutParams.rightMargin;

            rect.top = childView.getBottom() + layoutParams.bottomMargin;
            rect.bottom = rect.top +mDrawable.getIntrinsicHeight();

            mDrawable.setBounds(rect);
            mDrawable.draw(c);
        }
    }

    /**
     * 绘制竖向的分割线
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        Rect rect = new Rect();
        for (int i = 0; i < parent.getChildCount(); i++) {
            //Log.e("GridItemDecoration", "onDraw , position:" + i + " , ChildCount:" + parent.getChildCount());

            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();

            rect.left = childView.getRight() + layoutParams.rightMargin;
            rect.right = rect.left + mDrawable.getIntrinsicWidth();
            rect.top = childView.getTop() - layoutParams.topMargin;
            rect.bottom = childView.getBottom() + layoutParams.bottomMargin;

            mDrawable.setBounds(rect);
            mDrawable.draw(c);
        }
    }

    /**
     * 获取列数
     *
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = 1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    /**
     * 是否最后一列
     *
     * @param parent
     * @param view
     * @return
     */
    private boolean isLastColumn(RecyclerView parent, View view) {
        int spanCount = getSpanCount(parent);
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        // 如果是最后一列，则不需要绘制右边
        //Log.e("GridItemDecoration", "onDraw , %: " + (pos + 1) % spanCount);
        return (pos + 1) % spanCount == 0;
    }

    /**
     * 是否最后一行
     *
     * @param parent
     * @param view
     * @return
     */
    private boolean isLastRow(RecyclerView parent, View view) {
        int childCount = parent.getAdapter().getItemCount();
        int spanCount = getSpanCount(parent);
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        // 行数 能整除childCount / spanCount
        int rowNum = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
        // 如果在最后一行
        return pos + 1 > (rowNum - 1) * spanCount;
    }
}