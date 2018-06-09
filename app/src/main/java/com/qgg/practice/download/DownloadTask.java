package com.qgg.practice.download;

import android.support.annotation.Nullable;

import com.qgg.practice.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/30
 * @describe :最好可复用 使用享元设计模式
 */

public class DownloadTask {

    private DownloadCallback mDownloadCallback;
    private volatile int mDownloadSuccessNum;
    private List<DownloadRunnable> mRunnables;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));

    @Nullable
    private ExecutorService executorService;
    private long mContentLength;
    private String mUrl;

    public String getUrl() {
        return mUrl;
    }

    public DownloadTask(String url, long contentLength, DownloadCallback callback) {
        mContentLength = contentLength;
        mUrl = url;
        mDownloadCallback = callback;
        mRunnables = new ArrayList<>();
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, THREAD_SIZE,
                    30, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    Util.threadFactory("DownloadTask", false));
        }
        return executorService;
    }

    public void start() {
        long threadDownloadLength = mContentLength / THREAD_SIZE;
        LogUtils.i("mContentLength: " + mContentLength + "  threadDownloadLength:" + threadDownloadLength);

        for (int i = 0; i < THREAD_SIZE; i++) {
            //计算每个线程下载的内容，起始-截止
            long start = i * threadDownloadLength;
            long end = (start + threadDownloadLength) - 1;
            //有余数的情况
            if (i == THREAD_SIZE - 1) {
                end = mContentLength;
            }

            //从数据库读取进度
            List<DownloadEntity> downloadEntities = DaoManagerHelper.getManager().queryAll(mUrl);
            DownloadEntity downloadEntity = getDownloadEntity(i, downloadEntities);
            if (downloadEntity == null) {
                downloadEntity = new DownloadEntity(
                        start, end, mUrl, i, 0, mContentLength);
            }

            DownloadRunnable downloadRunnable = new DownloadRunnable(mUrl, i, start,
                    end, downloadEntity, new DownloadCallback() {
                @Override
                public void onFailure(IOException e) {
                    mDownloadCallback.onFailure(e);
                    //TODO 如果有一个线程异常了就需要处理异常
                    stop();
                }

                @Override
                public void onSuccess(File file) {
                    mDownloadSuccessNum += 1;
                    if (mDownloadSuccessNum == THREAD_SIZE) {
                        mDownloadCallback.onSuccess(file);
                        DownloadDispatcher.getInstance().recyclerTask(DownloadTask.this);
                        //TODO 文件下载成功 清除该文件临时存储
                    }
                }
            });
            mRunnables.add(downloadRunnable);
            //线程池执行
            executorService().execute(downloadRunnable);
        }
    }

    public void stop() {
        for (DownloadRunnable runnable : mRunnables) {
            runnable.stop();
        }
    }

    public DownloadEntity getDownloadEntity(int threadId, List<DownloadEntity> downloadEntities) {
        if (downloadEntities != null) {
            for (DownloadEntity entity : downloadEntities) {
                if (threadId == entity.getThreadId()) {
                    return entity;
                }
            }
        }
        return null;
    }

    public DownloadTask recycler(String url, long contentLength, DownloadCallback downloadCallback) {
        this.mUrl = url;
        this.mContentLength = contentLength;
        this.mDownloadCallback = downloadCallback;
        return this;
    }
}
