package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.widget.Toast;

import com.hkzr.wlwd.ui.app.App;

public class ToastUtil {
	public static void toast(Context c, String msg) {
		Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
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
	public static void showToast(String text){
		if(toast==null){
			toast = Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);//将文本设置给toast
		toast.show();
	}


}
