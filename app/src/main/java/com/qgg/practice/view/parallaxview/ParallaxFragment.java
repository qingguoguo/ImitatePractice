package com.qgg.practice.view.parallaxview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.qgg.practice.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/15
 * @describe :
 */

public class ParallaxFragment extends Fragment implements LayoutInflaterFactory {

    private static final String LAYOUT_RES = "LayoutRes";
    private ParallaxCompatViewInflater mParallaxCompatViewInflater;
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
    private int[] mParallaxAttrs = {R.attr.translationXIn, R.attr.translationXOut,
            R.attr.translationYIn, R.attr.translationYOut};

    @LayoutRes
    private int mLayoutRes;
    private List<View> mViewList;

    public static ParallaxFragment newInstance(@LayoutRes int layoutRes) {
        ParallaxFragment fragment = new ParallaxFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RES, layoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    public List<View> getViewList() {
        return mViewList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mViewList = new ArrayList<>();
        if (args != null) {
            mLayoutRes = args.getInt(LAYOUT_RES);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = inflater.cloneInContext(getContext());
        // 直接传inflater setFactory会导致所有View的解析都走这个类
        // clone一个inflater出来
        LayoutInflaterCompat.setFactory(layoutInflater, this);
        return layoutInflater.inflate(mLayoutRes, container, false);
    }

    /**
     * LayoutInflaterFactory的 onCreateView
     *
     * @param parent
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = createView(parent, name, context, attrs);
        if (view != null) {
            analysisAttr(view, context, attrs);
        }
        return view;
    }

    /**
     * 解析自定义的属性
     *
     * @param view
     * @param context
     * @param attrs
     */
    private void analysisAttr(View view, Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, mParallaxAttrs);
        int indexCount = array.getIndexCount();
        ParallaxTag parallaxTag = new ParallaxTag();
        if (indexCount <= 0) {
            return;
        }
        for (int i = 0; i < indexCount; i++) {
            int index = array.getIndex(i);
            switch (index) {
                case 0:
                    parallaxTag.translationXIn = array.getFloat(index, 0);
                    break;
                case 1:
                    parallaxTag.translationXOut = array.getFloat(index, 0);
                    break;
                case 2:
                    parallaxTag.translationYIn = array.getFloat(index, 0);
                    break;
                case 3:
                    parallaxTag.translationYOut = array.getFloat(index, 0);
                    break;
                default:
                    break;
            }
        }
        //自定义属性 通过Key存在TAG里面
        view.setTag(R.id.parallax_tag, parallaxTag);
        mViewList.add(view);
        array.recycle();
    }

    @SuppressLint("RestrictedApi")
    private View createView(View parent, String name, Context context, AttributeSet attrs) {
        if (mParallaxCompatViewInflater == null) {
            mParallaxCompatViewInflater = new ParallaxCompatViewInflater();
        }
        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser) ?
                    ((XmlPullParser) attrs).getDepth() > 1 : shouldInheritContext((ViewParent) parent);
        }
        return mParallaxCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            return false;
        }
        while (true) {
            if (parent == null) {
                return true;
            } else if (!(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                return false;
            }
            parent = parent.getParent();
        }
    }
}
