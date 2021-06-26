package com.hkzr.wlwd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;
import com.hkzr.wlwd.httpUtils.ReqUrl;
import com.hkzr.wlwd.model.ContactDB;
import com.hkzr.wlwd.model.ImgEntity;
import com.hkzr.wlwd.ui.activity.WelcomeActivity;
import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.base.BaseActivity;
import com.hkzr.wlwd.ui.fragment.ApplicationFragment;
import com.hkzr.wlwd.ui.fragment.ContactFragment;
import com.hkzr.wlwd.ui.fragment.HomeFragment;
import com.hkzr.wlwd.ui.fragment.PersonalFragment;
import com.hkzr.wlwd.ui.utils.SPUtil;
import com.hkzr.wlwd.ui.utils.ToastUtil;
import com.hkzr.wlwd.ui.utils.UpdataDialog;
import com.hkzr.wlwd.ui.widget.MorePopWindow;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;


import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * main page
 */
public class MainActivity extends BaseActivity implements MorePopWindow.Go,View.OnClickListener {
    ImageView title_left;
    TextView tv_title;
    ImageView ac_iv_search;
    ImageView seal_more;
    ImageView seal_refush;
    Button btn_mine;
    Button btn_home;

    Button btn_app;
    TextView btn_message;
    Button btn_contacts;
    FrameLayout fragmentContainer;
    TextView tv_count;

