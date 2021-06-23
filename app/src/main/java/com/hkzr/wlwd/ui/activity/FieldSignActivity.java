package com.hkzr.wlwd.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.PhotoUpdataBean;
import com.hkzr.wlwd.ui.app.User;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.BitmapAndStringUtils;
import com.hkzr.wlwd.ui.utils.Constant;
import com.hkzr.wlwd.ui.utils.ImageFactory;
import com.hkzr.wlwd.ui.utils.PicturePicker;
import com.hkzr.wlwd.ui.utils.TimeUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.utils.UriUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 外勤签到
 */

public class FieldSignActivity extends BaseActivity {
    @Bind(R.id.mapview)
    MapView mapview;
    @Bind(R.id.ll_location)
    LinearLayout llLocation;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.ed)
    EditText ed;
    @Bind(R.id.iv_xj)
    ImageView ivXj;
    @Bind(R.id.btn_field_sign)
    Button btnFieldSign;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    BaiduMap mBaiduMap;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.location);
    long mCurrenTime = 0;
    TimerThread timerThread;
    private String path;
    Bitmap bitmap = null;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/sign.jpg";// temp
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store the big
    private File tempFile;
    String pickPath;
    String bitmapString;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_field_sign);
        tvTitle.setText("外勤签到");
        initBaidu();
        mCurrenTime = getIntent().getLongExtra("time", 0);
        initData();
        timerThread = new TimerThread();
        timerThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
        mapview = null;
        isStop = true;
        timerThread.interrupt();
        timerThread = null;
    }

    private void initBaidu() {
        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mapview.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, bdA));
        //普通地图
        mapview.showZoomControls(false);
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

    LatLng latLng; //当前定位坐标
    String address;

    private void initData() {
        latLng = mUserInfoCache.getPoiInfo().location;
        address = mUserInfoCache.getPoiInfo().address;
        showLocation(latLng);
        tvAddress.setText(address);
    }


    @OnClick({R.id.iv_xj, R.id.btn_field_sign, R.id.left_LL, R.id.ll_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_xj:
                getPermission();
                initCamera();
                break;
            case R.id.btn_field_sign:
                sign();
                break;
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.ll_location:
                initData();
                break;
        }
    }

    private void sign() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("type", "5");
        mParams.put("coords", latLng.latitude + "," + latLng.longitude);
        mParams.put("addr", address);
        String s = ed.getText().toString().trim();
        String autoid = getIntent().getStringExtra("autoid");
        if (!TextUtils.isEmpty(autoid)) {
            mParams.put("autoid", autoid);
            mParams.put("t", "sign:rec_update");
        } else {
            mParams.put("t", "sign:rec_submit");
        }
        if (!TextUtils.isEmpty(s)) {
            mParams.put("remark", s + "");
        }
        if (!TextUtils.isEmpty(bitmapString)) {
            List<PhotoUpdataBean> photoUpdataBeen = new ArrayList<>();
            photoUpdataBeen.add(new PhotoUpdataBean(bitmapString, "jpg"));
            mParams.put("photos", JSON.toJSONString(photoUpdataBeen));
        }
        VolleyFactory.instance().post(FieldSignActivity.this, mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                toast("外勤打卡成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, true, false);
    }

    //获取访问相机,相册权限

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},
                    1);
        } else {
        }
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mCurrenTime += 1000l;
                btnFieldSign.setText(TimeUtil.long2String(mCurrenTime, TimeUtil.FORMAT_TIME2) + "外勤打卡");
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

    public void initCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(),
                Constant.PHOTO_FILE_NAME);
        if (f.exists()) {
            f.delete();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri fileUri = Uri.fromFile(f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, PicturePicker.PICK_SYSTEM_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PicturePicker.PICK_SYSTEM_PHOTO && resultCode == RESULT_OK) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        Constant.PHOTO_FILE_NAME);
                pickPath = tempFile.getAbsolutePath();
                bitmap = BitmapFactory.decodeFile(pickPath);
                bitmap = ImageFactory.compressScale(bitmap);
                ivXj.setImageBitmap(bitmap);
                bitmapString = BitmapAndStringUtils.convertIconToString(bitmap);
            } else {
                ToastUtil.toast(this, "未找到存储卡，无法存储照片！");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
    }
}
