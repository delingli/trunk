package com.hkzr.wlwd.ui.utils;

import com.hkzr.wlwd.ui.app.UserInfoCache;

/**
 * Created by admin on 2017/6/21.
 */

public class StringUtils {
    public static String toUserid(String userid) {
        String bh = UserInfoCache.init().getBhStr();
        if (!userid.startsWith(bh)) {
            userid = bh + "_" + userid.replace("-", "");
        }
        return userid.toLowerCase();
    }

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    public static boolean compareSystemUserId(String userid) {
        String bh = UserInfoCache.init().getBhStr();
        if (userid.startsWith(bh.toLowerCase() + "@")) {
            return true;
        }
        return false;
    }
}
