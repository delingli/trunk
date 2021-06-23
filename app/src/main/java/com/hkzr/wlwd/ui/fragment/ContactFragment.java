package com.hkzr.wlwd.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.ContactEntity;
import com.hkzr.wlwd.ui.activity.FriendsInfoActivity;
import com.hkzr.wlwd.ui.activity.OrganizationQueryActivity;
import com.hkzr.wlwd.ui.activity.SearchContactsActivity;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseFragment;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.view.MyListView;
import com.hkzr.wlwd.ui.widget.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 通讯录
 */
public class ContactFragment extends BaseFragment {
    @Bind(R.id.lv_friends)
    MyListView lv_friends;
    @Bind(R.id.lv_top)
    MyListView lv_top;
    @Bind(R.id.ll_top)
    LinearLayout ll_top;
    @Bind(R.id.ll_search)
    LinearLayout ll_search;

    OpenAdapter mMyAdapter;
    OpenAdapter mOrgMyAdapter;

    @Override
    public int getViewId() {
        return R.layout.layout_contact;
    }

    ContactEntity contactEntity;
    List<ContactEntity.LinklistBean> list;
    List<ContactEntity.OrglistBean> orglistBeen;

    @Override
    public void initWidget(View parent) {
        findView(parent);
        ll_top.setFocusable(true);
        ll_top.setFocusableInTouchMode(true);
        ll_top.requestFocus();
        initContact();
        initTop();

        initListener();
    }

    /**
     * 初始化顶部
     */
    private void initTop() {
        orglistBeen = new ArrayList<>();
        mOrgMyAdapter = new OpenAdapter(orglistBeen) {
            @Override
            public IHolder createHolder(int position) {
                return new OrgHolder();
            }
        };
        lv_top.setAdapter(mOrgMyAdapter);
    }

    /**
     * 初始化通讯录
     */
    private void initContact() {
        list = new ArrayList<>();
        mMyAdapter = new OpenAdapter(list) {
            @Override
            public IHolder createHolder(int position) {
                return new ContactHolder();
            }
        };
        lv_friends.setAdapter(mMyAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getContact();
    }

    @OnClick({R.id.ll_search})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                jump(SearchContactsActivity.class);
                break;
        }
    }

    private void initListener() {
        lv_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FriendsInfoActivity.class);
                intent.putExtra("userid", list.get(position).getUserId());
                startActivity(intent);
            }
        });
        lv_top.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrganizationQueryActivity.class);
                intent.putExtra("orgId", orglistBeen.get(position).getOrgId());
                startActivity(intent);
            }
        });
    }

    public void getContact() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "linkpage");
        LogUtils.e(mParams.toString());
        VolleyFactory.instance().post(getActivity(), mParams, ContactEntity.class, new VolleyFactory.BaseRequest<ContactEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                contactEntity = JSON.parseObject(object.toString(), ContactEntity.class);
                list = contactEntity.getLinklist();
                orglistBeen = contactEntity.getOrglist();
                mOrgMyAdapter.setModels(orglistBeen);
                mOrgMyAdapter.notifyDataSetChanged();
                mMyAdapter.setModels(list);
                mMyAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    class ContactHolder implements IHolder<ContactEntity.LinklistBean> {
        TextView tv_name, tv_code;
        ImageView iv_sex;
        XCRoundRectImageView iv_icon;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_friends, parent, false);
            tv_name = (TextView) convertview.findViewById(R.id.tv_name);
            tv_code = (TextView) convertview.findViewById(R.id.tv_code);
            iv_icon = (XCRoundRectImageView) convertview.findViewById(R.id.iv_icon);
            iv_sex = (ImageView) convertview.findViewById(R.id.iv_sex);
            return convertview;
        }

        @Override
        public void bindModel(int position, final ContactEntity.LinklistBean bean) {
            tv_name.setText(bean.getUserCn());
            tv_code.setText(bean.getMobilePhone());
            if (!TextUtils.isEmpty(bean.getPhotoUrl())) {
                Picasso.with(getActivity()).load(bean.getPhotoUrl()).error(R.drawable.morentouxiang).into(iv_icon);
            }
            if (!TextUtils.isEmpty(bean.getSex())) {
                iv_sex.setVisibility(View.VISIBLE);
                if ("男".equalsIgnoreCase(bean.getSex())) {
                    iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.men));
                } else if ("女".equalsIgnoreCase(bean.getSex())) {
                    iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.women));
                }
            } else {
                iv_sex.setVisibility(View.GONE);
            }
        }
    }

    class OrgHolder implements IHolder<ContactEntity.OrglistBean> {
        ImageView iv;
        TextView tv;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_contact_top, parent, false);
            iv = (ImageView) convertview.findViewById(R.id.iv_icon);
            tv = (TextView) convertview.findViewById(R.id.tv_name);
            return convertview;
        }

        @Override
        public void bindModel(int position, final ContactEntity.OrglistBean bean) {
            tv.setText(bean.getOrgName());
            if ("1".equals(bean.getOrgType())) {
                iv.setImageResource(R.drawable.zongbumen);
                tv.setTextColor(getResources().getColor(R.color.color_2d2d2e));
            } else {
                iv.setImageResource(R.drawable.zibumen);
                tv.setTextColor(getResources().getColor(R.color.color_5A5A5C));
            }
        }
    }
}
