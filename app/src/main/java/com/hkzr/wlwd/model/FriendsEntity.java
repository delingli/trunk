package com.hkzr.wlwd.model;

/**
 * 好友信息
 */

public class FriendsEntity {

    /**
     * UserId : d6814e2a-937c-4919-ae8e-eeef527c6c0b
     * UserCn : admin51
     * Sex : 男
     * CorpName : 三建公司机关
     * DeptName : 测试项目部
     * PosName : 项目经理
     * OfficePhone :
     * MobilePhone :
     * EMail :
     * WXNo :
     * QQNo :
     * PhotoUrl : http://60.29.55.21/Mobile/mobile.ashx?t=headpic&photoId=
     */

    private String UserId;
    private String UserCn;
    private String Sex;
    private String CorpName;
    private String DeptName;
    private String PosName;
    private String OfficePhone;
    private String MobilePhone;
    private String EMail;
    private String WXNo;
    private String QQNo;
    private String PhotoUrl;
    private String HideMyInfo;
    private String FriendChat;
    private String OpenId;
    private int IsMyContact;
    private int IsMyFriend;

    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }

    public String getHideMyInfo() {
        return HideMyInfo;
    }

    public void setHideMyInfo(String hideMyInfo) {
        HideMyInfo = hideMyInfo;
    }

    public String getFriendChat() {
        return FriendChat;
    }

    public void setFriendChat(String friendChat) {
        FriendChat = friendChat;
    }

    public int getIsMyContact() {
        return IsMyContact;
    }

    public void setIsMyContact(int isMyContact) {
        IsMyContact = isMyContact;
    }

    public int getIsMyFriend() {
        return IsMyFriend;
    }

    public void setIsMyFriend(int isMyFriend) {
        IsMyFriend = isMyFriend;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserCn() {
        return UserCn;
    }

    public void setUserCn(String UserCn) {
        this.UserCn = UserCn;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getCorpName() {
        return CorpName;
    }

    public void setCorpName(String CorpName) {
        this.CorpName = CorpName;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    public String getPosName() {
        return PosName;
    }

    public void setPosName(String PosName) {
        this.PosName = PosName;
    }

    public String getOfficePhone() {
        return OfficePhone;
    }

    public void setOfficePhone(String OfficePhone) {
        this.OfficePhone = OfficePhone;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String MobilePhone) {
        this.MobilePhone = MobilePhone;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getWXNo() {
        return WXNo;
    }

    public void setWXNo(String WXNo) {
        this.WXNo = WXNo;
    }

    public String getQQNo() {
        return QQNo;
    }

    public void setQQNo(String QQNo) {
        this.QQNo = QQNo;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }
}
