package com.hkzr.wlwd.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.listener.MyLocationListener;
import com.hkzr.wlwd.model.OutsideSignBean;
import com.hkzr.wlwd.model.SignTodayListBean;
import com.hkzr.wlwd.ui.activity.ExternalContactsActivity;
import com.hkzr.wlwd.ui.activity.MapActivity;
import com.hkzr.wlwd.ui.activity.OutSignActivity;
import com.hkzr.wlwd.ui.base.BaseFragment;
import com.hkzr.wlwd.ui.utils.TimeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 外勤签到
 */

public class FieldFragment extends BaseFragment implements View.OnClickListener {
    TextView tvDate;
    TextView tvAddress;
    TextView tvMap;
    MapView mMapView;
    EditText edInfo;
    TextView tvAnniu;
    TextView tvTime;
    LinearLayout llAnniu;
    TextView tv_tip;
    LinearLayout llTip;
    BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.location);
    public static final String LOCATION = "com.hkzr.wlwd.ui.fragment.MyBroadcast";
    public static final int REQUESTCODE = 7026;
    public static final int SUBMIT_REQUESTCODE = 7028;
    public static final int EXTERNAL_CONTACTS_REQUESTCODE = 7027;
    long mCurrentTime;

    @Override
    public int getViewId() {
        return R.layout.fragment_field;
    }

    Receiver receiver;
    LatLng mCurrentLatlng;
    String mCurrentAddress;
    TimerThread timerThread;
    String custno;
    String custcn;
    boolean isClean = true;

    @Override
    public void findView(View parent) {
        tvDate=     parent.findViewById(R.id.tv_date);
        tvAddress=     parent.findViewById(R.id.tv_address);
        tvMap=     parent.findViewById(R.id.tv_map);
        mMapView=     parent.findViewById(R.id.map);
        edInfo=     parent.findViewById(R.id.ed_info);
        tvAnniu=     parent.findViewById(R.id.tv_anniu);
        tvTime=     parent.findViewById(R.id.tv_time);
        llAnniu=     parent.findViewById(R.id.ll_anniu);
        tv_tip=     parent.findViewById(R.id.tv_tip);
        llTip=     parent.findViewById(R.id.ll_tip);
        parent.findViewById(R.id.tv_map).setOnClickListener(this);
        parent.findViewById(R.id.tv_select).setOnClickListener(this);
        parent.findViewById(R.id.ll_anniu).setOnClickListener(this);

    }

    @Override
    public void initWidget(View parent) {
        findView(parent);
        myListener = new MyLocationListener(getActivity());
        mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mBaiduMap = mMapView.getMap();
        tvDate.setText(TimeUtil.getCurrentTime(TimeUtil.FORMAT_YEAR_MONTH_DAY));
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        initLocation();
        doBusiness();
        initData();
        initListener();

    }

    private void initListener() {
        edInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isClean) {
                    custno = "";
                    isClean = true;
                }
            }
        });
        //注册广播
        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LOCATION);
        getActivity().registerReceiver(receiver, intentFilter);
        mLocationClient.start();
    }

    private void initData() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:rec_dayout");
        VolleyFactory.instance().post(getActivity(), mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
            @Override
            public void requestSucceed(String object) {
                if (!TextUtils.isEmpty(object)) {
                    OutsideSignBean outsideSignBean = JSONObject.parseObject(object.toString(), OutsideSignBean.class);
                    if (TextUtils.isEmpty(outsideSignBean.getRemark())) {
                        llTip.setVisibility(View.GONE);
                    } else {
                        llTip.setVisibility(View.VISIBLE);
                        tv_tip.setText(outsideSignBean.getRemark());
                    }
                    if (timerThread == null) {
                        mCurrentTime = TimeUtil.string2Long(outsideSignBean.getDate(), TimeUtil.FORMAT_DATE2_TIME);
                        timerThread = new TimerThread();
                        timerThread.start();
                    }
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
        timerThread.interrupt();
        timerThread = null;
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 20 * 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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

    private void doBusiness() {
        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setAllGesturesEnabled(false);   //关闭一切手势操作
        settings.setOverlookingGesturesEnabled(false);//屏蔽双指下拉时变成3D地图
        settings.setZoomGesturesEnabled(false);//获取是否允许缩放手势返回:是否允许缩放手势
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, bdA));
        mMapView.showZoomControls(false);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    public void showLocation() {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(mCurrentLatlng.latitude)
                .longitude(mCurrentLatlng.longitude).build();
        mBaiduMap.setMyLocationData(locData);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(mCurrentLatlng).zoom(16.5f);
        tvAddress.setText(mCurrentAddress);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_map:
                intent.setClass(getActivity(), MapActivity.class);
                intent.putExtra("type", 2);
                startActivityForResult(intent, REQUESTCODE);
                break;
            case R.id.tv_select:
                intent.setClass(getActivity(), ExternalContactsActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, EXTERNAL_CONTACTS_REQUESTCODE);
                break;
            case R.id.ll_anniu:
                String ed = edInfo.getText().toString().trim();
                if (TextUtils.isEmpty(ed)) {
                    toast("请选择或输入要拜访的对象");
                } else if (TextUtils.isEmpty(mCurrentAddress)) {
                    toast("请先选择位置");
                } else {
                    custcn = ed;
                    intent.setClass(getActivity(), OutSignActivity.class);
                    intent.putExtra("time", TimeUtil.long2String(mCurrentTime, TimeUtil.FORMAT_TIME2));
                    intent.putExtra("address", mCurrentAddress);
                    intent.putExtra("latlng", mCurrentLatlng.latitude + "," + mCurrentLatlng.longitude);
                    intent.putExtra("person", custcn);
                    intent.putExtra("personId", custno);
                    startActivityForResult(intent, SUBMIT_REQUESTCODE);
                }
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE && resultCode == Activity.RESULT_OK) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            mCurrentLatlng = new LatLng(latitude, longitude);
            mCurrentAddress = data.getStringExtra("address");
            showLocation();
        } else if (requestCode == EXTERNAL_CONTACTS_REQUESTCODE && resultCode == Activity.RESULT_OK) {
            custno = data.getStringExtra("LinkID");
            custcn = data.getStringExtra("LinkName");
            isClean = false;
            edInfo.setText(custcn);
        } else if (requestCode == SUBMIT_REQUESTCODE && resultCode == Activity.RESULT_OK) {
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrentLatlng = mUserInfoCache.getPoiInfo().location;
            mCurrentAddress = mUserInfoCache.getPoiInfo().address;
            showLocation();
            mLocationClient.stop();
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                mCurrentTime += 1000l;
                tvTime.setText(TimeUtil.long2String(mCurrentTime, TimeUtil.FORMAT_TIME2));
            }
        }
    };
    boolean isStop = false;

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
                        handler.sendEmptyMessage(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    break;
                }
            } while (true);
        }
    }
}
