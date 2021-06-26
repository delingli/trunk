package com.hkzr.wlwd.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.FileUtil;
import com.hkzr.wlwd.ui.utils.SDCardUtil;
import com.hkzr.wlwd.ui.utils.UpdataDialog;

import java.io.File;

/**
 * 系统设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rl_title;
    String nativeVersion;//本地当前版本号
    public static final String STORE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "XHW.apk";
    TextView tvBz;
    LinearLayout llXxxtx;
    TextView tvQchc;
    LinearLayout llQchc;
    TextView tvBbsj;
    LinearLayout llBbsj;
    ImageView ivLeft;
    TextView tvLeft;
    LinearLayout leftLL;
    TextView tvTitle;
    ImageView ivRight;
    TextView tvRight;
    LinearLayout rightLL;
    ImageView iv_new;

    private ProgressDialog progressDialog;

    private boolean isUpdata = false;
    private String cacheSize; // 缓存大小
    private File file;

    private void initViewBind() {
        tvBz = findViewById(R.id.tv_bz);
        rl_title = findViewById(R.id.rl_title);
        llXxxtx = findViewById(R.id.ll_xxxtx);
        tvQchc = findViewById(R.id.tv_qchc);
        llQchc = findViewById(R.id.ll_qchc);
        tvBbsj = findViewById(R.id.tv_bbsj);
        llBbsj = findViewById(R.id.ll_bbsj);
        ivLeft = findViewById(R.id.iv_left);
        tvLeft = findViewById(R.id.tv_left);
        leftLL = findViewById(R.id.left_LL);
        tvTitle = findViewById(R.id.tv_title);
        ivRight = findViewById(R.id.iv_right);
        rightLL = findViewById(R.id.right_LL);
        iv_new = findViewById(R.id.iv_new);
        findViewById(R.id.ll_xxxtx).setOnClickListener(this);
        findViewById(R.id.ll_qchc).setOnClickListener(this);
        findViewById(R.id.ll_bbsj).setOnClickListener(this);
        findViewById(R.id.left_LL).setOnClickListener(this);


    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        initViewBind();
        initViewDatas();
    }

    private void initViewDatas() {
        progressDialog = new ProgressDialog(this);
        file = SDCardUtil.getCacheDirectory(getApplicationContext());
        nativeVersion = App.getInstance().getVersionName();
        long l = FileUtil.getFileSize(file);
        cacheSize = FileUtil.formatFileSize(l);
        tvQchc.setText(cacheSize);
        tvTitle.setText(R.string.mine_set);
        isUpdata = App.getInstance().isUpdataApp();
        if (isUpdata) {
            tvBbsj.setText("V" + nativeVersion);
            iv_new.setVisibility(View.VISIBLE);
        } else {
            tvBbsj.setText("已是最新版本(V" + nativeVersion + ")");
            iv_new.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_xxxtx:
                jumpTo(NewMessageActivity.class);
                break;
            case R.id.ll_qchc:
                FileUtil.clearAllCache(this);
                tvQchc.setText("0KB");
                break;
            case R.id.ll_bbsj:
                if (isUpdata) {
                    UpdataDialog.showUpdataDialog(this, App.getInstance().getVersionCode(), App.getInstance().getDownloadUrl());
                } else {
                    UpdataDialog.showNewDialog(this);
                }
                break;
            case R.id.left_LL:
                finish();
                break;
        }
    }


}
