package com.qgg.practice.download;

import java.io.File;
import java.io.IOException;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/30
 * @describe :
 */

public interface DownloadCallback {

    void onFailure(IOException e);

    void onSuccess(File file);
}
