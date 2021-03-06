package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.hkzr.wlwd.ui.fragment.AttendanceSettingFragment;
import com.hkzr.wlwd.ui.fragment.AttendanceSignFragment;
import com.hkzr.wlwd.ui.fragment.WebFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 考勤
 */

public class CheckWorkAttendanceActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView iv_right;
    @Bind(R.id.right_LL)
    LinearLayout right_LL;
    @Bind(R.id.btn_check)
    Button btnCheck;
    @Bind(R.id.btn_statistics)
    Button btnStatistics;
    @Bind(R.id.btn_setting)
    Button btnSetting;
    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private FragmentManager fm;
    private FragmentTransaction ft;
    /**
     * bottom tab button
     */
    private Button[] btnTabs;
    /**
     * index of btnTabs
     */
    private int currentTabIndex;
    public static int index = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_check_work);
        tvTitle.setText("考勤签到");
        iv_right.setImageResource(R.drawable.help);
        fm = getSupportFragmentManager();
        btnTabs = new Button[3];
        btnTabs[0] = btnCheck;
        btnTabs[1] = btnStatistics;
        btnTabs[2] = btnSetting;
        btnTabs[0].setSelected(true);
        btnSetting.setVisibility(View.GONE);
        iniTab();
    }

    public void changeSetting(boolean change) {
        if (change) {
            btnSetting.setVisibility(View.VISIBLE);
        } else {
            btnSetting.setVisibility(View.GONE);
        }
    }

//    @Override
//    public void onAttachFragment(Fragment fragment) {
//        if (attendanceSignFragment == null && fragment instanceof AttendanceSignFragment)
//            attendanceSignFragment = (AttendanceSignFragment) fragment;
//        if (webFragment == null && fragment instanceof WebFragment)
//            webFragment = (WebFragment) fragment;
//        if (attendanceSettingFragment == null && fragment instanceof AttendanceSettingFragment)
//            attendanceSettingFragment = (AttendanceSettingFragment) fragment;
//    }

    AttendanceSignFragment attendanceSignFragment;
    WebFragment webFragment;
    AttendanceSettingFragment attendanceSettingFragment;
    private Fragment[] fragments;

    private void iniTab() {
        attendanceSignFragment = new AttendanceSignFragment();
        webFragment = WebFragment.newInstance(1);
        attendanceSettingFragment = new AttendanceSettingFragment();
        fragments = new Fragment[]{attendanceSignFragment, webFragment, attendanceSettingFragment};
        ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, attendanceSignFragment);
        ft.show(attendanceSignFragment).commit();
    }

    @OnClick({R.id.left_LL, R.id.btn_check, R.id.btn_statistics, R.id.btn_setting, R.id.right_LL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                if (index == 1) {
                    if (webFragment != null && webFragment.isVisible() && webFragment.isBack()) {
                        webFragment.back();
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.btn_check:
                index = 0;
                SwitchSkip();
                break;
            case R.id.btn_statistics:
                index = 1;
                webFragment.refresh();
                SwitchSkip();
                break;
            case R.id.btn_setting:
                index = 2;
                SwitchSkip();
                break;
            case R.id.right_LL:
                Intent intent = new Intent(this, SDK_WebView.class);
                intent.putExtra("title", "考勤帮助");
                intent.putExtra("url", App.getInstance().getH5Url() + ReqUrl.SysHelp_Sign + UserInfoCache.init().getTokenid());
                startActivity(intent);
                break;
        }
    }

    private void SwitchSkip() {
        ft = fm.beginTransaction();
        if (currentTabIndex != index) {
            if (!fragments[index].isAdded()) {
                ft.add(R.id.fragment_container, fragments[index]);
            }
            ft.hide(fragments[currentTabIndex]).show(fragments[index]).commit();
            btnTabs[currentTabIndex].setSelected(false);
            btnTabs[index].setSelected(true);
            currentTabIndex = index;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (index == 1) {
            if (webFragment != null && webFragment.isVisible() && webFragment.isBack()) {
                webFragment.back();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }
}
