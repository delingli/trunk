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

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2017/7/5.
 */

public class LeftFragment extends BaseFragment {
    @Bind(R.id.ll_left)
    LinearLayout ll_left;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.tv_company)
    TextView tvCompany;
    @Bind(R.id.iv_icon)
    XCRoundRectImageView ivIcon;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_occupation)
    TextView tvOccupation;
    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.tv_mobile)
    TextView tvMobile;
    @Bind(R.id.gv_top)
    GridView gvTop;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.lv_app)
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
    public void initWidget(View parent) {
        findView(parent);
        ivShare.setVisibility(View.GONE);
//        AndroidBug54971Workaround.assistActivity(ll_left);
        initListener();
        initSlidDatas();
        initLeftData();
    }

    @OnClick(R.id.iv_icon)
    public void toInfo() {
        jump(MineInfoActivity.class);

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
