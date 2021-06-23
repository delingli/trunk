/**
 * @author seek 951882080@qq.com
 * @version V1.0
 */

package com.hkzr.wlwd.ui.app;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.search.core.PoiInfo;
import com.hkzr.wlwd.ui.utils.Constant;
import com.hkzr.wlwd.ui.utils.SPUtil;

import java.util.Observable;


public class UserInfoCache extends Observable {

    private static UserInfoCache mUserInfoCache = null;

    private User mUser;
    private String zhStr;//账号
    private String mmStr;//密码
    private String bhStr;//服务编号
    private String openid;//第三方使用的id
    private String tokenid;
    private String key;
    private String userid;
    private double Latitude;
    private double Longitude;
    private String BaiduAddress;
    private PoiInfo poiInfo;//定位当前位置的信息

    public PoiInfo getPoiInfo() {
        return poiInfo;
    }

    public void setPoiInfo(PoiInfo poiInfo) {
        this.poiInfo = poiInfo;
    }

    public String getBaiduAddress() {
        return BaiduAddress;
    }

    public void setBaiduAddress(String baiduAddress) {
        BaiduAddress = baiduAddress;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getUserid() {
        if (TextUtils.isEmpty(userid)) {
            String json = SPUtil.readString("app", "userid");
            if (!TextUtils.isEmpty(json)) {
                userid = json;
            }
        }

        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
        SPUtil.write("app", "userid", userid);
    }

    public String getTokenid() {
        if (TextUtils.isEmpty(tokenid)) {
            String json = SPUtil.readString("app", "tokenid");
            if (!TextUtils.isEmpty(json)) {
                tokenid = json;
            }
        }
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
        SPUtil.write("app", "tokenid", tokenid);
    }

    public String getKey() {
        if (TextUtils.isEmpty(key)) {
            String json = SPUtil.readString("app", "key");
            if (!TextUtils.isEmpty(json)) {
                key = json;
            }
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        SPUtil.write("app", "key", key);
    }

    public String getOpenId() {
        if (TextUtils.isEmpty(openid)) {
            String json = SPUtil.readString("app", "openid");
            if (!TextUtils.isEmpty(json)) {
                openid = json;
            }
        }
        return openid;
    }

    public void setOpenId(String openId) {
        this.openid = openId;
        SPUtil.write("app", "openid", openid);
    }

    public String getZhStr() {
        if (zhStr == null) {
            String json = SPUtil.readString(Constant.SP_BASE,
                    Constant.SP_ZH);
            if (!TextUtils.isEmpty(json)) {
                zhStr = json;
            }
        }
        return zhStr;
    }

    public void setZhStr(String zhStr) {
        this.zhStr = zhStr;
        SPUtil.write(Constant.SP_BASE, Constant.SP_ZH,
                zhStr);
    }

    public String getMmStr() {
        if (mmStr == null) {
            String json = SPUtil.readString(Constant.SP_BASE,
                    Constant.SP_MM);
            if (!TextUtils.isEmpty(json)) {
                mmStr = json;
            }
        }
        return mmStr;
    }

    public void setMmStr(String mmStr) {
        this.mmStr = mmStr;
        SPUtil.write(Constant.SP_BASE, Constant.SP_MM,
                mmStr);
    }

    public String getBhStr() {
        if (bhStr == null) {
            String json = SPUtil.readString(Constant.SP_BASE,
                    Constant.SP_BH);
            if (!TextUtils.isEmpty(json)) {
                bhStr = json;
            }
        }
        return bhStr;
    }

    public void setBhStr(String bhStr) {
        this.bhStr = bhStr;
        SPUtil.write(Constant.SP_BASE, Constant.SP_BH,
                bhStr);
    }

    public static synchronized UserInfoCache init() {
        if (mUserInfoCache == null) {
            mUserInfoCache = new UserInfoCache();
        }
        return mUserInfoCache;
    }


    private void hasDataBeEidting(Object obj) {
        this.setChanged();
        this.notifyObservers(obj);
    }


    public User getUser() {
        if (mUser == null) {
            String json = SPUtil.readString(Constant.SP_BASE,
                    Constant.SP_USER);
            if (!TextUtils.isEmpty(json)) {
                mUser = JSON.parseObject(json, User.class);
            }
        }
        return mUser;
    }


    public void setUser(User user) {
        this.mUser = user;
        SPUtil.write(Constant.SP_BASE, Constant.SP_USER,
                JSON.toJSONString(user));
    }


    public void setUser(String userJsonString) {
        SPUtil.write(Constant.SP_BASE, Constant.SP_USER,
                userJsonString);
        this.mUser = JSON.parseObject(userJsonString,
                User.class);
    }

    public void clearUser() {
        SPUtil.write(Constant.SP_BASE, Constant.SP_USER,
                "");
        SPUtil.write(Constant.SP_BASE, Constant.SP_PERSONALINFO,
                "");
        this.mUser = null;

    }

    /**
     * 记住密码
     *
     * @param zh
     * @param mm
     * @param bh
     */
    public void SaveCB(String zh, String mm, String bh) {
        setZhStr(zh);
        setMmStr(mm);
        setBhStr(bh);
    }

    /**
     * 清除记住
     */
    public void ClearCB() {
        setZhStr("");
        setMmStr("");
        setBhStr("");
    }


}
