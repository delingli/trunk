package com.hkzr.wlwd.ui.utils;


import com.hkzr.wlwd.ui.app.App;

/**
 * Log
 *
 * @author Liang
 */
public class Log {

    /**
     * Log v
     *
     * @param msg
     */
    public static void v(String msg) {
        if (App.debuggable)
            android.util.Log.v(App.appName, msg);
    }

    /**
     * Log d
     *
     * @param msg
     */
    public static void d(String msg) {
        if (App.debuggable)
            android.util.Log.d(App.appName, msg);
    }

    /**
     * Log d
     *
     * @param msg
     */
    public static void d(String tag, String msg) {
        android.util.Log.d(tag, msg);
    }

    /**
     * Log i
     *
     * @param msg
     */
    public static void i(String msg) {
        if (App.debuggable)
            android.util.Log.i(App.appName, msg);
    }

    /**
     * Log w
     *
     * @param msg
     */
    public static void w(String msg) {
        if (App.debuggable)
            android.util.Log.w(App.appName, msg);
    }

    /**
     * Log e
     *
     * @param msg
     */
    public static void e(String msg) {
        if (App.debuggable)
            android.util.Log.e(App.appName, msg);
    }

}
