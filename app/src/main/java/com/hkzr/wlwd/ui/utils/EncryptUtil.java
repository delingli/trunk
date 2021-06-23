package com.hkzr.wlwd.ui.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description:加密工具类
 * @Author:陈恩裕 ceyjava@163.com
 * @Date:2014-11-24 上午9:48:46
 */
public class EncryptUtil {

	/**
	 * MD5加密
	 * @param msg 要加密的内容
	 * @return 加密后的内容
	 */
	public static String md5(String msg) {
		MessageDigest md;
		StringBuilder password = new StringBuilder("");
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(msg.getBytes());
			byte[] bytes = md.digest();
			for (int i = 0; i < bytes.length; i++) {
				String param = Integer.toString((bytes[i] & 0xff) + 0x100, 16);
				password.append(param.substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return password.toString();
	}

}
