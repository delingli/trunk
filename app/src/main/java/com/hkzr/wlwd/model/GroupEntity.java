package com.hkzr.wlwd.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.hkzr.wlwd.ui.app.User;

import java.util.List;

/**
 * Created by admin on 2017/6/12.
 */

public class GroupEntity {

    /**
     * GroupID : e4c806c6-2f65-41ce-a971-30618bf3b8de
     * GroupName : 测试群组11
     * Labels : 测试,技术
     * OwnerCn : sysadmin
     * Photo : http://dev.5lsoft.com/TJSJ/mobile/mobile.ashx?t=groupimg&tokenid={tokenid}&photoId=
     */

    private String GroupID;
    private String GroupName;
    private String Labels;
    private String OwnerCn;
    private String OwnerID;
    private String Photo;
    private String UserList;
    private List<User> users;


    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }


    public List<User> getUserList() {
        if (TextUtils.isEmpty(UserList) && users == null) {
            return null;
        }
        if (users == null) {
            users = JSON.parseArray(UserList, User.class);
        }
        return users;
    }

    public void setUserList(List<User> users) {
        this.users = users;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    private String Remark;

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

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }


}
