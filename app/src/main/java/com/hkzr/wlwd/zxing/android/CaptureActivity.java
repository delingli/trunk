package com.hkzr.wlwd.zxing.android;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.SDK_WebView;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.LiaoCarData;
import com.hkzr.wlwd.model.SYSBean;
import com.hkzr.wlwd.ui.app.User;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.productlist.Product;
import com.hkzr.wlwd.ui.utils.JumpSelect;
import com.hkzr.wlwd.ui.utils.LogUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.zxing.camera.CameraManager;
import com.hkzr.wlwd.zxing.view.ViewfinderView;
import com.iflytek.thirdparty.P;
import com.nostra13.dcloudimageloader.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个activity打开相机，在后台线程做常规的扫描；它绘制了一个结果view来帮助正确地显示条形码，在扫描的时候显示反馈信息，
 * 然后在扫描成功的时候覆盖扫描结果
 */
public final class CaptureActivity extends BaseActivity implements
        SurfaceHolder.Callback, View.OnClickListener {
    public static final String xcbf = "XCBF";
    public static final String tag = "tag";
    public static final String zljc = "ZLJC";
    public static final String cprk = "CPRK";
    private static final String TAG = CaptureActivity.class.getSimpleName();
    private LiaoCarData liaoCarData;
    private XingCaiPro mXingCaiPro;
    // 相机控制
    private TextView tv_sure;
    private TextView tv_cancel;
    private TextView tv_liao_value;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    // 电量控制
    private InactivityTimer inactivityTimer;
    // 声音、震动控制
    private BeepManager beepManager;

    private LinearLayout left_LL, ll_material_layout;
    private String tagss;
    private String bianma;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * OnCreate中初始化一些辅助类，如InactivityTimer（休眠）、Beep（声音）以及AmbientLight（闪光灯）
     */
//    @Override
//    public void onCreate(Bundle icicle) {
//        super.onCreate(icicle);
//        // 保持Activity处于唤醒状态
////        Window window = getWindow();
////        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//    }
    private Context mContext;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.capture);
        // 保持Activity处于唤醒状态
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        hasSurface = false;

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        this.mContext = this;
        if (getIntent() != null) {
            tagss = getIntent().getStringExtra(CaptureActivity.tag);
        }
        left_LL = (LinearLayout) findViewById(R.id.left_LL);
        tv_sure = findViewById(R.id.tv_sure);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_liao_value = findViewById(R.id.tv_liao_value);
        ll_material_layout = (LinearLayout) findViewById(R.id.ll_material_layout);
        left_LL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_sure.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                //请求
                toSubmitXingCai(bianma, mXingCaiPro.DistLJID);
                break;
            case R.id.tv_cancel:
                ll_material_layout.setVisibility(View.GONE);
                resumeCamera();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeCamera();
    }

    private void resumeCamera() {
        // CameraManager必须在这里初始化，而不是在onCreate()中。
        // 这是必须的，因为当我们第一次进入时需要显示帮助页，我们并不想打开Camera,测量屏幕大小
        // 当扫描框的尺寸不正确时会出现bug
        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);

        handler = null;

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // activity在paused时但不会stopped,因此surface仍旧存在；
            // surfaceCreated()不会调用，因此在这里初始化camera
            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();

        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onPause() {
        pauseCamera();
        super.onPause();
    }

    private void pauseCamera() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    /**
     * 扫描成功，处理反馈信息
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();

        boolean fromLiveScan = barcode != null;
        //这里处理解码完成后的结果，此处将参数回传到Activity处理
        if (fromLiveScan) {
            beepManager.playBeepSoundAndVibrate();
            LogUtil.d("ALL", rawResult.getText());
            if (isURL(rawResult.getText())) {
                if (zljc.equals(tagss)) {
                    toRequestZljc(rawResult.getText());
                    return;
                }


                Intent intent = new Intent(this, SDK_WebView.class);
                String url = rawResult.getText();
                if (url.contains("{tokenid}")) {
                    url = url.replace("{tokenid}", UserInfoCache.init().getTokenid());
                }
                intent.putExtra("title", "");
                intent.putExtra("url", url);
                intent.putExtra("isShowRight", false);
                startActivity(intent);
                finish();

            } else {
                bianma = rawResult.getText();
                if (xcbf.equals(tagss)) {
                    if (bianma.length() >= 36) {
                        toLiaoCar(rawResult.getText());//料车
                    } else if (bianma.length() == 12) {
                        toXingCai(rawResult.getText()); //型材
                    }
                } else if (zljc.equals(tagss)) {
                    toRequestZljc(rawResult.getText());

                } else if (cprk.equals(tagss)) {

                } else {
                    scancode(rawResult.getText());
                    finish();
                }
            }
//            Intent intent = getIntent();
//            intent.putExtra("codedContent", rawResult.getText());
//            intent.putExtra("codedBitmap", barcode);
//            setResult(RESULT_OK, intent);

        }

    }

    private void toRequestZljc(String text) {
//        {"action":"chk_scan_product","tokenId":"","data":"931794070711"}
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("action", "chk_scan_product");
        mParams.put("tokenId", UserInfoCache.init().getTokenid());
        mParams.put("data", text);

        JSONObject object = new JSONObject(mParams);
        LogUtil.d("ABC", object.toString());
        VolleyFactory.instance().xcbfPost(CaptureActivity.this, object, new VolleyFactory.AbsBaseRequest() {
            @Override
            public void requestFailed(String msg) {
                if (null != msg) {
                    ToastUtil.show(mContext, msg);
                }
                setResult(RESULT_OK, null);
                finish();
            }

            @Override
            public void requestSucceed(String ss) {
                LogUtil.d("ldlPP", ss);
                parseZljc(ss);
                finish();
            }
        });

    }

    private void parseZljc(String ss) {
        try {
            JSONObject obj = new JSONObject(ss);
            boolean success = obj.optBoolean("Success", false);
            String Message = obj.optString("Message");
            if (!success) {
                ToastUtil.show(this, Message);
                finish();
            }
            JSONObject returnObj = obj.optJSONObject("ReturnData");
            String id = returnObj.optString("ID");
            String ProductType = returnObj.optString("ProductType");
            String Assembly = returnObj.optString("Assembly");
            String FramePart = returnObj.optString("FramePart");
            Product pro = new Product();
            pro.Assembly = Assembly;
            pro.FramePart = FramePart;
            pro.id = id;
            pro.producttype = ProductType;
            Intent intent = new Intent();
            intent.putExtra("product", pro);
            setResult(RESULT_OK, intent);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Camera
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 显示底层错误信息并退出应用
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public static boolean isURL(String str) {
        //转换为小写
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return match(regex, str);


    }

    /**
     * 判断字符串是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean match(String regEx, String str) {
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 后台校验
     */
    private void scancode(String code) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "scancode");
        mParams.put("code", code);
        VolleyFactory.instance().post(CaptureActivity.this, mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {

                SYSBean sysBean = com.alibaba.fastjson.JSONObject.parseObject(object.toString(), SYSBean.class);
                if (sysBean != null) {
                    JumpSelect.jump(CaptureActivity.this, sysBean.getFunType(), sysBean.getFunLink());
                    finish();
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }

    /*---------------------------------------------------*/
    private void toLiaoCar(String data) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("action", "profile_scan_car");
        mParams.put("tokenId", UserInfoCache.init().getTokenid());
        mParams.put("data", data);
        org.json.JSONObject object = new org.json.JSONObject(mParams);
        VolleyFactory.instance().xcbfPost(CaptureActivity.this, object, new VolleyFactory.AbsBaseRequest() {
            @Override
            public void requestFailed(String msg) {
                if (null != msg) {
                    ToastUtil.show(mContext, msg);
                    LogUtil.d("ldl", msg);
                }
            }

            @Override
            public void requestSucceed(String string) {
                LogUtil.d("ldl", string);
                parseLiatChe(string);
            }
        });
/*        VolleyFactory.instance().xcbfPost(CaptureActivity.this, mParams, new VolleyFactory.AbsBaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                SYSBean sysBean = JSONObject.parseObject(object.toString(), SYSBean.class);
                if (sysBean != null) {
                    JumpSelect.jump(CaptureActivity.this, sysBean.getFunType(), sysBean.getFunLink());
                    finish();
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);*/
    }

    private void toXingCai(String data) {
//        {"action":"profile_scan","tokenId":"","data":{"carid":"9801d824-9c67-4d94-b391-715b7215b152","code":"018F00090033"}}
        if (liaoCarData == null || TextUtils.isEmpty(liaoCarData.ID)) {
            ToastUtil.show(mContext, "请先扫料车码");
            pauseCamera();
            resumeCamera();
            return;
        }
        Map<String, Object> mParams = new HashMap<String, Object>();
        Map<String, String> mParam2 = new HashMap<String, String>();
        mParam2.put("carid", liaoCarData.ID);
        mParam2.put("code", data);
        mParams.put("action", "profile_scan");
        mParams.put("tokenId", UserInfoCache.init().getTokenid());
        mParams.put("data", mParam2);

        org.json.JSONObject object = new org.json.JSONObject(mParams);
        LogUtil.d("ABC", object.toString());
        VolleyFactory.instance().xcbfPost(CaptureActivity.this, object, new VolleyFactory.AbsBaseRequest() {
            @Override
            public void requestFailed(String msg) {
                if (null != msg) {
                    ToastUtil.show(mContext, msg);
                }
                resumeCamera();
            }

            @Override
            public void requestSucceed(String string) {
                LogUtil.d("ldlPP", string);
                parseXingCai(string);
            }
        });
    }

    private void toSubmitXingCai(String code, String DistLJID) {
//        {"action":"profile_save","tokenId":"","data":{"lwid":"761f301b-6921-8f26-6a5c-5bfbbc5b8b01","code":"018F00090035"}}

        if (liaoCarData == null || TextUtils.isEmpty(liaoCarData.ID)) {
            ToastUtil.show(mContext, "请先扫料车码");
            pauseCamera();
            resumeCamera();
            return;
        }
        Map<String, Object> mParams = new HashMap<String, Object>();
        Map<String, String> mParam2 = new HashMap<String, String>();
        mParam2.put("lwid", DistLJID);
        mParam2.put("code", code);

        mParams.put("action", "profile_save");
        mParams.put("tokenId", UserInfoCache.init().getTokenid());
        mParams.put("data", mParam2);

        org.json.JSONObject object = new org.json.JSONObject(mParams);
        LogUtil.d("ABC", object.toString());
        VolleyFactory.instance().xcbfPost(CaptureActivity.this, object, new VolleyFactory.AbsBaseRequest() {
            @Override
            public void requestFailed(String msg) {
                if (null != msg) {
                    ToastUtil.show(mContext, msg);
                }
                ll_material_layout.setVisibility(View.GONE);
                resumeCamera();
            }

            @Override
            public void requestSucceed(String string) {
                LogUtil.d("ldlPP", string);
                parseSubmitData(string);
                resumeCamera();

            }
        });
    }

    private void parseSubmitData(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                org.json.JSONObject object = new org.json.JSONObject(str);
                boolean sucess = object.optBoolean("Success", false);
                String Message = object.optString("Message");
                if (sucess) {
                    ToastUtil.show(mContext, "提交成功");
                } else {
                    ToastUtil.show(mContext, Message);

                }
                ll_material_layout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void parseXingCai(String xingcai) {
        try {
            if (!TextUtils.isEmpty(xingcai)) {
                org.json.JSONObject object = new org.json.JSONObject(xingcai);
                boolean sucess = object.optBoolean("Success", false);
                String Message = object.optString("Message");
                if (sucess) {
                    mXingCaiPro = new XingCaiPro();
                    org.json.JSONObject objectData = object.optJSONObject("ReturnData");
                    mXingCaiPro.OrderId = objectData.optString("OrderId");
                    mXingCaiPro.Barcode = objectData.optString("Barcode");
                    mXingCaiPro.ID = objectData.optString("ID");
                    mXingCaiPro.CZID = objectData.optString("CZID");
                    mXingCaiPro.KSID = objectData.optString("KSID");
                    mXingCaiPro.Length = objectData.optInt("Length");
                    mXingCaiPro.FinishDescription = objectData.optString("FinishDescription");
                    mXingCaiPro.Name = objectData.optString("Name");
                    mXingCaiPro.DistLJID = objectData.optString("DistLJID");
                    mXingCaiPro.DistLJBM = objectData.optString("DistLJBM");
                    //todo 显示弹窗
                    ll_material_layout.setVisibility(View.VISIBLE);
                    tv_liao_value.setText(mXingCaiPro.DistLJBM);
                } else {
                    ToastUtil.show(mContext, Message);
                    resumeCamera();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseLiatChe(String ss) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(ss);
            org.json.JSONObject returnData = jsonObject.getJSONObject("ReturnData");
            String liaoName = returnData.getString("Name");
            liaoCarData = new LiaoCarData();
            liaoCarData.Code = returnData.getString("Code");
            liaoCarData.ID = returnData.getString("ID");
            liaoCarData.Name = liaoName;
            liaoCarData.QRCodeFileID = returnData.getString("QRCodeFileID");
            liaoCarData.Capacity = returnData.getInt("Capacity");
            if (!TextUtils.isEmpty(liaoName)) {
                ToastUtil.show(mContext, "扫码" + liaoName + "成功" + "请扫型材码");
                pauseCamera();
                resumeCamera();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class XingCaiPro {
        public String OrderId;
        public String Barcode;
        public String ID;
        public String CZID;
        public String KSID;
        public int Length;
        public String FinishDescription;
        public String Name;
        public String DistLJID;
        public String DistLJBM;
    }
}
