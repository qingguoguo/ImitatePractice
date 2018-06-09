package com.qgg.practice.download;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/31 10:08
 * @Describe :
 */
public class DownloadFacade {

    private static final DownloadFacade mFacade = new DownloadFacade();

    private DownloadFacade() {
    }

    public static DownloadFacade getFacade() {
        return mFacade;
    }

    public DownloadFacade init(Context context) {
        DownloadFileManager.getInstance().initFilePath(new File(
                Environment.getExternalStorageDirectory(), "qingguoguo"));
        DaoManagerHelper.getManager().init(context);
        return this;
    }

    public void startDownload(String url, DownloadCallback callback) {
        DownloadDispatcher.getInstance().startDownload(url, callback);
    }

    public void setMaxDownloadNum(int maxDownloadNum) {
        DownloadDispatcher.getInstance().setMaxDownloadNum(maxDownloadNum);
    }

    public void stopDownload(String url) {
        DownloadDispatcher.getInstance().stopDownload(url);
    }

    public void startDownload(String url) {
        // DownloadDispatcher.getDispatcher().startDownload(url);
    }
}
