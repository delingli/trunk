package com.hkzr.wlwd.model;

/**
 * Created by born on 2017/5/26.
 */

public class InsidContactEntity {

    private String UserId;

    private String UserCn;// 姓名

    private String Sex;

    private String CorpName;

    private String DeptName;

    private String PosName;

    private String MobilePhone;

    private String PhotoUrl;

    private String sortLetters;  //显示数据拼音的首字母

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserId() {
        return this.UserId;
    }

    public void setUserCn(String UserCn) {
        this.UserCn = UserCn;
    }

    public String getUserCn() {
        return this.UserCn;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getSex() {
        return this.Sex;
    }

    public void setCorpName(String CorpName) {
        this.CorpName = CorpName;
    }

    public String getCorpName() {
        return this.CorpName;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    public String getDeptName() {
        return this.DeptName;
    }

    public void setPosName(String PosName) {
        this.PosName = PosName;
    }

    public String getPosName() {
        return this.PosName;
    }

    public void setMobilePhone(String MobilePhone) {
        this.MobilePhone = MobilePhone;
    }

    public String getMobilePhone() {
        return this.MobilePhone;
    }

    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }

    public String getPhotoUrl() {
        return this.PhotoUrl;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
