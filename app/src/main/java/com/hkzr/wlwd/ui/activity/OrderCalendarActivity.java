package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.SDK_WebView;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.CalendarBean;
import com.hkzr.wlwd.model.CalendarDayEventBean;
import com.hkzr.wlwd.model.CalendarTypeBean;
import com.hkzr.wlwd.model.GroupEntity;
import com.hkzr.wlwd.ui.adapter.OrderCalendarExpandableListAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.DialogUtil;
import com.hkzr.wlwd.ui.utils.TimeUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.widget.MyExpandableListView;
import com.hkzr.wlwd.ui.widget.month.CalendarUtils;
import com.hkzr.wlwd.ui.widget.month.HolidayBean;
import com.hkzr.wlwd.ui.widget.month.MonthCalendarView;
import com.hkzr.wlwd.ui.widget.month.OnCalendarClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 他人日程
 */

public class OrderCalendarActivity extends BaseActivity implements OrderCalendarExpandableListAdapter.OnChildClickListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_month)
    TextView tvMonth;
    @Bind(R.id.tv_day)
    TextView tvDay;
    @Bind(R.id.tv_year)
    TextView tvYear;
    @Bind(R.id.mcvCalendar)
    MonthCalendarView mcvCalendar;
    @Bind(R.id.expandableListView)
    MyExpandableListView expandableListView;
    List<CalendarDayEventBean.GroupsBean> mCurrentCalendar;
    OrderCalendarExpandableListAdapter mAdapter;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    String type = "";
    CalendarTypeBean calendarTypeBean;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.layout_order_calendar);
        mcvCalendar.setFocusable(true);
        mcvCalendar.setFocusableInTouchMode(true);
        mcvCalendar.requestFocus();
        calendarTypeBean = getIntent().getExtras().getParcelable("CallBean");
        type = calendarTypeBean.getCalID();
        tvTitle.setText(calendarTypeBean.getCalTitle());
        initMonth();
        initNetData(type, mCurrentSelectYear, mCurrentSelectMonth + 1);
    }

    /**
     * 初始化月视图
     */
    private void initMonth() {
        Calendar calendar = Calendar.getInstance();
        resetCurrentSelectDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        mcvCalendar.setOnCalendarClickListener(mMonthCalendarClickListener);
    }

    /**
     * 设置当前日期
     *
     * @param year
     * @param month
     * @param day
     */
    private void resetCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
        tvMonth.setText(month + 1 + "月");
        tvDay.setText(day + "日");
        tvYear.setText(year + "年");
        initCalendar(type, mCurrentSelectYear + "-" + (mCurrentSelectMonth + 1) + "-" + mCurrentSelectDay);
    }

    /**
     * 月视图监听
     */
    private OnCalendarClickListener mMonthCalendarClickListener = new OnCalendarClickListener() {
        /**
         * 点击日期
         * @param year
         * @param month
         * @param day
         */
        @Override
        public void onClickDate(int year, int month, int day) {
            resetCurrentSelectDate(year, month, day);
        }

        /**
         *月份切换
         * @param year
         * @param month
         * @param day
         */
        @Override
        public void onPageChange(int year, int month, int day) {
            initNetData(type, mCurrentSelectYear, mCurrentSelectMonth + 1);
        }
    };


    @OnClick({R.id.rl_date, R.id.left_LL, R.id.tv_today})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_date:
                /*
                    选择日期
                 */
                DialogUtil.showTime(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String[] str = TimeUtil.date2String(date, TimeUtil.FORMAT_DATE).split("-");
                        int year = Integer.parseInt(str[0]);
                        int month = Integer.parseInt(str[1]);
                        int day = Integer.parseInt(str[2]);
                        initData(year, month - 1, day);
                    }
                }).show();
                break;
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.tv_today:
                String[] str = TimeUtil.getCurrentTime(TimeUtil.FORMAT_DATE).split("-");
                initData(Integer.parseInt(str[0]), Integer.parseInt(str[1]) - 1, Integer.parseInt(str[2]));
                break;
        }
    }

    /**
     * 跳转某一天
     *
     * @param year
     * @param month (0-11)
     * @param day   (1-31)
     */
    public void initData(int year, int month, int day) {
        int monthDis = CalendarUtils.getMonthsAgo(mCurrentSelectYear, mCurrentSelectMonth, year, month);
        int position = mcvCalendar.getCurrentItem() + monthDis;
        mcvCalendar.setCurrentItem(position);
        resetMonthViewDate(year, month, day, position);
    }

    /**
     * 重新加载月视图
     *
     * @param year
     * @param month
     * @param day
     * @param position
     */
    private void resetMonthViewDate(final int year, final int month, final int day, final int position) {
        if (mcvCalendar.getMonthViews().get(position) == null) {
            mcvCalendar.getMonthViews().get(position).postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetMonthViewDate(year, month, day, position);
                }
            }, 50);
        } else {
            mcvCalendar.getMonthViews().get(position).clickThisMonth(year, month, day);
        }
    }

    /**
     * 添加一个班休
     */
    public void addHistorykHint(List<HolidayBean> list) {
        if (mcvCalendar.getCurrentMonthView() != null)
            mcvCalendar.getCurrentMonthView().setHistoryHintList(list);
    }

    /**
     * 添加一个圆点提示
     *
     * @param day 月-日
     */
    public void addTaskHint(Integer day) {
        if (mcvCalendar.getCurrentMonthView() != null)
            mcvCalendar.getCurrentMonthView().addTaskHint(day);
    }

    /**
     * 添加一个圆点提示
     *
     * @param day 月-日
     */
    public void addTaskHint(List<Integer> day) {
        if (mcvCalendar.getCurrentMonthView() != null)
            mcvCalendar.getCurrentMonthView().setTaskHintList(day);
    }

    /**
     * 删除一个圆点提示
     *
     * @param day 月-日
     */
    public void removeTaskHint(Integer day) {
        if (mcvCalendar.getCurrentMonthView() != null)
            mcvCalendar.getCurrentMonthView().removeTaskHint(day);
    }

    /**
     * 获取当月班休及是否有日程
     *
     * @param type
     * @param year
     * @param month
     */
    private void initNetData(String type, int year, int month) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "rc:monthview");
        mParams.put("calid", type);
        mParams.put("year", year + "");
        mParams.put("month", month + "");
        VolleyFactory.instance().post(OrderCalendarActivity.this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
            @Override
            public void requestSucceed(String object) {
                CalendarBean calendarBean = JSONObject.parseObject(object, CalendarBean.class);
                List<HolidayBean> holidayBeen = new ArrayList<HolidayBean>();
                List<Integer> integers = new ArrayList<Integer>();
                for (int i = 0; i < calendarBean.getDayList().size(); i++) {
                    if (!TextUtils.isEmpty(calendarBean.getDayList().get(i).getFlag())) {
                        holidayBeen.add(new HolidayBean(i, Integer.parseInt(calendarBean.getDayList().get(i).getFlag())));
                    }
                    if ("1".equals(calendarBean.getDayList().get(i).getIsActive())) {
                        integers.add(i);
                    }
                }
                addTaskHint(integers);
                addHistorykHint(holidayBeen);
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }

    /**
     * 获取当天日程
     *
     * @param type
     * @param date
     */
    private void initCalendar(String type, String date) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "rc:dayevents");
        mParams.put("calid", type);
        mParams.put("date", date);
        VolleyFactory.instance().post(OrderCalendarActivity.this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
            @Override
            public void requestSucceed(String object) {
                CalendarDayEventBean calendarDayEventBean = JSONObject.parseObject(object, CalendarDayEventBean.class);
                mCurrentCalendar = calendarDayEventBean.getGroups();
                mAdapter = new OrderCalendarExpandableListAdapter(OrderCalendarActivity.this, mCurrentCalendar);
                mAdapter.setOnChildClickListener(OrderCalendarActivity.this);
                expandableListView.setAdapter(mAdapter);
                for (int i = 0; i < mCurrentCalendar.size(); i++) {
                    expandableListView.expandGroup(i);
                }
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }


    /**
     * 点击子布局
     *
     * @param groupPosition 组position
     * @param childPosition gridview的postion
     */
    @Override
    public void onChildClick(int groupPosition, int childPosition) {
        CalendarDayEventBean.GroupsBean.ListBean bean = mCurrentCalendar.get(groupPosition).getList().get(childPosition);
        if ("1".equals(bean.getEventType())) {
            Intent intent = new Intent(this, CalendarDetailActivity.class);
            intent.putExtra("EventId", bean.getEventId());
            intent.putExtra("date", mCurrentSelectYear + "-" + (mCurrentSelectMonth + 1) + "-" + mCurrentSelectDay);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SDK_WebView.class);
            intent.putExtra("title", "日程详情");
            intent.putExtra("url", bean.getEventUrl());
            intent.putExtra("isShowRight", true);
            startActivity(intent);
        }
    }

    @Override
    public void onCheckChildClick(int groupPosition, int childPosition) {

    }

}
