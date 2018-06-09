package com.qgg.practice.RxLogin;

import android.app.Activity;
import android.content.Intent;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/6
 * @describe :
 */

public class RxLogin implements UMAuthListener {

    static UMAuthListener mUMAuthListener;
    private Activity mActivity;
    private PublishSubject<RxLoginResult> mEmitter;
    private RxLoginResult mRxLoginResult;

    private RxLogin(Activity activity) {
        mActivity = activity;
        mUMAuthListener = this;
        mEmitter = PublishSubject.create();
        mRxLoginResult = new RxLoginResult();
    }

    public static RxLogin create(Activity activity) {
        return new RxLogin(activity);
    }

    public Observable<RxLoginResult> doAuthVerify(RxLoginPlatform platform) {
        // 创建一个透明的Activity
        Intent intent = new Intent(mActivity, RxLoginActivity.class);
        intent.putExtra(RxLoginActivity.PLATFORM_KEY, platform);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(0, 0);
        ArrayList<PublishSubject<RxLoginResult>> publishSubjects = new ArrayList<>(1);
        publishSubjects.add(mEmitter);
        mRxLoginResult.setPlatform(platform);
        return Observable.concat(Observable.fromIterable(publishSubjects));
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        mRxLoginResult.setSucceed(true);
        mRxLoginResult.setMsg("第三方登录成功");
        mRxLoginResult.setUseInfoMap(map);
        mEmitter.onNext(mRxLoginResult);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        mRxLoginResult.setSucceed(false);
        throwable.printStackTrace();
        mRxLoginResult.setMsg("第三方登录失败");
        mEmitter.onNext(mRxLoginResult);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        mRxLoginResult.setSucceed(false);
        mRxLoginResult.setMsg("取消登录");
        mEmitter.onNext(mRxLoginResult);
    }
}
