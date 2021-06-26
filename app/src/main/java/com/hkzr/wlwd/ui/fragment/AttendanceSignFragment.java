package com.hkzr.wlwd.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.listener.MyLocationListener;
import com.hkzr.wlwd.model.OrganizationQueryBean;
import com.hkzr.wlwd.model.SignCheckBean;
import com.hkzr.wlwd.model.SignSuccessBean;
import com.hkzr.wlwd.model.SignTodayListBean;
import com.hkzr.wlwd.ui.activity.CheckWorkAttendanceActivity;
import com.hkzr.wlwd.ui.activity.FieldSignActivity;
import com.hkzr.wlwd.ui.adapter.IHolder;
import com.hkzr.wlwd.ui.adapter.OpenAdapter;
import com.hkzr.wlwd.ui.base.BaseFragment;
import com.hkzr.wlwd.ui.utils.DialogUtil;
import com.hkzr.wlwd.ui.utils.TimeUtil;
import com.hkzr.wlwd.ui.view.MyListView;
import com.hkzr.wlwd.ui.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 考勤签到
 */

public class AttendanceSignFragment extends BaseFragment {

    MyListView lvSign;
    CircleImageView ivIcon;
    TextView tvName;
    TextView tvKqz;
    TextView tvDate;
    LinearLayout ll;
    View viewTop;
    TextView tvAnniu;
    TextView tvTime;
    LinearLayout llAnniu;
    ImageView ivTip;
    TextView tvTip;
    TextView tv_state;
    TextView tv;
    RelativeLayout rl_top;

    List<String> list;
    OpenAdapter mMyAdapter;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;
    Receiver receiver;
    LatLng latLng;
    long mCurrentTime = 0;
    SignTodayListBean signTodayListBean;
    TimerThread timerThread;
    public static final String LOCATION = "com.hkzr.wlwd.ui.fragment.SignMapBroadcast";
    SignCheckBean signCheckBean;
    boolean isWai = false; //是否是外勤打卡
    CheckWorkAttendanceActivity activity;
    boolean isSign = false;//是否可以打卡
    List<SignTodayListBean.ListBean> listBean;
    private final static int REQUEST = 7025;
    boolean isCheck = true; //是否校验
    boolean isStop = false;

    @Override
    public int getViewId() {
        return R.layout.fragment_attendance_sign;
    }

