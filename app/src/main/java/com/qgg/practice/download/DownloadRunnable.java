package com.qgg.practice.download;

import com.qgg.practice.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/30
 * @describe :
 */

public class DownloadRunnable implements Runnable {

    private static final int STATUS_DOWNLOADING = 0x001;
    private static final int STATUS_STOP = 0x003;
    private volatile int mStatus = STATUS_DOWNLOADING;
    private long mStart;
    private long mProgress;
    private long mEnd;
    private int mThreadId;
    private String mUrl;
    private DownloadCallback mDownloadCallback;
    private DownloadEntity mDownloadEntity;

    public DownloadRunnable(String url, int threadId, long start, long end,
                            DownloadEntity downloadEntity, DownloadCallback downloadCallback) {
        mStart = start;
        mThreadId = threadId;
        mEnd = end;
        mUrl = url;
        mDownloadEntity = downloadEntity;
        mProgress = downloadEntity.getProgress();
        mDownloadCallback = downloadCallback;
    }

    @Override
    public void run() {
        Response response;
        InputStream inputStream = null;
        RandomAccessFile accessFile = null;
        try {
            LogUtils.i("DownloadRunnable: " + this);
            response = DownloadOkHttpManager.getInstance().asyncResponse(mUrl, mStart, mEnd);

            //写数据
            inputStream = response.body().byteStream();
            File file = DownloadFileManager.getInstance().getFile(mUrl);
            accessFile = new RandomAccessFile(
                    file, "rwd");
            //移动指针
            accessFile.seek(mStart);

            int len;
            byte[] bytes = new byte[1024 * 10];
            while ((len = inputStream.read(bytes)) != -1) {
                if (mStatus == STATUS_STOP) {
                    //TODO 保存进度，把文件存到数据库
                    break;
                }
                mProgress += len;
                accessFile.write(bytes, 0, len);
            }
            mDownloadCallback.onSuccess(file);
        } catch (IOException e) {
            mDownloadCallback.onFailure(e);
            e.printStackTrace();
        } finally {
            // 保存到数据库
            mDownloadEntity.setProgress(mProgress);
            DaoManagerHelper.getManager().addEntity(mDownloadEntity);
            Util.closeStream(inputStream);
            Util.closeStream(accessFile);
        }
    }

    @Override
    public String toString() {
        return "DownloadRunnable{" +
                "mThreadId=" + mThreadId +
                ",mStart=" + mStart +
                ", mEnd=" + mEnd +
                ",mProgress=" + mProgress +
                '}';
    }

    public void stop() {
        mStatus = STATUS_STOP;
    }
}
