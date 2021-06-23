package com.hkzr.wlwd.ui.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * @Description:SD卡相关的工具类
 * @Date:2014-11-18 下午2:21:46
 */
public class SDCardUtil {
	/**
	 * 判断SDCard是否可用
	 * 
	 * @return boolean
	 */
	public static boolean isSDCardEnabled() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return String
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}

	/**
	 * 获取SD卡的剩余容量 单位byte
	 * 
	 * @return KB
	 */
	@SuppressWarnings("deprecation")
	public static long getSDCardAllSize() {
		if (isSDCardEnabled()) {
			StatFs stat = new StatFs(getSDCardPath());
			// 获取空闲的数据块的数量
			long availableBlocks = (long) stat.getAvailableBlocks();
			// 获取单个数据块的大小(byte)
			long blockSize = stat.getBlockSize();
			return blockSize * availableBlocks / 1024;
		}
		return 0;
	}

	public static File getCacheDirectory(Context context) {
		return getCacheDirectory(context, true);
	}

	public static File getCacheDirectory(Context context, boolean preferExternal) {
		File appCacheDir = null;
		String externalStorageState;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) {
			externalStorageState = "";
		}
		if (preferExternal
				&& Environment.MEDIA_MOUNTED.equals(externalStorageState)
				&& hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = context.getCacheDir().getPath();
			Log.d("",
					"Can't define system cache directory! cacheDirPath will be used.");
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context
				.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
		return perm == PackageManager.PERMISSION_GRANTED;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(
				Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(
				new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				Log.d("getExternalCacheDir:",
						"Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				Log.d("getExternalCacheDir",
						"Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}
}