    RelativeLayout title;
    LinearLayout layoutMainBottom;
    View line1;
    private FragmentManager fm;
    private FragmentTransaction ft;
    /**
     * bottom tab button
     */
    private View[] btnTabs;
    /**
     * index of btnTabs
     */
    private int currentTabIndex;
    public static int index = 0;
    public final static String RECEIVED = "com.hkzr.wlwd.ui.MainActivity";
private void initViewBind(){
    title_left= findViewById(R.id.title_left);
    tv_title= findViewById(R.id.tv_title);
    ac_iv_search= findViewById(R.id.ac_iv_search);
    seal_more= findViewById(R.id.seal_more);
    seal_refush= findViewById(R.id.seal_refush);
    btn_mine= findViewById(R.id.btn_mine);
    btn_home= findViewById(R.id.btn_home);
    btn_app= findViewById(R.id.btn_app);
    btn_message= findViewById(R.id.btn_message);
    btn_contacts= findViewById(R.id.btn_contacts);
    fragmentContainer= findViewById(R.id.fragment_container);
    tv_count= findViewById(R.id.tv_count);
    title= findViewById(R.id.title);
    layoutMainBottom= findViewById(R.id.layout_main_bottom);
    line1= findViewById(R.id.line1);

    findViewById(R.id.btn_home).setOnClickListener(this);
    findViewById(R.id.btn_message).setOnClickListener(this);
    findViewById(R.id.btn_contacts).setOnClickListener(this);
    findViewById(R.id.btn_app).setOnClickListener(this);
    findViewById(R.id.btn_mine).setOnClickListener(this);
    findViewById(R.id.title_left).setOnClickListener(this);
    findViewById(R.id.ac_iv_search).setOnClickListener(this);
    findViewById(R.id.seal_more).setOnClickListener(this);
    findViewById(R.id.seal_refush).setOnClickListener(this);




}
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        initViewBind();
        fm = getSupportFragmentManager();
        btnTabs = new View[4];
        btnTabs[0] = btn_home;
        btnTabs[1] = btn_app;
        btnTabs[2] = btn_contacts;
        btnTabs[3] = btn_mine;
        btnTabs[0].setSelected(true);
        checkVerson();
        ReqUrl.ROT_URL = SPUtil.readString(MainActivity.this, "app", "root");
        initSlidingMenu();
        initTab();
    }


    SlidingMenu localSlidingMenu;

    private void initSlidingMenu() {
//        localSlidingMenu = new SlidingMenu(this);
//        localSlidingMenu.setMode(SlidingMenu.LEFT);//设置左右滑菜单
//        localSlidingMenu.setTouchModeAbove(SlidingMenu.LEFT);//设置要使菜单滑动，触碰屏幕的范围
//        localSlidingMenu.setBehindOffsetRes(R.dimen.shadow_width);//设置划出时主页面显示的剩余宽度
//        localSlidingMenu.setShadowWidthRes(R.dimen.swidth);
//        localSlidingMenu.setShadowDrawable(R.drawable.shadow);
//        localSlidingMenu.setFadeEnabled(true);//设置滑动时菜单的是否渐变     <span style="white-space:pre">              </span>localSlidingMenu.setFadeDegree(0.35F);//<span style="font-family: Helvetica, arial, freesans, clean, sans-serif;">设置</span>滑动时的渐变程度
//        localSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT, false);//使SlidingMenu附加在Activity右边
//        localSlidingMenu.setMenu(R.layout.layout_left);//设置menu的布局文件
//        localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
//            public void onOpened() {
//
//            }
//        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void protectApp() {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    ContactFragment contactfrag;
    PersonalFragment personalFrag;
    ApplicationFragment appfrag;
    HomeFragment homeFragment;
    private Fragment[] fragments;

    /**
     * init fragments handled by tab
     */
    private void initTab() {
        homeFragment = new HomeFragment();
        appfrag = new ApplicationFragment();
        contactfrag = new ContactFragment();
        personalFrag = new PersonalFragment();
        fragments = new Fragment[]{homeFragment, appfrag, contactfrag, personalFrag};
        ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, homeFragment);
        ft.show(homeFragment).commit();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getFlags() == 0x12345) {
            index = intent.getIntExtra("selectIndex", 1);
            SwitchSkip();
            SwitchTitle();
        }
    }

    private void checkVerson() {
        if (App.getInstance().isUpdataApp()) {
            UpdataDialog.showUpdataDialog(MainActivity.this, App.getInstance().getVersionCode(), App.getInstance().getDownloadUrl());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                index = 0;
                break;
            case R.id.btn_contacts:
                index = 2;
                break;
            case R.id.btn_app:
                index = 1;
                break;
            case R.id.btn_mine:
                index = 3;
                break;
            case R.id.title_left:
                localSlidingMenu.toggle();
                break;
            case R.id.ac_iv_search:
                break;
            case R.id.seal_more:
                MorePopWindow morePopWindow = new MorePopWindow(MainActivity.this);
                morePopWindow.setmGo(MainActivity.this);
                morePopWindow.showPopupWindow(seal_more);
                break;
        }
        SwitchSkip();
        SwitchTitle();
    }





    private void SwitchTitle() {
        switch (index) {
            case 0:
                tv_title.setText("首页");
                ac_iv_search.setVisibility(View.GONE);
                seal_more.setVisibility(View.VISIBLE);
                seal_refush.setVisibility(View.GONE);
                break;
//            case 1:
//                tv_title.setText("消息");
//                ac_iv_search.setVisibility(View.GONE);
//                seal_more.setVisibility(View.VISIBLE);
//                seal_refush.setVisibility(View.GONE);
//                break;

            case 1:
                tv_title.setText("应用");
                ac_iv_search.setVisibility(View.GONE);
                seal_more.setVisibility(View.VISIBLE);
                seal_refush.setVisibility(View.GONE);
                break;
            case 2:
                tv_title.setText("通讯录");
                ac_iv_search.setVisibility(View.GONE);
                seal_more.setVisibility(View.VISIBLE);
                seal_refush.setVisibility(View.GONE);
                break;
            case 3:
                tv_title.setText("我的");
                ac_iv_search.setVisibility(View.GONE);
                seal_more.setVisibility(View.GONE);
                seal_refush.setVisibility(View.GONE);
                break;
        }
    }

    private void SwitchSkip() {
        Log.e("pp", currentTabIndex + "+++" + index);
        ft = fm.beginTransaction();
        if (currentTabIndex != index) {
            if (!fragments[index].isAdded()) {
                ft.add(R.id.fragment_container, fragments[index]);
            }
            ft.hide(fragments[currentTabIndex]).show(fragments[index]).commit();
            btnTabs[currentTabIndex].setSelected(false);
            btnTabs[index].setSelected(true);
            currentTabIndex = index;
        }
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
//        if (localSlidingMenu.isMenuShowing()) {
//            localSlidingMenu.toggle();
//            return;
//        }
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.toast(this, "再按一次返回键关闭程序");
            exitTime = System.currentTimeMillis();
            return;
        }
        Process.killProcess(Process.myPid());    //获取PID
        System.exit(0);   //常规退出法，返回值为0代表正常退出
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShortcutBadger.removeCount(this);
        App.getInstance().cancleNotify();
    }


    @Override
    public void home() {
        homeFragment.setRfresh();
    }

    @Override
    public void application() {
        appfrag.setRfresh();
    }

    private List<ContactDB> list = new ArrayList<>();

    @Override
    public void contact() {
        contactfrag.getContact();
    }


    ImgEntity entity = null;


}