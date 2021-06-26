package com.hkzr.wlwd.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.SignTodayListBean;
import com.hkzr.wlwd.ui.adapter.MapAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapLocationActivity extends BaseActivity implements View.OnClickListener {
    TextView tvTitle;
    TextView tvRight;
    MapView mMapView;
    ImageView imageView;
    TextView tv_tip;
    View view;
    ListView Maplistview;
    BaiduMap baiduMap;
    //当前经纬度
    double[] mMsg;
    private int checkPosition = 0;
    List<PoiInfo> dataList;
    PoiInfo mCurentInfo;
    LocationClient locationClient;
    LatLng mCurrentLatLng;
    MapAdapter adapter;
    public static final int RESULT_CODE = 6;
    private boolean isFirst = true;
    GeoCoder mGeoCoder;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.location);
    int type = 0;
private void initViewBind(){
    tvTitle=   findViewById(R.id.tv_title);
    tvRight=   findViewById(R.id.tv_right);
    mMapView=   findViewById(R.id.map);
    imageView=   findViewById(R.id.imageView);
    tv_tip=   findViewById(R.id.tv_tip);
    view=   findViewById(R.id.view);
    Maplistview=   findViewById(R.id.lv_address);
    findViewById(R.id.tv_right).setOnClickListener(this);
    findViewById(R.id.left_LL).setOnClickListener(this);


}
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_rong_map);
        initViewBind();
        tvTitle.setText("地图");
        type = getIntent().getIntExtra("type", 0);

        dataList = new ArrayList<PoiInfo>();
        adapter = new MapAdapter(dataList, checkPosition, this);
        Maplistview.setAdapter(adapter);
        mMapView.showZoomControls(false);
        locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
        locationClient.registerLocationListener(myListener);    //注册监听函数
        baiduMap = mMapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setOnMapTouchListener(touchListener);
        if (type == 0) {
            tvRight.setText("发送");
            baiduMap.setMyLocationEnabled(true);
        } else {
            tvRight.setText("完成");
            baiduMap.setMyLocationEnabled(false);
        }

        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);
        initListener();
        initBaidu();
        setLocationOption();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                if (type == 0 || type == 2) {
                    Uri uri = Uri.parse("http://api.map.baidu.com/staticimage?width=500&height=330&center=" + mCurentInfo.location.longitude + "," + mCurentInfo.location.latitude + "&zoom=17&markers=" + mCurentInfo.location.longitude + "," + mCurentInfo.location.latitude + "&markerStyles=m,A");
                    Intent intent = new Intent();
                    intent.putExtra("latitude", mCurentInfo.location.latitude);
                    intent.putExtra("longitude", mCurentInfo.location.longitude);
                    intent.putExtra("address", mCurentInfo.address);
                    intent.putExtra("locuri", uri.toString());
                    setResult(RESULT_CODE, intent);
                    finish();
                } else if (type == 1) {
                    toAddLoc(mCurentInfo);
                }
                break;
            case R.id.left_LL:
                finish();
                break;
        }
    }

    private void initListener() {
        Maplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo p = dataList.get(position);
                mCurentInfo = p;
                mCurrentLatLng = new LatLng(p.location.latitude, p.location.longitude);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(mCurrentLatLng, 16);    //设置地图中心点以及缩放级别
                baiduMap.animateMapStatus(u);
                adapter.setSelectIndex(position);
            }
        });
    }

    private void initBaidu() {
        if (getIntent().hasExtra("location")) {
            mMsg = getIntent().getDoubleArrayExtra("location");
        }
        if (mMsg != null) {
            Maplistview.setVisibility(View.GONE);
            tvRight.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            mCurrentLatLng = new LatLng(mMsg[0], mMsg[1]);
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(mCurrentLatLng, 16);    //设置地图中心点以及缩放级别
//            baiduMap.animateMapStatus(u);
            MarkerOptions overlayOptions = new MarkerOptions().position(mCurrentLatLng)
                    .icon(bdA).zIndex(5);
            baiduMap.addOverlay(overlayOptions);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(mCurrentLatLng).zoom(16f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        locationClient.start(); // 开始定位
    }



    private void toAddLoc(PoiInfo poiInfo) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:loc_add");
        mParams.put("unitid", getIntent().getStringExtra("unitid"));
        if ("[当前位置]".equals(poiInfo.name)) {
            mParams.put("locname", poiInfo.address);
        } else {
            mParams.put("locname", poiInfo.name);
        }

        mParams.put("coords", poiInfo.location.latitude + "," + poiInfo.location.longitude);
        mParams.put("locaddr", poiInfo.address);
        VolleyFactory.instance().post(this, mParams, SignTodayListBean.class, new VolleyFactory.BaseRequest<SignTodayListBean>() {
            @Override
            public void requestSucceed(String object) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    /**
     * 设置定位参数
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 20 * 1000;
        option.setScanSpan(5000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
        /*.accuracy(location.getRadius())*/
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);    //设置定位数据
            if (isFirst && mMsg == null) {
                isFirst = false;
                mCurrentLatLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(mCurrentLatLng, 20);//设置地图中心点以及缩放级别
                baiduMap.animateMapStatus(u);
                // 发起反地理编码检索
                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                        .location(mCurrentLatLng));
            }
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        locationClient.stop();
        locationClient.unRegisterLocationListener(myListener);
        myListener = null;
        locationClient = null;
    }

    // 地图触摸事件监听器
    BaiduMap.OnMapTouchListener touchListener = new BaiduMap.OnMapTouchListener() {
        @Override
        public void onTouch(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // 获取当前MapView中心屏幕坐标对应的地理坐标
                LatLng currentLatLng;
                currentLatLng = baiduMap.getMapStatus().target;
                System.out.println("----" + currentLatLng.latitude);
                // 发起反地理编码检索
                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                        .location(currentLatLng));
                adapter.setSelectIndex(0);
            }
        }
    };
    // 地理编码监听器
    OnGetGeoCoderResultListener GeoListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检索到结果
            }
            // 获取地理编码结果
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有找到检索结果
            }
            // 获取反向地理编码结果
            else {
                // 当前位置信息
                mCurentInfo = new PoiInfo();
                mCurentInfo.address = result.getAddress();
                mCurentInfo.location = result.getLocation();
                mCurentInfo.name = "[当前位置]";
                dataList.clear();
                dataList.add(mCurentInfo);

                // 将周边信息加入表
                if (result.getPoiList() != null) {
                    dataList.addAll(result.getPoiList());
                }
                // 通知适配数据已改变
                adapter.notifyDataSetChanged();
       /* mLoadBar.setVisibility(View.GONE);*/

            }
        }
    };
}