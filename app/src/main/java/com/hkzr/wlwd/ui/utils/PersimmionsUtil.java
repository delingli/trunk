package com.hkzr.wlwd.ui.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.app.App;


/**
 * 创    建:  lt  2018/2/23--13:44
 * 作    用:  权限申请相关
 * 注意事项:
 */

public class PersimmionsUtil {
    /**
     * 读写日程
     */
    public static final String DATE = Manifest.permission.READ_CALENDAR;
    /**
     * 相机
     */
    public static final String CAMERA = Manifest.permission.CAMERA;
    /**
     * 读写联系人和获取账户列表
     */
    public static final String PEOPLE = Manifest.permission.READ_CONTACTS;
    /**
     * 获取位置信息
     */
    public static final String LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    /**
     * 使用麦克风录制声音
     */
    public static final String AUDIO = Manifest.permission.RECORD_AUDIO;
    /**
     * 访问电话状态,打电话,获取和修改通话记录,允许使用视频电话,允许监视.修改或放弃播出电话
     */
    public static final String PHONE = Manifest.permission.READ_PHONE_STATE;
    /**
     * 读取和修改短信,发送短信,接收短信内容和短信广播,接收彩信
     */
    public static final String SMS = Manifest.permission.READ_SMS;
    /**
     * 读写sd卡
     */
    public static final String SD = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final int REQUEST_CODE_PERMISSION = 8132;

    private OnRequestPermissionCallBack mExecuteCallBack, cacheCallBack;

    //单例
    private static PersimmionsUtil myPersimmionsUtil;

    private PersimmionsUtil() {
    }

    public static PersimmionsUtil create() {
        if (myPersimmionsUtil == null) {
            synchronized (PersimmionsUtil.class) {
                if (myPersimmionsUtil == null)
                    myPersimmionsUtil = new PersimmionsUtil();
            }
        }
        return myPersimmionsUtil;
    }

    public interface OnRequestPermissionCallBack {
        /**
         * 权限全部申请成功
         */
        void onPermissionOk();

        /**
         * 有权限申请失败
         *
         * @param permission 权限名称,可以使用该权限名称直接去申请
         */
        void onPermissionError(String permission);

        /**
         * 申请的权限有的被"不在询问"了
         * <p>
         * 可以使用该类的showRemindDialog方法,输入为什么需要权限的描述信息,让用户手动赋予权限
         *
         * @param permission 权限名称
         */
        void onPermissionNotAsking(String permission);
    }

    public abstract class OnOkPermissionCallBack implements OnRequestPermissionCallBack {
        @Override
        public void onPermissionError(String permission) {
            ToastUtil.showToast(App.getInstance().getString(R.string.open_persimmion_error));
        }

        @Override
        public void onPermissionNotAsking(String permission) {
            ToastUtil.showToast(App.getInstance().getString(R.string.open_persimmion_error));
        }
    }

    /**
     * 判断是否已授权
     */
    public boolean isHavePermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 权限申请
     */
    public void requestPermission(Activity activity, OnRequestPermissionCallBack callBack, String... permissions) {
        mExecuteCallBack = callBack;
        StringBuilder sb = new StringBuilder();
        for (String s : permissions) {
            if (!isHavePermission(activity, s)) {
                //如果这条权限没有被申请
                sb.append(s + ",");
            }
        }
        if (StringUtils.isEmpty(sb)) {
            if (mExecuteCallBack != null) {
                cacheCallBack = mExecuteCallBack;
                mExecuteCallBack.onPermissionOk();
            }
            if (cacheCallBack == mExecuteCallBack) {//说明没有新的回调了,清除全部回调,以防发生奇怪事件
                mExecuteCallBack = null;
            } //还有新的回调,清除缓存回调
            cacheCallBack = null;
            return;
        }
        ActivityCompat.requestPermissions(activity, sb.toString().substring(0, sb.length() - 1).split(","), REQUEST_CODE_PERMISSION);
    }
    
