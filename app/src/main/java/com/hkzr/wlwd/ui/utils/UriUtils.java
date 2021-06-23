package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;


import com.hkzr.wlwd.BuildConfig;

import java.io.File;

public class UriUtils {
    public static Uri getUri(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileproviders", file);
        }
        return uri;


    }
}
