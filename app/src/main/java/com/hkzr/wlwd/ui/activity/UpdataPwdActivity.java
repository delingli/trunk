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

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 修改密码
 */
public class UpdataPwdActivity extends BaseActivity {
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
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.et_sjh)
    EditText etSjh;
    @Bind(R.id.et_mm)
    EditText etMm;
    @Bind(R.id.tv_yzm)
    TextView tvYzm;
    @Bind(R.id.et_xmm)
    EditText etXmm;
    @Bind(R.id.et_qxmm)
    EditText etQxmm;
    @Bind(R.id.tv_qr)
    TextView tvQr;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_updatapw);
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


    @OnClick({R.id.left_LL, R.id.tv_yzm, R.id.tv_qr})
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
