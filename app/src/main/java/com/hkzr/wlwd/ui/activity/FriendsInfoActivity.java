package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.CalendarTypeBean;
import com.hkzr.wlwd.model.FriendsCalendarBean;
import com.hkzr.wlwd.model.FriendsEntity;
import com.hkzr.wlwd.ui.MainActivity;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.widget.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


/**
 * 好友信息
 */

public class FriendsInfoActivity extends BaseActivity implements View.OnClickListener {
    TextView tv_title;
    ImageView iv_right;
    XCRoundRectImageView iv_icon;
    TextView tv_name;
    TextView tv_tel;
    TextView tv_email;
    TextView tv_weixin;
    TextView tv_qq;
    TextView tv_officephone;
    TextView tv_company;
    TextView tv_deptname;
    TextView tv_posname;
    //    @Bind(R.id.tv_add)
//    TextView tv_add;
//    @Bind(R.id.tv_send_message)
//    TextView tv_send_message;
//    @Bind(R.id.tv_del)
//    TextView tv_del;
    LinearLayout ll_mobile;
    View view_mobil;
    String userid;
    String openId;

    int IsMyContact = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_friends_info);
        initViewBind();
        tv_title.setText("好友详情");
        iv_right.setImageResource(R.drawable.shoucang);
        userid = getIntent().getStringExtra("userid");
        openId = getIntent().getStringExtra("openId");
        if (!TextUtils.isEmpty(userid) || !TextUtils.isEmpty(openId))
            initData();
    }

    private void initViewBind() {
        tv_title = findViewById(R.id.tv_title);
        iv_right = findViewById(R.id.iv_right);
        iv_icon = findViewById(R.id.iv_icon);
        tv_name = findViewById(R.id.tv_name);
        tv_tel = findViewById(R.id.tv_tel);
        tv_email = findViewById(R.id.tv_email);
        tv_weixin = findViewById(R.id.tv_weixin);
        tv_qq = findViewById(R.id.tv_qq);
        tv_officephone = findViewById(R.id.tv_officephone);
        tv_company = findViewById(R.id.tv_company);
        tv_deptname = findViewById(R.id.tv_deptname);
        tv_posname = findViewById(R.id.tv_posname);
        ll_mobile = findViewById(R.id.ll_mobile);
        view_mobil = findViewById(R.id.view_mobile);
//    @Bind(R.id.tv_add)
//    TextView tv_add;
//    @Bind(R.id.tv_send_message)
//    TextView tv_send_message;
//    @Bind(R.id.tv_del)
//    TextView tv_del;


        findViewById(R.id.left_LL).setOnClickListener(this);
        findViewById(R.id.right_LL).setOnClickListener(this);
        findViewById(R.id.tv_call).setOnClickListener(this);
        findViewById(R.id.tv_send).setOnClickListener(this);
        findViewById(R.id.tv_send_email).setOnClickListener(this);
        findViewById(R.id.tv_create_richeng).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.right_LL:
                contectFriends();
                break;
            case R.id.tv_call: //打电话
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + friendsEntity.getMobilePhone()));
                startActivity(intent);
                break;
            case R.id.tv_send:  //发短信
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + friendsEntity.getMobilePhone()));
                startActivity(intent);
                break;
            case R.id.tv_send_email: //发邮件
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + friendsEntity.getEMail()));
                startActivity(intent);
                break;
            case R.id.tv_create_richeng:  //创建日程
                initCalendarLimit();
                break;
        }
    }


    private void initCalendarLimit() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "rc:viewlimit");
        mParams.put("userid", friendsEntity.getUserId());
        VolleyFactory.instance().post(FriendsInfoActivity.this, mParams, FriendsEntity.class, new VolleyFactory.BaseRequest<FriendsEntity>() {
            @Override
            public void requestSucceed(String object) {
                FriendsCalendarBean friendsCalendarBean = JSONObject.parseObject(object.toString(), FriendsCalendarBean.class);
                if (friendsCalendarBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("CallBean", new CalendarTypeBean(friendsCalendarBean.getCalID(), friendsCalendarBean.getCalTitle(), ""));
                    jumpTo(OrderCalendarActivity.class, bundle);
                }
            }

            @Override
            public void requestFailed() {
            }
        }, true, false);
    }

    private void delFriends() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "frienddel");
        mParams.put("friendid", friendsEntity.getUserId());
        VolleyFactory.instance().post(FriendsInfoActivity.this, mParams, FriendsEntity.class, new VolleyFactory.BaseRequest<FriendsEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                toast("删除好友成功!");
                Intent intent = new Intent();
                intent.setAction(MainActivity.RECEIVED);
                intent.putExtra("type", "private");
                intent.putExtra("id", friendsEntity.getOpenId());
                sendBroadcast(intent);
                initData();
            }

            @Override
            public void requestFailed() {
            }
        }, false, false);
    }


    private void contectFriends() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "linkedit");
        mParams.put("linkid", friendsEntity.getUserId());
        if (IsMyContact == 1) {
            mParams.put("action", "delete");
        } else {
            mParams.put("action", "add");
        }
        VolleyFactory.instance().post(FriendsInfoActivity.this, mParams, FriendsEntity.class, new VolleyFactory.BaseRequest<FriendsEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                if (IsMyContact == 1) {
                    IsMyContact = 0;
                    iv_right.setImageResource(R.drawable.shoucang);
                } else {
                    IsMyContact = 1;
                    iv_right.setImageResource(R.drawable.collection_check);
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    FriendsEntity friendsEntity;

    private void initData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "userinfo");
        if (!TextUtils.isEmpty(userid)) {
            mParams.put("userid", userid);
        } else {
            mParams.put("openid", openId);
        }
        VolleyFactory.instance().post(FriendsInfoActivity.this, mParams, FriendsEntity.class, new VolleyFactory.BaseRequest<FriendsEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                friendsEntity = JSONObject.parseObject(object, FriendsEntity.class);
                setData();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    private int delType = 0;//删除好友按钮 0   添加还有 1

    private void setData() {
        if (!TextUtils.isEmpty(friendsEntity.getPhotoUrl())) {
            Picasso.with(this).load(friendsEntity.getPhotoUrl()).error(R.drawable.morentouxiang).into(iv_icon);
        }
        userid = friendsEntity.getUserId();
        openId = friendsEntity.getOpenId();
        tv_name.setText(friendsEntity.getUserCn());
        tv_tel.setText(friendsEntity.getMobilePhone());
        tv_email.setText(friendsEntity.getEMail());
        tv_weixin.setText(friendsEntity.getWXNo());
        tv_qq.setText(friendsEntity.getQQNo());
        tv_officephone.setText(friendsEntity.getOfficePhone());
        tv_company.setText(friendsEntity.getCorpName());
        tv_deptname.setText(friendsEntity.getDeptName());
        tv_posname.setText(friendsEntity.getPosName());
        if ("1".equals(friendsEntity.getHideMyInfo())) {
            ll_mobile.setVisibility(View.GONE);
            view_mobil.setVisibility(View.GONE);
        } else {
            ll_mobile.setVisibility(View.VISIBLE);
            view_mobil.setVisibility(View.VISIBLE);
        }

        if (friendsEntity.getIsMyContact() == 1) {
            IsMyContact = 1;
            iv_right.setImageResource(R.drawable.collection_check);
        } else {
            IsMyContact = 0;
            iv_right.setImageResource(R.drawable.shoucang);
        }

    }

}
