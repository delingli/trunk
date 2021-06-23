/**
 * @Title: IntentHelper.java
 * @Package com.huake.pda.utils
 * @Description: TODO(��һ�仰�������ļ���ʲô)
 * @author seek 951882080@qq.com
 * @date 2015-11-15 ����2:59:30
 * @version V1.0
 */

package com.hkzr.wlwd.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 用于activity跳转的帮助类
 *
 * @author zl
 *
 */
public class IntentHelper {
    public static final int REQUEST_CODE = 0x00005;
    public static final String RESULT_DATA = "data";
    public static final String TITLE = "title";
    public static final String ISVRPLAYER = "isVrPlayer";
    public static final String TIT = "tit";
    public static final String INT = "INT";

    private IntentHelper() {
    }

    public static void jump(Context context, Class<?> target) {
        context.startActivity(new Intent(context, target));
    }


	public static void jumpForResult(Activity context, Class<?> target) {
		context.startActivityForResult(new Intent(context, target),
				REQUEST_CODE);
	}
	public static void jumpForResult(Activity context, Class<?> target,String newid) {
		context.startActivityForResult(new Intent(context, target).putExtra("newId",newid),
				REQUEST_CODE);
	}
	public static void jumpForResult(Activity context, Class<?> target,String  p,String c,boolean ispub) {
		context.startActivityForResult(new Intent(context, target).putExtra("PROVINCE",p).putExtra("CITY",c).putExtra("ISPUB",ispub),
				REQUEST_CODE);
	}
	public static void jumpForResult(Activity context, Class<?> target, int i) {
		context.startActivityForResult(new Intent(context, target), i);
	}


    public static void jump(Context context, Class<?> target, String title) {
        context.startActivity(new Intent(context, target)
                .putExtra(TITLE, title));
    }

    public static void jump(Context context, Class<?> target, String title, String tit) {
        context.startActivity(new Intent(context, target)
                .putExtra(TITLE, title).putExtra(TIT, tit));
    }

    public static void jump(Context context, Class<?> target, String title, String tit, int i) {
        context.startActivity(new Intent(context, target)
                .putExtra(TITLE, title).putExtra(TIT, tit).putExtra(INT, i));
    }

    public static void jump(Context context, Class<?> target, Bundle mBundle) {
        Intent intent = new Intent(context, target);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    public static void jump(Activity context, Class<?> target, String title, int i) {
        /*context.startActivity(new Intent(context, target)
				.putExtra(TITLE, title));*/
        Intent intent = new Intent(context, target);
        intent.putExtra(TITLE, title);
        context.startActivityForResult(intent, i);
        //context.startActivityFo(new Intent(context, target), i);
    }

    public static void jumpActivity(Activity context, Class<?> target, String title, int i) {
		/*context.startActivity(new Intent(context, target)
				.putExtra(TITLE, title));*/
        Intent intent = new Intent(context, target);
        intent.putExtra(TITLE, title);
        context.startActivityForResult(intent, i);
        //context.startActivityFo(new Intent(context, target), i);
    }
}
