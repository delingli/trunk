package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences本地存储
 *
 * @author zl
 * @date 2016.1.26
 */
public class SPUtil {

    public static void write(Context context, String fileName, String k, int v) {
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putInt(k, v);
        editor.commit();
    }

    public static void write(Context context, String fileName, String k, long v) {
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putLong(k, v);
        editor.commit();
    }

    public static void write(Context context, String fileName, String k,
                             boolean v) {
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putBoolean(k, v);
        editor.apply();
    }

    public static void write(Context context, String fileName, String k,
                             String v) {
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putString(k, v);
        editor.apply();
    }

    public static long readLong(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getLong(k, 0);
    }

    public static int readInt(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getInt(k, 0);
    }

    public static int readInt(Context context, String fileName, String k,
                              int defv) {
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getInt(k, defv);
    }

    public static boolean readBoolean(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getBoolean(k, false);
    }

    public static String readString(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getString(k, null);
    }

    public static String[] readArrays(Context context, String fileName,
                                      String key) {
        String regularEx = "#";
        String[] str = null;
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        str = values.split(regularEx);
        return str;
    }

    public static void write(Context context, String fileName, String key,
                             String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            Editor et = sp.edit();
            et.putString(key, str);
            et.commit();
        }
    }

    public static void clear(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static String readString(String fileName, String k) {
        return readString(AppManager.getAppManager().currentActivity(),
                fileName, k);
    }
    public static void write(String fileName, String k, String v) {
        write(AppManager.getAppManager().currentActivity(), fileName, k, v);
    }
}
