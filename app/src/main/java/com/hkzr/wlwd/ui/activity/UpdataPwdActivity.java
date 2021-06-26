package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.SignTodayListBean;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.AppManager;
import com.hkzr.wlwd.ui.utils.SPUtil;
import com.hkzr.wlwd.ui.utils.TimeCount;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 修改密码
 */
public class UpdataPwdActivity extends BaseActivity implements View.OnClickListener {
    ImageView ivLeft;
    TextView tvLeft;
    LinearLayout leftLL;
    TextView tvTitle;
    ImageView ivRight;
    TextView tvRight;
    LinearLayout rightLL;
    RelativeLayout rlTitle;
    EditText etSjh;
    EditText etMm;
    TextView tvYzm;
    EditText etXmm;
    EditText etQxmm;
    TextView tvQr;

    private void initViewBind() {
        ivLeft = findViewById(R.id.iv_left);
        tvLeft = findViewById(R.id.tv_left);
        leftLL = findViewById(R.id.left_LL);
        tvTitle = findViewById(R.id.tv_title);
        ivRight = findViewById(R.id.iv_right);
        tvRight = findViewById(R.id.tv_right);
        rightLL = findViewById(R.id.right_LL);
        rlTitle = findViewById(R.id.rl_title);
        etSjh = findViewById(R.id.et_sjh);
        etMm = findViewById(R.id.et_mm);
        tvYzm = findViewById(R.id.tv_yzm);
        etXmm = findViewById(R.id.et_xmm);
        etQxmm = findViewById(R.id.et_qxmm);
        tvQr = findViewById(R.id.tv_qr);
        findViewById(R.id.left_LL).setOnClickListener(this);
        findViewById(R.id.tv_yzm).setOnClickListener(this);
        findViewById(R.id.tv_qr).setOnClickListener(this);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_updatapw);
        initViewBind();
        //初始化需要初始化的控件、赋值等操作
        initViewDatas();
    }

    String mobile = "";

    private void initViewDatas() {
        mobile = mUserInfoCache.getUser().getMobilePhone();
        if (TextUtils.isEmpty(mobile)) {
            etSjh.setText(getResources().getString(R.string.bdsjh) + "未绑定手机号");
            tvQr.setEnabled(false);
            tvYzm.setEnabled(false);
        } else {
            etSjh.setText(getResources().getString(R.string.bdsjh) + mobile);
            tvQr.setEnabled(true);
            tvYzm.setEnabled(true);
        }
        etSjh.setEnabled(false);
        tvTitle.setText(R.string.xgmm);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                finish();
                break;
            case R.id.tv_yzm:
                getCode();
                break;
            case R.id.tv_qr:
                updata();
                break;
        }

    }


    TimeCount timeCount;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount = null;
    }

    private void getCode() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sms:pw_modify");
        VolleyFactory.instance().post(this, mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
            @Override
            public void requestSucceed(String object) {
                timeCount = new TimeCount(60000, 1000, tvYzm);
                timeCount.start();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    private void updata() {
        String yzm = etMm.getText().toString().trim();
        String Xmm = etXmm.getText().toString().trim();
        String Qxmm = etQxmm.getText().toString().trim();
        if (TextUtils.isEmpty(yzm)) {
            toast("请输入验证码");
        } else if (TextUtils.isEmpty(Xmm)) {
            toast("请输入新密码");
        } else if (TextUtils.isEmpty(Qxmm)) {
            toast("请输入确认密码");
        } else if (!Xmm.equals(Qxmm)) {
            toast("两次密码输入不一致");
        } else {
            Map<String, String> mParams = new HashMap<String, String>();
            mParams.put("t", "changepass");
            mParams.put("newpass", Xmm);
            mParams.put("vcode", yzm);
            VolleyFactory.instance().post(this, mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
                @Override
                public void requestSucceed(String object) {
                    toast("密码修改成功,请重新登录!");
                    SPUtil.write(UpdataPwdActivity.this, "app", "login", "");
                    jumpTo(LoginActivity.class);
                    JPushInterface.setAlias(UpdataPwdActivity.this, "", null);
                    AppManager.getAppManager().finishOthersActivity(LoginActivity.class);
                    finish();
                }

                @Override
                public void requestFailed() {
//                ToastUtil.t("接口请求失败");
                }
            }, true, false);
        }
    }
}
