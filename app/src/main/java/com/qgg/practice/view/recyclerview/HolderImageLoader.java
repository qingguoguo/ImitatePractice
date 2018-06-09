package com.qgg.practice.view.recyclerview;

import android.content.Context;
import android.view.View;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/10
 * @describe :解耦图片加载
 */

public abstract class HolderImageLoader {

    private String mImagePath;

    public HolderImageLoader(String path) {
        mImagePath = path;
    }

    public String getPath() {
        return mImagePath;
    }

    /**
     * 加载图片的方法，根据需要自己实现
     *
     * @param context
     * @param imageView
     * @param imagePath
     */
    public abstract void loadImage(Context context, View imageView, String imagePath);

    public void loadImage(View imageView, String imagePath) {
        loadImage(null, imageView, imagePath);
    }
}
