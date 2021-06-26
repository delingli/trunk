package com.hkzr.wlwd.ui.activity;

import android.Manifest;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.model.PhotoUpdataBean;
import com.hkzr.wlwd.ui.adapter.ImageAdapter;
import com.hkzr.wlwd.ui.app.User;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.BitmapAndStringUtils;
import com.hkzr.wlwd.ui.utils.Constant;
import com.hkzr.wlwd.ui.utils.DialogUtil;
import com.hkzr.wlwd.ui.utils.ImageFactory;
import com.hkzr.wlwd.ui.utils.LookBigActivity;
import com.hkzr.wlwd.ui.utils.PicturePicker;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.utils.UriUtils;
import com.hkzr.wlwd.ui.view.MyGridView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 填写签到信息
 */

public class OutSignActivity extends BaseActivity implements View.OnClickListener {
    TextView tvTitle;
    TextView tvTime;
    TextView tvAddress;
    TextView tvPerson;
    EditText ed;
    MyGridView gvImage;
    TextView tvSubmit;


    String time = "";
    String address = "";
    String person = "";
    String personId = "";
    String latlngStr = "";
    List<Map<String, String>> list;
    //    ArrayList<Bitmap> btiList;
    ArrayList<String> pathList;
    String path;
    Dialog dialog;

    private void initViewBind() {
        tvTitle = findViewById(R.id.tv_title);
        tvTime = findViewById(R.id.tv_time);
        tvAddress = findViewById(R.id.tv_address);
        tvPerson = findViewById(R.id.tv_person);
        ed = findViewById(R.id.ed);
        gvImage = findViewById(R.id.gv_image);
        tvSubmit = findViewById(R.id.tv_submit);
        findViewById(R.id.left_LL).setOnClickListener(this);
        findViewById(R.id.tv_submit).setOnClickListener(this);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_out_sign);
        initViewBind();
        tvTitle.setText("外勤签到");
        time = getIntent().getStringExtra("time");
        address = getIntent().getStringExtra("address");
        person = getIntent().getStringExtra("person");
        personId = getIntent().getStringExtra("personId");
        latlngStr = getIntent().getStringExtra("latlng");
        list = new ArrayList<>();
        pathList = new ArrayList<>();
//        btiList = new ArrayList<>();
        dialog = DialogUtil.showDialogWait(this, "提交中...");
        initData();
        initListener();
    }

    private void initListener() {
        imageAdapter.setOnDelListener(new ImageAdapter.OnDelListener() {
            @Override
            public void onDel(int position) {
//                btiList.remove(position);
                pathList.remove(position);
                toImageList();
            }
        });
        gvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).containsKey("defult")) {
                    getPermission();
                    initCamera();
                } else {
                    Intent intent = new Intent(OutSignActivity.this, LookBigActivity.class);
                    intent.putStringArrayListExtra("image_list", pathList);
                    intent.putExtra("i", position);
                    startActivity(intent);
                }
            }
        });

    }

    ImageAdapter imageAdapter;

    private void initData() {
        tvTime.setText(time);
        tvAddress.setText(address);
        tvPerson.setText(person);
        toImageList();
    }

    private void toImageList() {
        list.clear();
        for (int i = 0; i < pathList.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("bitmap", pathList.get(i));
            list.add(map);
        }
        Map<String, String> map = new HashMap<>();
        map.put("defult", null);
        list.add(map);
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(list, OutSignActivity.this);
            gvImage.setAdapter(imageAdapter);
        } else {
            imageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                this.finish();
                break;
            case R.id.tv_submit:
                dialog.show();
                if (pathList.size() > 0) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Message message = new Message();
                            message.what = 1;

                            Bitmap bitmap = null;
                            List<PhotoUpdataBean> photoUpdataBeen = new ArrayList<>();
                            for (String str : pathList) {
                                bitmap = ImageFactory.compressScale(BitmapFactory.decodeFile(str));
                                photoUpdataBeen.add(new PhotoUpdataBean(BitmapAndStringUtils.convertIconToString(bitmap), "jpg"));
                                bitmap.recycle();
                            }
                            message.obj = JSON.toJSONString(photoUpdataBeen);
                            handler.sendMessage(message);
                        }
                    }.start();
                } else {
                    submit("");
                }
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                submit(msg.obj.toString());
            }
        }
    };

    private void submit(String json) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("custcn", person);
        if (!TextUtils.isEmpty(personId)) {
            mParams.put("custno", personId);
        }
        mParams.put("coords", latlngStr);
        mParams.put("addr", address);
        mParams.put("t", "sign:outsign");
        String s = ed.getText().toString().trim();
        if (!TextUtils.isEmpty(s)) {
            mParams.put("remark", s + "");
        }
        if (pathList.size() > 0) {
            mParams.put("photos", json);
        }
        VolleyFactory.instance().post(OutSignActivity.this, mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                toast("签到成功");
                dialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void requestFailed() {
                dialog.dismiss();
            }
        }, false, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        handler.removeCallbacks(null);
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
        }
    }


    public void initCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        String str = format.format(new Date());
        path = str + Constant.PHOTO_FILE_NAME;
        File f = new File(Environment.getExternalStorageDirectory(),
                path);
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
                try {
                    File tempFile = new File(Environment.getExternalStorageDirectory(), path);
                    String pickPath = tempFile.getAbsolutePath();
//                    Bitmap bitmap = ImageFactory.compressScale(BitmapFactory.decodeFile(pickPath);
//                    btiList.add(bitmap);
                    pathList.add(pickPath);

                    toImageList();
                } catch (Exception e) {

                }

//                bitmapString = BitmapAndStringUtils.convertIconToString(bitmap);
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

}
