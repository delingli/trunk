/**
 * @author seek 951882080@qq.com
 * @version V1.0
 */

package com.hkzr.wlwd.httpUtils;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UploadFileTask extends AsyncTask<String, Void, String> {
    private UploadListener listener;
    private String url;
    private Map<String, String> additionParam = null;

    private String fileKey = "image";

    public void setFileParamName(String name) {
        fileKey = name;
    }

    public UploadFileTask(String url, UploadListener listener) {
        this.listener = listener;
        this.url = url;
    }

    public UploadFileTask setAddParams(String key, String value) {
        if (additionParam == null) {
            additionParam = new HashMap<String, String>();
        }
        additionParam.put(key, value);
        return this;
    }

    @Override
    protected void onPreExecute() {
        if (listener != null) {
            listener.onPreExecute(url);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        int size = params.length;
        FormFile[] files = new FormFile[size];
        for (int i = 0; i < size; i++) {
            File file = new File(params[i]);
            FormFile formfile = new FormFile(file.getName(), file, fileKey,
                    "application/octet-stream");
            files[i] = formfile;
        }
        String result;
        try {
            Map<String, String> stringParams = new HashMap<String, String>();
            if (additionParam != null) {
                stringParams.putAll(additionParam);
            }
            result = new FileUploadReq(HostProvider.getHost(url), stringParams,
                    files).post();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            Log.e(url, result);
//            BaseEntity baseResult = JSON.parseObject(result.toString(),
//                    BaseEntity.class);
//            if (baseResult.isSuccess()) {
//                if (listener != null) {
//                    listener.onFinishExecute(url, result);
//                }
//            } else {
//                if (listener != null)
//                    listener.onError(url, baseResult.getMessage());
//            }
        } else {
            if (listener != null)
                listener.onError(url, "unknow error!");
        }

    }
}
