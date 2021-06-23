package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.CalendarTypeBean;
import com.hkzr.wlwd.model.GroupEntity;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 查看他人日程列表
 */

public class LookCalendarActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.filter_edit)
    ClearEditText filterEdit;
    @Bind(R.id.tv_none)
    TextView tv_none;

    OpenAdapter mMyAdapter;
    List<CalendarTypeBean> searchBean;
    List<CalendarTypeBean> calendarBean;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_look_calendar);
        tvTitle.setText("查看他人日程");
        calendarBean = new ArrayList<>();
        searchBean = new ArrayList<>();
        mMyAdapter = new OpenAdapter(calendarBean) {
            @Override
            public IHolder createHolder(int position) {
                return new MyHolder();
            }
        };
        listview.setAdapter(mMyAdapter);
        initCalendar();
        initListener();
    }

    private void initListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("CallBean", calendarBean.get(position));
                jumpTo(OrderCalendarActivity.class, bundle);
            }
        });
        filterEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = filterEdit.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    mMyAdapter.setModels(calendarBean);
                    mMyAdapter.notifyDataSetChanged();
                    tv_none.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                } else {
                    searchBean.clear();
                    for (CalendarTypeBean calendarTypeBean : calendarBean) {
                        if (calendarTypeBean.getCalTitle().indexOf(str) != -1) {
                            searchBean.add(calendarTypeBean);
                        }
                    }
                    if (searchBean.size() > 0) {
                        tv_none.setVisibility(View.GONE);
                        listview.setVisibility(View.VISIBLE);
                        mMyAdapter.setModels(searchBean);
                        mMyAdapter.notifyDataSetChanged();
                    } else {
                        tv_none.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 获取日程列表
     */
    private void initCalendar() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "rc:otherlist");
        VolleyFactory.instance().post(LookCalendarActivity.this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
            @Override
            public void requestSucceed(String object) {
                calendarBean.clear();
                calendarBean.addAll(JSONObject.parseArray(object, CalendarTypeBean.class));
                mMyAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }

    @OnClick(R.id.left_LL)
    public void onViewClicked() {
        this.finish();
    }


    class MyHolder implements IHolder<CalendarTypeBean> {
        TextView tv;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_calendar_list, parent, false);
            tv = (TextView) convertview.findViewById(R.id.tv_name);
            return convertview;
        }

        @Override
        public void bindModel(int position, final CalendarTypeBean bean) {
            tv.setText(bean.getCalTitle());
        }
    }

}
