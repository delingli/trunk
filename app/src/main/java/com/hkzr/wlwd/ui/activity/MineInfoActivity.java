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

import butterknife.Bind;
import butterknife.OnClick;

public class MineInfoActivity extends BaseActivity {

    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.left_LL)
    LinearLayout leftLL;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.right_LL)
    LinearLayout rightLL;
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.iv_head)
    XCRoundRectImageView ivHead;
    @Bind(R.id.tv_yx)
    TextView tvYx;
    @Bind(R.id.tv_sj)
    TextView tvSj;
    @Bind(R.id.tv_qq)
    TextView tvQq;
    @Bind(R.id.tv_wx)
    TextView tvWx;
    @Bind(R.id.tv_zj)
    TextView tvZj;
    @Bind(R.id.tv_dw)
    TextView tvDw;
    @Bind(R.id.tv_bm)
    TextView tvBm;
    @Bind(R.id.tv_gw)
    TextView tvGw;
    @Bind(R.id.cb_1)
    CheckBox cb_1;
    PicturePicker picturePicker = null;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/icon.jpg";// temp
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store the big
    private String headPath = "";
    private File tempFile;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mine_info);
        //???????????????????????????????????????????????????
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


    @OnClick({R.id.left_LL, R.id.tv_yx, R.id.iv_head, R.id.tv_qq, R.id.tv_wx, R.id.tv_zj, R.id.tv_dw, R.id.tv_bm, R.id.tv_gw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_LL:
                finish();
                break;
            case R.id.tv_yx:
                showFixDialog("????????????", Constant.SP_EM);
                break;
            case R.id.iv_head:
                getPermission();
                picturePicker.showPickDialog(this);
                break;
            case R.id.tv_sj:
//                showFixDialog("????????????", Constant.SP_MB);
                toast("??????????????????");
                break;
            case R.id.tv_qq:
                showFixDialog("??????QQ", Constant.SP_QQ);
                break;
            case R.id.tv_wx:
                showFixDialog("????????????", Constant.SP_WX);
                break;
            case R.id.tv_zj:
                showFixDialog("????????????", Constant.SP_OFF);
                break;
            case R.id.tv_dw:
//                showFixDialog("????????????", tvDw);
                toast("??????????????????");
                break;
            case R.id.tv_bm:
//                showFixDialog("????????????", tvBm);
                toast("??????????????????");
                break;
            case R.id.tv_gw:
//                showFixDialog("????????????", tvGw);
                toast("??????????????????");
                break;
        }
    }


    //??????????????????,????????????
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
                // ???????????????????????????????????????
                Uri uri = data.getData();
                headPath = MiPictureHelper.cropHeadImg(this, uri);
            }
        } else if (requestCode == PicturePicker.PICK_SYSTEM_PHOTO && resultCode == RESULT_OK) {
            //????????????
            if (MiPictureHelper.hasSdcard()) {
                headPath = MiPictureHelper.cropHeadImg(this, UriUtils.getUri(this, picturePicker.getFile()));
            } else {
                toast("?????????");
            }

        } else if (requestCode == PicturePicker.PHOTO_REQUEST_CUT && resultCode == RESULT_OK) {
//                pictureUrl = getRealFilePath(this,
////                        imageUri);
//                String pickPath = headPath;  // ?????????????????????????????????
            bitmap = BitmapFactory.decodeFile(headPath);
            upHeadServer(GetImageStr(headPath));

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    AlertDialog alertDialog = null;

    /**
     * ?????????????????? ??????dialog
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
                            ToastUtil.t("??????????????????");
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
     * ???????????????textview
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
     * ???????????????textview  ?????????
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
     * ????????????
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
//                ToastUtil.t("??????????????????");
            }
        }, false, false);
    }

    //???????????????????????????????????????????????????????????????Base64????????????
    public String GetImageStr(String imgpath) {
        InputStream in = null;
        byte[] data = null;
        //????????????????????????
        try {
            in = new FileInputStream(imgpath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //???????????????Base64??????
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);//??????Base64????????????????????????????????? 
        return new String(Base64.encode(data, Base64.DEFAULT));//??????Base64?????????????????????????????????
    }


    /**
     * ??????????????????   ??????????????????
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
//                ToastUtil.t("??????????????????");
            }
        }, false, false);
    }

    /**
     * ??????????????????
     *
     * @param fixType  ???????????????
     * @param fixValue ????????????
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
//                ToastUtil.t("??????????????????");
            }
        }, false, false);
    }
}
