package com.hkzr.wlwd.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.AddCalendarBean;
import com.hkzr.wlwd.model.CalendarTypeBean;
import com.hkzr.wlwd.model.GroupEntity;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.DialogUtil;
import com.hkzr.wlwd.ui.utils.TimeUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 添加日程
 */

public class AddCalendarActivity extends BaseActivity implements View.OnClickListener {
    TextView tvTitle;
    EditText tvCalendarTitle;
    CheckBox cb;
    TextView tvStartTime;
    TextView tvEndTime;
    TextView tvRemind;
    TextView tvRepeat;
    TextView tvAccount;
    EditText edAddress;
    EditText edRemarks;
    RadioGroup rg_gk;
    RadioButton rb_mr;
    RadioButton rb_sm;
    TextView tv_end_repeat;
    LinearLayout ll_end_repeat;
    View view_end_repeat;
    LinearLayout ll_account;
    TextView tvRight;
    LinearLayout ll_repeat;
    View view_repeat;

    private static final int REQUESTCODE = 0x1211;
    boolean isDay = false;//是否是全天
    Date startDate, endDate;
    int isPublic = 1;
    String[] RemindStr = new String[]{"不提醒", "活动开始前", "提前5分钟", "提前10分钟", "提前15分钟", "提前30分钟", "提前1小时", "提前2小时", "提前1天", "提前2天", "提示1周"};
    String[] RemindStr2 = new String[]{"不提醒", "当天(上午8时)", "当天(上午9时)", "提前1天(上午8时)", "提前1天(上午9时)", "提前2天(上午8时)", "提前2天(上午9时)"};

    String[] Remind = new String[]{"0", "1", "M5", "M10", "M15", "M30", "H1", "H2", "D1", "D2", "W1"};
    String[] Remind2 = new String[]{"0", ":H8", ":H9", ":D1:H8", ":D1:H9", ":D2:H8", ":D2:H9"};
    String[] Repeat = new String[]{"一次性活动", "每天", "每个工作日"};
    int RemindIndex = 1;//提醒
    int repeatIndex = 0;//重复
    String end_repeat = "";

    // 信息列表提示框
    private AlertDialog alertDialog; //选择日程弹框
    List<CalendarTypeBean> list;  //日程分类列表
    String[] calendarStr; //日程分类列表
    int calendarType = 0; //选择的日程index
    double latitude, longitude;
    String address;
    Date mCurrentdate;
    int type;
    AddCalendarBean addCalendarBean;
    String eventId;
    boolean isChange = true;
    String mListCurrentDate = ""; //当前选中日期

