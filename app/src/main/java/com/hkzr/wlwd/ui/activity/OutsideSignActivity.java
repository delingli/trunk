package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.SDK_WebView;
import com.hkzr.wlwd.httpUtils.ReqUrl;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.fragment.FieldFragment;
import com.hkzr.wlwd.ui.fragment.WebFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 外勤签到
 */

public class OutsideSignActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView iv_right;
    @Bind(R.id.right_LL)
    LinearLayout right_LL;
    @Bind(R.id.btn_check)
    Button btnCheck;
    @Bind(R.id.btn_footprint)
    Button btnFootprint;
    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;
    private FragmentManager fm;
    private FragmentTransaction ft;
    public static int index = 0;

    /**
     * bottom tab button
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_outside_sign);
        tvTitle.setText("外勤签到");
        iv_right.setImageResource(R.drawable.help);
        fm = getSupportFragmentManager();
        iniTab();
    }

    FieldFragment fieldFragment;
    WebFragment webFragment;

    private void iniTab() {
        fieldFragment = new FieldFragment();
        webFragment = WebFragment.newInstance(2);
        ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, fieldFragment);
        ft.show(fieldFragment).commit();
        btnCheck.setSelected(true);
    }

    @OnClick({R.id.left_LL, R.id.btn_check, R.id.btn_footprint, R.id.right_LL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.btn_check:
                index = 0;
                btnCheck.setSelected(true);
                btnFootprint.setSelected(false);
                ft = fm.beginTransaction();
                if (webFragment.isAdded()) {
                    ft.hide(webFragment);
                }
                ft.show(fieldFragment).commit();
                break;
            case R.id.btn_footprint:
                index = 1;
                btnCheck.setSelected(false);
                btnFootprint.setSelected(true);
                ft = fm.beginTransaction();
                webFragment.refresh();
                if (!webFragment.isAdded()) {
                    ft.add(R.id.fragment_container, webFragment);
                }
                ft.hide(fieldFragment).show(webFragment).commit();
                break;
            case R.id.right_LL:
                Intent intent = new Intent(this, SDK_WebView.class);
                intent.putExtra("title", "签到帮助");
                intent.putExtra("url", App.getInstance().getH5Url() + ReqUrl.SysHelp_qiandao+ UserInfoCache.init().getTokenid());
                startActivity(intent);
                break;
        }
    }
}
