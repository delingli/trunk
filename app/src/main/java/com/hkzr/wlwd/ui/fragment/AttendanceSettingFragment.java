package com.hkzr.wlwd.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.SignSettingBean;
import com.hkzr.wlwd.model.SignTodayListBean;
import com.hkzr.wlwd.ui.activity.SignSettingActivity;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseFragment;
import com.hkzr.wlwd.ui.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 考勤设置
 */

public class AttendanceSettingFragment extends BaseFragment {
    MyListView lvSetting;

    @Override
    public int getViewId() {
        return R.layout.fragment_att_setting;
    }

    List<SignSettingBean> list;
    OpenAdapter mMyAdapter;

    @Override
    public void findView(View parent) {
        lvSetting=  parent.findViewById(R.id.lv_setting);
    }

    @Override
    public void initWidget(View parent) {
        findView(parent);
        list = new ArrayList<>();
        mMyAdapter = new OpenAdapter(list) {
            @Override
            public IHolder createHolder(int position) {
                return new SettingHolder();
            }
        };
        lvSetting.setAdapter(mMyAdapter);
        lvSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SignSettingActivity.class);
                intent.putExtra("id", list.get(position).getUnitID());
                intent.putExtra("name", list.get(position).getUnitName());
                startActivity(intent);
            }
        });
        initData();
    }

    private void initData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:group_list");
        VolleyFactory.instance().post(getActivity(), mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
            @Override
            public void requestSucceed(String object) {
                if (!TextUtils.isEmpty(object)) {
                    list = JSON.parseArray(object.toString(), SignSettingBean.class);
                    if (list != null && list.size() > 0) {
                        mMyAdapter.setModels(list);
                        mMyAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }


    class SettingHolder implements IHolder<SignSettingBean> {
        TextView tv_name, tv_content;
        ImageView iv_setting;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_setting, parent, false);
            tv_name = (TextView) convertview.findViewById(R.id.tv_name);
            tv_content = (TextView) convertview.findViewById(R.id.tv_content);
            iv_setting = (ImageView) convertview.findViewById(R.id.iv_setting);
            return convertview;
        }

        @Override
        public void bindModel(int position, final SignSettingBean bean) {
            tv_name.setText(bean.getUnitName());
            tv_content.setText(bean.getRemark());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
