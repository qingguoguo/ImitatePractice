package com.qgg.practice.http;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/29
 * @describe : MultipartBody代理 实现OkHttp上传文件进度监听
 */

public class ExMultipartBody extends RequestBody {

    private MultipartBody mMultipartBody;
    private long mCurrentLength;
    private OkUpLoadProgressListener mProgressListener;

    public ExMultipartBody(MultipartBody multipartBody) {
        mMultipartBody = multipartBody;
    }

    public void setProgressListener(OkUpLoadProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mMultipartBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mMultipartBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        // 要想拿到进度，需要知道当前写文件的长度，可使用代理
        // 如果代理 BufferedSink 需要重写的方法太多，比较麻烦
        // 看源码可以用 ForwardingSink
        ForwardingSink forwardingSink = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                mCurrentLength += byteCount;
                if (mProgressListener != null) {
                    mProgressListener.onProgress(contentLength(), mCurrentLength);
                }
                super.write(source, byteCount);
            }
        };
        BufferedSink bufferedSink = Okio.buffer(forwardingSink);
        mMultipartBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    public interface OkUpLoadProgressListener {
        /**
         * 回调上传文件进度
         * @param totalLength
         * @param currentLength
         */
        void onProgress(long totalLength, long currentLength);
    }
}
