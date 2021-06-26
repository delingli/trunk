package com.hkzr.wlwd.httpUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hkzr.wlwd.model.ServiceEntity;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.utils.DesUtil;
import com.hkzr.wlwd.ui.utils.GsonInstance;
import com.hkzr.wlwd.ui.utils.Log;
import com.hkzr.wlwd.ui.utils.LogUtil;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.utils.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Volley 工厂
 *
 * @author Liang
 */
public class VolleyFactory {
    private static VolleyFactory instance = null;
    private RequestQueue requestQueue = null;
    private static ProgressDialog dialog;

    public static VolleyFactory instance() {
        if (instance == null)
            instance = new VolleyFactory();
        return instance;
    }

    Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            super.handleMessage(msg);
        }
    };

    protected String getOfflineHash(String reqUrl, Object reqObject) {
        if (reqObject == null) {
            return "";
        }
        String reqHash = "" + reqUrl.hashCode();// url的hash
        String paramHash = "" + reqObject.toString().hashCode();// 对象的字符串形式hash

        // if (App.IsLogin()) {
        // return GlobalUser.getUser().getToken() + hash.hashCode();
        // } else {
        return reqHash + "_" + paramHash;
    }

    /**
     * 网络请求 Method : POST 默认展示dialog
     *
     * @param <T>
     */
    public <T> void post(final Context c,
                         Map<String, String> reqObject, final Class<T> repClass,
                         final BaseRequest<T> baseRequest) {
        post(c, reqObject, repClass, baseRequest, true, false);
    }

    /**
     * 网络请求 Method : POST
     *
     * @param <T>
     */
    public <T> void post(final Context c, String reqUrl, Map<String, String> reqObject,
                         final Class<T> repClass, final BaseRequest<T> baseRequest,
                         final boolean needDialog) {
        post(c, reqObject, repClass, baseRequest, false, false);
    }

    private String getDESParams(Object mParams) {
        String mJson = GsonInstance.gson().toJson(mParams);
        Log.e("请求(明文) : " + mJson);
        String str = DesUtil.desCrypto(mJson);
        Log.e("请求(密文) : " + str);
        return str;
    }

    ServiceEntity userEntity;

    public <T> void post(final Context c,
                         final Map<String, String> reqObject, final Class<T> repClass,
                         final BaseRequest<T> baseRequest, final boolean needDialog,
                         final boolean loadLocalCache) {
        if (c == null) {
            Log.e("上下文不能为空");
            return;
        }
        if (needDialog && c != null) {
            // dialog = DialogUtil.createLoadingDialog(c);
            dialog = new ProgressDialog(c);
            dialog.setMessage("加载中 ..");
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            // ((Activity) c).showDialog(DialogUtil.DIALOG_LOADING);
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(c);
        JSONObject params = new JSONObject(reqObject);
        Map<String, String> map = new HashMap<>();
        map.put("AppIdent", "");
        map.put("Sign", "");
        String data = "";
        if ("login".equals(reqObject.get("t"))) {
            data = DesUtil.desCrypto(params.toString());
        } else if ("modifypic".equals(reqObject.get("t"))) {
            map.put("photo", reqObject.get("photo"));
            map.put("TokenId", UserInfoCache.init().getTokenid());
            data = DesUtil.desCrypto(params.toString(), UserInfoCache.init().getKey());
        } else if ("chat:group_create".equals(reqObject.get("t")) || "chat:group_edit".equals(reqObject.get("t")) ||
                "sign:rec_submit".equals(reqObject.get("t")) || "sign:rec_update".equals(reqObject.get("t")) ||
                "sign:outsign".equals(reqObject.get("t"))) {
            map.put("photos", reqObject.get("photos"));
            map.put("TokenId", UserInfoCache.init().getTokenid());
            reqObject.remove("photos");
            params = new JSONObject(reqObject);
            data = DesUtil.desCrypto(params.toString(), UserInfoCache.init().getKey());
        } else {
            map.put("TokenId", UserInfoCache.init().getTokenid());
            data = DesUtil.desCrypto(params.toString(), UserInfoCache.init().getKey());
        }
        LogUtils.e("参数明文:=======" + params.toString());
        map.put("Data", data);
        LogUtils.e(map.toString());
        JSONObject mmap = new JSONObject(map);
        final String url = App.getInstance().getRootUrl();
        Log.e("request url : " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, mmap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonStr) {
                LogUtils.e(jsonStr.toString());
                if (needDialog) {
                    if (dialog.isShowing()) {
                        handler.sendEmptyMessage(0);
                    }
                }
                // Log.d("返回 jsonStr : " + jsonStr);
                if (jsonStr != null && !jsonStr.equals("")) {

                    BaseReq<T> repObject = null;
                    T ojsonObj = null;
                    try {
                        @SuppressWarnings("unchecked")
                        BaseReq<T> tmp = GsonInstance.gson().fromJson(
                                jsonStr.toString(), BaseReq.class);
                        repObject = tmp;

//                        ojsonObj = repObject.getValue(repClass);
                    } catch (Exception e) {
                        Log.e("反序列化失败!");
                        if (baseRequest != null)
                            baseRequest.requestFailed();
                        e.printStackTrace();
                        return;
                    }
                    try {
                        if (repObject != null) {
                            if ("0000".equals(repObject.ResultCode)
                                    && repObject.Success) { // 正常响应码
                                // 0000
                                if (repObject.ReturnData == null) {
                                    Log.e("返回(明文) : " + jsonStr);
                                }
                                if (baseRequest != null)
                                    baseRequest
                                            .requestSucceed(repObject.ReturnData);
                            } else {
                                ToastUtil.toast(c, repObject.Message);
                                if (baseRequest != null)
                                    baseRequest.requestFailed();
                                Log.e("返回错误 : " + jsonStr);
                                Log.e("错误接口：" + url);
                            }
                        } else {
                            ToastUtil.toast(c, "错误响应");
                            Log.e("返回错误 : " + jsonStr);
                            Log.e("错误接口：" + url);
                            if (baseRequest != null)
                                baseRequest.requestFailed();
                        }
                    } catch (Exception e) {
                        Log.e("逻辑回调处理异常!");
                        Log.e("错误接口：" + url);
                        e.printStackTrace();
                    }
                } else {
                    Log.e("返回错误 : " + jsonStr);
                    Log.e("错误接口：" + url);
                    ToastUtil.toast(c, "请求失败");
                    if (baseRequest != null)
                        baseRequest.requestFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!TextUtils.isEmpty(error.getMessage())) {
                    LogUtils.e(error.getMessage());
                    Log.e(error.toString());
                }
                if (needDialog) {

                    if (dialog.isShowing()) {
                        if (dialog.isShowing()) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
                ToastUtil.toast(c, "网络异常，请检查网络环境");
                if (baseRequest != null)
                    baseRequest.requestFailed();
            }
        });

        jsonObjectRequest.setRetryPolicy(RetryPolicyretryPolicy);
        requestQueue.add(jsonObjectRequest);
    }

    public void xcbfPost(final Context c,
                         final JSONObject mmap,
                         final AbsBaseRequest baseRequest) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(c);
        }
         String url = App.getInstance().getRootUrl();
        String urls = url.substring(0, url.lastIndexOf("/"));
        LogUtil.d("LDL", urls);
        urls = urls + "/mesapi.aspx";
        Log.e("request url : " + urls);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urls, mmap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonStr) {
                if (jsonStr != null) {
                    LogUtils.d("xiaoman", jsonStr.toString());
                    try {
                        String ResultCode = jsonStr.optString("ResultCode");
                        if (ResultCode.equals("0")) {
                            if (baseRequest != null) {
                                baseRequest.requestSucceed(jsonStr.toString());
                            }
                        } else {
                            String msg = jsonStr.optString("msg");
                            if (baseRequest != null) {
                                baseRequest.requestFailed(msg);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!TextUtils.isEmpty(error.getMessage())) {
                    LogUtils.e(error.getMessage());
                    Log.e(error.toString());
                }
                ToastUtil.toast(c, "网络异常，请检查网络环境");
                if (baseRequest != null) {
                    baseRequest.requestFailed(error.getMessage());

                }
            }
        });

        jsonObjectRequest.setRetryPolicy(RetryPolicyretryPolicy);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * 网络请求 Method : POST
     *
     * @param <T>
     */
    public <T> void FirstPost(final Context c, final String reqUrl,
                              final Map<String, String> reqObject, final Class<T> repClass,
                              final BaseRequest<T> baseRequest, final boolean needDialog,
                              final boolean loadLocalCache) {
        if (c == null) {
            Log.e("上下文不能为空");
            return;
        }
        if (reqUrl == null || reqUrl.equals("")) {
            Log.e("请求Url不能为空");
            return;
        }

        Log.e("request url : " + reqUrl + reqObject.toString());
        if (needDialog && c != null) {
            // dialog = DialogUtil.createLoadingDialog(c);
            dialog = new ProgressDialog(c);
            dialog.setMessage("加载中 ..");
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            // ((Activity) c).showDialog(DialogUtil.DIALOG_LOADING);
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(c);
        JSONObject params = new JSONObject(reqObject);
        String data = DesUtil.desCrypto(params.toString());
        Map<String, String> map = new HashMap<>();
//        if ("sms:pw_forget".equals(reqObject.get("t"))) {
//            map.put("TokenId", "");
//        }
        map.put("AppIdent", "");
        map.put("Sign", "");
        map.put("Data", data);
        LogUtils.e(map.toString());
        JSONObject mmap = new JSONObject(map);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reqUrl, mmap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonStr) {
                LogUtils.e(jsonStr.toString());
                if (needDialog) {
                    if (dialog.isShowing()) {
                        handler.sendEmptyMessage(0);
                    }
                }
                // Log.d("返回 jsonStr : " + jsonStr);
                if (jsonStr != null && !jsonStr.equals("")) {

                    BaseReq<T> repObject = null;
                    T ojsonObj = null;
                    try {
                        @SuppressWarnings("unchecked")
                        BaseReq<T> tmp = GsonInstance.gson().fromJson(
                                jsonStr.toString(), BaseReq.class);
                        repObject = tmp;

//                        ojsonObj = repObject.getValue(repClass);
                    } catch (Exception e) {
                        Log.e("反序列化失败!");
                        if (baseRequest != null)
                            baseRequest.requestFailed();
                        e.printStackTrace();
                        return;
                    }
                    try {
                        if (repObject != null) {
                            if ("0000".equals(repObject.ResultCode)
                                /*&& repObject.Successs*/) { // 正常响应码
                                // 0000
                                if (repObject.ReturnData == null) {
                                    Log.e("返回(明文) : " + jsonStr);
                                }
                                if (baseRequest != null)
                                    baseRequest
                                            .requestSucceed(repObject.ReturnData);
                            } else {
                                ToastUtil.toast(c, repObject.Message);
                                if (baseRequest != null)
                                    baseRequest.requestFailed();
                                Log.e("返回错误 : " + jsonStr);
                                Log.e("错误接口：" + reqUrl);
                            }
                        } else {
                            ToastUtil.toast(c, "错误响应");
                            Log.e("返回错误 : " + jsonStr);
                            Log.e("错误接口：" + reqUrl);
                            if (baseRequest != null)
                                baseRequest.requestFailed();
                        }
                    } catch (Exception e) {
                        Log.e("逻辑回调处理异常!");
                        Log.e("错误接口：" + reqUrl);
                        e.printStackTrace();
                    }
                } else {
                    Log.e("返回错误 : " + jsonStr);
                    Log.e("错误接口：" + reqUrl);
                    ToastUtil.toast(c, "请求失败");
                    if (baseRequest != null)
                        baseRequest.requestFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!TextUtils.isEmpty(error.getMessage())) {
                    LogUtils.e(error.getMessage());
                    Log.e(error.toString());
                }
                if (needDialog) {

                    if (dialog.isShowing()) {
                        if (dialog.isShowing()) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
                ToastUtil.toast(c, "网络异常，请检查网络环境");
                if (baseRequest != null)
                    baseRequest.requestFailed();
            }
        });
        jsonObjectRequest.setRetryPolicy(RetryPolicyretryPolicy);
        requestQueue.add(jsonObjectRequest);
    }


    RetryPolicy RetryPolicyretryPolicy = new DefaultRetryPolicy(6000, 0,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    public interface BaseRequest<T> {
        /**
         * 请求成功
         */
        public void requestSucceed(String string);

        /**
         * 请求失败
         */
        public void requestFailed();

    }

    public interface AbsBaseRequest {
        /**
         * 请求成功
         */
        public void requestSucceed(String string);

        /**
         * 请求失败
         */
        public void requestFailed(String msg);

    }


}
