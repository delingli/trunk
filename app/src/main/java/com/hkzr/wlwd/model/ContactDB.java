package com.hkzr.wlwd.model;

import org.litepal.crud.DataSupport;

/**
 * Created by born on 2017/6/5.
 */

public class ContactDB extends DataSupport {
    private String UserId;

    private String UserCn;// 姓名

    private String Sex;

    private String CorpName;

    private String DeptName;

    private String PosName;

    private String MobilePhone;

    private String PhotoUrl;

    private String  OpenId;

    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserCn() {
        return UserCn;
    }

    public void setUserCn(String userCn) {
        UserCn = userCn;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getCorpName() {
        return CorpName;
    }

    public void setCorpName(String corpName) {
        CorpName = corpName;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getPosName() {
        return PosName;
    }

    public void setPosName(String posName) {
        PosName = posName;
    }

}
