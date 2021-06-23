package com.hkzr.wlwd.model;

import java.util.List;

/**
 * Created by admin on 2017/6/23.
 */

public class SignTodayListBean {

    /**
     * Date : 2017-06-26 10:07:20
     * InOut : 上班
     * IsAdmin : true
     * UnitName : 质量技术部
     * list : [{"Addr":"北京市-朝阳区-清河营东路","AutoID":"2a6878b1-b3c0-41a2-9775-b0374f842023","CheckTime":"/Date(1498442611333)/","Coords":"40.056314,116.444927","EmpID":"56a9f268-9ec0-40b2-b696-206eceddd634","EmpName":"测试员1","InOut":"上班","LocID":"877edd47-74ca-4b96-aa98-c944e313ff5c","Result":"正常","SignType":"3"},{"Addr":"北京市-朝阳区-清河营东路","AutoID":"e064d7bd-96d1-4e32-848f-6cbdced06165","CheckTime":"/Date(1498442619273)/","Coords":"40.056313,116.444926","EmpID":"56a9f268-9ec0-40b2-b696-206eceddd634","EmpName":"测试员1","InOut":"下班","LocID":"877edd47-74ca-4b96-aa98-c944e313ff5c","Result":"正常","SignType":"3"},{"Addr":"北京市-朝阳区-清河营东路","AutoID":"f7f1c34d-bf57-4472-b038-48866a7cb4cb","CheckTime":"/Date(1498442622300)/","Coords":"40.056313,116.444926","EmpID":"56a9f268-9ec0-40b2-b696-206eceddd634","EmpName":"测试员1","InOut":"上班","LocID":"877edd47-74ca-4b96-aa98-c944e313ff5c","Result":"正常","SignType":"3"},{"Addr":"北京市-朝阳区-清河营东路","AutoID":"ed9c38f5-a741-452a-b84e-86e05d3dfc0d","CheckTime":"/Date(1498442814367)/","Coords":"40.056318,116.444912","EmpID":"56a9f268-9ec0-40b2-b696-206eceddd634","EmpName":"测试员1","InOut":"下班","LocID":"877edd47-74ca-4b96-aa98-c944e313ff5c","Result":"正常","SignType":"3"}]
     */

    private String Date;
    private String InOut;
    private boolean IsAdmin;
    private String UnitName;
    private List<ListBean> list;

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getInOut() {
        return InOut;
    }

    public void setInOut(String InOut) {
        this.InOut = InOut;
    }

    public boolean isIsAdmin() {
        return IsAdmin;
    }

    public void setIsAdmin(boolean IsAdmin) {
        this.IsAdmin = IsAdmin;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * Addr : 北京市-朝阳区-清河营东路
         * AutoID : 2a6878b1-b3c0-41a2-9775-b0374f842023
         * CheckTime : /Date(1498442611333)/
         * Coords : 40.056314,116.444927
         * EmpID : 56a9f268-9ec0-40b2-b696-206eceddd634
         * EmpName : 测试员1
         * InOut : 上班
         * LocID : 877edd47-74ca-4b96-aa98-c944e313ff5c
         * Result : 正常
         * SignType : 3
         */

        private String Addr;
        private String AutoID;
        private String CheckTime;
        private String Coords;
        private String EmpID;
        private String EmpName;
        private String InOut;
        private String LocID;
        private String Result;
        private String SignType;

        public String getAddr() {
            return Addr;
        }

        public void setAddr(String Addr) {
            this.Addr = Addr;
        }

        public String getAutoID() {
            return AutoID;
        }

        public void setAutoID(String AutoID) {
            this.AutoID = AutoID;
        }

        public String getCheckTime() {
            return CheckTime;
        }

        public void setCheckTime(String CheckTime) {
            this.CheckTime = CheckTime;
        }

        public String getCoords() {
            return Coords;
        }

        public void setCoords(String Coords) {
            this.Coords = Coords;
        }

        public String getEmpID() {
            return EmpID;
        }

        public void setEmpID(String EmpID) {
            this.EmpID = EmpID;
        }

        public String getEmpName() {
            return EmpName;
        }

        public void setEmpName(String EmpName) {
            this.EmpName = EmpName;
        }

        public String getInOut() {
            return InOut;
        }

        public void setInOut(String InOut) {
            this.InOut = InOut;
        }

        public String getLocID() {
            return LocID;
        }

        public void setLocID(String LocID) {
            this.LocID = LocID;
        }

        public String getResult() {
            return Result;
        }

        public void setResult(String Result) {
            this.Result = Result;
        }

        public String getSignType() {
            return SignType;
        }

        public void setSignType(String SignType) {
            this.SignType = SignType;
        }
    }
}
