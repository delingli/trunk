package com.hkzr.wlwd.httpUtils;

import com.hkzr.wlwd.ui.app.App;
import com.hkzr.wlwd.ui.app.UserInfoCache;

/**
 * 网络请求参数配置
 *
 * @author zl
 * @date 2016.1.22
 */
public class ReqUrl {

    /**
     * 外包公司的融云key
     */
//    public final static String RONG_KEY="tdrvipksrshf5";
    /**
     * 生产环境 接口根目录
     */
    public final static String RONG_KEY = "x4vkb1qpvbmhk";
    public static String ROT_URL = "http://oa.5lsoft.com/oa/unsafe/";//API根地址

    /**
     * 登录
     */
    public final static String login = "Login";


    /**
     * 1.1	云服务号验证
     */
    public final static String yunservice = "mobile.ashx";


    public final static String FirstRot = "http://yun.5lsoft.com/service/";

    /**
     * 关于我们
     */
    public final static String AboutUs = "/AboutUs.aspx?tokenId=" + UserInfoCache.init().getTokenid();
    /**
     * 系统帮助
     */
    public final static String SysHelp = "/SysHelp.aspx?tokenId=" + UserInfoCache.init().getTokenid();
    /**
     * 意见反馈
     */
    public final static String Advice = "/Advice.aspx?tokenId=" + UserInfoCache.init().getTokenid();
    /**
     * 注册
     */
    public final static String regist = "http://yun.5lsoft.com/service/UserReg.aspx?code=szth";
    /**
     * 工单
     */
    public final static String TaskMain = "/TaskMain.aspx?tokenId=" + UserInfoCache.init().getTokenid();
    /**
     * 审批
     */
    public final static String FlowMain = "/FlowMain.aspx?tokenId=" + UserInfoCache.init().getTokenid();
    /**
     * 考勤帮助
     */
    public final static String SysHelp_Sign = "/SysHelp.aspx?fun=kaoqin&tokenId=";

    /**
     * 考勤统计
     */
    public final static String KQReport = "/Workasp/KaoQin/KQReport.aspx?tokenId=";

    /**
     * 我的足迹
     */
    public final static String MyTracks = "/Workasp/KaoQin/MyTracks.aspx?tokenId=";
    /**
     * 签到帮助
     */
    public final static String SysHelp_qiandao = "/SysHelp.aspx?fun=qiandao&tokenId=";


    /**
     * 日程设置
     */
    public static String getCalendarSetting(String calendarId) {
        return App.getInstance().getH5Url() + "/Workasp/RiCheng/RcProfile.aspx?tokenId=" + UserInfoCache.init().getTokenid() + "&calId=" + calendarId;
    }

    /**
     * 日程订阅
     */
    public static String getCalendarDingyue() {
        return App.getInstance().getH5Url() + "/Workasp/RiCheng/RcSubscribe.aspx?tokenId=" + UserInfoCache.init().getTokenid();
    }

    /**
     * 日程列表
     */
    public static String getCalendarList(String calendarId) {
        return App.getInstance().getH5Url() + "/Workasp/RiCheng/RcListView.aspx?tokenId=" + UserInfoCache.init().getTokenid() + "&calId=" + calendarId;
    }
}
