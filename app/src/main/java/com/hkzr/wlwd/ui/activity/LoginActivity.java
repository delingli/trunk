package com.hkzr.wlwd.ui.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.ReqUrl;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.httpUtils.VolleyFactory.BaseRequest;
import com.hkzr.wlwd.model.LoginEntity;
import com.hkzr.wlwd.model.LoginUserDao;
import com.hkzr.wlwd.model.ServiceEntity;
import com.hkzr.wlwd.ui.MainActivity;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.app.User;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.DateUtils;
import com.hkzr.wlwd.ui.utils.DisplayUtil;
import com.hkzr.wlwd.ui.utils.ExampleUtil;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.utils.SPUtil;
import com.hkzr.wlwd.ui.utils.StatusBarUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements PopupWindow.OnDismissListener, View.OnClickListener {
    EditText etZh;
    EditText etMm;
    EditText etBh;
    TextView tvDl;
    CheckBox cbJzmm;
    //    @Bind(R.id.tv_wjmm)
//    TextView tvWjmm;
    ImageView iv_username;
    LinearLayout ll_zh;
    LinearLayout l_username;
    CheckBox cb_zddl;//自动登录
    LinearLayout l_eye;
    ImageView iv_eye;


    boolean isShow = false;
    private ProgressDialog progressDialog;
    boolean isShowPwd = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        StatusBarUtil.setTran(this);
        initViewBind();
        try {
            initData();
            getPersimmions();
            initViewDatas();
            initPopuWindow();
            String login = SPUtil.readString(LoginActivity.this, "app", "login");
            if (loginUserDaos.size() > 0) {
                if (!TextUtils.isEmpty(login)) { //自动登录
                    tvDl.performClick();
                    cb_zddl.setChecked(true);
                } else {//不自动登录
                    cb_zddl.setChecked(false);
                }
            }
        } catch (Exception e) {

        }
    }

    private void initViewBind() {
        etZh = findViewById(R.id.et_zh);
        etMm = findViewById(R.id.et_mm);
        etBh = findViewById(R.id.et_bh);
        tvDl = findViewById(R.id.tv_dl);
        cbJzmm = findViewById(R.id.cb_jzmm);
        //    @Bind(R.id.tv_wjmm)
//    TextView tvWjmm;
        iv_username = findViewById(R.id.iv_username);
        ll_zh = findViewById(R.id.ll_zh);
        l_username = findViewById(R.id.l_username);
        cb_zddl = findViewById(R.id.cb_zddl);
        l_eye = findViewById(R.id.l_eye);
        iv_eye = findViewById(R.id.iv_eye);
        findViewById(R.id.tv_dl).setOnClickListener(this);
        findViewById(R.id.cb_jzmm).setOnClickListener(this);
        findViewById(R.id.iv_username).setOnClickListener(this);
        findViewById(R.id.l_username).setOnClickListener(this);
        findViewById(R.id.l_eye).setOnClickListener(this);
    }

    OpenAdapter mMyAdapter;
    List<LoginUserDao> loginUserDaos;
    LoginUserDao currentUser;

    private void initPopuWindow() {
        View view = View.inflate(this, R.layout.popu_listview, null);
        ListView mPopListView = (ListView) view.findViewById(R.id.listview);
        if (loginUserDaos.size() > 0) {
            mMyAdapter = new OpenAdapter(loginUserDaos) {
                @Override
                public IHolder createHolder(int position) {
                    return new UserHolder();
                }
            };
            mPopListView.setAdapter(mMyAdapter);
            mPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentUser = loginUserDaos.get(position);
                    etZh.setText(currentUser.getUsername());
                    if (!TextUtils.isEmpty(currentUser.getPassword())) {
                        etMm.setText(currentUser.getPassword());
                    } else {
                        etMm.setText("");
                    }
                    etBh.setText(currentUser.getServiceCode());
                    isShow = false;
                    mPopupWindow.dismiss();
                }
            });
        }
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth() - DisplayUtil.dip2px(this, 60);//屏幕款一半
        mPopupWindow = new PopupWindow(view, width, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE)); // 设置popWindow背景颜色
        mPopupWindow.setFocusable(true); // 让popWindow获取焦点
        mPopupWindow.setOnDismissListener(this);
    }


    private void initData() {
        loginUserDaos = DataSupport.where("1==1 ORDER BY createTime DESC").find(LoginUserDao.class);
        if (loginUserDaos.size() > 0) {
            currentUser = loginUserDaos.get(0);
            etZh.setText(currentUser.getUsername());
            if (!TextUtils.isEmpty(currentUser.getPassword())) {
                etMm.setText(currentUser.getPassword());
            } else {
                etMm.setText("");
            }
            etBh.setText(currentUser.getServiceCode());
        }


    }

    PopupWindow mPopupWindow;

    private void initViewDatas() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录...");
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dl:
                checkLoginBf();
                break;
            case R.id.cb_jzmm:
                break;
