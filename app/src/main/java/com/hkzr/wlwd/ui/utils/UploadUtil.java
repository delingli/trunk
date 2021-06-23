package com.hkzr.wlwd.ui.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;


import com.hkzr.wlwd.R;

import java.io.File;


public class UploadUtil {
    File file;

    /**
     * 下面是一个自定义的回调函数，用到回调上传文件是否完成
     *
     * @author shimingzheng
     */
    public interface OnUploadProcessListener {
        /**
         * 上传响应
         *
         * @param responseCode
         * @param message
         */
        void onUploadFail(int responseCode, String message);

        /**
         * 上传响应
         *
         * @param responseCode
         * @param message
         */
        void onUploadDone(int responseCode, String message);
    }

    private static OnUploadProcessListener onUploadProcessListener;

    public void setOnMoreItemClickListener(OnUploadProcessListener onUploadProcessListener) {
        this.onUploadProcessListener = onUploadProcessListener;
    }

    /**
     * 更新
     *
     * @param title
     * @param percent
     */
    @SuppressLint("NewApi")
    public static void notifyChange(String title, int percent) {
        Context context = AppManager.getAppManager().currentActivity();
        Notification notification = null;
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        /*** 自定义  Notification 的显示****/
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_item);
        contentView.setImageViewResource(R.id.icon, R.drawable.ic_launcher);
        contentView.setProgressBar(R.id.progress1, 100, percent, false);
        contentView.setTextViewText(R.id.tv_progress, percent + "%");
        builder.setContent(contentView);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (100 == percent) {
            notificationManager.cancel(1121);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notification = builder.build();
            }
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notificationManager.notify(1121, notification);
        }
    }

    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (apkFile != null && apkFile.exists()) {
            chmod(apkFile.getAbsolutePath());//授权
            intent.setDataAndType(UriUtils.getUri(context, apkFile),
                    "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            ToastUtil.showToast("安装失败,安装文件未找到");
        }
    }

    public static void chmod(String pathc) {
        String chmodCmd = "chmod 666 " + pathc;
        try {
            Runtime.getRuntime().exec(chmodCmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
