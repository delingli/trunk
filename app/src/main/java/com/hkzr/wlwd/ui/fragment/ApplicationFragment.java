package com.hkzr.wlwd.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.SDK_WebView;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.ApplicationEntity;
import com.hkzr.wlwd.model.ServiceEntity;
import com.hkzr.wlwd.ui.adapter.AppExpandableListAdapter;
import com.hkzr.wlwd.ui.adapter.AppTopAdapter;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.base.BaseFragment;
import com.hkzr.wlwd.ui.utils.JumpSelect;
import com.hkzr.wlwd.ui.utils.LogUtil;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.view.MyGridView;
import com.hkzr.wlwd.ui.widget.MyAdGallery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2017/4/24.
 * 应用
 */
public class ApplicationFragment extends BaseFragment {

    ExpandableListView expandableListView;
    //    LinearLayout llGd;
//    LinearLayout llSp;
//    LinearLayout llSys;
//    LinearLayout llQiandao;
    LinearLayout ll_top;
    MyAdGallery adgallery;
    LinearLayout ovalLayout;
    RelativeLayout rl_banner;
    View headerView;
    MyGridView gv_top;

    @Override
    public void findView(View parent) {
        expandableListView = parent.findViewById(R.id.expandableListView);
    }

    @Override
    public int getViewId() {
        return R.layout.layout_app;
    }

    @Override
    public void initWidget(View parent) {
        findView(parent);
        initHeaderView();
//        initDatas();

    }


    private void initHeaderView() {
        headerView = View.inflate(getActivity(), R.layout.layout_banner, null);
//        llGd = (LinearLayout) headerView.findViewById(R.id.ll_gd);
//        llSp = (LinearLayout) headerView.findViewById(R.id.ll_sp);
//        llSys = (LinearLayout) headerView.findViewById(R.id.ll_sys);
//        llQiandao = (LinearLayout) headerView.findViewById(R.id.ll_qiandao);
        ll_top = (LinearLayout) headerView.findViewById(R.id.ll_top);
        rl_banner = (RelativeLayout) headerView.findViewById(R.id.rl_banner);
        adgallery = (MyAdGallery) headerView.findViewById(R.id.adgallery);
        ovalLayout = (LinearLayout) headerView.findViewById(R.id.ovalLayout);
        gv_top = (MyGridView) headerView.findViewById(R.id.gv_top);
        gv_top.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JumpSelect.jump(getActivity(), topList.get(position).getFunType(), topList.get(position).getFunLink());
            }
        });
        expandableListView.addHeaderView(headerView);
