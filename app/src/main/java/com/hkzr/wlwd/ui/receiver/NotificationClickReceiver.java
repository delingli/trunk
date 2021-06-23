package com.hkzr.wlwd.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.utils.UriUtils;

import java.io.File;

public class NotificationClickReceiver extends BroadcastReceiver {
    Context context;

    public NotificationClickReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        File apkFile = (File) intent.getSerializableExtra("extra");
        Intent intent1 = startInstallIntent(apkFile);
        context.startActivity(intent1);
    }


    /**
     * 请求安装
     *
     * @param file
     */
    private Intent startInstallIntent(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (file != null && file.exists()) {
            chmod(file.getAbsolutePath());//授权
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(UriUtils.getUri(context, file),
                    "application/vnd.android.package-archive");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            ToastUtil.showToast("安装失败,安装文件未找到");
        }
        return intent;
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
