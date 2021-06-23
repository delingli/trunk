package com.hkzr.wlwd.ui.activity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.SignTodayListBean;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 添加wifi
 */

public class WiFiListActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_wifi)
    ListView lvWifi;
    List<ScanResult> list;
    OpenAdapter mMyAdapter;
    int mCurrentPosition = -1;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);
        tvTitle.setText("添加办公Wi-Fi");
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        list = wifiManager.getScanResults();
        if (list == null) {
            list = new ArrayList<>();
        } else {
            List<String> s = getIntent().getStringArrayListExtra("list");
            if (s != null) {
                for (int j = 0; j < s.size(); j++) {
                    for (int i = 0; i < s.size(); i++) {
                        if (list.get(i).BSSID.equals(s.get(j))) {
                            list.remove(i);
                        }
                    }
                }
            }
        }
        mMyAdapter = new OpenAdapter(list) {
            @Override
            public IHolder createHolder(int position) {
                return new WifiHolder();
            }
        };
        lvWifi.setAdapter(mMyAdapter);
        lvWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentPosition != position) {
                    mCurrentPosition = position;
                } else {
                    mCurrentPosition = -1;
                }
                mMyAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.left_LL, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.tv_submit:
                if (mCurrentPosition == -1) {
                    toast("请选择添加的Wi-Fi");
                } else {
                    toAdd();
                }
                break;
        }
    }

    private void toAdd() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:wifi_add");
        mParams.put("unitid", getIntent().getStringExtra("unitid"));
        mParams.put("wfid", list.get(mCurrentPosition).BSSID);
        mParams.put("wfname", list.get(mCurrentPosition).SSID);
        VolleyFactory.instance().post(this, mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
            @Override
            public void requestSucceed(String object) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    class WifiHolder implements IHolder<ScanResult> {
        TextView tv_name, tv_mac;
        CheckBox cb;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_wifi, parent, false);
            tv_name = (TextView) convertview.findViewById(R.id.tv_name);
            tv_mac = (TextView) convertview.findViewById(R.id.tv_mac);
            cb = (CheckBox) convertview.findViewById(R.id.cb);
            return convertview;
        }

        @Override
        public void bindModel(int position, final ScanResult bean) {
            tv_name.setText(bean.SSID);
            tv_mac.setText(bean.BSSID);
            if (mCurrentPosition == position) {
                cb.setChecked(true);
            } else {
                cb.setChecked(false);
            }
        }
    }
}