//        llGd.setOnClickListener(this);
//        llSp.setOnClickListener(this);
//        llSys.setOnClickListener(this);
//        llQiandao.setOnClickListener(this);
        View v = View.inflate(getActivity(), R.layout.layout_bottom, null);
        expandableListView.addFooterView(v);
    }

    public void setAd(final String[] imageUrl) {
        // 第二和第三参数 2选1 ,参数2为 图片网络路径数组 ,参数3为图片id的数组,本地测试用 ,2个参数都有优先采用 参数2
        adgallery.start(getActivity(), imageUrl, 3666, ovalLayout,
                R.drawable.yuandian_on, R.drawable.yuandian);
        adgallery.setMyOnItemClickListener(new MyAdGallery.MyOnItemClickListener() {
            @Override
            public void onItemClick(int curIndex) {
                Intent intent = new Intent(getActivity(), SDK_WebView.class);
                String url = entity.getSlider().getList().get(curIndex).getTarget();
                if (!TextUtils.isEmpty(url)) {
                    if (url.contains("{tokenid}")) {
                        url = url.replace("{tokenid}", UserInfoCache.init().getTokenid());
                    }
                    Log.e("Application_list", url);
                    intent.putExtra("title", entity.getSlider().getList().get(curIndex).getTitle());
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }
        });
    }

    ApplicationEntity entity;
    List<ApplicationEntity.GroupsBean> group_list;
    List<ApplicationEntity.SliderBean.ListBean> bannerList; //轮播图
    List<ApplicationEntity.TopbarBean.ListBeanX> topList;

    private void initDatas() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "applist");
        mParams.put("user", App.getInstance().getUserName());
        VolleyFactory.instance().post(getActivity(), mParams, ServiceEntity.class, new VolleyFactory.BaseRequest<ServiceEntity>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                LogUtil.d("ldl", object.toString());
                entity = JSON.parseObject(object.toString(), ApplicationEntity.class);
                initDataView();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }

    public void setRfresh() {
        initDatas();
    }

    @Override
    public void onResume() {
        super.onResume();
        setRfresh();
    }

    private void initDataView() {
        group_list = entity.getGroups();
        if (entity.getTopbar().isShow()) {
            ll_top.setVisibility(View.VISIBLE);
            topList = entity.getTopbar().getList();
            AppTopAdapter appTopAdapter = new AppTopAdapter(getActivity(), topList);
            gv_top.setAdapter(appTopAdapter);
        } else {
            ll_top.setVisibility(View.GONE);
        }
        if (entity.getSlider().isShow()) {
            rl_banner.setVisibility(View.VISIBLE);
            bannerList = entity.getSlider().getList();
            if (bannerList != null && bannerList.size() > 0) {
                String[] str = new String[bannerList.size()];
                for (int i = 0; i < bannerList.size(); i++) {
                    str[i] = bannerList.get(i).getImgUrl();
                }
                setAd(str);
            }
        } else {
            rl_banner.setVisibility(View.GONE);
        }
        if (group_list != null) {
            AppExpandableListAdapter appExpandableListAdapter = new AppExpandableListAdapter(getActivity(), group_list);
//            LogUtil.d("ldl",group_list.toString());
            expandableListView.setAdapter(appExpandableListAdapter);
            appExpandableListAdapter.setOnChildClickListener(new AppExpandableListAdapter.OnChildClickListener() {
                @Override
                public void onChildClick(int groupPosition, int childPosition) {
//                        toast("点击第" + groupPosition + "组第" + childPosition + "项");
//                    if (!TextUtils.isEmpty(group_list.get(groupPosition).getList().get(childPosition).getFunLink())) {
//                        Intent intent = new Intent(getActivity(), SDK_WebView.class);
//                        String url = group_list.get(groupPosition).getList().get(childPosition).getFunLink();
//                        if (url.contains("{tokenid}")) {
//                            url = url.replace("{tokenid}", UserInfoCache.init().getTokenid());
//                        }
//                        Log.e("ApplicationFragment", url);
//                        intent.putExtra("title", group_list.get(groupPosition).getList().get(childPosition).getFunName());
//                        intent.putExtra("url", url);
//                        startActivity(intent);
                    JumpSelect.jump(getActivity(),
                            group_list.get(groupPosition).getList().get(childPosition).getFunType(),
                            group_list.get(groupPosition).getList().get(childPosition).getFunLink(),
                            group_list.get(groupPosition).getList().get(childPosition).getFunCode()
                    );
//                    }
                }
            });
            for (int i = 0; i < group_list.size(); i++) {
                expandableListView.expandGroup(i);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent(getActivity(), SDK_WebView.class);
//        switch (v.getId()) {
//            case R.id.ll_gd:
//                intent.putExtra("title", "工单");
//                intent.putExtra("url", App.getInstance().getH5Url() + ReqUrl.TaskMain);
//                startActivity(intent);
//                break;
//            case R.id.ll_sp:
//                intent.putExtra("title", "审批");
//                intent.putExtra("url", App.getInstance().getH5Url() + ReqUrl.FlowMain);
//                startActivity(intent);
//                break;
//            case R.id.ll_sys:
//                jump(CaptureActivity.class);
//                break;
//            case R.id.ll_qiandao:
//                jump(CheckWorkAttendanceActivity.class);
//                break;
//        }
//    }
}
