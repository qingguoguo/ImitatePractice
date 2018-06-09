package com.qgg.practice.download;

import com.qgg.practice.LogUtils;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/30
 * @describe :先获取到文件长度，在通过设置请求头，开多线程下载文件 随机流写文件 RandomAccessFile
 */

public class DownloadDispatcher {

    private int maxDownloadNum = 5;
    private final Deque<DownloadTask> mReadyTasks = new ArrayDeque<>();
    private final Deque<DownloadTask> mTaskPool = new ArrayDeque<>();
    private final Deque<DownloadTask> mRunningTasks = new ArrayDeque<>();
    private final Deque<DownloadTask> mStopTasks = new ArrayDeque<>();

    private static DownloadDispatcher mInstance = new Singleton<DownloadDispatcher>() {
        @Override
        protected DownloadDispatcher create() {
            return new DownloadDispatcher();
        }
    }.get();

    private DownloadDispatcher() {
    }

    public static DownloadDispatcher getInstance() {
        return mInstance;
    }

    void setMaxDownloadNum(int maxDownloadNum) {
        this.maxDownloadNum = maxDownloadNum;
    }

    public void startDownload(final String url, final DownloadCallback downloadCallback) {
        //先获取到文件长度，在通过设置请求头，开多线程下载文件 随机流写文件 RandomAccessFile
        Call call = DownloadOkHttpManager.getInstance().asyncCall(url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                downloadCallback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //先获取文件长度
                long contentLength = response.body().contentLength();
                if (contentLength <= -1) {
                    //TODO 可以单线程下载
                    return;
                }
                //TODO DownloadTask复用
                DownloadTask downloadTask = getDownloadTaskFormPool(url, contentLength, downloadCallback);
                //DownloadTask downloadTask = new DownloadTask(url, contentLength, downloadCallback);
                // 任务数是否超过最大并行下载数
                if (mRunningTasks.size() < maxDownloadNum) {
                    downloadTask.start();
                    mRunningTasks.add(downloadTask);
                } else {
                    mReadyTasks.add(downloadTask);
                }
            }
        });
    }

    private DownloadTask getDownloadTaskFormPool(String url, long contentLength, DownloadCallback downloadCallback) {
        if (mTaskPool.size() > 0) {
            DownloadTask downloadTask = mTaskPool.getFirst();
            return downloadTask.recycler(url, contentLength, downloadCallback);
        } else {
            return new DownloadTask(url, contentLength, downloadCallback);
        }
    }

    public void recyclerTask(DownloadTask downloadTask) {
        mRunningTasks.remove(downloadTask);
        //移除，如果还有需要下载的 ，可开始下载

        //TODO 复用 DownloadTask
        mTaskPool.addLast(downloadTask);

        DownloadTask lastTask = mReadyTasks.getFirst();
        mReadyTasks.removeFirst();
        mRunningTasks.addLast(lastTask);
        lastTask.start();
    }

    public void stopDownload(String url) {
        if (url == null) {
            return;
        }
        // 这个停止的不是正在下载的
        for (DownloadTask stopTask : mStopTasks) {
            if (url.equals(stopTask.getUrl())) {
                stopTask.stop();
            }
        }
        for (DownloadTask runningTask : mRunningTasks) {
            if (url.equals(runningTask.getUrl())) {
                runningTask.stop();
            }
        }
        for (DownloadTask readTask : mReadyTasks) {
            if (url.equals(readTask.getUrl())) {
                readTask.stop();
            }
        }
        LogUtils.i("DownloadDispatcher: " + this);
    }

    @Override
    public String toString() {
        return "DownloadDispatcher{" +
                "maxDownloadNum=" + maxDownloadNum +
                ", mReadyTasks=" + mReadyTasks +
                ", mTaskPool=" + mTaskPool +
                ", mRunningTasks=" + mRunningTasks +
                ", mStopTasks=" + mStopTasks +
                '}';
    }
}
