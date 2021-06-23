package com.hkzr.wlwd.model;

import java.util.List;

/**
 * Created by admin on 2017/6/24.
 */

public class GroupInfoEntity {


    /**
     * GroupID : 23759d65-dc29-4f58-90bc-1bea7e37a214
     * GroupName : 佛莫
     * GroupType : 1
     * JoinConfirm : 是
     * Joined : true
     * Labels : 八路军
     * OwnerCn : 测试员5
     * OwnerID : abdceb08-bc4b-4644-bc17-ed80df411e2e
     * Photo : http://dev.5lsoft.com/TJSJ/mobile/mobile.ashx?t=groupimg&photoId=e5271135-c794-4412-94cb-8d30a9560950&_rnd=636339032222570000
     * Remark : 看见了
     * UserList : [{"CorpName":"三建公司机关","DeptName":"测试项目部","MobilePhone":"","PhotoUrl":"?t=headpic&tokenid={tokenid}&photoId=","PosName":"项目管理员","Sex":"男","UserCn":"测试员4","UserId":"2d7aa7d3-5b5e-463f-bfe7-67c46b3496bc"},{"CorpName":"三建公司机关","DeptName":"测试项目部","MobilePhone":"","PhotoUrl":"?t=headpic&tokenid={tokenid}&photoId=18a24e08-e9e8-4645-8be0-04f56fe5b759","PosName":"项目管理员","Sex":"男","UserCn":"测试员5","UserId":"abdceb08-bc4b-4644-bc17-ed80df411e2e"}]
     * _AutoID : 23759d65-dc29-4f58-90bc-1bea7e37a214
     * _CreateTime : /Date(1498277622257)/
     * _IsDel : 0
     * _OrgCode : 000010005
     * _UpdateTime : /Date(1498277622257)/
     * _UserName : abdceb08-bc4b-4644-bc17-ed80df411e2e
     */

    private String GroupID;
    private String GroupName;
    private String GroupType;
    private String JoinConfirm;
    private boolean Joined;
    private String Labels;
    private String OwnerCn;
    private String OwnerID;
    private String Photo;
    private String Remark;
    private String _AutoID;
    private String _CreateTime;
    private int _IsDel;
    private String _OrgCode;
    private String _UpdateTime;
    private String _UserName;
    private List<UserListBean> UserList;

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String GroupID) {
        this.GroupID = GroupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getGroupType() {
        return GroupType;
    }

    public void setGroupType(String GroupType) {
        this.GroupType = GroupType;
    }

    public String getJoinConfirm() {
        return JoinConfirm;
    }

    public void setJoinConfirm(String JoinConfirm) {
        this.JoinConfirm = JoinConfirm;
    }

    public boolean isJoined() {
        return Joined;
    }

    public void setJoined(boolean Joined) {
        this.Joined = Joined;
    }

    public String getLabels() {
        return Labels;
    }

    public void setLabels(String Labels) {
        this.Labels = Labels;
    }

    public String getOwnerCn() {
        return OwnerCn;
    }

    public void setOwnerCn(String OwnerCn) {
        this.OwnerCn = OwnerCn;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String OwnerID) {
        this.OwnerID = OwnerID;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String get_AutoID() {
        return _AutoID;
    }

    public void set_AutoID(String _AutoID) {
        this._AutoID = _AutoID;
    }

    public String get_CreateTime() {
        return _CreateTime;
    }

    public void set_CreateTime(String _CreateTime) {
        this._CreateTime = _CreateTime;
    }

    public int get_IsDel() {
        return _IsDel;
    }

    public void set_IsDel(int _IsDel) {
        this._IsDel = _IsDel;
    }

    public String get_OrgCode() {
        return _OrgCode;
    }

    public void set_OrgCode(String _OrgCode) {
        this._OrgCode = _OrgCode;
    }

    public String get_UpdateTime() {
        return _UpdateTime;
    }

    public void set_UpdateTime(String _UpdateTime) {
        this._UpdateTime = _UpdateTime;
    }

    public String get_UserName() {
        return _UserName;
    }

    public void set_UserName(String _UserName) {
        this._UserName = _UserName;
    }

    public List<UserListBean> getUserList() {
        return UserList;
    }

    public void setUserList(List<UserListBean> UserList) {
        this.UserList = UserList;
    }

    public static class UserListBean {
        /**
         * CorpName : 三建公司机关
         * DeptName : 测试项目部
         * MobilePhone :
         * PhotoUrl : ?t=headpic&tokenid={tokenid}&photoId=
         * PosName : 项目管理员
         * Sex : 男
         * UserCn : 测试员4
         * UserId : 2d7aa7d3-5b5e-463f-bfe7-67c46b3496bc
         */

        private String CorpName;
        private String DeptName;
        private String MobilePhone;
        private String PhotoUrl;
        private String PosName;
        private String Sex;
        private String UserCn;
        private String UserId;

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

        public String getMobilePhone() {
            return MobilePhone;
        }

        public void setMobilePhone(String MobilePhone) {
            this.MobilePhone = MobilePhone;
        }

        public String getPhotoUrl() {
            return PhotoUrl;
        }

        public void setPhotoUrl(String PhotoUrl) {
            this.PhotoUrl = PhotoUrl;
        }

        public String getPosName() {
            return PosName;
        }

        public void setPosName(String PosName) {
            this.PosName = PosName;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String Sex) {
            this.Sex = Sex;
        }

        public String getUserCn() {
            return UserCn;
        }

        public void setUserCn(String UserCn) {
            this.UserCn = UserCn;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }
    }
}
