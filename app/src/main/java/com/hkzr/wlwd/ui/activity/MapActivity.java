package com.hkzr.wlwd.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.SignTodayListBean;
import com.hkzr.wlwd.ui.adapter.MapAdapter;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 地图微调
 */

public class MapActivity extends BaseActivity implements OnGetPoiSearchResultListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.map)
    MapView map;
    @Bind(R.id.lv_address)
    PullToRefreshListView lvAddress;
    int type = -1;// 1添加位置 2 地图微调
    public LocationClient mLocationClient = null;
    List<PoiInfo> dataList;
    int checkPosition = 0;
    int pageNum = 0;
    MapAdapter adapter;
    PoiSearch mPoiSearch;
    BaiduMap mBaiduMap;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.location);
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                adapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_map);
        tvTitle.setText("地图微调");
        tvRight.setText("完成");
        initListener();
        dataList = new ArrayList<PoiInfo>();
        adapter = new MapAdapter(dataList, checkPosition, this);
        type = getIntent().getIntExtra("type", -1);
        lvAddress.setAdapter(adapter);
        iniBaidu();
        initLocation();
        mLocationClient.start();
    }


    private void iniBaidu() {
        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mLocationClient = new LocationClient(this);     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mBaiduMap = map.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, bdA));
        //普通地图
        map.showZoomControls(false);
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

    public void showLocation(LatLng ll) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(ll.latitude)
                .longitude(ll.longitude).build();
        mBaiduMap.setMyLocationData(locData);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(16.5f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void initListener() {
        lvAddress.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        // 刷新数据时不允许滑动
        lvAddress.setScrollingWhileRefreshingEnabled(false);
        lvAddress.setPullToRefreshOverScrollEnabled(false);
        lvAddress.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                refreshView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放加载");
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "最后更新时间:" + TimeUtil.getTime());
                pageNum++;
                searchNeayBy(pageNum);
            }
        });
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkPosition = position - 1;
                adapter.setSelectIndex(checkPosition);
                showLocation(dataList.get(checkPosition).location);
            }
        });
    }

    @OnClick({R.id.left_LL, R.id.right_LL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.right_LL:
                if (dataList.get(checkPosition) != null) {
                    if (type == 1) {
                        toAddLoc(dataList.get(checkPosition));
                    } else if (type == 2) {
                        Intent intent = new Intent();
                        intent.putExtra("latitude", dataList.get(checkPosition).location.latitude);
                        intent.putExtra("longitude", dataList.get(checkPosition).location.longitude);
                        intent.putExtra("address", dataList.get(checkPosition).address);
                        setResult(Activity.RESULT_OK, intent);
                        this.finish();
                    }
                }
                break;
        }
    }

    private void toAddLoc(PoiInfo poiInfo) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "sign:loc_add");
        mParams.put("unitid", getIntent().getStringExtra("unitid"));
        mParams.put("locname", poiInfo.name);
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

    private void searchNeayBy(int index) {
        // POI初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();
        poiNearbySearchOption.keyword("公司");
        poiNearbySearchOption.location(new LatLng(mUserInfoCache.getLatitude(), mUserInfoCache.getLongitude()));
        poiNearbySearchOption.radius(100);  // 检索半径，单位是米
        poiNearbySearchOption.pageCapacity(20);  // 默认每页10条
        poiNearbySearchOption.pageNum(index);
        mPoiSearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        lvAddress.onRefreshComplete();
        // 获取POI检索结果
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            Toast.makeText(MapActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
//          mBaiduMap.clear();
            if (result != null) {
                if (result.getAllPoi() != null && result.getAllPoi().size() > 0) {
                    dataList.addAll(result.getAllPoi());
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        }
    }

 
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        map.onDestroy();
        map = null;
    }


    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null)
                return;
            PoiInfo info = new PoiInfo();
            info.address = location.getCity() + location.getDistrict() + location.getStreet();
            if (!TextUtils.isEmpty(location.getFloor())) {
                info.address += location.getFloor();
            }
            info.city = location.getCity();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            info.location = ll;
            info.name = location.getAddrStr();
            dataList.add(info);
            showLocation(ll);
            searchNeayBy(pageNum);
            mLocationClient.stop();

        }
    };
}
