 /**  

* @author seek 951882080@qq.com  

* @version V1.0  

*/ 

package com.hkzr.wlwd.httpUtils;

public interface UploadListener {
	/**
	 * 上传之前执行  比如显示对话框
	 * @param tag
	 */
	void onPreExecute(String tag);
	/**
	 * 执行结束后调用  返回结果
	 * @param tag 请求时的url
	 * @param result 结果
	 */
	void onFinishExecute(String tag, String result);
	/**
	 * 错误返回
	 * @param tag
	 * @param errMsg
	 */
	void onError(String tag, String errMsg);
}
