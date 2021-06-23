package com.hkzr.wlwd.model;

/**
 * Created by admin on 2017/7/13.
 */

public class SystemMessageGroup {

    /**
     * fromUserId : ””
     * groupId : ””
     */

    private String fromUserId;
    private String groupId;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
