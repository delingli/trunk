package com.hkzr.wlwd.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.model.OrganizationQueryBean;
import com.hkzr.wlwd.ui.activity.FriendsInfoActivity;
import com.hkzr.wlwd.ui.activity.OrganizationQueryActivity;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseFragment;
import com.hkzr.wlwd.ui.widget.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;

/**
 * 组织机构子fragment
 */

public class OrganizationFragment extends BaseFragment {
    @Bind(R.id.lv_organization)
    ListView lv_organization;

    @Override
    public int getViewId() {
        return R.layout.fragment_list;
    }

    public OrganizationFragment() {
    }

    public static OrganizationFragment newInstance(String type) {
        OrganizationFragment f = new OrganizationFragment();
        Bundle b = new Bundle();
        b.putString("json", type);
        f.setArguments(b);
        return f;

    }

    String json;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            json = getArguments().getString("json");
        }
    }

    OrganizationQueryActivity organizationQueryActivity;
    List<OrganizationQueryBean.SubListBean> list;
    OpenAdapter mMyAdapter;

    @Override
    public void initWidget(View parent) {
        findView(parent);
        organizationQueryActivity = (OrganizationQueryActivity) getActivity();
        OrganizationQueryBean organizationQueryBean = JSONObject.parseObject(json, OrganizationQueryBean.class);
        list = organizationQueryBean.getSubList();
        mMyAdapter = new OpenAdapter(list) {
            @Override
            public IHolder createHolder(int position) {
                return new OrganizationHolder();
            }
        };
        lv_organization.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("9".equals(list.get(position).getOrgType())) {
                    Intent intent = new Intent(organizationQueryActivity, FriendsInfoActivity.class);
                    intent.putExtra("userid", list.get(position).getOrgId());
                    startActivity(intent);
                } else {
                    organizationQueryActivity.nextOrgan(list.get(position).getOrgId());
                }
            }
        });
        lv_organization.setAdapter(mMyAdapter);
    }

    class OrganizationHolder implements IHolder<OrganizationQueryBean.SubListBean> {
        TextView tv_group_name, tv_group_count, tv_user_name, tv_user_SubName;
        XCRoundRectImageView iv_icon;
        LinearLayout ll_organization, ll_user;
        ImageView iv_sex;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_organization, parent, false);
            ll_organization = (LinearLayout) convertview.findViewById(R.id.ll_organization);
            ll_user = (LinearLayout) convertview.findViewById(R.id.ll_user);
            tv_group_name = (TextView) convertview.findViewById(R.id.tv_group_name);
            tv_group_count = (TextView) convertview.findViewById(R.id.tv_group_count);
            tv_user_name = (TextView) convertview.findViewById(R.id.tv_user_name);
            tv_user_SubName = (TextView) convertview.findViewById(R.id.tv_user_SubName);
            iv_icon = (XCRoundRectImageView) convertview.findViewById(R.id.iv_icon);
            iv_sex = (ImageView) convertview.findViewById(R.id.iv_sex);
            return convertview;
        }

        @Override
        public void bindModel(int position, final OrganizationQueryBean.SubListBean bean) {
            if ("9".equals(bean.getOrgType())) {
                tv_user_name.setText(bean.getOrgName());
                ll_user.setVisibility(View.VISIBLE);
                ll_organization.setVisibility(View.GONE);
                tv_user_SubName.setText(bean.getSubName());
                if (!TextUtils.isEmpty(bean.getPhotoUrl())) {
                    Picasso.with(getActivity()).load(bean.getPhotoUrl()).into(iv_icon);
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
            } else {
                ll_user.setVisibility(View.GONE);
                ll_organization.setVisibility(View.VISIBLE);
                tv_group_name.setText(bean.getOrgName());
                tv_group_count.setText(bean.getUserCount() != 0 ? bean.getUserCount() + "" : "");
            }
        }
    }
}
