package com.hkzr.wlwd.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.SignInfoBean;
import com.hkzr.wlwd.model.SignTodayListBean;
import com.hkzr.wlwd.ui.adapter.SignSettingLocationAdapter;
import com.hkzr.wlwd.ui.adapter.SignSettingWiFiAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 考勤组设置
 */

public class SignSettingActivity extends BaseActivity {
    public static final int REQUESTCODE = 7023;

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_wifi)
    MyListView lvWifi;
    @Bind(R.id.tv_add_wifi)
    TextView tvAddWifi;
    @Bind(R.id.lv_address)
    MyListView lvAddress;
    @Bind(R.id.tv_add_address)
    TextView tvAddAddress;
    @Bind(R.id.tv_group_name)
    TextView tv_group_name;


    String UnitID, name;
    SignInfoBean signInfoBean;
    SignSettingLocationAdapter signSettingLocationAdapter;
    SignSettingWiFiAdapter signSettingWiFiAdapter;
    String mCurrentWifiMac;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.acitivty_sign_setting);
        tvTitle.setText("考勤组设置");
        UnitID = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)) {
            tv_group_name.setText(name);
        }
        initData();
    }

    private void initData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:group_info");
        mParams.put("unitid", UnitID);
        VolleyFactory.instance().post(this, mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
            @Override
            public void requestSucceed(String object) {
                if (!TextUtils.isEmpty(object)) {
                    signInfoBean = JSONObject.parseObject(object.toString(), SignInfoBean.class);
                    if (signInfoBean.getList_loc() != null) {
                        signSettingLocationAdapter = new SignSettingLocationAdapter(SignSettingActivity.this, signInfoBean.getList_loc());
                        signSettingLocationAdapter.setOnDelListener(new SignSettingLocationAdapter.OnDelListener() {
                            @Override
                            public void onDel(final int position) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(SignSettingActivity.this);
                                dialog.setTitle("提示");
                                dialog.setMessage("是否删除?");
                                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        locDel(signInfoBean.getList_loc().get(position).getAutoID());
                                    }
                                }).setNegativeButton("取消", null);
                                dialog.show();
                            }
                        });
                        lvAddress.setAdapter(signSettingLocationAdapter);
                    }
                    if (signInfoBean.getList_wifi() != null) {
                        getWifiInfo();
                        signSettingWiFiAdapter = new SignSettingWiFiAdapter(SignSettingActivity.this, signInfoBean.getList_wifi(), mCurrentWifiMac);
                        signSettingWiFiAdapter.setOnDelListener(new SignSettingWiFiAdapter.OnDelListener() {
                            @Override
                            public void onDel(final int position) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(SignSettingActivity.this);
                                dialog.setTitle("提示");
                                dialog.setMessage("是否删除?");
                                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        wifiDel(signInfoBean.getList_wifi().get(position).getAutoID());
                                    }
                                }).setNegativeButton("取消", null);
                                dialog.show();
                            }
                        });
                        lvWifi.setAdapter(signSettingWiFiAdapter);
                    }
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }


    private void getWifiInfo() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        mCurrentWifiMac = info.getBSSID();
    }

    @OnClick({R.id.left_LL, R.id.tv_add_wifi, R.id.tv_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.tv_add_wifi:
                Intent intent = new Intent(this, WiFiListActivity.class);
                intent.putExtra("unitid", UnitID);
                ArrayList<String> wifi = new ArrayList<>();
                for (SignInfoBean.WifiInfo wifiInfo : signInfoBean.getList_wifi()) {
                    wifi.add(wifiInfo.getWFID());
                }
                intent.putStringArrayListExtra("list", wifi);
                startActivityForResult(intent, REQUESTCODE);
                break;
            case R.id.tv_add_address:
                Intent intent2 = new Intent(this, MapLocationActivity.class);
                intent2.putExtra("unitid", UnitID);
                intent2.putExtra("type", 1);
                startActivityForResult(intent2, REQUESTCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            initData();
        }
    }

    private void locDel(String AutoID) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:loc_del");
        mParams.put("autoid", AutoID);
        VolleyFactory.instance().post(this, mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
            @Override
            public void requestSucceed(String object) {
                toast("删除成功");
                initData();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    private void wifiDel(String AutoID) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:wifi_del");
        mParams.put("autoid", AutoID);
        VolleyFactory.instance().post(this, mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
            @Override
            public void requestSucceed(String object) {
                toast("删除成功");
                initData();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }
}
