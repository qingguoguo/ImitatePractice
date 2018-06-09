package com.qgg.practice;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qgg.practice.download.DownloadCallback;
import com.qgg.practice.download.DownloadFacade;
import com.qgg.practice.eventbus.EventBus;
import com.qgg.practice.eventbus.Subscribe;
import com.qgg.practice.eventbus.ThreadMode;
import com.qgg.practice.http.CacheRequestInterceptor;
import com.qgg.practice.http.CacheResponseInterceptor;
import com.qgg.practice.http.ExMultipartBody;
import com.qgg.practice.http.HttpCallBack;
import com.qgg.practice.http.HttpUtils;
import com.qgg.practice.http.OkHttpEngine;
import com.qgg.practice.view.navigationbar.NavigationBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NavigationBarTestActivity extends AppCompatActivity {

    private File mApkFile;
    private File mCacheFile;
    private TextView mTextView;

    String urlXunfei = "http://imtt.dd.qq.com/16891/42DF795A082D25AA87DB38EB319AB45D.apk?fsname=com.iflytek.speechcloud_1.1.1045_112.apk&csr=1bbd";
    String urlWx = "http://imtt.dd.qq.com/16891/6F77AEDEDB4F0E5728545F3AA8E0A1CB.apk?fsname=com.tencent.weishi_4.3.0.88_430.apk&csr=1bbd";
    String urlQQ = "http://imtt.dd.qq.com/16891/50328FE26F6F8CDC6089C8EDECF8845B.apk?fsname=com.tencent.mobileqq_7.6.3_850.apk&csr=1bbd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nagation_bar_test);
        View rooView = findViewById(R.id.root_view);

        NavigationBar navigationBar = new NavigationBar
                .Builder(this, R.layout.navigation_bar, (ViewGroup) rooView)
                .setText(R.id.back, "返回设置").setOnClickListener(R.id.back, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).create();
        EventBus.getDefault().register(this);
        mTextView = (TextView) findViewById(R.id.stop);

        mCacheFile = new File(Environment.getExternalStorageDirectory(), "cache");
        mApkFile = new File(Environment.getExternalStorageDirectory(), "fly.apk");
        //installFile(mApkFile);
    }

    public void download(View view) {
        //startActivity(new Intent(this, RegisterActivity.class));
        //httpUtilGet();
        //okHttpGet();
        //upLoad();
        //downLoad();
        multiThreadDownload(urlXunfei);
        multiThreadDownload(urlQQ);
        multiThreadDownload(urlWx);
        DownloadFacade.getFacade().stopDownload(urlXunfei);
        DownloadFacade.getFacade().stopDownload(urlQQ);
        DownloadFacade.getFacade().stopDownload(urlWx);
    }

    public void stop(View view) {
        DownloadFacade.getFacade().stopDownload(urlXunfei);
        DownloadFacade.getFacade().stopDownload(urlQQ);
        DownloadFacade.getFacade().stopDownload(urlWx);
    }

   public void restart(View view) {
        //startActivity(new Intent(this, RegisterActivity.class));
        //httpUtilGet();
        //okHttpGet();
        //upLoad();
        //downLoad();
       multiThreadDownload(urlXunfei);
       multiThreadDownload(urlQQ);
       multiThreadDownload(urlWx);
    }

    /**
     * 多线程下载
     */
    private void multiThreadDownload(String url) {
        DownloadFacade.getFacade().init(this).startDownload(url, new DownloadCallback() {
            @Override
            public void onFailure(IOException e) {
                LogUtils.i("onFailure: " + e);
            }

            @Override
            public void onSuccess(File file) {
                LogUtils.i("file: " + file);
                installFile(file);
            }
        });
    }

    /**
     * 单线程下载
     */
    private void downLoad() {
        String url = "http://imtt.dd.qq.com/16891/42DF795A082D25AA87DB38EB319AB45D.apk?fsname=com.iflytek.speechcloud_1.1.1045_112.apk&csr=1bbd";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i("onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.i("onResponse , response: " + response);
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream(mApkFile);
                int len;
                byte[] bytes = new byte[1024 * 10];
                while ((len = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);
                }
            }
        });
    }

    private void okHttpGet() {
        //请求同一个接口 有网 30s 内可以读缓存，没网每次请求都读缓存
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(mCacheFile, 100 * 1024 * 1024))
                .addInterceptor(new CacheRequestInterceptor(this))
                .addNetworkInterceptor(new CacheResponseInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://apis.juhe.cn/idcard/index?cardno=360726199310242641&dtype=json&key=f5767a38e224c26d30dd535aba2043de")
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.i("response.body: " + response.body().string());
                LogUtils.i("response.cacheResponse : " + response.cacheResponse() +
                        " , response.networkResponse : " + response.networkResponse());
            }
        });
    }

    private void httpUtilGet() {
        HttpUtils.with(this).exchangeEngine(new OkHttpEngine()).url("https://api.saiwuquan.com/api/appv2/sceneModel")
                .get()
                .execute(new HttpCallBack<String>() {
                    @Override
                    public void onError(Exception e) {
                        LogUtils.i(e.toString());
                        Toast.makeText(NavigationBarTestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtils.i(s);
                        Toast.makeText(NavigationBarTestActivity.this, s, Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * 上传文件
     */
    private void upLoad() {
        File file = new File(Environment.getExternalStorageDirectory(), "testfile.txt");
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();

        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("platform", "android");
        builder.addFormDataPart("mFile", file.getName(), RequestBody.create(
                MediaType.parse(guessMimeType(file.getAbsolutePath())), file
        ));

        MultipartBody multipartBody = builder.build();
        ExMultipartBody exMultipartBody = new ExMultipartBody(multipartBody);

        Request request = new Request.Builder().url("https://api.saiwuquan.com/api/upload")
                .post(exMultipartBody).build();

        exMultipartBody.setProgressListener(new ExMultipartBody.OkUpLoadProgressListener() {
            @Override
            public void onProgress(final long totalLength, final long currentLength) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NavigationBarTestActivity.this,
                                currentLength + "/" + totalLength, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.i(response.toString());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void updateText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private String guessMimeType(String filePath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimType = fileNameMap.getContentTypeFor(filePath);
        if (TextUtils.isEmpty(mimType)) {
            return "application/octet-stream";
        }
        return mimType;
    }

    /**
     * 安装APK
     *
     * @param file
     */
    private void installFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是 AndroidN 以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
}
