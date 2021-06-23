package com.hkzr.wlwd.ui.utils;

import android.text.TextUtils;
import android.util.Log;

import com.hkzr.wlwd.BuildConfig;


/**
 * 突破4K限制打印log
 */
public class LogUtil {
    public static void v(String tagName, String msg) {
        log(0, tagName, msg);
    }

    public static void d(String tagName, String msg) {
        log(1, tagName, msg);
    }

    public static void i(String tagName, String msg) {
        log(2, tagName, msg);
    }

    public static void w(String tagName, String msg) {
        log(3, tagName, msg);
    }

    public static void e(String tagName, String msg) {
        log(4, tagName, msg);
    }

    private static void log(int type, String tagName, String msg) {
        if (!BuildConfig.DEBUG)
            return;
        if (TextUtils.isEmpty(msg))
            return;
        int max_str_length = 2001 - tagName.length();
        while (msg.length() > max_str_length) {
            switch (type) {
                case 0:
                    Log.v(tagName, msg.substring(0, max_str_length));
                    break;
                case 1:
                    Log.d(tagName, msg.substring(0, max_str_length));
                    break;
                case 2:
                    Log.i(tagName, msg.substring(0, max_str_length));
                    break;
                case 3:
                    Log.w(tagName, msg.substring(0, max_str_length));
                    break;
                case 4:
                    Log.e(tagName, msg.substring(0, max_str_length));
                    break;
            }
            msg = msg.substring(max_str_length);
        }
        switch (type) {
            case 0:
                Log.v(tagName, msg);
                break;
            case 1:
                Log.d(tagName, msg);
                break;
            case 2:
                Log.i(tagName, msg);
                break;
            case 3:
                Log.w(tagName, msg);
                break;
            case 4:
                Log.e(tagName, msg);
                break;
        }
    }
}
