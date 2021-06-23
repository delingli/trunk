package com.hkzr.wlwd.ui.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.hkzr.wlwd.R;

import java.util.Calendar;
import java.util.Date;


/**

 */
public class DialogUtil {


    /**
     * 在界面中间显示对话框
     */
    public static Dialog showDialogCenter(Context context, View v, int width) {
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(true);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
		 * 对象,这样这可以以同样的方式改变这个Activity的属性.
		 */
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        if (width != -1) {
            lp.width = DisplayUtil.dip2px(context, width);
        }
        dialogWindow.setGravity(Gravity.CENTER);
        return dialog;
    }

    /**
     * 在界面中间显示对话框
     */
    public static Dialog showDialogRight(Context context, View v, int width) {
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(true);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
		 * 对象,这样这可以以同样的方式改变这个Activity的属性.
		 */
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = DisplayUtil.getDensityHeight(context);
        if (width != -1) {
            lp.width = DisplayUtil.dip2px(context, width);
        }

        dialogWindow.setGravity(Gravity.RIGHT);

        return dialog;
    }

    public static Dialog showDialogConfirm(Context context, String title, String content,
                                           String leftBtn, final View.OnClickListener leftListener,
                                           String rightBtn, final View.OnClickListener rightListener, int Visibility) {
        View dialogView = View.inflate(context, R.layout.dialog_confirm, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView tvContent = (TextView) dialogView.findViewById(R.id.dialog_content);
        TextView dialog_left_btn = (TextView) dialogView.findViewById(R.id.dialog_left_btn);
        TextView dialog_right_btn = (TextView) dialogView.findViewById(R.id.dialog_right_btn);
        dialog_left_btn.setText(leftBtn);
        tvTitle.setText(title);
        tvContent.setText(content);
        dialog_right_btn.setText(rightBtn);
        dialog_right_btn.setVisibility(Visibility);
        dialog_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (rightListener != null) {
                    rightListener.onClick(v);
                }
            }
        });
        dialog_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (leftListener != null) {
                    leftListener.onClick(v);
                }
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                            dialog.dismiss();
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }
        );
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DisplayUtil.dip2px(context, 300);
        dialogWindow.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog showDialogWait(Context context, String content) {
        View dialogView = View.inflate(context, R.layout.dialog_wait, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
        TextView tvContent = (TextView) dialogView.findViewById(R.id.dialog_content);
        if (TextUtils.isEmpty(content)) {
            tvContent.setText("请稍等...");
        } else {
            tvContent.setText(content);
        }
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DisplayUtil.dip2px(context, 300);
        dialogWindow.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
//        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static TimePickerView showTime(Context c, TimePickerView.OnTimeSelectListener onTimeSelectListener) {
        TimePickerView pvTime = new TimePickerView.Builder(c, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(15)//滚轮文字大小
                .setLineSpacingMultiplier(3f)
                .setTitleSize(16)//标题文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(c.getResources().getColor(R.color.black_333333))//标题文字颜色
                .setSubmitColor(c.getResources().getColor(R.color.main_color))//确定按钮文字颜色
                .setCancelColor(c.getResources().getColor(R.color.main_color))//取消按钮文字颜色
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", null, null, null)//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();

        return pvTime;
    }

    /**
     * 选择时间日期
     *
     * @param c
     * @param onTimeSelectListener
     * @param b                    是否显示时间
     * @return
     */
    public static TimePickerView showDateTime(Context c, TimePickerView.OnTimeSelectListener onTimeSelectListener, boolean b) {
        TimePickerView pvTime = new TimePickerView.Builder(c, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(15)//滚轮文字大小
                .setLineSpacingMultiplier(3f)
                .setTitleSize(16)//标题文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(c.getResources().getColor(R.color.black_333333))//标题文字颜色
                .setSubmitColor(c.getResources().getColor(R.color.main_color))//确定按钮文字颜色
                .setCancelColor(c.getResources().getColor(R.color.main_color))//取消按钮文字颜色
                .setType(new boolean[]{true, true, true, b, b, false})
                .setLabel(null, null, null, null, null, null)//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();

        return pvTime;
    }

    /**
     * 选择时间日期
     *
     * @param c
     * @param onTimeSelectListener
     * @param b                    是否显示时间
     * @return
     */
    public static TimePickerView showDateTime(Context c, Date date, TimePickerView.OnTimeSelectListener onTimeSelectListener, boolean b) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        TimePickerView pvTime = new TimePickerView.Builder(c, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(15)//滚轮文字大小
                .setLineSpacingMultiplier(3f)
                .setDate(calendar)
                .setTitleSize(16)//标题文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(c.getResources().getColor(R.color.black_333333))//标题文字颜色
                .setSubmitColor(c.getResources().getColor(R.color.main_color))//确定按钮文字颜色
                .setCancelColor(c.getResources().getColor(R.color.main_color))//取消按钮文字颜色
                .setType(new boolean[]{true, true, true, b, b, false})
                .setLabel(null, null, null, null, null, null)//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();

        return pvTime;
    }

    public static Dialog showGroupDialogConfirm(Context context, String content,
                                                String leftBtn, final View.OnClickListener leftListener,
                                                String rightBtn, final View.OnClickListener rightListener) {
        View dialogView = View.inflate(context, R.layout.dialog_group_handle, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
        TextView tvContent = (TextView) dialogView.findViewById(R.id.tv_indication);
        TextView dialog_left_btn = (TextView) dialogView.findViewById(R.id.btn_ok);
        TextView dialog_right_btn = (TextView) dialogView.findViewById(R.id.btn_cancel);
        dialog_left_btn.setText(leftBtn);
        tvContent.setText(content);
        dialog_right_btn.setText(rightBtn);
        dialog_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (rightListener != null) {
                    rightListener.onClick(v);
                }
            }
        });
        dialog_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (leftListener != null) {
                    leftListener.onClick(v);
                }
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                            dialog.dismiss();
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }
        );
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.width = DisplayUtil.dip2px(context, 300);
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}

