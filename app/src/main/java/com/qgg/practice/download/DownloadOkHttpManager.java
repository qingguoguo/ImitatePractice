package com.qgg.practice.download;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/30
 * @describe :
 */

public class DownloadOkHttpManager {

    private OkHttpClient mOkHttpClient;

    private static DownloadOkHttpManager mInstance = new Singleton<DownloadOkHttpManager>() {
        @Override
        protected DownloadOkHttpManager create() {
            return new DownloadOkHttpManager();
        }
    }.get();


    private DownloadOkHttpManager() {
        mOkHttpClient = new OkHttpClient.Builder().build();
    }

    public static DownloadOkHttpManager getInstance() {
        return mInstance;
    }

    public Call asyncCall(String url) {
        Request request = new Request.Builder().url(url).build();
        return mOkHttpClient.newCall(request);
    }

    public Response asyncResponse(String url, long start, long end) throws IOException {
        Request request = new Request.Builder().url(url)
                .addHeader("Range", "bytes=" + start + "-" + end).build();
        return mOkHttpClient.newCall(request).execute();
    }
}
