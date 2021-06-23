package com.hkzr.wlwd.ui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hkzr.wlwd.httpUtils.DownloadListener;
import com.hkzr.wlwd.httpUtils.DownloadManager;
import com.hkzr.wlwd.ui.receiver.NotificationClickReceiver;

import java.io.File;

/**
 * Created by Mitchell on 2016/10/8.
 */

public class DownloadAPKService extends Service {
    String downloadUrl;//下载地址
    String storePath;//本地保存地址
    public static final String KEY_DOWNURL = "KEY_DOWNURL";
    public static final String KEY_STOREURL = "KEY_STOREURL";
    public static final int BROADCAST_REQUESTCODE = 100;

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    int downloadProgress = 0;
    int notifycationID;
    private static final int ONPREPAREDOWNLOAD = 0;
    private static final int ONDOWNLOADING = 1;
    private static final int ONDOWNLOADFINISHED = 2;
    private static final int ONERROR = 3;
    private int totalCount;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            downloadUrl = intent.getStringExtra(KEY_DOWNURL);
            storePath = intent.getStringExtra(KEY_STOREURL);
            if (!TextUtils.isEmpty(downloadUrl) && !TextUtils.isEmpty(storePath)) {
                downloadFile(downloadUrl, storePath);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        notificationManager.cancel(notifycationID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 下载文件
     *
     * @param downloadUrl
     * @param storePath
     */
    private void downloadFile(String downloadUrl, String storePath) {
        new DownloadManager(downloadUrl, storePath, new DownloadListener() {
            @Override
            public void onPrepareDownload(int total) {
                totalCount = total;
                Message message = new Message();
                message.what = ONPREPAREDOWNLOAD;
                message.obj = total;
                handler.sendMessage(message);
            }

            @Override
            public void onDowningListener(int current, int total) {
                Message message = new Message();
                message.what = ONDOWNLOADING;
                message.obj = current;
                handler.sendMessage(message);
            }

            @Override
            public void onComplete(int total, File file) {
                Log.e("onDowning", "完成");
                Message message = new Message();
                message.what = ONDOWNLOADFINISHED;
                message.obj = file;
                handler.sendMessage(message);
//                stopSelf();//关闭Service
            }

            @Override
            public void onError(Exception e) {
                Log.e("onDowning", " 错误");
                Message message = new Message();
                message.what = ONERROR;
                message.obj = e;
                handler.sendMessage(message);
            }
        });
    }


    /**
     * 开启通知
     */
    private void startNorification(int max) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("正在更新");//设置通知栏标题
        builder.setTicker("正在更新"); //通知首次出现在通知栏，带上升动画效果的
        builder.setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        builder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
        builder.setOngoing(false);//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//        builder.setSmallIcon(R.drawable.logo);
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo));
        builder.setProgress(max, downloadProgress, false);
        notifycationID = max;
        notificationManager.notify(notifycationID, builder.build());

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ONPREPAREDOWNLOAD) {
                int total = (int) msg.obj;
                startNorification(total);
            } else if (msg.what == ONDOWNLOADING) {
                int current = (int) msg.obj;
                builder.setProgress(totalCount, current, false);
                notificationManager.notify(notifycationID, builder.build());
            } else if (msg.what == ONDOWNLOADFINISHED) {
                File file = (File) msg.obj;
                builder.setProgress(totalCount, totalCount, false);
                builder.setDefaults(Notification.DEFAULT_SOUND);
                builder.setContentTitle("下载完成，点击安装");
                builder.setTicker("下载完成");
                Intent clickIntent = new Intent(DownloadAPKService.this, NotificationClickReceiver.class);
                clickIntent.putExtra("extra", file);
                builder.setContentIntent(PendingIntent.getBroadcast(getApplicationContext(), BROADCAST_REQUESTCODE, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                builder.build().flags = Notification.FLAG_AUTO_CANCEL;//用户单击通知后自动消失
                notificationManager.notify(notifycationID, builder.build());
            } else if (msg.what == ONERROR) {
                Exception exception = (Exception) msg.obj;
                Toast.makeText(DownloadAPKService.this, "下载错误", Toast.LENGTH_SHORT).show();
                builder.setContentTitle("下载错误");
                builder.setTicker("下载错误");
                notificationManager.notify(notifycationID, builder.build());
                stopSelf();//关闭Service
            }
        }
    };

}