    /**
     * Activity的onRequestPermissionsResult回调内调用
     */
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults == null || grantResults.length == 0) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION://结果可能成功可能失败
                for (int i = 0; i < ListUtil.getSize(grantResults); i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // 其中有权限申请失败
                        //先判断是否是被不在询问的
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                permissions[i])) {
                            if (mExecuteCallBack != null) {
                                cacheCallBack = mExecuteCallBack;
                                mExecuteCallBack.onPermissionNotAsking(permissions[i]);
                            }
                            if (cacheCallBack == mExecuteCallBack) {
                                mExecuteCallBack = null;
                            }
                            cacheCallBack = null;
                            return;
                        }
                        if (mExecuteCallBack != null) {
                            cacheCallBack = mExecuteCallBack;
                            mExecuteCallBack.onPermissionError(permissions[i]);
                        }
                        if (cacheCallBack == mExecuteCallBack) {
                            mExecuteCallBack = null;
                            cacheCallBack = null;
                        } else
                            cacheCallBack = null;
                        return;
                    }
                }
                // 权限全部申请成功
                if (mExecuteCallBack != null) {
                    cacheCallBack = mExecuteCallBack;
                    mExecuteCallBack.onPermissionOk();
                }
                if (cacheCallBack == mExecuteCallBack) {
                    mExecuteCallBack = null;
                }
                cacheCallBack = null;
                break;
            default://其他情况
                mExecuteCallBack = null;
                cacheCallBack = null;
                break;
        }
    }

    /**
     * 跳转到应用设置权限的页面
     * <p>
     * 并可以在onActivityResult方法中判断如果requestCode==REQUEST_CODE_PERMISSION,
     * 表示从设置界面回来了,可以再次进行判断是否拥有了权限
     *
     * @param detail 为什么需要该权限的描述
     */

    public void showRemindDialog(final Activity activity, String detail) {
        CommonDialogUtils dialog = new CommonDialogUtils(activity);
        dialog.setCancelable(false);
        dialog.setShowTitle(true);
        dialog.setCancleText("取消");
        dialog.setOkText("去设置");
        dialog.showDialog("权限申请", detail, new CommonDialogUtils.ClickListener() {
            @Override
            public void okClick() {
                PersimmionsUtil.create().startAppSettings(activity);
            }

            @Override
            public void cancleClick() {

            }
        });
    }

    /**
     * 跳转到系统自带的应用程序信息界面
     */
    @TargetApi(9)
    public void startAppSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, REQUEST_CODE_PERMISSION);
    }

    public static class CommonDialogUtils {
        private Context context;
        private String okText;
        private String cancleText;
        private boolean showMessage = true;
        private int titleColorResource;
        private int messageColorResource;
        private boolean autoFinish = true;
        private boolean cancelable = true;
        private boolean cancleOutSide = true;
        private boolean showCancleBt = true;
        private boolean showOkBt = true;
        private int okTextColor = 0;
        private int titleTextSize;
        TextView titleTv;
        Dialog dialog;
        boolean showTitle = true;
        TextView messageTv;

        public CommonDialogUtils(Context context) {
            this.context = context;
            okText = "确定";
            cancleText = "取消";
        }

        public void setShowTitle(boolean showTitle) {
            this.showTitle = showTitle;
        }

        public void setShowMessage(boolean showMessage) {
            this.showMessage = showMessage;
        }

        public void setAutoFinish(boolean autoFinish) {
            this.autoFinish = autoFinish;
        }

        public void setOkText(String okText) {
            this.okText = okText;
        }

        public void setCancleText(String cancleText) {
            this.cancleText = cancleText;
        }

        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }

        public void setCancelableOutSide(boolean cancelable) {
            this.cancleOutSide = cancelable;
        }

        public void setTitleTextColor(int titleColorResource) {
            this.titleColorResource = titleColorResource;
        }

        public void setMessageTextColor(int messageColorResource) {
            this.messageColorResource = messageColorResource;
        }


        public void showDialog(String title, String message, final ClickListener listener) {
            if (dialog != null) {
                if (!messageTv.getText().toString().equals(message)) {
                    messageTv.setText(message);
                }
                dialog.show();
                return;
            }
            dialog = new Dialog(context, R.style.customDialogStyle);
            View view = View.inflate(context, R.layout.dialog_finish, null);
            dialog.setContentView(view);
            titleTv = (TextView) view.findViewById(R.id.title_tv);
            messageTv = (TextView) view.findViewById(R.id.message);
            TextView cancleTv = (TextView) view.findViewById(R.id.cancel_tv);
            TextView okTv = (TextView) view.findViewById(R.id.positive_tv);
            if (titleColorResource != 0) {
                titleTv.setTextColor(context.getResources().getColor(titleColorResource));
            }
            if (messageColorResource != 0) {
                messageTv.setTextColor(context.getResources().getColor(messageColorResource));
            }
            if (!showTitle) {
                titleTv.setVisibility(View.GONE);
            }
            if (titleTextSize != 0) {
                titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
            }
            if (!showMessage) {
                messageTv.setVisibility(View.GONE);
            }
            if (!showOkBt) {
                okTv.setVisibility(View.GONE);
            }
            if (okTextColor != 0) {
                okTv.setTextColor(context.getResources().getColor(okTextColor));
            }
            if (!showCancleBt) {
                cancleTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(okText)) {
                okTv.setText(okText);
            }
            if (!TextUtils.isEmpty(cancleText)) {
                cancleTv.setText(cancleText);
            }
            if (title != null) {
                titleTv.setText(title);
            }
            if (message != null) {
                messageTv.setText(message);
            }

            if (okText == null) {
                okTv.setVisibility(View.GONE);
            }

            if (cancleText == null) {
                cancleTv.setVisibility(View.GONE);
            }

            okTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.okClick();
                    }
                    if (autoFinish) {
                        dialog.dismiss();
                    }
                }
            });

            cancleTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.cancleClick();
                    }
                    dialog.dismiss();
                }
            });

            dialog.setCancelable(cancelable);
//            dialog.setCanceledOnTouchOutside(cancleOutSide);
            dialog.show();

        }

        public void setShowCancleBt(boolean showCancleBt) {
            this.showCancleBt = showCancleBt;
        }

        public void setShowOkBt(boolean showOkBt) {
            this.showOkBt = showOkBt;
        }

        public void setOkTextColor(int colorResouce) {
            okTextColor = colorResouce;
        }

        public void setTitleTextSize(int size) {
            titleTextSize = size;
        }

        public interface ClickListener {
            void okClick();

            void cancleClick();
        }
    }
}
