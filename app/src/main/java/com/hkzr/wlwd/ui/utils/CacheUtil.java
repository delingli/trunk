package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.util.Log;


import com.hkzr.wlwd.ui.app.App;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @Description:缓存工具类
 * @Date:2014-11-21 下午5:38:13
 */
public class CacheUtil {
	private static final String TAG = CacheUtil.class.getName();
	private static Context context = App.getInstance()
			.getApplicationContext();
	/** 最短缓存时间 */
	public static final int CACHE_TIMEOUT_MIN = 1000 * 60 * 5;
	/** 较短缓存时间 */
	public static final int CACHE_TIMEOUT_SHORT = 1000 * 60 * 30;
	/** 中等缓存时间 */
	public static final int CACHE_TIMEOUT_MEDIUM = 1000 * 60 * 60 * 24;
	/** 较长缓存时间 */
	public static final int CACHE_TIMEOUT_LONG = 11000 * 60 * 60 * 24 * 3;
	/** 最长缓存时间 */
	public static final int CACHE_TIMEOUT_MAX = 11000 * 60 * 60 * 24 * 7;

	/**
	 * @Description:缓存模式
	 * @Date:2014-11-21 下午5:53:14
	 */
	public enum CacheModel {
		CACHE_MODEL_MIN, CACHE_MODEL_SHORT, CACHE_MODEL_MEDIUM, CACHE_MODEL_LONG, CACHE_MODEL_MAX
	}

	/**
	 * 获取缓存
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            缓存的数据
	 * @param model
	 *            缓存模式
	 * @return String
	 */
	public static String getCache(String url, Map<String, String> params,
			CacheModel model) {
		if (url == null || "".equals(url)) {
			return null;
		}
		String encodeUrl = encodeUrl(url, params);
		// L.d("getCache", "encodeUrl=" + encodeUrl);
		String result = null;
		String path = SDCardUtil.getCacheDirectory(context) + File.separator
				+ EncryptUtil.md5(encodeUrl);
		// L.d("getCache", "path=" + path);
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			long expiredTime = System.currentTimeMillis() - file.lastModified();
			/** 网络可用时 */
			if (NetUtil.isNetworkAvailable(context)) {
				if (expiredTime < 0) {
					return null;
				}
				if (model == CacheModel.CACHE_MODEL_MIN) {
					if (expiredTime > CACHE_TIMEOUT_MIN) {
						return null;
					}
				} else if (model == CacheModel.CACHE_MODEL_SHORT) {
					if (expiredTime > CACHE_TIMEOUT_SHORT) {
						return null;
					}
				} else if (model == CacheModel.CACHE_MODEL_MEDIUM) {
					if (expiredTime > CACHE_TIMEOUT_MEDIUM) {
						return null;
					}
				} else if (model == CacheModel.CACHE_MODEL_LONG) {
					if (expiredTime > CACHE_TIMEOUT_LONG) {
						return null;
					}
				} else {
					if (expiredTime > CACHE_TIMEOUT_MAX) {
						return null;
					}
				}
			}
			try {
				result = FileUtil.readTextFile(file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return result;
	}

	/**
	 * 设置缓存
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param data
	 *            缓存数据
	 */
	public static void setCache(String url, Map<String, String> params,
			String data) {
		if (url == null || "".equals(url)) {
			return;
		}
		String encodeUrl = encodeUrl(url, params);
		// L.d("setCache", "encodeUrl=" + encodeUrl);
		File dir = new File(SDCardUtil.getCacheDirectory(context)
				.getAbsolutePath());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// L.d("setCache","fileDir=" + dir + File.separator +
		// EncryptUtil.md5(encodeUrl));
		File file = new File(dir + File.separator + EncryptUtil.md5(encodeUrl));
		try {
			FileUtil.writeTextFile(file, data);
		} catch (IOException e) {
			Log.d(TAG, "write " + file.getAbsolutePath() + " data failed!");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 带参数的缓存路径
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return String
	 */
	public static String encodeUrl(String url, Map<String, String> params) {
		if (!params.isEmpty() && params.size() > 0) {
			StringBuilder sb = new StringBuilder(url);
			sb.append("?");
			try {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
					sb.append('=');
					sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
					sb.append('&');
				}
				return sb.toString();
			} catch (UnsupportedEncodingException e) {
				Log.d(TAG, "Encoding not supported:UTF-8");
			}
		}
		return url;
	}

	/**
	 * 删除历史缓存文件
	 * 
	 * @param cacheFile
	 */
	public static void clearCache(File cacheFile) {
		if (cacheFile == null) {
			if (SDCardUtil.isSDCardEnabled()) {
				File cacheDir = new File(context.getExternalCacheDir()
						.getAbsolutePath());
				if (cacheDir.exists()) {
					clearCache(cacheDir);
				}
			}
			File cacheDir = new File(context.getCacheDir().getAbsolutePath());
			if (cacheDir.exists()) {
				clearCache(cacheDir);
			}
		} else if (cacheFile.isFile()) {
			cacheFile.delete();
		} else if (cacheFile.isDirectory()) {
			File[] childFiles = cacheFile.listFiles();
			for (int i = 0; i < childFiles.length; i++) {
				clearCache(childFiles[i]);
			}
		}
	}

}
