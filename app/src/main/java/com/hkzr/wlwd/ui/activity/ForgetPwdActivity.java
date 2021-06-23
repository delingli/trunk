package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.ReqUrl;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.LoginEntity;
import com.hkzr.wlwd.model.ServiceEntity;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.ExampleUtil;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.utils.TimeCount;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 忘记密码
 */

public class ForgetPwdActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_server)
    EditText etServer;
    @Bind(R.id.et_username)
    EditText etUsername;
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
    TimeCount timeCount;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_forgetpw);
        tvTitle.setText("忘记密码");

    }


    @OnClick({R.id.left_LL, R.id.tv_yzm, R.id.tv_qr})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.tv_yzm:
                String serStr = etServer.getText().toString().trim();
                String uname = etUsername.getText().toString().trim();
                if (TextUtils.isEmpty(serStr)) {
                    toast("请先输入服务号");
                } else if (TextUtils.isEmpty(uname)) {
                    toast("请先输入用户名");
                } else {
                    checkService(serStr, uname);
                }
                break;
            case R.id.tv_qr:
                String serStr1 = etServer.getText().toString().trim();
                String uname1 = etUsername.getText().toString().trim();
                String yzm = etMm.getText().toString().trim();
                String Xmm = etXmm.getText().toString().trim();
                String Qxmm = etQxmm.getText().toString().trim();
                if (TextUtils.isEmpty(targetUrl)) {
                    toast("请先获取验证码");
                } else if (TextUtils.isEmpty(serStr1)) {
                    toast("请输入服务号");
                } else if (TextUtils.isEmpty(uname1)) {
                    toast("请输入用户名");
                } else if (TextUtils.isEmpty(yzm)) {
                    toast("请输入验证码");
                } else if (TextUtils.isEmpty(Xmm)) {
                    toast("请输入新密码");
                } else if (TextUtils.isEmpty(Qxmm)) {
                    toast("请输入确认密码");
                } else if (!Xmm.equals(Qxmm)) {
                    toast("两次密码不一致,请重新输入");
                } else {
                    updataPwd(uname1, yzm, Xmm);
                }
                break;
        }
    }

    private void updataPwd(String uname1, String yzm, String xmm) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "forgetpass");
        mParams.put("username", uname1);
        mParams.put("vcode", yzm);
        mParams.put("newpass", xmm);
        mParams.put("uuid", ExampleUtil.getIMEI(ForgetPwdActivity.this ));
        VolleyFactory.instance().FirstPost(ForgetPwdActivity.this, targetUrl, mParams, ServiceEntity.class, new VolleyFactory.BaseRequest<ServiceEntity>() {

            @Override
            public void requestSucceed(String object) {
                toast("密码修改成功");
                finish();
            }

            @Override
            public void requestFailed() {
            }
        }, true, false);
    }

    String targetUrl;

    /**
     * 检查服务编号
     * http://yun.5lsoft.com/service/mobile.ashx?t=1&code=5LSOFT&mtype=1&User=apptest
     * zhStr账号   fwbhStr 服务号
     */
    private void checkService(final String fwbhStr, final String zhStr) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "1");
        mParams.put("code", fwbhStr);
        mParams.put("mtype", "1");
        mParams.put("user", zhStr);
        mParams.put("ver", App.getInstance().getVersionName());
        VolleyFactory.instance().FirstPost(ForgetPwdActivity.this, ReqUrl.FirstRot + ReqUrl.yunservice, mParams, ServiceEntity.class, new VolleyFactory.BaseRequest<ServiceEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                ServiceEntity entity = JSON.parseObject(object.toString(), ServiceEntity.class);
                targetUrl = entity.getTargetUrl();
                sendMsg(zhStr);
            }

            @Override
            public void requestFailed() {
            }
        }, false, false);
    }


    private void sendMsg(String zhStr) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sms:pw_forget");
        mParams.put("username", zhStr);
        mParams.put("uuid", ExampleUtil.getIMEI(ForgetPwdActivity.this ));

        VolleyFactory.instance().FirstPost(ForgetPwdActivity.this, targetUrl, mParams, LoginEntity.class, new VolleyFactory.BaseRequest<LoginEntity>() {
            @Override
            public void requestSucceed(String string) {
                timeCount = new TimeCount(60000, 1000, tvYzm);
                timeCount.start();
            }

            @Override
            public void requestFailed() {

            }
        }, false, false);


    }

}
