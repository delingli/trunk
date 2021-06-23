package com.hkzr.wlwd.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hkzr.wlwd.ui.MainActivity;
import com.hkzr.wlwd.ui.app.App;


/**
 * Created by admin on 2017/8/20.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public NotificationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        App.getInstance().cancleNotify();
        Intent in = new Intent(context, MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//关键的一步，设置启动模式
        context.startActivity(in);
    }
}