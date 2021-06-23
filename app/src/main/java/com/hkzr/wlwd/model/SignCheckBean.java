package com.hkzr.wlwd.model;

/**
 * Created by admin on 2017/6/23.
 */

public class SignCheckBean {

    /**
     * LocID :
     * LocName :
     * LocType :
     * OutCheck : true
     * Remark :
     * Success : true
     */

    private String LocID;
    private String LocName;
    private String LocType;
    private boolean OutCheck;
    private String Remark;
    private boolean Success;

    public String getLocID() {
        return LocID;
    }

    public void setLocID(String LocID) {
        this.LocID = LocID;
    }

    public String getLocName() {
        return LocName;
    }

    public void setLocName(String LocName) {
        this.LocName = LocName;
    }

    public String getLocType() {
        return LocType;
    }

    public void setLocType(String LocType) {
        this.LocType = LocType;
    }

    public boolean isOutCheck() {
        return OutCheck;
    }

    public void setOutCheck(boolean OutCheck) {
        this.OutCheck = OutCheck;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }
}
