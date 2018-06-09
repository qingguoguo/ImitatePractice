package com.qgg.practice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.qgg.practice.R;

/**
 * 作者:qingguoguo
 * 创建日期：2018/4/29 on 22:56
 * 描述:
 */

public class RatingBar extends View {

    private int mNorMalStarId;
    private int mFocusStarId;
    private int mMaxNum = 5;
    private int mCurrentStarNum;
    private int marginChild;
    private Bitmap mNorMalStarBitmap;
    private Bitmap mFocusStarBitmap;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        mNorMalStarId = ta.getResourceId(R.styleable.RatingBar_normalStar, 0);
        mFocusStarId = ta.getResourceId(R.styleable.RatingBar_focusStar, 0);
        mMaxNum = ta.getInteger(R.styleable.RatingBar_maxNum, 5);
        marginChild = ta.getDimensionPixelSize(R.styleable.RatingBar_marginChild, 5);
        mNorMalStarBitmap = BitmapFactory.decodeResource(getResources(), mNorMalStarId);
        mFocusStarBitmap = BitmapFactory.decodeResource(getResources(), mFocusStarId);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = mNorMalStarBitmap.getHeight();
        int width = mNorMalStarBitmap.getWidth() * mMaxNum + marginChild * (mMaxNum - 1);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //自己绘制
        Log.e("TAG", "onDraw:"+mCurrentStarNum + "");
        for (int i = 0; i < mMaxNum; i++) {
            int left = i * mFocusStarBitmap.getWidth() + i * marginChild;
            if (i < mCurrentStarNum) {
                //绘制选中的星星
                canvas.drawBitmap(mFocusStarBitmap, left, 0, null);
            } else {
                //绘制未选中的星星
                canvas.drawBitmap(mNorMalStarBitmap, left, 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", event.getAction() + "");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://0
                // case MotionEvent.ACTION_UP://1 减少invalidate次数
            case MotionEvent.ACTION_MOVE://2
                int x = (int) event.getX();
                if (x >= 0 && x <= getWidth() && event.getY() >= 0 && event.getY() <= getHeight()) {
                    //计算选中星星的个数
                    int starNum = x / (mFocusStarBitmap.getWidth() + marginChild) + 1;
                    //个数相同的情况不绘制
                    if (mCurrentStarNum != starNum) {
                        mCurrentStarNum = starNum;
                        invalidate();
                    }
                }
                return true;
        }
        return super.onTouchEvent(event);
    }
}
