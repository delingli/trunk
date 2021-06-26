package com.hkzr.wlwd.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.VolleyFactory;
import com.hkzr.wlwd.ui.app.User;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.utils.Constant;
import com.hkzr.wlwd.ui.utils.LogUtils;
import com.hkzr.wlwd.ui.utils.MiPictureHelper;
import com.hkzr.wlwd.ui.utils.PicturePicker;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.utils.UriUtils;
import com.hkzr.wlwd.ui.widget.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class MineInfoActivity extends BaseActivity implements View.OnClickListener{

    ImageView ivLeft;
    TextView tvLeft;
    LinearLayout leftLL;
    TextView tvTitle;
    ImageView ivRight;
    TextView tvRight;
    LinearLayout rightLL;
    RelativeLayout rlTitle;
    XCRoundRectImageView ivHead;
    TextView tvYx;
    TextView tvSj;
    TextView tvQq;
    TextView tvWx;
    TextView tvZj;
    TextView tvDw;
    TextView tvBm;
    TextView tvGw;
    CheckBox cb_1;
    PicturePicker picturePicker = null;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/icon.jpg";// temp
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store the big
    private String headPath = "";
    private File tempFile;

    private void initViewBind() {
        ivLeft = findViewById(R.id.iv_left);
        tvLeft = findViewById(R.id.tv_left);
        leftLL = findViewById(R.id.left_LL);
        tvTitle = findViewById(R.id.tv_title);
        ivRight = findViewById(R.id.iv_right);
        tvRight = findViewById(R.id.tv_right);
        rightLL = findViewById(R.id.right_LL);
        rlTitle = findViewById(R.id.rl_title);
        ivHead = findViewById(R.id.iv_head);
        tvYx = findViewById(R.id.tv_yx);
        tvSj = findViewById(R.id.tv_sj);
        tvQq = findViewById(R.id.tv_qq);
        tvWx = findViewById(R.id.tv_wx);
        tvZj = findViewById(R.id.tv_zj);
        tvDw = findViewById(R.id.tv_dw);
        tvBm = findViewById(R.id.tv_bm);
        tvGw = findViewById(R.id.tv_gw);
        cb_1 = findViewById(R.id.cb_1);
        findViewById(R.id.left_LL).setOnClickListener(this);
        findViewById(R.id.tv_yx).setOnClickListener(this);
        findViewById(R.id.iv_head).setOnClickListener(this);
        findViewById(R.id.tv_qq).setOnClickListener(this);
        findViewById(R.id.tv_wx).setOnClickListener(this);
        findViewById(R.id.tv_zj).setOnClickListener(this);
        findViewById(R.id.tv_dw).setOnClickListener(this);
        findViewById(R.id.tv_bm).setOnClickListener(this);
        findViewById(R.id.tv_gw).setOnClickListener(this);


    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mine_info);
        initViewBind();
        //初始化需要初始化的控件、赋值等操作
        initViewDatas();
    }

    private void initViewDatas() {
        tvTitle.setText(R.string.mine_grzl);
        picturePicker = PicturePicker.init(this);
        cb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fixInfo("HideMyInfo", "1");
                } else {
                    fixInfo("HideMyInfo", "0");
                }
            }
        });
        setInfo();
    }

    private void setInfo() {
        if (mUserInfoCache.getUser() != null) {
            User user = mUserInfoCache.getUser();
            tvYx.setText(user.getEMail());
            tvSj.setText(user.getMobilePhone());
            tvQq.setText(user.getQQNo());
            tvWx.setText(user.getWXNo());
            tvZj.setText(user.getOfficePhone());
            tvDw.setText(user.getCorpName());
            tvBm.setText(user.getDeptName());
            tvGw.setText(user.getPosName());
            Picasso.with(this).invalidate(mUserInfoCache.getUser().getPhotoUrl());
            Picasso.with(this).load(mUserInfoCache.getUser().getPhotoUrl())/*.placeholder(R.drawable.morentouxiang)*/.error(R.drawable.morentouxiang).into(ivHead);
            if ("1".equals(user.getHideMyInfo())) {
                cb_1.setChecked(true);
            } else {
                cb_1.setChecked(false);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                finish();
                break;
            case R.id.tv_yx:
                showFixDialog("修改邮箱", Constant.SP_EM);
                break;
            case R.id.iv_head:
                getPermission();
                picturePicker.showPickDialog(this);
                break;
            case R.id.tv_sj:
//                showFixDialog("修改手机", Constant.SP_MB);
                toast("暂不支持修改");
                break;
            case R.id.tv_qq:
                showFixDialog("修改QQ", Constant.SP_QQ);
                break;
            case R.id.tv_wx:
                showFixDialog("修改微信", Constant.SP_WX);
                break;
            case R.id.tv_zj:
                showFixDialog("修改座机", Constant.SP_OFF);
                break;
            case R.id.tv_dw:
//                showFixDialog("修改单位", tvDw);
                toast("暂不支持修改");
                break;
            case R.id.tv_bm:
//                showFixDialog("修改部门", tvBm);
                toast("暂不支持修改");
                break;
            case R.id.tv_gw:
//                showFixDialog("修改岗位", tvGw);
                toast("暂不支持修改");
                break;
        }
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


    Bitmap bitmap = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PicturePicker.PICK_TACK_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                // 得到修改之前的图片的全路径
                Uri uri = data.getData();
                headPath = MiPictureHelper.cropHeadImg(this, uri);
            }
        } else if (requestCode == PicturePicker.PICK_SYSTEM_PHOTO && resultCode == RESULT_OK) {
            //获取失败
            if (MiPictureHelper.hasSdcard()) {
                headPath = MiPictureHelper.cropHeadImg(this, UriUtils.getUri(this, picturePicker.getFile()));
            } else {
                toast("无图片");
            }

        } else if (requestCode == PicturePicker.PHOTO_REQUEST_CUT && resultCode == RESULT_OK) {
//                pictureUrl = getRealFilePath(this,
////                        imageUri);
//                String pickPath = headPath;  // 获取图片路径的方法调用
            bitmap = BitmapFactory.decodeFile(headPath);
            upHeadServer(GetImageStr(headPath));

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    AlertDialog alertDialog = null;

    /**
     * 修改个人信息 显示dialog
     *
     * @param titleStr
     * @param fixType
     */
    private void showFixDialog(String titleStr,
                               final String fixType) {
        alertDialog = new AlertDialog.Builder(this)
                .create();
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final Window window = alertDialog.getWindow();
        window.getDecorView().setBackgroundColor(this.getResources().getColor(R.color
                .transparency_0));
        window.setContentView(R.layout.all_dialog);
        TextView titleTv = (TextView) window.findViewById(R.id.text_title);
        final EditText etStr = (EditText) window.findViewById(R.id.et_str);

        titleTv.setText(titleStr);

        TextView textView = checkTv(fixType);
        etStr.setText(textView.getText());
        window.setLayout(
                window.getContext().getResources().getDisplayMetrics().widthPixels,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        TextView tvCommit = (TextView) window.findViewById(R.id.sure_t);
        TextView tvCancel = (TextView) window.findViewById(R.id.center_t);
        tvCommit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(etStr.getText())) {
                            fixInfo(fixType, etStr.getText().toString());


                        } else {
                            ToastUtil.t("内容不能为空");
                        }
                    }
                });
        tvCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });

        alertDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 判断是那个textview
     */
    private TextView checkTv(String tvType) {
        TextView tv = null;
        if (tvType.equals(Constant.SP_WX)) {
            tv = tvWx;
        } else if (tvType.equals(Constant.SP_QQ)) {
            tv = tvQq;
        } else if (tvType.equals(Constant.SP_OFF)) {
            tv = tvZj;
        } else if (tvType.equals(Constant.SP_EM)) {
            tv = tvYx;
        } else if (tvType.equals(Constant.SP_MB)) {
            tv = tvSj;
        }

        return tv;
    }

    /**
     * 判断是那个textview  并赋值
     */
    private void checkTvAndSetDatas(String tvType, String tvValue) {
        if (tvType.equals(Constant.SP_WX)) {
            mUserInfoCache.getUser().setWXNo(tvValue);
            tvWx.setText(tvValue);
        } else if (tvType.equals(Constant.SP_QQ)) {
            mUserInfoCache.getUser().setQQNo(tvValue);
            tvQq.setText(tvValue);
        } else if (tvType.equals(Constant.SP_OFF)) {
            mUserInfoCache.getUser().setOfficePhone(tvValue);
            tvZj.setText(tvValue);
        } else if (tvType.equals(Constant.SP_EM)) {
            mUserInfoCache.getUser().setEMail(tvValue);
            tvYx.setText(tvValue);
        } else if (tvType.equals(Constant.SP_MB)) {
            mUserInfoCache.getUser().setMobilePhone(tvValue);
            tvSj.setText(tvValue);
        } else if (tvType.equals(Constant.SP_MB)) {
            mUserInfoCache.getUser().setMobilePhone(tvValue);
            tvSj.setText(tvValue);
        }

    }

    /**
     * 上传头像
     */
    private void upHeadServer(String phone) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "modifypic");
        mParams.put("userid", mUserInfoCache.getUserid());
        mParams.put("TokenId", mUserInfoCache.getTokenid());
        mParams.put("fmt", "jpg");
        mParams.put("photo", phone);

        VolleyFactory.instance().post(MineInfoActivity.this, mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                getPersonInfo();

            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }

    //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
    public String GetImageStr(String imgpath) {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgpath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);//返回Base64编码过的字节数组字符串 
        return new String(Base64.encode(data, Base64.DEFAULT));//返回Base64编码过的字节数组字符串
    }


    /**
     * 获取个人信息   方便及时刷新
     */
    private void getPersonInfo() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "userinfo");
        mParams.put("userid", mUserInfoCache.getUserid());

        VolleyFactory.instance().post(MineInfoActivity.this, mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                LogUtils.e(object.toString());
                User entity = JSON.parseObject(object.toString(), User.class);
                mUserInfoCache.setUser(entity);
                if (bitmap != null) {
                    ivHead.setImageBitmap(bitmap);
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }

    /**
     * 修改个人信息
     *
     * @param fixType  修改的位置
     * @param fixValue 修改的值
     */
    private void fixInfo(final String fixType, final String fixValue) {

        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("t", "modifyuser");
        mParams.put(fixType, fixValue);
        mParams.put("TokenId", mUserInfoCache.getTokenid());

        VolleyFactory.instance().post(MineInfoActivity.this, mParams, User.class, new VolleyFactory.BaseRequest<User>() {
            @Override
            public void requestSucceed(String object) {
                checkTvAndSetDatas(fixType, fixValue);
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }

            @Override
            public void requestFailed() {
//                ToastUtil.t("接口请求失败");
            }
        }, false, false);
    }
}
