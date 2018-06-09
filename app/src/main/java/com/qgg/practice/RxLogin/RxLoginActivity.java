package com.qgg.practice.RxLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/6
 * @describe :
 */

public class RxLoginActivity extends AppCompatActivity implements UMAuthListener {

    static final String PLATFORM_KEY = "platform_key";
    private UMShareAPI mUMShareAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ImageView imageView = new ImageView(this);
//        imageView.setBackground(new ColorDrawable(Color.TRANSPARENT));
//        setContentView(imageView);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mUMShareAPI = UMShareAPI.get(this);
        RxLoginPlatform rxLoginPlatform = (RxLoginPlatform) getIntent().getSerializableExtra(PLATFORM_KEY);
        mUMShareAPI.deleteOauth(this,platformChange(rxLoginPlatform),null);
        mUMShareAPI.getPlatformInfo(this, platformChange(rxLoginPlatform), this);
    }

    private SHARE_MEDIA platformChange(RxLoginPlatform platform) {
        switch (platform) {
            case PLATFORM_QQ:
                return SHARE_MEDIA.QQ;
            case PLATFORM_WEIXIN:
                return SHARE_MEDIA.WEIXIN;
            default:
                return SHARE_MEDIA.QQ;
        }
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        RxLogin.mUMAuthListener.onStart(share_media);
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        RxLogin.mUMAuthListener.onComplete(share_media, i, map);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        RxLogin.mUMAuthListener.onError(share_media, i, throwable);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        RxLogin.mUMAuthListener.onCancel(share_media, i);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
