package com.qgg.practice.RxLogin;

import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/6
 * @describe :封装登录返回结果
 */

class RxLoginResult {

    private RxLoginPlatform mPlatform;
    private String msg;
    private boolean mIsSucceed;
    private Map<String, String> mUseInfoMap;

    public RxLoginPlatform getPlatform() {
        return mPlatform;
    }

    public void setPlatform(RxLoginPlatform platform) {
        mPlatform = platform;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSucceed() {
        return mIsSucceed;
    }

    public void setSucceed(boolean succeed) {
        mIsSucceed = succeed;
    }

    public Map<String, String> getUseInfoMap() {
        return mUseInfoMap;
    }

    public void setUseInfoMap(Map<String, String> useInfoMap) {
        mUseInfoMap = useInfoMap;
    }
}
