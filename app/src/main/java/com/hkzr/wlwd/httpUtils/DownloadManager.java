package com.hkzr.wlwd.httpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mitchell on 2016/10/7.
 */

public class DownloadManager implements Runnable {
    private String downloadUrl;
    private String storePath;
    DownloadListener downloadListener;
    File storeFile;
    float length;
    public DownloadManager(String downloadUrl, String storePath, DownloadListener downloadListener) {
        this.downloadUrl = downloadUrl;
        this.storePath = storePath;
        this.downloadListener = downloadListener;
        Thread thread=new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        float currentLenth=0;
//        BufferedInputStream bufferedInputStream=null;
//        BufferedOutputStream bufferedOutputStream=null;
        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(downloadUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            length= conn.getContentLength();//获取文件大小
//            bufferedInputStream = new BufferedInputStream(url.openStream());
            inputStream=url.openStream();
            storeFile = new File(storePath);
            if (!storeFile.exists()) {
                if (!storeFile.getParentFile().exists()) {
                    storeFile.getParentFile().mkdirs();
                }
                storeFile.createNewFile();
            }else {
                storeFile.delete();
                if (!storeFile.getParentFile().exists()) {
                    storeFile.getParentFile().mkdirs();
                }
                storeFile.createNewFile();
            }
            fileOutputStream=new FileOutputStream(storePath);
//            bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
            byte[] buffer  = new byte[4*1024];// 缓冲数组
            int read  = 0;
            if(downloadListener!=null){
                downloadListener.onPrepareDownload(100);
            }
            int progress;
            int indicator=0;
            while ((read = inputStream.read(buffer))!=-1){
                fileOutputStream.write(buffer,0,read);
                currentLenth=currentLenth+read;
                progress = (int)(currentLenth / length * 100);
                if(progress>indicator){
                    downloadListener.onDowningListener(progress,100);
                    indicator=indicator+5;
                }
            }

            downloadListener.onDowningListener(100,100);
            try {
                fileOutputStream.close();
                inputStream.close();
                conn.disconnect();
                if(downloadListener!=null){
                    downloadListener.onComplete(100,storeFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
                downloadListener.onError(e);
            }

        } catch (IOException e) {
            e.printStackTrace();
            if(downloadListener!=null){
                downloadListener.onError(e);
            }
        }finally {

        }
    }
}
