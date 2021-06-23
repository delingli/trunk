package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Timer;

/**
 * Created by XHW on 2016/10/9.
 */

public class MyWebViewClient extends WebViewClient {
    Context context;
    Timer timer;
    long timeout;
    WebView webView;
    Handler mHandler;

    public MyWebViewClient(Context context,Timer timer,long timeout,WebView webView,Handler mHandler) {
        this.context=context;
        this.timer=timer;
        this.timeout=timeout;
        this.webView=webView;
        this.mHandler=mHandler;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
//        if(url.startsWith("http://")&&getRespStatus(url)==404){
//            ToastUtil.showToast("未找到页面");
//            view.stopLoading();
////            //载入本地assets文件夹下的错误提示页面
////            view.loadUrl("file://...");
//            Message msg = new Message();
//            msg.what = 1;
//            mHandler.sendMessage(msg);
//            view.stopLoading();
//        }else if(url.startsWith("http://")&&getRespStatus(url)==200){
//            ToastUtil.showToast("请求失效");
//            Message msg = new Message();
//            msg.what = 1;
//            mHandler.sendMessage(msg);
//            view.stopLoading();
//        }
//        else{
//            view.loadUrl(url);
//        }
        return true;
    }


    //正在加载
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
//        timer = new Timer();
//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//                        /*
//                         * 超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
//                         */
//                if (webView.getProgress() < 100) {
//                    Log.d("testTimeout", "timeout...........");
//                    Message msg = new Message();
//                    msg.what = 1;
//                    mHandler.sendMessage(msg);
//                    timer.cancel();
//                    timer.purge();
//                }
//            }
//        };
//        timer.schedule(tt, timeout, 1);
    }

    //加载完成
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
//        timer.cancel();
//        timer.purge();
    }

    //加载失败
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
//        ToastUtil.showToast("加载失败");
        Message msg = new Message();
        msg.what = 1;
        mHandler.sendMessage(msg);
    }

//    private int getRespStatus(String url){
//        int status=-1;
//        try{
//            HttpHead head = new HttpHead(url);
//            HttpClient client = new DefaultHttpClient();
//            HttpResponse resp= (HttpResponse) client.execute(head);
//            status=resp.getStatusCode();
//        }catch(IOException e){}
//        return status;
//    }


}
