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

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 系统设置
 */
public class SettingActivity extends BaseActivity {
    @Bind(R.id.rl_title)
    RelativeLayout rl_title;
    String nativeVersion;//本地当前版本号
    public static final String STORE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "XHW.apk";
    @Bind(R.id.tv_bz)
    TextView tvBz;
    @Bind(R.id.ll_xxxtx)
    LinearLayout llXxxtx;
    @Bind(R.id.tv_qchc)
    TextView tvQchc;
    @Bind(R.id.ll_qchc)
    LinearLayout llQchc;
    @Bind(R.id.tv_bbsj)
    TextView tvBbsj;
    @Bind(R.id.ll_bbsj)
    LinearLayout llBbsj;
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.left_LL)
    LinearLayout leftLL;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.right_LL)
    LinearLayout rightLL;
    @Bind(R.id.iv_new)
    ImageView iv_new;

    private ProgressDialog progressDialog;

    private boolean isUpdata = false;
    private String cacheSize; // 缓存大小
    private File file;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
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


    @OnClick({R.id.ll_xxxtx, R.id.ll_qchc, R.id.ll_bbsj, R.id.left_LL })
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
