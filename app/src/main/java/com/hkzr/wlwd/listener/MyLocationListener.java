package com.hkzr.wlwd.listener;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.fragment.AttendanceSignFragment;
import com.hkzr.wlwd.ui.fragment.FieldFragment;

public class MyLocationListener implements BDLocationListener {
    private String address;
    private Context context;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        UserInfoCache user = UserInfoCache.init();
        StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        sb.append("\nsatellite1111 : ");
        sb.append(location.getCity());
        boolean locationSuccess = false;

        if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());// 单位：公里每小时
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());

            sb.append("\nheight : ");
            sb.append(location.getAltitude());// 单位：米
            sb.append("\ndirection : ");
            sb.append(location.getDirection());// 单位度
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            sb.append("\ndescribe : ");
            sb.append("gps定位成功");
            address = location.getCity() + "-" + location.getDistrict() + "-"
                    + location.getStreet();
            // sendLoginBroadCast(address);
            locationSuccess = true;
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            // 运营商信息
            sb.append("\noperationers : ");
            sb.append(location.getOperators());
            sb.append("\ndescribe : ");
            sb.append("网络定位成功");
            address = location.getCity() + "-" + location.getDistrict() + "-"
                    + location.getStreet();
            // sendLoginBroadCast(address);
            locationSuccess = true;
        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
            locationSuccess = true;
        } else if (location.getLocType() == BDLocation.TypeServerError) {
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        sb.append("\nlocationdescribe : ");
        sb.append(location.describeContents());// 位置语义化信息
//		List<Poi> list = location.get
//		if (list != null) {
//			sb.append("\npoilist size = : ");
//			sb.append(list.size());
//			for (Poi p : list) {
//				sb.append("\npoi= : ");
//				sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//			}
//		}

        Log.i("BaiduLocationApiDem", sb.toString());

        if (locationSuccess) {
            user.setLatitude(location.getLatitude());
            user.setLongitude(location.getLongitude());
            user.setBaiduAddress(address);
            App.getInstance().setCity(location.getCity());
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            PoiInfo info = new PoiInfo();
            info.address = location.getCity() + location.getDistrict() + location.getStreet();
            if (!TextUtils.isEmpty(location.getFloor())) {
                info.address += location.getFloor();
            }
            info.city = location.getCity();
            info.location = ll;
            info.name = location.getAddrStr();
            user.setPoiInfo(info);
            onReceive(context);
        }
    }

    public void onReceive(Context context) {
        Intent intent = new Intent();
        intent.setAction(FieldFragment.LOCATION);
        context.sendBroadcast(intent);
//        Intent intent2 = new Intent();
//        intent2.setAction(MapActivity.LOCATION);
//        context.sendBroadcast(intent2);
        Intent intent3 = new Intent();
        intent3.setAction(AttendanceSignFragment.LOCATION);
        context.sendBroadcast(intent3);

    }
}
