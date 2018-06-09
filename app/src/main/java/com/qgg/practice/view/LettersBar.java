package com.qgg.practice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.qgg.practice.R;

/**
 * 作者:王青 admin
 * 创建日期：2018/5/1 on 20:09
 * 描述:
 */
public class LettersBar extends View {

    private String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint mPaint;
    private int mTextSize;
    private int mTextColor;
    private float dy;
    private String mCurrentTouchLetter;
    private Paint mOnTouchPaint;
    private int mTextOnTouchColor;
    private int itemHeight;

    public LettersBar(Context context) {
        this(context, null);
    }

    public LettersBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LettersBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LettersBar);
        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.LettersBar_letterSize, (int) sp2px(10));
        mTextColor = typedArray.getColor(R.styleable.LettersBar_letterColor, Color.BLUE);
        mTextOnTouchColor = typedArray.getColor(R.styleable.LettersBar_letterSelectColor, Color.RED);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mTextColor);

        mOnTouchPaint = new Paint();
        mOnTouchPaint.setTextSize(mTextSize);
        mOnTouchPaint.setAntiAlias(true);
        mOnTouchPaint.setDither(true);
        mOnTouchPaint.setColor(mTextOnTouchColor);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        Log.e("TAG", "基线：" + dy);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < mLetters.length; i++) {
            float v = mPaint.measureText(mLetters[i]);
            Log.e("TAG", mLetters[i] + "宽度：" + v);
        }
        //宽度用最宽的字母
        int width = (int) (mPaint.measureText("W") + getPaddingLeft() + getPaddingRight());
        //高度直接获取
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x, y;
        itemHeight = (getHeight() - getPaddingBottom() - getPaddingTop()) / mLetters.length;
        for (int i = 0; i < mLetters.length; i++) {
            x = getWidth() - getPaddingLeft() - getPaddingRight() - mPaint.measureText(mLetters[i]);
            x = x / 2;
            y = itemHeight / 2 + dy;
            if (mLetters[i].equals(mCurrentTouchLetter)) {
                canvas.drawText(mLetters[i], x + getPaddingLeft(), y + itemHeight * i, mOnTouchPaint);
            } else {
                canvas.drawText(mLetters[i], x + getPaddingLeft(), y + itemHeight * i, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                boolean inX = x >= 0 && x <= getWidth();
                boolean inY = y >= 0 && y <= getHeight();
                if (inX && inY) {
                    int index = (int) (y / itemHeight);
                    String letter = mLetters[checkIndex(index, mLetters.length)];
                    if (letter.equals(mCurrentTouchLetter)) {
                        return true;
                    }
                    mCurrentTouchLetter = letter;
                    if (mSelectOnTouchListen != null) {
                        mSelectOnTouchListen.onTouch(mCurrentTouchLetter, false);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mSelectOnTouchListen != null) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSelectOnTouchListen.onTouch(mCurrentTouchLetter, true);
                        }
                    }, 1000);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private int checkIndex(int index, int length) {
        if (index < 0) {
            return 0;
        }
        if (index > length - 1) {
            return length - 1;
        }
        return index;
    }

    private SelectOnTouchListen mSelectOnTouchListen;

    public void setSelectOnTouchListen(SelectOnTouchListen selectOnTouchListen) {
        mSelectOnTouchListen = selectOnTouchListen;
    }

    interface SelectOnTouchListen {
        void onTouch(String letter, boolean isUp);
    }

    /**
     * sp 转 px
     *
     * @param sp
     * @return
     */
    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * @param dip
     * @return
     */
    private int dipToPx(int dip) {
        final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return (int) (displayMetrics.density * dip + 0.5f);
    }
}
