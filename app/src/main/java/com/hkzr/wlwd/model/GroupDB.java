package com.hkzr.wlwd.model;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/6/24.
 */

public class GroupDB extends DataSupport {
    private String GroupID;
    private String GroupName;
    private String Photo;

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
