package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.FriendListEntity;
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

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 添加好友
 */

public class AddFriendsActivity extends BaseActivity {
    @Bind(R.id.tv_right)
    TextView tv_right;
    @Bind(R.id.left_LL)
    LinearLayout leftLL;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_search)
    EditText tvSearch;
    @Bind(R.id.lv_friends)
    MyListView lvFriends;
    @Bind(R.id.ll_ed)
    LinearLayout ll_ed;

    List<FriendListEntity> list;
    List<FriendListEntity> searchList;

    OpenAdapter mMyAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        ll_ed.setFocusable(true);
        ll_ed.setFocusableInTouchMode(true);
        tvTitle.setText("我的好友");
        tv_right.setText("添加朋友");
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
        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddFriendsActivity.this, FriendsInfoActivity.class);
                intent.putExtra("userid", ((FriendListEntity) mMyAdapter.getPositionModel(position)).getFriendId());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "friendlist");
        VolleyFactory.instance().post(AddFriendsActivity.this, mParams, FriendListEntity.class, new VolleyFactory.BaseRequest<FriendListEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                list = JSONArray.parseArray(object, FriendListEntity.class);
                if (list == null) {
                    list = new ArrayList<FriendListEntity>();
                }
                mMyAdapter.setModels(list);
                mMyAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailed() {
            }
        }, true, false);
    }


    @OnClick({R.id.tv_search, R.id.left_LL, R.id.right_LL})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.right_LL:
                jumpTo(InsidContactActivity.class);
                break;
        }
    }

    class FriendHolder implements IHolder<FriendListEntity> {
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
        public void bindModel(int position, final FriendListEntity bean) {
            tv_name.setText(bean.getFriendCn());
            if (bean.getFState() == 1) {
                tv_end.setText("已接受");
                tv_end.setVisibility(View.VISIBLE);
                tv_yes.setVisibility(View.GONE);
            } else if (bean.getFState() == 0) {
                if (bean.getFlag() == 0) {
                    tv_end.setVisibility(View.GONE);
                    tv_yes.setVisibility(View.VISIBLE);
                    tv_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            agree(bean.getKeyId());
                        }
                    });
                } else {
                    tv_end.setText("待接受");
                    tv_end.setVisibility(View.VISIBLE);
                    tv_yes.setVisibility(View.GONE);
                }
            }
            tv_des.setText(bean.getHelloTxt());
            if (!TextUtils.isEmpty(bean.getPhotoUrl())) {
                Picasso.with(AddFriendsActivity.this).load(bean.getPhotoUrl()).into(iv_icon);
            }
        }
    }

    private void agree(String keyId) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "friendconf");
        mParams.put("keyid", keyId);
        VolleyFactory.instance().post(AddFriendsActivity.this, mParams, FriendListEntity.class, new VolleyFactory.BaseRequest<FriendListEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
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
            for (FriendListEntity entity : list) {
                if (!TextUtils.isEmpty(entity.getFriendCn()) && entity.getFriendCn().contains(name)) {
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
