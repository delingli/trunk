package com.hkzr.wlwd.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.base.BaseActivity;

/**
 * WebView基类
 */

public class WebActivity extends BaseActivity {

    WebView webView;
    TextView tv_title;


    public void back() {
        this.finish();
    }

    private void initViewBind() {
        webView = findViewById(R.id.web);
        tv_title = findViewById(R.id.tv_title);
        findViewById(R.id.left_LL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webview);
        initViewBind();
        String title = getIntent().getStringExtra("title");
        tv_title.setText(title);
        String url = getIntent().getStringExtra("url");
        initWebSetting();
        webView.loadUrl(url);
        webView.setFocusable(true);
        // 设置Web视图
        webView.setWebViewClient(new webViewClient());
    }

    private void initWebSetting() {
        WebSettings webSettings = webView.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                showDialog(message, "", result);
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      JsPromptResult result) {
                // TODO Auto-generated method stub
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
    }

    private void showDialog(final String message, final String type, final JsResult result) {
        AlertDialog.Builder b2 = new AlertDialog.Builder(WebActivity.this)
                .setTitle("提示").setMessage(message)
                .setPositiveButton("确定",
                        new AlertDialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (result != null) {
                                    result.confirm();
                                }
                            }
                        });

        b2.setCancelable(false);
        b2.create();
        b2.show();
    }

    // 设置回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                finish();
                return false;
            }
        }
        return false;
    }
    //获取JS返回的字符串数据、


    // Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onPause() {
        webView.reload();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