    @Override
    public void initWidget(View parent) {
        findView(parent);
        rl_top.setFocusable(true);
        rl_top.setFocusableInTouchMode(true);
        rl_top.requestFocus();
        activity = (CheckWorkAttendanceActivity) getActivity();
        tvName.setText(mUserInfoCache.getUser().getUserCn());
        Picasso.with(getActivity()).invalidate(mUserInfoCache.getUser().getPhotoUrl());
        Picasso.with(getActivity()).load(mUserInfoCache.getUser().getPhotoUrl())/*.placeholder(R.drawable.morentouxiang)*/.error(R.drawable.morentouxiang).into(ivIcon);
        tvDate.setText(TimeUtil.getCurrentTime(TimeUtil.FORMAT_DATE2));
        list = new ArrayList<>();
        mMyAdapter = new OpenAdapter(list) {
            @Override
            public IHolder createHolder(int position) {
                return new AttendanceSignHolder();
            }
        };
        lvSign.setAdapter(mMyAdapter);
        myListener = new MyLocationListener(getActivity());
        mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        initData();
        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LOCATION);
        getActivity().registerReceiver(receiver, intentFilter);
        mLocationClient.start();
    }

    @Override
    public void findView(View parent) {
        lvSign = parent.findViewById(R.id.lv_sign);
        ivIcon = parent.findViewById(R.id.iv_icon);
        tvName = parent.findViewById(R.id.tv_name);
        tvKqz = parent.findViewById(R.id.tv_kqz);
        tvDate = parent.findViewById(R.id.tv_date);
        ll = parent.findViewById(R.id.ll);
        viewTop = parent.findViewById(R.id.view_top);
        tvAnniu = parent.findViewById(R.id.tv_anniu);
        tvTime = parent.findViewById(R.id.tv_time);
        llAnniu = parent.findViewById(R.id.ll_anniu);
        ivTip = parent.findViewById(R.id.iv_tip);
        tv_state = parent.findViewById(R.id.tv_state);
        tv = parent.findViewById(R.id.tv);
        rl_top = parent.findViewById(R.id.rl_top);
 parent.findViewById(R.id.ll_anniu).setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         if (isSign && !isWai) { //考勤打卡
             sign(null);
         } else if (isSign && isWai) { //外勤打卡
             Intent intent = new Intent(getActivity(), FieldSignActivity.class);
             intent.putExtra("time", mCurrentTime);
             startActivityForResult(intent, REQUEST);
         }
     }
 });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        isStop = true;
        getActivity().unregisterReceiver(receiver);
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
    }

    /**
     * 获取考勤记录
     */
    private void initData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:rec_daylist");
        VolleyFactory.instance().post(getActivity(), mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
            @Override
            public void requestSucceed(String object) {
                if (!TextUtils.isEmpty(object)) {
                    signTodayListBean = JSONObject.parseObject(object.toString(), SignTodayListBean.class);
                    setDataView();
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }


    @Override
    public void onPause() {
        super.onPause();
        isCheck = false;
    }

    String mCurrentWifiMac;
    String mCurrentWifiName;

    private void getWifiInfo() {
        WifiManager wifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        mCurrentWifiMac = info.getBSSID();
        mCurrentWifiName = info.getSSID();
    }


    /**
     * 获取考勤校验
     */
    private void initSignData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:check");
        mParams.put("coords", latLng.latitude + "," + latLng.longitude);
        if (!TextUtils.isEmpty(mCurrentWifiMac)) {
            mParams.put("wifi", mCurrentWifiMac);
        }
        VolleyFactory.instance().post(getActivity(), mParams, OrganizationQueryBean.class, new VolleyFactory.BaseRequest<OrganizationQueryBean>() {
            @Override
            public void requestSucceed(String object) {
                if (!TextUtils.isEmpty(object)) {
                    signCheckBean = JSONObject.parseObject(object.toString(), SignCheckBean.class);
                    if (signCheckBean.isSuccess()) {
                        isWai = false;
                        isSign = true;
                        llAnniu.setEnabled(true);
                        ivTip.setImageResource(R.drawable.chengong);
                        llAnniu.setBackgroundResource(R.drawable.anniu_blue);
                        if ("上班".equals(signTodayListBean.getInOut())) {
                            tvAnniu.setText("上班打卡");
                        } else if ("下班".equals(signTodayListBean.getInOut())) {
                            tvAnniu.setText("下班打卡");
                        }
                    } else {
                        ivTip.setImageResource(R.drawable.tixing);
                        if (signCheckBean.isOutCheck()) {
                            llAnniu.setEnabled(true);
                            isWai = true;
                            isSign = true;
                            llAnniu.setBackgroundResource(R.drawable.anniu_green);
                            tvAnniu.setText("外勤打卡");
                        } else {
                            llAnniu.setEnabled(false);
                            llAnniu.setBackgroundResource(R.drawable.anniu_off);
                            tvAnniu.setText("考勤打卡");
                        }
                    }
                    tvTip.setText(signCheckBean.getRemark());
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }


    private void setDataView() {
        if ("上班".equals(signTodayListBean.getInOut())) {
            tv_state.setText("上");
            tv.setText("上班打卡");
            tvAnniu.setText("上班打卡");
        } else if ("下班".equals(signTodayListBean.getInOut())) {
            tv_state.setText("下");
            tv.setText("下班打卡");
            tvAnniu.setText("下班打卡");
        }
        if (signTodayListBean.isIsAdmin()) {
            activity.changeSetting(true);
        } else {
            activity.changeSetting(false);
        }
        tvKqz.setText(signTodayListBean.getUnitName());
        mCurrentTime = TimeUtil.string2Long(signTodayListBean.getDate(), TimeUtil.FORMAT_DATE2_TIME);
        if (timerThread == null) {
            timerThread = new TimerThread();
            timerThread.start();
        }
        listBean = signTodayListBean.getList();
        if (listBean != null && listBean.size() > 0) {
            viewTop.setVisibility(View.VISIBLE);
            mMyAdapter.setModels(listBean);
            mMyAdapter.notifyDataSetChanged();
        } else {
            viewTop.setVisibility(View.INVISIBLE);
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == Activity.RESULT_OK) {
            initData();
        }
    }

    class AttendanceSignHolder implements IHolder<SignTodayListBean.ListBean> {
        TextView tv_state, tv_sign_time, tv_content, tv_change;
        View view_top, view_bottom;
        ImageView iv;

        @Override
        public View createConvertView(LayoutInflater inflater, ViewGroup parent) {
            View convertview = inflater.inflate(R.layout.item_sign_list, parent, false);
            view_top = convertview.findViewById(R.id.view_top);
            tv_state = (TextView) convertview.findViewById(R.id.tv_state);
            tv_sign_time = (TextView) convertview.findViewById(R.id.tv_sign_time);
            view_bottom = convertview.findViewById(R.id.view_bottom);
            iv = (ImageView) convertview.findViewById(R.id.iv);
            tv_content = (TextView) convertview.findViewById(R.id.tv_content);
            tv_change = (TextView) convertview.findViewById(R.id.tv_change);
            return convertview;
        }

        @Override
        public void bindModel(int position, final SignTodayListBean.ListBean bean) {
            view_bottom.setVisibility(View.VISIBLE);
            if (position == 0) {
                view_top.setVisibility(View.INVISIBLE);
            } else {
                view_top.setVisibility(View.VISIBLE);
            }
            String time = "";
            if ("上班".equals(bean.getInOut())) {
                tv_state.setText("上");
                time = "上班打卡时间";
            } else if ("下班".equals(bean.getInOut())) {
                tv_state.setText("下");
                time = "下班打卡时间";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.FORMAT_TIME);
            time += sdf.format(TimeUtil.string2Date(bean.getCheckTime(), TimeUtil.FORMAT_DATE2_TIME));
            tv_sign_time.setText(time);
            if ("3".equals(bean.getSignType()) || "5".equals(bean.getSignType())) {
                iv.setImageResource(R.drawable.sign_weizhi);
            } else if ("4".equals(bean.getSignType())) {
                iv.setImageResource(R.drawable.wifi);
            }
            tv_content.setText(bean.getAddr());
            if (position == listBean.size() - 1) {
                tv_change.setVisibility(View.VISIBLE);
            } else {
                tv_change.setVisibility(View.GONE);
            }
            tv_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSign && !isWai) { //考勤打卡
                        sign(bean.getAutoID());
                    } else if (isSign && isWai) { //外勤打卡
                        Intent intent = new Intent(getActivity(), FieldSignActivity.class);
                        intent.putExtra("time", mCurrentTime);
                        intent.putExtra("autoid", bean.getAutoID());
                        startActivityForResult(intent, REQUEST);
                    }
                }
            });
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(5000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            latLng = mUserInfoCache.getPoiInfo().location;
            handler.sendEmptyMessage(2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isCheck = true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mCurrentTime += 1000l;
                if (tvTime != null) {
                    tvTime.setText(TimeUtil.long2String(mCurrentTime, TimeUtil.FORMAT_TIME2));
                }
            } else if (msg.what == 2) {
                if (isCheck) {
                    getWifiInfo();
                    initSignData();
                }
            }
        }
    };


    /**
     * 显示时间
     */
    class TimerThread extends Thread {
        @Override
        public void run() {
            super.run();
            do {
                if (!isStop) {
                    try {
                        sleep(1000);
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    break;
                }
            } while (true);
        }
    }

    /**
     * 提交考勤(更新打卡)
     */
    private void sign(String autoid) {
        Map<String, String> mParams = new HashMap<String, String>();
        if (!TextUtils.isEmpty(autoid)) {
            mParams.put("autoid", autoid);
            mParams.put("t", "sign:rec_update");
        } else {
            mParams.put("t", "sign:rec_submit");
        }
        mParams.put("type", signCheckBean.getLocType());
        if ("3".equals(signCheckBean.getLocType())) {
            mParams.put("coords", latLng.latitude + "," + latLng.longitude);
            mParams.put("addr", mUserInfoCache.getPoiInfo().address);
        } else if ("4".equals(signCheckBean.getLocType())) {
            if (!TextUtils.isEmpty(mCurrentWifiMac)) {
                mParams.put("wifi", mCurrentWifiMac);
            }
        }
        VolleyFactory.instance().post(getActivity(), mParams, OrganizationQueryBean.class, new VolleyFactory.BaseRequest<OrganizationQueryBean>() {
            @Override
            public void requestSucceed(String object) {
                if (!TextUtils.isEmpty(object)) {
                    SignSuccessBean signSuccessBean = JSONObject.parseObject(object.toString(), SignSuccessBean.class);
                    showDialog(signSuccessBean);
                    initData();
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }

    private void showDialog(SignSuccessBean signSuccessBean) {
        View view = View.inflate(getActivity(), R.layout.item_dialog_succ, null);
        final Dialog dialog = DialogUtil.showDialogCenter(getActivity(), view, 250);
        TextView tv_tip = (TextView) view.findViewById(R.id.tv_tip);
        TextView tv_state = (TextView) view.findViewById(R.id.tv_state);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_iknow = (TextView) view.findViewById(R.id.tv_iknow);
        tv_tip.setText(signSuccessBean.getTipMsg());
        tv_state.setText(signSuccessBean.getInOut());
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.FORMAT_TIME);
        tv_time.setText(sdf.format(TimeUtil.string2Date(signSuccessBean.getCheckTime(), TimeUtil.FORMAT_DATE2_TIME)));
        tv_iknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
