package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.FriendsEntity;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 好友申请
 */

public class FriendRequestActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout leftLL;
    TextView tvTitle;
    EditText edInfo;
    TextView tvSend;
    String userid;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_friendrequest);
        initViewBind();
        tvTitle.setText("好友申请");
        userid = getIntent().getStringExtra("userid");
    }

    private void initViewBind() {
        leftLL = findViewById(R.id.left_LL);
        tvTitle = findViewById(R.id.tv_title);
        edInfo =findViewById (R.id.ed_info);
        tvSend =findViewById (R.id.tv_send);
        findViewById(R.id.left_LL).setOnClickListener(this);
        findViewById(R.id.tv_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.tv_send:
                String ed = edInfo.getText().toString();
                addFriends(ed);
                break;
        }
    }


    private void addFriends(String ed) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "friendadd");
        mParams.put("friendid", userid);
        mParams.put("hellotxt", ed);

        VolleyFactory.instance().post(FriendRequestActivity.this, mParams, FriendsEntity.class, new VolleyFactory.BaseRequest<FriendsEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                toast("申请发送成功!");
                FriendRequestActivity.this.finish();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }
}
