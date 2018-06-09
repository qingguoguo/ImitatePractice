package com.qgg.practice.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.qgg.practice.LogUtils;
import com.qgg.practice.http.cache.CacheDataUtil;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/2
 * @describe :网络请求
 */

public class OkHttpEngine implements IHttpEngine {
    private static final java.lang.String TAG = "OkHttpEngine";
    private static OkHttpClient mOkHttpClient = new OkHttpClient();
    private static Handler mHandler = new Handler();
    private Call mCall;
    private EngineCallBack mEngineCallBack;

    @Override
    public void get(final boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String fUrl = HttpUtils.jointParams(url, params);
        LogUtils.i(TAG, "Get请求路径：" + url);
        //1.判断是否需要缓存
        if (cache) {
            //2.是否有缓存数据
            String cacheResultJson = CacheDataUtil.getCacheResultJson(fUrl);
            if (!TextUtils.isEmpty(cacheResultJson)) {
                LogUtils.i(TAG, "请求读到缓存先回调出去");
                callBack.onSuccess(cacheResultJson);
            }
        }

        Request.Builder requestBuilder = new Request.Builder().url(url).tag(context);

        //可以省略，默认是GET请求
        Request request = requestBuilder.build();
        mEngineCallBack = callBack;
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resultJson = response.body().string();
                LogUtils.i(TAG, "Get返回结果：" + resultJson);

                //网络请求返回来的结果，与缓存比较，如果不相同就返回，并添加到缓存中
                if (cache) {
                    String cacheResultJson = CacheDataUtil.getCacheResultJson(fUrl);
                    if (!TextUtils.isEmpty(resultJson) && resultJson.equals(cacheResultJson)) {
                        LogUtils.i(TAG, "Get返回结果和缓存一致：" + resultJson);
                        return;
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(resultJson);
                    }
                });

                LogUtils.i(TAG, "Get返回结果和缓存不一致：" + resultJson);
                if (cache) {
                    CacheDataUtil.cacheData(fUrl, resultJson);
                }
            }
        });
    }

    @Override
    public void post(boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        url = HttpUtils.jointParams(url, params);
        mEngineCallBack = callBack;
        LogUtils.i(TAG, "Post请求路径：" + url);
        // 了解 Okhttp
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
                          @Override
                          public void onFailure(okhttp3.Call call, final IOException e) {
                              mHandler.post(new Runnable() {
                                  @Override
                                  public void run() {
                                      callBack.onError(e);
                                  }
                              });
                          }

                          @Override
                          public void onResponse(okhttp3.Call call, Response response) throws IOException {
                              // 这个 两个回掉方法都不是在主线程中
                              final String result = response.body().string();
                              mHandler.post(new Runnable() {
                                  @Override
                                  public void run() {
                                      callBack.onSuccess(result);
                                  }
                              });
                          }
                      }
        );
    }

    /**
     * 组装post请求参数body
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    /**
     * 添加参数
     */
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // 处理文件 --> Object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    public void cancel() {
        mCall.cancel();
        mEngineCallBack.onCancel();
    }
}
