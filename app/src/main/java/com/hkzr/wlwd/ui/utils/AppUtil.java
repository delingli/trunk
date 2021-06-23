package com.hkzr.wlwd.ui.utils;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:App辅助类
 * @Date:2014-11-17 上午10:39:53
 */
public class AppUtil {

	/**
	 * 获取应用程序名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取应用程序版本号对应的名称
	 * 
	 * @param context
	 *            上下文
	 * @return String
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取应用程序版本号对应的代码
	 * 
	 * @param context
	 *            上下文
	 * @return int
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}


	/**
	 * 验证是否是URL
	 * @author YOLANDA
	 * @param url
	 * @return
	 */
	public static boolean isTrueURL(String url){
		String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
		Pattern patt = Pattern. compile(regex );
		Matcher matcher = patt.matcher(url);
		return matcher.matches();
	}
	/**
	 *
	 * @param version1 当前
	 * @param version2 线上
	 * @return true为线上大于当前   false 不需要更新
	 */
	public static boolean compareVersion(String version1, String version2) {
		if (version1.equals(version2)) {
			return false;
		}
		String[] version1Array = version1.split("\\.");
		String[] version2Array = version2.split("\\.");
		int index = 0;
		// 获取最小长度值
		int minLen = Math.min(version1Array.length, version2Array.length);
		int diff = 0;
		// 循环判断每位的大小
		while (index < minLen
				&& (diff = Integer.parseInt(version1Array[index])
				- Integer.parseInt(version2Array[index])) == 0) {
			index++;
		}
		if (diff == 0) {
			// 如果位数不一致，比较多余位数
			for (int i = index; i < version1Array.length; i++) {
				if (Integer.parseInt(version1Array[i]) > 0) {
					return false;
				}
			}

			for (int i = index; i < version2Array.length; i++) {
				if (Integer.parseInt(version2Array[i]) > 0) {
					return true;
				}
			}
			return false;
		} else {
			return !(diff > 0) ;
		}
	}
}
