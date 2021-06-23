/**  

 * @Title: AppHost.java 

 * @Description: get the app host

 * @author seek 951882080@qq.com  

 * @version V1.0  

 */

package com.hkzr.wlwd.httpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import android.text.TextUtils;

public class HostProvider {

	public static String getHost(String code) {
		return ReqUrl.ROT_URL + code;
	}

	/**
	 * wrap get url
	 * 
	 * @param code
	 * @param params
	 * @return
	 */
	public static String getHost(String code, Map<String, String> params) {
		try {
			return _MakeURL(ReqUrl.ROT_URL + code, params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String _MakeURL(String p_url, Map<String, String> params)
			throws UnsupportedEncodingException {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');
		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			// url.append(String.valueOf(params.get(name)));
			String value = params.get(name);
			if (TextUtils.isEmpty(value)) {
				value = "";
			}
			url.append(URLEncoder.encode(value, "utf-8"));
		}

		return url.toString().replace("?&", "?");
	}

}
