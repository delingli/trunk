 /**  

* @author seek 951882080@qq.com  

* @version V1.0  

*/ 

package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

public class ScreenUtils {
	
	/**
	 * 获取屏幕尺寸 封装成一个坐标点
	 * @param context
	 * @return
	 */
	public static Point getScreenSize(Context context) {
		Point p = new Point();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		p.y = dm.heightPixels - getStatusHeight(context);
		p.x = dm.widthPixels;
		return p;
	}
	
	/**
	 * 获取状态栏高度
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			sbar = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(sbar);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sbar;
	}

}
