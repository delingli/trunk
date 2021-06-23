package com.hkzr.wlwd.ui.receiver;

import android.content.Context;


import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;


/**
 * Created by admin on 2017/12/19.
 */

public class MiNotificationReceiver extends PushMessageReceiver {

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
    }
}
