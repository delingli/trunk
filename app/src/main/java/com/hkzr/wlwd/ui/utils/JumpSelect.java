package com.hkzr.wlwd.ui.utils;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.hkzr.wlwd.SDK_WebView;
import com.hkzr.wlwd.model.JumpCalendarBean;
import com.hkzr.wlwd.model.SystemMessageGroup;
import com.hkzr.wlwd.ui.MainActivity;
import com.hkzr.wlwd.ui.activity.AddFriendsActivity;
import com.hkzr.wlwd.ui.activity.CalendarActivity;
import com.hkzr.wlwd.ui.activity.CalendarDetailActivity;
import com.hkzr.wlwd.ui.activity.CheckWorkAttendanceActivity;
import com.hkzr.wlwd.ui.activity.ExternalContactsActivity;
import com.hkzr.wlwd.ui.activity.InsidContactActivity;
import com.hkzr.wlwd.ui.activity.OrganizationQueryActivity;
import com.hkzr.wlwd.ui.activity.OutsideSignActivity;
import com.hkzr.wlwd.ui.activity.UpdataPwdActivity;
import com.hkzr.wlwd.ui.activity.WaitVerifyActivity;
import com.hkzr.wlwd.ui.app.UserInfoCache;
import com.hkzr.wlwd.ui.productlist.ProductListActivity;
import com.hkzr.wlwd.ui.productwarehouse.ProductWareHouseActivity;
import com.hkzr.wlwd.ui.profilecheck.ProfileCheckActivity;
import com.hkzr.wlwd.zxing.android.CaptureActivity;


/**
 * Created by admin on 2017/6/13.
 */

public class JumpSelect {
    /**
     * 通过类型判断跳转页面
     *
     * @param context
     * @param FunType 类型  url  /app
     * @param FunLink url是为H5   app时为原生应用
     */
    public static void jump(Context context, String FunType, String FunLink, String... FunCode) {
        Intent intent = new Intent();
        if ("app".equals(FunType)) {
            switch (FunLink) {
                case "scancode": //扫一扫
                    intent.setClass(context, CaptureActivity.class);
                    break;
                case "checkin": //签到:
                    intent.setClass(context, CheckWorkAttendanceActivity.class);
                    break;
                case "outsign": //外勤签到:
                    intent.setClass(context, OutsideSignActivity.class);
                    break;
                case "schedule": //我的日程
                    intent.setClass(context, CalendarActivity.class);
                    break;
                case "addrlist": //通讯录
                    intent.setClass(context, MainActivity.class);
                    intent.putExtra("selectIndex", 1);
                    intent.setFlags(0x12345);
                    break;
                case "orglist": //组织查询
                    intent.setClass(context, OrganizationQueryActivity.class);
                    break;
                case "userlist": //内部联系人
                    intent.setClass(context, InsidContactActivity.class);
                    break;
                case "outlinklist": //外部联系人
                    intent.setClass(context, ExternalContactsActivity.class);
                    break;
                case "modifypwd": //修改密码
                    intent.setClass(context, UpdataPwdActivity.class);
                    break;
            }
            context.startActivity(intent);
        } else if ("url".equals(FunType)) {
            if (FunCode != null && FunCode.length > 0 && FunCode[0].equals(CaptureActivity.xcbf)) {
                intent.setClass(context, CaptureActivity.class);
                intent.putExtra(CaptureActivity.tag, CaptureActivity.xcbf);
                context.startActivity(intent);
            } else if (FunCode != null && FunCode.length > 0 && FunCode[0].equals(CaptureActivity.zljc)) {
                intent.setClass(context, ProductListActivity.class);
                context.startActivity(intent);
            } else if (FunCode != null && FunCode.length > 0 && FunCode[0].equals(CaptureActivity.cprk)) {
                intent.setClass(context, ProductWareHouseActivity.class);
                context.startActivity(intent);
            } else {
                intent.setClass(context, SDK_WebView.class);
                if (FunLink.contains("{tokenid}")) {
                    FunLink = FunLink.replace("{tokenid}", UserInfoCache.init().getTokenid());
                }
                intent.putExtra("url", FunLink);
                context.startActivity(intent);
            }
        } else if ("friend_apply".equals(FunType)) {
            intent.setClass(context, AddFriendsActivity.class);
            context.startActivity(intent);
        } else if ("joingroup_apply".equals(FunType)) {
            SystemMessageGroup systemMessageGroup = JSONObject.parseObject(FunLink, SystemMessageGroup.class);
            intent.putExtra("groupId", systemMessageGroup.getGroupId());
            intent.setClass(context, WaitVerifyActivity.class);
            context.startActivity(intent);
        } else if ("schedule_alert".equals(FunType)) {
            JumpCalendarBean bean = JSONObject.parseObject(FunLink, JumpCalendarBean.class);
            intent.putExtra("EventId", bean.getEventId());
            intent.putExtra("date", bean.getDate());
            intent.setClass(context, CalendarDetailActivity.class);
            context.startActivity(intent);
        }

    }
}
