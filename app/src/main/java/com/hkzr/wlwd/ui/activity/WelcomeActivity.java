package com.hkzr.wlwd.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.base.BActivity;

/**
 * 启动页
 */
public class WelcomeActivity extends BActivity {

    //
//    @Bind(R.id.tv_ljty)
//    TextView tvLjty;
    private int recLen = 1;

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        App.flag = 0;
        handler.postDelayed(runnable, 1000);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            if (recLen == 3) {
                jumpTo(LoginActivity.class);
                finish();
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    //    @OnClick(R.id.tv_ljty)
//    public void onClick() {
//        handler.removeCallbacks(runnable);
//        jumpTo(LoginActivity.class);
//        finish();
//    }

}
