package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.IntentHelper;


/**
 * Created by admin on 2017/6/26.
 */
public class ExternalWebClientActivity extends BaseActivity {

    WebView wv;
    ImageView ivLeft;
    TextView tvLeft;
    LinearLayout leftLL;
    TextView tvTitle;
    ImageView ivRight;
    TextView tvRight;
    LinearLayout rightLL;
    RelativeLayout rlTitle;

    public void back() {
        this.finish();
    }

    private void initViewBind() {
        wv=   findViewById(R.id.wv);
        ivLeft=   findViewById(R.id.iv_left);
        tvLeft=   findViewById(R.id.tv_left);
        leftLL=   findViewById(R.id.left_LL);
        tvTitle=   findViewById(R.id.tv_title);
        ivRight=   findViewById(R.id.iv_right);
        tvRight=   findViewById(R.id.tv_right);
        rightLL=   findViewById(R.id.right_LL);
        rlTitle=   findViewById(R.id.rl_title);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.layout_web);
        initViewBind();
        tvTitle.setText("联系人详情页面");
        String url = getIntent().getStringExtra(IntentHelper.TITLE);
        initWebSetting();
        wv.loadUrl(url);
        wv.setFocusable(true);
        // 设置Web视图
        wv.setWebViewClient(new webViewClient());
    }

    private void initWebSetting() {
        WebSettings webSettings = wv.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        wv.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
    }

    // 设置回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wv.canGoBack()) {
                wv.goBack();
                return true;
            } else {
                finish();
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onPause() {
        wv.reload();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (wv != null) {
            wv.destroy();
            wv = null;
        }
        super.onDestroy();
    }
}
