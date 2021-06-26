/**
 * @author seek 951882080@qq.com
 * @version V1.0
 */

package com.hkzr.wlwd.ui.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.MainActivity;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.utils.AppManager;
import com.hkzr.wlwd.ui.utils.StatusBarUtil;

import java.util.Observable;
import java.util.Observer;


public class BaseFragmentActivity extends FragmentActivity implements Observer {
    protected RequestQueue queue = null;
    protected UserInfoCache mUserInfoCache = null;


    private boolean isStatusBar = true;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
//            FragmentManager manager = getSupportFragmentManager();
//            if (manager != null) {
//                manager.popBackStackImmediate(null, 1);
//            }
        }
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (App.flag == -1) {//flag为-1说明程序被杀掉
            protectApp();
        } else {
            AppManager.getAppManager().addActivity(this);
            mUserInfoCache = UserInfoCache.init();
            mUserInfoCache.addObserver(this);
            queue = App.getInstance().getRequestQueue();
            initView(savedInstanceState);
            StatusBarUtil.setStatusBarColor(BaseFragmentActivity.this, R.color.main_color);
//        StatusBarUtil.StatusBarLightMode(BaseActivity.this);
            if (isStatusBar) {
                StatusBarUtil.setStatusBarColor(BaseFragmentActivity.this, R.color.main_color);
            }
        }
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
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void update(Observable observable, Object data) {

    }
}
