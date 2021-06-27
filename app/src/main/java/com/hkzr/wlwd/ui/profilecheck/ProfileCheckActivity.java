package com.hkzr.wlwd.ui.profilecheck;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.LogUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.zxing.android.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileCheckActivity extends BaseActivity {

    private TextView tv_title;
    private RecyclerView mRecyclerView;
    private RecycleAdaptersz mRecycleAdaptersz;

    private void initViewBind() {

        mRecyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("检查项目");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mRecycleAdaptersz = new RecycleAdaptersz(this, null));
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_profilecheck);
        initViewBind();
        toRequestCheckData();
    }

    class CheckItemBean {
        public String ItemName;
        public String Stand;
        public String ChkMethod;
        public String ID;
    }

    public void toRequestCheckData() {
//        {"action":"chk_scan_product","tokenId":"","data":""}
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("action", "chk_chkitems");
        mParams.put("tokenId","");
        mParams.put("data", "");

        org.json.JSONObject object = new org.json.JSONObject(mParams);
        String ss=object.toString();
        LogUtil.d("ABC", object.toString());
        VolleyFactory.instance().xcbfPost(ProfileCheckActivity.this, object, new VolleyFactory.AbsBaseRequest() {
            @Override
            public void requestFailed(String msg) {
                if (null != msg) {
                    ToastUtil.show(ProfileCheckActivity.this, msg);
                }
            }

            @Override
            public void requestSucceed(String str) {
                LogUtil.d("ldlPP", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String Message = jsonObject.optString("Message");

                    if (jsonObject.optBoolean("Success")) {
                        JSONArray returnData = jsonObject.optJSONArray("ReturnData");
                        List<CheckItemBean> ll = new ArrayList<>();
                        for (int i = 0; i < returnData.length(); ++i) {
                            CheckItemBean r = new CheckItemBean();
                            JSONObject jsonObject1 = returnData.getJSONObject(i);
                            r.ChkMethod = jsonObject1.optString("ChkMethod");
                            r.ID = jsonObject1.optString("ID");
                            r.ItemName = jsonObject1.optString("ItemName");
                            r.Stand = jsonObject1.optString("Stand");
                            ll.add(r);
                        }
                        mRecycleAdaptersz.addList(ll);

                    } else {
                        if (!TextUtils.isEmpty(Message)) {
                            ToastUtil.show(ProfileCheckActivity.this, Message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
