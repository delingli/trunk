package com.hkzr.wlwd.httpUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.utils.NetUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:自定义JsonObjectRequest类
 * @Date:2014-9-30 下午4:53:37
 */
public class JsonObjectRequest extends Request<JSONObject> {
    private Map<String, String> _params;
    private Listener<JSONObject> mListener;

    /**
     * 自定义的JsonObjectRequest构造方法
     *
     * @param method        GET or POST
     * @param url           请求地址
     * @param params        请求参数
     * @param listener      成功
     * @param errorListener 失败
     */
    public JsonObjectRequest(int method, String url,
                             Map<String, String> params, Listener<JSONObject> listener,
                             ErrorListener errorListener) {
        super(method, ReqUrl.ROT_URL + url, errorListener);
        if(!NetUtil.isNetworkAvailable(App.getContext())){
            ToastUtil.showToast("当前网络异常");
            return;
        }
        mListener = listener;
        LogUtils.e(params.toString());
        if (method == Method.POST) {
            _params = params;
        }
//        Log.e("TAG", ReqUrl.ROT_URL + url + params.toString() );
//        Log.e("请求参数", ReqUrl.ROT_URL + url + params.toString() );
        setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * @param method
     * @param url           可以为空，如果为null 请求实际地址为baseurl
     * @param params
     * @param listener
     * @param errorListener
     * @param baseurl       基本请求地址，如果url为空，实际请求地址为url+baseurl
     */
    public JsonObjectRequest(int method, String url, Map<String, String> params,
                             Listener<JSONObject> listener, ErrorListener errorListener,
                             String baseurl) {
        super(method, baseurl + url, errorListener);
        if(!NetUtil.isNetworkAvailable(App.getContext())){
            ToastUtil.showToast("当前网络异常");
            return;
        }
        LogUtils.e(baseurl + url);
        LogUtils.e(params.toString());
        Log.e("请求参数：",baseurl + url+"  " + params.toString());
        mListener = listener;
        if (method == Method.POST) {
            _params = params;
        }
        setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * 自定义的JsonObjectRequest构造方法
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param listener      成功
     * @param errorListener 失败
     */
    public JsonObjectRequest(String url, Context context,
                             Listener<JSONObject> listener, ErrorListener errorListener,boolean isNeedDialog ) {
        super(Method.GET, ReqUrl.ROT_URL+url, errorListener);
        if(!NetUtil.isNetworkAvailable(App.getContext())){
            ToastUtil.showToast("当前网络异常");
            return;
        }
        Log.e("请求参数url：",url);
        mListener = listener;
        setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (isNeedDialog) {
            dialog = new ProgressDialog(context);
            dialog.setMessage("加载中 ..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                        @Override
                                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                                return true;
                                            } else {
                                                return false;
                                            }
                                        }
                                    }
            );
            dialog.show();
        }
    }
    private ProgressDialog dialog;
    /**
     * 自定义的JsonObjectRequest构造方法
     *
     * @param method        GET or POST
     * @param url           请求地址
     * @param params        请求参数
     * @param listener      成功
     * @param errorListener 失败
     * @param isNeedDialog  是否需要dialog
     */
    public JsonObjectRequest(int method, String url, Context context,
                             Map<String, String> params, Listener<JSONObject> listener,
                             ErrorListener errorListener, boolean isNeedDialog) {
        super(method, ReqUrl.ROT_URL + url, errorListener);
        if(!NetUtil.isNetworkAvailable(App.getContext())){
            ToastUtil.showToast("当前网络异常");
            return;
        }

        mListener = listener;
        LogUtils.e("请求参数  ： "+ ReqUrl.ROT_URL + url + "" + params.toString());
        if (method == Method.POST) {
            _params = params;
        }
        setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (isNeedDialog) {
            dialog = new ProgressDialog(context);
            dialog.setMessage("加载中 ..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                        @Override
                                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                                return true;
                                            } else {
                                                return false;
                                            }
                                        }
                                    }
            );
            dialog.show();
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return _params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Log.e("返回参数：",jsonString.toString());
            if(jsonString.equals("")){
                jsonString =   "{\"Success\":true,\"Msg\":\"登录成功\"}";
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    private String postHeader;

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> mHeaders = new HashMap<String, String>();
//        mHeaders.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//        mHeaders.put("Content-Type", "text/html; charset=utf-8");
        mHeaders.put("Content-Type","application/json; charset=utf-8");
        return mHeaders;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    /**
     * 拼接请求url
     *
     * @param url    请求url
     * @param params 请求参数
     * @return
     */
    private static String encodeUrl(String url, Map<String, String> params) {
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
                return sb.toString().substring(0, sb.toString().length() - 1);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "Encoding not supported: " + "UTF-8", e);
            }
        }
        return url;
    }
}
