package com.qgg.practice.view.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/9
 * @describe :
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDrawable;

    public LinearItemDecoration(Context context, int drawableResId) {
        mDrawable = ContextCompat.getDrawable(context, drawableResId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //给底部留10个px的空间 判断不是最后一个item就画，但是
        //parent.getChildCount()是在不断变化的，换个思路
        int position = parent.getChildAdapterPosition(view);
//            if (position != parent.getChildCount() - 1) {
//                outRect.bottom = 10;
//            }
        //给顶部留空间 判断不是第一个item就画
        //兼容 GridLayoutManager
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            outRect.top = mDrawable.getIntrinsicHeight();
        } else {
            if (position != 0) {
                outRect.top = mDrawable.getIntrinsicHeight();
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Rect rect = new Rect();
        for (int i = 1; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
//            rect.left = parent.getPaddingLeft() - layoutParams.leftMargin;
//            rect.right = childView.getWidth() + parent.getPaddingRight() + mDrawable.getIntrinsicWidth() + layoutParams.rightMargin;
//            rect.bottom = childView.getTop() - layoutParams.topMargin;

            rect.left = parent.getPaddingLeft();
            rect.right = parent.getWidth() + parent.getPaddingRight() + mDrawable.getIntrinsicWidth();
            rect.bottom = childView.getTop() - layoutParams.topMargin;
            rect.top = rect.bottom - mDrawable.getIntrinsicHeight();

            mDrawable.setBounds(rect);
            mDrawable.draw(c);
        }
    }
}
