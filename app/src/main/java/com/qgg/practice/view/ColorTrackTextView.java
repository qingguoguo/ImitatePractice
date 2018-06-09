package com.qgg.practice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.qgg.practice.R;


/**
 * 作者:qingguoguo
 * 创建日期：2018/4/29 on 13:01
 * 描述:文字变色的TextView
 */

public class ColorTrackTextView extends TextView {

    private int mStartColor;
    private int mEndColor;
    private int mBaseLine;
    private Paint mPaint;
    private float mPercent;
    private String mText;
    private Rect mBounds;
    private Paint.FontMetrics mFontMetrics;
    private Orientation mOrientation = Orientation.left_to_right;

    public enum Orientation {
        left_to_right, right_to_left
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        mStartColor = ta.getColor(R.styleable.ColorTrackTextView_startColor, Color.BLUE);
        mEndColor = ta.getColor(R.styleable.ColorTrackTextView_endColor, Color.BLACK);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextSize(getTextSize());
        ta.recycle();
    }

    /**
     * 设置偏移百分比
     *
     * @param percent
     */
    public void setPosition(float percent) {
        this.mPercent = percent;
        invalidate();
    }

    /**
     * 设置方向
     *
     * @param orientation
     */
    public void setOrientation(Orientation orientation) {
        mOrientation = orientation;
    }

    /**
     * 设置改变后的颜色值
     *
     * @param color
     */
    public void setChangeColor(int color) {
        this.mEndColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //自己画不用系统的
        //super.onDraw(canvas);

        mText = (String) getText();
        mBounds = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mBounds);
        mFontMetrics = mPaint.getFontMetrics();
        mBaseLine = getHeight() / 2 + (int) ((mFontMetrics.bottom - mFontMetrics.top) / 2 - mFontMetrics.bottom);

        if (mOrientation == Orientation.left_to_right) {
            //从左往右
            //左边的文字 mEndColor
            mPaint.setColor(mEndColor);
            drawText(canvas, 0, getWidth() * mPercent, mPaint);
            //右边的文字 mStartColor
            mPaint.setColor(mStartColor);
            drawText(canvas, getWidth() * mPercent, getWidth(), mPaint);
        } else {
            //从右往左,右边是end,左边是start
            //左边的文字
            mPaint.setColor(mStartColor);
            drawText(canvas, 0, getWidth() - getWidth() * mPercent, mPaint);
            //右边的文字
            mPaint.setColor(mEndColor);
            drawText(canvas, getWidth() - getWidth() * mPercent, getWidth(), mPaint);
        }
    }

    /**
     * 画Text 起始位置 结束位置
     *
     * @param canvas
     * @param start
     * @param end
     * @param paint
     */
    private void drawText(Canvas canvas, float start, float end, Paint paint) {
        canvas.save();
        canvas.clipRect(start, 0, end, getHeight());
        canvas.drawText(mText, getWidth() / 2 - mBounds.width() / 2, mBaseLine, paint);
        canvas.restore();
    }
}
