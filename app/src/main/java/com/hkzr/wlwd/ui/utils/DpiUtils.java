package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DpiUtils {
	public static String getDisplayMetrics(Context cx) {
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        str += "The absolute width:" + String.valueOf(screenWidth) + "pixels\n";
        str += "The absolute heightin:" + String.valueOf(screenHeight)
                        + "pixels\n";
        str += "The logical density of the display.:" + String.valueOf(density)
                        + "\n";
        str += "X dimension :" + String.valueOf(xdpi) + "pixels per inch\n";
        str += "Y dimension :" + String.valueOf(ydpi) + "pixels per inch\n";
        return str;
}

}
