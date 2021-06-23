package com.hkzr.wlwd.model;

import java.util.List;

/**
 * Created by admin on 2017/5/17.
 */

public class ContactEntity {


    private List<OrglistBean> orglist;
    private List<LinklistBean> linklist;

    public List<OrglistBean> getOrglist() {
        return orglist;
    }

    public void setOrglist(List<OrglistBean> orglist) {
        this.orglist = orglist;
    }

    public List<LinklistBean> getLinklist() {
        return linklist;
    }

    public void setLinklist(List<LinklistBean> linklist) {
        this.linklist = linklist;
    }

    public static class OrglistBean {
        /**
         * SubList : null
         * OrgId : 63D506A3-F039-4C49-9EA5-EEE6B24777A4
         * OrgName : 伍联维度
         * SubName : null
         * OrgType : 1
         */

        private String OrgId;
        private String OrgName;
        private String SubName;
        private String OrgType;

      
        public String getOrgId() {
            return OrgId;
        }

        public void setOrgId(String OrgId) {
            this.OrgId = OrgId;
        }

        public String getOrgName() {
            return OrgName;
        }

        public void setOrgName(String OrgName) {
            this.OrgName = OrgName;
        }

        public String getSubName() {
            return SubName;
        }

        public void setSubName(String SubName) {
            this.SubName = SubName;
        }

        public String getOrgType() {
            return OrgType;
        }

        public void setOrgType(String OrgType) {
            this.OrgType = OrgType;
        }
    }

    public static class LinklistBean {
        /**
         * UserId : 5521806d-aa5b-43ad-9b5b-d7a65eee20e5
         * UserCn : 程子龙
         * Sex : null
         * CorpName : 伍联维度
         * DeptName : 项目部
         * PosName : 项目总监
         * MobilePhone : ******
         * PhotoUrl : http://oa.5lsoft.com/mobile/mobile.ashx?t=headpic&photoId=dbd8fcdd-3a2a-47bf-a2b1-373b7789b92f
         */

        private String UserId;
        private String UserCn;
        private String Sex;
        private String CorpName;
        private String DeptName;
        private String PosName;
        private String MobilePhone;
        private String PhotoUrl;

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
    }
}
