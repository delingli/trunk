package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.SDK_WebView;
import com.hkzr.wlwd.httpUtils.ReqUrl;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.CalendarBean;
import com.hkzr.wlwd.model.CalendarDayEventBean;
import com.hkzr.wlwd.model.CalendarTypeBean;
import com.hkzr.wlwd.model.GroupEntity;
import com.hkzr.wlwd.ui.adapter.CalendarExpandableListAdapter;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.DialogUtil;
import com.hkzr.wlwd.ui.utils.TimeUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.widget.MoreCalendarPopWindow;
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


/**
 * 我的日程
 */

public class CalendarActivity extends BaseActivity implements PopupWindow.OnDismissListener, CalendarExpandableListAdapter.OnChildClickListener, MoreCalendarPopWindow.Go,View.OnClickListener {
   private TextView tvTitle;
    private  TextView tvMonth;
    private   TextView tvDay;
    private TextView tvYear;
    private MonthCalendarView mcvCalendar;
    private ImageView ivRight;
    private   TextView tv_today;
    private  MyExpandableListView expandableListView;
    List<CalendarDayEventBean.GroupsBean> mCurrentCalendar;
    CalendarExpandableListAdapter mAdapter;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    String type = "";
    ArrayList<CalendarTypeBean> adminCalsBeen = new ArrayList<>();
    OpenAdapter mMyAdapter;
    MoreCalendarPopWindow moreCalendarPopWindow;
    PopupWindow mPopupWindow; //标题点击弹出
    int checkIndex = 0;     //当前选中日程
    boolean isShow = false;  //是否显示popup

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.layout_calendar);
        initViewBind();
        mcvCalendar.setFocusable(true);
        mcvCalendar.setFocusableInTouchMode(true);
        mcvCalendar.requestFocus();
        moreCalendarPopWindow = new MoreCalendarPopWindow(this, true);
        moreCalendarPopWindow.setmGo(this);
        tvTitle.setText("我的日程");
        ivRight.setImageResource(R.drawable.web_more);
        initMonth();
        initCalendarType();
        initNetData(type, mCurrentSelectYear, mCurrentSelectMonth + 1);
    }

    private void initViewBind() {
        tvTitle = findViewById(R.id.tv_title);
        tvMonth = findViewById(R.id.tv_month);
        tvDay = findViewById(R.id.tv_day);
        tvYear = findViewById(R.id.tv_year);
        mcvCalendar = findViewById(R.id.mcvCalendar);
        ivRight = findViewById(R.id.iv_right);
        tv_today = findViewById(R.id.tv_today);
        expandableListView = findViewById(R.id.expandableListView);

        findViewById(R.id.rl_date).setOnClickListener(this);
        findViewById(R.id.left_LL).setOnClickListener(this);
        findViewById(R.id.right_LL).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);
        findViewById(R.id.tv_title).setOnClickListener(this);
        findViewById(R.id.tv_today).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
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
            case R.id.right_LL:
                moreCalendarPopWindow.showPopupWindow(ivRight);
                break;
            case R.id.tv_title:
                showPopupWindow();
                isShow = !isShow;
                if (isShow) {
                    mPopupWindow.showAsDropDown(tvTitle, 0, 0);
                } else {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.iv_add:
                Intent in = new Intent(this, AddCalendarActivity.class);
                in.putParcelableArrayListExtra("calendarList", adminCalsBeen);
                in.putExtra("date", mCurrentSelectYear + "-" + (mCurrentSelectMonth + 1) + "-" + mCurrentSelectDay);
                startActivityForResult(in, 1);
                break;
            case R.id.tv_today:
                String[] str = TimeUtil.getCurrentTime(TimeUtil.FORMAT_DATE).split("-");
                initData(Integer.parseInt(str[0]), Integer.parseInt(str[1]) - 1, Integer.parseInt(str[2]));
                break;
        }
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
        VolleyFactory.instance().post(CalendarActivity.this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
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
        VolleyFactory.instance().post(CalendarActivity.this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
            @Override
            public void requestSucceed(String object) {
                CalendarDayEventBean calendarDayEventBean = JSONObject.parseObject(object, CalendarDayEventBean.class);
                mCurrentCalendar = calendarDayEventBean.getGroups();
                mAdapter = new CalendarExpandableListAdapter(CalendarActivity.this, mCurrentCalendar);
                mAdapter.setOnChildClickListener(CalendarActivity.this);
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

    public void showPopupWindow() {
        View view = View.inflate(this, R.layout.popu_calendar_listview, null);
        ListView mPopListView = (ListView) view.findViewById(R.id.listview);
        mMyAdapter = new OpenAdapter(adminCalsBeen) {
            @Override
            public IHolder createHolder(int position) {
                return new MyHolder();
            }
        };
        mPopListView.setAdapter(mMyAdapter);
        mPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkIndex != position) {
                    checkIndex = position;
                    type = adminCalsBeen.get(position).getCalID();
                    tvTitle.setText(adminCalsBeen.get(position).getCalTitle());
                    initNetData(type, mCurrentSelectYear, mCurrentSelectMonth + 1);
                    initCalendar(type, mCurrentSelectYear + "-" + (mCurrentSelectMonth + 1) + "-" + mCurrentSelectDay);
                }
                mPopupWindow.dismiss();
            }
        });

        //获取组件的宽度
        int widths = tvTitle.getMeasuredWidth();
        mPopupWindow = new PopupWindow(view, widths, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 设置popWindow背景颜色
        mPopupWindow.setFocusable(true); // 让popWindow获取焦点
        mPopupWindow.setOnDismissListener(this);
    }

    @Override
    public void onDismiss() {
        isShow = false;
    }


    /**
     * 获取日程列表
     */
    private void initCalendarType() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "rc:admincals");
        VolleyFactory.instance().post(CalendarActivity.this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
            @Override
            public void requestSucceed(String object) {
                List<CalendarTypeBean> calendarBean = JSONObject.parseArray(object, CalendarTypeBean.class);
                adminCalsBeen.clear();
                adminCalsBeen.add(new CalendarTypeBean("", "我的日程", ""));
                if (calendarBean != null && calendarBean.size() > 0) {
                    adminCalsBeen.addAll(calendarBean);
                } else {
                    tvTitle.setCompoundDrawables(null, null, null, null);
                    tvTitle.setEnabled(false);
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
            startActivityForResult(intent, 2);
        } else {
            Intent intent = new Intent(this, SDK_WebView.class);
            intent.putExtra("title", "日程详情");
            intent.putExtra("url", bean.getEventUrl());
            intent.putExtra("isShowRight", true);
            startActivity(intent);
        }
    }

    /**
     * 点击子布局中的check
     *
     * @param groupPosition 组position
     * @param childPosition gridview的postion
     */
    @Override
    public void onCheckChildClick(int groupPosition, int childPosition) {
        CalendarDayEventBean.GroupsBean.ListBean groupsBean = mCurrentCalendar.get(groupPosition).getList().get(childPosition);
        String type = "";
        if ("0".equals(groupsBean.getChecked())) {
            type = "1";
        } else if ("1".equals(groupsBean.getChecked())) {
            type = "0";
        } else {
            return;
        }
        toCheck(groupsBean.getEventId(), type, groupPosition, childPosition);

    }

    @Override
    public void setting() {
        Intent intent = new Intent(this, SDK_WebView.class);
        intent.putExtra("title", "日程设置");
        intent.putExtra("url", ReqUrl.getCalendarSetting(type));
        intent.putExtra("isShowRight", true);
        startActivity(intent);
    }

    @Override
    public void dingyue() {
        Intent intent = new Intent(this, SDK_WebView.class);
        intent.putExtra("title", "日程订阅");
        intent.putExtra("url", ReqUrl.getCalendarDingyue());
        intent.putExtra("isShowRight", true);
        startActivity(intent);
    }

    @Override
    public void look() {
        jumpTo(LookCalendarActivity.class);
    }

    @Override
    public void list() {
        Intent intent = new Intent(this, SDK_WebView.class);
        intent.putExtra("title", "日程列表");
        intent.putExtra("url", ReqUrl.getCalendarList(type));
        intent.putExtra("isShowRight", true);
        startActivity(intent);
    }

    @Override
    public void del(String flag) {

    }

    @Override
    public void updata(String flag) {

    }


    class MyHolder implements IHolder<CalendarTypeBean> {
        TextView tv;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_calendar, parent, false);
            tv = (TextView) convertview.findViewById(R.id.tv);
            return convertview;
        }

        @Override
        public void bindModel(int position, final CalendarTypeBean bean) {
            if (position == checkIndex) {
                tv.setSelected(true);
            } else {
                tv.setSelected(false);
            }
            tv.setText(bean.getCalTitle());
        }
    }

    private void toCheck(String id, final String type, final int groupPosition, final int childPosition) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "rc:chkevent");
        mParams.put("eventid", id);
        mParams.put("check", type);
        mParams.put("date", mCurrentSelectYear + "-" + (mCurrentSelectMonth + 1) + "-" + mCurrentSelectDay);
        VolleyFactory.instance().post(this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
            @Override
            public void requestSucceed(String object) {
                mCurrentCalendar.get(groupPosition).getList().get(childPosition).setChecked(type);
                expandableListView.collapseGroup(groupPosition);
                expandableListView.expandGroup(groupPosition);
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 || requestCode == 2) && resultCode == 2) {
            initNetData(type, mCurrentSelectYear, mCurrentSelectMonth + 1);
            initCalendar(type, mCurrentSelectYear + "-" + (mCurrentSelectMonth + 1) + "-" + mCurrentSelectDay);
        }
    }
}
