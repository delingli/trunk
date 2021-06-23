package com.hkzr.wlwd.model;

/**
 * Created by born on 2017/5/13.
 */

public class LoginEntity {

    private String tokenid;

    private String key;

    private String userid;
    private String openid;

    public String getOpenId() {
        return openid;
    }

    public void setOpenId(String openId) {
        this.openid = openId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getTokenid() {
        return this.tokenid;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
