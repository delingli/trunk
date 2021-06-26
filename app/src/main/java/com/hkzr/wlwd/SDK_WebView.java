package com.hkzr.wlwd;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.DialogUtil;
import com.hkzr.wlwd.ui.utils.StatusBarUtil;

import io.dcloud.EntryProxy;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.ICore.ICoreStatusListener;
import io.dcloud.common.DHInterface.ISysEventListener.SysEventType;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.feature.internal.sdk.SDK;

/**
 * 本demo为以webview控件方式集成5+ sdk，
 */
public class SDK_WebView extends BaseActivity implements View.OnClickListener {
    LinearLayout ll_root;
    ImageView iv_back;
    ImageView iv_close;
    TextView tv_title;
    ImageView iv_more;
    FrameLayout framelayout;

    boolean doHardAcc = true;
    EntryProxy mEntryProxy = null;
    String url = "";
    boolean isCanBack = true;

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        url = getIntent().getStringExtra("url");
//        if(TextUtils.isEmpty(url)){
//            url = "http://www.baidu.com";
//        }
//        id = getIntent().getIntExtra("id", 0);
//        if (mEntryProxy == null) {
//            FrameLayout rootView = new FrameLayout(this);
//            // 创建5+内核运行事件监听
//            WebviewModeListener wm = new WebviewModeListener(this, rootView);
//            // 初始化5+内核
//            mEntryProxy = EntryProxy.init(this, wm);
//            // 启动5+内核
//            mEntryProxy.onCreate(this, savedInstanceState, SDK.IntegratedMode.WEBVIEW, null);
//            setContentView(rootView);
//            StatusBarUtil.setStatusBarColor(SDK_WebView.this, R.color.main_color);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, R.color.main_color);

    }

    private void initBind() {
        ll_root = findViewById(R.id.ll_root);
        iv_back = findViewById(R.id.iv_back);
        iv_close = findViewById(R.id.iv_close);
        tv_title = findViewById(R.id.tv_title);
        iv_more = findViewById(R.id.iv_more);
        framelayout = findViewById(R.id.framelayout);
        iv_back.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        iv_more.setOnClickListener(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
//        StatusBarUtil.StatusBarLightMode(this);
//        StatusBarUtil.setStatusBarColor(this, R.color.color_0095DE);
        initBind();
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        if (!getIntent().getBooleanExtra("isShowRight", true)) {
            iv_more.setVisibility(View.GONE);
        } else {
            iv_more.setVisibility(View.VISIBLE);
        }
        if (mEntryProxy == null) {
            // 创建5+内核运行事件监听
            WebviewModeListener wm = new WebviewModeListener(this, framelayout);
            // 初始化5+内核
            mEntryProxy = EntryProxy.init(this, wm);
            // 启动5+内核
            mEntryProxy.onCreate(this, savedInstanceState, SDK.IntegratedMode.WEBVIEW, null);
//            setContentView(rootView);
//            ll_root.addView(rootView);

        }


    }

    @Override
    public void onPause() {
        mEntryProxy.onPause(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mEntryProxy.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEntryProxy.onStop(this);
        mEntryProxy = null;
        if (webviewInstance != null) {
            webviewInstance.destroy();
            webviewInstance = null;
        }
        webview = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mEntryProxy.onActivityExecute(this, SysEventType.onCreateOptionMenu, menu);
    }


    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getFlags() == Intent.FLAG_ACTIVITY_NEW_TASK) {
            url = intent.getStringExtra("url");
            String title = intent.getStringExtra("title");
            if (!TextUtils.isEmpty(title))
                tv_title.setText(title);
            if (TextUtils.isEmpty(url)) {
                url = "http://www.baidu.com";
            }
            webview.loadUrl(url);
        } else if (intent.getFlags() != 0x10600000) {// 非点击icon调用activity时才调用newintent事件
            mEntryProxy.onNewIntent(this, intent);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean _ret = mEntryProxy.onActivityExecute(this, SysEventType.onKeyDown, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean _ret = mEntryProxy.onActivityExecute(this, SysEventType.onKeyUp, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        boolean _ret = mEntryProxy.onActivityExecute(this, SysEventType.onKeyLongPress, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyLongPress(keyCode, event);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        try {
            int temp = this.getResources().getConfiguration().orientation;
            if (mEntryProxy != null) {
                mEntryProxy.onConfigurationChanged(this, temp);
            }
            super.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mEntryProxy.onActivityExecute(this, SysEventType.onActivityResult, new Object[]{requestCode, resultCode, data});
    }

    WebView webviewInstance;
    IWebview webview = null;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (!isBack) {
                    if (webviewInstance != null) {
                        if (webviewInstance.canGoBack()) {
                            webviewInstance.goBack();
                        } else {
                            this.finish();
                        }
                    }
                }
                break;
            case R.id.iv_close:
                if (!isClose) {
                    this.finish();
                }
                break;
            case R.id.iv_more:
                if (!isMore) {
                    webview.loadUrl("javascript:_app_menu()");
                }
                break;
        }
    }

    class WebviewModeListener implements ICoreStatusListener {

        LinearLayout btns = null;
        Activity activity = null;
        ViewGroup mRootView = null;
        Dialog pd = null;

        public WebviewModeListener(Activity activity, ViewGroup rootView) {
            this.activity = activity;
            mRootView = rootView;
            btns = new LinearLayout(activity);
            //显示ProgressDialog
            pd = DialogUtil.showDialogWait(activity, "页面加载中,请稍等...");
//        mRootView.setBackgroundResource(R.color.main_color);
            mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (webview != null) {
                        webview.onRootViewGlobalLayout(mRootView);
                    }
                }
            });
        }

        /**
         * 5+内核初始化完成时触发
         */
        @Override
        public void onCoreInitEnd(ICore coreHandler) {
            // 单页面集成时要加载页面的路径，可以是本地文件路径也可以是网络路径
            webview = SDK.createWebview(activity, url, new IWebviewStateListener() {
                @Override
                public Object onCallBack(int pType, Object pArgs) {
                    switch (pType) {
                        case IWebviewStateListener.ON_WEBVIEW_READY:
                            // 准备完毕之后添加webview到显示父View中，设置排版不显示状态，避免显示webview时，html内容排版错乱问题
                            ((IWebview) pArgs).obtainFrameView().obtainMainView().setVisibility(View.INVISIBLE);
                            SDK.attach(mRootView, ((IWebview) pArgs));
                            break;
                        case IWebviewStateListener.ON_PAGE_STARTED:
                            // 首页面开始加载事件
                            pd.show();
                            break;
                        case IWebviewStateListener.ON_PROGRESS_CHANGED:
                            // 首页面加载进度变化
                            break;
                        case IWebviewStateListener.ON_PAGE_FINISHED:
                            pd.dismiss();
                            // 页面加载完毕，设置显示webview
                            tv_title.setText(webview.obtainPageTitle());
                            webview.obtainFrameView().obtainMainView().setVisibility(View.VISIBLE);
                            break;
                    }
                    return null;
                }
            });
            webviewInstance = webview.obtainWebview();
            webviewInstance.getSettings().setSupportZoom(true);
            webviewInstance.getSettings().setBuiltInZoomControls(true);
            webviewInstance.addJavascriptInterface(new JsObject(), "ezapp");
            // 监听返回键
            webviewInstance.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (!isBack) {
                            if (isCanBack) {
                                if (webviewInstance != null) {
                                    if (webviewInstance.canGoBack()) {
                                        webviewInstance.goBack();
                                        isCanBack = false;
                                    } else {
                                        finish();
                                    }
                                }
                            } else {
                                isCanBack = true;
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
        }

        // 5+SDK 开始初始化时触发
        @Override
        public void onCoreReady(ICore coreHandler) {
            try {
                // 初始化5+ SDK，
                // 5+SDK的其他接口需要在SDK初始化后才能調用
                SDK.initSDK(coreHandler);
                // 当前应用可使用全部5+API
                SDK.requestAllFeature();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * // 通过代码注册扩展插件的示例
         * private void regNewApi() {
         * // 扩展插件在js层的标识
         * String featureName = "T";
         * // 扩展插件的原生类名
         * String className = "com.HBuilder.integrate.webview.WebViewMode_FeatureImpl";
         * // 扩展插件的JS层封装的方法
         * String content = "(function(plus){function test(){return plus.bridge.execSync('T','test',[arguments]);}plus.T = {test:test};})(window.plus);";
         * // 向5+SDK注册扩展插件
         * SDK.registerJsApi(featureName, className, content);
         * }
         **/

        @Override
        public boolean onCoreStop() {

            return false;
        }
    }

    boolean isBack = false;
    boolean isClose = false;
    boolean isMore = false;

    public class JsObject {
        @JavascriptInterface
        public void BtnDisabled(String str, boolean b) {
            if ("back".equals(str)) {
                isBack = b;
            } else if ("close_eye".equals(str)) {
                isClose = b;
            } else if ("more".equals(str)) {
                isMore = b;
            }
        }
    }
}
