package com.hkzr.wlwd.ui.app;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.receiver.NotificationBroadcastReceiver;
import com.hkzr.wlwd.ui.utils.AppUtil;
import com.hkzr.wlwd.ui.utils.Constant;
import com.hkzr.wlwd.ui.utils.SPUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePalApplication;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import io.dcloud.common.adapter.util.UEH;


/**
 * Created by zl on 2016/6/21.
 * MultiDexApplication
 */
public class App extends MultiDexApplication {
    private String token;
    private String city;//城市名
    private static App instance;
    public static String appName = "WLWD";
    public static String app = "e云办公";
    public static boolean debuggable = true;
    private static Context context;
    private RequestQueue mRequestQueue;
    private static String deviceId;
    private int position = -1;
    private String rootUrl;
    private String h5Url;
    public final static int NOTIFY_TAG = 0x1335;
    private String versionCode = "";//后台返回的最新版本号
    private String downloadUrl = "";//后台返回的最新版本下载地址

    public boolean isQihooTrafficFreeValidate = true;

    public static int flag = -1;

    public static Context getContext() {
        return instance;
    }

    public static void setInstance(Context var0) {
        if (context == null) {
            context = var0;
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this);
        setInstance(this.getApplicationContext());
        UEH.catchUncaughtException(this.getApplicationContext());
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        instance = this;
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        context = this;
        mRequestQueue = Volley.newRequestQueue(this);
        CrashReport.initCrashReport(getApplicationContext(), "4102a4b1ca", false);
//        AppError err = new AppError();
//        err.initUncaught();
        SDKInitializer.initialize(this);

    }

    public void onLowMemory() {
        super.onLowMemory();

    }

    public void onTrimMemory(int var1) {
        super.onTrimMemory(var1);

    }

    public String getToken() {
        String tokenid = SPUtil.readString(instance, "app", "tokenid");
        return tokenid;
    }

    public String getUserName() {
        String user = SPUtil.readString(instance, Constant.SP_BASE, Constant.SP_ZH);
        return user;
    }


    public static App getInstance() {
        if (instance == null) {
            return instance = new App();
        } else {
            return instance;
        }
    }

    /**
     * 获取请求队列
     *
     * @return RequestQueue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }


    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        App.deviceId = deviceId;
    }


    public void setCity(String city) {
        SPUtil.write(getApplicationContext(), "area", "area", city);//保存城市名
        this.city = city;
    }

    public String getCity() {
//        if (TextUtils.isEmpty(city)) {
//            city = SPUtil.readString(this, "area", "area");
//        }
        return city;
    }


    /**
     * 获取用户ID
     *
     * @return
     */
    public String getUserID() {
        return SPUtil.readString(this, "user", "userId");
    }

    /**
     * 设置用户ID
     */
    public void setUserID(String userID) {
        SPUtil.write(this, "user", "userId", userID);
    }

    /**
     * 获取用户昵称
     *
     * @return
     */
    public String getUserNickName() {
        return SPUtil.readString(this, "user", "nickName");
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getRootUrl() {
        if (TextUtils.isEmpty(rootUrl)) {
            rootUrl = SPUtil.readString(this, "app", "root");
        }
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
        SPUtil.write(this, "app", "root", rootUrl);
    }

    public String getH5Url() {
        if (TextUtils.isEmpty(h5Url)) {
            h5Url = SPUtil.readString(this, "app", "h5Url");
        }
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
        SPUtil.write(this, "app", "h5Url", h5Url);
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * get App versionCode
     *
     * @return
     */
    public String getVersion() {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * get App versionName
     *
     * @return
     */
    public String getVersionName() {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //    public boolean isUpdataApp() {
//        if (!TextUtils.isEmpty(versionCode)) {
//            if (getVersionName().equals(versionCode)) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }
    
    public boolean isUpdataApp() {
        if (!TextUtils.isEmpty(versionCode)) {
            String version = getVersionName();
            return AppUtil.compareVersion(version, versionCode);
        }
        return false;
    }

    protected final static String msg = "%1个联系人发来%2条消息";

    private NotificationManager myManager = null;
    private Notification myNotification;
    public static final String STATUS_BAR_COVER_CLICK_ACTION = "com.hkzr.wlwd.NotificationBroadcastReceiver";
    NotificationBroadcastReceiver notificationBroadcastReceiver;

    @SuppressLint("NewApi")
    public void notifyChange(String content) {
        if (notificationBroadcastReceiver == null) {
            notificationBroadcastReceiver = new NotificationBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(STATUS_BAR_COVER_CLICK_ACTION);
        registerReceiver(notificationBroadcastReceiver, filter);
        Intent buttonIntent = new Intent(STATUS_BAR_COVER_CLICK_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(App.this, 100, buttonIntent, PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder buider = new Notification.Builder(this);
        buider.setAutoCancel(true);
        buider.setContentTitle(app);
        buider.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        buider.setContentText(content);
        buider.setSmallIcon(R.drawable.ic_launcher);
        buider.setWhen(System.currentTimeMillis());
        buider.setPriority(Notification.PRIORITY_HIGH);//高优先级
        buider.setContentIntent(pi);
        myNotification = buider.build();
        myManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        myManager.notify(NOTIFY_TAG, myNotification);
    }


    public void cancleNotify() {
        myManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        myManager.cancel(NOTIFY_TAG);
    }

    private void soundRing() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.reset();
        mp.setDataSource(context,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mp.prepare();
        mp.start();

    }
}