//            case R.id.tv_wjmm:
//                //忘记密码
//                jumpTo(ForgetPwdActivity.class);
//                break;
            case R.id.l_username:
            case R.id.iv_username:
                isShow = !isShow;
                if (isShow) {
                    iv_username.setImageResource(R.drawable.edit_up);
                    mPopupWindow.showAsDropDown(etZh, 0, 10);
                } else {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.l_eye:
                isShowPwd = !isShowPwd;
                if (isShowPwd) {
                    //选择状态 显示明文--设置为可见的密码
                    //mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    /**
                     * 第二种
                     */
                    iv_eye.setImageResource(R.drawable.open);
                    etMm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    //mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    /**
                     * 第二种
                     */
                    iv_eye.setImageResource(R.drawable.close_eye);
                    etMm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    /**
     * 登录前数据检查
     */
    private void checkLoginBf() {
        String zhStr = etZh.getText().toString();
        String mmStr = etMm.getText().toString();
        String fwbhStr = etBh.getText().toString();
        etBh.setSelected(true);
        if (TextUtils.isEmpty(zhStr)) {
            ToastUtil.t(R.string.q_zh);
        } else if (TextUtils.isEmpty(mmStr)) {
            ToastUtil.t(R.string.q_mm);
        } else if (TextUtils.isEmpty(fwbhStr)) {
            ToastUtil.t(R.string.q_fwbh);
        } else {
            progressDialog.show();
            checkService(zhStr, mmStr, fwbhStr);
        }

    }

    String fwbhStr, targetUrl, CustName;

    /**
     * 检查服务编号
     * http://yun.5lsoft.com/service/mobile.ashx?t=1&code=5LSOFT&mtype=1&User=apptest
     * zhStr账号 mmStr密码   fwbhStr 服务号
     */
    private void checkService(final String zhStr, final String mmStr, final String fwbhStr) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "1");
        mParams.put("code", fwbhStr);
        mParams.put("mtype", "1");
        mParams.put("user", zhStr);
        mParams.put("ver", App.getInstance().getVersionName());
        VolleyFactory.instance().FirstPost(LoginActivity.this, ReqUrl.FirstRot + ReqUrl.yunservice, mParams, ServiceEntity.class, new BaseRequest<ServiceEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                ServiceEntity entity = JSON.parseObject(object.toString(), ServiceEntity.class);
                LoginActivity.this.fwbhStr = fwbhStr;
                targetUrl = entity.getTargetUrl();
                CustName = entity.getCustName();
                App.getInstance().setRootUrl(targetUrl);
                App.getInstance().setH5Url(entity.getAppRoot());
                App.getInstance().setDownloadUrl(entity.getDownUrl());
                App.getInstance().setVersionCode(entity.getVersion());
                toLogin(zhStr, mmStr, fwbhStr);
            }

            @Override
            public void requestFailed() {
                if (currentUser != null) {
                    App.getInstance().setRootUrl(currentUser.getUrl());
                    toLogin(zhStr, mmStr, fwbhStr);
                } else {
                    ToastUtil.t("网络请求失败");
                    progressDialog.dismiss();
                }
            }
        }, false, false);
    }

    /**
     * 登录
     */
    private void toLogin(final String zh, final String mm, final String bh) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "login");
        mParams.put("session", 1 + "");
        mParams.put("mtype", "1");
        mParams.put("pass", mm);
        mParams.put("user", zh);
        mParams.put("uuid", ExampleUtil.getIMEI(LoginActivity.this));
        mParams.put("ver", App.getInstance().getVersionName());
        VolleyFactory.instance().post(LoginActivity.this, mParams, ServiceEntity.class, new BaseRequest<ServiceEntity>() {

            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());

                LoginEntity entity = JSON.parseObject(object.toString(), LoginEntity.class);
                mUserInfoCache.setTokenid(entity.getTokenid());
                mUserInfoCache.setKey(entity.getKey());
                mUserInfoCache.setUserid(entity.getUserid());
                mUserInfoCache.setBhStr(fwbhStr);
                mUserInfoCache.setOpenId(entity.getOpenId());
                jpushLogin(entity.getOpenId());
                if (DataSupport.where("username = ?  AND serviceCode = ?", zh, bh).find(LoginUserDao.class).size() > 0) {
                    ContentValues values = new ContentValues();
                    if (cbJzmm.isChecked()) {
                        values.put("password", mm);
                    } else {
                        values.put("password", "");
                    }
                    values.put("createTime", DateUtils.getCurTime("yyyy-MM-dd HH:mm:ss"));
                    DataSupport.updateAll(LoginUserDao.class, values, "username = ? ", zh);
                } else {
                    LoginUserDao loginUserDao = new LoginUserDao();
                    loginUserDao.setUsername(zh);
                    loginUserDao.setCreateTime(DateUtils.getCurTime("yyyy-MM-dd HH:mm:ss"));
                    loginUserDao.setServiceCode(bh);
                    loginUserDao.setCompanyName(CustName);
                    loginUserDao.setUrl(targetUrl);
                    if (cbJzmm.isChecked()) {
                        loginUserDao.setPassword(mm);
                    }
                    loginUserDao.save();
                }
                if (cb_zddl.isChecked()) {
                    SPUtil.write(LoginActivity.this, "app", "login", "1");
                } else {
                    SPUtil.write(LoginActivity.this, "app", "login", "");
                }
                getPersonInfo();
            }

            @Override
            public void requestFailed() {
                progressDialog.dismiss();
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }


    /**
     * 获取个人信息   方便及时刷新
     */
    private void getPersonInfo() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "userinfo");
        mParams.put("userid", mUserInfoCache.getUserid());
        mParams.put("TokenId", mUserInfoCache.getTokenid());
        VolleyFactory.instance().post(LoginActivity.this, mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                User entity = JSON.parseObject(object.toString(), User.class);
                mUserInfoCache.setUser(entity);
                progressDialog.dismiss();
                jumpTo(MainActivity.class);
                finish();
            }

            @Override
            public void requestFailed() {
                progressDialog.dismiss();
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }

    // jpush注册alias
    public void jpushLogin(String usercode) {
        JPushInterface.setAlias(this, 111, usercode);
    }

    @Override
    public void onDismiss() {
        iv_username.setImageResource(R.drawable.edit_down);
        isShow = false;
    }

    class UserHolder implements IHolder<LoginUserDao> {
        TextView tv_username;
        ImageView iv_del;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_login_popu, parent, false);
            tv_username = (TextView) convertview.findViewById(R.id.tv_username);
            iv_del = (ImageView) convertview.findViewById(R.id.iv_del);
            return convertview;
        }

        @Override
        public void bindModel(int position, final LoginUserDao bean) {
            tv_username.setText(bean.getUsername() + "(" + bean.getServiceCode() + ")");
            iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.isSaved()) {
                        isShow = false;
                        mPopupWindow.dismiss();
                        bean.delete();
                        loginUserDaos.remove(bean);
                    }
                }
            });
        }
    }
}
