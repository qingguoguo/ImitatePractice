package com.qgg.practice.view.taglayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/2
 * @describe :
 */

public class TagLayout extends ViewGroup {

    private List<List<View>> mChildViewList = new ArrayList<>();

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getPaddingBottom() + getPaddingTop();
        mChildViewList.clear();

        //每一行子View最大可用宽度
        int childTotalWidth = width - getPaddingLeft() - getPaddingRight();

        int rowWidth = 0;
        int maxHeight = 0;
        List<View> rowViews = new ArrayList<>();

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            //获取子View的LayoutParams
            ViewGroup.MarginLayoutParams params = getLayoutParams(childView);
            //水平方向margin
            int horizontalMargin = params.leftMargin + params.rightMargin;
            //垂直方向margin
            int verticalMargin = params.topMargin + params.bottomMargin;

            if (rowWidth + childView.getMeasuredWidth() + horizontalMargin > childTotalWidth) {
                //超过了最大可用宽度 childTotalWidth，换行
                //换行作为下一行 第一个view要先加上自己的宽度，后面的再开始累加
                rowWidth = childView.getMeasuredWidth() + horizontalMargin;
                height += maxHeight;
                mChildViewList.add(rowViews);
                //开始新的一行
                rowViews = new ArrayList<>();
            } else {
                //没有超过最大可用宽度 childTotalWidth，累加child的宽度到rowWidth
                rowWidth += childView.getMeasuredWidth() + horizontalMargin;
                maxHeight = Math.max(childView.getMeasuredHeight() + verticalMargin, maxHeight);
            }
            rowViews.add(childView);
        }

        mChildViewList.add(rowViews);
        height += maxHeight;
        setMeasuredDimension(width, height);
    }

    /**
     * 获取View的 LayoutParams
     *
     * @param childView
     * @return
     */
    private MarginLayoutParams getLayoutParams(View childView) {
        return (MarginLayoutParams) childView.getLayoutParams();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left, top = getPaddingTop(), right, bottom;

        for (List<View> views : mChildViewList) {

            //换行，重置left  maxHeight
            int maxHeight = 0;
            left = getPaddingLeft();

            for (View childView : views) {
                if (childView.getVisibility() == GONE) {
                    continue;
                }
                ViewGroup.MarginLayoutParams params = getLayoutParams(childView);

                left += params.leftMargin;
                int childTop = top + params.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();

                childView.layout(left, childTop, right, bottom);

                //left累加
                left += childView.getMeasuredWidth() + params.rightMargin;
                //保存当前行中最大的子View高度
                maxHeight = Math.max(maxHeight, childView.getMeasuredHeight() + params.topMargin + params.bottomMargin);
            }
            //换行，top加上一行的最大高度
            top += maxHeight;
        }
    }

    /**
     * 要获取 Margin参数需要重写 generateLayoutParams方法，参考LinearLayout源码
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewGroup.MarginLayoutParams(getContext(), attrs);
    }

    private FlowBaseAdapter mFlowBaseAdapter;
    private DataSetObserver mDataSetObserver;

    public void setAdapter(FlowBaseAdapter flowBaseAdapter) {
        if (flowBaseAdapter == null) {
            throw new NullPointerException("flowBaseAdapter 不能为 null");
        }

        if (mFlowBaseAdapter != null && mDataSetObserver != null) {
            mFlowBaseAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mFlowBaseAdapter = null;
        mFlowBaseAdapter = flowBaseAdapter;
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                resetLayout();
            }
        };
        mFlowBaseAdapter.registerDataSetObserver(mDataSetObserver);
        resetLayout();
    }

    private void resetLayout() {
        //先清空View
        removeAllViews();
        mFlowBaseAdapter.addViewAll(this);
        List<View> viewList = mFlowBaseAdapter.getData();
        for (int i = 0; i < mFlowBaseAdapter.getCounts(); i++) {
            addView(viewList.get(i));
        }
    }
}
