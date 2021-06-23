package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.DialogUtil;
import com.hkzr.wlwd.ui.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选项通用
 */

public class CalendarOptionActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.listview)
    ListView mListview;
    int type = 0; //0 提醒 1 重复 2,结束重复
    List<String> list = new ArrayList<>();
    String[] Remind = new String[]{"不提醒", "活动开始前", "提前5分钟", "提前10分钟", "提前15分钟", "提前30分钟", "提前1小时", "提前2小时", "提前1天", "提前2天", "提示1周"};
    String[] Remind2 = new String[]{"不提醒", "当天(上午8时)", "当天(上午9时)", "提前1天(上午8时)", "提前1天(上午9时)", "提前2天(上午8时)", "提前2天(上午9时)"};

    String[] Repeat = new String[]{"一次性活动", "每天", "每个工作日"};
    OpenAdapter mMyAdapter;
    int index = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_calendar_option);
        type = getIntent().getExtras().getInt("type");
        index = getIntent().getExtras().getInt("index");
        initListener();
        initData();
    }

    private void initData() {
        switch (type) {
            case 0:
                tvTitle.setText("提醒设置");
                list = Arrays.asList(Remind);
                break;
            case 1:
                tvTitle.setText("重复");
                String date = getIntent().getExtras().getString("date");
                String[] str = date.split("-");
                list.addAll(Arrays.asList(Repeat));
                list.add("每周" + TimeUtil.getweek(date));
                list.add("每月" + str[2] + "日");
                list.add("每年" + str[1] + "月" + str[2] + "日");
                break;
            case 2:
                String endDate = getIntent().getExtras().getString("date");
                tvTitle.setText("结束重复");
                list.add("永不");
                if (TextUtils.isEmpty(endDate)) {
                    index = 0;
                    list.add("时间");
                } else {
                    index = 1;
                    list.add("时间(" + endDate + ")");
                }
                break;
            case 3:
                tvTitle.setText("提醒设置");
                list = Arrays.asList(Remind2);
                break;

        }
        mMyAdapter = new OpenAdapter(list) {
            @Override
            public IHolder createHolder(int position) {
                return new MyHolder();
            }
        };
        mListview.setAdapter(mMyAdapter);
    }

    private void initListener() {
        mListview.setOnItemClickListener(this);
    }


    @OnClick(R.id.left_LL)
    public void onViewClicked() {
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Intent intent = getIntent();
        switch (type) {
            case 0:
                intent.putExtra("index", position);
                intent.putExtra("text", list.get(position));
                setResult(11, intent);
                this.finish();
                break;
            case 1:
                intent.putExtra("index", position);
                intent.putExtra("text", list.get(position));
                setResult(12, intent);
                this.finish();
                break;
            case 2:
                if (position == 0) {
                    setResult(13);
                    this.finish();
                } else {
                    DialogUtil.showTime(this, new TimePickerView.OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            intent.putExtra("date", TimeUtil.date2String(date, TimeUtil.FORMAT_DATE));
                            setResult(13, intent);
                            finish();
                        }
                    }).show();
                }
                break;
            case 3:
                intent.putExtra("index", position);
                intent.putExtra("text", list.get(position));
                setResult(14, intent);
                this.finish();
                break;

        }
    }

    class MyHolder implements IHolder<String> {
        TextView tv;
        ImageView iv_select;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_text, parent, false);
            iv_select = (ImageView) convertview.findViewById(R.id.iv_select);
            tv = (TextView) convertview.findViewById(R.id.tv);
            return convertview;
        }

        @Override
        public void bindModel(int position, final String bean) {
            tv.setText(bean);
            if (position == index) {
                iv_select.setVisibility(View.VISIBLE);
            } else {
                iv_select.setVisibility(View.GONE);
            }
        }
    }
}
