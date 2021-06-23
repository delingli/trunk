package com.hkzr.wlwd.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.ReqUrl;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.base.BaseFragment;

import butterknife.Bind;

/**
 * web  通用fragment
 */

public class WebFragment extends BaseFragment {
    @Bind(R.id.webview)
    WebView mWebview;

    public WebFragment() {
    }

    int type;

    public static WebFragment newInstance(int type) {
        WebFragment f = new WebFragment();
        Bundle b = new Bundle();
        b.putInt("type", type);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public boolean isBack() {
        return mWebview.canGoBack();
    }

    public void back() {
        mWebview.goBack();
    }
    @Override
    public int getViewId() {
        return R.layout.fragment_web;
    }

    @Override
    public void initWidget(View parent) {
        findView(parent);
        initWebSetting();
        mWebview.setFocusable(true);
        // 设置Web视图
        mWebview.setWebViewClient(new webViewClient());
        if (type == 1) { //考勤统计
            mWebview.loadUrl(App.getInstance().getH5Url() + ReqUrl.KQReport + UserInfoCache.init().getTokenid());
        } else if (type == 2) { //足迹
            mWebview.loadUrl(App.getInstance().getH5Url() + ReqUrl.MyTracks + UserInfoCache.init().getTokenid());
        }
    }

    private void initWebSetting() {
        WebSettings webSettings = mWebview.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //不使用缓存：
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(false);
    }

    public void refresh() {
        if (type == 1) { //考勤统计
            mWebview.loadUrl(App.getInstance().getH5Url() + ReqUrl.KQReport + UserInfoCache.init().getTokenid());
        } else if (type == 2) { //足迹
            mWebview.loadUrl(App.getInstance().getH5Url() + ReqUrl.MyTracks + UserInfoCache.init().getTokenid());
        }
    }

    // Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("tag", url + "");
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebview.destroy();
    }
}
