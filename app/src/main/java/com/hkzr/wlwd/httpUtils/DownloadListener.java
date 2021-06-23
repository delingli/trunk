package com.hkzr.wlwd.httpUtils;

import java.io.File;

/**
 * Created by Mitchell on 2016/10/7.
 */

public interface DownloadListener {
    void onPrepareDownload(int total);
    void onDowningListener(int current,int total);
    void onError(Exception e);
    void onComplete(int total,File file);
}
