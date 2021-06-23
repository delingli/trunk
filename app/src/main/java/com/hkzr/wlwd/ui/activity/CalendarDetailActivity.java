package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.AddCalendarBean;
import com.hkzr.wlwd.model.CalendarDetailBean;
import com.hkzr.wlwd.model.GroupEntity;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.widget.MoreCalendarPopWindow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 日程详情
 */

public class CalendarDetailActivity extends BaseActivity implements MoreCalendarPopWindow.Go {
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.iv_more)
    ImageView ivMore;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_calendar)
    TextView tvCalendar;
    @Bind(R.id.tv_remind)
    TextView tvRemind;
    @Bind(R.id.view_remark)
    View viewRemark;
    @Bind(R.id.tv_remark)
    TextView tvRemark;
    @Bind(R.id.ll_remark)
    LinearLayout llRemark;
    @Bind(R.id.view_address)
    View view_address;
    @Bind(R.id.rl_address)
    LinearLayout rl_address;
    @Bind(R.id.tvtitle)
    TextView tvtitle;

    String EventId;
    String[] RemindStr2 = new String[]{"不提醒", "当天(上午8时)", "当天(上午9时)", "提前1天(上午8时)", "提前1天(上午9时)", "提前2天(上午8时)", "提前2天(上午9时)"};
    String[] Remind = new String[]{"不提醒", "活动开始前", "提前5分钟", "提前10分钟", "提前15分钟", "提前30分钟", "提前1小时", "提前2小时", "提前1天", "提前2天", "提示1周"};
    String[] RemindIndex = new String[]{"0", "1", "M5", "M10", "M15", "M30", "H1", "H2", "D1", "D2", "W1"};
    String[] Remind2 = new String[]{"0", ":H8", ":H9", ":D1:H8", ":D1:H9", ":D2:H8", ":D2:H9"};

    AddCalendarBean addCalendarBean;
    MoreCalendarPopWindow moreCalendarPopWindow;
    String date;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_calendar_derail);
        EventId = getIntent().getStringExtra("EventId");
        date = getIntent().getStringExtra("date");
        ivRight.setImageResource(R.drawable.web_more);
        tvTitle.setText("日程详情");
        moreCalendarPopWindow = new MoreCalendarPopWindow(this, false);
        moreCalendarPopWindow.setmGo(this);
        initData();
    }


    private void initData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "rc:eventinfo");
        mParams.put("eventid", EventId);
        mParams.put("date", date);
        VolleyFactory.instance().post(this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
            @Override
            public void requestSucceed(String object) {
                CalendarDetailBean calendarDetailBean = JSONObject.parseObject(object, CalendarDetailBean.class);
                addCalendarBean = calendarDetailBean.getModel();
                tvtitle.setText(addCalendarBean.getSubject());
                tvTime.setText(addCalendarBean.getStartTime());
                if (!TextUtils.isEmpty(addCalendarBean.getLocation())) {
                    view_address.setVisibility(View.VISIBLE);
                    rl_address.setVisibility(View.VISIBLE);
                    tvAddress.setText(addCalendarBean.getLocation());
                } else {
                    view_address.setVisibility(View.GONE);
                    rl_address.setVisibility(View.GONE);
                }

                tvTime.setText(addCalendarBean.getTimeInfo());
                String des = addCalendarBean.getDescription();
                tvCalendar.setText(addCalendarBean.getCalendarName());
                if (TextUtils.isEmpty(des)) {
                    llRemark.setVisibility(View.GONE);
                    viewRemark.setVisibility(View.GONE);
                } else {
                    llRemark.setVisibility(View.VISIBLE);
                    viewRemark.setVisibility(View.VISIBLE);
                    tvRemark.setText(des);
                }
                String latlng = addCalendarBean.getCoords();
                if (TextUtils.isEmpty(latlng)) {
                    ivMore.setVisibility(View.INVISIBLE);
                    rl_address.setOnClickListener(null);
                } else {
                    ivMore.setVisibility(View.VISIBLE);
                    String[] str = latlng.split(",");
                    final double[] locationMessage = new double[]{Double.parseDouble(str[0]), Double.parseDouble(str[1])};
                    rl_address.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CalendarDetailActivity.this, MapLocationActivity.class);
                            intent.putExtra("location", locationMessage);
                            startActivity(intent);
                        }
                    });
                }
                if ("1".equals(addCalendarBean.getIsDayEvent())) {
                    List<String> list = Arrays.asList(Remind2);
                    tvRemind.setText(RemindStr2[list.indexOf(addCalendarBean.getAlertSet())]);
                } else {
                    List<String> list = Arrays.asList(RemindIndex);
                    tvRemind.setText(Remind[list.indexOf(addCalendarBean.getAlertSet())]);
                }
//                List<String> list = Arrays.asList(RemindIndex);
//                tvRemind.setText(Remind[list.indexOf(addCalendarBean.getAlertSet())]);
                String[] limit = calendarDetailBean.getLimit().split("\\|");
                if (limit != null && limit.length == 2) {
                    boolean b = !"0".equals(addCalendarBean.getRepeat());
                    if ("0".equals(limit[0]) && "0".equals(limit[1])) {
                        ivRight.setVisibility(View.GONE);
                    }
                    moreCalendarPopWindow.GoneView(limit, b);
                } else {
                    ivRight.setVisibility(View.GONE);
                }
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    @OnClick({R.id.left_LL, R.id.right_LL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.right_LL:
                moreCalendarPopWindow.showPopupWindow(ivRight);
                break;
        }
    }

    @Override
    public void setting() {
        //用不到
    }

    @Override
    public void dingyue() {
        //用不到
    }

    @Override
    public void look() {
        //用不到
    }

    @Override
    public void list() {
        //用不到
    }

    @Override
    public void del(String flag) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "rc:delevent");
        mParams.put("eventid", EventId);
        mParams.put("flag", flag);
        if ("1".equals(flag)) {
            mParams.put("date", date);
        }
        VolleyFactory.instance().post(this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
            @Override
            public void requestSucceed(String object) {
                toast("删除成功");
                setResult(2);
                finish();
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    @Override
    public void updata(String flag) {
        Intent in = new Intent(this, AddCalendarActivity.class);
        in.putExtra("calendar", addCalendarBean);
        if ("1".equals(flag)) {  //事件
            in.putExtra("type", 2);
            in.putExtra("date", date);
        } else {   //系列
            in.putExtra("type", 1);
        }
        startActivityForResult(in, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            setResult(2);
            String id = data.getStringExtra("id");
            if (!TextUtils.isEmpty(id)) {
                EventId = id;
            }
            initData();
        }
    }
}
