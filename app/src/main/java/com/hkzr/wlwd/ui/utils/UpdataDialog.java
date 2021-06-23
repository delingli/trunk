package com.hkzr.wlwd.ui.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;

/**
 * Created by admin on 2016/9/14.
 */
public class UpdataDialog {
    public static void showUpdataDialog(Context context, final String version, final String url) {
        Dialog dialog = DialogUtil.showDialogConfirm(context, "提示", "有新版本是否更新?", "取消", null, "更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "xiazai");
                downloadApk(version, url);
            }
        }, View.VISIBLE);
        dialog.show();

    }

    public static void showNewDialog(Context context) {
        Dialog dialog = DialogUtil.showDialogConfirm(context, "提示", "已经是最新版本!", "确定", null, "更新", null, View.GONE);
        dialog.show();
    }

    private static void downloadApk(String versionCode, String downLoadUrl) {
        FinalHttp http = new FinalHttp();
        final String path = FileUtils.getApkFile(Constant.FILE_NAME_APK)
                + "wlwd" + versionCode + ".apk";
        checkFile(path);

        http.download(downLoadUrl, path, true, new AjaxCallBack<File>() {
            @Override
            public void onLoading(long count, long current) {
                int percent = (int) (current * 100 / count);
                UploadUtil.notifyChange("e云办公", percent);
            }

            @Override
            public void onSuccess(File t) {
                super.onSuccess(t);
                UploadUtil.installApk(AppManager.getAppManager().currentActivity(), t);
            }
        });
    }

    private static void checkFile(String path) {
        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            file.delete();
        }
    }
}
