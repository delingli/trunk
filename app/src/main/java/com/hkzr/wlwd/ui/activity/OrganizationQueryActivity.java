package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.OrganizationQueryBean;
import com.hkzr.wlwd.ui.adapter.MyFragmentAdapter;
import com.hkzr.wlwd.ui.adapter.OrganizationRecyleViewAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.fragment.OrganizationFragment;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 组织查询
 */

public class OrganizationQueryActivity extends BaseActivity implements OrganizationRecyleViewAdapter.MyItemClickListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_organization)
    RecyclerView rv_organization;
    @Bind(R.id.viewpager)
    NoScrollViewPager viewpager;
    @Bind(R.id.iv_home)
    ImageView iv_home;


    String orgid = "";
    int currentItem = -1;
    List<Fragment> fragments = new ArrayList<>();
    MyFragmentAdapter myFragmentAdapter;
    List<String> title = new ArrayList<>();
    OrganizationRecyleViewAdapter myRecyclerAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_organization);
        tvTitle.setText("组织查询");
        orgid = getIntent().getStringExtra("orgId");
        if (TextUtils.isEmpty(orgid)) {
            orgid = "";
            iv_home.setVisibility(View.GONE);
        }
        myFragmentAdapter = new MyFragmentAdapter(this.getSupportFragmentManager(), fragments);
        viewpager.setAdapter(myFragmentAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        rv_organization.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        myRecyclerAdapter = new OrganizationRecyleViewAdapter(title, this);
        myRecyclerAdapter.setMyItemClickListener(this);
        rv_organization.setAdapter(myRecyclerAdapter);
        getOrganization();
    }


    public void getOrganization() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "orguserlist");
        mParams.put("orgid", orgid);
        VolleyFactory.instance().post(this, mParams, OrganizationQueryBean.class, new VolleyFactory.BaseRequest<OrganizationQueryBean>() {
            @Override
            public void requestSucceed(String object) {
                if (!TextUtils.isEmpty(object)) {
                    OrganizationQueryBean entity = JSONObject.parseObject(object, OrganizationQueryBean.class);
                    LogUtils.e(object.toString());
                    currentItem++;
                    fragments.add(OrganizationFragment.newInstance(object));
                    myFragmentAdapter.notifyDataSetChanged();
                    title.add(entity.getOrgName());
                    myRecyclerAdapter.notifyDataSetChanged();
                    viewpager.setCurrentItem(currentItem);
                    rv_organization.scrollToPosition(currentItem);
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    @OnClick({R.id.iv_back, R.id.iv_close, R.id.iv_home})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                int current = viewpager.getCurrentItem();
                currentItem = current - 1;
                if (current == 0) {
                    this.finish();
                } else {
                    title.remove(current);
                    fragments.remove(current);
                    myFragmentAdapter.notifyDataSetChanged();
                    myRecyclerAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.iv_close:
                this.finish();
                break;
            case R.id.iv_home:
                this.finish();
                jumpTo(OrganizationQueryActivity.class);
                break;
        }
    }

    public void nextOrgan(String orgid) {
        this.orgid = orgid;
        getOrganization();
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            int current = viewpager.getCurrentItem();
            currentItem = current - 1;
            if (current == 0) {
                this.finish();
            } else {
                title.remove(current);
                fragments.remove(current);
                myFragmentAdapter.notifyDataSetChanged();
                myRecyclerAdapter.notifyDataSetChanged();
//                rv_organization.scrollToPosition(currentItem);
//                viewpager.setCurrentItem(current - 1);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onItemClick(View view, int postion) {
        if (postion == currentItem) {
            return;
        }
        currentItem = postion;
        int size = fragments.size();
        for (int i = postion + 1; i < size; i++) {
            title.remove(postion + 1);
            fragments.remove(postion + 1);
        }
        myFragmentAdapter.notifyDataSetChanged();
        myRecyclerAdapter.notifyDataSetChanged();
        viewpager.setCurrentItem(currentItem);
//        rv_organization.scrollToPosition(currentItem);

    }
}