    private void initViewBind() {
        tvTitle = findViewById(R.id.tv_title);
        tvCalendarTitle = findViewById(R.id.tv_calendar_title);
        cb = findViewById(R.id.cb);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        tvRemind = findViewById(R.id.tv_remind);
        tvAccount = findViewById(R.id.tv_account);
        edAddress = findViewById(R.id.ed_address);
        edRemarks = findViewById(R.id.ed_remarks);
        rg_gk = findViewById(R.id.rg_gk);
        rb_mr = findViewById(R.id.rb_mr);
        rb_sm = findViewById(R.id.rb_sm);
        tv_end_repeat = findViewById(R.id.tv_end_repeat);
        ll_end_repeat = findViewById(R.id.ll_end_repeat);
        view_end_repeat = findViewById(R.id.view_end_repeat);
        ll_account = findViewById(R.id.ll_account);
        tvRight = findViewById(R.id.tv_right);
        ll_repeat = findViewById(R.id.ll_repeat);
        view_repeat = findViewById(R.id.view_repeat);

        findViewById(R.id.left_LL).setOnClickListener(this);
        findViewById(R.id.ll_start_time).setOnClickListener(this);
        findViewById(R.id.tv_select_address).setOnClickListener(this);
        findViewById(R.id.ll_end_time).setOnClickListener(this);
        findViewById(R.id.ll_remind).setOnClickListener(this);
        findViewById(R.id.ll_end_repeat).setOnClickListener(this);
        findViewById(R.id.ll_repeat).setOnClickListener(this);
        findViewById(R.id.ll_account).setOnClickListener(this);
        findViewById(R.id.right_LL).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        HideKeyboard(view);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.ll_start_time:
                /*选择日期时间*/
                DialogUtil.showDateTime(this, startDate, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (date.getTime() >= mCurrentdate.getTime()) {
                            startDate = date;
                            if (startDate.getTime() > endDate.getTime()) {
                                endDate = TimeUtil.long2Date(startDate.getTime() + 3600000, TimeUtil.FORMAT_DATE_TIME);
                            }
                            switchTime();
                        }
                    }
                }, !isDay).show();
                break;

            case R.id.ll_end_time:
                /*选择日期时间*/
                DialogUtil.showDateTime(this, endDate, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (date.getTime() >= mCurrentdate.getTime()) {
                            if (date.getTime() >= startDate.getTime()) {
                                endDate = date;
                                switchTime();
                            }
                        }

                    }
                }, !isDay).show();
                break;
            case R.id.ll_remind:
                bundle.putInt("type", isDay ? 3 : 0);
                bundle.putInt("index", RemindIndex);
                jumpTo(CalendarOptionActivity.class, bundle, 111);
                break;
            case R.id.ll_end_repeat:
                bundle.putInt("type", 2);
                bundle.putString("date", end_repeat);
                jumpTo(CalendarOptionActivity.class, bundle, 111);
                break;
            case R.id.ll_repeat:
                bundle.putInt("type", 1);
                bundle.putInt("index", repeatIndex);
                bundle.putString("date", TimeUtil.date2String(startDate, TimeUtil.FORMAT_DATE));
                jumpTo(CalendarOptionActivity.class, bundle, 111);
                break;
            case R.id.ll_account:
                alertDialog.show();
                break;
            case R.id.right_LL:
                checkData();
                break;
            case R.id.tv_select_address:
                Intent intent = new Intent(this, MapLocationActivity.class);
                intent.putExtra("type", 2);
                startActivityForResult(intent, REQUESTCODE);
                break;

        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_add_calendar);
        initViewBind();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        type = getIntent().getIntExtra("type", 0);
        mCurrentdate = new Date();
        tvRight.setText("保存");
        if (type == 0) {
            tvTitle.setText("日程新建");
            String date = getIntent().getStringExtra("date");
            Date d = TimeUtil.string2Date(date + " " + TimeUtil.getCurrentTime(TimeUtil.FORMAT_TIME), TimeUtil.FORMAT_DATE_TIME);
            startDate = d;
            endDate = TimeUtil.long2Date(startDate.getTime() + 3600000, TimeUtil.FORMAT_DATE_TIME);
            list = getIntent().getParcelableArrayListExtra("calendarList");
            initData();
        } else if (type == 1) { //系列
            tvTitle.setText("日程修改");
            addCalendarBean = getIntent().getParcelableExtra("calendar");
            eventId = addCalendarBean.getEventId();
            try {
                initDataView();
            } catch (Exception e) {

            }
        } else if (type == 2) { //事件修改
            tvTitle.setText("日程修改");
            addCalendarBean = getIntent().getParcelableExtra("calendar");
            eventId = addCalendarBean.getEventId();
            mListCurrentDate = getIntent().getStringExtra("date");
            try {
                initDataView();
            } catch (Exception e) {

            }
        }
        initListener();
        creatListAlertDialog();
    }

    /**
     * 修改页面
     */
    private void initDataView() {
        tvCalendarTitle.setText(addCalendarBean.getSubject());
        isDay = "1".equals(addCalendarBean.getIsDayEvent());
        cb.setChecked(isDay);
        startDate = TimeUtil.string2Date(addCalendarBean.getStartTime(), TimeUtil.FORMAT_DATE_TIME);
        endDate = TimeUtil.string2Date(addCalendarBean.getEndTime(), TimeUtil.FORMAT_DATE_TIME);
        switchTime();
        if ("1".equals(addCalendarBean.getIsPrivate())) {
            isPublic = 1;
            rb_mr.setChecked(true);
            rb_sm.setChecked(false);
        } else {
            isPublic = 2;
            rb_mr.setChecked(false);
            rb_sm.setChecked(true);
        }
        if (isDay) {
            List<String> list = Arrays.asList(Remind2);
            RemindIndex = list.indexOf(addCalendarBean.getAlertSet());
            tvRemind.setText(RemindStr2[RemindIndex]);
        } else {
            List<String> list = Arrays.asList(Remind);
            RemindIndex = list.indexOf(addCalendarBean.getAlertSet());
            tvRemind.setText(RemindStr[RemindIndex]);
        }


        ll_account.setEnabled(false);
        tvAccount.setText(addCalendarBean.getCalendarName());

        if (type == 2 && (TextUtils.isEmpty(addCalendarBean.getExcepType()) && !"0".equals(addCalendarBean.getRepeat()) || !TextUtils.isEmpty(addCalendarBean.getExcepType()))) {
            view_repeat.setVisibility(View.GONE);
            ll_repeat.setVisibility(View.GONE);
            String[] s1 = addCalendarBean.getStartTime().split(" ");
            String s = mListCurrentDate;
            if (s1 != null && s1.length == 2) {
                s += " " + s1[1];
            }
            startDate = TimeUtil.string2Date(s, TimeUtil.FORMAT_DATE_TIME);
            if (startDate.getTime() > endDate.getTime()) {
                endDate = TimeUtil.long2Date(startDate.getTime() + 3600000, TimeUtil.FORMAT_DATE_TIME);
            }
            switchTime();
        } else {
            repeatIndex = Integer.parseInt(addCalendarBean.getRepeat());
            switch (repeatIndex) {
                case 0:
                    tvRepeat.setText(Repeat[repeatIndex]);
                    break;
                case 1:
                case 2:
                    tvRepeat.setText(Repeat[repeatIndex]);
                    ll_end_repeat.setVisibility(View.VISIBLE);
                    view_end_repeat.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(addCalendarBean.getREndDate())) {
                        tv_end_repeat.setText("永不");
                    } else {
                        tv_end_repeat.setText(TimeUtil.String2String(addCalendarBean.getREndDate(), TimeUtil.FORMAT_DATE));
                    }
                    break;
                case 3:
                    tvRepeat.setText("每周" + TimeUtil.getweek(addCalendarBean.getStartTime()));
                    ll_end_repeat.setVisibility(View.VISIBLE);
                    view_end_repeat.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(addCalendarBean.getREndDate())) {
                        tv_end_repeat.setText("永不");
                    } else {
                        tv_end_repeat.setText(TimeUtil.String2String(addCalendarBean.getREndDate(), TimeUtil.FORMAT_DATE));
                    }
                    break;
                case 4:
                    tvRepeat.setText("每月" + addCalendarBean.getStartTime().split("-")[2] + "日");
                    ll_end_repeat.setVisibility(View.VISIBLE);
                    view_end_repeat.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(addCalendarBean.getREndDate())) {
                        tv_end_repeat.setText("永不");
                    } else {
                        tv_end_repeat.setText(TimeUtil.String2String(addCalendarBean.getREndDate(), TimeUtil.FORMAT_DATE));
                    }
                    break;
                case 5:
                    tvRepeat.setText("每年" + addCalendarBean.getStartTime().split("-")[1] + "月" + addCalendarBean.getStartTime().split("-")[2] + "日");
                    ll_end_repeat.setVisibility(View.VISIBLE);
                    view_end_repeat.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(addCalendarBean.getREndDate())) {
                        tv_end_repeat.setText("永不");
                    } else {
                        tv_end_repeat.setText(TimeUtil.String2String(addCalendarBean.getREndDate(), TimeUtil.FORMAT_DATE));
                    }
                    break;
            }
        }
        address = addCalendarBean.getLocation();
        edAddress.setText(address);
        if (!TextUtils.isEmpty(addCalendarBean.getCoords())) {
            try {
                String[] str = addCalendarBean.getCoords().split(",");
                longitude = Double.parseDouble(str[1]);
                latitude = Double.parseDouble(str[0]);
            } catch (Exception e) {
            }
        }
        edRemarks.setText(addCalendarBean.getDescription());
    }

    private void initListener() {
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDay = isChecked;
                RemindIndex = 1;
                if (isDay) {
                    tvRemind.setText(RemindStr2[RemindIndex]);
                } else {
                    tvRemind.setText(RemindStr[RemindIndex]);
                }
                switchTime();
            }
        });
        rg_gk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_mr) {
                    isPublic = 1;
                } else {
                    isPublic = 2;
                }
            }
        });
        edAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChange) {
                    latitude = 0;
                    longitude = 0;
                } else {
                    isChange = !isChange;
                }
            }
        });

    }

    private void switchTime() {
        if (isDay) {
            tvStartTime.setText(TimeUtil.date2String(startDate, TimeUtil.FORMAT_DATE));
            tvEndTime.setText(TimeUtil.date2String(endDate, TimeUtil.FORMAT_DATE));
        } else {
            tvStartTime.setText(TimeUtil.date2String(startDate, TimeUtil.FORMAT_DATE_TIME));
            tvEndTime.setText(TimeUtil.date2String(endDate, TimeUtil.FORMAT_DATE_TIME));
        }
    }

    /**
     * 添加页面
     */
    private void initData() {
        switchTime();
        tvRemind.setText("活动开始前");
        tvRepeat.setText("一次性活动");
        tvAccount.setText("我的日程");
        calendarStr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            calendarStr[i] = list.get(i).getCalTitle();
        }
    }


    //隐藏虚拟键盘
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 校验数据
     */
    private void checkData() {
        String title = tvCalendarTitle.getText().toString().trim();
        String address = edAddress.getText().toString().trim();
        String remarks = edRemarks.getText().toString().trim();
        String starTime = tvStartTime.getText().toString().trim();
        String endTime = tvEndTime.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            toast("请输入标题");
            return;
        }
        String latlng = null;
        if (longitude != 0 && latitude != 0) {
            latlng = latitude + "," + longitude;
        }
        String callid = "";
        if (list == null && addCalendarBean != null) {
            callid = addCalendarBean.getCalendarId();
        } else {
            callid = list.get(calendarType).getCalID();
        }
        String calendarTitle = tvAccount.getText().toString();

        AddCalendarBean addCalendarBean = new AddCalendarBean(eventId, callid, calendarTitle, title, (isDay ? 1 : 0) + "", 1 + "", starTime, endTime, repeatIndex + "", end_repeat, isDay ?
                Remind2[RemindIndex] : Remind[RemindIndex], address, latlng, remarks, isPublic + "");
        submit(addCalendarBean);
    }


    public void creatListAlertDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Light_dialog);
        alertBuilder.setItems(calendarStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                calendarType = index;
                tvAccount.setText(list.get(index).getCalTitle());
                alertDialog.dismiss();
            }
        });
        alertDialog = alertBuilder.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == 11) {
                RemindIndex = data.getIntExtra("index", 0);
                tvRemind.setText(data.getStringExtra("text"));
            } else if (resultCode == 12) {
                repeatIndex = data.getIntExtra("index", 0);
                tvRepeat.setText(data.getStringExtra("text"));
                if (repeatIndex != 0) {
                    ll_end_repeat.setVisibility(View.VISIBLE);
                    view_end_repeat.setVisibility(View.VISIBLE);
                    end_repeat = "";
                    tv_end_repeat.setText("永不");
                } else {
                    ll_end_repeat.setVisibility(View.GONE);
                    view_end_repeat.setVisibility(View.GONE);
                    end_repeat = "";
                }
            } else if (resultCode == 13) {
                if (data == null) {
                    tv_end_repeat.setText("永不");
                    end_repeat = "";
                } else {
                    end_repeat = data.getStringExtra("date");
                    tv_end_repeat.setText(end_repeat);
                }
            } else if (resultCode == 14) {
                RemindIndex = data.getIntExtra("index", 0);
                tvRemind.setText(data.getStringExtra("text"));
            }
        } else if (requestCode == REQUESTCODE && resultCode == MapLocationActivity.RESULT_CODE) {
            isChange = false;
            latitude = data.getDoubleExtra("latitude", 0.00);
            longitude = data.getDoubleExtra("longitude", 0.00);
            address = data.getStringExtra("address");
            edAddress.setText(address);
        }
    }

    /**
     * 提交
     */
    private void submit(AddCalendarBean addCalendarBean) {
        Map<String, String> mParams = new HashMap<String, String>();
        if (type == 0) {
            mParams.put("t", "rc:addevent");
        } else if (type == 1) {
            mParams.put("t", "rc:chgevent");
            mParams.put("flag", "2");
        } else if (type == 2) {
            mParams.put("t", "rc:chgevent");
            mParams.put("flag", "1");
            mParams.put("date", mListCurrentDate);
        }
        mParams.put("model", JSONObject.toJSONString(addCalendarBean));
        VolleyFactory.instance().post(AddCalendarActivity.this, mParams, GroupEntity.class, new VolleyFactory.BaseRequest<GroupEntity>() {
            @Override
            public void requestSucceed(String object) {
                Intent intent = new Intent();
                if (type == 0) {
                    toast("添加成功");
                } else {
                    toast("修改成功");
                    if (type == 2) {
                        AddCalendarBean bean = JSONObject.parseObject(object, AddCalendarBean.class);
                        intent.putExtra("id", bean.getEventId());

                    }
                }
                setResult(2, intent);
                finish();
            }

            @Override
            public void requestFailed() {
                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

}
