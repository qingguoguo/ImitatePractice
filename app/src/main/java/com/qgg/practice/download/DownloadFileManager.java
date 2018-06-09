package com.qgg.practice.download;

import android.content.Context;

import java.io.File;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/30
 * @describe :
 */
class DownloadFileManager {
    private Context mContext;
    private File mRootDir;

    private static DownloadFileManager mInstance = new Singleton<DownloadFileManager>() {
        @Override
        protected DownloadFileManager create() {
            return new DownloadFileManager();
        }
    }.get();

    private DownloadFileManager() {
    }

    public static DownloadFileManager getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public void initFilePath(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file.exists() && file.isDirectory()) {
            mRootDir = file;
        }
    }

    /**
     * 通过 URL 获取文件路径
     *
     * @param url
     * @return
     */
    public File getFile(String url) {
        String fileName = Util.string2MD5(url);
        if (mRootDir == null) {
            mRootDir = mContext.getCacheDir();
        }
        return new File(mRootDir, fileName);
    }
}
