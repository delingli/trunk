package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 
* @ClassName: CommonUtils 
* @Description: 
* @author yiw
* @date 2015-12-28 下午4:16:01 
*
 */
public class CommonUtils {

	public static void showSoftInput(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		//imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}
	
	public static void hideSoftInput(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
	}
	
	public static boolean isShowSoftInput(Context context){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		//获取状态信息
		return imm.isActive();//true 打开
	}

	/**
	 * 判断SDCard是否存在,并可写
	 *
	 * @return
	 */
	public static boolean checkSDCard() {
		String  flag = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(flag);
	}
	/**
	 * 检测网络是否可用
	 *
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
}
