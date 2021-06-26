package com.hkzr.wlwd.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.LeftGroupBean;
import com.hkzr.wlwd.ui.activity.MineInfoActivity;
import com.hkzr.wlwd.ui.adapter.LeftAppAdapter;
import com.hkzr.wlwd.ui.adapter.LeftGrpupAdapter;
import com.hkzr.wlwd.ui.adapter.LeftTopAdapter;
import com.hkzr.wlwd.ui.app.User;
import com.hkzr.wlwd.ui.base.BaseFragment;
import com.hkzr.wlwd.ui.utils.JumpSelect;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.widget.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2017/7/5.
 */

public class LeftFragment extends BaseFragment {
    LinearLayout ll_left;
    ImageView ivShare;
    TextView tvCompany;
    XCRoundRectImageView ivIcon;
    TextView tvName;
    TextView tvOccupation;
    TextView tvEmail;
    TextView tvMobile;
    GridView gvTop;
    ListView lvGroup;
    ListView lvApp;

    private LeftGroupBean leftGroupBean;
    //show_head_toast_bg
    LeftGroupBean.TopbarBean topbarBean;
    List<LeftGroupBean.GroupsBean> groupsListBeen;
    LeftGrpupAdapter leftGrpupAdapter;
    LeftAppAdapter leftAppAdapter;
    List<LeftGroupBean.TopbarBean.ListBean> topList;

    @Override
    public int getViewId() {
        return R.layout.left_layout;
    }

    @Override
    public void findView(View parent) {
        ll_left = parent.findViewById(R.id.ll_left);
        ivShare = parent.findViewById(R.id.iv_share);
        tvCompany = parent.findViewById(R.id.tv_company);
        ivIcon = parent.findViewById(R.id.iv_icon);
        tvName = parent.findViewById(R.id.tv_name);
        tvOccupation = parent.findViewById(R.id.tv_occupation);
        tvEmail = parent.findViewById(R.id.tv_email);
        tvMobile = parent.findViewById(R.id.tv_mobile);
        gvTop = parent.findViewById(R.id.gv_top);
        lvGroup = parent.findViewById(R.id.lv_group);
        lvApp = parent.findViewById(R.id.lv_app);
        parent.findViewById(R.id.iv_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump(MineInfoActivity.class);
            }
        });

    }

    @Override
    public void initWidget(View parent) {
        findView(parent);
        ivShare.setVisibility(View.GONE);
//        AndroidBug54971Workaround.assistActivity(ll_left);
        initListener();
        initSlidDatas();
        initLeftData();
    }


    private void initListener() {
        gvTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JumpSelect.jump(getActivity(), topbarBean.getList().get(position).getFunType(), topbarBean.getList().get(position).getFunLink());
            }
        });
        lvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LeftGroupBean.GroupsBean groupsBean = groupsListBeen.get(position);
                leftGrpupAdapter.setmCurrent(position);
                if (leftAppAdapter != null) {
                    leftAppAdapter.setList(groupsBean.getList());
                } else {
                    leftAppAdapter = new LeftAppAdapter(getActivity(), groupsBean.getList());
                    lvApp.setAdapter(leftAppAdapter);
                }
            }
        });
        lvApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LeftGroupBean.GroupsBean.ListBeanX listBeanX = leftAppAdapter.getList().get(position);
                JumpSelect.jump(getActivity(), listBeanX.getFunType(), listBeanX.getFunLink());
            }
        });
//        ivShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jump(CalendarActivity.class);
//            }
//        });
    }

    private void initLeftData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "leftpanel");
        VolleyFactory.instance().post(getActivity(), mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                leftGroupBean = JSONObject.parseObject(object.toString(), LeftGroupBean.class);
                showData();
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    /**
     * 展示左侧菜单数据
     */
    private void showData() {
        topbarBean = leftGroupBean.getTopbar();
        if (topbarBean.isShow()) {
            gvTop.setVisibility(View.VISIBLE);
            topList = topbarBean.getList();
            LeftTopAdapter appTopAdapter = new LeftTopAdapter(getActivity(), topList);
            gvTop.setAdapter(appTopAdapter);
        } else {
            gvTop.setVisibility(View.GONE);
        }
        groupsListBeen = leftGroupBean.getGroups();
        if (groupsListBeen.size() > 0) {
            leftGrpupAdapter = new LeftGrpupAdapter(getActivity(), groupsListBeen, 0);
            lvGroup.setAdapter(leftGrpupAdapter);
            leftAppAdapter = new LeftAppAdapter(getActivity(), groupsListBeen.get(0).getList());
            lvApp.setAdapter(leftAppAdapter);
        }

    }

    private void initSlidDatas() {
        if (mUserInfoCache.getUser() != null) {
            Picasso.with(getActivity()).invalidate(mUserInfoCache.getUser().getPhotoUrl());
            Picasso.with(getActivity()).load(mUserInfoCache.getUser().getPhotoUrl())./*placeholder(R.drawable.morentouxiang)*/error(R.drawable.morentouxiang).into(ivIcon);
            tvCompany.setText(mUserInfoCache.getUser().getCorpName());
            tvName.setText(mUserInfoCache.getUser().getUserCn());
            tvEmail.setText(mUserInfoCache.getUser().getEMail());
            tvOccupation.setText(mUserInfoCache.getUser().getPosName());
            tvMobile.setText(mUserInfoCache.getUser().getMobilePhone());
        } else {
            getPersonInfo();
        }

    }

    /**
     * 获取个人信息   方便及时刷新
     */
    private void getPersonInfo() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "userinfo");
        mParams.put("userid", mUserInfoCache.getUserid());
        mParams.put("TokenId", mUserInfoCache.getTokenid());
        VolleyFactory.instance().post(getActivity(), mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                User entity = JSON.parseObject(object.toString(), User.class);
                mUserInfoCache.setUser(entity);
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }
}
