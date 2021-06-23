package com.hkzr.wlwd.ui.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hkzr.wlwd.R;


public class MoreCalendarPopWindow extends PopupWindow {
    RelativeLayout rl_setting;
    RelativeLayout add_dy;
    RelativeLayout add_look;
    RelativeLayout rl_list;
    RelativeLayout rl_del;
    RelativeLayout rl_del_work;
    RelativeLayout rl_updata;
    RelativeLayout rl_updata_work;
    TextView tv_del, tv_update;
    boolean isWork = false; //是否是事件

    @SuppressLint("InflateParams")
    public MoreCalendarPopWindow(final Activity context, boolean b) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.popupwindow_calendar, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);


        rl_setting = (RelativeLayout) content.findViewById(R.id.rl_setting);
        add_dy = (RelativeLayout) content.findViewById(R.id.add_dy);
        add_look = (RelativeLayout) content.findViewById(R.id.add_look);
        rl_list = (RelativeLayout) content.findViewById(R.id.rl_list);
        rl_del = (RelativeLayout) content.findViewById(R.id.rl_del);
        rl_updata = (RelativeLayout) content.findViewById(R.id.rl_updata);
        rl_updata_work = (RelativeLayout) content.findViewById(R.id.rl_updata_work);
        rl_del_work = (RelativeLayout) content.findViewById(R.id.rl_del_work);
        tv_del = (TextView) content.findViewById(R.id.tv_del);
        tv_update = (TextView) content.findViewById(R.id.tv_update);
        int visibility = b ? View.VISIBLE : View.GONE;
        int visibility2 = b ? View.GONE : View.VISIBLE;
        rl_setting.setVisibility(visibility);
//        add_dy.setVisibility(visibility);
        add_look.setVisibility(visibility);
//        rl_list.setVisibility(visibility);
        rl_del.setVisibility(visibility2);
        rl_updata.setVisibility(visibility2);
        rl_del_work.setVisibility(visibility2);
        rl_updata_work.setVisibility(visibility2);

        rl_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGo != null) {
                    mGo.setting();
                }
                MoreCalendarPopWindow.this.dismiss();
            }
        });
        add_dy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mGo != null) {
                    mGo.dingyue();
                }
                MoreCalendarPopWindow.this.dismiss();
            }

        });
        add_look.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mGo != null) {
                    mGo.look();
                }
                MoreCalendarPopWindow.this.dismiss();
            }

        });
        rl_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGo != null) {
                    mGo.list();
                }
                MoreCalendarPopWindow.this.dismiss();
            }
        });
        rl_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGo != null) {
                    mGo.del(isWork ? "1" : "2");
                }
                MoreCalendarPopWindow.this.dismiss();
            }
        });
        rl_del_work.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGo != null) {
                    mGo.del("1");
                }
                MoreCalendarPopWindow.this.dismiss();
            }
        });
        rl_updata.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGo != null) {
                    mGo.updata(isWork ? "1" : "2");
                }
                MoreCalendarPopWindow.this.dismiss();
            }
        });
        rl_updata_work.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGo != null) {
                    mGo.updata("1");
                }
                MoreCalendarPopWindow.this.dismiss();
            }
        });
    }

    public void GoneView(String[] str, boolean isCF) {
        isWork = !isCF;
        if ("0".equals(str[0])) {
            rl_del.setVisibility(View.GONE);
            rl_updata_work.setVisibility(View.GONE);
        } else {
            rl_del.setVisibility(View.VISIBLE);
            if (isCF) {
                tv_del.setText("删除系列");
                rl_del_work.setVisibility(View.VISIBLE);
            } else {
                tv_del.setText("删除");
                rl_del_work.setVisibility(View.GONE);
            }
        }
        if ("0".equals(str[1])) {
            rl_updata.setVisibility(View.GONE);
            rl_updata_work.setVisibility(View.GONE);
        } else {
            rl_updata.setVisibility(View.VISIBLE);
            if (isCF) {
                tv_update.setText("编辑系列");
                rl_updata_work.setVisibility(View.VISIBLE);
            } else {
                rl_updata_work.setVisibility(View.GONE);
                tv_update.setText("编辑");
            }
        }
        if ("0".equals(str[0]) && "0".equals(str[1])) {
            isCanShow = false;
        }
    }

    boolean isCanShow = true;

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (isCanShow) {
            if (!this.isShowing()) {
                // 以下拉方式显示popupwindow
                this.showAsDropDown(parent, 0, 0);
            } else {
                this.dismiss();
            }
        }
    }

    private Go mGo;

    public void setmGo(Go go) {
        this.mGo = go;
    }

    public interface Go {
        public void setting();

        public void dingyue();

        public void look();

        public void list();

        public void del(String flag);

        public void updata(String flag);

    }
}
