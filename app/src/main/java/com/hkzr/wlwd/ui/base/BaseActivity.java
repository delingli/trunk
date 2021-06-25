/**
 * @author seek 951882080@qq.com
 * @version V1.0
 */

package com.hkzr.wlwd.ui.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.MainActivity;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.utils.AppManager;
import com.hkzr.wlwd.ui.utils.PersimmionsUtil;
import com.hkzr.wlwd.ui.utils.StatusBarUtil;

import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements Observer {
    protected RequestQueue queue = null;
    protected UserInfoCache mUserInfoCache = null;


    private boolean isStatusBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (App.flag == -1) {//flag为-1说明程序被杀掉
            protectApp();
        } else {
            AppManager.getAppManager().addActivity(this);
            mUserInfoCache = UserInfoCache.init();
            mUserInfoCache.addObserver(this);
            queue = App.getInstance().getRequestQueue();
            if (isStatusBar) {
                StatusBarUtil.setStatusBarColor(this, R.color.main_color);
            }
            initView(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    protected void initView(Bundle savedInstanceState) {

    }

    protected void protectApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//清空栈里MainActivity之上的所有activty
        startActivity(intent);
        finish();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public boolean isStatusBar() {
        return isStatusBar;
    }

    public void setIsStatusBar(boolean isStatusBar) {
        this.isStatusBar = isStatusBar;
    }


    /**
     * toast a message
     *
     * @param o
     */
    protected void toast(Object o) {
        if (o instanceof Integer) {
            Toast.makeText(this, (Integer) o, Toast.LENGTH_SHORT).show();
        } else if (o instanceof CharSequence) {
            Toast.makeText(this, (CharSequence) o, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * jump to target activity without bundle data
     *
     * @param cls
     */
    protected void jumpTo(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * jump to target activity without bundle data
     *
     * @param cls
     */
    protected void jumpTo(Class<?> cls, Bundle data) {
        startActivity(new Intent(this, cls).putExtras(data));
    }

    /**
     * jump to target activity without bundle data
     *
     * @param cls
     */
    protected void jumpTo(Class<?> cls, Bundle data, int requestCode) {
        startActivityForResult(new Intent(this, cls).putExtras(data), requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 申请权限
     */
    @TargetApi(23)
    public void getPersimmions() {
        PersimmionsUtil.create().requestPermission(this, new PersimmionsUtil.OnRequestPermissionCallBack() {
            @Override
            public void onPermissionOk() {

            }

            @Override
            public void onPermissionError(String permission) {
                getPersimmions();
            }

            @Override
            public void onPermissionNotAsking(String permission) {
                PersimmionsUtil.create().showRemindDialog(BaseActivity.this,
                        "在设置-应用-" + App.appName + "-权限中开启存储,相机和电话权限，以正常使用该应用");
            }
        }, PersimmionsUtil.SD, PersimmionsUtil.CAMERA, PersimmionsUtil.PHONE, PersimmionsUtil.LOCATION);
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PersimmionsUtil.create().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void update(Observable observable, Object data) {

    }

}
