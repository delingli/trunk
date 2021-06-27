package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.app.App;

public class ToastUtil {
    public static void toast(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    private static Toast mToast;

    public static void show(Context c, String text) {
        TextView msgTv = null;
        if (mToast == null) {
            mToast = new Toast(c.getApplicationContext());
//            View.inflate()
            View view = View.inflate(c.getApplicationContext(), R.layout.my_toast_layout, null);
            msgTv = (TextView) view.findViewById(R.id.message);
            msgTv.setText(text);

            mToast.setView(view);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
        } else {
            msgTv = (TextView) mToast.getView().findViewById(R.id.message);
            msgTv.setText(text);
        }
        mToast.show();
    }

    public static void toast(Context c, int msgId) {
        Toast.makeText(c, msgId, Toast.LENGTH_SHORT).show();
    }

    public static void t(Object obj) {
        if (obj instanceof Integer) {
            Toast.makeText(AppManager.getAppManager().currentActivity(),
                    (Integer) obj, Toast.LENGTH_SHORT).show();
        } else if (obj instanceof CharSequence) {
            Toast.makeText(AppManager.getAppManager().currentActivity(),
                    (CharSequence) obj, Toast.LENGTH_SHORT).show();
        }
    }

    private static Toast toast;//单例的toast

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);//将文本设置给toast
        toast.show();
    }


}
