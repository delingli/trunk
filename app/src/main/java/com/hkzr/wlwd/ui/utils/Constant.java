package com.hkzr.wlwd.ui.utils;

/**
 * @author seek 951882080@qq.com
 * @version V1.0
 */


public interface Constant {

    String FILE_NAME_ROOT = "/wlwd/";
    String FILE_NAME_ERROR = FILE_NAME_ROOT + "errorLog/";
    String FILE_NAME_APK = FILE_NAME_ROOT + "apk/";
    String FILE_NAME_IMG = FILE_NAME_ROOT + "img/";
    // save path
    public final static String APK_PATH = "/wlwd/apk/";
    public static final String PHOTO_FILE_NAME = "icon.jpg";
    
    //activity 直接数据传输   key的定义
    String Type = "type";




    //activity 直接数据传输   value的定义
    String REGISTER = "1";//注册
    String FORGETPW = "2";//忘记密码
    String FIXPW = "3";//修改密码
    String ABOUT = "4";//关于
    String HELP = "5";//互助
    String DAYARRAY = "6";//日程
    
    
    //数据存储
    String SP_BASE = "wlwd";
    String SP_EXIT = "is_exit";
    String SP_IDENTITY = "current_identity";
    String SP_PERSONALINFO = "personal_info";
    String SP_USER = "user_data";
    String SP_ORDER = "order_id";

    String SP_ZH = "zhanghao";
    String SP_MM = "mima";
    String SP_BH = "fuwubianhao";


    String SP_EM = "EMail";
    String SP_MB = "MobilePhone";
    String SP_QQ = "QQNo";
    String SP_WX = "WXNo";
    String SP_OFF = "OfficePhone";
    
}
