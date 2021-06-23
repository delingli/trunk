package com.hkzr.wlwd.model;

import java.util.List;

/**
 * Created by admin on 2017/6/24.
 */

public class SignInfoBean {

    /**
     * AdminCn :
     * AdminID :
     * Remark :
     * UnitCode : KQDY-300000-02
     * UnitID : 80915a7b-73cb-4af0-880d-907bbd5fd0a6
     * UnitName : 总经理办公室
     * list_loc : []
     * list_wifi : []
     */

    private String AdminCn;
    private String AdminID;
    private String Remark;
    private String UnitCode;
    private String UnitID;
    private String UnitName;
    private List<LocationInfo> list_loc;
    private List<WifiInfo> list_wifi;

    public String getAdminCn() {
        return AdminCn;
    }

    public void setAdminCn(String AdminCn) {
        this.AdminCn = AdminCn;
    }

    public String getAdminID() {
        return AdminID;
    }

    public void setAdminID(String AdminID) {
        this.AdminID = AdminID;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getUnitCode() {
        return UnitCode;
    }

    public void setUnitCode(String UnitCode) {
        this.UnitCode = UnitCode;
    }

    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String UnitID) {
        this.UnitID = UnitID;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public List<LocationInfo> getList_loc() {
        return list_loc;
    }

    public void setList_loc(List<LocationInfo> list_loc) {
        this.list_loc = list_loc;
    }

    public List<WifiInfo> getList_wifi() {
        return list_wifi;
    }

    public void setList_wifi(List<WifiInfo> list_wifi) {
        this.list_wifi = list_wifi;
    }

    public class LocationInfo {
        private String AutoID;
        private String LocName;
        private String LocAddr;
        private String Coords;

        public String getAutoID() {
            return AutoID;
        }

        public void setAutoID(String autoID) {
            AutoID = autoID;
        }

        public String getLocName() {
            return LocName;
        }

        public void setLocName(String locName) {
            LocName = locName;
        }

        public String getLocAddr() {
            return LocAddr;
        }

        public void setLocAddr(String locAddr) {
            LocAddr = locAddr;
        }

        public String getCoords() {
            return Coords;
        }

        public void setCoords(String coords) {
            Coords = coords;
        }
    }

    public class WifiInfo {
        private String AutoID;
        private String WFID;
        private String WFName;

        public String getAutoID() {
            return AutoID;
        }

        public void setAutoID(String autoID) {
            AutoID = autoID;
        }

        public String getWFID() {
            return WFID;
        }

        public void setWFID(String WFID) {
            this.WFID = WFID;
        }

        public String getWFName() {
            return WFName;
        }

        public void setWFName(String WFName) {
            this.WFName = WFName;
        }
    }
}
