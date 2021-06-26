/**
 * @author seek 951882080@qq.com
 * @version V1.0
 */

package com.hkzr.wlwd.ui.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.utils.AppManager;
import com.hkzr.wlwd.ui.utils.SystemBarTintManager;


public abstract class BActivity extends AppCompatActivity {
    protected RequestQueue queue = null;

    protected abstract void initView(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppManager.getAppManager().addActivity(this);
        queue = App.getInstance().getRequestQueue();
        initView(savedInstanceState);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initStatusBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setTintColor(getResources().getColor(
                    R.color.main_color));

//            tintManager.setTintColor(getResources().getColor(
//                    R.color.white_ffffff));
        }
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, config.getPixelInsetTop(false), 0,
                config.getPixelInsetBottom());
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
}
