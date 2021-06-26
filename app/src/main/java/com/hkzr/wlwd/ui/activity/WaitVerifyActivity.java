package com.hkzr.wlwd.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.VerifierEntity;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.view.MyListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 添加好友
 */

public class WaitVerifyActivity extends BaseActivity implements View.OnClickListener {
    TextView tv_right;
    LinearLayout leftLL;
    TextView tvTitle;
    EditText tvSearch;
    MyListView lvFriends;
    LinearLayout ll_ed;

    List<VerifierEntity> list;
    List<VerifierEntity> searchList;

    OpenAdapter mMyAdapter;

    String mGroupId = null;

    private void initViewBind() {
        tv_right = findViewById(R.id.tv_right);
        leftLL = findViewById(R.id.left_LL);
        tvTitle = findViewById(R.id.tv_title);
        tvSearch = findViewById(R.id.tv_search);
        lvFriends = findViewById(R.id.lv_friends);
        ll_ed = findViewById(R.id.ll_ed);
        findViewById(R.id.tv_search).setOnClickListener(this);
        findViewById(R.id.left_LL).setOnClickListener(this);
        findViewById(R.id.right_LL).setOnClickListener(this);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_wait_verify);
        initViewBind();
        mGroupId = getIntent().getStringExtra("groupId");

        ll_ed.setFocusable(true);
        ll_ed.setFocusableInTouchMode(true);
        tvTitle.setText("加群通知");
        list = new ArrayList<>();
        mMyAdapter = new OpenAdapter(list) {
            @Override
            public IHolder createHolder(int position) {
                return new FriendHolder();
            }
        };
        lvFriends.setAdapter(mMyAdapter);
        initData();

        initListener();
    }

    private void initListener() {
        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });
    }

    private void initData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "chat:group_msg");
        mParams.put("groupid", mGroupId);
        VolleyFactory.instance().post(WaitVerifyActivity.this, mParams, VerifierEntity.class, new VolleyFactory.BaseRequest<VerifierEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                //list = JSONArray.parseArray(object, VerifierEntity.class);
                list = VerifierEntity.parseArray(object);
                if (list == null) {
                    list = new ArrayList<VerifierEntity>();
                }
                mMyAdapter.setModels(list);
                mMyAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailed() {
            }
        }, true, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.right_LL:
                break;
        }
    }

    class FriendHolder implements IHolder<VerifierEntity> {
        TextView tv_name, tv_des, tv_yes, tv_end;
        ImageView iv_icon;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_friend, parent, false);
            tv_name = (TextView) convertview.findViewById(R.id.tv_name);
            tv_des = (TextView) convertview.findViewById(R.id.tv_des);
            tv_yes = (TextView) convertview.findViewById(R.id.tv_yes);
            tv_end = (TextView) convertview.findViewById(R.id.tv_end);
            iv_icon = (ImageView) convertview.findViewById(R.id.iv_icon);
            return convertview;
        }

        @Override
        public void bindModel(int position, final VerifierEntity bean) {
            tv_name.setText("" + bean.getUserCn());
            tv_des.setText("申请加入" + bean.getGroupName());

            if (!TextUtils.isEmpty(bean.getPhotoUrl())) {
                Picasso.with(WaitVerifyActivity.this).load(bean.getPhotoUrl()).into(iv_icon);
            }

            String result = bean.getResult();
            if (!TextUtils.isEmpty(result)) {
                if (result.equals("1")) {
                    tv_end.setText("同意");
                    tv_end.setVisibility(View.VISIBLE);
                    tv_yes.setVisibility(View.GONE);
                } else if (result.equals("2")) {
                    tv_end.setText("拒绝");
                    tv_end.setVisibility(View.VISIBLE);
                    tv_yes.setVisibility(View.GONE);
                }
            } else {
                tv_end.setVisibility(View.GONE);
                tv_yes.setVisibility(View.VISIBLE);
                tv_yes.setText("待验证");
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WaitVerifyActivity.this);
                        View v = View.inflate(WaitVerifyActivity.this, R.layout.dialog_wait_verify, null);
                        builder.setView(v);
                        final AlertDialog dialog = builder.create();
                        v.findViewById(R.id.tv_deny).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                agree(bean.get_AutoID(), false);
                                dialog.dismiss();
                            }
                        });
                        v.findViewById(R.id.tv_agree).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                agree(bean.get_AutoID(), true);
                                dialog.dismiss();
                            }
                        });
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.show();
                    }
                });
            }
        }
    }

    private void agree(String autoId, boolean agree) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "chat:group_join_conf");
        mParams.put("autoid", autoId);
        mParams.put("keyid", autoId);
        mParams.put("result", agree ? "1" : "2");
        VolleyFactory.instance().post(WaitVerifyActivity.this, mParams, VerifierEntity.class, new VolleyFactory.BaseRequest<VerifierEntity>() {
            @Override
            public void requestSucceed(String object) {
                initData();
            }

            @Override
            public void requestFailed() {
            }
        }, false, false);
    }

    public void search(String name) {
        if (!TextUtils.isEmpty(name)) {
            searchList = new ArrayList<>();
            for (VerifierEntity entity : list) {
                if (!TextUtils.isEmpty(entity.getUserCn()) && entity.getUserCn().contains(name)) {
                    searchList.add(entity);
                }
            }
            mMyAdapter.setModels(searchList);
            mMyAdapter.notifyDataSetChanged();
        } else {
            mMyAdapter.setModels(list);
            mMyAdapter.notifyDataSetChanged();
        }

    }
}
